package com.example.demo.api.user.qo;

public class ShoppingWo {
    private boolean isWithMerchant;

    private boolean isWithProduct;
    public static ShoppingWo getNonInstance() {
        return new ShoppingWo();
    }

    public static ShoppingWo getProductListInstance() {
        return getNonInstance();
    }
    public static ShoppingWo getProductDetailInstance() {
        return getNonInstance().setWithMerchant(true).setWithProduct(true);
    }

    public boolean isWithMerchant() {
        return isWithMerchant;
    }

    public ShoppingWo setWithMerchant(boolean isWithMerchant) {
        this.isWithMerchant = isWithMerchant;
        return this;
    }

    public boolean isWithProduct() {
        return isWithProduct;
    }

    public ShoppingWo setWithProduct(boolean isWithProduct) {
        this.isWithProduct = isWithProduct;
        return this;
    }
    
}
