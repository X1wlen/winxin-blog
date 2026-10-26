-- =====================================================================
-- WinXin Blog 建表 SQL 初稿（骨架阶段，供 review；字段增减随业务实现调整）
-- 环境：MySQL 8.x / utf8mb4 / InnoDB
-- 约定：
--   1. 主键统一 bigint unsigned，业务层用 MyBatis-Plus 雪花 ID（assign_id）
--   2. 每表含 created_at / updated_at；需要软删的表含 deleted（0 未删，1 已删）
--   3. 金额、计数等用 NOT NULL DEFAULT 0，避免 NULL 参与运算
--   4. 字符集 utf8mb4（支持 emoji），排序规则 utf8mb4_0900_ai_ci（MySQL 8 默认）
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `winxin_blog`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `winxin_blog`;

-- ---------------------------------------------------------------------
-- 一、RBAC 权限模型（用户 / 角色 / 权限 五表）
-- ---------------------------------------------------------------------

-- 1. 用户表
CREATE TABLE `user` (
    `id`          BIGINT UNSIGNED     NOT NULL COMMENT '主键（雪花 ID）',
    `username`    VARCHAR(32)         NOT NULL COMMENT '登录名，唯一',
    `password`    VARCHAR(100)        NOT NULL COMMENT '密码（BCrypt 等散列后的密文，禁止明文）',
    `nickname`    VARCHAR(32)         NOT NULL DEFAULT '' COMMENT '昵称（展示用）',
    `email`       VARCHAR(64)                  DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(255)                 DEFAULT NULL COMMENT '头像 URL',
    `bio`         VARCHAR(255)                 DEFAULT NULL COMMENT '个人简介',
    `status`      TINYINT             NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 禁用',
    `deleted`     TINYINT             NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE = InnoDB COMMENT = '用户表';

-- 2. 角色表
CREATE TABLE `role` (
    `id`          BIGINT UNSIGNED     NOT NULL COMMENT '主键（雪花 ID）',
    `code`        VARCHAR(32)         NOT NULL COMMENT '角色编码，如 admin / user，唯一',
    `name`        VARCHAR(32)         NOT NULL COMMENT '角色名称',
    `description` VARCHAR(128)                 DEFAULT NULL COMMENT '角色描述',
    `status`      TINYINT             NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 禁用',
    `deleted`     TINYINT             NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB COMMENT = '角色表';

-- 3. 权限表
CREATE TABLE `permission` (
    `id`          BIGINT UNSIGNED     NOT NULL COMMENT '主键（雪花 ID）',
    `code`        VARCHAR(64)         NOT NULL COMMENT '权限编码，如 article:publish、comment:delete，唯一',
    `name`        VARCHAR(32)         NOT NULL COMMENT '权限名称',
    `type`        TINYINT             NOT NULL DEFAULT 1 COMMENT '类型：1 接口权限，2 菜单/页面权限',
    `description` VARCHAR(128)                 DEFAULT NULL COMMENT '权限描述',
    `status`      TINYINT             NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 禁用',
    `deleted`     TINYINT             NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB COMMENT = '权限表';

-- 4. 用户-角色 关联表
CREATE TABLE `user_role` (
    `id`          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '主键（纯关联表可用自增）',
    `user_id`     BIGINT UNSIGNED     NOT NULL COMMENT '用户 ID',
    `role_id`     BIGINT UNSIGNED     NOT NULL COMMENT '角色 ID',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE = InnoDB COMMENT = '用户-角色关联表';

-- 5. 角色-权限 关联表
CREATE TABLE `role_permission` (
    `id`            BIGINT UNSIGNED   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       BIGINT UNSIGNED   NOT NULL COMMENT '角色 ID',
    `permission_id` BIGINT UNSIGNED   NOT NULL COMMENT '权限 ID',
    `created_at`    DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE = InnoDB COMMENT = '角色-权限关联表';

-- ---------------------------------------------------------------------
-- 二、内容模型（文章 / 分类 / 标签 / 评论）
-- ---------------------------------------------------------------------

-- 6. 分类表
CREATE TABLE `category` (
    `id`          BIGINT UNSIGNED     NOT NULL COMMENT '主键（雪花 ID）',
    `name`        VARCHAR(32)         NOT NULL COMMENT '分类名，唯一',
    `slug`        VARCHAR(64)         NOT NULL COMMENT 'URL 别名（SEO 友好路径），唯一',
    `description` VARCHAR(128)                 DEFAULT NULL COMMENT '分类描述',
    `sort`        INT                 NOT NULL DEFAULT 0 COMMENT '排序值（小在前）',
    `status`      TINYINT             NOT NULL DEFAULT 1 COMMENT '状态：1 显示，0 隐藏',
    `deleted`     TINYINT             NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE = InnoDB COMMENT = '文章分类表';

-- 7. 标签表
CREATE TABLE `tag` (
    `id`          BIGINT UNSIGNED     NOT NULL COMMENT '主键（雪花 ID）',
    `name`        VARCHAR(32)         NOT NULL COMMENT '标签名，唯一',
    `slug`        VARCHAR(64)         NOT NULL COMMENT 'URL 别名，唯一',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE = InnoDB COMMENT = '文章标签表';

-- 8. 文章表
CREATE TABLE `article` (
    `id`           BIGINT UNSIGNED    NOT NULL COMMENT '主键（雪花 ID）',
    `user_id`      BIGINT UNSIGNED    NOT NULL COMMENT '作者用户 ID',
    `category_id`  BIGINT UNSIGNED             DEFAULT NULL COMMENT '分类 ID（允许未分类）',
    `title`        VARCHAR(128)       NOT NULL COMMENT '标题',
    `summary`      VARCHAR(512)                DEFAULT NULL COMMENT '摘要（AI 模块可自动生成后回填）',
    `cover_image`  VARCHAR(255)                DEFAULT NULL COMMENT '封面图 URL',
    `content`      MEDIUMTEXT         NOT NULL COMMENT '正文（Markdown）',
    `content_type` TINYINT            NOT NULL DEFAULT 1 COMMENT '正文类型：1 Markdown，2 HTML',
    `status`       TINYINT            NOT NULL DEFAULT 0 COMMENT '状态：0 草稿，1 已发布，2 已隐藏',
    `view_count`   INT UNSIGNED       NOT NULL DEFAULT 0 COMMENT '浏览数（高频更新，后续可迁 Redis 计数）',
    `like_count`   INT UNSIGNED       NOT NULL DEFAULT 0 COMMENT '点赞数',
    `published_at` DATETIME                    DEFAULT NULL COMMENT '首次发布时间（列表按此排序）',
    `deleted`      TINYINT            NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`   DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status_published_at` (`status`, `published_at`)
) ENGINE = InnoDB COMMENT = '文章表';

-- 9. 文章-标签 关联表（多对多）
CREATE TABLE `article_tag` (
    `id`          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id`  BIGINT UNSIGNED     NOT NULL COMMENT '文章 ID',
    `tag_id`      BIGINT UNSIGNED     NOT NULL COMMENT '标签 ID',
    `created_at`  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB COMMENT = '文章-标签关联表';

-- 10. 评论表（自关联支持楼中楼回复）
CREATE TABLE `comment` (
    `id`               BIGINT UNSIGNED  NOT NULL COMMENT '主键（雪花 ID）',
    `article_id`       BIGINT UNSIGNED  NOT NULL COMMENT '所属文章 ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '评论人用户 ID',
    `parent_id`        BIGINT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '父评论 ID（0 表示根评论，非 0 为回复）',
    `reply_to_user_id` BIGINT UNSIGNED           DEFAULT NULL COMMENT '被回复人用户 ID（用于前端展示"回复@某人"）',
    `content`          VARCHAR(1000)    NOT NULL COMMENT '评论内容',
    `status`           TINYINT          NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 已隐藏',
    `like_count`       INT UNSIGNED     NOT NULL DEFAULT 0 COMMENT '点赞数',
    `deleted`          TINYINT          NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删，1 已删',
    `created_at`       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_parent` (`article_id`, `parent_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB COMMENT = '评论表';
