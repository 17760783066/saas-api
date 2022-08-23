package com.example.demo.api.article.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.api.article.service.ArticleService;

@Component
public class TagSchedulerTask {

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    private void doSyncTag() {

        articleService.syncTagWeight();
    }

}
