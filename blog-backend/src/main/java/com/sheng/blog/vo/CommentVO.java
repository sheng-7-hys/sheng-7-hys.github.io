package com.sheng.blog.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论展示 VO（包含作者信息及子评论列表）
 */
@Data
public class CommentVO {

    /** 评论ID */
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 评论者ID */
    private Long userId;

    /** 评论者用户名 */
    private String username;

    /** 评论者头像 */
    private String avatar;

    /** 评论内容 */
    private String content;

    /** 父评论ID（0 表示顶层） */
    private Long parentId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 子评论列表（回复） */
    private List<CommentVO> children;
}
