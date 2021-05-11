package com.bear.animal.controller;

import com.bear.animal.controller.req.CommentReq;
import com.bear.animal.service.IBehaviorService;
import com.bear.animal.service.ICommentService;
import com.bear.animal.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 评论操作控制层
 * @author bear
 * @date 2021年04月10日 13:17
 */
@Slf4j
@Controller
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IBehaviorService behaviorService;

    /**
     * 评论图片
     * @param commentData
     * @param <T>
     * @return
     */
    @PostMapping("/comment/publish")
    @ResponseBody
    @ResponseResult
    public <T> T publishComment(@RequestBody CommentReq commentData) {
        log.info("commentData: {}", commentData);
        behaviorService.updateBehaviorScore(commentData.getUser_id(), commentData.getImage_id(), 2);
        return (T) commentService.publicComment(commentData);
    }

    /**
     * 根据图片id分页获取图片评论
     * @param image_id 图片id
     * @param offset 偏移量
     * @param <T>
     * @return
     */
    @GetMapping("/comment/{image_id}/{offset}")
    @ResponseBody
    @ResponseResult
    public <T> T getComment(@PathVariable("image_id") String image_id,
                            @PathVariable("offset") String offset) {
        log.info("image_id: {}, offset: {}", image_id, offset);
        Long imageId = Long.parseLong(image_id);
        int offsetNum = Integer.parseInt(offset);
        return (T) commentService.getComment(imageId, offsetNum, 10);
    }

}
