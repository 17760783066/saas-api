package com.example.demo.api.article.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.common.util.SimpleHtmlParser;

@Entity
@Table
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String title;

    @Column
    private String descr;

    @Column
    private String cover;

    @Column
    private String content;

    @Column(updatable = false)
    private Long createdAt;

    @Column
    private Long lastModified;

    @Column
    private Integer userId;

    @Column
    private Integer pv;

    @Column
    private Integer commentNum;

    @Column
    private Integer collectNum;

    @Column
    private Integer likeNum;

    @Column
    private Byte status;

    @Transient
    private Map user;

    @Transient
    private List<Tag> tags;

    private Byte type;
    @Transient
    private Byte isCollected = 0;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getId() {
        return id == null ? 0 : id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return SimpleHtmlParser.removeScript(content);
    }

    public void setContent(String content) {
        this.content = SimpleHtmlParser.removeScript(content);
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Map getUser() {
        return user;
    }

    public void setUser(Map user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public Byte getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Byte isCollected) {
        this.isCollected = isCollected;
    }

}
