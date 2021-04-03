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
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Query(value = "select * from al_user where username = ?1 and password = ?2", nativeQuery = true)
    UserEntity findByUsernameAndPassword(String username, String password);

}
