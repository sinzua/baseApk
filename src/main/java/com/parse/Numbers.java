package com.parse;

class Numbers {
    Numbers() {
    }

    static Number add(Number first, Number second) {
        if ((first instanceof Double) || (second instanceof Double)) {
            return Double.valueOf(first.doubleValue() + second.doubleValue());
        }
        if ((first instanceof Float) || (second instanceof Float)) {
            return Float.valueOf(first.floatValue() + second.floatValue());
        }
        if ((first instanceof Long) || (second instanceof Long)) {
            return Long.valueOf(first.longValue() + second.longValue());
        }
        if ((first instanceof Integer) || (second instanceof Integer)) {
            return Integer.valueOf(first.intValue() + second.intValue());
        }
        if ((first instanceof Short) || (second instanceof Short)) {
            return Integer.valueOf(first.shortValue() + second.shortValue());
        }
        if ((first instanceof Byte) || (second instanceof Byte)) {
            return Integer.valueOf(first.byteValue() + second.byteValue());
        }
        throw new RuntimeException("Unknown number type.");
    }

    static Number subtract(Number first, Number second) {
        if ((first instanceof Double) || (second instanceof Double)) {
            return Double.valueOf(first.doubleValue() - second.doubleValue());
        }
        if ((first instanceof Float) || (second instanceof Float)) {
            return Float.valueOf(first.floatValue() - second.floatValue());
        }
        if ((first instanceof Long) || (second instanceof Long)) {
            return Long.valueOf(first.longValue() - second.longValue());
        }
        if ((first instanceof Integer) || (second instanceof Integer)) {
            return Integer.valueOf(first.intValue() - second.intValue());
        }
        if ((first instanceof Short) || (second instanceof Short)) {
            return Integer.valueOf(first.shortValue() - second.shortValue());
        }
        if ((first instanceof Byte) || (second instanceof Byte)) {
            return Integer.valueOf(first.byteValue() - second.byteValue());
        }
        throw new RuntimeException("Unknown number type.");
    }

    static int compare(Number first, Number second) {
        if ((first instanceof Double) || (second instanceof Double)) {
            return (int) Math.signum(first.doubleValue() - second.doubleValue());
        }
        if ((first instanceof Float) || (second instanceof Float)) {
            return (int) Math.signum(first.floatValue() - second.floatValue());
        }
        if ((first instanceof Long) || (second instanceof Long)) {
            long diff = first.longValue() - second.longValue();
            if (diff < 0) {
                return -1;
            }
            return diff > 0 ? 1 : 0;
        } else if ((first instanceof Integer) || (second instanceof Integer)) {
            return first.intValue() - second.intValue();
        } else {
            if ((first instanceof Short) || (second instanceof Short)) {
                return first.shortValue() - second.shortValue();
            }
            if ((first instanceof Byte) || (second instanceof Byte)) {
                return first.byteValue() - second.byteValue();
            }
            throw new RuntimeException("Unknown number type.");
        }
    }
}
