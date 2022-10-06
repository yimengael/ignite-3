/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "ignite/protocol/utils.h"

#include "node_connection.h"

namespace ignite::detail {

NodeConnection::NodeConnection(
    uint64_t id, std::shared_ptr<network::AsyncClientPool> pool, std::shared_ptr<IgniteLogger> logger)
    : m_id(id)
    , m_pool(std::move(pool))
    , m_logger(std::move(logger)) {
}

NodeConnection::~NodeConnection() {
    for (auto &handler : m_requestHandlers) {
        auto handlingRes = result_of_operation<void>([&]() {
            auto res = handler.second->setError(ignite_error("Connection closed before response was received"));
            if (res.has_error())
                m_logger->logError(
                    "Uncaught user callback exception while handling operation error: " + res.error().what_str());
        });
        if (handlingRes.has_error())
            m_logger->logError("Uncaught user callback exception: " + handlingRes.error().what_str());
    }
}

bool NodeConnection::handshake() {
    static constexpr int8_t CLIENT_TYPE = 2;

    std::vector<std::byte> message;
    {
        protocol::buffer_adapter buffer(message);
        buffer.write_raw(bytes_view(protocol::MAGIC_BYTES.data(), protocol::MAGIC_BYTES.size()));

        protocol::write_message_to_buffer(buffer, [&context = m_protocolContext](protocol::writer &writer) {
            auto ver = context.getVersion();

            writer.write(ver.getMajor());
            writer.write(ver.getMinor());
            writer.write(ver.getPatch());

            writer.write(CLIENT_TYPE);

            // Features.
            writer.write_binary_empty();

            // Extensions.
            writer.write_map_empty();
        });
    }

    return m_pool->send(m_id, std::move(message));
}

void NodeConnection::processMessage(bytes_view msg) {
    protocol::reader reader(msg);
    auto responseType = reader.read_int32();
    if (MessageType(responseType) != MessageType::RESPONSE) {
        m_logger->logWarning("Unsupported message type: " + std::to_string(responseType));
        return;
    }

    auto reqId = reader.read_int64();
    auto handler = getAndRemoveHandler(reqId);

    if (!handler) {
        m_logger->logError("Missing handler for request with id=" + std::to_string(reqId));
        return;
    }

    auto err = protocol::read_error(reader);
    if (err) {
        m_logger->logError("Error: " + err->what_str());
        auto res = handler->setError(std::move(err.value()));
        if (res.has_error())
            m_logger->logError(
                "Uncaught user callback exception while handling operation error: " + res.error().what_str());
        return;
    }

    auto handlingRes = handler->handle(reader);
    if (handlingRes.has_error())
        m_logger->logError("Uncaught user callback exception: " + handlingRes.error().what_str());
}

ignite_result<void> NodeConnection::processHandshakeRsp(bytes_view msg) {
    m_logger->logDebug("Got handshake response");

    protocol::reader reader(msg);

    auto verMajor = reader.read_int16();
    auto verMinor = reader.read_int16();
    auto verPatch = reader.read_int16();

    ProtocolVersion ver(verMajor, verMinor, verPatch);
    m_logger->logDebug("Server-side protocol version: " + ver.toString());

    // We now only support a single version
    if (ver != ProtocolContext::CURRENT_VERSION)
        return {ignite_error("Unsupported server version: " + ver.toString())};

    auto err = protocol::read_error(reader);
    if (err)
        return {ignite_error(err.value())};

    (void)reader.read_int64(); // TODO: IGNITE-17606 Implement heartbeats
    (void)reader.read_string_nullable(); // Cluster node ID. Needed for partition-aware compute.
    (void)reader.read_string_nullable(); // Cluster node name. Needed for partition-aware compute.

    reader.skip(); // Features.
    reader.skip(); // Extensions.

    m_protocolContext.setVersion(ver);
    m_handshakeComplete = true;

    return {};
}

std::shared_ptr<ResponseHandler> NodeConnection::getAndRemoveHandler(int64_t id) {
    std::lock_guard<std::mutex> lock(m_requestHandlersMutex);

    auto it = m_requestHandlers.find(id);
    if (it == m_requestHandlers.end())
        return {};

    auto res = std::move(it->second);
    m_requestHandlers.erase(it);

    return res;
}

} // namespace ignite::detail
