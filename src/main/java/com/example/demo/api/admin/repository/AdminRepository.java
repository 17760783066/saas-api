package com.example.demo.api.admin.repository;


import com.example.demo.api.admin.model.Admin;
import com.example.demo.common.reposiotry.BaseRepository;

import java.util.List;
import java.util.Set;

public interface AdminRepository extends BaseRepository<Admin, Integer> {
    Admin findByMobile(String mobile);

    List<Admin> findByIdIn(Set<Integer> ids);
}
