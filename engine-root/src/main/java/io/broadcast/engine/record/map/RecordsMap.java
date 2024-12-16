package io.broadcast.engine.record.map;

import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface RecordsMap<K, V> {

    Map<Record<K>, V> toMap();

    Set<Record<K>> toRecordsSet();

    List<Record<K>> toRecordsList();

    RecordsMap<K, V> merge(Map<Record<K>, V> other);

    RecordsMap<K, V> merge(RecordsMap<K, V> other);

    RecordsMap<K, V> put(Record<K> key, V value);

    RecordsMap<K, V> put(K key, V value);

    RecordsMap<K, V> put(K key, Function<K, V> valueFactory);

    RecordsMap<K, V> delete(Record<K> key);

    RecordsMap<K, V> delete(K key);

    V get(K key);

    V get(Record<K> key);

    Optional<V> getOptional(K key);

    Optional<V> getOptional(Record<K> key);

    interface MapBuilder<K, V> {

        MapBuilder<K, V> put(Record<K> key, V value);

        MapBuilder<K, V> put(K key, V value);

        MapBuilder<K, V> put(K key, Function<K, V> valueFactory);

        MapBuilder<K, V> putStream(Iterable<K> keys, Function<K, V> valueFactory);

        RecordsMap<K, V> build();
    }

    @Contract(" -> new")
    static <K, V> @NotNull MapBuilder<K, V> builderHashMap() {
        return new WrappedInternalRecordsMap<>(new HashMap<>());
    }

    @Contract(" -> new")
    static <K, V> @NotNull MapBuilder<K, V> builderConcurrentHashMap() {
        return new WrappedInternalRecordsMap<>(new ConcurrentHashMap<>());
    }

    @Contract(" -> new")
    static <K, V> @NotNull RecordsMap<K, V> newHashMap() {
        return new WrappedInternalRecordsMap<>(new HashMap<>());
    }

    @Contract(" -> new")
    static <K, V> @NotNull RecordsMap<K, V> newConcurrentHashMap() {
        return new WrappedInternalRecordsMap<>(new ConcurrentHashMap<>());
    }
}
