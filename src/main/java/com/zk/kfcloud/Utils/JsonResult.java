package com.zk.kfcloud.Utils;

public class JsonResult {

    private Integer status;

    private String msg;

    private Object data;

    private String ok;

     /*
        构造
     */

    public JsonResult() {

    }

    public JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(Object data) {
        this.status = 200;
        this.msg = "ok";
        this.data = data;
    }

    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    /*
        status：200，msg = "ok";
     */
    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }

    public static JsonResult errMsg(String msg) {
        return new JsonResult(500, msg, null);
    }

    public static JsonResult errMap(Object obj) {
        return new JsonResult(501, "error", obj);
    }

    public static JsonResult errTokenMsg(String msg) {
        return new JsonResult(502, msg, null);
    }

    public static JsonResult errException(String msg) {
        return new JsonResult(555, msg, null);
    }


    /**
     * getset方法
     *
     * @return
     */

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
