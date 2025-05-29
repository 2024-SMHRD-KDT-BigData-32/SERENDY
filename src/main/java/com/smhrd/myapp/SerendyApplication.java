package com.smhrd.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.smhrd")
@EnableJpaRepositories(basePackages = "com.smhrd.repository")
@EntityScan("com.smhrd.entity") // 👈 꼭 추가!!!
public class SerendyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SerendyApplication.class, args);
	}

}
