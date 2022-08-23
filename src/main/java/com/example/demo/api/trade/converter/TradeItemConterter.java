package com.example.demo.api.trade.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.api.trade.entity.TradeItem;

@Converter(autoApply = true)
public class TradeItemConterter implements AttributeConverter<List<TradeItem>, String>{
    @Override
    public String convertToDatabaseColumn(List<TradeItem> items) {
        return JSON.toJSONString(items);
    }

    @Override
    public List<TradeItem> convertToEntityAttribute(String data) {
        try {
            return JSONArray.parseArray(data, TradeItem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
