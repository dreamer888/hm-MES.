package com.lgl.mes.system.dto;

import com.lgl.mes.system.entity.SysRole;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-16
 */
public class SysRoleDTO extends SysRole {

    /**
     * 角色是否选中
     */
    private boolean checked;

    /**
     * 菜单列表
     */
    List<SysMenuDTO> sysMenuDtos;

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<SysMenuDTO> getSysMenuDtos() {
        return sysMenuDtos;
    }

    public void setSysMenuDtos(List<SysMenuDTO> sysMenuDtos) {
        this.sysMenuDtos = sysMenuDtos;
    }
}
