package com.bear.animal.except;

import com.bear.animal.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     * @param e
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResult handlerThrowable(Throwable e, HttpServletRequest request) {
        ErrorResult errorResult = ErrorResult.failure(ResultCode.SYSTEM_ERROR, e);
        return errorResult;
    }

    /**
     * 处理自定义异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ErrorResult handlerBusinessException(BusinessException e, HttpServletRequest request) {
        ErrorResult errorResult = ErrorResult.failure(e.getResultCode(), e);
        return errorResult;
    }

}
