package com.example.demo.api.merchant.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.admin.repository.RoleRepository;
import com.example.demo.api.common.service.CommonService;
import com.example.demo.api.merchant.authority.MerchantAdminContexts;
import com.example.demo.api.merchant.authority.MerchantAdminSessionWrapper;
import com.example.demo.api.merchant.entity.MerchantPermissionVO;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.model.MerchantAdminSession;
import com.example.demo.api.merchant.model.MerchantRole;
import com.example.demo.api.merchant.qo.MerchantAdminQo;
import com.example.demo.api.merchant.qo.MerchantAdminSessionQo;
import com.example.demo.api.merchant.qo.MerchantRoleQo;
import com.example.demo.api.merchant.repository.MerchantAdminRepository;
import com.example.demo.api.merchant.repository.MerchantAdminSessionRepository;
import com.example.demo.api.merchant.repository.MerchantRepository;
import com.example.demo.api.merchant.repository.MerchantRoleRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.DateUtils;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MerchantAdminServiceImpl implements MerchantAdminService, AdminError {
    @Value("${admin.salt}")
    private String salt;
    @Value("${admin.session-hours}")
    private Integer sessionHours;
    @Autowired
    private MerchantRoleRepository merchantRoleRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private MerchantAdminRepository merchantAdminRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MerchantAdminSessionRepository merchantAdminSessionRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<String, MerchantAdminSessionWrapper> sessionCache;

    @PostConstruct
    public void init() {
        sessionCache = kvCacheFactory.create(
                new CacheOptions("merchantAdmin_session", 1, sessionHours * DateUtils.SECOND_PER_HOUR),
                new RepositoryProvider<String, MerchantAdminSessionWrapper>() {

                    @Override
                    public MerchantAdminSessionWrapper findByKey(String key) throws ServiceException {
                        MerchantAdminSession merchantAdminSession = merchantAdminSessionRepository.findByToken(key);
                        if (merchantAdminSession == null
                                || merchantAdminSession.getExpireAt() < System.currentTimeMillis()) {
                            throw new SessionServiceException();
                        }
                        MerchantAdmin merchantAdmin = merchantAdminRepository
                                .findById(merchantAdminSession.getAdminId()).orElse(null);
                        wrapMerchantAdmin(merchantAdmin);
                        Merchant merchant = merchantRepository.findById(merchantAdmin.getMerchantId()).orElse(null);
                        return new MerchantAdminSessionWrapper(merchantAdmin, merchantAdminSession,merchant);
                    }

                    @Override
                    public Map<String, MerchantAdminSessionWrapper> findByKeys(Collection<String> ids)
                            throws ServiceException {
                        throw new UnsupportedOperationException("findByKeys");
                    }

                }, new BeanModelConverter<>(MerchantAdminSessionWrapper.class));
    }

    @Override
    public void saveRole(MerchantRole role, AdminType type) {
        if (type == AdminType.MERCHANT) {
            role.setMerchantId(MerchantAdminContexts.requestMerchantId());
        }
        merchantRoleRepository.save(role);
    }

    private void wrapMerchantAdmin(MerchantAdmin merchantAdmin) {
        if (merchantAdmin != null) {
            merchantAdmin.setMerchantRole(merchantRole(merchantAdmin.getMerchantRoleId()));
        }
    }

    @Override
    public void saveMerchantAdmin(MerchantAdmin merchantAdmin, AdminType adminType) {
        if (StringUtils.isEmpty(merchantAdmin.getName())) {
            throw new ServiceException(ERR_NAME_EMPTY);
        }
        if (StringUtils.isNotChinaMobile(merchantAdmin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }
        if (merchantAdmin.getMerchantRoleId() != null) {
            if (!merchantRoleRepository.existsById(merchantAdmin.getMerchantRoleId())) {
                throw new ArgumentServiceException("merchantRoleId");
            }
        }
        Integer merchantId = merchantAdmin.getMerchantId();
        if (adminType == AdminType.MERCHANT) {
            merchantId = MerchantAdminContexts.requestMerchantId();
        } else {
            if (merchantId == null || merchantId == 0) {
                throw new ArgumentServiceException("merchantId");
            }
        }

        MerchantAdmin exist = merchantAdminRepository
                .findByMerchantIdAndMobile(merchantId, merchantAdmin.getMobile());
        if (merchantAdmin.getId() != null && merchantAdmin.getId() > 0) {
            if (StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_NOT_FOUNT);
            }
            exist.setName(merchantAdmin.getName());
            exist.setMerchantRoleId(merchantAdmin.getMerchantRoleId());
            exist.setImg(merchantAdmin.getImg());
            if (StringUtils.isNotEmpty(merchantAdmin.getPassword())) {
                if (StringUtils.validateStrongPassword(merchantAdmin.getPassword()) == null) {
                    throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
                }
                exist.setPassword(StringUtils.encryptPassword(merchantAdmin.getPassword(), salt));
            }
            merchantAdminRepository.save(exist);
        } else {

            if (!StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_EXIST);
            }
            if (StringUtils.validateStrongPassword(merchantAdmin.getPassword()) == null) {
                throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
            }
            merchantAdmin.setMerchantId(merchantId);
            merchantAdmin.setPassword(StringUtils.encryptPassword(merchantAdmin.getPassword(), salt));
            merchantAdmin.setStatus(Constants.STATUS_OK);
            merchantAdmin.setCreatedAt(System.currentTimeMillis());
            merchantAdminRepository.save(merchantAdmin);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> signin(MerchantAdmin merchantAdmin, ValCode valCode) {

        ValCode vCode = commonService.getValCode(valCode.getKey());

        if (!vCode.getCode().equals(valCode.getCode())) {
            throw new ServiceException(ErrorCode.ERR_VALCODE);
        }

        if (StringUtils.isNotChinaMobile(merchantAdmin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }

        MerchantAdmin exist = merchantAdminRepository.findByMerchantIdAndMobile(merchantAdmin.getMerchantId(),
                merchantAdmin.getMobile());
        if (exist == null) {
            throw new ServiceException(ERR_ACCOUNT_NOT_EXIST);
        }

        if (exist.getStatus() != Constants.STATUS_OK) {
            throw new ServiceException(ERR_ACCOUNT_FORBIDDEN);
        }

        MerchantAdminSession session = createMerchantAdminSession(exist);
        exist.setSigninAt(System.currentTimeMillis());
        merchantAdminRepository.save(exist);
        return CollectionUtils.arrayAsMap("merchantAdmin", exist, "merchantAdminSession", session, "merchantRole",
                merchantRole(exist.getMerchantRoleId()));

    }

    @Override
    public MerchantRole merchantRole(int id) {
        return merchantRoleRepository.findById(id).orElse(null);
    }

    private MerchantAdminSession createMerchantAdminSession(MerchantAdmin merchantAdmin) {
        long now = System.currentTimeMillis();
        MerchantAdminSession session = new MerchantAdminSession();
        session.setId(merchantAdmin.getId());
        session.setToken(StringUtils.randomAlphanumeric(64));
        session.setSigninAt(now);
        session.setExpireAt(now + sessionHours * DateUtils.MILLIS_PER_HOUR);
        session.setAdminId(merchantAdmin.getId());
        merchantAdminSessionRepository.save(session);
        return session;
    }

    @Override
    public MerchantAdminSessionWrapper findByToken(String token) {
        return sessionCache.findByKey(token);
    }

    @Override
    public Map<String, Object> profile() {
        Integer merchantAdminId = MerchantAdminContexts.requestMerchantAdminId();
        MerchantAdmin merchantAdmin = merchantAdmin(merchantAdminId, true);
        return CollectionUtils.arrayAsMap("merchantAdmin", merchantAdmin);
    }

    @Override
    public MerchantAdmin merchantAdmin(int id, boolean init) {
        MerchantAdmin merchantAdmin = findById(id);
        if (init) {
            wrapMerchantAdmin(merchantAdmin);
        }
        return merchantAdmin;
    }

    private MerchantAdmin findById(Integer id) {
        MerchantAdmin merchantAdmin = merchantAdminRepository.findById(id).orElse(null);
        if (StringUtils.isNull(merchantAdmin)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return merchantAdmin;
    }

    @Override
    public List<MerchantAdmin> admins(MerchantAdminQo qo, AdminType adminType) {
        if (adminType == AdminType.MERCHANT) {
            qo.setMerchantId(MerchantAdminContexts.requestMerchantId());
        }
        List<MerchantAdmin> merchantAdmins = merchantAdminRepository.findAll(qo);
        List<MerchantRole> merchantRoles = merchantRoles(false);

        for (MerchantAdmin merchantAdmin : merchantAdmins) {
            for (MerchantRole merchantRole : merchantRoles) {
                if (merchantAdmin.getMerchantRoleId().intValue() == merchantRole.getId()) {
                    merchantAdmin.setMerchantRole(merchantRole);
                }
            }
        }
        return merchantAdmins;
    }


    public List<MerchantRole> merchantRoles(boolean init) {
        List<MerchantRole> merchantRoles = merchantRoleRepository.findAll();
        if (init) {
            for (MerchantRole r : merchantRoles) {
                r.setPstr(MerchantPermissionVO.initAdmPermissionsByPs(r.getPermissions()));
            }
        }
        return merchantRoles;
    }

    @Override
    public List<MerchantRole> roles(MerchantRoleQo qo, AdminType adminType) {
        if (adminType == AdminType.MERCHANT) {
            qo.setMerchantId(MerchantAdminContexts.requestMerchantId());
        }

        List<MerchantRole> roles = merchantRoleRepository.findAll(qo);
        for (MerchantRole r : roles) {
            r.setPstr(MerchantPermissionVO.initAdmPermissionsByPs(r.getPermissions()));
        }
        return roles;
    }

    @Override
    public Page<MerchantAdminSession> merchantAdminSessions(MerchantAdminSessionQo qo) {
        return merchantAdminSessionRepository.findAll(qo);
    }

}
