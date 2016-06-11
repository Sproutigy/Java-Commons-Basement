package com.sproutigy.commons.basement.interfaces;

public interface Handler<T> {
    void handle(T event) throws Exception;
}
