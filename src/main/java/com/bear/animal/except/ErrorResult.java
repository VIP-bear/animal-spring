package com.bear.animal.except;

import com.bear.animal.enums.ResultCode;
import lombok.Data;

/**
 * 异常信息
 */
@Data
public class ErrorResult {

    // 异常状态码和信息
    private ResultCode resultCode;
    // 异常堆栈信息
    private String errors;

    public static ErrorResult failure(ResultCode resultCode, Throwable e) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setResultCode(resultCode);
        errorResult.setErrors(e.getMessage());
        return errorResult;
    }

}
