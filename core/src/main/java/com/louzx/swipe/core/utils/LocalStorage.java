package com.louzx.swipe.core.utils;

import com.alibaba.fastjson.JSONObject;

public class LocalStorage {

    private final static ThreadLocal<JSONObject> LOCAL_STORAGE = new ThreadLocal<>();

    public static JSONObject init () {
        JSONObject property = LOCAL_STORAGE.get();
        if (null == property) {
            property = new JSONObject();
            LOCAL_STORAGE.set(property);
        }
        return property;
    }

    public static Object getProperty(String name) {
        JSONObject property = LocalStorage.getProperty();
        return null != property ? property.get(name) : null;
    }

    public static JSONObject getProperty() {
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
