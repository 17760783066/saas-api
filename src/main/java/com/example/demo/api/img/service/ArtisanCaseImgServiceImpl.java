package com.example.demo.api.img.service;

import java.util.List;

import com.example.demo.api.img.model.ArtisanCaseImg;
import com.example.demo.api.img.qo.ArtisanCaseImgQo;
import com.example.demo.api.img.repository.ArtisanCaseImgRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtisanCaseImgServiceImpl implements ArtisanCaseImgService {
    @Autowired
    private ArtisanCaseImgRepository artisanCaseImgRepository;

    @Override
    public void save(ArtisanCaseImg artisanCaseImg) {
        artisanCaseImgRepository.save(artisanCaseImg);
    }

    @Override
    public List<ArtisanCaseImg> qo(ArtisanCaseImgQo qo) {
        return artisanCaseImgRepository.findAll(qo);
    }
    private ArtisanCaseImg getById(Integer id) {
        ArtisanCaseImg artisanCaseImg = artisanCaseImgRepository.findById(id).orElse(null);
        
        return artisanCaseImg;
    }
    @Override
    public ArtisanCaseImg one(Integer id) {
        ArtisanCaseImg artisanCaseImg = getById(id);
        return artisanCaseImg;
    }

    @Override
    public void remove(Integer id) {
        artisanCaseImgRepository.delete(getById(id));
    }

    @Override
    public void status(Integer id,Byte status) {
        ArtisanCaseImg artisanCaseImg = getById(id);
        artisanCaseImg.setStatus(status);
        artisanCaseImgRepository.save(artisanCaseImg);
    }

}
