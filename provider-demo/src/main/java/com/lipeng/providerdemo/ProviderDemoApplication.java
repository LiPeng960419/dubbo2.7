package com.lipeng.providerdemo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@ComponentScan({"com.lipeng.common", "com.lipeng.providerdemo"})
public class ProviderDemoApplication {

	public static void main(String[] args) {
		new SpringApplication(ProviderDemoApplication.class).run(args);
	}

}