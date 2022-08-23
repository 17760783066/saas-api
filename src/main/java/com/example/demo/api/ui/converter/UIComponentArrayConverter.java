package com.example.demo.api.ui.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.api.ui.entity.UIComponent;

@Converter(autoApply = true)

public class UIComponentArrayConverter implements AttributeConverter<List<UIComponent>,String>{
    @Override
    public String convertToDatabaseColumn(List<UIComponent> list) {
        return JSON.toJSONString(list);
    }

    @Override
    public List<UIComponent> convertToEntityAttribute(String data) {
        try {
            return JSONArray.parseArray(data, UIComponent.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
