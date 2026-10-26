/**
 * 请求入参对象（预留占位，不实现）。
 *
 * <p>约定：命名 XxxQuery（分页查询）/ XxxDTO（写入），后缀体现用途；
 * 校验注解（jakarta.validation：@NotBlank、@Size 等）写在 DTO 上，
 * Controller 参数加 @Valid 即可触发全局异常处理器的统一 400 响应。
 */
package com.winxin.blog.dto;
