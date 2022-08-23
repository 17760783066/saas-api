package com.example.demo.api.article.model;

import javax.persistence.*;

@Entity
@Table
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Integer articleId;

    @Column
    private Integer tagId;

    @Column
    private Integer pv = 0;

    @Column
    private Integer collectNum = 0;

    @Column
    private Integer likeNum = 0;

    @Column
    private Byte status;

    @Transient
    private Tag tag;

    public ArticleTag() {
    }

    public ArticleTag(Integer articleId, Integer tagId, Integer pv, Integer collectNum, Integer likeNum, Byte status) {
        this.articleId = articleId;
        this.tagId = tagId;
        this.pv = pv;
        this.collectNum = collectNum;
        this.likeNum = likeNum;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

}
