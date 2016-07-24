package com.sproutigy.commons.basement.config.sources;

import java.util.Map;

public class MapConfigSource extends AbstractConfigSource {
    protected Map map;

    public MapConfigSource(Map map, boolean modifiable) {
        super(modifiable);
        this.map = map;
    }

    @Override
    public Object load(String key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<String> keys() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void store(String key, Object value) throws Exception {
        super.store(key, value);
        if (value != null) {
            map.put(key, value);
        } else {
            map.remove(key);
        }
    }
}
