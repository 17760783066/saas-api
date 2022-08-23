package com.example.demo.api.common.service;

import com.example.demo.common.model.ValCode;

import java.util.List;
import java.util.Map;

public interface CommonService {

    void saveValCode(Long key, ValCode valCode);

    ValCode getValCode(Long key);

    void genValCode(ValCode valCode, String smsTplCode);

    void resetPassword(ValCode valCode, String password);

    Map geocoder(String lat, String lng, String key);

    List<String> getPayNumber(String url);

    Map<String, Object> oem(Integer merchantId);



}
