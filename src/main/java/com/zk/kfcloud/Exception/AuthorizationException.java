package com.zk.kfcloud.Exception;

public class AuthorizationException extends Exception {

    private String errorMsg;  //异常对应的描述信息

    public AuthorizationException(String message, String errorMsg) {
        super(message);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "AuthorizationException{" +
                "errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
