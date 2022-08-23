package com.example.demo.api.user.controller;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.user.entity.UserConstants;
import com.example.demo.api.user.qo.UserQo;
import com.example.demo.api.user.service.UserService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import com.example.demo.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/user")
@RequiredAdminType(AdminType.ADMIN)
@RequiredPermission(AdminPermission.USER_EDIT)
public class AdmUserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/props")
    public ModelAndView props() {
        return feedback(CollectionUtils.arrayAsMap("professions", UserConstants.PROFESSIONS, "educations", UserConstants.EDUCATIONS));
    }

    @RequestMapping(value = "/user")
    public ModelAndView user(Integer id) {
        return feedback(userService.user(id, AdminType.ADMIN));
    }

    @RequestMapping(value = "/users")
    public ModelAndView users(String userQo) {
        return feedback(userService.users(parseModel(userQo, new UserQo()), AdminType.ADMIN));
    }

    @RequestMapping(value = "/status")
    public ModelAndView status(Integer id, Byte status) {
        userService.status(id, status);
        return feedback();
    }

}