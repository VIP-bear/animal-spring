package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
