package com.lipeng.providerdemo.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng
 * @Date: 2020/12/11 17:23
 */
@Component
@Slf4j
public class DubboConfig {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @PostConstruct
    public void registerShutdownHook() {
        log.info("[SpringBootShutdownHook] Register ShutdownHook....");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                int timeOut = Integer.parseInt(ConfigUtils.getProperty("dubbo.service.shutdown.wait", "2000"));
                log.info("[SpringBootShutdownHook] Application need sleep {} seconds to wait Dubbo shutdown", (double) timeOut / 1000.0D);
                Thread.sleep(timeOut);
                configurableApplicationContext.close();
                log.info("[SpringBootShutdownHook] ApplicationContext closed, Application shutdown");
            } catch (InterruptedException e) {
                log.info("[SpringBootShutdownHook] Dubbo shutdown hook close error");
            }
        }));
    }

}