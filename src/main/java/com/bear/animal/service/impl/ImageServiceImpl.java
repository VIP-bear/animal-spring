package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.UploadImageReq;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.ImageRepository;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.except.BusinessException;
import com.bear.animal.service.IImageService;
import com.bear.animal.util.AliyunProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

/**
 * 图片服务实现
 * @author bear
 * @date 2021年04月08日 9:45
 */
@Service
public class ImageServiceImpl implements IImageService {

    @Autowired
    private AliyunProvider aliyunProvider;

    @Autowired
    private ImageRepository imageRepository;

    /**
     * 上传图片
     *
     * @param uploadImageMessage
     * @return
     */
    @Transactional
    @Override
    public Result uploadImage(UploadImageReq uploadImageMessage) {
        // base64转MultipartFile
        MultipartFile file = aliyunProvider.base64ToMultipart(uploadImageMessage.getImage());
        if (file == null) {
            // 图片转码失败
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 上传图片到阿里云oss
        String image_url = aliyunProvider.upload(file, "");
        if (image_url == null) {
            // 图片上传失败
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        ImageEntity imageEntity = new ImageEntity();
        BeanUtils.copyProperties(uploadImageMessage, imageEntity);
        imageEntity.setImage_url(image_url);
        imageEntity.setImage_upload_time(new Timestamp(System.currentTimeMillis()));
        imageEntity.setImage_favorites_count(0);
        imageEntity.setImage_like_count(0);
        imageEntity.setImage_view_count(0);
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(uploadImageMessage.getUser_id());
        imageEntity.setUser(userEntity);
        ImageEntity res = imageRepository.save(imageEntity);
        if (null == res) {
            // 保存失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 保存成功
        return Result.success();
    }
}
