package com.winxin.blog.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 基础配置（仅 ORM 基础设施，不含业务逻辑）。
 *
 * <p>分页插件属于 ORM 基础能力：不注册则 IPage 查询不生效，
 * 文章模块的"分页列表"接口（见 docs/03-接口文档.md）依赖它。
 * 其余策略（事务边界、缓存、逻辑删除、字段填充等）按边界约定由你自行实现。
 */
@Configuration
@MapperScan("com.winxin.blog.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
