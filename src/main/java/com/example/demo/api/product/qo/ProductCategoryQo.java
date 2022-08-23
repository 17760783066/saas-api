package com.example.demo.api.product.qo;

import java.util.Arrays;
import java.util.List;

import com.example.demo.api.product.model.ProductConstants;
import com.example.demo.common.reposiotry.support.DataQueryObjectSort;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class ProductCategoryQo extends DataQueryObjectSort{
    @QueryField(type = QueryType.EQUAL, name = "categoryId")
    private Integer categoryIds;
    protected String sortPropertyName = "priority";
    private Byte _status;
    @QueryField(type = QueryType.IN, name = "status")
    private List<Byte> statusArr;

    public ProductCategoryQo() {
    }

    public ProductCategoryQo(Byte _status) {
        if (_status == ProductConstants.PRODUCT_STATUS_ON) {
            this.setStatusArr(Arrays.asList(ProductConstants.PRODUCT_STATUS_ON));
        } else if (_status == ProductConstants.PRODUCT_STATUS_OFF) {
            this.setStatusArr(
                    Arrays.asList(ProductConstants.PRODUCT_STATUS_ON, ProductConstants.PRODUCT_STATUS_OFF));
        }
    }


    public List<Byte> getStatusArr() {
        return statusArr;
    }


    public void setStatusArr(List<Byte> statusArr) {
        this.statusArr = statusArr;
    }


    public Byte get_status() {
        return _status;
    }
    public void set_status(Byte _status) {
        this._status = _status;
    }
    

    public String getSortPropertyName() {
        return sortPropertyName;
    }
    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }


    public Integer getCategoryIds() {
        return categoryIds;
    }


    public void setCategoryIds(Integer categoryIds) {
        this.categoryIds = categoryIds;
    }
    
}
