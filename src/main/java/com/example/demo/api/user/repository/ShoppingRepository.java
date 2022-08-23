package com.example.demo.api.user.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.user.model.Shopping;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingRepository extends BaseRepository<Shopping, Integer>{
    
    List<Shopping> findByIdIn(List<Integer> ids);

    @Transactional
    @Modifying
    @Query("delete from Shopping where  id  in(:ids)")
    void deleteByIds(List<Integer> ids);

    @Transactional
    @Modifying
    @Query("update Shopping set number=:number where id=:id")
    void updateNumber(Integer id, Integer number);

    @Transactional
    @Modifying
    @Query("update Shopping set productSno=:productSno where id=:id")
    void updateParams(Integer id, String productSno);

    Shopping findByUserIdAndProductIdAndMerchantIdAndProductSno(Integer userId, Integer productId, Integer merchantId, String productSno);


}
