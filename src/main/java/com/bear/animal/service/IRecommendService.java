package com.bear.animal.service;

import com.bear.animal.controller.Result;

/**
 * 推荐服务层接口
 * @author bear
 * @date 2021年04月11日 12:18
 */
public interface IRecommendService {

    /**
     * 推荐图片给用户
     * @param userId
     * @return
     */
    Result getRecommendImage(Long userId);
}
