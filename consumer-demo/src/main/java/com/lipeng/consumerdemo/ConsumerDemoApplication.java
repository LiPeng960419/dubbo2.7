package com.lipeng.consumerdemo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@EnableHystrix
@ComponentScan({"com.lipeng.common", "com.lipeng.consumerdemo"})
public class ConsumerDemoApplication {

	/**
	 * dubbo2.7 修复了dubbo优雅关闭
	 * https://www.jianshu.com/p/69b704279066?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
	 * https://my.oschina.net/yangzhongyu/blog/3048619
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplication(ConsumerDemoApplication.class).run(args);
	}

}