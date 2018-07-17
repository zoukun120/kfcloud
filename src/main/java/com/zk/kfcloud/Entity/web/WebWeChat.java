package com.zk.kfcloud.Entity.web;

public class WebWeChat {
    private Integer user_id;
    private String loginname;
    private String username;
    private Integer status;
    private Integer login_status;
    private Integer alarm_auth;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLogin_status() {
        return login_status;
    }

    public void setLogin_status(Integer login_status) {
        this.login_status = login_status;
    }

    public Integer getAlarm_auth() {
        return alarm_auth;
    }

    public void setAlarm_auth(Integer alarm_auth) {
        this.alarm_auth = alarm_auth;
    }

    @Override
    public String toString() {
        return "WebWeChat{" +
                "user_id=" + user_id +
                ", loginname='" + loginname + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", login_status=" + login_status +
                ", alarm_auth=" + alarm_auth +
                '}';
    }
}
