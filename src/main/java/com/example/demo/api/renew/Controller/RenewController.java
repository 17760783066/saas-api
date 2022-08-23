package com.example.demo.api.renew.Controller;


import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.renew.model.Renew;
import com.example.demo.api.renew.qo.RenewQo;
import com.example.demo.api.renew.qo.RenewWo;
import com.example.demo.api.renew.service.RenewService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/renew")
@RequiredAdminType(AdminType.ADMIN)
public class RenewController extends BaseController {
    @Autowired
    private RenewService renewService;

    @RequestMapping(value = "/renews")
    @RequiredPermission(AdminPermission.RENEW_LIST)
    public ModelAndView renews(String renewQo) {
        return feedback(renewService.renews(parseModel(renewQo, new RenewQo()),
                RenewWo.getRenewListInstance()));
    }
    
    @RequestMapping(value = "/option")
    @RequiredPermission(AdminPermission.RENEW_LIST)
    public ModelAndView editStatus(String renew) {
        renewService.editStatus(parseModel(renew, new Renew())) ;
        return feedback();
    }
}
