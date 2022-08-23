package com.example.demo.api.ui.service;

import com.example.demo.api.img.model.ArtisanCaseImg;
import com.example.demo.api.ui.model.UI;
import com.example.demo.api.ui.qo.UIQo;
import com.example.demo.common.exception.ServiceException;

import org.springframework.data.domain.Page;

public interface UIService {
    void save(UI ui) throws ServiceException;
    void setDefault(int id) throws ServiceException;
    Page<UI> uis(UIQo qo);
    UI ui(Integer id);
    void img(ArtisanCaseImg artisanCaseImg);
    void status(Integer id, Byte isDefault);
    UI one();

}
