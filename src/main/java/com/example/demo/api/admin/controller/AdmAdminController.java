package com.example.demo.api.admin.controller;


import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.admin.entity.AdminPermissionVO;
import com.example.demo.api.admin.model.Role;
import com.example.demo.api.admin.qo.AdminQo;
import com.example.demo.api.admin.qo.AdminSessionQo;
import com.example.demo.api.admin.service.AdminService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/admin")
@RequiredAdminType(AdminType.ADMIN)
public class AdmAdminController extends BaseController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/save_role")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView save(String role) {
        adminService.saveRole(parseModel(role, new Role()));
        return feedback(null);
    }

    @RequestMapping(value = "/remove_role")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView remove_role(Integer id) throws ServiceException {
        adminService.removeRole(id);
        return feedback(null);
    }

    @RequestMapping(value = "/role")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView role(Integer id) throws ServiceException {
        return feedback(adminService.role(id));
    }

    @RequestMapping(value = "/permissions")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView permissions() throws ServiceException {
        return feedback(AdminPermissionVO.initAdmPermissions());
    }

    @RequestMapping(value = "/roles")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView roles() throws ServiceException {
        return feedback(adminService.roles(true));
    }

    @RequestMapping(value = "/admin_sessions")
    @RequiredPermission(AdminPermission.ADMIN_LIST)
    public ModelAndView adminSessions(String adminSessionQo) {
        return feedback(adminService.adminSessions(parseModel(adminSessionQo, new AdminSessionQo())));
    }

    @RequestMapping(value = "/remove_admin")
    @RequiredPermission(AdminPermission.ADMIN_EDIT)
    public ModelAndView adminRemove(Integer id) {
        adminService.removeAdmin(id);
        return feedback(null);
    }

    @RequestMapping(value = "/save_admin")
    @RequiredPermission(AdminPermission.ADMIN_EDIT)
    public ModelAndView saveAdmin(String admin) {
        adminService.saveAdmin(parseModel(admin, new Admin()));
        return feedback(null);
    }

    @RequestMapping(value = "/admin")
    @RequiredPermission(AdminPermission.ADMIN_EDIT)
    public ModelAndView admin(Integer id) throws ServiceException {
        return feedback(adminService.admin(id, false));
    }

    @RequestMapping(value = "/admin_status")
    @RequiredPermission(AdminPermission.ADMIN_EDIT)
    public ModelAndView admin_status(Integer id, Byte status) throws ServiceException {
        adminService.adminStatus(id, status);
        return feedback(null);
    }

    @RequestMapping(value = "/admins")
    @RequiredPermission(AdminPermission.ADMIN_LIST)
    public ModelAndView admins(String adminQo) throws ServiceException {
        AdminQo qo = new AdminQo();
        if (StringUtils.isNotEmpty(adminQo)) {
            qo = parseModel(adminQo, new AdminQo());
        }
        return feedback(adminService.admins(qo));
    }

    @RequestMapping(value = "/update_password")
    public ModelAndView updatePassword(String password, String repeatPassword, String oldPassword) {
        adminService.updatePassword(password, repeatPassword, oldPassword);
        return feedback(null);
    }

    @RequestMapping(value = "/profile")
    public ModelAndView profile() {
        return feedback(adminService.profile());
    }

    @RequestMapping(value = "/signin")
    @RequiredAdminType(AdminType.NONE)
    public ModelAndView signin(String admin, String valCode) {
        return feedback(adminService.signin(parseModel(admin, new Admin()), parseModel(valCode, new ValCode())));
    }

}
