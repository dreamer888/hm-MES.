package com.lgl.mes.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lgl.mes.system.dto.SysRoleDTO;
import com.lgl.mes.system.dto.SysUserDTO;
import com.lgl.mes.system.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-16
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID获取角色列表信息
     *
     * @param userId 系统用户ID
     * @return 角色列表
     * @throws Exception 异常
     */
    List<SysRoleDTO> listByUserId(String userId) throws Exception;

    /**
     * 重新建立用户角色关系
     *
     * @param sysUserDTO 系统用户DTO
     * @throws Exception 异常
     */
    void rebuild(SysUserDTO sysUserDTO) throws Exception;
}
