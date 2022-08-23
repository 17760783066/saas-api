package com.example.demo.api.user.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.api.user.model.Address;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends BaseRepository<Address, Integer>{
    List<Address> findByUserId(Integer id);

    Address findByUserIdAndAndIsDefault(Integer id, Byte num);

    @Transactional
    @Modifying
    @Query(value = "update address set is_default=:isDefault where user_id=:userId", nativeQuery = true)
    void updateIsDefault(Integer userId, Byte isDefault);


}
