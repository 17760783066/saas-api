package com.example.demo.common.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.api.product.model.Spec;

@Converter(autoApply = true)

public class SpecArrayConverter implements AttributeConverter<List<Spec>,String>{

    @Override
    public String convertToDatabaseColumn(List<Spec> attribute) {
        return JSON.toJSONString(attribute);

    }

    @Override
    public List<Spec> convertToEntityAttribute(String dbData) {
        try {
            return JSONArray.parseArray(dbData, Spec.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
