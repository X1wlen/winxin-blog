package com.winxin.blog.common.result;

import org.springframework.http.HttpStatus;

/**
 * 统一错误码枚举。
 *
 * <p>code 分段规则（新增模块错误码时先在对应分段内取值）：
 * <pre>
 *   0          成功
 *   10000+     通用错误（参数、系统、资源、方法等）
 *   11000+     认证模块（预留）
 *   12000+     文章模块（预留）
 *   13000+     用户模块（预留）
 *   14000+     评论模块（预留）
 *   15000+     AI 模块（预留）
 * </pre>
 */
public enum ResultCode {

    SUCCESS(0, "success", HttpStatus.OK),

    // ---------- 10000+ 通用 ----------
    SYSTEM_ERROR(10000, "系统内部错误，请稍后再试", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED(10001, "参数校验失败", HttpStatus.BAD_REQUEST),
    PARAM_ERROR(10002, "请求参数错误", HttpStatus.BAD_REQUEST),
    BODY_NOT_READABLE(10003, "请求体解析失败", HttpStatus.BAD_REQUEST),
    TYPE_MISMATCH(10004, "参数类型不匹配", HttpStatus.BAD_REQUEST),
    MISSING_PARAM(10005, "缺少必填参数", HttpStatus.BAD_REQUEST),
    NOT_FOUND(10200, "请求的资源不存在", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(10201, "不支持的请求方法", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT(10300, "资源状态冲突", HttpStatus.CONFLICT),
    BUSINESS_ERROR(10900, "业务处理失败", HttpStatus.BAD_REQUEST),

    // ---------- 11000+ 认证模块（预留，由你实现 Security/JWT 时使用） ----------
    UNAUTHORIZED(11000, "未登录或登录已过期", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(11001, "无权访问该资源", HttpStatus.FORBIDDEN);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ResultCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /** 与 code 关联的 HTTP 状态码，全局异常处理器据此设置响应状态 */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
