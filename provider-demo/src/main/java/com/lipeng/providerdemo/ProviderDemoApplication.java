package com.lipeng.providerdemo;

import com.lipeng.common.utils.SpringContextUtils;
import com.lipeng.common.vo.ResultVo;
import java.util.Map;
import org.apache.dubbo.config.AbstractConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDubbo
@ComponentScan({"com.lipeng.common", "com.lipeng.providerdemo"})
@RestController
public class ProviderDemoApplication {

	public static void main(String[] args) {
		new SpringApplication(ProviderDemoApplication.class).run(args);
	}

	@Value("${app.name:error}")
	private String name;

	@GetMapping
	public ResultVo resultVo() {
		Map<String, AbstractConfig> beansOfType = SpringContextUtils.getBeansByClass(AbstractConfig.class);
		return ResultVo.success(name);
	}

}