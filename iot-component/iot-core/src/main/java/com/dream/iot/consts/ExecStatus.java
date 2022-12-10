package com.dream.iot.consts;

/**
 * 协议的执行状态
 */
public enum ExecStatus {
    success("成功"), timeout("超时"), offline("设备断线"), fail("失败");

    public String desc;

    ExecStatus(String desc) {
        this.desc = desc;
    }

}
