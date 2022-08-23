package com.example.demo.api.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.article.entity.ArticleConstants;
import com.example.demo.api.article.model.Article;
import com.example.demo.api.article.model.Tag;
import com.example.demo.api.article.qo.ArticleQo;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.qo.TagQo;
import com.example.demo.api.article.service.ArticleService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;

@Controller
@RequestMapping("/adm/article")
@RequiredAdminType(AdminType.ADMIN)
public class AdmArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/articles")
    @RequiredPermission({ AdminPermission.ARTICLE_EDIT })
    public ModelAndView articles(String articleQo) {
        return feedback(articleService.articles(parseModel(articleQo, new ArticleQo()), AdminType.ADMIN,
                ArticleWO.getAdminListInstance()));
    }

    @RequestMapping("/article")
    @RequiredPermission(AdminPermission.ARTICLE_EDIT)
    public ModelAndView article(Integer id) {
        return feedback(articleService.article(id));
    }

    @RequestMapping("/remove")
    @RequiredPermission(AdminPermission.ARTICLE_EDIT)
    public ModelAndView remove(Integer id) {
        articleService.removeArticle(id, AdminType.ADMIN);
        return feedback();
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(String article) {
        articleService.saveArticle(parseModel(article, new Article()));
        return feedback();
    }

    @RequestMapping("/update_status")
    @RequiredPermission(AdminPermission.ARTICLE_EDIT)
    public ModelAndView updateStatus(Integer id, Byte status) {
        articleService.statusArticle(id, status, AdminType.ADMIN);
        return feedback();
    }

    @RequestMapping("/tag_types")
    @RequiredPermission(AdminPermission.TAG_EDIT)
    public ModelAndView tagTypes() {
        return feedback(ArticleConstants.TAGTYPES);
    }

    @RequestMapping("/tags")
    @RequiredPermission(AdminPermission.TAG_EDIT)
    public ModelAndView tags(String tagQo) {
        return feedback(articleService.tags(parseModel(tagQo, new TagQo())));
    }

    @RequestMapping("/save_tag")
    @RequiredPermission(AdminPermission.TAG_EDIT)
    public ModelAndView saveTag(String tag) {
        articleService.saveTag(parseModel(tag, new Tag()));
        return feedback();
    }

    @RequestMapping("/remove_tag")
    @RequiredPermission(AdminPermission.TAG_EDIT)
    public ModelAndView removeTag(Integer id) {
        articleService.removeTag(id);
        return feedback();
    }

}
