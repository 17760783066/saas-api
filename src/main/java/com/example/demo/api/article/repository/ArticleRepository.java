package com.example.demo.api.article.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.api.article.model.Article;
import com.example.demo.common.reposiotry.BaseRepository;

public interface ArticleRepository extends BaseRepository<Article, Integer> {

    List<Article> findByIdIn(Collection<Integer> ids);

    @Transactional
    @Modifying
    @Query("update Article set pv=pv+1 where id=:id")
    void updatePV(Integer id);

    @Transactional
    @Modifying
    @Query("update Article set collectNum=collectNum+:num where id=:id")
    void updateCollectNum(Integer id, Integer num);

    @Transactional
    @Modifying
    @Query("update Article set likeNum=likeNum+:num where id=:id")
    void updateLikeNum(Integer id, Integer num);

    @Transactional
    @Modifying
    @Query("update Article set status=:status where id=:id")
    void updateStatus(Integer id, Byte status);

    @Query(value = "SELECT * FROM article WHERE content REGEXP :keywords", nativeQuery = true)
    Page<Article> search(String keywords, Pageable page);

    @Query(value = "from Article where status=1 ")
    List<Article> getSortedList(Pageable pageable);

}