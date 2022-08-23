package com.example.demo.api.common.service;

import com.example.demo.common.sms.ISmsService;
import com.example.demo.common.sms.SmsTpl;
import com.example.demo.common.task.ApiTask;

public class SendSmsTask extends ApiTask {

    private ISmsService smsService;
    private SmsTpl tpl;

    public SendSmsTask(ISmsService smsService, SmsTpl tpl) {
        super();
        this.smsService = smsService;
        this.tpl = tpl;
    }

    @Override
    protected void doApiWork() {
        smsService.send(tpl);
    }
}