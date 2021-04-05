package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.controller.res.LoginResult;
import com.bear.animal.controller.res.RegisterResult;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.UserRepository;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.except.BusinessException;
import com.bear.animal.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * 用户服务接口实现
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    // 密码加密
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    // 设置redis缓存中的key
    private final String key = "sessionId";

    /**
     * 登录
     *
     * @param loginMessage 登录信息
     * @return
     */
    @Override
    public Result login(LoginReq loginMessage) {
        UserEntity userEntity;
        if (loginMessage.getUsername().endsWith(".com")) {
            // 根据邮箱查找用户
            userEntity = userRepository.findByEmail(loginMessage.getUsername());
        } else {
            // 根据用户名查找用户
            userEntity = userRepository.findByUsername(loginMessage.getUsername());
        }
        if (null == userEntity || !passwordEncoder.matches(loginMessage.getPassword(), userEntity.getPassword())) {
            // 用户名或者密码错误
            return Result.failure(ResultCode.USER_LOGIN_ERROR);
        }
        // 登录成功
        LoginResult loginResult = new LoginResult();
        BeanUtils.copyProperties(userEntity, loginResult);
        // 随机生成一个sessionId
        String sessionId = UUID.randomUUID().toString();
        loginResult.setSession_id(sessionId);
        // 将sessionId存入redis数据库中(key为sessionId, value为用户信息)
        redisTemplate.boundHashOps(key).put(sessionId, loginResult);
        return Result.success(loginResult);
    }

    /**
     * 注册
     *
     * @param registerMessage 注册信息
     * @return
     */
    @Transactional
    @Override
    public Result register(RegisterReq registerMessage) {
        UserEntity userEntity;
        // 根据邮箱查找用户
        userEntity = userRepository.findByEmail(registerMessage.getEmail());
        if (null != userEntity) {
            // 该邮箱已被注册
            return Result.failure(ResultCode.USER_EMAIL_EXIST);
        }
        userEntity = userRepository.findByUsername(registerMessage.getUsername());
        if (null != userEntity) {
            // 该用户已存在
            return Result.failure(ResultCode.USER_HAS_EXIST);
        }

        // 注册账号
        // 先进行直接注册，以后有时间可以进行邮箱验证注册
        userEntity = new UserEntity();
        userEntity.setUsername(registerMessage.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerMessage.getPassword()));
        userEntity.setEmail(registerMessage.getEmail());
        userEntity.setCreate_time(new Timestamp(System.currentTimeMillis()));
        UserEntity res = userRepository.save(userEntity);
        if (null == res) {
            // 注册失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 注册成功
        RegisterResult registerResult = new RegisterResult();
        BeanUtils.copyProperties(userEntity, registerResult);
        // 随机生成一个sessionId
        String sessionId = UUID.randomUUID().toString();
        registerResult.setSession_id(sessionId);
        // 将sessionId存入redis数据库中(key为sessionId, value为用户信息)
        redisTemplate.boundHashOps(key).put(sessionId, registerResult);
        return Result.success(registerResult);
    }

}