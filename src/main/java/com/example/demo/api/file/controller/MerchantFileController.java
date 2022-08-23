package com.example.demo.api.file.controller;

import com.example.demo.api.file.service.FileService;
import com.example.demo.api.merchant.entity.MerchantPermission;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredMerchantPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/merchant/file")
@RequiredAdminType(AdminType.MERCHANT)
public class MerchantFileController extends BaseController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload_token")
    @RequiredMerchantPermission(MerchantPermission.NONE)
    public ModelAndView uploadToken(String namespace, String fileName, int fileSize) throws Exception {
        return feedback(fileService.uploadToken(namespace, fileName, fileSize, true));
    }

}
