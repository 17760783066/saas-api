package com.example.demo.api.product.qo;

import java.util.List;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class ProductQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL, name = "status", zero2Null = true)
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "productId",zero2Null = true)
    private Integer productId;
    @QueryField(type = QueryType.EQUAL, name = "merchantId",zero2Null = true)
    private Integer merchantId;

    @QueryField(type = QueryType.FULL_LIKE, name = "name", zero2Null = true)
    private String name;

    // 混合的categoryId
    private List<Integer> mixtureCategoryIds;

    @QueryField(type = QueryType.IN, name = "categoryId")
    private List<Integer> categoryIds;

    public ProductQo() {
    }
    
    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getMixtureCategoryIds() {
        return mixtureCategoryIds;
    }

    public void setMixtureCategoryIds(List<Integer> mixtureCategoryIds) {
        this.mixtureCategoryIds = mixtureCategoryIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

   

}
