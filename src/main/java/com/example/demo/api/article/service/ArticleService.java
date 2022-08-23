package com.example.demo.api.article.service;

import com.example.demo.api.article.model.Article;
import com.example.demo.api.article.model.ArticleTag;
import com.example.demo.api.article.model.Tag;
import com.example.demo.api.article.qo.ArticleQo;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.qo.TagQo;
import com.example.demo.common.controller.auth.AdminType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleService {

    Page<Article> articles(ArticleQo qo, AdminType adminType, ArticleWO wo);

    Article article(Integer id);

    Article article(Integer id, ArticleWO wo);

    void statusArticle(Integer id, byte status, AdminType adminType);

    Map<Integer, Article> findByIdIn(Set<Integer> ids);

    void saveArticle(Article article);

    void removeArticle(Integer id, AdminType adminType);

    List<Article> getTopByFiledAndSize(String sortField, Integer size, ArticleWO wo);

    void updateCollectNum(int articleId, int num);

    // tag
    List<Tag> tags(TagQo qo);

    List<Tag> tagsAll();

    void saveTag(Tag tag);

    void removeTag(int id);

    // articleTag
    List<ArticleTag> articleTags(int articleId);

    ArticleTag saveArticleTag(int articleId, int tagId);

    void removeArticleTag(int id);

    void syncTagWeight();
}
