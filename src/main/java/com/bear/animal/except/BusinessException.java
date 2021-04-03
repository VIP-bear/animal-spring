package com.bear.animal.except;

import com.bear.animal.enums.ResultCode;
import lombok.Data;

/**
 * 自定义异常类，运行时异常
 */
@Data
public class BusinessException extends RuntimeException {

    private ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

}
