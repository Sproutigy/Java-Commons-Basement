package com.sproutigy.commons.basement.config;

import com.sproutigy.commons.basement.PrimitiveValueConverter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public abstract class AbstractConfig implements Config {

    private final Config parent;
    private final Map<String, Collection<ConfigListener>> listeners = new ConcurrentHashMap<String, Collection<ConfigListener>>();

    public AbstractConfig() {
        this(null);
    }

    public AbstractConfig(Config parent) {
        this.parent = parent;
    }

    protected abstract Object load(String key);

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        Object value = load(key);
        if (value == null) {
            if (parent != null) {
                return parent.get(key);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.of((T)value);
        }
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        Optional opt = get(key);
        if (!opt.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(PrimitiveValueConverter.convert(key, clazz));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Optional<T> optValue = get(key);
        if (optValue.isPresent() && !optValue.toString().equals("Optional[Optional.empty]")) {
            return optValue.get();
        }
        return defaultValue;
    }

    @Override
    public <T> void watch(String key, ConfigListener<T> listener) {
        T value = get(key, (T) null);
        listener.onConfigValue(key, value);
        watchChanges(key, listener);
    }

    @Override
    public <T> void watchChanges(String key, ConfigListener<T> listener) {
        Collection<ConfigListener> listenersCollection = listeners.computeIfAbsent(key, new Function<String, CopyOnWriteArrayList<ConfigListener>>() {
            public CopyOnWriteArrayList<ConfigListener> apply(String s) {
                return new CopyOnWriteArrayList<>();
            }
        });
        listenersCollection.add(listener);
    }

    @Override
    public <T> void watch(String key, T defaultValue, ConfigListener<T> listener) {
        T value = getAndWatchChanges(key, defaultValue, listener);
        listener.onConfigValue(key, value);
    }

    @Override
    public <T> Optional<T> getAndWatchChanges(String key, ConfigListener<T> listener) {
        Optional<T> value = get(key);
        watchChanges(key, listener);
        return value;
    }

    @Override
    public <T> T getAndWatchChanges(String key, T defaultValue, ConfigListener<T> listener) {
        T value = get(key, defaultValue);
        watchChanges(key, listener);
        return value;
    }

    public <T> void unwatch(String key, ConfigListener<T> listener) {
        Collection<ConfigListener> listenersCollection = listeners.get(key);
        if (listenersCollection != null) {
            listenersCollection.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    protected void notifyWatchers(String key, Object value) {
        Collection<ConfigListener> listenersCollection = listeners.get(key);
        if (listenersCollection != null) {
            for (ConfigListener listener : listenersCollection) {
                try {
                    listener.onConfigValue(key, value);
                } catch (Throwable ignore) {
                }
            }
        }
    }

    @Override
    public void set(String key, Object value) {
        if (!isModifiable()) {
            throw new IllegalStateException("Configuration not modifiable");
        }

        try {
            store(key, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not store config value", e);
        }
        notifyWatchers(key, value);
    }

    @Override
    public void remove(String key) {
        set(key, null);
    }

    @Override
    public Config getParent() {
        return parent;
    }

    protected abstract void store(String key, Object value) throws Exception;
}
