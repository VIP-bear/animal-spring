package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
