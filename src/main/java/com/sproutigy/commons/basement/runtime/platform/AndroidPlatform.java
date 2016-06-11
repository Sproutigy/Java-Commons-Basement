package com.sproutigy.commons.basement.runtime.platform;

public class AndroidPlatform extends UnixPlatform {

    public final static String TYPE = "Android";

    AndroidPlatform() {
    }

    public static boolean isCurrent() {
        return Platform.current() instanceof AndroidPlatform;
    }

    public static AndroidPlatform current() {
        if (!isCurrent()) throw new RuntimeException("Current runtime is not Android");
        return (AndroidPlatform) Platform.current();
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
