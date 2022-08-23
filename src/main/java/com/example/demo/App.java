package com.example.demo;

import com.example.demo.common.reposiotry.BaseRepositoryFactoryBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
