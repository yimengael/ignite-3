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

package org.apache.ignite.internal.cluster.management.network.messages;

import java.util.Set;
import org.apache.ignite.internal.cluster.management.network.auth.Authentication;
import org.apache.ignite.network.NetworkMessage;
import org.apache.ignite.network.annotations.Transferable;

/**
 * Message for initializing the Cluster Management Group.
 */
@Transferable(CmgMessageGroup.CMG_INIT)
public interface CmgInitMessage extends NetworkMessage {
    /**
     * Consistent IDs of nodes that host the CMG.
     */
    Set<String> cmgNodes();

    /**
     * Consistent IDs of nodes that host the Meta Storage.
     */
    Set<String> metaStorageNodes();

    /**
     * Name of the cluster that will be a part of the generated cluster tag.
     */
    String clusterName();

    /**
     * REST authentication configuration that should be applied after init.
     */
    Authentication restAuthToApply();
}
