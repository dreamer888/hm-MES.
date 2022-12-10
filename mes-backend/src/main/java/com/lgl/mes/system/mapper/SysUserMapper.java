package com.lgl.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgl.mes.system.dto.SysUserDTO;
import com.lgl.mes.system.entity.SysUser;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-15
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

	SysUserDTO selectUserAndRoleByUsername(String username) throws Exception;
}
