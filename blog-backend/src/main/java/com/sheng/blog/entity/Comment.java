package com.sheng.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {

    /** 主键（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章ID（外键关联 article 表） */
    private Long articleId;

    /** 评论者ID（外键关联 user 表） */
    private Long userId;

    /** 评论内容 */
    private String content;

    /** 父评论ID（0 表示顶层评论） */
    private Long parentId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
