package com.example.demo.api.user.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.api.user.model.User;
import com.example.demo.common.reposiotry.BaseRepository;

public interface UserRepository extends BaseRepository<User, Integer> {

    List<User> findByIdIn(Collection<Integer> ids);

    User findByMobile(String mobile);

    @Query(value = "from User where status=1 ")
    List<User> getSortedList(Pageable pageable);

}