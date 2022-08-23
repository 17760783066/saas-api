package com.example.demo.api.article.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectSort;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class TagQo extends DataQueryObjectSort {

    @QueryField(type = QueryType.EQUAL, name = "type")
    private Byte type;

    public TagQo() {
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type == 0 ? null : type;
    }
}
