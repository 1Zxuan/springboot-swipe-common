package com.louzx.swipe.core.utils;

import java.util.HashMap;
import java.util.Map;

public class LocalStorage {

    private final static ThreadLocal<Map<String, Object>> LOCAL_STORAGE = new ThreadLocal<>();

    public static Map<String, Object> init () {
        Map<String, Object> property = LOCAL_STORAGE.get();
        if (null == property) {
            property = new HashMap<>(256);
            LOCAL_STORAGE.set(property);
        }
        return property;
    }

    public static Object getProperty(String name) {
        Map<String, Object> property = LocalStorage.getProperty();
        return null != property ? property.get(name) : null;
    }

    public static Map<String, Object> getProperty() {
        return LocalStorage.init();
    }

    public static void put(String name, Object value) {
        LocalStorage.getProperty().put(name, value);
    }

    public static void remove(String name) {
        if (null == name) {
            LocalStorage.LOCAL_STORAGE.remove();
        } else {
            LocalStorage.getProperty().remove(name);
        }
    }

    public static void remove() {
        remove(null);
    }

    public static void clear() {
        LocalStorage.getProperty().clear();
    }

}
