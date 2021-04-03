package com.bear.animal.service;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;

/**
 * 用户服务接口
 */
public interface IUserService {

    /**
     * 登录
     * @param loginMessage 登录信息
     * @return
     */
    Result login(LoginReq loginMessage);

    /**
     * 注册
     * @param registerMessage 注册信息
     * @return
     */
    Result register(RegisterReq registerMessage);

}
