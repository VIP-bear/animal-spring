package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 标签数据库表操作
 * @author bear
 * @date 2021年04月09日 9:48
 */
@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {


}
