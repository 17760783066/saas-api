package com.example.demo.api.product.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.product.model.ProductTemplate;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductTemplateRepository extends BaseRepository<ProductTemplate,Integer>{
    @Transactional
    @Modifying
    @Query("delete from ProductTemplate where id  in(:ids)")
    void deleteByIds(List<Integer> ids);
    ProductTemplate findFirstByIdBeforeOrderByIdDesc(Integer id);
    ProductTemplate findFirstByIdAfterOrderByIdAsc(Integer id);
}
