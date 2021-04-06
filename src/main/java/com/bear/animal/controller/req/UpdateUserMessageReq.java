package com.bear.animal.controller.req;

import lombok.Data;

/**
 * 更新用户信息
 * @author bear
 * @date 2021年04月06日 22:59
 */
@Data
public class UpdateUserMessageReq {

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户描述
     */
    private String user_introduction;

}
