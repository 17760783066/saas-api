package com.example.demo.api.user.service;

import java.util.List;

import com.example.demo.api.user.model.Shopping;
import com.example.demo.api.user.qo.ShoppingQo;
import com.example.demo.api.user.qo.ShoppingWo;

public interface ShoppingService {
    List<Shopping> listShopping(ShoppingQo qo,ShoppingWo wo);

    Shopping shoppingSave(Shopping shopping);

    void updateNumber(Integer id, Integer number);
    void updateParams(Integer id, String productSno);

    List<Shopping> findAll(ShoppingWo wo);

    void removeOne(Integer id);

    List<Shopping> findCartList(List<Integer> ids,ShoppingWo wo);

    void removeList(List<Integer> cartIds);



}
