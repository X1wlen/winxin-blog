package com.winxin.blog.common.exception;

import org.springframework.http.HttpStatus;

import com.winxin.blog.common.result.ResultCode;

import lombok.Getter;

/**
 * 业务异常：Service 层校验失败、资源不存在等场景主动抛出，
 * 由 {@link GlobalExceptionHandler} 统一转为 Result 结构响应。
 *
 * <p>使用约定：优先携带 {@link ResultCode} 中已定义的错误码，
 * 模块专属错误码在对应分段（11000+ 等）内自行扩展。
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;
    private final transient HttpStatus httpStatus;

    public BusinessException(ResultCode resultCode) {
        this(resultCode, resultCode.getMessage());
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.httpStatus = resultCode.getHttpStatus();
    }

    /** 未在 ResultCode 中登记的模块级错误码，默认映射为 HTTP 400 */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
