package com.example.demo.api.renew.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.service.AdminService;
import com.example.demo.api.bill.model.Bill;
import com.example.demo.api.bill.repository.BillRepository;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.repository.MerchantRepository;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.renew.model.Renew;
import com.example.demo.api.renew.model.RenewConstants;
import com.example.demo.api.renew.qo.RenewQo;
import com.example.demo.api.renew.qo.RenewWo;
import com.example.demo.api.renew.repository.RenewRepository;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.Duration;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RenewServiceImpl implements RenewService, AdminError {
    @Autowired
    private RenewRepository renewRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private BillRepository billRepository;

    private Renew getById(Integer id) {
        Renew renew = renewRepository.findById(id).orElse(null);
        if (renew == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return renew;
    }

    @Override
    public Page<Renew> renews(RenewQo qo, RenewWo wo) {
        Page<Renew> page = renewRepository.findAll(qo);
        wrapRenew(page.getContent(), wo);
        return page;
    }

    private void wrapRenew(List<Renew> renews, RenewWo wo) {
        int size = renews.size();
        Set<Integer> adminIds = new HashSet<>();

        Map<Integer, Admin> adminMap = new HashMap<>(size);
        Map<Integer, Merchant> merchantMap = new HashMap<>();

        if (wo.isWithMerchant()) {
            Set<Integer> merchantIds = renews.stream().map(Renew::getMerchantId).collect(Collectors.toSet());
            merchantMap = merchantService.findByIdIn(merchantIds);
        }

        if (wo.isWithAdmin()) {
            adminIds.addAll(renews.stream().map(Renew::getAdminId).collect(Collectors.toSet()));

        }
        if (wo.isWithAuditor()) {
            adminIds.addAll(renews.stream().map(Renew::getAuditorId).collect(Collectors.toSet()));
        }
        if (CollectionUtils.isNotEmpty(adminIds)) {
            adminMap = adminService.findByIdIn(adminIds);
        }

        for (Renew item : renews) {
            if (wo.isWithMerchant()) {
                item.setMerchant(merchantMap.get(item.getMerchantId()));
            }
            if (wo.isWithAdmin()) {
                item.setAdmin(adminMap.get(item.getAdminId()));

            }
            if (wo.isWithAuditor()) {
                item.setAuditor(adminMap.get(item.getAuditorId()));
            }
        }
    }

    @Override
    public void renewPass(Integer id) {
        Renew renew = getById(id);
        renew.setStatus(Constants.STATUS_PASS);
        renew.setAuditAt(System.currentTimeMillis());
        renewRepository.save(renew);
    }

    @Override
    public void renewFail(Integer id) {
        Renew renew = getById(id);
        renew.setStatus(Constants.STATUS_HALT);
        renew.setAuditAt(System.currentTimeMillis());
        renewRepository.save(renew);
    }

    @Override
    public void editStatus(Renew renew) {
        Renew sRenew = getById(renew.getId());
        Bill c = new Bill();

        if (sRenew.getStatus() == RenewConstants.CHECK_WAIT) {
            if (renew.getStatus() == 1 || renew.getStatus() == null) {
            Bill bill = billRepository.findByPayNumber(sRenew.getPayNumber());
            if (bill == null) {
                c.setAmount(sRenew.getAmount());
                c.setPayNumber(sRenew.getPayNumber());
                c.setPayType(sRenew.getPayType());
                c.setRenferType(RenewConstants.RENEW);
                c.setRenderId(sRenew.getId());
                billRepository.save(c);
                sRenew.setAuditAt(System.currentTimeMillis());
                sRenew.setStatus(RenewConstants.CHECK_SUCCESS);
                String duration = sRenew.getDuration();
                Integer merchantId = sRenew.getMerchantId();
                Merchant merchant = merchantService.getById(merchantId);
                Long b = parseDuration(merchant, duration);
                merchant.setValidThru(b);
                merchantRepository.save(merchant);
            } else {
                throw new ServiceException(ERR_PAYNUMBER_EXIST);
            }
        }
        if (renew.getStatus() == 2) {
            sRenew.setStatus(RenewConstants.CHECK_FAIL);
            sRenew.setReason(renew.getReason());
            sRenew.setAuditAt(System.currentTimeMillis());
        }
        renewRepository.save(sRenew);
        }
        
    }

    private long parseDuration(Merchant merchant, String duration) throws ServiceException {
        Duration dur = Duration.parse(duration);
        if (dur == null || dur.forever()) {
            throw new ArgumentServiceException("Bad duration");
        }
        long now = System.currentTimeMillis();
        return dur.addDate(new Date(merchant.getValidThru() > now ? merchant.getValidThru() : now)).getTime();
    }

}
