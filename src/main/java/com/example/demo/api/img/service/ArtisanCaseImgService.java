package com.example.demo.api.img.service;

import java.util.List;

import com.example.demo.api.img.model.ArtisanCaseImg;
import com.example.demo.api.img.qo.ArtisanCaseImgQo;


public interface ArtisanCaseImgService {

    void save(ArtisanCaseImg artisanCaseImg);

    List<ArtisanCaseImg> qo(ArtisanCaseImgQo qo);

    ArtisanCaseImg one(Integer id);

    void remove(Integer id);

    void status(Integer id,Byte status);
    
}
