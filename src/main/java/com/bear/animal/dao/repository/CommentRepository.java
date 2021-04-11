package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据库表操作
 * @author bear
 * @date 2021年04月09日 10:35
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /**
     * 根据图片id获取评论（按时间排序）
     * @param imageId
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select * from al_comment where image_id = ?1 order by comment_time desc limit ?2, ?3", nativeQuery = true)
    List<CommentEntity> findByImageId(Long imageId, int offset, int size);
}
