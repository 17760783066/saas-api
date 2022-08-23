package com.example.demo.api.product.service;
import java.util.List;

import com.example.demo.api.product.model.ProductCategory;
import com.example.demo.common.controller.auth.AdminType;



public interface ProductCategoryService {

    void save(ProductCategory product);

    List<ProductCategory> tree(AdminType adminType);

    List<ProductCategory> merchantTree();

    void status(Integer id,Byte status);
    
    List<ProductCategory> list(AdminType adminType);
    
    List<ProductCategory> sequences2List(List<String> ss);

    ProductCategory getById(Integer categoryId);


}
