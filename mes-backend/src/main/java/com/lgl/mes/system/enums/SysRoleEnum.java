package com.lgl.mes.system.enums;

/**
 * 系统用户枚举
 *
 * @author dreamer，75039960@qq.com
 * @date 2021/10/17 9:45
 */
public enum SysRoleEnum {

    DELETED_NORMAL("0", "正常"),

    DELETED_DEL("1", "删除"),

    DELETED_DISABLED("2", "禁用");

    /**
     * The Code
     */
    String code;
    /**
     * The Desc
     */
    String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    SysRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
