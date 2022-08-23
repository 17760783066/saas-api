package com.example.demo.api.img.controller;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.img.model.ArtisanCaseImg;
import com.example.demo.api.img.qo.ArtisanCaseImgQo;
import com.example.demo.api.img.service.ArtisanCaseImgService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/img")
@RequiredAdminType(AdminType.ADMIN)
public class ArtisanCaseImgController extends BaseController{
    @Autowired
    private ArtisanCaseImgService artisanCaseImgService;
    @RequestMapping(value = "/save_img")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView save(String artisanCaseImg) {
        artisanCaseImgService.save(parseModel(artisanCaseImg, new ArtisanCaseImg()));
        return feedback();
    }
    @RequestMapping(value = "/artisanCaseImgs")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView qo(String artisanCaseImgQo) {
        
        return feedback(artisanCaseImgService.qo(parseModel(artisanCaseImgQo, new ArtisanCaseImgQo())));
    } 
    @RequestMapping(value = "/artisanCaseImg")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView one(Integer id) {
        
        return feedback(artisanCaseImgService.one(id));
    }
    @RequestMapping(value = "/remove")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView remove(Integer id) {
        artisanCaseImgService.remove(id);
        return feedback();
    }
    @RequestMapping(value = "/status")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView status(Integer id,Byte status) {
        artisanCaseImgService.status(id,status);
        return feedback();
    }
}
