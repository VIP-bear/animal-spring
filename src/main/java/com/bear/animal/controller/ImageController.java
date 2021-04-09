package com.bear.animal.controller;

import com.bear.animal.controller.req.UploadImageReq;
import com.bear.animal.service.IImageService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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

    /**
     * 获取每日排行榜图片信息
     * @param <T>
     * @return
     */
    @GetMapping("/image/ranking")
    @ResponseBody
    @ResponseResult
    public <T> T getRanking() {
        return (T) imageService.getRanking();
    }

    /**
     * 根据图片id获取图片详细信息
     * @param imageId 图片id
     * @param <T>
     * @return
     */
    @GetMapping("/image/{image_id}/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getImageMessage(@PathVariable("image_id") String imageId,
                                 @PathVariable("user_id") String userId)  {
        log.info("imageId: {}, userId: {}", imageId, userId);
        return (T) imageService.getImageMessage(Long.parseLong(imageId), Long.parseLong(userId));
    }

}
