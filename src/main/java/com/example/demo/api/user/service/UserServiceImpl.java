package com.example.demo.api.user.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.api.common.entity.ValCodeConstants;
import com.example.demo.api.common.service.CommonService;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.authority.UserSessionWrapper;
import com.example.demo.api.user.entity.UserConfig;
import com.example.demo.api.user.model.Shopping;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.model.UserSession;
import com.example.demo.api.user.qo.ShoppingQo;
import com.example.demo.api.user.qo.UserQo;
import com.example.demo.api.user.repository.ShoppingRepository;
import com.example.demo.api.user.repository.UserRepository;
import com.example.demo.api.user.repository.UserSessionRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.context.Context;
import com.example.demo.common.context.Contexts;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.DateUtils;
import com.example.demo.common.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Value("${user.salt}")
    private String salt;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    
    @Autowired
    private CommonService commonService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, User> userCache;
    private KvCacheWrap<String, UserSessionWrapper> sessionCache;

    @Autowired
    private UserConfig userConfig;

    @PostConstruct
    public void init() {
        userCache = kvCacheFactory.create(new CacheOptions("user", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, User>() {
                    @Override
                    public User findByKey(Integer key) throws ServiceException {
                        return getById(key);
                    }

                    @Override
                    public Map<Integer, User> findByKeys(Collection<Integer> ids) throws ServiceException {
                        List<User> list = userRepository.findAllById(ids);
                        return list.stream().collect(Collectors.toMap(User::getId, m -> m));
                    }
                }, new BeanModelConverter<>(User.class));

        sessionCache = kvCacheFactory.create(
                new CacheOptions("user_session", 1, userConfig.getSessionDays() * DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<String, UserSessionWrapper>() {

                    @Override
                    public UserSessionWrapper findByKey(String key) throws ServiceException {
                        UserSession session = userSessionRepository.findByToken(key);
                        if (session == null || session.getExpireAt() < System.currentTimeMillis()) {
                            return null;
                        }
                        User user = userRepository.findById(session.getUserId()).orElse(null);
                        return new UserSessionWrapper(user, session);
                    }

                    @Override
                    public Map<String, UserSessionWrapper> findByKeys(Collection<String> ids) throws ServiceException {
                        throw new UnsupportedOperationException("findByKeys");
                    }

                }, new BeanModelConverter<>(UserSessionWrapper.class));
    }

    @Override
    public Map<Integer, User> findByIdIn(Set<Integer> ids) {
        return userCache.findByKeys(ids);
    }

    @Override
    public Page<User> users(UserQo qo, AdminType adminType) {

        if (adminType != AdminType.ADMIN) {
            qo.setStatus(Constants.STATUS_OK);
        }

        return userRepository.findAll(qo);
    }

    @Override
    public void signup(User user, ValCode valCode) {
        validRegUser(user, valCode);
        user.setCreatedAt(System.currentTimeMillis());
        userRepository.save(user);
    }

    private void validRegUser(User user, ValCode valCode) {

        if (Objects.isNull(user) || Objects.isNull(valCode)) {
            throw new ArgumentServiceException("user|valCode");
        }
        ValCode vc = commonService.getValCode(valCode.getKey());

        if (!(valCode.getUserType().intValue() == vc.getUserType()
                && valCode.getAccountType().intValue() == vc.getAccountType()
                && valCode.getAccount().equals(vc.getAccount()) && valCode.getCode().equals(vc.getCode()))) {
            throw new ServiceException(ErrorCode.ERR_VALCODE);
        }

        findByAccount(valCode, true);

        if (StringUtils.isEmpty(user.getName())) {
            throw new ArgumentServiceException("name");
        }

        if (user.getStatus() == null || user.getStatus() == 0) {
            user.setStatus(Constants.STATUS_OK);
        }

        user.setMobile(valCode.getAccount());
    }

    private User getById(Integer id) {
        if (id == null || id == 0) {
            throw new ArgumentServiceException("userId");
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User user(Integer id, AdminType adminType) {
        User user = userCache.findByKey(id);
        if (adminType != AdminType.ADMIN && user.getStatus() == Constants.STATUS_HALT) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return user;
    }

    @Override
    public void status(Integer id, byte status) {
        User user = getById(id);
        user.setStatus(status);
        userRepository.save(user);
        userCache.remove(id);
    }

    private User findByAccount(ValCode valCode, boolean negative) {
        User user = null;

        if (valCode.getAccountType() == ValCodeConstants.MOBILE) {
            user = userRepository.findByMobile(valCode.getAccount());
        }

        if (negative) {
            if (user != null) {
                throw new ServiceException(ErrorCode.ERR_ACCOUNT_EXIST);
            }
        } else {
            if (user == null) {
                throw new ServiceException(ErrorCode.ERR_ACCOUNT_NOT_EXIST);
            }
            if (user.getStatus() != Constants.STATUS_OK) {
                throw new ServiceException(ErrorCode.ERR_ACCOUNT_FORBIDDEN);
            }
        }
        return user;
    }

    @Override
    public List<User> getTopByFiledAndSize(String sortField, Integer size) {
        return userRepository.getSortedList(PageRequest.of(0, size, Sort.Direction.DESC, sortField));
    }

    @Override
    public UserSessionWrapper findByToken(String token) {
        return sessionCache.findByKey(token);
    }

    @Override
    @Transactional
    public UserSessionWrapper signin(ValCode valCode) {
        User exist = userRepository.findByMobile(valCode.getAccount());
        if (exist == null) {
            throw new ServiceException(ErrorCode.ERR_USER_NOT_FOUNT);
        }
        UserSessionWrapper wrapper = createUserSessionWrap(exist);
        exist.setSigninAt(System.currentTimeMillis());
        userRepository.save(exist);
        return wrapper;
    }

    private UserSessionWrapper createUserSessionWrap(User user) {

        String token = StringUtils.randomAlphanumeric(64);

        long now = System.currentTimeMillis();

        UserSession session = new UserSession();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setSigninAt(now);
        session.setExpireAt(now + userConfig.getSessionDays() * DateUtils.MILLIS_PER_DAY);
        userSessionRepository.save(session);

        UserSessionWrapper sessionWrapper = new UserSessionWrapper(user, session);

        Context context = Contexts.get();
        context.setSession(sessionWrapper);

        return sessionWrapper;

    }

    @Override
    public Map<String, Object> profile() {
        User user = new User();
        Integer userId = UserContexts.sessionUserId();
        if (userId != null) {
            user = user(userId, AdminType.USER);
        }
        return CollectionUtils.arrayAsMap("user", user);
    }

    @Override
    public void saveProfile(User user) {
        User exist = getById(UserContexts.requestUserId());
        exist.setName(user.getName());
        exist.setUserInfo(user.getUserInfo());
        userRepository.save(exist);
        userCache.remove(exist.getId());

    }

    @Override
    public void updateCollectNum(int userId, int num) {
        User user = getById(userId);
        user.setCollectNum(user.getCollectNum() + num);
        userRepository.save(user);
        userCache.remove(userId);
    }

    @Override
    public User user(int id) {
        return getById(id);
    }

   

}