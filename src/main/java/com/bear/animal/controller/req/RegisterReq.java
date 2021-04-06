package com.bear.animal.controller.req;

import lombok.Data;

/**
 * 注册请求数据
 * @author bear
 * @date 2021年04月06日 22:59
 */
@Data
public class RegisterReq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

}
