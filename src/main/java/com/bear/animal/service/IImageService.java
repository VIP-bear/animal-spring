package com.bear.animal.service;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.UploadImageReq;

/**
 * 图片服务层接口
 * @author bear
 * @date 2021年04月08日 9:43
 */
public interface IImageService {

    /**
     * 上传图片
     * @param uploadImageMessage
     * @return
     */
    Result uploadImage(UploadImageReq uploadImageMessage);

    /**
     * 获取每日排行榜图片
     * @return
     */
    Result getRanking();

    /**
     * 根据图片id获取图片详细信息
     * @return
     */
    Result getImageMessage(Long imageId, Long userId);

    /**
     * 根据图片id更新图片浏览数
     * @param imageId
     */
    void updateImageViewCount(Long imageId);

    /**
     * 根据图片id更新图片收藏数
     * @param imageId
     * @param count
     */
    void updateImageFavoritesCount(Long imageId, int count);

    /**
     * 根据用户id获取关注用户的图片
     * @param userId
     * @return
     */
    Result getAttentionUserImage(Long userId, int offset, int size);

    /**
     * 获取用户收藏图片列表
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    Result getFavoritesImageList(Long userId, Integer offset, int size);

    /**
     * 获取新发布的图片
     * @param offset
     * @param size
     * @return
     */
    Result getNewImageList(int offset, int size);
}
