package com.example.demo.api.product.service;

import java.util.List;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.product.model.Brand;
import com.example.demo.api.product.qo.BrandQo;
import com.example.demo.api.product.repository.BrandRepository;
import com.example.demo.api.renew.model.RenewConstants;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService, AdminError {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand list(Integer id) {
        Brand brand = item(id);
        return brand;
    }

    @Override
    public Brand item(int id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (StringUtils.isNull(brand)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public void save(Brand brand) {
        if (brand.getId() != null && brand.getId() > 0) {// 编辑
            brandRepository.save(brand);
        } else {// 新建
            if (brand.getName() == null) {
                throw new ArgumentServiceException("名字为空");
            }
            if (brand.getCover() == null) {
                throw new ArgumentServiceException("Logo为空");
            }
            if (brand.getProductCategorySequences() == null) {
                throw new ArgumentServiceException("分类为空");
            }
            brand.setStatus(RenewConstants.CHECK_WAIT);
            brand.setCreatedAt(System.currentTimeMillis());
            brandRepository.save(brand);
        }
    }

    @Override
    public Page<Brand> page(BrandQo qo, AdminType admin) {
        return brandRepository.findAll(qo);
    }

    @Override
    public List<Brand> list() {
        BrandQo qo = new BrandQo();
        qo.setStatus(Constants.STATUS_OK);
        qo.setPageSize(Constants.PAGESIZE_INF);
        return brandRepository.findAllBySize(qo);
    }

    @Override
    public void status(Integer id, Byte status) {
        Brand brand = item(id);
        brand.setStatus(status);
        brandRepository.save(brand);
    }

    @Override
    public Page<Brand> pageUser(BrandQo qo) {
        return brandRepository.findAll(qo);
    }
}
