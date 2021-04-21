package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签数据库表操作
 * @author bear
 * @date 2021年04月09日 9:48
 */
@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    /**
     * 根据标签查找图片
     * @param imageTag
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select distinct image_id from al_tag where tag_name in ?1 limit ?2,?3", nativeQuery = true)
    List<Long> findByTagList(List<String> imageTag, int offset, int size);
}
