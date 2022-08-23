package com.example.demo.api.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.api.article.qo.ArticleQo;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.service.ArticleService;
import com.example.demo.api.home.service.HomeService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;

@Controller
@RequestMapping(value = "/common/home")
public class HomeController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "/home")
    public ModelAndView home(String platform) {
        return feedback(homeService.home(platform));
    }

    @RequestMapping(value = "/articles")
    public ModelAndView articles(String articleQo) {
        return feedback(articleService.articles(parseModel(articleQo, new ArticleQo()), AdminType.NONE,
                ArticleWO.getCommonListInstance()));
    }

}
