package com.lgl.mes.common;

import com.lgl.mes.system.entity.SysUser;
import org.apache.shiro.SecurityUtils;

/**
 * 基础前端控制器
 *
 * @author dreamer，75039960@qq.com
 * @date 2021/9/27 16:05
 */
public class BaseController {

    public SysUser getSysUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }
}
