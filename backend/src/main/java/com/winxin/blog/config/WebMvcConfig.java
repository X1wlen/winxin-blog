package com.winxin.blog.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 全局配置：跨域（CORS）。
 *
 * <p>设计说明：
 * <ul>
 *   <li>开发期前端( http://localhost:5173 )直连后端( http://localhost:8080 )属跨域请求，
 *       在此统一放行，前端代码里不需要逐个接口处理跨域</li>
 *   <li>来源列表外置到配置（app.cors.allowed-origins），生产环境用环境变量
 *       APP_CORS_ALLOWED_ORIGINS 覆盖，禁止直接写死</li>
 *   <li>allowCredentials(true) 要求来源必须是精确列表（不能是 *），因此采用 allowedOrigins</li>
 * </ul>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

    public WebMvcConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = Arrays.stream(appProperties.cors().allowedOrigins().split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
