package com.example.demo.common.sms;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements ISmsService {

    @Autowired
    private SmsConfig config;

    @Override
    public boolean send(SmsTpl smsTpl) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(), config.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", smsTpl.getPhoneNumbers());
        request.putQueryParameter("SignName", smsTpl.getSignName());
        request.putQueryParameter("TemplateCode", smsTpl.getTemplateCode());
        request.putQueryParameter("TemplateParam", smsTpl.getTemplateParam());

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

            if (response.getData().equals("OK")) {
                return true;
            }

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
