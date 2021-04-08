package com.bear.animal.controller;

import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.controller.req.UpdateUserMessageReq;
import com.bear.animal.service.IUserService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户操作控制层
 */
@Controller
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录请求
     * @param loginMessage 登录数据
     * @param <T> 返回泛型
     * @return
     */
    @PostMapping(value = "/user/login")
    @ResponseBody
    @ResponseResult
    public <T> T login(@RequestBody LoginReq loginMessage) {
        log.info("loginMessage: {}", loginMessage);
        return (T) userService.login(loginMessage);
    }

    /**
     * 用户注册请求
     * @param registerMessage 注册信息
     * @param <T> 返回泛型
     * @return
     */
    @PostMapping("/user/register")
    @ResponseBody
    @ResponseResult
    public <T> T register(@RequestBody RegisterReq registerMessage) {
        log.info("registerMessage: {}", registerMessage);
        return (T) userService.register(registerMessage);
    }

    /**
     * 更新用户信息请求
     * @param updateUserMessage 用户信息
     * @param <T>
     * @return
     */
    @PostMapping("/user/update_message")
    @ResponseBody
    @ResponseResult
    public <T> T updateUserMessage(@RequestBody UpdateUserMessageReq updateUserMessage) {
        log.info("updateUserMessage: {}", updateUserMessage);
        return (T) userService.updateUserMessage(updateUserMessage);
    }

}
