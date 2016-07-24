package com.sproutigy.commons.basement.config;

import com.sproutigy.commons.basement.config.sources.ConfigSource;

import java.util.Optional;

public interface Config {
    Config ROOT = new CompoundConfig(ConfigSource.ENVIRONMENT, ConfigSource.SYSTEM_PROPS);
    Config EMPTY = new CompoundConfig();

    <T> Optional<T> get(String key);

    <T> Optional<T> get(String key, Class<T> clazz);

    <T> T get(String key, T defaultValue);

    <T> void watch(String key, ConfigListener<T> listener);

    <T> void watchChanges(String key, ConfigListener<T> listener);

    <T> void watch(String key, T defaultValue, ConfigListener<T> listener);

    <T> Optional<T> getAndWatchChanges(String key, ConfigListener<T> listener);

    <T> T getAndWatchChanges(String key, T defaultValue, ConfigListener<T> listener);

    <T> void unwatch(String key, ConfigListener<T> listener);

    boolean isModifiable();

    void set(String key, Object value);

    void remove(String key);

    Config getParent();
}
