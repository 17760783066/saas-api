package com.example.demo.api.admin.repository;


import com.example.demo.api.admin.model.AdminSession;
import com.example.demo.common.reposiotry.BaseRepository;

public interface AdminSessionRepository extends BaseRepository<AdminSession, Integer> {
    AdminSession findByToken(String token);
}
