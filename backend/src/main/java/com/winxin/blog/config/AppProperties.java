package com.winxin.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用级自定义配置（前缀 app.*），对应 application.yml 中的 app 节点。
 *
 * <p>用不可变 record 承载配置（Boot 4 官方推荐写法）：字段即配置项，
 * 类型安全、无需手写 getter/setter；后续扩展（如 app.upload.*）在此追加嵌套 record。
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Cors cors) {

    /** 跨域配置：allowedOrigins 为逗号分隔的来源列表，可用环境变量 APP_CORS_ALLOWED_ORIGINS 覆盖 */
    public record Cors(String allowedOrigins) {
    }
}
