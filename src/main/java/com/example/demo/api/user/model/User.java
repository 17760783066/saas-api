package com.example.demo.api.user.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.example.demo.api.user.converter.UserInfoConverter;
import com.example.demo.api.user.entity.UserConstants;
import com.example.demo.api.user.entity.UserInfo;
import com.example.demo.common.model.KeyValue;
import com.example.demo.common.util.CollectionUtils;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private String mobile;

    @Column
    @Convert(converter = UserInfoConverter.class)
    private UserInfo userInfo;

    @Column(updatable = false)
    private Long createdAt;

    @Column(updatable = false)
    private Long signinAt;

    private Integer collectNum;

    @Column
    private Byte status;

    public Integer getId() {
        return id == null ? 0 : id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Long getSigninAt() {
        return signinAt;
    }

    public void setSigninAt(Long signinAt) {
        this.signinAt = signinAt;
    }

    public Map getBriefInfo() {

        String avatar = null;
        String profession = "未知职业";
        byte gender = 0;
        String constellation = null;
        try {

            UserInfo userInfo = this.getUserInfo();
            if (userInfo != null) {
                List<KeyValue> ps = UserConstants.PROFESSIONS.stream()
                        .filter(item -> item.getKey() == userInfo.getProfession().byteValue())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ps)) {
                    profession = ps.get(0).getVal();
                }
                gender = userInfo.getGender();
                avatar = userInfo.getAvatar();
                constellation = userInfo.getConstellation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CollectionUtils.arrayAsMap("id", id, "avatar", avatar, "name", this.getName(), "gender", gender,
                "profession", profession, "constellation", constellation);

    }


    public Integer getCollectNum() {
        return collectNum == null ? 0 : collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

}
