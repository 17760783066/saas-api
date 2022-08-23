package com.example.demo.api.article.converter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sunnysuperman.commons.util.JSONUtil;
import com.sunnysuperman.commons.util.StringUtil;
import com.sunnysuperman.kvcache.KvCacheException;
import com.sunnysuperman.kvcache.converter.ModelConverter;

import com.example.demo.api.article.model.Tag;

import java.util.Map;

public class TagMapConverter implements ModelConverter<Map<Integer, Tag>> {
    private static final TagMapConverter INSTANCE = new TagMapConverter();

    public TagMapConverter() {

    }

    public static TagMapConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<Integer, Tag> deserialize(byte[] value) throws KvCacheException {
        String s = new String(value, StringUtil.UTF8_CHARSET);
        return JSONObject.parseObject(s, new TypeReference<Map<Integer, Tag>>() {
        });
    }

    @Override
    public byte[] serialize(Map<Integer, Tag> model) throws KvCacheException {
        String s = JSONUtil.toJSONString(model);
        return s.getBytes(StringUtil.UTF8_CHARSET);
    }
}
