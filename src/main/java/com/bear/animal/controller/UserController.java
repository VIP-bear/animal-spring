package com.bear.animal.controller;

import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.service.IUserService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户操作控制层
 */
@Controller
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/user/login")
    @ResponseBody
    @ResponseResult
    public <T> T login(LoginReq loginMessage) {
        return (T)userService.login(loginMessage);
    }

}
