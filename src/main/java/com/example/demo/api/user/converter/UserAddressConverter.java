package com.example.demo.api.user.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.example.demo.api.user.model.Address;

@Converter(autoApply = true)


public class UserAddressConverter implements AttributeConverter<Address, String>{
    @Override
    public String convertToDatabaseColumn(Address address) {
        return JSON.toJSONString(address);
    }

    @Override
    public Address convertToEntityAttribute(String data) {
        try {
            return JSON.parseObject(data, Address.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
