package com.example.demo.api.user.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.demo.api.user.model.UserCollect;
import com.example.demo.api.user.qo.UserCollectQo;

public interface UserCollectService {

    byte isCollected(byte collectType, int collectId);

    int collect(byte collectType, int collectId);

    Map<Integer, UserCollect> findByCollectIdIn(int userId, byte collectType, Collection<Integer> ids);

    Page<UserCollect> userCollects(UserCollectQo qo);

    void removeMyCollect(int id);

}
