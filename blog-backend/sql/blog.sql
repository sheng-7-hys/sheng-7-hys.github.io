-- ============================================================
-- Sheng's Blog 数据库初始化脚本
-- 数据库：MySQL 8.0
-- 编码：utf8mb4（支持 emoji）
-- 时区：Asia/Shanghai
-- ============================================================

-- 创建数据库（若不存在则新建）
CREATE DATABASE IF NOT EXISTS blog_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- ============================================================
-- 1. 用户表（user）
-- ============================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    `username`    VARCHAR(50)  NOT NULL                COMMENT '用户名（唯一、非空）',
    `password`    VARCHAR(255) NOT NULL                COMMENT '密码（BCrypt 加密存储）',
    `email`       VARCHAR(100)     NULL DEFAULT NULL   COMMENT '邮箱（可选）',
    `avatar`      VARCHAR(500)     NULL DEFAULT NULL   COMMENT '头像路径/URL',
    `role`        TINYINT      NOT NULL DEFAULT 0      COMMENT '角色：0-普通用户，1-管理员',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 2. 分类表（category）
-- ============================================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    `name`        VARCHAR(100) NOT NULL                COMMENT '分类名称（唯一）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章分类表';

-- ============================================================
-- 3. 文章表（article）
-- ============================================================
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    `title`       VARCHAR(200)  NOT NULL                COMMENT '文章标题（非空）',
    `content`     LONGTEXT      NOT NULL                COMMENT '文章内容（非空）',
    `category_id` BIGINT            NULL DEFAULT NULL   COMMENT '分类ID（外键关联 category 表，可为空）',
    `user_id`     BIGINT        NOT NULL                COMMENT '作者ID（外键关联 user 表）',
    `view_count`  INT           NOT NULL DEFAULT 0      COMMENT '阅读量',
    `is_publish`  TINYINT       NOT NULL DEFAULT 1      COMMENT '发布状态：0-草稿，1-已发布',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id`     (`user_id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_is_publish`  (`is_publish`),
    INDEX `idx_create_time` (`create_time`),
    CONSTRAINT `fk_article_user`     FOREIGN KEY (`user_id`)     REFERENCES `user`     (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ============================================================
-- 4. 评论表（comment）
-- ============================================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    `article_id`  BIGINT    NOT NULL                COMMENT '文章ID（外键关联 article 表）',
    `user_id`     BIGINT    NOT NULL                COMMENT '评论者ID（外键关联 user 表）',
    `content`     TEXT      NOT NULL                COMMENT '评论内容',
    `parent_id`   BIGINT    NOT NULL DEFAULT 0      COMMENT '父评论ID（0 表示顶层评论）',
    `create_time` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_article_id`  (`article_id`),
    INDEX `idx_user_id`     (`user_id`),
    INDEX `idx_parent_id`   (`parent_id`),
    INDEX `idx_create_time` (`create_time`),
    CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_comment_user`    FOREIGN KEY (`user_id`)    REFERENCES `user`    (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================================
-- 5. 初始数据（管理员账号 + 示例分类 + 示例文章）
-- ============================================================

-- 管理员账号：admin / 123456
-- 密码使用 BCrypt 加密（对应明文：123456）
INSERT INTO `user` (`username`, `password`, `email`, `avatar`, `role`) VALUES
(
    'admin',
    '$2b$10$iBFlgxd3aC/fAN4Kbh7zPeGEDkuQUpFtpTjWNSvmIAvEe5vC5lNZG',
    'admin@sheng-blog.com',
    'https://github.com/sheng-7-hys.png',
    1  -- 管理员
),
(
    'sheng',
    '$2b$10$iBFlgxd3aC/fAN4Kbh7zPeGEDkuQUpFtpTjWNSvmIAvEe5vC5lNZG',
    'han.ys@outlook.com',
    'https://github.com/sheng-7-hys.png',
    1  -- 管理员
);

-- 注意：上述 BCrypt 哈希值是示例值。实际使用时请通过以下代码生成：
-- System.out.println(new BCryptPasswordEncoder().encode("123456"));
-- 或在 MySQL 中验证：
-- SELECT * FROM user WHERE username = 'admin';

-- 示例分类
INSERT INTO `category` (`name`) VALUES
    ('技术笔记'),
    ('生活随笔'),
    ('学习总结'),
    ('CPL');

-- 示例文章（关联用户 ID=1 的管理员）
INSERT INTO `article` (`title`, `content`, `category_id`, `user_id`, `view_count`, `is_publish`) VALUES
(
    '欢迎使用 Sheng Blog 系统',
    '欢迎使用基于 Spring Boot + MyBatis-Plus 构建的 Sheng Blog 系统！\n\n本系统保留了原有的 Jekyll 博客 UI 风格，在此基础上新增了完整的博客管理功能：\n\n- 用户注册 / 登录 / 退出\n- 文章发布 / 编辑 / 删除\n- 分类管理\n- 评论与回复\n\n默认管理员账号：admin\n默认密码：123456\n\n希望你用得愉快 ✨',
    1,
    1,
    0,
    1
),
(
    'C语言和数据结构日常随笔',
    '- `free(p)` 只释放该空间，标记该空间为可复用，空间中数据暂时不会被清除，指针值未变，需要使用 `p=NULL`，防止野指针。\n- 顺序表函数 `clearlist(Sqlist &L)` 仅使 length 变为 0，所存储值仍也存在，待覆盖。\n- 头插法可用于建立逆向链表\n- 就地逆转单链表方法',
    4,
    1,
    0,
    1
);

-- ============================================================
-- 确认创建成功
-- ============================================================
SELECT 'blog_db 初始化完成！' AS message;
SELECT TABLE_NAME, TABLE_COMMENT
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'blog_db';
