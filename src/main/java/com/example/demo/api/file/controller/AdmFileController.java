package com.example.demo.api.file.controller;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.file.service.FileService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/adm/file")
@RequiredAdminType(AdminType.ADMIN)
public class AdmFileController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload_token")
    @RequiredPermission(AdminPermission.NONE)
    public ModelAndView uploadToken(String namespace, String fileName, int fileSize) throws Exception {
        return feedback(fileService.uploadToken(namespace, fileName, fileSize, true));
    }
    @RequestMapping(value = "/img_upload_watermark")
    @RequiredPermission(AdminPermission.SETTING)
    public ModelAndView uploadWithWaterMark(MultipartFile file) throws Exception {
        return feedback(fileService.uploadWithWaterMark(file, AdminType.ADMIN));
    }

}
