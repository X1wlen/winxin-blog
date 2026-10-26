package com.winxin.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 应用启动类。
 *
 * <p>@ConfigurationPropertiesScan 开启对 {@code @ConfigurationProperties} 类型安全配置的
 * 扫描（Boot 4 推荐用 record 承载配置，见 config.AppProperties）。
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
