package com.lipeng.providerdemo.service;

import com.lipeng.common.dto.UserDto;
import com.lipeng.common.entity.User;
import com.lipeng.common.interfaces.UserService;
import com.lipeng.common.mapstruct.UserMapper;
import com.lipeng.common.vo.ResultVo;
import com.lipeng.common.vo.UserVo;
import com.lipeng.providerdemo.filter.RedisRateLimitFilter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/23 16:38
 */
@Slf4j
@DubboService(version = "1.0.0", parameters = {Constants.TPS_LIMIT_RATE_KEY, "1", Constants.TPS_LIMIT_INTERVAL_KEY, "2000", RedisRateLimitFilter.LIMIT_TIME, "30", RedisRateLimitFilter.LIMIT_COUNT, "5"})
public class UserServiceImplV1 implements UserService {

    @Value("${server.port}")
    private int port;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUser(Long userId) {
        log.info("this is provider service,userId:" + userId + ",port:" + port);
        return "this is provider service,userId:" + userId + ",port:" + port;
    }

    public ResultVo<User> getUserV1Error(String name) {
        return ResultVo.fail("getUserV1 fallback");
    }

    @Override
    @HystrixCommand(fallbackMethod = "getUserV1Error")
    public ResultVo<User> getUserV1(String name) {
        // dubbo超时配置
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // 模拟服务异常  服务降级
//        if (Math.random() > 0.5) {
//            throw new RuntimeException();
//        }
        User user = new User();
        user.setName(name + "," + port);
        user.setPassword("UserServiceImplV1 passwordV1");
        return ResultVo.success(user);
    }

    @Override
    public ResultVo getUserV2(String name) {
        User user = new User();
        user.setName(name);
        user.setPassword("UserServiceImplV1 passwordV2");
        return ResultVo.success(user);
    }

    @Override
    public ResultVo getUserV3(UserVo userVo) {
        UserDto dto = userMapper.convert(userVo);
        User user = userMapper.convert(dto);
        return ResultVo.success(user);
    }

    @Override
    public ResultVo getUserV4(UserVo userVo) {
        UserDto dto = userMapper.convert(userVo);
        User user = userMapper.convert(dto);
        return ResultVo.success(user);
    }

}