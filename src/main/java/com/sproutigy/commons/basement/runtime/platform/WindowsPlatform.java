package com.sproutigy.commons.basement.runtime.platform;

import java.io.File;
import java.io.IOException;

public class WindowsPlatform extends Platform {

    public final static String TYPE = "Windows";

    WindowsPlatform() {
    }

    public static boolean isCurrent() {
        return Platform.current() instanceof WindowsPlatform;
    }

    public static WindowsPlatform current() {
        if (!isCurrent()) throw new RuntimeException("Current runtime is not Windows");
        return (WindowsPlatform) Platform.current();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public File getUserHome() {
        File result = super.getUserHome();

        String homeDrive = System.getenv("HOMEDRIVE");
        String homePath = System.getenv("HOMEPATH");
        if (homeDrive != null && homePath != null) {
            File homeDir = new File(homeDrive + homePath);
            if (homeDir.isDirectory() && homeDir.canWrite()) {
                result = homeDir;
            }
        } else {
            String userProfile = System.getenv("USERPROFILE");
            if (userProfile != null) {
                File userProfileDir = new File(userProfile);
                if (userProfileDir.isDirectory() && userProfileDir.canWrite()) {
                    result = userProfileDir;
                }
            }
        }

        return result;
    }

    @Override
    public File getAppDataRoot() {
        File result = new File(getUserHome(), "Application Data");

        String appData = System.getenv("APPDATA");
        if (appData != null) {
            File appDataDir = new File(appData);
            if (appDataDir.isDirectory() && appDataDir.canWrite()) {
                result = appDataDir;
            }
        }

        return result;
    }

    @Override
    public boolean browse(String uri) {
        boolean ok = super.browse(uri);
        if (ok) return true;
        return cmdStart(uri);
    }

    @Override
    public boolean open(File file) {
        boolean ok = false;
        ok = super.open(file);
        if (ok) return true;
        try {
            return cmdStart(file.getCanonicalPath());
        } catch (IOException e) {
            return false;
        }
    }

    private boolean cmdStart(String uri) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start " + uri);
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}
