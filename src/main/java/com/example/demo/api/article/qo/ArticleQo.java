package com.example.demo.api.article.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class ArticleQo extends DataQueryObjectPage {

    private String sortPropertyName = "lastModified";

    @QueryField(type = QueryType.FULL_LIKE, name = {"title", "content"})
    private String titleOrContent;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;
    @QueryField(type = QueryType.EQUAL, name = "type")
    private Byte type;

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Integer userId;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status == 0 ? null : status;
    }

    @Override
    public String getSortPropertyName() {
        return sortPropertyName;
    }

    @Override
    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    public String getTitleOrContent() {
        return titleOrContent;
    }

    public void setTitleOrContent(String titleOrContent) {
        this.titleOrContent = titleOrContent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

}
