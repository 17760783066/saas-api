package com.example.demo.api.user.repository;

import com.example.demo.api.user.model.UserSession;
import com.example.demo.common.reposiotry.BaseRepository;

public interface UserSessionRepository extends BaseRepository<UserSession, Integer> {

    UserSession findByToken(String token);

}