package com.sproutigy.commons.basement.config;

public interface ConfigListener<T> {
    void onConfigValue(String key, T value);
}
