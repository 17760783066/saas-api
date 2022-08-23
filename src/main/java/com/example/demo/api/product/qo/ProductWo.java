package com.example.demo.api.product.qo;

public class ProductWo {
    private boolean isWithMerchant;

    public static ProductWo getNonInstance() {
        return new ProductWo();
    }

    public static ProductWo getProductListInstance() {
        return getNonInstance();
    }
    public static ProductWo getProductDetailInstance() {
        return getNonInstance().setWithMerchant(true);
    }

    public boolean isWithMerchant() {
        return isWithMerchant;
    }

    public ProductWo setWithMerchant(boolean isWithMerchant) {
        this.isWithMerchant = isWithMerchant;
        return this;
    }
}
