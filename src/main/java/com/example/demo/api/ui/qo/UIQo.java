package com.example.demo.api.ui.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class UIQo extends DataQueryObjectPage{
   
    @QueryField(type = QueryType.EQUAL, name = "isDefault", zero2Null = true)
    private Byte isDefault;

    public UIQo() {
    }

    public UIQo(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }
    
}
