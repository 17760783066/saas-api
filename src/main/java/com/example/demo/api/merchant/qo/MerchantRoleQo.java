package com.example.demo.api.merchant.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectSort;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class MerchantRoleQo extends DataQueryObjectSort {

    @QueryField(type = QueryType.EQUAL, name = "merchantId")
    private Integer merchantId;

    public MerchantRoleQo() {

    }

    public MerchantRoleQo(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

}
