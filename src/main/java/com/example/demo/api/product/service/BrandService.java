package com.example.demo.api.product.service;

import java.util.List;

import com.example.demo.api.product.model.Brand;
import com.example.demo.api.product.qo.BrandQo;
import com.example.demo.common.controller.auth.AdminType;

import org.springframework.data.domain.Page;

public interface BrandService {

    Brand list(Integer id);

    void save(Brand brand);

    Page<Brand> page(BrandQo qo, AdminType admin);

    List<Brand> list();

    void status(Integer id, Byte status);

    Brand item(int id);

    Page<Brand> pageUser(BrandQo qo);


}
