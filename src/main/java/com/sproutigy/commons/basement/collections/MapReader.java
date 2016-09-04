package com.sproutigy.commons.basement.collections;

import java.util.*;

public class MapReader<K, V> implements Iterable<Map.Entry<K, V>> {

    private Map<K, V> map;

    public MapReader(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException("map == null");
        }
        this.map = map;
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Optional<V> get(K key) {
        V value = map.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    public V getOr(K key, V defaultValue) {
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public V getOrNull(K key) {
        return map.get(key);
    }

    public V getRequired(K key) {
        V value = map.get(key);
        if (value == null) {
            throw new IllegalStateException("Required key not found: " + key);
        }
        return value;
    }

    public Set<K> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public Collection<V> values() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
        return new Iterator<Map.Entry<K, V>>() {
            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override
            public Map.Entry<K, V> next() {
                final Map.Entry<K, V> entry = entryIterator.next();
                return Collect.unmodifiable(entry);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public Map<K, V> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MapReader) {
            return map.equals(((MapReader) obj).map);
        }
        return false;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
