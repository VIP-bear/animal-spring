package com.bear.animal.service.impl;

import com.bear.animal.dao.entity.BehaviorEntity;
import com.bear.animal.dao.repository.BehaviorRepository;
import com.bear.animal.service.IBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户行为服务层实现
 * @author bear
 * @date 2021年04月10日 12:05
 */
@Service
@Slf4j
public class BehaviorServiceImpl implements IBehaviorService {

    @Autowired
    private BehaviorRepository behaviorRepository;

    /**
     * 更新用户行为权重
     *
     * @param userId  用户id
     * @param imageId 图片id
     * @param score   权重值
     * @return
     */
    @Transactional
    @Override
    public void updateBehaviorScore(Long userId, Long imageId, int score) {
        log.info("userId: {}, imageId: {}", userId, imageId);
        // 根据用户id和图片id获取用户行为
        Long behaviorId = behaviorRepository.findByUserIdAndImageId(userId, imageId);
        log.info("behaviorId: {}", behaviorId);
        if (null != behaviorId) {
            // 更新用户权重值
            int effectRow = behaviorRepository.updateScoreByUserIdAndImageId(userId, imageId, score);
        } else {
            // 插入用户行为
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUser_id(userId);
            behaviorEntity.setImage_id(imageId);
            behaviorEntity.setUser_behavior_score(score);
            behaviorRepository.save(behaviorEntity);
        }
    }
}
