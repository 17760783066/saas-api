package com.example.demo.api.ui.controller;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.ui.model.UI;
import com.example.demo.api.ui.qo.UIQo;
import com.example.demo.api.ui.service.UIService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/ui")
@RequiredAdminType(AdminType.ADMIN)
public class AdmUIController extends BaseController {
    @Autowired
    private UIService uiService;

    @RequestMapping(value = "/save")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView save(String ui) {
        uiService.save(parseModel(ui, new UI()));
        return feedback();
    }

    @RequestMapping(value = "/uis")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView uis(String uiQo) {
        return feedback(uiService.uis(parseModel(uiQo, new UIQo())));
    }

    @RequestMapping(value = "/ui")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView ui(Integer id) {
        return feedback(uiService.ui(id));
    }

    @RequestMapping(value = "/isDefault")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView status(Integer id, Byte isDefault) {
        uiService.status(id, isDefault);
        return feedback();
    }
}
