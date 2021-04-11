package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 数据库图片表操作
 * @author bear
 * @date 2021年04月08日 10:20
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    /**
     * 根据时间和浏览量获取今日榜单
     * @param today
     * @return
     */
    @Query(value = "select * from al_image where unix_timestamp(image_upload_time) >= unix_timestamp(?1) " +
            "order by image_view_count desc limit 18", nativeQuery = true)
    List<ImageEntity> getRankingByUploadTime(Timestamp today);

    /**
     * 根据图片id获取图片
     * @param imageId
     * @return
     */
    @Query(value = "select * from al_image where image_id = ?1", nativeQuery = true)
    ImageEntity findImageByImageId(Long imageId);

    /**
     * 根据用户id获取图片链接
     * @param user_id
     * @return
     */
    @Query(value = "select image_url from al_image where user_id = ?1 order by image_upload_time desc limit 6", nativeQuery = true)
    List<String> findImageUrlByUserId(Long user_id);

    /**
     * 根据图片id更新图片浏览数
     * @param imageId
     */
    @Modifying
    @Query(value = "update al_image set image_view_count = image_view_count + 1 where image_id = ?1", nativeQuery = true)
    void updateImageViewCountByImageId(Long imageId);

    /**
     * 根据图片id更新图片收藏数
     * @param imageId
     * @param count
     */
    @Modifying
    @Query(value = "update al_image set image_favorites_count = image_favorites_count + ?2 where image_id = ?1", nativeQuery = true)
    void updateImageFavoritesCountByImageId(Long imageId, int count);
}
