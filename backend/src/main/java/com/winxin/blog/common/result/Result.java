package com.winxin.blog.common.result;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一响应结构：所有接口（含异常）都返回 {code, message, data}。
 *
 * <p>设计约定：
 * <ul>
 *   <li>code = 0 表示成功；非 0 为业务/系统错误码，分段规则见 {@link ResultCode}</li>
 *   <li>HTTP 状态码与 Result.code 分离：HTTP 表达传输层语义（4xx/5xx），
 *       Result.code 表达业务语义，两者由 {@link ResultCode#getHttpStatus()}} 关联</li>
 *   <li>前端 Axios 拦截器按同一约定解析：code !== 0 或 HTTP 非 2xx 一律走统一错误提示</li>
 * </ul>
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码：0 成功，非 0 失败（分段见 ResultCode） */
    private int code;

    /** 提示信息：成功为 "success"，失败为可直接展示给用户的中文描述 */
    private String message;

    /** 业务数据，失败时为 null */
    private T data;

    public static <T> Result<T> ok() {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> ok(T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return build(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return build(resultCode.getCode(), message, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return build(code, message, null);
    }

    /** Java 侧便捷判断；@JsonIgnore 避免 boolean getter 被序列化出契约之外的字段 */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }

    private static <T> Result<T> build(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }
}
