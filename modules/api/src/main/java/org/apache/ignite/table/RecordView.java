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

package org.apache.ignite.table;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.ignite.tx.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Table view interface provides methods to access table records.
 *
 * @param <R> Mapped record type.
 * @see org.apache.ignite.table.mapper.Mapper
 */
public interface RecordView<R> {
    /**
     * Gets a record with the same key column values as the given one from a table.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return Record with all columns filled from the table.
     */
    R get(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Asynchronously gets a record with the same key column values as the given one from a table.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<R> getAsync(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Gets records from a table.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with key columns set. The records cannot be {@code null}.
     * @return Records with all columns filled from the table. The order of collection elements is
     *     guaranteed to be the same as the order of {@code keyRecs}. If a record does not exist, the
     *     element at the corresponding index of the resulting collection is null.
     */
    Collection<R> getAll(@Nullable Transaction tx, @NotNull Collection<R> keyRecs);

    /**
     * Asynchronously gets records from a table.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with the key columns set. The records cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Collection<R>> getAllAsync(@Nullable Transaction tx, @NotNull Collection<R> keyRecs);

    /**
     * Inserts a record into a table, if it does not exist, or replaces an existing one.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to insert into the table. The record cannot be {@code null}.
     */
    void upsert(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously inserts a record into a table, if it does not exist, or replaces the existing one.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to insert into the table. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Void> upsertAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Inserts records into a table, if they do not exist, or replaces the existing ones.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to insert into the table. The records cannot be {@code null}.
     */
    void upsertAll(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Asynchronously inserts a record into a table, if it does not exist, or replaces the existing one.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to insert into the table. The records cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Void> upsertAllAsync(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Inserts a record into a table, or replaces an existing record and returns the replaced record.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec A record to insert into the table. The record cannot be {@code null}.
     * @return Replaced record or {@code null} if it did not exist.
     */
    R getAndUpsert(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously inserts a record into a table, or replaces an existing record and returns the replaced record.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to insert into the table. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<R> getAndUpsertAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Inserts a record into a table if it does not exists.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to insert into the table. The record cannot be {@code null}.
     * @return {@code True} if successful, {@code false} otherwise.
     */
    boolean insert(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously inserts a record into a table if it does not exists.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to insert into the table. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Boolean> insertAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Inserts into a table records that do not exist, skips those that exist.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to insert into the table. The records cannot be {@code null}.
     * @return Skipped records.
     */
    Collection<R> insertAll(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Asynchronously inserts into a table records that do not exist, skips those that exist.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to insert into the table. The records cannot be {@code null}.
     * @return Future representing pending completion of the operation.
     */
    @NotNull CompletableFuture<Collection<R>> insertAllAsync(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Replaces an existing record associated with the same key column values as the given record.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to replace with. The record cannot be {@code null}.
     * @return {@code True} if a record was found and replaced successfully, {@code false} otherwise.
     */
    boolean replace(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Replaces an expected record in the table with the given new one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param oldRec Record to replace. The record cannot be {@code null}.
     * @param newRec Record to replace with. The record cannot be {@code null}.
     * @return {@code True} if a record was replaced successfully, {@code false} otherwise.
     */
    boolean replace(@Nullable Transaction tx, @NotNull R oldRec, @NotNull R newRec);

    /**
     * Asynchronously replaces an existing record associated with the same key columns values as the given record.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to replace with. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Boolean> replaceAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously replaces an existing record in the table with the given new one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param oldRec Record to replace. The record cannot be {@code null}.
     * @param newRec Record to replace with. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Boolean> replaceAsync(@Nullable Transaction tx, @NotNull R oldRec, @NotNull R newRec);

    /**
     * Gets an existing record associated with the same key columns values as the given one, then replaces it with the given one.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to replace with. The record cannot be {@code null}.
     * @return Replaced record or {@code null} if it did not exist.
     */
    R getAndReplace(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously gets an existing record associated with the same key column values as the given one,
     * then replaces it with the given one.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to replace with. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<R> getAndReplaceAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Deletes a record with the same key column values as the given one from a table.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return {@code True} if removed successfully, {@code false} otherwise.
     */
    boolean delete(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Asynchronously deletes a record with the same key column values as the given one from a table.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Boolean> deleteAsync(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Deletes the given record from a table.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to delete. The record cannot be {@code null}.
     * @return {@code True} if removed successfully, {@code false} otherwise.
     */
    boolean deleteExact(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Asynchronously deletes the given record from a table.
     *
     * @param tx  Transaction or {@code null} to auto-commit.
     * @param rec Record to delete. The record cannot be {@code null}.
     * @return Future tha represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Boolean> deleteExactAsync(@Nullable Transaction tx, @NotNull R rec);

    /**
     * Gets and deletes from a table a record with the same key column values as the given one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return Removed record or {@code null} if it dod not exist.
     */
    R getAndDelete(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Asynchronously gets and deletes from a table a record with the same key columns values as the given one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<R> getAndDeleteAsync(@Nullable Transaction tx, @NotNull R keyRec);

    /**
     * Removes from a table records with the same key column values as the given one.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with the key columns set. The records cannot be {@code null}.
     * @return Records with the key columns set that did not exist.
     */
    Collection<R> deleteAll(@Nullable Transaction tx, @NotNull Collection<R> keyRecs);

    /**
     * Asynchronously removes from a table records with the same key column values as the given one.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with the key columns set. The records cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Collection<R>> deleteAllAsync(@Nullable Transaction tx, @NotNull Collection<R> keyRecs);

    /**
     * Remove the given records from a table.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to delete. The records cannot be {@code null}.
     * @return Records that were not deleted.
     */
    Collection<R> deleteAllExact(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Asynchronously removes the given records from a table.
     *
     * @param tx   Transaction or {@code null} to auto-commit.
     * @param recs Records to delete. The records cannot be {@code null}.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull CompletableFuture<Collection<R>> deleteAllExactAsync(@Nullable Transaction tx, @NotNull Collection<R> recs);

    /**
     * Executes the InvokeProcessor code against a record with the same key column values as the given one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec Record with the key columns set. The record cannot be {@code null}.
     * @param proc   Invoke processor.
     * @param <T>    InvokeProcessor result type.
     * @return Results of the processing.
     */
    <T extends Serializable> T invoke(@Nullable Transaction tx, @NotNull R keyRec, InvokeProcessor<R, R, T> proc);

    /**
     * Asynchronously executes the InvokeProcessor code against a record with the same key columns values as the given one.
     *
     * @param tx     Transaction or {@code null} to auto-commit.
     * @param keyRec A record with the key columns set. The record cannot be {@code null}.
     * @param proc   Invoke processor.
     * @param <T>    InvokeProcessor result type.
     * @return Future that represents the pending completion of the operation.
     */
    @NotNull <T extends Serializable> CompletableFuture<T> invokeAsync(@Nullable Transaction tx, @NotNull R keyRec,
            InvokeProcessor<R, R, T> proc);

    /**
     * Executes the InvokeProcessor code against records with the same key column values as the given ones.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with the key columns set. The records cannot be {@code null}.
     * @param proc    Invoke processor.
     * @param <T>     InvokeProcessor result type.
     * @return Results of the processing.
     */
    <T extends Serializable> Map<R, T> invokeAll(@Nullable Transaction tx, @NotNull Collection<R> keyRecs, InvokeProcessor<R, R, T> proc);

    /**
     * Asynchronously executes the InvokeProcessor code against records with the same key columns values as the given ones.
     *
     * @param tx      Transaction or {@code null} to auto-commit.
     * @param keyRecs Records with the key columns set. The records cannot be {@code null}.
     * @param proc    Invoke processor.
     * @param <T>     InvokeProcessor result type.
     * @return Results of the processing.
     */
    @NotNull <T extends Serializable> CompletableFuture<Map<R, T>> invokeAllAsync(@Nullable Transaction tx, @NotNull Collection<R> keyRecs,
            InvokeProcessor<R, R, T> proc);
}
