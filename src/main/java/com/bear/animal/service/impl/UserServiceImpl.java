package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.AttentionReq;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.controller.req.UpdateUserMessageReq;
import com.bear.animal.controller.res.AttentionMessage;
import com.bear.animal.controller.res.LoginResult;
import com.bear.animal.controller.res.RegisterResult;
import com.bear.animal.dao.entity.AttentionEntity;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.AttentionRepository;
import com.bear.animal.dao.repository.ImageRepository;
import com.bear.animal.dao.repository.UserRepository;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.except.BusinessException;
import com.bear.animal.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口实现
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttentionRepository attentionRepository;

    @Autowired
    private ImageRepository imageRepository;

    // 密码加密
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        userEntity.setAttention_count(0);
        userEntity.setFollow_count(0);
        UserEntity res = userRepository.save(userEntity);
        if (null == res) {
            // 注册失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 注册成功
        RegisterResult registerResult = new RegisterResult();
        BeanUtils.copyProperties(userEntity, registerResult);
        return Result.success(registerResult);
    }

    /**
     * 更新用户信息
     *
     * @param updateUserMessage 更新信息
     * @return
     */
    @Transactional
    @Override
    public Result updateUserMessage(UpdateUserMessageReq updateUserMessage) {
        Long userId = updateUserMessage.getUser_id();
        String password = passwordEncoder.encode(updateUserMessage.getPassword()).toString();
        String introduction = updateUserMessage.getUser_introduction();
        int effectRow = userRepository.updateByUserId(userId, password, introduction);
        if (effectRow == 0) {
            // 更新失败
            return Result.failure(ResultCode.USER_UPDATE_FAIL);
        }
        // 更新成功
        Optional<UserEntity> userList = userRepository.findById(userId);
        if (userList == null) {
            // 发生系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        return Result.success(userList.get());
    }

    /**
     * 关注/取消用户
     *
     * @param attentionData
     * @return
     */
    @Transactional
    @Override
    public Result attentionUser(AttentionReq attentionData) {
        int state = attentionData.getState();
        if (state == 0) {
            // 0: 取消关注
            int effectRow = attentionRepository.deleteByUserIdAndAttentionUserId(attentionData.getUser_id(),
                    attentionData.getAttention_user_id());
            if (effectRow == 0) {
                return Result.failure(ResultCode.FAIL);
            }
            // 更新用户粉丝数
            userRepository.updateFollowCountByUserId(attentionData.getAttention_user_id(), -1);
            // 更新用户关注数
            userRepository.updateAttentionCountByUserId(attentionData.getUser_id(), -1);
        } else {
            // 1: 关注
            AttentionEntity attentionEntity = new AttentionEntity();
            UserEntity userEntity = new UserEntity();
            userEntity.setUser_id(attentionData.getUser_id());
            attentionEntity.setUser(userEntity);
            attentionEntity.setAttention_user_id(attentionData.getAttention_user_id());
            AttentionEntity res = attentionRepository.save(attentionEntity);
            if (null == res) {
                return Result.failure(ResultCode.FAIL);
            }
            // 更新用户粉丝数
            userRepository.updateFollowCountByUserId(attentionData.getAttention_user_id(), 1);
            // 更新用户关注数
            userRepository.updateAttentionCountByUserId(attentionData.getUser_id(), 1);
        }
        return Result.success();
    }

    /**
     * 获取关注用户列表
     *
     * @param userId 用户id
     * @param offset 偏移量
     * @param size 偏移量
     * @return
     */
    @Transactional
    @Override
    public Result getAttentionUserList(Long userId, Integer offset, Integer size) {
        // 获取关注用户id
        List<Long> attentionUserIdList = attentionRepository.findByUserIdOffset(userId, offset, size);

        // 根据用户id获取图片
        List<AttentionMessage> res = new ArrayList<>();
        for (Long id : attentionUserIdList) {
            List<ImageEntity> imageList = imageRepository.findByUserId(id);
            UserEntity user = new UserEntity();
            if (imageList == null) {
                user = userRepository.findById(id).get();
            } else {
                user = imageList.get(0).getUser();
            }
            AttentionMessage attentionMessage = new AttentionMessage();
            attentionMessage.setUser(user);
            attentionMessage.setImageList(imageList);
            res.add(attentionMessage);
        }
        return Result.success(res);
    }

    /**
     * 获取粉丝列表
     *
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Result getFollowUserList(Long userId, Integer offset, Integer size) {
        List<AttentionEntity> attentionUserList = attentionRepository.findByAttentionUserIdOffset(userId, offset, size);
        List<UserEntity> userListRes = new ArrayList<>();
        for (AttentionEntity attentionEntity : attentionUserList) {
            userListRes.add(attentionEntity.getUser());
        }
        return Result.success(userListRes);
    }
}
