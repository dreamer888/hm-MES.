package com.lgl.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgl.mes.system.entity.SysRole;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-16
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

	/**
	 * 根据用户 id 获取角色列表
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<SysRole> listByUserId(String userId) throws Exception;
}
