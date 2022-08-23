package com.example.demo.api.merchant.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class MerchantAdminSessionQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "merchantAdminId")
    private Integer merchantAdminId;

    
    public MerchantAdminSessionQo() {
    }

    
    public MerchantAdminSessionQo(Integer merchantAdminId) {
        this.merchantAdminId = merchantAdminId;
    }


    public Integer getMerchantAdminId() {
        return merchantAdminId;
    }

    public void setMerchantAdminId(Integer merchantAdminId) {
        this.merchantAdminId = merchantAdminId;
    }

    
}
