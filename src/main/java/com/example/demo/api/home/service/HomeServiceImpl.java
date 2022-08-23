package com.example.demo.api.home.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.ObjectConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.api.article.entity.ArticleConstants;
import com.example.demo.api.article.model.Article;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.service.ArticleService;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.service.UserService;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.KeyValue;
import com.example.demo.common.util.CollectionUtils;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<String, Object> homeCache;


    @PostConstruct
    public void init() {
        homeCache = kvCacheFactory.create(new CacheOptions("home", 4, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<String, Object>() {
                    @Override
                    public Object findByKey(String key) throws ServiceException {
                        return initHome(key);
                    }

                    @Override
                    public Map<String, Object> findByKeys(Collection<String> ids) throws ServiceException {
                        throw new UnsupportedOperationException("findByKeys");
                    }
                }, new ObjectConverter());

    }

    private Map initHome(String platform) {

        ArticleWO aWO = ArticleWO.getCommonListInstance();

        List<Article> pvArticles = articleService.getTopByFiledAndSize("pv", 10, aWO);
        List<Article> commentArticles = articleService.getTopByFiledAndSize("comment_num", 10, aWO);
        List<Article> latestArticles = articleService.getTopByFiledAndSize("last_modified", 10, aWO);

        List<KeyValue> tagTypes = ArticleConstants.TAGTYPES;

        List<User> _latestUsers = userService.getTopByFiledAndSize("created_at", 3);

        List<Map> latestUsers = new ArrayList<>(3);
        for (User user : _latestUsers) {
            latestUsers.add(user.getBriefInfo());
        }

        return CollectionUtils.arrayAsMap("pvArticles", pvArticles, "commentArticles", commentArticles,
                "latestArticles", latestArticles, "tagTypes", tagTypes, "latestUsers", latestUsers);
    }

    @Override
    public Object home(String platform) {
        return homeCache.findByKey(platform);
    }

}