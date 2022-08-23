package com.example.demo.api.product.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.example.demo.api.product.model.Product;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.qo.ProductWo;
import com.example.demo.common.controller.auth.AdminType;

import org.springframework.data.domain.Page;

public interface ProductService {

    Product item(Integer productId);

    void save(Product product);

    void remove(List<Integer> ids);

    Page<Product> page(ProductQo qo, AdminType adminType);
    List<Product> list(ProductQo qo);

    Map<String, Object> three(Integer id);

    void status(Integer id, Byte status);

    List<Product> list();
    Product item(Integer productId,ProductWo wo);
    Map<Integer, Product> findByIdIn(Collection<Integer> ids);

    void saveAll(List<Product> products);

}
