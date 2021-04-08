package com.bear.animal.controller;

import com.bear.animal.controller.req.UploadImageReq;
import com.bear.animal.service.IImageService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 图片操作控制层
 * @author bear
 * @date 2021年04月07日 18:47
 */
@Controller
@Slf4j
public class ImageController {

    @Autowired
    private IImageService imageService;

    /**
     * 上传图片请求
     * @param uploadImageMessage
     * @param <T>
     * @return
     */
    @PostMapping("/image/upload")
    @ResponseBody
    @ResponseResult
    public <T> T uploadImage(@RequestBody UploadImageReq uploadImageMessage) {
        log.info("uploadImageMessage: {}", uploadImageMessage);
        return (T) imageService.uploadImage(uploadImageMessage);
    }

}
