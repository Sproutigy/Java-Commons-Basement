package com.sproutigy.commons.basement.collections;

import java.util.*;

public abstract class MapBuilder<K, V> {

    protected LinkedList<Map.Entry<K, V>> entries = new LinkedList<>();

    public static <K, V> HashMapBuilder<K, V> ofHashMap() {
        return new HashMapBuilder<>();
    }

    public static <K, V> LinkedHashMapBuilder<K, V> ofLinkedHashMap() {
        return new LinkedHashMapBuilder<>();
    }

    public static <K, V> TreeMapBuilder<K, V> ofTreeMap() {
        return new TreeMapBuilder<>();
    }

    public static <K, V> TreeMapBuilder<K, V> ofTreeMap(Comparator<? super K> comparator) {
        return new TreeMapBuilder<>(comparator);
    }

    public MapBuilder<K, V> put(K key, V value) {
        return put(new EntryImpl<>(key, value));
    }

    public MapBuilder<K, V> put(Map.Entry<K, V> entry) {
        entries.add(entry);
        return this;
    }

    public MapBuilder<K, V> putAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry);
        }
        return this;
    }

    public void clear() {
        entries.clear();
    }

    public int count() {
        return entries.size();
    }

    protected Map<K, V> putEntriesTo(Map<K, V> map) {
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public abstract Map<K, V> build();

    public Map<K, V> buildImmutable() {
        return Collections.unmodifiableMap(build());
    }

    public static abstract class Specific<K, V, T extends Map<K, V>> extends MapBuilder<K, V> {
        @Override
        public Specific<K, V, T> put(K key, V value) {
            super.put(key, value);
            return this;
        }

        @Override
        public Specific<K, V, T> put(Map.Entry<K, V> entry) {
            super.put(entry);
            return this;
        }

        @Override
        public Specific<K, V, T> putAll(Map<K, V> map) {
            super.putAll(map);
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected T putEntriesTo(Map<K, V> map) {
            return (T) super.putEntriesTo(map);
        }

        @Override
        public abstract T build();
    }

    protected static class EntryImpl<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public EntryImpl(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    public static class HashMapBuilder<K, V> extends Specific<K, V, HashMap<K, V>> {
        @Override
        public HashMap<K, V> build() {
            return putEntriesTo(new HashMap<K, V>(count()));
        }
    }

    public static class LinkedHashMapBuilder<K, V> extends Specific<K, V, LinkedHashMap<K, V>> {
        @Override
        public LinkedHashMap<K, V> build() {
            return putEntriesTo(new LinkedHashMap<K, V>(count()));
        }
    }

    public static class TreeMapBuilder<K, V> extends Specific<K, V, TreeMap<K, V>> {
        private Comparator<? super K> comparator = null;

        public TreeMapBuilder() {
        }

        public TreeMapBuilder(Comparator<? super K> comparator) {
            this.comparator = comparator;
        }

        @Override
        public TreeMap<K, V> build() {
            return putEntriesTo(new TreeMap<K, V>(comparator));
        }
    }
}
