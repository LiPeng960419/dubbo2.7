package com.lipeng.consumerdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng
 * @Date: 2020/12/11 17:23
 */
@Component
@Slf4j
public class DubboConfig {

	@Bean("prodRegistryConfig")
	@ConfigurationProperties(prefix = "dubbo.registries.prod")
	public RegistryConfig prodRegistryConfig() {
		return new RegistryConfig();
	}

	@Bean("grayRegistryConfig")
	@ConfigurationProperties(prefix = "dubbo.registries.gray")
	public RegistryConfig grayRegistryConfig() {
		return new RegistryConfig();
	}

}