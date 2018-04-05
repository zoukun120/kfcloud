package com.zk.kfcloud.Entity.web;

public class WeChat {
    private Integer id;

    private String openId;

    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WeChat{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", userId=" + userId +
                '}';
    }
}