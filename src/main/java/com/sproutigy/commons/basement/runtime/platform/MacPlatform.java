package com.sproutigy.commons.basement.runtime.platform;

import java.io.File;

public class MacPlatform extends Platform {

    public final static String TYPE = "MacOS";

    MacPlatform() {
    }

    public static boolean isCurrent() {
        return Platform.current() instanceof MacPlatform;
    }

    public static MacPlatform current() {
        if (!isCurrent()) throw new RuntimeException("Current runtime is not MacOS");
        return (MacPlatform) Platform.current();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public File getAppDataRoot() {
        return new File(System.getProperty("user.home") + File.separator + "Library" + File.separator
                + "Application Support");
    }
}
