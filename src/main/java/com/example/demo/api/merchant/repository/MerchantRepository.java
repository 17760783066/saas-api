package com.example.demo.api.merchant.repository;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends BaseRepository<Merchant, Integer> {
    @Query(value = "select count(*) from Merchant where status = :s and validThru < :t ")
    Long countByValidThru(byte s, long t);
}
