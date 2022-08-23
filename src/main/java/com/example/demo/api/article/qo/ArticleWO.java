package com.example.demo.api.article.qo;

public class ArticleWO {

    private boolean isWithAuthor;

    private boolean isWithTags;

    private boolean isWithCollect;

    private boolean isWithLike;

    private boolean isWithContent;

    public static ArticleWO getNonInstance() {
        return new ArticleWO();
    }

    public static ArticleWO getAdminListInstance() {
        return getNonInstance().setWithAuthor(true).setWithTags(true);
    }

    public static ArticleWO getCommonListInstance() {
        return getNonInstance().setWithAuthor(true).setWithTags(true);
    }

    public static ArticleWO getCommonDetailInstance() {
        return getNonInstance().setWithAuthor(true).setWithTags(true).setWithCollect(true).setWithLike(true)
                .setWithContent(true);
    }

    public static ArticleWO getAuthorListInstance() {
        return getNonInstance().setWithTags(true);
    }

    public static ArticleWO getAuthorDetailInstance() {
        return getNonInstance().setWithTags(true).setWithContent(true);
    }

    public boolean isWithAuthor() {
        return isWithAuthor;
    }

    public ArticleWO setWithAuthor(boolean withAuthor) {
        isWithAuthor = withAuthor;
        return this;
    }

    public boolean isWithTags() {
        return isWithTags;
    }

    public ArticleWO setWithTags(boolean withTags) {
        isWithTags = withTags;
        return this;
    }

    public boolean isWithCollect() {
        return isWithCollect;
    }

    public ArticleWO setWithCollect(boolean isWithCollect) {
        this.isWithCollect = isWithCollect;
        return this;
    }

    public boolean isWithLike() {
        return isWithLike;
    }

    public ArticleWO setWithLike(boolean isWithLike) {
        this.isWithLike = isWithLike;
        return this;
    }

    public boolean isWithContent() {
        return isWithContent;
    }

    public ArticleWO setWithContent(boolean isWithContent) {
        this.isWithContent = isWithContent;
        return this;
    }

}
