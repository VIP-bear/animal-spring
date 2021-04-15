package com.bear.animal.controller;

import com.bear.animal.controller.req.UploadImageReq;
import com.bear.animal.service.IBehaviorService;
import com.bear.animal.service.IFavoritesService;
import com.bear.animal.service.IImageService;
import com.bear.animal.service.IRecommendService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IBehaviorService behaviorService;

    @Autowired
    private IFavoritesService favoritesService;

    @Autowired
    private IRecommendService recommendService;

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
     * 根据用户推荐图片
     * @param user_id
     * @param <T>
     * @return
     */
    @GetMapping("/image/recommend/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getRecommendImage(@PathVariable("user_id") String user_id) {
        Long userId = Long.parseLong(user_id);
        return (T) recommendService.getRecommendImage(userId);
    }

    /**
     * 根据用户id获取关注用户的图片
     * @param user_id
     * @param <T>
     * @return
     */
    @GetMapping("/image/attention/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getAttentionUserImage(@PathVariable("user_id") String user_id) {
        log.info("user_id: {}", user_id);
        Long userId = Long.parseLong(user_id);
        return (T) imageService.getAttentionUserImage(userId);
    }

    /**
     * 根据图片id获取图片详细信息
     * @param image_id 图片id
     * @param user_id 用户id
     * @param <T>
     * @return
     */
    @GetMapping("/image/{image_id}/{user_id}")
    @ResponseBody
    @ResponseResult
    public <T> T getImageMessage(@PathVariable("image_id") String image_id,
                                 @PathVariable("user_id") String user_id)  {
        log.info("imageId: {}, userId: {}", image_id, user_id);
        Long userId = Long.parseLong(user_id);
        Long imageId = Long.parseLong(image_id);
        behaviorService.updateBehaviorScore(userId, imageId, 1);
        imageService.updateImageViewCount(imageId);
        return (T) imageService.getImageMessage(imageId, userId);
    }

    /**
     * 收藏/取消收藏图片
     * @param image_id 图片id
     * @param user_id 用户id
     * @param flag 0表示取消收藏，1表示收藏
     * @param <T>
     * @return
     */
    @GetMapping("/image/favorites/{image_id}/{user_id}/{flag}")
    @ResponseBody
    @ResponseResult
    public <T> T favoritesImage(@PathVariable("image_id") String image_id,
                                @PathVariable("user_id") String user_id,
                                @PathVariable("flag") String flag) {
        log.info("imageId: {}, userId: {}, flag: {}", image_id, user_id, flag);
        Long userId = Long.parseLong(user_id);
        Long imageId = Long.parseLong(image_id);
        int flagNum = Integer.parseInt(flag);
        boolean state = false;
        if (flagNum == 1) {
            // 收藏
            state = true;
            behaviorService.updateBehaviorScore(userId, imageId, 3);
            imageService.updateImageFavoritesCount(imageId, 1);
        } else {
            // 取消收藏
            state = false;
            behaviorService.updateBehaviorScore(userId, imageId, -3);
            imageService.updateImageFavoritesCount(imageId, -1);
        }
        return (T) favoritesService.updateImageFavoritesState(imageId, userId, state);
    }

    /**
     * 获取用户收藏图片列表
     * @param user_id
     * @param offset
     * @param <T>
     * @return
     */
    @GetMapping("/image/favorites/{user_id}/{offset}")
    @ResponseBody
    @ResponseResult
    public <T> T getFavoritesImageList(@PathVariable("user_id") String user_id,
                                       @PathVariable("offset") String offset) {
        log.info("user_id: {}, offset: {}", user_id, offset);
        Long userId = Long.parseLong(user_id);
        Integer offsetNum = Integer.parseInt(offset);
        return (T) imageService.getFavoritesImageList(userId, offsetNum, 10);
    }

}
