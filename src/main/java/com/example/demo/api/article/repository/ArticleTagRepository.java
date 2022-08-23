package com.example.demo.api.article.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.api.article.model.ArticleTag;
import com.example.demo.common.reposiotry.BaseRepository;

import java.util.List;
import java.util.Set;

public interface ArticleTagRepository extends BaseRepository<ArticleTag, Integer> {

    void deleteByTagId(Integer tagId);

    void deleteByArticleId(Integer articleId);

    @Transactional
    @Modifying
    @Query("update ArticleTag set pv=pv+1 where articleId=:id")
    void updatePV(Integer id);

    @Transactional
    @Modifying
    @Query("update ArticleTag set collectNum=collectNum+:num where articleId=:id")
    void updateCollectNum(Integer id, Integer num);

    @Transactional
    @Modifying
    @Query("update ArticleTag set likeNum=likeNum+:num where articleId=:id")
    void updateLikeNum(Integer id, Integer num);

    @Transactional
    @Modifying
    @Query("update ArticleTag set pv=:pv,collectNum=:collectNum,likeNum=:likeNum where articleId=:id")
    void updatePVAndCollectNumAndLikeNum(Integer id, int pv, int collectNum,int likeNum);

    @Transactional
    @Modifying
    @Query("update ArticleTag set status=:status where articleId=:id")
    void updateStatus(Integer id, Byte status);

    List<ArticleTag> findByArticleId(Integer articleId);

    List<ArticleTag> findByArticleIdIn(Set<Integer> articleIds);

    @Query(value = "select count(id) as num, sum(pv) as pv, sum(collectNum) as collectNum, sum(likeNum) as likeNum from ArticleTag  where tagId=:tagId and status=1 ")
    Object countAndSumByTagId(int tagId);

}
