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

package org.apache.ignite.internal.security.authentication;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a request to authenticate using a username and a password.
 */
public class UsernamePasswordRequest implements AuthenticationRequest<String, String> {

    private final String login;

    private final String password;

    public UsernamePasswordRequest() {
        login = null;
        password = null;
    }

    public UsernamePasswordRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Nullable
    @Override
    public String getIdentity() {
        return login;
    }

    @Nullable
    @Override
    public String getSecret() {
        return password;
    }
}
