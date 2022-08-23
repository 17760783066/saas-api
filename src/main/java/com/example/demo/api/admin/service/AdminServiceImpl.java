package com.example.demo.api.admin.service;

import com.example.demo.api.admin.authority.AdminContexts;
import com.example.demo.api.admin.authority.AdminSessionWrapper;
import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.admin.entity.AdminPermissionVO;
import com.example.demo.api.admin.model.*;
import com.example.demo.api.admin.qo.AdminQo;
import com.example.demo.api.admin.qo.AdminSessionQo;
import com.example.demo.api.admin.repository.AdminRepository;
import com.example.demo.api.admin.repository.AdminSessionRepository;
import com.example.demo.api.admin.repository.RoleRepository;
import com.example.demo.api.common.entity.ValCodeConstants;
import com.example.demo.api.common.service.CommonService;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService, AdminError {
    @Value("${admin.salt}")
    private String salt;
    @Value("${admin.session-hours}")
    private Integer sessionHours;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminSessionRepository adminSessionRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<String, AdminSessionWrapper> sessionCache;

    private KvCacheWrap<Integer, Admin> adminCache;

    @PostConstruct
    public void init() {
        sessionCache = kvCacheFactory.create(
                new CacheOptions("admin_session", 1, sessionHours * DateUtils.SECOND_PER_HOUR),
                new RepositoryProvider<String, AdminSessionWrapper>() {

                    @Override
                    public AdminSessionWrapper findByKey(String key) throws ServiceException {
                        AdminSession session = adminSessionRepository.findByToken(key);
                        if (session == null || session.getExpireAt() < System.currentTimeMillis()) {
                            throw new SessionServiceException();
                        }
                        Admin admin = adminRepository.findById(session.getAdminId()).orElse(null);
                        wrapAdmin(admin);
                        return new AdminSessionWrapper(admin, session);
                    }

                    @Override
                    public Map<String, AdminSessionWrapper> findByKeys(Collection<String> ids) throws ServiceException {

                        throw new UnsupportedOperationException("findByKeys");
                    }

                }, new BeanModelConverter<>(AdminSessionWrapper.class));

        adminCache = kvCacheFactory.create(new CacheOptions("admin", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Admin>() {
                    @Override
                    public Admin findByKey(Integer key) throws ServiceException {
                        return getById(key);
                    }

                    @Override
                    public Map<Integer, Admin> findByKeys(Collection<Integer> keys) throws ServiceException {

                        keys = keys.stream().filter(a -> a != null && a > 0).collect(Collectors.toSet());
                        if (CollectionUtils.isEmpty(keys)) {
                            return Collections.emptyMap();
                        }

                        List<Admin> list = adminRepository.findAllById(keys);
                        return list.stream().collect(Collectors.toMap(Admin::getId, m -> m));
                    }
                }, new BeanModelConverter<>(Admin.class));

    }

    private Admin getById(Integer id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return admin;
    }
    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void removeRole(int id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role role(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> roles(boolean init) {
        List<Role> roles = roleRepository.findAll();
        if (init) {
            for (Role r : roles) {
                r.setPstr(AdminPermissionVO.initAdmPermissionsByPs(r.getPermissions()));
            }
        }
        return roles;
    }

    @Override
    public List<Admin> admins(AdminQo qo) {

        List<Admin> admins = adminRepository.findAll(qo);
        List<Role> roles = roles(false);

        for (Admin admin : admins) {
            for (Role role : roles) {
                if (admin.getRoleId().intValue() == role.getId()) {
                    admin.setRole(role);
                }
            }
        }
        return admins;
    }

    @Override
    public void saveAdmin(Admin admin) throws ServiceException {
        if (StringUtils.isEmpty(admin.getName())) {
            throw new ServiceException(ERR_NAME_EMPTY);
        }
        if (StringUtils.isNotChinaMobile(admin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }
        if (!roleRepository.existsById(admin.getRoleId())) {
            throw new ArgumentServiceException("roleId");
        }
        Admin exist = adminRepository.findByMobile(admin.getMobile());
        if (admin.getId() != null && admin.getId() > 0) {
            if (StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_NOT_FOUNT);
            }
            exist.setName(admin.getName());
            exist.setRoleId(admin.getRoleId());
            exist.setImg(admin.getImg());
            if (StringUtils.isNotEmpty(admin.getPassword())) {
                if (StringUtils.validateStrongPassword(admin.getPassword()) == null) {
                    throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
                }
                exist.setPassword(StringUtils.encryptPassword(admin.getPassword(), salt));
            }
            adminRepository.save(exist);
        } else {
            if (!StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_EXIST);
            }
            if (StringUtils.validateStrongPassword(admin.getPassword()) == null) {
                throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
            }
            admin.setPassword(StringUtils.encryptPassword(admin.getPassword(), salt));
            admin.setStatus(Constants.STATUS_OK);
            admin.setCreatedAt(System.currentTimeMillis());
            adminRepository.save(admin);
        }
    }

    @Override
    public void adminStatus(int id, byte status) throws ServiceException {
        Admin admin = findById(id);
        admin.setStatus(status);
        adminRepository.save(admin);
    }

    private Admin findById(Integer id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (StringUtils.isNull(admin)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return admin;
    }

    @Override
    public Admin admin(int id, boolean init) {
        Admin admin = findById(id);
        if (init) {
            wrapAdmin(admin);
        }
        return admin;
    }

    @Override
    public void removeAdmin(int id) throws ServiceException {
        adminRepository.deleteById(id);
    }

    @Override
    public Page<AdminSession> adminSessions(AdminSessionQo qo) {
        return adminSessionRepository.findAll(qo);
    }

    @Override
    public void updatePassword(String password, String repeatPassword, String oldPassword) throws ServiceException {

        int adminId = AdminContexts.requestAdminId();

        Admin admin = findById(adminId);
        if (!admin.getPassword().equals(StringUtils.encryptPassword(oldPassword, salt))) {
            throw new ServiceException(ERR_WORN_PASSWORD_INCONSISTENT);
        }
        if (!password.equals(repeatPassword)) {
            throw new ServiceException(ERR_PASSWORD_INCONSISTENT);
        }
        admin.setPassword(StringUtils.encryptPassword(password, salt));

        adminRepository.save(admin);
    }

    private void wrapAdmin(Admin admin) {
        if (admin != null) {
            admin.setRole(role(admin.getRoleId()));
        }
    }

    @Override
    @Transactional
    public Map<String, Object> signin(Admin admin, ValCode valCode) {

        ValCode vCode = commonService.getValCode(valCode.getKey());

        if (!vCode.getCode().equals(valCode.getCode())) {
            throw new ServiceException(ErrorCode.ERR_VALCODE);
        }

        if (StringUtils.isNotChinaMobile(admin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }

        Admin exist = adminRepository.findByMobile(admin.getMobile());
        if (exist == null) {
            throw new ServiceException(ERR_ACCOUNT_NOT_EXIST);
        }

        if (StringUtils.isEmpty(admin.getPassword()) || !exist.getPassword().equals(StringUtils.encryptPassword(admin
                .getPassword(), salt))) {
            throw new ServiceException(ERR_PASSWORD_INVALID);
        }

        if (exist.getStatus() != Constants.STATUS_OK) {
            throw new ServiceException(ERR_ACCOUNT_FORBIDDEN);
        }

        AdminSession session = createAdminSession(exist);
        exist.setSigninAt(System.currentTimeMillis());
        adminRepository.save(exist);
        return CollectionUtils.arrayAsMap("admin", exist, "session", session, "role", role(exist.getRoleId()));

    }

    private AdminSession createAdminSession(Admin admin) {
        long now = System.currentTimeMillis();
        AdminSession session = new AdminSession();
        session.setAdminId(admin.getId());
        session.setToken(StringUtils.randomAlphanumeric(64));
        session.setSigninAt(now);
        session.setExpireAt(now + sessionHours * DateUtils.MILLIS_PER_HOUR);
        adminSessionRepository.save(session);
        return session;
    }

    @Override
    public AdminSessionWrapper findByToken(String token) {
        return sessionCache.findByKey(token);
    }

    @Override
    public Admin findByAccount(ValCode valCode) {
        Admin admin = null;

        if (valCode.getAccountType() == ValCodeConstants.MOBILE) {
            admin = adminRepository.findByMobile(valCode.getAccount());
        }
        if (admin == null) {
            throw new ServiceException(ERR_ACCOUNT_NOT_EXIST);
        }
        if (admin.getStatus() != Constants.STATUS_OK) {
            throw new ServiceException(ERR_ACCOUNT_FORBIDDEN);
        }
        return admin;
    }

    @Override
    public void resetPassword(ValCode valCode, String password) {
        Admin admin = findByAccount(valCode);
        admin.setPassword(StringUtils.encryptPassword(password, salt));
        adminRepository.save(admin);
    }

    @Override
    public Map<String, Object> profile() {
        Integer adminId = AdminContexts.requestAdminId();
        Admin admin = admin(adminId, true);
        return CollectionUtils.arrayAsMap("admin", admin);
    }

    @Override
    public void getPassword(String password) {
        System.out.println(StringUtils.encryptPassword(password, salt));
    }

    @Override
    public Map<Integer, Admin> findByIdIn(Collection<Integer> ids) {
        return adminCache.findByKeys(ids);
    }
}
