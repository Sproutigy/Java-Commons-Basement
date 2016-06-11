package com.sproutigy.commons.basement.exceptions;

public final class Throwables {
    private Throwables() {
    }

    public static boolean isCausedBy(Class<? extends Throwable> check, Throwable e) {
        if (e.getClass().isAssignableFrom(check)) {
            return true;
        }
        if (e.getCause() != null) {
            return isCausedBy(check, e.getCause());
        }
        return false;
    }
}
