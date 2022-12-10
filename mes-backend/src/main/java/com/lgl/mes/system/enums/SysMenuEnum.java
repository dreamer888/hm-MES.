package com.lgl.mes.system.enums;

/**
 * 系统用户枚举
 *
 * @author dreamer，75039960@qq.com
 * @date 2021/10/17 9:45
 */
public enum SysMenuEnum {

    /**
     * 类型：0 目录；1 菜单；2 按钮
     */
    TYPE_CATALOG("0", "目录"),

    TYPE_MENU("1", "菜单"),

    TYPE_BUTTON("2", "按钮");

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

    SysMenuEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
