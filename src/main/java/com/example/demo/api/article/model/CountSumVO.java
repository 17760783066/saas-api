package com.example.demo.api.article.model;

import java.lang.reflect.Array;

public class CountSumVO {

    private Long count = 0L;
    private Long pv = 0L;
    private Long collectNum = 0L;
    private Long likeNum = 0L;

    public Long getWeight() {
        return count * 10 + pv + collectNum * 3 + likeNum * 2;
    }

    public CountSumVO() {
    }

    public CountSumVO parseObject(Object obj) {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Long[] os = new Long[length];
            for (int i = 0; i < os.length; i++) {
                try {
                    os[i] = Long.parseLong(Array.get(obj, i).toString());
                } catch (Exception e) {
                    os[i] = 0L;
                }
            }
            return new CountSumVO(os[0], os[1], os[2], os[3]);
        }
        return new CountSumVO();
    }

    public CountSumVO(Long count, Long pv, Long collectNum, Long likeNum) {
        this.count = count;
        this.pv = pv;
        this.collectNum = collectNum;
        this.likeNum = likeNum;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    @Override
    public String toString() {
        return getCount() + " " + getPv() + " " + getCollectNum() + " " + getLikeNum() + " " + getWeight();
    }

    public Long getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Long collectNum) {
        this.collectNum = collectNum;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }
}
