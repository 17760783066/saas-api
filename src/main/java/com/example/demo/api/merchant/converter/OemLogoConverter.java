package com.example.demo.api.merchant.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.example.demo.api.merchant.model.Oem;

@Converter(autoApply = true)
public class OemLogoConverter implements AttributeConverter<Oem, String>{
    @Override
    public String convertToDatabaseColumn(Oem obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Oem convertToEntityAttribute(String data) {
        try {
            return JSON.parseObject(data, Oem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
