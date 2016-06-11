package com.sproutigy.commons.basement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NullableLoggerFactory {

    private NullableLoggerFactory() {
    }

    public static Logger getLogger(Class clazz) {
        try {
            Class.forName("org.slf4j.Logger");
            return LoggerFactory.getLogger(clazz);
        } catch (ClassNotFoundException e) {
        }

        return null;
    }

    public static Logger getLogger(String name) {
        try {
            Class.forName("org.slf4j.Logger");
            return LoggerFactory.getLogger(name);
        } catch (ClassNotFoundException e) {
        }

        return null;
    }

    public static void logError(Logger logger, Throwable throwable) {
        logError(logger, throwable.getMessage(), throwable);
    }

    public static void logError(Logger logger, String message, Throwable throwable) {
        if (logger != null && logger.isErrorEnabled()) {
            logger.error(message, throwable);
        } else {
            System.err.println(message);
            throwable.printStackTrace();
        }
    }
}

