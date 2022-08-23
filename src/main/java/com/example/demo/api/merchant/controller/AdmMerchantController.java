package com.example.demo.api.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.qo.MerchantAdminQo;
import com.example.demo.api.merchant.qo.MerchantQo;
import com.example.demo.api.merchant.qo.MerchantRoleQo;
import com.example.demo.api.merchant.service.MerchantAdminService;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.renew.model.Renew;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import com.example.demo.common.exception.ServiceException;

@Controller
@RequestMapping(value = "/adm/merchant")
@RequiredAdminType(AdminType.ADMIN)

public class AdmMerchantController extends BaseController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantAdminService merchantAdminService;

    @RequestMapping(value = "/save")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView save(String merchant) {
        merchantService.saveMerchant(parseModel(merchant, new Merchant()));
        return feedback();

    }

    @RequestMapping(value = "/merchant-list")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchants(String merchantQo) {
        return feedback(merchantService.merchants(parseModel(merchantQo, new MerchantQo())));
    }

    @RequestMapping(value = "/merchants")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchantLess(String merchantQo) {
        return feedback(merchantService.merchantsLess(parseModel(merchantQo, new MerchantQo())));
    }

    @RequestMapping(value = "/item")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchant(Integer id) throws Exception {
        return feedback(merchantService.merchant(id));
    }

    @RequestMapping(value = "/merchant_status")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchantStatus(Integer id, Byte status) {
        merchantService.merchantStatus(id, status);
        return feedback();
    }

    @RequestMapping(value = "/save_merchantAdmin")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView saveMerchantAdmin(String merchantAdmin) {
        merchantAdminService.saveMerchantAdmin(parseModel(merchantAdmin, new MerchantAdmin()),AdminType.ADMIN);
        return feedback();
    }

    @RequestMapping(value = "/roles")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView roles(String merchantRoleQo) throws ServiceException {
        return feedback(merchantAdminService.roles(parseModel(merchantRoleQo, new MerchantRoleQo()), AdminType.ADMIN));
    }

    @RequestMapping(value = "/merchant_admins")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView merchantAdmins(String merchantAdminQo) {
        return feedback(
                merchantAdminService.admins(parseModel(merchantAdminQo, new MerchantAdminQo()), AdminType.ADMIN));
    }

    @RequestMapping(value = "/create")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView create(String renew) {
        merchantService.create(parseModel(renew, new Renew()));
        return feedback();
    }

    @RequestMapping(value = "/validThru")
    @RequiredPermission(AdminPermission.MERCHANT_LIST)
    public ModelAndView validThru(Integer day) {
        return feedback(merchantService.validThru(day));
    }

}
