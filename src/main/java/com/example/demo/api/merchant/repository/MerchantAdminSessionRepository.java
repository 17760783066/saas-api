package com.example.demo.api.merchant.repository;

import com.example.demo.api.merchant.model.MerchantAdminSession;
import com.example.demo.common.reposiotry.BaseRepository;

public interface MerchantAdminSessionRepository extends BaseRepository<MerchantAdminSession,Integer>{
    MerchantAdminSession findByToken(String token);
}
