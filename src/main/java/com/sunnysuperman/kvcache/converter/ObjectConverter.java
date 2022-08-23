package com.sunnysuperman.kvcache.converter;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.util.ByteUtils;
import com.sunnysuperman.commons.util.JSONUtil;
import com.sunnysuperman.kvcache.KvCacheException;


public class ObjectConverter implements ModelConverter<Object> {
    @Override
    public Object deserialize(byte[] bytes) throws KvCacheException {
        return JSON.parseObject(ByteUtils.toString(bytes));
    }

    @Override
    public byte[] serialize(Object object) throws KvCacheException {
        return ByteUtils.fromString(JSONUtil.toJSONString(object));
    }
}
