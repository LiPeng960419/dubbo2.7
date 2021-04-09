package com.lipeng.consumerdemo.controller;

import com.lipeng.common.entity.User;
import com.lipeng.common.interfaces.UserService;
import com.lipeng.common.vo.ResultVo;
import com.lipeng.common.vo.UserVo;
import com.lipeng.consumerdemo.config.DubboReferenceFactory;
import com.lipeng.consumerdemo.config.DubboReferenceUtils;
import com.lipeng.consumerdemo.config.SpringReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/23 16:41
 */
@RestController
@Slf4j
public class OrderController {

    /*
    check 检查提供方服务是否可用
    timeout 调用提供方延时
    retries 第一次失败后 之后重试次数
    version 使用提供方的版本 可以指定 *表示任意
    url dubbo直连服务提供方  url为提供方路径 如127.0.0.1:7001
    loadbalance 负载均衡 ramdon roundrobin leastactive 随机，轮询，最少活跃调用

    如果注册中心使用nacos 不支持version
     */
    @DubboReference(mock = "true", version = "*", stub = "com.lipeng.consumerdemo.service.UserServiceStub")
    private UserService userService;

    @DubboReference(mock = "true", version = "1.0.0", stub = "com.lipeng.consumerdemo.service.UserServiceStub")
    private UserService userService1;

    @DubboReference(mock = "true", version = "2.0.0", stub = "com.lipeng.consumerdemo.service.UserServiceStub")
    private UserService userService2;

    @Autowired
    private DubboReferenceFactory dubboReferenceFactory;

    @SpringReference(mock = "true", version = "1.0.0", stub = "com.lipeng.consumerdemo.service.UserServiceStub")
    private UserService userService3;

    @GetMapping("/nacos/{userId}")
    public ResultVo nacos(@PathVariable Long userId) {
        log.info(userService1.getUser(userId));
        log.info(userService2.getUser(userId));

        return userService1.getUserV1(String.valueOf(userId));
    }

    @GetMapping("/invoke")
    public ResultVo invoke() {
        Map map = new HashMap<>();
        map.put("ParamType", "java.lang.Long");  //后端接口参数类型
        map.put("Object", 1);  //用以调用后端接口的实参
        Object getUser = DubboReferenceUtils.genericInvoke(UserService.class, "getUser", Collections.singletonList(map));
        return ResultVo.success(getUser);
    }

    @GetMapping("/gray")
    public ResultVo gray() {
//        UserService bean = dubboReferenceFactory.getDubboBean(UserService.class, "1.0.0");
//        String user = bean.getUser(345L);
//        UserService dubboBean = DubboReferenceUtils.getGrayDubboBean(UserService.class, "1.0.0");
//        String user = dubboBean.getUser(123L);
        String user = userService3.getUser(123L);
        return ResultVo.success(user);
    }

    @GetMapping("/v1/nacos/{userId}")
    public ResultVo v1(@PathVariable Long userId) {
        try {
            RpcContext.getContext().setAttachment("userId", "123");
            return userService1.getUserV1(String.valueOf(userId));
        }finally {
            RpcContext.getContext().clearAttachments();
        }
    }

    @GetMapping("/v2/nacos/{userId}")
    public ResultVo v2(@PathVariable Long userId) {
        return userService2.getUserV1(String.valueOf(userId));
    }

    @GetMapping("/order/{userId}")
    public String order(@PathVariable Long userId) {
        log.info("OrderController order userId:" + userId);
        return userService.getUser(userId);
    }

    /**
     * v1和v2返回都是对象User
     *
     * @param name
     * @return
     */
    @GetMapping("/getUserV1")
    public ResultVo getUserV1(String name) {
        ResultVo<User> userV1 = userService.getUserV1(name);
        return userV1;
    }

    @GetMapping("/getUserV2")
    public ResultVo getUserV2(String name) {
        ResultVo userV2 = userService.getUserV2(name);
        return userV2;
    }

    @GetMapping("/getUserV3")
    public ResultVo getUserV3(UserVo vo) {
        ResultVo userV3 = userService.getUserV3(vo);
        return userV3;
    }

    @GetMapping("/getUserV4")
    public ResultVo getUserV4(@RequestBody UserVo vo) {
        ResultVo userV4 = userService.getUserV4(vo);
        return userV4;
    }

}