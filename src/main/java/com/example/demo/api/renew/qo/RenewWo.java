package com.example.demo.api.renew.qo;

public class RenewWo {
    private boolean isWithMerchant;

    private boolean isWithAdmin;

    private boolean isWithAuditor;
    public static RenewWo getNonInstance() {
        return new RenewWo();
    }
    public static RenewWo getRenewListInstance() {
        return getNonInstance().setWithMerchant(true).setWithAdmin(true).setWithAuditor(true);
    }
    public boolean isWithMerchant() {
        return isWithMerchant;
    }
    public RenewWo setWithMerchant(boolean isWithMerchant) {
        this.isWithMerchant = isWithMerchant;
        return this;
    }
    public boolean isWithAdmin() {
        return isWithAdmin;
    }
    public RenewWo setWithAdmin(boolean isWithAdmin) {
        this.isWithAdmin = isWithAdmin;
        return this;
    }
    public boolean isWithAuditor() {
        return isWithAuditor;
    }
    public RenewWo setWithAuditor(boolean isWithAuditor) {
        this.isWithAuditor = isWithAuditor;
        return this;
    }

}
