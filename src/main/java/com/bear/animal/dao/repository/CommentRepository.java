package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 评论数据库表操作
 * @author bear
 * @date 2021年04月09日 10:35
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
