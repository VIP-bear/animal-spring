package com.bear.animal.controller;

import com.bear.animal.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 对请求响应的结果进行封装
 */
@Data
public class Result implements Serializable {

    // 状态码
    private Integer code;
    // 信息描述
    private String message;
    // 数据
    private Object data;

    // 返回成功
    public static Result success() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }

    // 返回成功（带数据）
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    // 返回失败
    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    // 返回失败（带数据）
    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        result.setData(data);
        return result;
    }

}
