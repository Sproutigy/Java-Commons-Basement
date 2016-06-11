package com.sproutigy.commons.basement;

import java.util.concurrent.TimeUnit;

public final class Sleep {
    private Sleep() {
    }

    public static void interruptable(long duration, TimeUnit timeUnit) throws InterruptedException {
        interruptable(timeUnit.toMillis(duration));
    }

    public static void interruptable(long millis) throws InterruptedException {
        interruptable(millis, 0);
    }

    public static void interruptable(long millis, int nanos) throws InterruptedException {
        Thread.sleep(millis, nanos);
    }

    public static void unchecked(long duration, TimeUnit timeUnit) {
        unchecked(timeUnit.toMillis(duration));
    }

    public static void unchecked(long millis) {
        unchecked(millis, 0);
    }

    public static void unchecked(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean untilInterrupted(long millis) {
        return untilInterrupted(millis, 0);
    }

    public static boolean untilInterrupted(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
            return false;
        } catch (InterruptedException ignore) {
            return true;
        }
    }

    public static boolean untilInterrupted(long duration, TimeUnit timeUnit) {
        return untilInterrupted(timeUnit.toMillis(duration));
    }
}
