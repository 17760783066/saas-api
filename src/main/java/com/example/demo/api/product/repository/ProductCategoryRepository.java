package com.example.demo.api.product.repository;

import javax.transaction.Transactional;

import com.example.demo.api.product.model.ProductCategory;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory,Integer>{
    ProductCategory findBySequence(Integer sequence);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ProductCategory set status= :status where sequence like CONCAT(:sequence,'%')")
    void status(@Param(value = "status") Byte status, @Param(value = "sequence") String sequence);

}
