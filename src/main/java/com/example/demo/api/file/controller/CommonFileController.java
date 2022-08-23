package com.example.demo.api.file.controller;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.file.util.ImgBase64Utils;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/common/file")
@RequiredAdminType(AdminType.NONE)
public class CommonFileController extends BaseController {

    @RequestMapping(value = "/img_to_base64")
    @RequiredPermission(AdminPermission.NONE)
    public ModelAndView imgToBase64(String url) throws Exception {
        return feedback(ImgBase64Utils.base64FromURL(url));
    }

}
