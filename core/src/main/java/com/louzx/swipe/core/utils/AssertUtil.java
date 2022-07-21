package com.louzx.swipe.core.utils;

import java.util.function.Supplier;

public class AssertUtil {

    public static void isThrow (boolean condition, String message) throws Exception {
        isThrow(condition, () -> new Exception(message));
    }

    public static void isThrow(boolean condition, Supplier<? extends Exception> supplier) throws Exception {
        if (condition)
            throw supplier.get();
    }
}
