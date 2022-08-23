package com.example.demo.api.product.service;

import java.util.List;
import java.util.Map;

import com.example.demo.api.product.model.ProductTemplate;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.common.controller.auth.AdminType;

import org.springframework.data.domain.Page;

public interface ProductTemplateService {

    Page<ProductTemplate> page(ProductQo qo, AdminType adminType);

    void status(Integer id, Byte status);

    void save(ProductTemplate productTemplate);

    void remove(List<Integer> ids);

    ProductTemplate item(Integer id);

    Map<String, Object> three(Integer id);

}
