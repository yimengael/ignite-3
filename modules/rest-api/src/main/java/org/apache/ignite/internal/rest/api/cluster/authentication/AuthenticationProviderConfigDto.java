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

package org.apache.ignite.internal.rest.api.cluster.authentication;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ignite.security.AuthenticationProviderConfig;
import org.apache.ignite.security.AuthenticationType;

/**
 * REST representation of {@link AuthenticationProviderConfig}.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BasicAuthenticationProviderConfigDto.class, names = {"basic", "BASIC"})
})
@Schema(
        name = "AuthenticationProviderConfig",
        description = "Configuration for the authentication provider.",
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "basic", schema = BasicAuthenticationProviderConfigDto.class)
        },
        oneOf = {
                BasicAuthenticationProviderConfigDto.class
        },
        subTypes = {
                BasicAuthenticationProviderConfigDto.class
        }
)
public interface AuthenticationProviderConfigDto {

    /** Authentication type. */
    AuthenticationType type();

    /** Name of the authentication provider. */
    String name();
}
