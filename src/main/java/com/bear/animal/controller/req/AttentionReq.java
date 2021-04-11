package com.bear.animal.controller.req;

import lombok.Data;

/**
 * 关注/取消用户请求数据
 * @author bear
 * @date 2021年04月11日 20:50
 */
@Data
public class AttentionReq {

    // 用户id
    private Long user_id;

    // 被关注用户id
    private Long attention_user_id;

    // 0: 取消关注, 1: 关注
    private int state;

}
