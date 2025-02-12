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

package org.apache.ignite.internal.metastorage;

import java.util.Collection;
import java.util.List;
import org.apache.ignite.internal.tostring.IgniteToStringInclude;
import org.apache.ignite.internal.tostring.S;

/**
 * Watch event contains all entry updates done under one revision. Each particular entry update in this revision is represented by {@link
 * EntryEvent} entity.
 */
public class WatchEvent {
    /** Events about each entry update in the revision. */
    @IgniteToStringInclude
    private final List<EntryEvent> entryEvts;

    private final long revision;

    /**
     * Constructs an watch event with given entry events collection.
     *
     * @param entryEvts Events for entries corresponding to an update under one revision.
     * @param revision Revision of the updated entries.
     */
    public WatchEvent(Collection<EntryEvent> entryEvts, long revision) {
        this.entryEvts = List.copyOf(entryEvts);
        this.revision = revision;
    }

    /**
     * Constructs watch event with single entry update.
     *
     * @param entryEvt Entry event.
     */
    public WatchEvent(EntryEvent entryEvt) {
        this(List.of(entryEvt), entryEvt.newEntry().revision());
    }

    /**
     * Returns {@code true} if watch event contains only one entry event.
     *
     * @return {@code True} if watch event contains only one entry event.
     */
    public boolean single() {
        return entryEvts.size() == 1;
    }

    /**
     * Returns collection of entry entry event done under one revision.
     *
     * @return Collection of entry entry event done under one revision.
     */
    public Collection<EntryEvent> entryEvents() {
        return entryEvts;
    }

    /**
     * Returns entry event. It is useful method in case when we know that only one event was modified.
     *
     * @return Entry event.
     */
    public EntryEvent entryEvent() {
        assert single();

        return entryEvts.get(0);
    }

    /**
     * Returns the revision of the modified entries.
     *
     * @return Event revision.
     */
    public long revision() {
        return revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WatchEvent event = (WatchEvent) o;

        if (revision != event.revision) {
            return false;
        }
        return entryEvts.equals(event.entryEvts);
    }

    @Override
    public int hashCode() {
        int result = entryEvts.hashCode();
        result = 31 * result + (int) (revision ^ (revision >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return S.toString(this);
    }
}
