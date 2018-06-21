package com.zk.kfcloud.Exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(int errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;  //异常对应的返回码
    private String errorMsg;  //异常对应的描述信息

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "UserNotFoundException{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
