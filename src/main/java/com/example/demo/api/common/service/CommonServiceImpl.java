package com.example.demo.api.common.service;

import com.example.demo.api.admin.service.AdminService;
import com.example.demo.api.common.entity.ValCodeConstants;
import com.example.demo.api.common.entity.WordsWrap;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.common.L;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.cache.SingleRepositoryProvider;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.service.TaskService;
import com.example.demo.common.sms.ISmsService;
import com.example.demo.common.sms.SmsTpl;
import com.example.demo.common.util.ByteUtils;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.DateUtils;
import com.example.demo.common.util.SimpleHttpClient;
import com.example.demo.common.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.sunnysuperman.commons.bean.Bean;
import com.sunnysuperman.commons.util.FormatUtil;
import com.sunnysuperman.commons.util.JSONUtil;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Long, ValCode> valCodeCache;

    private KvCacheWrap<Byte, String> accessTokenCache;
    @Value("${baiduocr.client-id}")
    private String clientId;

    @Value("${baiduocr.client-secret}")
    private String clientSecret;





    @PostConstruct
    public void init() {
        valCodeCache = kvCacheFactory.create(new CacheOptions("val_code", 1, 600), null,
                new BeanModelConverter<>(ValCode.class));
        accessTokenCache = kvCacheFactory.create(
                new CacheOptions("baiduocr_access_token", 1, 30 * DateUtils.SECOND_PER_DAY),
                new SingleRepositoryProvider<Byte, String>() {

                    @Override
                    public String findByKey(Byte key) throws ServiceException {
                        return getAccessToken();
                    }
                }, new BeanModelConverter<>(String.class));
    }

    @Override
    public void saveValCode(Long key, ValCode valCode) {
        valCodeCache.save(key, valCode);
    }

    @Override
    public ValCode getValCode(Long key) {
        ValCode valCode = valCodeCache.findByKey(key);
        if (valCode == null) {
            throw new ServiceException(ErrorCode.ERR_VALCODE);
        }
        return valCode;
    }

    @Override
    public void genValCode(ValCode valCode, String smsTplCode) {

        boolean isEmail = valCode.getAccountType() == ValCodeConstants.EMAIL;

        if (valCode.getUserType() == ValCodeConstants.ADMIN) {
            adminService.findByAccount(valCode);
        }
        String code = StringUtils.getRandNum(6);
        valCode.setCode(code);
        saveValCode(valCode.getKey(), valCode);
        if (isEmail) {
            // TODO
        } else {

            SmsTpl tpl = new SmsTpl(smsTplCode, valCode.getAccount(),
                    JSON.toJSONString(CollectionUtils.arrayAsMap("code", code)));

            taskService.addTask(new SendSmsTask(smsService, tpl));
        }
    }

    @Override
    public void resetPassword(ValCode valCode, String password) {

        ValCode vc = getValCode(valCode.getKey());

        if (!(valCode.getUserType().intValue() == vc.getUserType()
                && valCode.getAccountType().intValue() == vc.getAccountType()
                && valCode.getAccount().equals(vc.getAccount()) && valCode.getCode().equals(vc.getCode()))) {
            throw new ServiceException(ErrorCode.ERR_VALCODE);
        }

        if (valCode.getUserType() == ValCodeConstants.ADMIN) {
            adminService.resetPassword(valCode, password);
        }
    }

    @Override
    public Map geocoder(String lat, String lng, String key) {
        String url = "http://apis.map.qq.com/ws/geocoder/v1/?location=" + lat + "," + lng + "&key=" + key;

        Map<String, Object> params = new HashMap<>();

        SimpleHttpClient client = new SimpleHttpClient();
        String responseString = "";
        try {
            responseString = client.get(url, params, CollectionUtils.arrayAsMap());
        } catch (Exception e) {
            throw new ArgumentServiceException("geocode");
        }
        if (L.isInfoEnabled()) {
            L.info("mbyApi - " + url + ", result: " + responseString);
        }
        Map<?, ?> response = JSONUtil.parseJSONObject(responseString);
        Integer status = FormatUtil.parseInteger(response.get("status"));
        if (L.isInfoEnabled()) {
            L.info("response status = " + status);
        }
        return response;
    }

    private String getAccessToken() {
        Map<String, Object> params = new HashMap<>();

        String path = "https://aip.baidubce.com/oauth/2.0/token";

        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "client_credentials");

        SimpleHttpClient client = new SimpleHttpClient();
        String responseString = "";
        try {
            responseString = client.post(path, params, CollectionUtils.arrayAsMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (L.isInfoEnabled()) {
            L.info("baiduApi - " + path + ", result: " + responseString);
        }
        Map<?, ?> response = JSONUtil.parseJSONObject(responseString);

        return response.get("access_token").toString();
    }

    @Override
    public List<String> getPayNumber(String url) {

        String accessToken = accessTokenCache.findByKey(ByteUtils.BYTE_1);

        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("url", url);
        params.put("detect_direction", "false");
        params.put("paragraph", "false");
        params.put("probability", "false");

        SimpleHttpClient client = new SimpleHttpClient();
        String responseString = "";
        try {
            responseString = client.post(path, params, Collections.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (L.isInfoEnabled()) {
            L.info("baiduApi - " + path + ", result: " + responseString);
        }
        Map<?, ?> response = JSONUtil.parseJSONObject(responseString);

        List<WordsWrap> list = Bean.fromJson(response.get("words_result").toString(), WordsWrap.class);

        return list.stream().filter(item -> item.getWords().matches("[0-9]+")).map(WordsWrap::getWords)
                .collect(Collectors.toList());
    }
    @Override
    public Map<String, Object> oem(Integer merchantId) {

        Merchant merchant = merchantService.getById(merchantId);
        return CollectionUtils.arrayAsMap("name", merchant.getName(), "oem", merchant.getOem());
    }


}
