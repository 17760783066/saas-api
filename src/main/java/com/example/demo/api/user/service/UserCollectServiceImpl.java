package com.example.demo.api.user.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.api.article.model.Article;
import com.example.demo.api.article.service.ArticleService;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.entity.UserConstants;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.model.UserCollect;
import com.example.demo.api.user.qo.UserCollectQo;
import com.example.demo.api.user.repository.UserCollectRepository;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.ByteUtils;
import com.example.demo.common.util.CollectionUtils;

@Service
public class UserCollectServiceImpl implements UserCollectService {

    @Autowired
    private UserCollectRepository userCollectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public int collect(byte collectType, int collectId) {

        if (!UserConstants.COLLECTTYPES.contains(collectType)) {
            throw new ArgumentServiceException("collectType");
        }

        Integer userId = UserContexts.requestUserId();

        UserCollect exist = userCollectRepository.findByUserIdAndCollectTypeAndCollectId(userId, collectType,
                collectId);

        int id = 0;
        int num = 0;
        if (exist == null) {
            exist = new UserCollect(userId, collectType, collectId, System.currentTimeMillis());
            id = userCollectRepository.save(exist).getId();
            num = 1;
        } else {
            userCollectRepository.delete(exist);
            num = -1;
        }

        if (collectType == UserConstants.COLLECT_TYPE_USER) {
            userService.updateCollectNum(collectId, num);
        } else if (collectType == UserConstants.COLLECT_TYPE_ARTICLE) {
            articleService.updateCollectNum(collectId, num);
        }

        return id;
    }

    @Override
    public byte isCollected(byte collectType, int collectId) {
        Integer userId = UserContexts.requestUserId();
        UserCollect exist = userCollectRepository.findByUserIdAndCollectTypeAndCollectId(userId, collectType,
                collectId);
        return exist == null ? ByteUtils.BYTE_0 : ByteUtils.BYTE_1;
    }

    @Override
    public Map<Integer, UserCollect> findByCollectIdIn(int userId, byte collectType, Collection<Integer> ids) {
        List<UserCollect> list = userCollectRepository.findByUserIdAndCollectTypeAndCollectIdIn(userId, collectType,
                ids);

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        return list.stream().collect(Collectors.toMap(UserCollect::getCollectId, m -> m));
    }

    @Override
    public Page<UserCollect> userCollects(UserCollectQo qo) {

        qo.setUserId(UserContexts.requestUserId());

        if (!UserConstants.COLLECTTYPES.contains(qo.getCollectType())) {
            throw new ArgumentServiceException("collectType");
        }

        Page<UserCollect> page = userCollectRepository.findAll(qo);

        List<UserCollect> list = page.getContent();
        if (CollectionUtils.isNotEmpty(list)) {
            Map<Integer, User> authors = new HashMap<>(list.size());
            Map<Integer, Article> articles = new HashMap<>(list.size());

            boolean isAuthor = qo.getCollectType().byteValue() == UserConstants.COLLECT_TYPE_USER;
            boolean isArticle = qo.getCollectType().byteValue() == UserConstants.COLLECT_TYPE_ARTICLE;

            Set<Integer> collectIds = list.stream().map(UserCollect::getCollectId).collect(Collectors.toSet());

            if (isAuthor) {
                authors = userService.findByIdIn(collectIds);
            } else if (isArticle) {
                articles = articleService.findByIdIn(collectIds);
            }

            for (UserCollect item : list) {
                if (isAuthor) {
                    item.setAuthor(authors.get(item.getCollectId()));
                } else if (isArticle) {
                    item.setArticle(articles.get(item.getCollectId()));
                }
            }

        }
        return page;
    }

    private UserCollect findById(Integer id) {
        if (id == null || id == 0) {
            throw new ArgumentServiceException("id");
        }
        return userCollectRepository.findById(id).orElse(null);
    }

    private UserCollect getById(int id) {
        UserCollect userCollect = findById(id);
        if (userCollect == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return userCollect;
    }

    private UserCollect writableUserCollect(int id) {
        UserCollect userCollect = getById(id);
        if (userCollect.getUserId().intValue() != UserContexts.requestUserId()) {
            throw new ServiceException(ErrorCode.ERR_PERMISSION_DENIED);
        }
        return userCollect;
    }

    @Override
    @Transactional
    public void removeMyCollect(int id) {

        UserCollect userCollect = writableUserCollect(id);

        byte collectType = userCollect.getCollectType();
        int collectId = userCollect.getCollectId();

        if (collectType == UserConstants.COLLECT_TYPE_USER) {
            userService.updateCollectNum(collectId, -1);
        } else if (collectType == UserConstants.COLLECT_TYPE_ARTICLE) {
            articleService.updateCollectNum(collectId, -1);
        }

        userCollectRepository.deleteById(id);
    }

}