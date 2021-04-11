package com.bear.animal.controller.req;

import lombok.Data;

/**
 * 评论请求数据
 * @author bear
 * @date 2021年04月10日 13:20
 */
@Data
public class CommentReq {

    // 图片id
    private Long image_id;

    // 用户id
    private Long user_id;

    // 评论内容
    private String comment_content;

}
