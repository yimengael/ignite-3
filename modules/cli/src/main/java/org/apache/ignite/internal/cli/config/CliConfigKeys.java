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

package org.apache.ignite.internal.cli.config;

/** CLI config keys and constants. */
public enum CliConfigKeys {

    /** Default cluster or node URL property name. */
    CLUSTER_URL(Constants.CLUSTER_URL),

    /** Last connected cluster or node URL property name. */
    LAST_CONNECTED_URL(Constants.LAST_CONNECTED_URL),

    /** REST trust store path property name. */
    REST_TRUST_STORE_PATH(Constants.REST_TRUST_STORE_PATH),

    /** REST trust store password property name. */
    REST_TRUST_STORE_PASSWORD(Constants.REST_TRUST_STORE_PASSWORD),

    /** REST key store path property name. */
    REST_KEY_STORE_PATH(Constants.REST_KEY_STORE_PATH),

    /** REST key store password property name. */
    REST_KEY_STORE_PASSWORD(Constants.REST_KEY_STORE_PASSWORD),

    /** Default JDBC URL property name. */
    JDBC_URL(Constants.JDBC_URL),

    /** Basic authentication login. */
    BASIC_AUTHENTICATION_LOGIN(Constants.BASIC_AUTHENTICATION_LOGIN),

    /** Basic authentication password. */
    BASIC_AUTHENTICATION_PASSWORD(Constants.BASIC_AUTHENTICATION_PASSWORD);

    private final String value;

    public String value() {
        return value;
    }

    /** Constants for CLI config. */
    public static final class Constants {
        public static final String CLUSTER_URL = "ignite.cluster-endpoint-url";

        public static final String LAST_CONNECTED_URL = "ignite.last-connected-url";

        public static final String REST_TRUST_STORE_PATH = "ignite.rest.trust-store.path";

        public static final String REST_TRUST_STORE_PASSWORD = "ignite.rest.trust-store.password";

        public static final String REST_KEY_STORE_PATH = "ignite.rest.key-store.path";

        public static final String REST_KEY_STORE_PASSWORD = "ignite.rest.key-store.password";

        public static final String JDBC_URL = "ignite.jdbc-url";

        public static final String BASIC_AUTHENTICATION_LOGIN = "ignite.auth.basic.login";

        public static final String BASIC_AUTHENTICATION_PASSWORD = "ignite.auth.basic.password";
    }

    CliConfigKeys(String value) {
        this.value = value;
    }
}
