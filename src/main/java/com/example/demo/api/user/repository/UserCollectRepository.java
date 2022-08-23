package com.example.demo.api.user.repository;

import java.util.Collection;
import java.util.List;

import com.example.demo.api.user.model.UserCollect;
import com.example.demo.common.reposiotry.BaseRepository;

public interface UserCollectRepository extends BaseRepository<UserCollect, Integer> {

    UserCollect findByUserIdAndCollectTypeAndCollectId(int userId, byte collectType, int collectId);

    List<UserCollect> findByUserIdAndCollectTypeAndCollectIdIn(int userId, byte collectType,
            Collection<Integer> collectIds);

}