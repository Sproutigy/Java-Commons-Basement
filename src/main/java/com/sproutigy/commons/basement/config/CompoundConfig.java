package com.sproutigy.commons.basement.config;

import com.sproutigy.commons.basement.config.sources.ConfigSource;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompoundConfig extends AbstractConfig implements Iterable<ConfigSource> {

    private Config parent;
    private List<ConfigSource> sources;

    public CompoundConfig(ConfigSource... sources) {
        this(null, sources);
    }

    public CompoundConfig(Config parent, ConfigSource... sources) {
        this.parent = parent;
        this.sources = new CopyOnWriteArrayList<>(sources);
    }

    public void addHead(ConfigSource source) {
        sources.add(0, source);
    }

    public void addTail(ConfigSource source) {
        sources.add(source);
    }

    public void remove(ConfigSource source) {
        sources.remove(source);
    }

    @Override
    protected Object load(String key) {
        for (ConfigSource source : sources) {
            Object value = source.load(key);
            if (value != null) {
                return value;
            }
        }
        if (parent != null) {
            return parent.get(key, null);
        }

        return null;
    }

    @Override
    protected void store(String key, Object value) throws Exception {
        for (ConfigSource source : sources) {
            if (source.isModifiable()) {
                source.store(key, value);
                return;
            }
        }
        throw new IllegalStateException("Configuration not modifiable");
    }

    @Override
    public boolean isModifiable() {
        for (ConfigSource source : sources) {
            if (source.isModifiable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return sources.iterator();
    }
}
