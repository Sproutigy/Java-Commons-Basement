package com.sproutigy.commons.basement.runtime.platform;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public abstract class Platform {

    private static Platform current;

    Platform() {
    }

    public static Platform current() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (current == null) {
            if (osName.contains("win")) {
                current = new WindowsPlatform();
            } else if (osName.contains("mac") || osName.contains("darwin")) {
                current = new MacPlatform();
            } else if (osName.contains("linux") || osName.contains("unix")) {
                current = new UnixPlatform();
            } else {
                String javaRuntime = System.getProperty("java.runtime.name");
                if (javaRuntime != null) {
                    if (javaRuntime.contains("Android")) {
                        current = new AndroidPlatform();
                    }
                }
            }
            if (current == null) {
                current = new UnknownPlatform();
            }
        }

        return current;
    }

    public final String getFullName() {
        return System.getProperty("os.name");
    }

    public String getType() {
        return getFullName();
    }

    public File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public File getAppDataRoot() {
        return new File(System.getenv("APPDATA"));
    }

    public File getAppDataDir(String appName) {
        return new File(getAppDataRoot(), appName);
    }

    public boolean browse(String uri) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
                return true;
            } catch (IOException e) {
            } catch (URISyntaxException e) {
            }
        }

        return false;
    }

    public boolean open(File file) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                Desktop.getDesktop().open(file);
                return true;
            } catch (IOException e) {
            }
        }

        return false;
    }

    public boolean edit(File file) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
            try {
                Desktop.getDesktop().edit(file);
                return true;
            } catch (IOException e) {
            }
        }

        return false;
    }

    public boolean print(File file) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
            try {
                Desktop.getDesktop().print(file);
                return true;
            } catch (IOException e) {
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return System.getProperty("os.name") + ":" + System.getProperty("os.arch");
    }
}
