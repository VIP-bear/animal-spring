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

    /**
     * 推荐与图片相关联的图片
     * @param imageId
     * @param offset
     * @param size
     * @return
     */
    Result getRecommendRelatedImage(Long imageId, int offset, int size);

    /**
     * 推荐用户
     * @param userId
     * @return
     */
    Result getRecommendUser(Long userId, int size);

    /**
     * 搜索图片
     * @param searchName
     * @return
     */
    Result searchImage(String searchName, int offset, int size);
}
