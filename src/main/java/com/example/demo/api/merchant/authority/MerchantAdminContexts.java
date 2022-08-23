package com.example.demo.api.merchant.authority;

import java.util.List;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.common.context.Contexts;
import com.example.demo.common.context.SessionWrapper;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;

public class MerchantAdminContexts extends Contexts {
    public static MerchantAdminSessionWrapper getMerchantAdminSessionWrapper() {
        SessionWrapper session = getWrapper();
        if (session == null) {
            return null;
        }
        if (!(session instanceof MerchantAdminSessionWrapper)) {
            return null;
        }
        return (MerchantAdminSessionWrapper) session;
    }

    public static MerchantAdminSessionWrapper requireMerchantAdminSessionWrapper() throws ServiceException {
        MerchantAdminSessionWrapper session = getMerchantAdminSessionWrapper();
        if (session == null) {
            throw new ServiceException(ERR_SESSION_EXPIRES);
        }
        return session;
    }


    public static Integer sessionMerchantAdminId() throws ServiceException {
        MerchantAdminSessionWrapper wrapper = getMerchantAdminSessionWrapper();
        if (wrapper == null) {
            return null;
        }
        return wrapper.getMerchantAdmin().getId();
    }

    public static List<String> requestProductCategorySequence() throws ServiceException{
        MerchantAdminSessionWrapper merchantAdminSessionWrapper = requireMerchantAdminSessionWrapper();
        return merchantAdminSessionWrapper.getMerchant().getProductCategorySequence();
    }

    public static Integer requestMerchantAdminId() throws ServiceException {

        Integer MerchantAdminId = sessionMerchantAdminId();
        if (MerchantAdminId == null) {
            throw new SessionServiceException();
        }
        return MerchantAdminId;
    }

    public static Integer requestMerchantId() throws ServiceException {
        Merchant merchant = requireMerchantAdminSessionWrapper().getMerchant();
        return merchant.getId();
    }

    private static List<String> requestMerchantAdminPermissions() throws ServiceException {
        MerchantAdmin merchantAdmin = requireMerchantAdminSessionWrapper().getMerchantAdmin();
        return merchantAdmin.getMerchantRole().getPermissions();
    }

    public static boolean checkMerchantAdminPermission(String permission) {
        return requestMerchantAdminPermissions().contains(permission);
    }
}
