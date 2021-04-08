package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据库图片表操作
 * @author bear
 * @date 2021年04月08日 10:20
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

}
