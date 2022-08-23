package com.example.demo.common.controller;

import com.example.demo.common.L;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.commons.bean.Bean;
import com.sunnysuperman.commons.bean.ParseBeanOptions;
import com.sunnysuperman.commons.util.FormatUtil;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BaseController implements WebappKeys {

    protected static ModelAndView feedback() {
        return feedback(null);
    }

    protected static ModelAndView feedback(Object ret) {
        Object result = ret != null ? ret : "success";
        Map<String, Object> data = new HashMap<>();
        data.put("errcode", 0);
        data.put("result", result);
        return new ModelAndView(new JsonView(data));
    }

    protected static int parseInt(Integer i) {
        return FormatUtil.parseIntValue(i, 0);
    }

    protected static int parseInt(Integer i, int defaultValue) {
        return FormatUtil.parseIntValue(i, defaultValue);
    }

    protected static boolean parseBoolean(String b) {
        return FormatUtil.parseBoolean(b, false);
    }

    protected static boolean parseBoolean(String b, boolean defaultValue) {
        return FormatUtil.parseBoolean(b, defaultValue);
    }

    protected static Date parseDate(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return FormatUtil.parseDate(s);
    }

    public static <T> T parseModel(String modelJSON, T model) throws ServiceException {
        return parseModel(modelJSON, model, null, null);
    }

    protected static <T> T parseModel(String modelJSON, T model, String key) throws ServiceException {
        return parseModel(modelJSON, model, key, null);
    }

    protected static <T> T parseModel(String modelJSON, T model, ParseBeanOptions options) throws ServiceException {
        return parseModel(modelJSON, model, null, options);
    }

    protected static <T> T parseModel(String modelJSON, T model, String key, ParseBeanOptions options)
            throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model, options);
        } catch (Exception e) {
            L.error(e);
            throw new ArgumentServiceException(key != null ? key : "model");
        }
    }

    protected static <T> T parseModel(HttpServletRequest request, T bean) {
        return parseModel(request, bean, null);
    }

    protected static <T> T parseModel(HttpServletRequest request, T bean, Collection<String> excludeKeys) {
        Enumeration<?> enu = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean shouldExcludeKeys = excludeKeys != null && !excludeKeys.isEmpty();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement().toString();
            if (shouldExcludeKeys && excludeKeys.contains(key)) {
                continue;
            }
            String value = FormatUtil.parseString(request.getParameter(key));
            value = StringUtils.trimToNull(value);
            if (value == null) {
                continue;
            }
            map.put(key, value);
        }
        return Bean.fromMap(map, bean);
    }

    protected static <T> List<T> parseList(String modelJSON, Class<T> model) throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model);
        } catch (Exception e) {
            throw new ArgumentServiceException("model");
        }
    }

    protected static <T> List<T> parseList(String modelJSON, Class<T> model, String msg) throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model);
        } catch (Exception e) {
            throw new ArgumentServiceException(msg);
        }
    }

    protected static String getHeader(HttpServletRequest request, String key) {
        String value = WebUtils.getHeader(request, key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    HttpServletRequest getRequest() {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ra.getRequest();
    }

    protected String getRemoteAddress() {
        return WebUtils.getRemoteAddress(getRequest());
    }

}
