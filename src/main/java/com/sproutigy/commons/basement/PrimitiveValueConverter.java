package com.sproutigy.commons.basement;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class PrimitiveValueConverter {
    private PrimitiveValueConverter() { }

    public static <T> T convert(Object o, Class<T> clazz) {
        if (o == null || Obj.isEmpty(o)) {
            if (clazz.isPrimitive()) {
                if (clazz == boolean.class) {
                    return (T) (Boolean) false;
                }
                if (clazz == byte.class) {
                    return (T) (Byte)(byte) 0;
                }
                if (clazz == short.class) {
                    return (T) (Short)(short) 0;
                }
                if (clazz == int.class) {
                    return (T) (Integer) 0;
                }
                if (clazz == long.class) {
                    return (T) (Long)(long) 0;
                }
                if (clazz == float.class) {
                    return (T) (Float)(float) Float.NaN;
                }
                if (clazz == double.class) {
                    return (T) (Double)(double) Double.NaN;
                }
            }

            return null;
        }

        if (clazz.getClass().equals(String.class)) {
            return (T) o.toString();
        }

        if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return (T) (Byte) Byte.parseByte(o.toString());
        }
        if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return (T) (Short) Short.parseShort(o.toString());
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return (T) (Integer) Integer.parseInt(o.toString());
        }
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return (T) (Long) Long.parseLong(o.toString());
        }
        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return (T) (Float) Float.parseFloat(o.toString());
        }
        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return (T) (Double) Double.parseDouble(o.toString());
        }
        if (clazz.equals(BigInteger.class)) {
            return (T) new BigInteger(o.toString());
        }
        if (clazz.equals(BigDecimal.class) || clazz.equals(Number.class)) {
            return (T) new BigDecimal(o.toString());
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            String s = o.toString().toLowerCase();
            if (s.isEmpty() || s.equals("false") || s.equals("off") || s.equals("not") || s.equals("no") || s.equals("0")) {
                return (T) Boolean.FALSE;
            } else {
                return (T) Boolean.TRUE;
            }
        }

        if (o.getClass().equals(Object.class) || o.getClass().equals(clazz) || clazz.isAssignableFrom(o.getClass())) {
            return (T) o;
        }

        throw new IllegalArgumentException("Cannot convert value to " + clazz.getName());
    }

}
