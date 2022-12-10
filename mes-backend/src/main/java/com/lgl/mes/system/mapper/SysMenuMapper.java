package com.lgl.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgl.mes.system.dto.SysMenuDTO;
import com.lgl.mes.system.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-16
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色id查询菜单列表
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    List<SysMenuDTO> listByRoleId(String roleId) throws Exception;

    /**
     * 根据用户输入的菜单名称 模糊匹配
     *
     * @param menuName 菜单名称
     * @return 用户结果
     * @throws Exception 异常
     */
    List<SysMenu> listBySearchByName(String menuName) throws Exception;
}
