package com.bear.animal.controller.req;

import lombok.Data;

/**
 * 登录请求数据
 * @author bear
 * @date 2021年04月06日 22:59
 */
@Data
public class LoginReq {

    /**
     * 用户名（用户名/邮箱）
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
