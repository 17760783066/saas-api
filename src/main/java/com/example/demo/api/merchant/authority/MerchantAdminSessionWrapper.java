package com.example.demo.api.merchant.authority;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.model.MerchantAdminSession;
import com.example.demo.common.context.SessionWrapper;

public class MerchantAdminSessionWrapper implements SessionWrapper{
    private MerchantAdmin merchantAdmin;
    private MerchantAdminSession merchantAdminSession;
    private Merchant merchant;
    public MerchantAdminSessionWrapper() {
    }

    public MerchantAdminSessionWrapper(MerchantAdmin merchantAdmin, MerchantAdminSession merchantAdminSession,
            Merchant merchant) {
        this.merchantAdmin = merchantAdmin;
        this.merchantAdminSession = merchantAdminSession;
        this.merchant = merchant;
    }
    public MerchantAdmin getMerchantAdmin() {
        return merchantAdmin;
    }

    public void setMerchantAdmin(MerchantAdmin merchantAdmin) {
        this.merchantAdmin = merchantAdmin;
    }

    public MerchantAdminSession getMerchantAdminSession() {
        return merchantAdminSession;
    }

    public void setMerchantAdminSession(MerchantAdminSession merchantAdminSession) {
        this.merchantAdminSession = merchantAdminSession;
    }
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    
}
