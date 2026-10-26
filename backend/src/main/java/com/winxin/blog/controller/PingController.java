package com.winxin.blog.controller;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winxin.blog.common.result.Result;

/**
 * 健康检查：GET /ping。
 *
 * <p>作用是端到端验证基础链路：CORS → Axios 拦截器 → 统一 Result 结构。
 * 健康检查类接口允许直接在 Controller 返回，不强制经过 Service 层。
 * 注意：不引入 spring-boot-starter-webmvc 之外的 actuator，避免骨架阶段暴露多余端点。
 */
@RestController
@Tag(name = "健康检查", description = "服务连通性验证")
public class PingController {

    @GetMapping("/ping")
    public Result<Map<String, Object>> ping() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("service", "winxin-blog");
        data.put("message", "pong");
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("timestamp", OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return Result.ok(data);
    }
}
