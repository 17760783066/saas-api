package com.example.demo.api.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.api.article.model.Article;

@Entity
@Table
public class UserCollect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Byte collectType;

    @Column
    private Integer collectId;

    @Column(updatable = false)
    private Long createdAt;

    @Transient
    private Article article;

    @Transient
    private User author;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getCollectType() {
        return collectType;
    }

    public void setCollectType(Byte collectType) {
        this.collectType = collectType;
    }

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
    }

    public UserCollect(Integer userId, Byte collectType, Integer collectId, Long createdAt) {
        this.userId = userId;
        this.collectType = collectType;
        this.collectId = collectId;
        this.createdAt = createdAt;
    }

    public UserCollect() {
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
