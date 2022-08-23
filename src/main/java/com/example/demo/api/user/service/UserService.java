package com.example.demo.api.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.demo.api.user.authority.UserSessionWrapper;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.qo.UserQo;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.model.ValCode;

import org.springframework.data.domain.Page;

public interface UserService {
  
    Page<User> users(UserQo qo, AdminType adminType);

    User user(Integer id, AdminType adminType);

    void status(Integer id, byte status);

    Map<Integer, User> findByIdIn(Set<Integer> ids);

    List<User> getTopByFiledAndSize(String sortField, Integer size);

    void updateCollectNum(int userId, int num);

    // signin

    void signup(User user, ValCode valCode);

    UserSessionWrapper findByToken(String token);

    UserSessionWrapper signin(ValCode valCode);

    Map<String, Object> profile();

    void saveProfile(User user);

  //merchant
    User user(int id);

}
