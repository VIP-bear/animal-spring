package com.bear.animal.service;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.AttentionReq;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.controller.req.UpdateUserMessageReq;

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

    /**
     * 更新用户信息
     * @param updateUserMessage 更新信息
     * @return
     */
    Result updateUserMessage(UpdateUserMessageReq updateUserMessage);

    /**
     * 关注/取消用户
     * @param attentionData
     * @return
     */
    Result attentionUser(AttentionReq attentionData);

    /**
     * 获取关注用户列表
     * @param userId 用户id
     * @param offset 偏移量
     * @return
     */
    Result getAttentionUserList(Long userId, Integer offset, Integer size);

    /**
     * 获取粉丝列表
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    Result getFollowUserList(Long userId, Integer offset, Integer size);
}
