package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.AttentionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 关注用户数据库表操作
 * @author bear
 * @date 2021年04月09日 10:40
 */
@Repository
public interface AttentionRepository extends JpaRepository<AttentionEntity, Long> {

    /**
     * 根据用户id和关注用户id查找记录数量
     * @param userId
     * @param attentionUserId
     * @return
     */
    @Query(value = "select count(*) from al_attention where user_id = ?1 and attention_user_id = ?2", nativeQuery = true)
    int findByAttentionUserId(Long userId, Long attentionUserId);

    /**
     * 根据用户id和被关注用户id删除记录
     * @param user_id
     * @param attention_user_id
     * @return
     */
    @Modifying
    @Query(value = "delete from al_attention where user_id = ?1 and attention_user_id = ?2", nativeQuery = true)
    int deleteByUserIdAndAttentionUserId(Long user_id, Long attention_user_id);
}
