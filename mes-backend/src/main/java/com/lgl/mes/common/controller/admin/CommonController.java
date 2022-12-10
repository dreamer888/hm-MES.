package com.lgl.mes.common.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公共 Controller
 *
 * @author dreamer，75039960@qq.com
 * @date 2022/3/11
 */
@RequestMapping("/admin/common")
@Controller("adminCommonController")
public class CommonController {

    /**
     * 公用前端UI
     * @param fileName
     * @return
     */
    @GetMapping({"/ui/{fileName}"})
    public String searchPanelUI(@PathVariable String fileName) {
        return "/common/" + fileName;
    }
}
