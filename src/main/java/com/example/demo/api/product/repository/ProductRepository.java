package com.example.demo.api.product.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.product.model.Product;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends BaseRepository<Product,Integer>{
    @Transactional
    @Modifying
    @Query("delete from Product where id  in(:ids)")
    void deleteByIds(List<Integer> ids);
    Product findFirstByIdBeforeOrderByIdDesc(Integer id);
    Product findFirstByIdAfterOrderByIdAsc(Integer id);
}
