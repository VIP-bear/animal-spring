package com.bear.animal.service;

/**
 * 用户行为服务层接口
 * @author bear
 * @date 2021年04月10日 11:56
 */
public interface IBehaviorService {

    /**
     * 更新用户行为权重
     * @param userId 用户id
     * @param imageId 图片id
     * @param score 权重值
     * @return
     */
    void updateBehaviorScore(Long userId, Long imageId, int score);

}
