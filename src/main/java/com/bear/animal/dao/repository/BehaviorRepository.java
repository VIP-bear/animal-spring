package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.BehaviorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行为数据库表操作
 * @author bear
 * @date 2021年04月09日 10:32
 */
@Repository
public interface BehaviorRepository extends JpaRepository<BehaviorEntity, Long> {

    /**
     * 根据用户id和图片id更新用户行为权重
     * @param userId
     * @param imageId
     * @param score
     * @return
     */
    @Modifying
    @Query(value = "update al_behavior set user_behavior_score = user_behavior_score + ?3 where user_id = ?1 and image_id = ?2", nativeQuery = true)
    int updateScoreByUserIdAndImageId(Long userId, Long imageId, int score);

    /**
     * 根据用户id获取用户前m行为(按分数排序)
     * @param user_id
     * @return
     */
    @Query(value = "select image_id from al_behavior where user_id = ?1 order by user_behavior_score desc limit ?2", nativeQuery = true)
    List<Long> findByUserId(Long user_id, int m);

    /**
     * 根据用户id获取用户前m行为(按behavior_id排序)
     * @param user_id
     * @return
     */
    @Query(value = "select image_id from al_behavior where user_id = ?1 order by behavior_id desc limit ?2", nativeQuery = true)
    List<Long> findByUserIdOrderTime(Long user_id, int m);

    /**
     * 根据用户id和图片id获取用户行为
     * @param userId
     * @param imageId
     * @return
     */
    @Query(value = "select behavior_id from al_behavior where user_id = ?1 and image_id = ?2", nativeQuery = true)
    Long findByUserIdAndImageId(Long userId, Long imageId);

    /**
     * 查询用户行为记录数
     * @param userId
     * @return
     */
    @Query(value = "select count(*) from al_behavior where user_id = ?1", nativeQuery = true)
    int findRecordCountByUserId(Long userId);
}
