package com.sheng.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 */
@Data
@TableName("article")
public class Article {

    /** 主键（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章标题（非空） */
    private String title;

    /** 文章内容（非空） */
    private String content;

    /** 分类ID（外键关联 category 表） */
    private Long categoryId;

    /** 作者ID（外键关联 user 表） */
    private Long userId;

    /** 阅读量（默认 0） */
    private Integer viewCount;

    /** 发布状态：0-草稿，1-已发布 */
    private Integer isPublish;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
