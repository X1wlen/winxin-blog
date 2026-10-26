package com.winxin.blog.common.exception;

import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.winxin.blog.common.result.Result;
import com.winxin.blog.common.result.ResultCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器：所有 Controller 抛出的异常在此统一收口，
 * 保证任何异常路径都返回统一 Result 结构 + 与之匹配的 HTTP 状态码。
 *
 * <p>日志策略：业务类异常（4xx）打 warn，系统类异常（5xx）打 error 并带堆栈，
 * 避免可预期的业务失败刷出无意义的 ERROR 堆栈。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常：以异常自身携带的 code/httpStatus 为准 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(Result.fail(e.getCode(), e.getMessage()));
    }

    /** 请求体/表单参数校验失败（@Valid；MethodArgumentNotValidException 是 BindException 的子类，一并覆盖） */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException e) {
        String detail = e.getFieldErrors().stream()
                .map(error -> error.getField() + " " + Objects.requireNonNullElse(error.getDefaultMessage(), "不合法"))
                .collect(Collectors.joining("; "));
        return build(ResultCode.VALIDATION_FAILED, detail);
    }

    /** 方法级参数校验失败（@RequestParam / @PathVariable 上的约束注解） */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Result<Void>> handleHandlerMethodValidation(HandlerMethodValidationException e) {
        return build(ResultCode.VALIDATION_FAILED, e.getAllErrors().stream()
                .map(Object::toString)
                .collect(Collectors.joining("; ")));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolation(ConstraintViolationException e) {
        return build(ResultCode.VALIDATION_FAILED, e.getMessage());
    }

    /** 请求体 JSON 解析失败 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return build(ResultCode.BODY_NOT_READABLE);
    }

    /** 路径/查询参数类型转换失败 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        return build(ResultCode.TYPE_MISMATCH);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少必填参数: {}", e.getParameterName());
        return build(ResultCode.MISSING_PARAM);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return build(ResultCode.METHOD_NOT_ALLOWED);
    }

    /** 未匹配到任何接口/静态资源（Boot 3.2+ 默认抛出该异常而非 404 静态页） */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Void>> handleNoResourceFound(NoResourceFoundException e) {
        return build(ResultCode.NOT_FOUND);
    }

    /** 兜底：未预期异常统一按系统错误处理，对外隐藏堆栈细节 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("未捕获异常", e);
        return build(ResultCode.SYSTEM_ERROR);
    }

    private ResponseEntity<Result<Void>> build(ResultCode resultCode) {
        return ResponseEntity.status(resultCode.getHttpStatus()).body(Result.fail(resultCode));
    }

    private ResponseEntity<Result<Void>> build(ResultCode resultCode, String message) {
        return ResponseEntity.status(resultCode.getHttpStatus()).body(Result.fail(resultCode, message));
    }
}
