package com.dream.iot.test;

public enum StatusCode {
    Success(8, "成功"),
    Failed_Unknown(-1, "未知错误")
    ;

    public int code;
    public String desc;

    StatusCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StatusCode getInstance(int code) {
        switch (code) {
            case 8: return Success;

            default: return Failed_Unknown;
        }
    }
}
