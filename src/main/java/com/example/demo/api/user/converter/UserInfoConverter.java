package com.example.demo.api.user.converter;


import com.example.demo.api.user.entity.UserInfo;
import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserInfoConverter implements AttributeConverter<UserInfo, String> {

    @Override
    public String convertToDatabaseColumn(UserInfo obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public UserInfo convertToEntityAttribute(String data) {
        try {
            return JSON.parseObject(data, UserInfo.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
