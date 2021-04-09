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
}
