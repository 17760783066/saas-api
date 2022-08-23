package com.example.demo.api.renew.repository;

import java.util.Collection;
import java.util.List;

import com.example.demo.api.renew.model.Renew;
import com.example.demo.common.reposiotry.BaseRepository;

public interface RenewRepository extends BaseRepository<Renew,Integer> {
    List<Renew> findByIdIn(Collection<Integer> ids);
}
