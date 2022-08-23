package com.example.demo.api.ui.service;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.img.model.ArtisanCaseImg;
import com.example.demo.api.img.repository.ArtisanCaseImgRepository;
import com.example.demo.api.ui.entity.SettingError;
import com.example.demo.api.ui.entity.UIComponent;
import com.example.demo.api.ui.entity.UIComponentKeyVO;
import com.example.demo.api.ui.model.UI;
import com.example.demo.api.ui.qo.UIQo;
import com.example.demo.api.ui.repository.UIRepository;
import com.example.demo.common.context.Contexts;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UIServiceImpl implements UIService, SettingError {
    @Autowired
    private UIRepository uiRepository;
    @Autowired
    private ArtisanCaseImgRepository artisanCaseImgRepository;
    // private KvCacheWrap<String, UI> uiCache;
    // @Autowired
    // private KvCacheFactory kvCacheFactory;

    @Override
    @Transactional
    public void save(UI ui) throws ServiceException {
        validateUI(ui);
        Integer id = ui.getId();
        if (id == null || id == 0) {
            uiRepository.save(ui);
        } else {
            UI exist = findById(id);
            exist.setTitle(ui.getTitle());
            exist.setComponents(ui.getComponents());
            uiRepository.save(exist);
        }

        if (ui.getIsDefault() == Constants.STATUS_OK) {
            setDefault(ui.getId());
        }
    }

    @Override
    @Transactional
    public void setDefault(int id) throws ServiceException {
        uiRepository.offDefault(Constants.STATUS_HALT);
        UI ui = findById(id);
        ui.setIsDefault(Constants.STATUS_OK);
        uiRepository.save(ui);

    }

    private UI findById(Integer id) {
        UI ui = uiRepository.findById(id).orElse(null);
        if (StringUtils.isNull(ui)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return ui;
    }
  

    private void validateUI(UI ui) throws ServiceException {

        String title = ui.getTitle();
        if (StringUtils.isEmpty(title)) {
            throw new ServiceException(ERR_TITLE_VALID_DENIED);
        }

        List<UIComponent> components = ui.getComponents();
        if (components.size() == 0) {
            throw new ServiceException(ERR_UI_COMPONENT_VALID_DENIED);
        }

        for (UIComponent component : components) {
            if (!UIComponentKeyVO.contains(component.getKey())
                    || (!UIComponentKeyVO.contains("SELECT") && CollectionUtils.isEmpty(component.getList()))) {
                throw new ServiceException(ERR_UI_COMPONENT_VALID_DENIED);
            }
        }
        if (ui.getIsDefault() == null) {
            ui.setIsDefault(Constants.STATUS_HALT);
        }

        boolean update = ui.getId() != null && ui.getId() > 0;

        if (!update) {
            ui.setCreatedAt(System.currentTimeMillis());
        }
    }

    @Override
    public Page<UI> uis(UIQo qo) {
        Page<UI> page = uiRepository.findAll(qo);
        return page;
    }

    @Override
    public UI ui(Integer id) {
        return findById(id);
    }

    @Override
    public void img(ArtisanCaseImg artisanCaseImg) {
        artisanCaseImgRepository.save(artisanCaseImg);
    }

    @Override
    public void status(Integer id, Byte isDefault) {
        uiRepository.offDefault(Constants.STATUS_HALT);
        UI item = ui(id);
        item.setIsDefault(isDefault);
        uiRepository.save(item);
    }

    @Override
    public UI one() {
        UI ui = uiRepository.findByIsDefault(Constants.STATUS_OK);
        return ui;
    }

}
