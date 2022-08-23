package com.example.demo.api.merchant.controller;


import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.merchant.entity.MerchantPermissionVO;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.model.MerchantRole;
import com.example.demo.api.merchant.qo.MerchantAdminQo;
import com.example.demo.api.merchant.qo.MerchantAdminSessionQo;
import com.example.demo.api.merchant.qo.MerchantRoleQo;
import com.example.demo.api.merchant.service.MerchantAdminService;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ValCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/merchant/adm")
@RequiredAdminType(AdminType.MERCHANT)


public class MchMerchantAdminController extends BaseController {
    @Autowired
    private MerchantAdminService merchantAdminService;

    @Autowired
    private MerchantService merchantService;
    @RequestMapping(value = "/signin")
    @RequiredAdminType(AdminType.NONE)
    public ModelAndView signin(String merchantAdmin, String valCode) {
        return feedback(merchantAdminService.signin(parseModel(merchantAdmin, new MerchantAdmin()), parseModel(valCode, new ValCode())));
    }

    @RequestMapping(value = "/profile")
    public ModelAndView profile() {
        return feedback(merchantAdminService.profile());
    }

    @RequestMapping(value = "/merchantAdmins")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView admins(String merchantAdminQo) throws ServiceException {
        return feedback(merchantAdminService.admins(parseModel(merchantAdminQo, new MerchantAdminQo()),AdminType.MERCHANT));
    }

    @RequestMapping(value = "/roles")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView roles(String merchantRoleQo) throws ServiceException {
        return feedback(merchantAdminService.roles(parseModel(merchantRoleQo, new MerchantRoleQo()), AdminType.MERCHANT));
    }

    @RequestMapping(value = "/role")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView role(Integer id) throws ServiceException {
        return feedback(merchantAdminService.merchantRole(id));
    }

    @RequestMapping(value = "/merchantAdmin")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchantAdmin(Integer id) throws ServiceException {
        return feedback(merchantAdminService.merchantAdmin(id, false));
    }
    @RequestMapping(value = "/save_merchantAdmin")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView saveMerchantAdmin(String merchantAdmin,AdminType adminType) {
        merchantAdminService.saveMerchantAdmin(parseModel(merchantAdmin, new MerchantAdmin()),AdminType.MERCHANT);
        return feedback();
    }

    @RequestMapping(value = "/merchantAdmin_sessions")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchantAdminSessions(String merchantAdminSessionQo) {
        return feedback(merchantAdminService.merchantAdminSessions(parseModel(merchantAdminSessionQo, new MerchantAdminSessionQo())));
    }
    @RequestMapping(value = "/permissions")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView permissions() throws ServiceException {
        return feedback(MerchantPermissionVO.initAdmPermissions());
    }
    @RequestMapping(value = "/save_merchantRole")
    @RequiredPermission(AdminPermission.ROLE_EDIT)
    public ModelAndView saveMerchantRole(String role) {
        merchantAdminService.saveRole(parseModel(role, new MerchantRole()),AdminType.MERCHANT);
        return feedback(null);
    }

}
