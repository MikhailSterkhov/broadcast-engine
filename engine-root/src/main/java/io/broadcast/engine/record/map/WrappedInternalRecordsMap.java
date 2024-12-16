package io.broadcast.engine.record.map;

import io.broadcast.engine.record.Record;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
class WrappedInternalRecordsMap<K, V> implements RecordsMap<K, V>, RecordsMap.MapBuilder<K, V> {

    private final Map<Record<K>, V> internal;

    @Override
    public Map<Record<K>, V> toMap() {
        return Collections.unmodifiableMap(internal);
    }

    @Override
    public Set<Record<K>> toRecordsSet() {
        return Collections.unmodifiableSet(internal.keySet());
    }

    @Override
    public List<Record<K>> toRecordsList() {
        return Collections.unmodifiableList(new ArrayList<>(internal.keySet()));
    }

    @Override
    public RecordsMap<K, V> merge(Map<Record<K>, V> other) {
        internal.putAll(other);
        return this;
    }

    @Override
    public RecordsMap<K, V> merge(RecordsMap<K, V> other) {
        internal.putAll(other.toMap());
        return this;
    }

    @Override
    public WrappedInternalRecordsMap<K, V> put(Record<K> key, V value) {
        internal.put(key, value);
        return this;
    }

    @Override
    public WrappedInternalRecordsMap<K, V> put(K key, V value) {
        return put(new Record<>(key), value);
    }

    @Override
    public WrappedInternalRecordsMap<K, V> put(K key, Function<K, V> valueFactory) {
        return put(key, valueFactory.apply(key));
    }

    @Override
    public MapBuilder<K, V> putStream(Iterable<K> keys, Function<K, V> valueFactory) {
        keys.forEach(key -> put(key, valueFactory.apply(key)));
        return this;
    }

    @Override
    public RecordsMap<K, V> build() {
        return this;
    }

    @Override
    public RecordsMap<K, V> delete(Record<K> key) {
        internal.remove(key);
        return this;
    }

    @Override
    public RecordsMap<K, V> delete(K key) {
        return delete(new Record<>(key));
    }

    @Override
    public V get(K key) {
        return internal.get(new Record<>(key));
    }

    @Override
    public V get(Record<K> key) {
        return internal.get(key);
    }

    @Override
    public Optional<V> getOptional(K key) {
        return Optional.ofNullable(get(key));
    }

    @Override
    public Optional<V> getOptional(Record<K> key) {
        return Optional.ofNullable(get(key));
    }
}
