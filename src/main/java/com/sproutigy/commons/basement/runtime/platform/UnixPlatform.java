package com.sproutigy.commons.basement.runtime.platform;

import java.io.File;

public class UnixPlatform extends Platform {

    public final static String TYPE = "UNIX/Linux";

    UnixPlatform() {
    }

    public static boolean isCurrent() {
        return Platform.current() instanceof UnixPlatform;
    }

    public static UnixPlatform current() {
        if (!isCurrent()) throw new RuntimeException("Current runtime is not UNIX");
        return (UnixPlatform) Platform.current();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public File getUserHome() {
        File home = getUserHome();
        if (home == null || !home.exists()) {
            home = new File("~");
        }
        return home;
    }

    @Override
    public File getAppDataRoot() {
        return new File("/usr/share");
    }
}
