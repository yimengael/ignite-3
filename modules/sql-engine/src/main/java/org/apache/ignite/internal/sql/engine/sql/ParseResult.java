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

package org.apache.ignite.internal.sql.engine.sql;

import java.util.List;
import org.apache.calcite.sql.SqlNode;

/**
 * Result of parsing SQL string.
 */
public abstract class ParseResult {

    private final int dynamicParamsCount;

    /**
     * Constructor.
     *
     * @param dynamicParamsCount the number of dynamic parameters.
     */
    ParseResult(int dynamicParamsCount) {
        if (dynamicParamsCount < 0) {
            throw new IllegalArgumentException("Number of dynamic parameters must be positive but got " + dynamicParamsCount);
        }
        this.dynamicParamsCount = dynamicParamsCount;
    }

    /** The number of dynamic parameters in this result. */
    public int dynamicParamsCount() {
        return dynamicParamsCount;
    }

    /** Returns a list of parsed statements. */
    public abstract List<SqlNode> statements();
}
