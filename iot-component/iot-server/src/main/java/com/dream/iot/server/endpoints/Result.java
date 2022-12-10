package com.dream.iot.server.endpoints;

import com.alibaba.fastjson.JSON;

public class Result {

    private Object data;

    private int code;

    private String msg;

    public static final int ERROR_CODE = 500;
    public static final int SUCCESS_CODE = 200;

    public static final String SUCCESS_MSG = "OK";

    public static Result success(Object data) {
        return new Result(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static Result fail(String msg) {
        return new Result(null, ERROR_CODE, msg);
    }

    public Result(Object data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public String textJson() {
        return JSON.toJSONString(this);
    }

    public byte[] binaryJson() {
        return JSON.toJSONBytes(this);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
