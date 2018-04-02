package com.zk.kfcloud.Exception;

public class MenuCreateException extends Exception {

    private String errorMsg;  //异常对应的描述信息

    public MenuCreateException() {
        super();
    }

    public MenuCreateException(String errorMsg) {
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
        return "AccessException{" +
                "errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
