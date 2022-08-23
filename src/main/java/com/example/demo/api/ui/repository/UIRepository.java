package com.example.demo.api.ui.repository;

import javax.transaction.Transactional;

import com.example.demo.api.ui.model.UI;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UIRepository extends BaseRepository<UI,Integer> {
    @Transactional
    @Modifying
    @Query("update UI set isDefault= :isDefault")
    void offDefault(@Param(value = "isDefault") Byte isDefault);

    UI findByIsDefault( Byte isDefault);

}
