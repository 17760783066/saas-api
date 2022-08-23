package com.example.demo.api.trade.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.trade.model.Trade;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TradeRepository extends BaseRepository<Trade, Integer>{
    List<Trade> findByIdIn(Collection<Integer> Ids);

    Trade findByOrderNumber(String orderNum);

    @Transactional
    @Modifying
    @Query("delete from Trade where id  in(:ids)")
    void deleteByIds(List<Integer> ids);

    @Transactional
    @Modifying
    @Query("update Trade set type=:type where id=:id")
    void updateTradeType(Integer id, Byte type);

    List<Trade> findByUserId(Integer id);

    List<Trade> findAllByTypeEqualsAndCreatedAtBefore(Byte type, Long checkTime);

}
