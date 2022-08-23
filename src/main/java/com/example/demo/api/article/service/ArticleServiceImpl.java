package com.example.demo.api.article.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.demo.api.article.converter.TagMapConverter;
import com.example.demo.api.article.model.Article;
import com.example.demo.api.article.model.ArticleTag;
import com.example.demo.api.article.model.CountSumVO;
import com.example.demo.api.article.model.Tag;
import com.example.demo.api.article.qo.ArticleQo;
import com.example.demo.api.article.qo.ArticleWO;
import com.example.demo.api.article.qo.TagQo;
import com.example.demo.api.article.repository.ArticleRepository;
import com.example.demo.api.article.repository.ArticleTagRepository;
import com.example.demo.api.article.repository.TagRepository;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.service.UserCollectService;
import com.example.demo.api.user.service.UserService;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;
import com.sunnysuperman.kvcache.converter.ListModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCollectService userCollectService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, Article> articleCache;

    private KvCacheWrap<Integer, List<Tag>> tagAllCache;

    private KvCacheWrap<Integer, Map<Integer, Tag>> tagMapCache;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        articleCache = kvCacheFactory.create(new CacheOptions("article", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Article>() {
                    @Override
                    public Article findByKey(Integer key) throws ServiceException {
                        return getById(key);
                    }

                    @Override
                    public Map<Integer, Article> findByKeys(Collection<Integer> ids) throws ServiceException {
                        List<Article> list = articleRepository.findByIdIn(ids);
                        return list.stream().collect(Collectors.toMap(Article::getId, m -> m));
                    }
                }, new BeanModelConverter<>(Article.class));

        tagAllCache = kvCacheFactory.create(new CacheOptions("tag_all", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, List<Tag>>() {

                    @Override
                    public List<Tag> findByKey(Integer id) throws ServiceException {
                        return tagRepository.findAll();
                    }

                    @Override
                    public Map<Integer, List<Tag>> findByKeys(Collection<Integer> ids) throws ServiceException {
                        throw new UnsupportedOperationException("findByKeys");
                    }
                }, new ListModelConverter<>(Tag.class));

        tagMapCache = kvCacheFactory.create(new CacheOptions("tag_map", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Map<Integer, Tag>>() {

                    @Override
                    public Map<Integer, Tag> findByKey(Integer id) throws ServiceException {
                        List<Tag> tags = tagRepository.findAll();
                        return tags.stream().collect(Collectors.toMap(Tag::getId, item -> item));
                    }

                    @Override
                    public Map<Integer, Map<Integer, Tag>> findByKeys(Collection<Integer> ids) throws ServiceException {
                        throw new UnsupportedOperationException("findByKeys");
                    }
                }, new TagMapConverter());
    }

    @Override
    public Map<Integer, Article> findByIdIn(Set<Integer> ids) {
        return articleCache.findByKeys(ids);
    }

    @Override
    public Page<Article> articles(ArticleQo qo, AdminType adminType, ArticleWO wo) {
        if (adminType == AdminType.USER) {
            qo.setUserId(UserContexts.requestUserId());
        } else if (adminType == AdminType.NONE) {
            qo.setStatus(Constants.STATUS_OK);
        }
        Page<Article> page = articleRepository.findAll(qo);
        // wrapArticle(page.getContent(), wo);
        return page;
    }

    @Override
    public void saveArticle(Article article) {

        validArticle(article);
        boolean update = article.getId() > 0;

        long currT = System.currentTimeMillis();
        article.setLastModified(currT);

        if (update) {
            Article exist = writableArticle(article.getId());
            exist.setTitle(article.getTitle());
            exist.setDescr(article.getDescr());
            exist.setCover(article.getCover());
            exist.setContent(article.getContent());
            exist.setType(article.getType());
            articleRepository.save(exist);
            articleCache.remove(article.getId());
        } else {
            article.setCreatedAt(currT);
            article.setPv(0);
            article.setCommentNum(0);
            article.setCollectNum(0);
            article.setLikeNum(0);
            article.setLastModified(System.currentTimeMillis());
            articleRepository.save(article);
        }

    }

    private void validArticle(Article article) {
        if (StringUtils.isEmpty(article.getTitle()) || article.getTitle().length() > 100) {
            throw new ServiceException(ErrorCode.ERR_ARTICLE_TITLE);
        }
        if (StringUtils.isEmpty(article.getContent())) {
            throw new ServiceException(ErrorCode.ERR_ARTICLE_TITLE);
        }
        if (article.getStatus() == null || article.getStatus() == 0) {
            article.setStatus(Constants.STATUS_HALT);
        }
    }

    private Article getById(Integer id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return article;
    }

    @Override
    public Article article(Integer id) {
        return getById(id);
    }

    @Override
    public Article article(Integer id, ArticleWO wo) {
        updatePV(id);
        Article article = getById(id);
        // wrapArticle(Collections.singletonList(article), wo);
        return article;
    }

    private void updatePV(Integer id) {
        articleRepository.updatePV(id);
        articleCache.remove(id);
        articleTagRepository.updatePV(id);
    }

    private Article writableArticle(Integer id) {
        Article article = getById(id);
        if (article.getUserId().intValue() != UserContexts.requestUserId()) {
            throw new ServiceException(ErrorCode.ERR_PERMISSION_DENIED);
        }
        return article;
    }

    // private void wrapArticle(List<Article> articles, ArticleWO wo) {
    //     Integer userId = UserContexts.sessionUserId();
    //     int size = articles.size();
    //     Set<Integer> articleIds = articles.stream().map(Article::getId).collect(Collectors.toSet());
    //     Map<Integer, User> userMap = new HashMap<>(size);
    //     Map<Integer, Tag> tagMap = new HashMap<>();
    //     Map<Integer, List<ArticleTag>> articleTagMap = new HashMap<>(size);
    //     Map<Integer, UserCollect> articleCollectMap = new HashMap<>();
    //     if (wo.isWithAuthor()) {
    //         Set<Integer> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
    //         userMap = userService.findByIdIn(userIds);
    //     }
    //     if (wo.isWithTags()) {
    //         tagMap = tagMapCache.findByKey(0);
    //         List<ArticleTag> articleTagList = articleTagRepository.findByArticleIdIn(articleIds);
    //         articleTagMap = articleTagList.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));
    //     }
    //     if (userId != null && userId > 0 && wo.isWithCollect()) {
    //         articleCollectMap = userCollectService.findByCollectIdIn(userId, UserConstants.COLLECT_TYPE_ARTICLE,
    //                 articleIds);
    //     }

    //     for (Article article : articles) {
    //         if (wo.isWithAuthor()) {
    //             User user = userMap.get(article.getUserId());
    //             if (user != null) {
    //                 article.setUser(user.getBriefInfo());
    //             }
    //         }
    //         if (wo.isWithTags()) {
    //             List<ArticleTag> articleTagList = articleTagMap.get(article.getId());
    //             if (CollectionUtils.isNotEmpty(articleTagList)) {
    //                 List<Tag> tags = new ArrayList<>(articleTagList.size());
    //                 for (ArticleTag articleTag : articleTagList) {

    //                     Tag mtag = tagMap.get(articleTag.getTagId());
    //                     if (!Objects.isNull(mtag)) {
    //                         tags.add(mtag);
    //                     }

    //                 }
    //                 article.setTags(tags);
    //             }
    //         }
    //         if (userId != null && userId > 0 && wo.isWithCollect()) {
    //             article.setIsCollected(
    //                     articleCollectMap.get(article.getId()) == null ? ByteUtils.BYTE_0 : ByteUtils.BYTE_1);
    //         }
    //         if (!wo.isWithContent()) {
    //             article.setContent(null);
    //             entityManager.detach(article);
    //         }
    //     }
    // }

    @Override
    public void statusArticle(Integer id, byte status, AdminType adminType) {
        Article article = null;
        if (adminType == AdminType.USER) {
            article = writableArticle(id);
        } else {
            article = getById(id);
        }
        article.setStatus(status);
        articleRepository.save(article);
        articleCache.remove(id);
        articleTagRepository.updateStatus(id, status);
    }

    @Override
    public void removeArticle(Integer id, AdminType adminType) {
        if (adminType == AdminType.USER) {
            writableArticle(id);
        }
        articleRepository.deleteById(id);
        articleCache.remove(id);
        articleTagRepository.deleteByArticleId(id);
    }

    @Override
    public List<Article> getTopByFiledAndSize(String sortField, Integer size, ArticleWO wo) {
        List<Article> list = articleRepository.getSortedList(PageRequest.of(0, size, Sort.Direction.DESC, sortField));
        // wrapArticle(list, wo);
        return list;
    }

    @Override
    public List<Tag> tags(TagQo qo) {
        return tagRepository.findAll(qo);
    }

    @Override
    public List<Tag> tagsAll() {
        return tagAllCache.findByKey(0);
    }

    private Tag getTag(int id) {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return tag;
    }

    @Override
    public void saveTag(Tag tag) {
        Integer id = tag.getId();
        if (id == null || id == 0) {
            tag.setCreatedAt(System.currentTimeMillis());
            tagRepository.save(tag);
        } else {
            Tag exist = getTag(id);
            exist.setTitle(tag.getTitle());
            tagRepository.save(exist);
        }
        tagMapCache.remove(0);
        tagAllCache.remove(0);
    }

    @Override
    @Transactional
    public void removeTag(int id) {
        tagRepository.deleteById(id);
        articleTagRepository.deleteByTagId(id);
        tagMapCache.remove(0);
        tagAllCache.remove(0);
    }

    @Override
    public List<ArticleTag> articleTags(int articleId) {
        List<ArticleTag> articleTags = articleTagRepository.findByArticleId(articleId);
        Map<Integer, Tag> map = tagMapCache.findByKey(0);
        for (ArticleTag articleTag : articleTags) {
            articleTag.setTag(map.get(articleTag.getTagId()));
        }
        return articleTags;
    }

    @Override
    public ArticleTag saveArticleTag(int articleId, int tagId) {
        Article article = writableArticle(articleId);
        ArticleTag articleTag = new ArticleTag(articleId, tagId, article.getPv(), article.getCollectNum(),
                article.getLikeNum(), article.getStatus());
        articleTagRepository.save(articleTag);
        return articleTag;
    }

    @Override
    public void removeArticleTag(int id) {
        articleTagRepository.deleteById(id);
    }

    @Override
    public void syncTagWeight() {

        List<Tag> tags = tags(new TagQo());
        for (Tag tag : tags) {
            Object obj = articleTagRepository.countAndSumByTagId(tag.getId());
            if (obj == null) {
                continue;
            }
            CountSumVO vo = new CountSumVO().parseObject(obj);
            System.out.println(vo.toString());

            tag.setWeight(vo.getWeight());
            tagRepository.save(tag);
        }
        tagAllCache.remove(0);
    }

    @Override
    public void updateCollectNum(int articleId, int num) {
        Article article = getById(articleId);
        article.setCollectNum(article.getCollectNum() + num);
        articleRepository.save(article);
        articleCache.remove(articleId);
    }

}