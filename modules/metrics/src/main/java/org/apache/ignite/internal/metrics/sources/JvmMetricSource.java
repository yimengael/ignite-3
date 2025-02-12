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

package org.apache.ignite.internal.metrics.sources;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.function.Supplier;
import org.apache.ignite.internal.metrics.LongGauge;
import org.apache.ignite.internal.metrics.Metric;
import org.apache.ignite.internal.metrics.MetricSet;
import org.apache.ignite.internal.metrics.MetricSource;
import org.jetbrains.annotations.Nullable;

/**
 * Metric source, which provides JVM metrics like memory usage, gc stats etc.
 */
public class JvmMetricSource implements MetricSource {

    /** Source name. */
    private static final String SOURCE_NAME = "jvm";

    /** Timeout for memory usage stats cache. */
    private static final long MEMORY_USAGE_CACHE_TIMEOUT = 1000;

    /** JVM standard MXBean to provide information about memory usage. */
    private final MemoryMXBean memoryMxBean;

    /** True, if source is enabled, false otherwise. */
    private boolean enabled;

    /**
     * Constructor.
     *
     * @param memoryMxBean MXBean implementation to receive memory info.
     */
    JvmMetricSource(MemoryMXBean memoryMxBean) {
        this.memoryMxBean = memoryMxBean;
    }

    /**
     * Constructs new metric source with standard MemoryMXBean as metric provider.
     */
    public JvmMetricSource() {
        memoryMxBean = ManagementFactory.getMemoryMXBean();
    }

    /** {@inheritDoc} */
    @Override
    public String name() {
        return SOURCE_NAME;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized @Nullable MetricSet enable() {
        var metrics = new HashMap<String, Metric>();

        CachedMemoryUsage heapMemoryUsage = new CachedMemoryUsage(memoryMxBean::getHeapMemoryUsage, MEMORY_USAGE_CACHE_TIMEOUT);
        metrics.put("memory.heap.init",
                new LongGauge("memory.heap.init", "Initial amount of heap memory", () -> heapMemoryUsage.get().getInit()));
        metrics.put("memory.heap.used",
                new LongGauge("memory.heap.used",
                        "Current used amount of heap memory",
                        () -> heapMemoryUsage.get().getUsed()));
        metrics.put("memory.heap.committed",
                new LongGauge("memory.heap.committed",
                        "Committed amount of heap memory",
                        () -> heapMemoryUsage.get().getCommitted()));
        metrics.put("memory.heap.max",
                new LongGauge("memory.heap.max",
                        "Maximum amount of heap memory",
                        () -> heapMemoryUsage.get().getMax()));

        CachedMemoryUsage nonHeapMemoryUsage = new CachedMemoryUsage(memoryMxBean::getNonHeapMemoryUsage, MEMORY_USAGE_CACHE_TIMEOUT);
        metrics.put("memory.non-heap.init",
                new LongGauge("memory.non-heap.init",
                        "Initial amount of non-heap memory",
                        () -> nonHeapMemoryUsage.get().getInit()));
        metrics.put("memory.non-heap.used",
                new LongGauge("memory.non-heap.used",
                        "Used amount of non-heap memory",
                        () -> nonHeapMemoryUsage.get().getUsed()));
        metrics.put("memory.non-heap.committed",
                new LongGauge("memory.non-heap.committed",
                        "Committed amount of non-heap memory",
                        () -> nonHeapMemoryUsage.get().getCommitted()));
        metrics.put("memory.non-heap.max",
                new LongGauge("memory.non-heap.max",
                        "Maximum amount of non-heap memory",
                        () -> nonHeapMemoryUsage.get().getMax()));

        enabled = true;

        return new MetricSet(SOURCE_NAME, metrics);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void disable() {
        enabled = false;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean enabled() {
        return enabled;
    }

    /**
     * Simple wrapper for memoization memory usage stats.
     */
    private static class CachedMemoryUsage {
        /** Source of memory usage stats. */
        private final Supplier<MemoryUsage> source;

        /** Timeout of cache in ms. */
        private final long timeout;

        /** Last update time in ms. */
        private volatile long lastUpdateTime;

        /** Last received from source value. */
        private volatile MemoryUsage currentVal;

        /**
         * Constructor.
         *
         * @param source Source of memory usage data.
         * @param timeout Cache timeout in millis.
         */
        private CachedMemoryUsage(Supplier<MemoryUsage> source, long timeout) {
            this.source = source;
            this.timeout = timeout;

            update();
        }

        /**
         * Returns current cached value.
         *
         * @return Current cached value.
         */
        private MemoryUsage get() {
            if ((System.currentTimeMillis() - lastUpdateTime) > timeout) {
                update();
            }

            return currentVal;
        }

        /**
         * Update cache value and last update time.
         */
        private synchronized void update() {
            currentVal = source.get();

            lastUpdateTime = System.currentTimeMillis();
        }
    }
}
