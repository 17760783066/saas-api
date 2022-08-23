package com.example.demo.api.merchant.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.qo.MerchantQo;
import com.example.demo.api.renew.model.Renew;
import com.example.demo.common.exception.ServiceException;

import org.springframework.data.domain.Page;

public interface MerchantService {
    void saveMerchant(Merchant merchant);

    Page<Merchant> merchants(MerchantQo qo);

    Merchant merchant(int id);

    void merchantStatus(Integer id, Byte status) throws ServiceException;

    void create(Renew renew);

    Map<Integer, Merchant> findByIdIn(Collection<Integer> ids);
    Map<Integer, Merchant> findByIds(Set<Integer> ids);


    Page<Merchant> merchantsLess(MerchantQo qo);

    Merchant getById(Integer id);

    Long validThru(int day);
}
