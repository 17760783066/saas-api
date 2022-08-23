package com.example.demo.api.merchant.repository;

import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.common.reposiotry.BaseRepository;

public interface MerchantAdminRepository extends BaseRepository<MerchantAdmin,Integer>{
    MerchantAdmin findByMerchantIdAndMobile(int id,String mobile);

}
