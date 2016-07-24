package com.sproutigy.commons.basement.config;

import com.sproutigy.commons.basement.config.sources.ConfigSource;

public class SingleSourceConfig extends AbstractConfig {

    private ConfigSource source;

    public SingleSourceConfig(ConfigSource source) {
        this(null, source);
    }

    public SingleSourceConfig(Config parent, ConfigSource source) {
        super(parent);
        this.source = source;
    }

    public ConfigSource getSource() {
        return source;
    }

    @Override
    public boolean isModifiable() {
        return source.isModifiable();
    }

    @Override
    protected Object load(String key) {
        return source.load(key);
    }

    @Override
    protected void store(String key, Object value) throws Exception {
        source.store(key, value);
    }
}
