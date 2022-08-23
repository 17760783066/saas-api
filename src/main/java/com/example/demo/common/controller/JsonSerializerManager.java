package com.example.demo.common.controller;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.sunnysuperman.commons.util.JSONUtil;

import java.util.HashMap;

public class JsonSerializerManager {
    private static java.util.Map<java.lang.reflect.Type, ObjectSerializer> serializers = new HashMap<>();

    public static void register(java.lang.reflect.Type type, ObjectSerializer serializer) {
        synchronized (serializers) {
            serializers.put(type, serializer);
        }
    }

    public static String serialize(Object result) {
        return JSONUtil.toJSONString(result, serializers);
    }

}
