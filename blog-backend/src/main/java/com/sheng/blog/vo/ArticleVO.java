package com.sheng.blog.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章展示 VO（包含分类名称、作者用户名等关联信息）
 */
@Data
public class ArticleVO {

    /** 文章ID */
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章内容 */
    private String content;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 作者ID */
    private Long userId;

    /** 作者用户名 */
    private String username;

    /** 作者头像 */
    private String avatar;

    /** 阅读量 */
    private Integer viewCount;

    /** 发布状态：0-草稿，1-已发布 */
    private Integer isPublish;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
