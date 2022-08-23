package com.example.demo.common.converter;

import java.util.List;

import javax.persistence.AttributeConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.api.product.model.Param;

public class ParamArrayConverter implements AttributeConverter<List<Param>,String>{

    @Override
    public String convertToDatabaseColumn(List<Param> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<Param> convertToEntityAttribute(String dbData) {
       try {
           return JSONArray.parseArray(dbData, Param.class);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       } 
    }
    
}
