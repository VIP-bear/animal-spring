package com.bear.animal.controller;

import com.bear.animal.controller.req.AttentionReq;
import com.bear.animal.controller.req.LoginReq;
import com.bear.animal.controller.req.RegisterReq;
import com.bear.animal.controller.req.UpdateUserMessageReq;
import com.bear.animal.service.IRecommendService;
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

    @Autowired
    private IRecommendService recommendService;

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

    /**
     * 关注/取消用户
     * @param attentionData
     * @param <T>
     * @return
     */
    @PostMapping("/user/attention")
    @ResponseBody
    @ResponseResult
    public <T> T attentionUser(@RequestBody AttentionReq attentionData) {
        log.info("attentionData: {}", attentionData);
        return (T) userService.attentionUser(attentionData);
    }

    /**
     * 根据用户id获取关注用户列表
     * @param user_id
     * @param offset
     * @param <T>
     * @return
     */
    @GetMapping("/user/attention_list/{user_id}/{offset}")
    @ResponseBody
    @ResponseResult
    public <T> T attentionUserList(@PathVariable("user_id") String user_id,
                                   @PathVariable("offset") String offset) {
        log.info("user_id: {}, offset: {}", user_id, offset);
        Long userId = Long.parseLong(user_id);
        Integer offsetNum = Integer.parseInt(offset);
        return (T) userService.getAttentionUserList(userId, offsetNum, 10);
    }

    /**
     * 根据用户id获取粉丝列表
     * @param user_id
     * @param offset
     * @param <T>
     * @return
     */
    @GetMapping("/user/follow_list/{user_id}/{offset}")
    @ResponseBody
    @ResponseResult
    public <T> T followUserList(@PathVariable("user_id") String user_id,
                                @PathVariable("offset") String offset) {
        log.info("user_id: {}, offset: {}", user_id, offset);
        Long userId = Long.parseLong(user_id);
        Integer offsetNum = Integer.parseInt(offset);
        return (T) userService.getFollowUserList(userId, offsetNum, 10);
    }

    /**
     * 根据用户id获取用户上传图片列表
     * @param user_id
     * @param offset
     * @param <T>
     * @return
     */
    @GetMapping("/user/upload_image/{user_id}/{offset}")
    @ResponseBody
    @ResponseResult
    public <T> T getUploadImageList(@PathVariable("user_id") String user_id,
                                    @PathVariable("offset") String offset) {
        log.info("user_id: {}, offset: {}", user_id, offset);
        Long userId = Long.parseLong(user_id);
        Integer offsetNum = Integer.parseInt(offset);
        return (T) userService.getUploadImageList(userId, offsetNum, 10);
    }

    /**
     * 根据用户id获取用户信息
     * @param user_id
     * @param <T>
     * @return
     */
    @GetMapping("/user/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getUserMessage(@PathVariable("user_id") String user_id) {
        log.info("userId: {}", user_id);
        Long userId = Long.parseLong(user_id);
        return (T) userService.getUserMessage(userId);
    }

    @GetMapping("/user/recommend_user/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getRecommendUser(@PathVariable("user_id") String user_id) {
        log.info("userId: {}", user_id);
        Long userId = Long.parseLong(user_id);
        return (T) recommendService.getRecommendUser(userId, 6);
    }

}
