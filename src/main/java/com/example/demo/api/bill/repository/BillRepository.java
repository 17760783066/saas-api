package com.example.demo.api.bill.repository;

import java.util.Collection;

import com.example.demo.api.bill.model.Bill;
import com.example.demo.common.reposiotry.BaseRepository;

import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends BaseRepository<Bill, Integer>{
    Bill findByPayNumber(String payNumber);
    Collection<Bill> findByIdIn(Collection<Integer> Ids);
    @Query("update Bill set status=:status where payNumber=:payNumber")
    void updateBillStatus(String payNumber, Byte status);



}
