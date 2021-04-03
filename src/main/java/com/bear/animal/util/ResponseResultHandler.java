package com.bear.animal.util;

import com.bear.animal.controller.Result;
import com.bear.animal.except.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 对响应进行包装
 */
@Slf4j
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    // 标记名称
    public static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    /**
     * 请求是否包含了包装注解标记，没有就直接返回
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = sra.getRequest();
        // 判断请求，是否有包装标记
        com.bear.animal.util.ResponseResult responseResultAnn = (com.bear.animal.util.ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResultAnn == null ? false : true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("进入返回体，重写格式，处理中...");
        if (body instanceof ErrorResult) {
            log.info("发生了问题...");
            ErrorResult errorResult = (ErrorResult) body;
            return Result.failure(errorResult.getResultCode(), errorResult.getErrors());
        }
        log.info("操作成功...");
        return Result.success(body);
    }
}
