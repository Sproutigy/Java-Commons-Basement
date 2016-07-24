package com.sproutigy.commons.basement.config.sources;

public interface ConfigSource {
    ConfigSource ENVIRONMENT = new MapConfigSource(System.getenv(), false);
    ConfigSource SYSTEM_PROPS = new PropertiesConfigSource(System.getProperties(), false);

    Object load(String key);

    boolean isModifiable();

    Iterable<String> keys();

    void store(String key, Object value) throws Exception;

    void refresh() throws Exception;

    void flush() throws Exception;
}
