package com.lgl.mes.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgl.mes.system.dto.SysMenuDTO;
import com.lgl.mes.system.entity.SysMenu;
import com.lgl.mes.system.vo.TreeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2021-10-16
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 根据角色id查询菜单列表
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    List<SysMenuDTO> listByRoleId(String roleId) throws Exception;

    /**
     * 系统首页初始化菜单树数据
     *
     * @return 系统首页初始化菜单树数据
     * @throws Exception 异常
     */
    Map<String, Object> listIndexMenuTree() throws Exception;


    /**
     * 用户搜索系统首页初始化菜单树数据
     *
     * @return 系统首页初始化菜单树数据
     * @throws Exception 异常
     */
    Map<String, Object> listIndexMenuSearchTree(String menuName) throws Exception;


    /**
     * 获取系统菜单树
     *
     * @return 系统菜单树
     * @throws Exception 异常
     */
    List<TreeVO<SysMenu>> listMenuTree() throws Exception;
}
