package com.sproutigy.commons.basement.runtime;

import java.lang.ref.WeakReference;

public final class RuntimeUtil {
    private RuntimeUtil() {
    }

    public static void forceGarbageCollect() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<>(obj);
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
        System.runFinalization();
    }
}
