package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.AttentionEntity;
import com.bear.animal.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据用户id查找关注者
     * @param userId
     * @return
     */
    @Query(value = "select attention_user_id from al_attention where user_id = ?1", nativeQuery = true)
    List<Long> findByUserId(Long userId);

    /**
     * 根据用户id获取关注用户id列表
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select attention_user_id from al_attention where user_id = ?1 limit ?2,?3", nativeQuery = true)
    List<Long> findByUserIdOffset(Long userId, Integer offset, Integer size);

    /**
     * 根据关注用户id获取被关注者信息
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select * from al_attention where attention_user_id = ?1 limit ?2,?3", nativeQuery = true)
    List<AttentionEntity> findByAttentionUserIdOffset(Long userId, Integer offset, Integer size);
}
