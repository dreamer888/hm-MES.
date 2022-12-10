package com.lgl.mes.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lgl.mes.common.BaseEntity;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2020-03-05
 */
@TableName("sp_sys_role_menu")
public class SysRoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 菜单id
     */
    private String menuId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
