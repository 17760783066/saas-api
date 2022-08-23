package com.example.demo.api.trade.service;

import java.util.List;

import com.example.demo.api.trade.entity.TradeWrapOption;
import com.example.demo.api.trade.model.Trade;
import com.example.demo.api.trade.qo.TradeQo;

import org.springframework.data.domain.Page;

public interface TradeService {
    //user
    Integer save(Trade trade, Integer total,Integer totalAmoun);

    Trade findById(Integer id);

    void updateType(Integer id, Byte type);
    //merchant
    Trade warpUserFindById(Integer id);
    Page<Trade> trades(TradeQo qo, TradeWrapOption option);

}
