package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

/**
 * 评论请求结果数据
 * @author bear
 * @date 2021年04月10日 13:32
 */
@Data
public class CommentResult {

    private Long comment_id;

    private UserEntity user;

    private String comment_content;

    private String comment_time;

}
