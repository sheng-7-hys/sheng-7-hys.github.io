package com.sheng.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文章发布/编辑 DTO
 */
@Data
public class ArticleDTO {

    /** 文章ID（编辑时传入，新增时为 null） */
    private Long id;

    /** 文章标题 */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /** 文章内容 */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /** 分类ID */
    private Long categoryId;

    /** 发布状态：0-草稿，1-已发布 */
    @NotNull(message = "发布状态不能为空")
    private Integer isPublish;
}
