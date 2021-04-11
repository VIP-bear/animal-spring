package com.bear.animal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码
 */

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(400, "操作失败"),
    /* 参数错误: 1001-1999 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    /* 用户错误: 2001-2999 */
    USER_NOT_LOGGED_IN(2001, "用户未登录，访问的路径需要验证，请登录"),
    USER_LOGIN_ERROR(2002, "账号或者密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXIST(2005, "用户已存在"),
    USER_EMAIL_EXIST(2006, "邮箱已被注册"),
    USER_UPDATE_FAIL(2007, "信息更新失败"),
    /* 图片错误: 3001-3999 */
    IMAGE_FAVORITES_FAIL(3001, "图片收藏失败"),
    /* 系统错误: 5001-5999 */
    SYSTEM_ERROR(5001, "系统异常")
    ;

    // 状态码
    private Integer code;
    // 信息描述
    private String message;


}
