package com.example.demo.api.trade.entity;

public class TradeWrapOption {
    private boolean withMerchant;
    private boolean withUser;
    public TradeWrapOption() {
    }
    public boolean isWithMerchant() {
        return withMerchant;
    }

    public boolean isWithUser() {
        return withUser;
    }

    private TradeWrapOption setWithMerchant() {
        this.withMerchant = true;
        return this;
    }

    private TradeWrapOption setWithUser() {
        this.withUser = true;
        return this;
    }

    public static TradeWrapOption getMerchantListInstance() {
        return new TradeWrapOption().setWithMerchant().setWithUser();
    }

    public static TradeWrapOption getUserListInstance() {
        return new TradeWrapOption().setWithMerchant();
    }


}
