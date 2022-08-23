package com.example.demo.api.common.controller;

import com.example.demo.api.common.service.CommonService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.sms.SmsTpl;
import com.example.demo.common.util.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Autowired  
    private CommonService commonService;

    @RequestMapping(value = "/gen_valCode_signin")
    public void genValCode(HttpServletResponse response, Long key) {
        try {
            response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            String randomString = new RandomValidateCodeUtil().getRandcode(response, key);// 输出验证码图片方法
            commonService.saveValCode(key, new ValCode(randomString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/gen_valCode_reset_pwd")
    public ModelAndView genValCodeResetPwd(String valCode) throws Exception {
        commonService.genValCode(parseModel(valCode, new ValCode()), SmsTpl.TPL_RESET_PWD);
        return feedback(null);
    }

    @RequestMapping(value = "/gen_valCode")
    public ModelAndView genValCode(String valCode) throws Exception {
        commonService.genValCode(parseModel(valCode, new ValCode()), SmsTpl.TPL_VAL_CODE);
        return feedback(null);
    }

    @RequestMapping(value = "/geocoder")
    @RequiredAdminType(value = AdminType.NONE)
    public ModelAndView geocoder(String lat, String lng, String key) {
        return feedback(commonService.geocoder(lat, lng, key));
    }
    @RequestMapping(value = "/get_paynumber")
    public ModelAndView getPayNumber(String url) {
        return feedback(commonService.getPayNumber(url));
    }
    @RequestMapping("/oem")
    public ModelAndView oem(Integer merchantId) {
        return feedback(commonService.oem(merchantId));
    }


}
