package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据库用户表操作
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return
     */
    @Query(value = "select * from al_user where email = ?1", nativeQuery = true)
    UserEntity findByEmail(String email);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return
     */
    @Query(value = "select * from al_user where username = ?1", nativeQuery = true)
    UserEntity findByUsername(String username);

    /**
     * 根据用户id更新用户信息
     * @param userId 用户id
     * @param password 密码
     * @param introduction 用户描述
     * @return
     */
    @Modifying
    @Query(value = "update al_user set password = ?2, user_introduction = ?3 where user_id = ?1", nativeQuery = true)
    Integer updateByUserId(Long userId, String password, String introduction);

    /**
     * 随机获取n个用户
     * @param n
     * @return
     */
    @Query(value = "select * from al_user where user_id >= ((select max(user_id) from al_user) - " +
            "(select min(user_id) from al_user)) * rand() + (select min(user_id) from al_user) limit ?1", nativeQuery = true)
    List<UserEntity> getUserByRandom(int n);

    /**
     * 根据用户id更新用户粉丝数
     * @param user_id
     * @param followCount
     * @return
     */
    @Modifying
    @Query(value = "update al_user set follow_count = follow_count + ?2 where user_id = ?1", nativeQuery = true)
    int updateFollowCountByUserId(Long user_id, int followCount);

    /**
     * 根据用户id更新用户关注数
     * @param user_id
     * @param attentionCount
     * @return
     */
    @Modifying
    @Query(value = "update al_user set attention_count = attention_count + ?2 where user_id = ?1", nativeQuery = true)
    int updateAttentionCountByUserId(Long user_id, int attentionCount);
}
