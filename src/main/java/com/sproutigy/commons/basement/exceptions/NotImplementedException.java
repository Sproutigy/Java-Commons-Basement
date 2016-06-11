package com.sproutigy.commons.basement.exceptions;

public class NotImplementedException extends UnsupportedOperationException {
    public NotImplementedException() {
        this("Not implemented");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
