/**
 * 持久化实体（预留占位，不实现）。
 *
 * <p>约定：与数据库表一一对应（表名见 docs/sql/init.sql），
 * 类名驼峰对应下划线表名，@TableName/@TableId 显式声明；
 * 字段类型用包装类型，日期用 java.time（LocalDateTime）。
 * 注意：entity 不出 Service 层，对外一律转 DTO/VO，避免数据库结构泄漏到接口。
 */
package com.winxin.blog.entity;
