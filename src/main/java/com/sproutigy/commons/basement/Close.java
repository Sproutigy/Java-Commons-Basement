package com.sproutigy.commons.basement;

import org.slf4j.Logger;

import java.io.Closeable;

public class Close {

    private static final Logger log = NullableLoggerFactory.getLogger(Close.class);

    public static void unchecked(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void unchecked(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void silently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable ignore) {
        }
    }

    public static void silently(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Throwable ignore) {
        }
    }

    public static void loggable(Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable e) {
            NullableLoggerFactory.logError(log, "Error occurred while closing resource", e);
        }
    }

    public static void loggable(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Throwable e) {
            NullableLoggerFactory.logError(log, "Error occurred while closing resource", e);
        }
    }

    public static void loggableAndThrowable(Closeable closeable) throws Exception {
        try {
            closeable.close();
        } catch (Throwable e) {
            NullableLoggerFactory.logError(log, "Error occurred while closing resource", e);
            throw e;
        }
    }

    public static void loggableAndThrowable(AutoCloseable closeable) throws Exception {
        try {
            closeable.close();
        } catch (Throwable e) {
            NullableLoggerFactory.logError(log, "Error occurred while closing resource", e);
            throw e;
        }
    }
}
