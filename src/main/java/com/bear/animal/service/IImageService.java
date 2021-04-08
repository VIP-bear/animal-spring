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
}
