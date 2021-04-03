package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.UserRepository;
import com.bear.animal.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务接口实现
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 登录
     *
     * @param loginMessage 登录信息
     * @return
     */
    @Override
    public Result login(LoginReq loginMessage) {
        UserEntity userEntity = userRepository.findByUsernameAndPassword(loginMessage.getUsername(), loginMessage.getPassword());
        return null;
    }

    /**
     * 注册
     *
     * @param registerMessage 注册信息
     * @return
     */
    @Override
    public Result register(RegisterReq registerMessage) {
        return null;
    }
}
