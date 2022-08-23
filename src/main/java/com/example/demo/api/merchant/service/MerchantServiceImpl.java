package com.example.demo.api.merchant.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.admin.authority.AdminContexts;
import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.merchant.entity.MerchantPermission;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.model.MerchantRole;
import com.example.demo.api.merchant.qo.MerchantQo;
import com.example.demo.api.merchant.repository.MerchantRepository;
import com.example.demo.api.renew.model.Renew;
import com.example.demo.api.renew.model.RenewConstants;
import com.example.demo.api.renew.repository.RenewRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.ByteUtils;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.DateUtils;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService ,AdminError{
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantAdminService merchantAdminService;

    @Autowired
    private RenewRepository renewRepository;

    private KvCacheWrap<Integer, Merchant> merchantCache;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    @PostConstruct
    public void init() {
        merchantCache = kvCacheFactory.create(new CacheOptions("merchant", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Merchant>() {
                    @Override
                    public Merchant findByKey(Integer key) throws ServiceException {
                        return getById(key);
                    }

                    @Override
                    public Map<Integer, Merchant> findByKeys(Collection<Integer> keys) throws ServiceException {

                        keys = keys.stream().filter(a -> a != null && a > 0).collect(Collectors.toSet());
                        if (CollectionUtils.isEmpty(keys)) {
                            return Collections.emptyMap();
                        }

                        List<Merchant> list = merchantRepository.findAllById(keys);
                        return list.stream().collect(Collectors.toMap(Merchant::getId, m -> m));
                    }
                }, new BeanModelConverter<>(Merchant.class));

    }

    private MerchantRole createSuperRole(int merchantId) {
        MerchantRole role = new MerchantRole();
        role.setName("超级管理员");
        role.setMerchantId(merchantId);
        role.setPermissions(MerchantPermission.getSuperPermission());
        merchantAdminService.saveRole(role, AdminType.NONE);
        return role;
    }

    @Override
    public void saveMerchant(Merchant merchant) {

        if (merchant.getId() != null && merchant.getId() > 0) {

            Merchant chant = writablMerchant(merchant.getId());
            chant.setName(merchant.getName());
            chant.setMobile(merchant.getMobile());
            chant.setCover(merchant.getCover());
            chant.setProductCategorySequence(merchant.getProductCategorySequence());
            merchantRepository.save(chant);
        } else {
            if (merchant.getName()==null) {
                throw new ArgumentServiceException("名称不能为空");
            }
            if (merchant.getMobile()==null) {
                throw new ArgumentServiceException("电话不能为空");
            }if (merchant.getCover()==null) {
                throw new ArgumentServiceException("Logo不能为空");
            }
            if (merchant.getProductCategorySequence()==null) {
                throw new ArgumentServiceException("经营范围不能为空");
            }
            if (merchant.getLocation()==null) {
                throw new ArgumentServiceException("地址不能为空");
            }
            merchant.setCreatedAt(System.currentTimeMillis());
            merchant.setStatus(ByteUtils.BYTE_1);
            merchant.setValidThru(System.currentTimeMillis());
            merchantRepository.save(merchant);
            merchantAdminService.saveRole(createSuperRole(merchant.getId()), AdminType.ADMIN);

        }

    }

    private Merchant writablMerchant(Integer id) {
        Merchant merchant = getById(id);
        return merchant;
    }

    public Merchant getById(Integer id) {
        Merchant merchant = merchantRepository.findById(id).orElse(null);
        if (merchant == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return merchant;
    }

    @Override
    public Page<Merchant> merchants(MerchantQo qo) {

        return merchantRepository.findAll(qo);
    }

    private Merchant findById(Integer id) {
        Merchant merchant = merchantRepository.findById(id).orElse(null);
        if (StringUtils.isNull(merchant)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return merchant;
    }

    @Override
    public Merchant merchant(int id) {
        Merchant merchant = findById(id);
        return merchant;
    }

    @Override
    public void merchantStatus(Integer id, Byte status) throws ServiceException {
        Merchant merchant = findById(id);
        merchant.setStatus(status);
        merchantRepository.save(merchant);
    }

    @Override
    public void create(Renew renew) {
        if (renew.getPayImg()==null) {
            throw new ArgumentServiceException("支付凭证不能为空");
        }
        if (renew.getPayNumber()==null) {
            throw new ArgumentServiceException("流水号不能为空");
        }
        if (renew.getRemark()==null) {
            throw new ArgumentServiceException("备注不能为空");
        }
        renew.setAmount(renew.getAmount()*100);
        renew.setAdminId(AdminContexts.requestAdminId());
        renew.setCreatedAt(System.currentTimeMillis());
        renew.setStatus(RenewConstants.CHECK_WAIT);
        renewRepository.save(renew);

    }

    @Override
    public Map<Integer, Merchant> findByIdIn(Collection<Integer> ids) {
        return merchantCache.findByKeys(ids);
    }

    @Override
    public Page<Merchant> merchantsLess(MerchantQo qo) {

        return merchantRepository.findAll(qo);
    }

    @Override
    public Long validThru(int day) {

        return merchantRepository.countByValidThru(Constants.STATUS_OK, System.currentTimeMillis() + day * DateUtils.SECOND_PER_DAY * 1000L);

    }

    @Override
    public Map<Integer, Merchant> findByIds(Set<Integer> ids) {
        return merchantCache.findByKeys(ids);
    }

}
