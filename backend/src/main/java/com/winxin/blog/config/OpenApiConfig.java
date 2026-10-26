package com.winxin.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI（springdoc 3.x）文档元信息配置。
 *
 * <p>访问入口（dev 环境）：
 * <ul>
 *   <li>Swagger UI：/swagger-ui.html</li>
 *   <li>OpenAPI JSON：/v3/api-docs</li>
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blogOpenApi() {
        return new OpenAPI().info(new Info()
                .title("WinXin Blog API")
                .description("个人博客系统接口文档（骨架阶段：仅包含健康检查，业务模块见 docs/03-接口文档.md）")
                .version("v0.1.0")
                .contact(new Contact().name("winxin")));
        // TODO(认证模块): 接入 JWT 后，在此注册 SecurityScheme（bearerAuth），
        // 使文档页支持"Authorize"携带 Token 调试。
    }
}
