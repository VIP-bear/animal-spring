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
    @Query(value = "select * from al_image where user_id = ?1 order by image_upload_time desc limit ?2", nativeQuery = true)
    List<ImageEntity> findImageUrlByUserId(Long user_id, int size);

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

    /**
     * 根据用户id查找图片
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select * from al_image where user_id = ?1 order by image_upload_time desc limit ?2,?3", nativeQuery = true)
    List<ImageEntity> findByUserId(Long userId, Integer offset, Integer size);

    /**
     * 根据用户id集查找图片
     * @param userIdList
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select * from al_image where user_id in ?1 order by image_upload_time desc limit ?2,?3", nativeQuery = true)
    List<ImageEntity> findByUserIdList(List<Long> userIdList, int offset, int size);

    /**
     * 根据图片id查找图片标签
     * @param imageId
     * @return
     */
    @Query(value = "select image_tags from al_image where image_id = ?1", nativeQuery = true)
    String findTagByImageId(Long imageId);

    /**
     * 根据图片id查找图片地址
     * @param imageId
     * @return
     */
    @Query(value = "select image_url from al_image where image_id = ?1", nativeQuery = true)
    String findImageUrlByImageId(Long imageId);

    /**
     * 根据浏览数获取图片
     * @param size
     * @return
     */
    @Query(value = "select * from al_image order by image_view_count desc limit ?1,?2", nativeQuery = true)
    List<ImageEntity> findImageByViewCount(int offset, int size);

    /**
     * 根据图片id排序获取图片
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "select * from al_image order by image_id desc limit ?1,?2", nativeQuery = true)
    List<ImageEntity> findImageOrderByImageId(int offset, int size);
}
