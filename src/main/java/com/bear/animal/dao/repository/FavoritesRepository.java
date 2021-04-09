package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 收藏数据库表操作
 * @author bear
 * @date 2021年04月09日 10:41
 */
@Repository
public interface FavoritesRepository extends JpaRepository<FavoritesEntity, Long> {

    /**
     * 根据用户id和图片查找记录数量
     * @param userId
     * @param imageId
     * @return
     */
    @Query(value = "select count(*) from al_favorites where user_id = ?1 and image_id = ?2", nativeQuery = true)
    int findByUserIdAndImageId(Long userId, Long imageId);
}
