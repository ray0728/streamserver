package com.rcircle.service.stream.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpContext {
    public static final String AUTH_TOKEN = "authorization";
    private Map<String, String> contextMap = new HashMap<>();

    public void setValue(String key, String value) {
        contextMap.put(key, value);
    }

    public String getValue(String key) {
        return contextMap.get(key);
    }
}
