package com.example.demo.api.article.controller;

import com.example.demo.api.article.entity.ArticleConstants;
import com.example.demo.api.article.qo.ArticleQo;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.qo.TagQo;
import com.example.demo.api.article.service.ArticleService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.Touchable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/usr/article")
@RequiredAdminType(AdminType.USER)
public class UsrArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/articles")
    public ModelAndView articles(String articleQo) {
        return feedback(articleService.articles(parseModel(articleQo, new ArticleQo()), AdminType.USER,
                ArticleWO.getAuthorListInstance()));
    }

    @RequestMapping(value = "/article")
    public ModelAndView article(Integer id) {
        return feedback(articleService.article(id));
    }

    @RequestMapping(value = "/home_article")
    @Touchable(true)
    public ModelAndView homeArticle(Integer id) {
        return feedback(articleService.article(id, ArticleWO.getCommonDetailInstance()));
    }

    // @RequestMapping(value = "/save")
    // public ModelAndView save(String article) {
    //     articleService.saveArticle(parseModel(article, new Article()));
    //     return feedback(null);
    // }

    @RequestMapping(value = "/status")
    public ModelAndView status(Integer id, Byte status) {
        articleService.statusArticle(id, status, AdminType.USER);
        return feedback(null);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(Integer id) {
        articleService.removeArticle(id, AdminType.USER);
        return feedback(null);
    }

    @RequestMapping("/tag_types")
    public ModelAndView tagTypes() {
        return feedback(ArticleConstants.TAGTYPES);
    }

    @RequestMapping("/tags")
    public ModelAndView tags(String tagQo) {
        return feedback(articleService.tags(parseModel(tagQo, new TagQo())));
    }

    @RequestMapping("/article_tags")
    public ModelAndView articleTags(Integer articleId) {
        return feedback(articleService.articleTags(articleId));
    }

    @RequestMapping("/save_article_tag")
    public ModelAndView saveArticleTag(Integer articleId, Integer tagId) {
        return feedback(articleService.saveArticleTag(articleId, tagId));
    }

    @RequestMapping("/remove_article_tag")
    public ModelAndView removeArticleTag(Integer id) {
        articleService.removeArticleTag(id);
        return feedback();
    }

}
