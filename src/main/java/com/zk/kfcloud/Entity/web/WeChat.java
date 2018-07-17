package com.zk.kfcloud.Entity.web;

import java.util.Date;

public class WeChat {
    private Integer id;

    private String openId;

    private String userName;

    private Integer userId;

    private Integer loginStatus;

    private Date loginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public void setName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getName() {
        return userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "WeChat{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\''+
                ", loginStatus=" + loginStatus +
                ", loginTime=" + loginTime +
                '}';
    }
}