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

import org.apache.ignite.internal.security.exception.AuthenticationException;

/**
 * General interface for all authenticators.
 */
public interface Authenticator {

    /**
     * Authenticates a user with the given request.
     * If a successful authentication is returned, the object must be an instance of {@link UserDetails}.
     */
    UserDetails authenticate(AuthenticationRequest<?, ?> authenticationRequest) throws AuthenticationException;
}
