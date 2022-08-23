package com.example.demo.api.user.entity;

import com.example.demo.common.util.DateUtils;

public class UserInfo {

    private Byte gender;
    private Long birthDate; // 生日
    private String constellation; // 星座

    private Byte profession; // 专业
    private Byte education; // 学历

    private String remark;

    private String avatar;

    public Byte getGender() {
        return gender == null ? 1 : gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getBirthDate() {
        return birthDate == null ? 0 : birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getConstellation() {
        if (birthDate == null) {
            return "未知";
        }
        return DateUtils.getBirthDateConstellation(birthDate);
    }

    public Byte getProfession() {
        return profession == null ? 0 : profession;
    }

    public void setProfession(Byte profession) {
        this.profession = profession;
    }

    public Byte getEducation() {
        return education;
    }

    public void setEducation(Byte education) {
        this.education = education;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
