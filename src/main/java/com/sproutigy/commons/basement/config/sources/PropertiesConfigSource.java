package com.sproutigy.commons.basement.config.sources;

import java.util.Properties;

public class PropertiesConfigSource extends AbstractConfigSource {
    protected Properties properties;

    public PropertiesConfigSource(Properties properties, boolean modifiable) {
        super(modifiable);
        this.properties = properties;
    }

    @Override
    public Object load(String key) {
        return properties.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<String> keys() {
        return (Iterable) properties.keySet();
    }

    @Override
    public void store(String key, Object value) throws Exception {
        super.store(key, value);
        if (value != null) {
            properties.put(key, value);
        } else {
            properties.remove(key);
        }
    }
}
