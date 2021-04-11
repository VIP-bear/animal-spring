package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.CommentReq;
import com.bear.animal.controller.res.CommentResult;
import com.bear.animal.dao.entity.CommentEntity;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.CommentRepository;
import com.bear.animal.dao.repository.UserRepository;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.except.BusinessException;
import com.bear.animal.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论服务层实现
 * @author bear
 * @date 2021年04月10日 13:26
 */
@Slf4j
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发布评论
     *
     * @param commentData
     * @return
     */
    @Transactional
    @Override
    public Result publicComment(CommentReq commentData) {
        CommentEntity commentEntity = new CommentEntity();
        ImageEntity imageEntity = new ImageEntity();
        UserEntity userEntity = new UserEntity();
        imageEntity.setImage_id(commentData.getImage_id());
        userEntity.setUser_id(commentData.getUser_id());
        commentEntity.setImage(imageEntity);
        commentEntity.setUser(userEntity);
        commentEntity.setComment_content(commentData.getComment_content());
        commentEntity.setComment_time(new Timestamp(System.currentTimeMillis()));
        // 保存评论
        CommentEntity saveRes = commentRepository.save(commentEntity);
        if (null == saveRes) {
            // 评论失败
            return Result.failure(ResultCode.FAIL);
        }
        CommentResult commentResult = new CommentResult();
        BeanUtils.copyProperties(saveRes, commentResult);
        commentResult.setUser(userRepository.findById(commentData.getUser_id()).get());
        return Result.success(commentResult);
    }

    /**
     * 根据图片id分页获取图片评论
     *
     * @param imageId
     * @param offset
     * @param size
     * @return
     */
    @Transactional
    @Override
    public Result getComment(Long imageId, int offset, int size) {
        List<CommentEntity> commentList = commentRepository.findByImageId(imageId, offset, size);
        if (null == commentList) {
            // 获取评论失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        List<CommentResult> res = new ArrayList<>();
        for (CommentEntity commentEntity : commentList) {
            CommentResult commentResult = new CommentResult();
            BeanUtils.copyProperties(commentEntity, commentResult);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String commentTime = format.format(commentEntity.getComment_time());
            commentResult.setComment_time(commentTime);
            res.add(commentResult);
        }
        log.info("commentRes: {}", res);
        return Result.success(res);
    }
}
