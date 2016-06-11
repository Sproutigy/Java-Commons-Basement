package com.sproutigy.commons.basement.runtime.platform;

public final class Architecture {

    private Architecture() {
    }

    public static boolean isJVM64bit() {
        return System.getProperty("os.arch").contains("64");
    }

    public static boolean isJVM32bit() {
        return !isJVM64bit();
    }

    public static boolean isOS32bit() {
        return !isOS64bit();
    }

    public static boolean isOS64bit() {
        if (System.getProperty("os.name").contains("Windows")) {
            return (System.getenv("ProgramFiles(x86)") != null);
        }

        String procArch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        if (procArch != null) {
            if (procArch.endsWith("64")) return true;
        }

        if (wow64Arch != null) {
            if (wow64Arch.endsWith("64")) return true;
        }

        return System.getProperty("os.arch").contains("64");
    }

    public static boolean isX86() {
        String arch = System.getProperty("os.arch");
        return arch.contains("86") || arch.contains("amd64") || arch.contains("ia64");
    }
}
