package com.example.demo.common.converter;


import com.example.demo.common.model.Location;
import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(Location obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Location convertToEntityAttribute(String data) {
        try {
            return JSON.parseObject(data, Location.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
