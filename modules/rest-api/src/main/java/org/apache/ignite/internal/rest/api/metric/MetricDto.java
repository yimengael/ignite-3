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

package org.apache.ignite.internal.rest.api.metric;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.Nullable;

/**
 * REST representation of Metric.
 */
@Schema(name = "Metric")
public class MetricDto {
    /** Metric name. */
    private final String name;

    /** Metric description. */
    private final String desc;

    /**
     * Constructor.
     *
     * @param name metric name
     * @param desc metric description
     */
    @JsonCreator
    public MetricDto(
            @JsonProperty("name") String name,
            @JsonProperty("desc") @Nullable String desc
    ) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * Returns the metric name.
     *
     * @return metric name
     */
    @JsonGetter("name")
    public String name() {
        return name;
    }

    /**
     * Returns the metric description.
     *
     * @return metric description
     */
    @JsonGetter("desc")
    public @Nullable String desc() {
        return desc;
    }
}
