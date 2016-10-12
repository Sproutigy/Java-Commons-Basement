package com.sproutigy.commons.basement;

import java.lang.reflect.Method;
import java.util.*;

public final class Obj {

    private Obj() {
    }

    private static final OptionalHandler[] OPTIONAL_HANDLERS = {
            new OptionalHandler("java.util.Optional", "isPresent", "get"),
            new OptionalHandler("com.google.common.base.Optional", "isPresent", "get"),
    };

    private static OptionalHandler matchOptionalHandler(Object o) {
        if (o != null) {
            for (OptionalHandler optionalHandler : OPTIONAL_HANDLERS) {
                if (optionalHandler.matches(o)) {
                    return optionalHandler;
                }
            }
        }

        return null;
    }

    public static boolean isOptional(Object o) {
        if (o != null) {
            for (OptionalHandler optionalHandler : OPTIONAL_HANDLERS) {
                if (optionalHandler.matches(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T nonnull(Object o) {
        if (o == null) {
            throw new NullPointerException("Expected non-null object");
        }

        if (isOptional(o)) {
            OptionalHandler optionalHandler = matchOptionalHandler(o);
            if (!optionalHandler.isPresent(o)) {
                throw new NullPointerException("Expected non-null object, but optional value is absent");
            } else {
                return (T) optionalHandler.get(o);
            }
        }

        return (T) o;
    }

    public static <T> T nonnull(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }

        throw new NullPointerException("Expected non-null argument");
    }

    @SuppressWarnings("unchecked")
    public static <T> T nullable(Object o) {
        if (o == null) {
            return null;
        }

        if (isOptional(o)) {
            OptionalHandler optionalHandler = matchOptionalHandler(o);
            if (!optionalHandler.isPresent(o)) {
                return null;
            } else {
                return (T) optionalHandler.get(o);
            }
        }

        return (T) o;
    }

    @SuppressWarnings("unchecked")
    public static <T> T nonempty(Object o) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException("Expected non-empty object");
        }

        if (isOptional(o)) {
            OptionalHandler optionalHandler = matchOptionalHandler(o);
            if (!optionalHandler.isPresent(o)) {
                throw new IllegalArgumentException("Expected non-empty object");
            } else {
                return (T) optionalHandler.get(o);
            }
        }

        return (T) o;
    }

    public static <T> T nonempty(T... args) {
        for (T arg : args) {
            if (!isEmpty(arg)) {
                return arg;
            }
        }

        throw new IllegalArgumentException("Expected non-empty argument");
    }

    /**
     * Prints null as empty string instead of "null", gets value from optionals
     *
     * @param o
     * @return
     */
    public static String stringify(Object o) {
        String str = null;
        if (o == null) {
            str = "";
        } else if (isOptional(o)) {
            OptionalHandler optionalHandler = matchOptionalHandler(o);
            if (!optionalHandler.isPresent(o)) {
                str = "";
            } else {
                str = optionalHandler.get(o).toString();
            }
        }

        if (str != null) {
            return str;
        } else {
            return "";
        }
    }

    public static boolean equals(Object... objs) {
        if (objs.length == 0) {
            return false;
        }
        if (objs.length == 1) {
            return true;
        }

        for (int i = 0; i < objs.length - 1; i++) {
            if (!Objects.equals(objs[i], objs[i - 1])) {
                return false;
            }
        }

        return true;
    }

    public static int hashCode(Object o) {
        return Objects.hashCode(o);
    }

    public static int hashCode(Object... values) {
        return Objects.hash(values);
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }

        if (o instanceof CharSequence) {
            return ((CharSequence) o).length() == 0;
        }
        if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        }
        if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }
        if (o instanceof Object[]) {
            return ((Object[]) o).length == 0;
        }
        if (o instanceof Enumeration) {
            return ((Enumeration) o).hasMoreElements();
        }
        if (o instanceof Iterable) {
            return ((Iterable) o).iterator().hasNext();
        }
        if (o instanceof Iterator) {
            return ((Iterator) o).hasNext();
        }

        if (isOptional(o)) {
            OptionalHandler optionalHandler = matchOptionalHandler(o);
            return optionalHandler.isPresent(o);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getClassOrNull(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Method getMethodOrNull(Class clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }


    private static class OptionalHandler {
        private String className;
        private Class clazz = null;
        private Method methodPresence = null;
        private Method methodGet = null;

        public OptionalHandler(String className, String methodNamePresence, String methodNameGet) {
            this.className = className;

            this.clazz = getClassOrNull(className);
            if (this.clazz != null) {
                methodPresence = getMethodOrNull(clazz, methodNamePresence);
                if (methodPresence != null) {
                    methodPresence.setAccessible(true);
                }

                methodGet = getMethodOrNull(clazz, methodNameGet);
                if (methodGet != null) {
                    methodGet.setAccessible(true);
                }
            }
        }

        public boolean matches(Object o) {
            if (o == null) {
                return false;
            }
            return o.getClass().getName().equals(className);
        }

        public boolean isPresent(Object optional) {
            try {
                return (boolean) methodPresence.invoke(optional);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot check presence of an optional", e);
            }
        }

        public Object get(Object optional) {
            try {
                return methodGet.invoke(optional);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot get value of an optional");
            }
        }

        @Override
        public String toString() {
            return className;
        }
    }
}
