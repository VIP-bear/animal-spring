package com.bear.animal.service;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.CommentReq;

/**
 * 评论服务层接口
 * @author bear
 * @date 2021年04月10日 13:24
 */
public interface ICommentService {

    /**
     * 发布评论
     * @param commentData
     * @return
     */
    Result publicComment(CommentReq commentData);

    /**
     * 根据图片id分页获取图片评论
     * @param imageId
     * @param offset
     * @param size
     * @return
     */
    Result getComment(Long imageId, int offset, int size);
}
