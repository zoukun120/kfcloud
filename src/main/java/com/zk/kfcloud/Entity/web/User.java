package com.zk.kfcloud.Entity.web;

import java.util.Date;

public class User {
    private Integer userId;

    private String loginname;

    private String password;

    private String username;

    private String rights;

    private Boolean status;

    private Integer roleId;

    private Date lastLogin;

    private Integer parentId;

    private String openId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights == null ? null : rights.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginname='" + loginname + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", rights='" + rights + '\'' +
                ", status=" + status +
                ", roleId=" + roleId +
                ", lastLogin=" + lastLogin +
                ", parentId=" + parentId +
                ", openId='" + openId + '\'' +
                '}';
    }
}