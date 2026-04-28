package com.sheng.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
@TableName("category")
public class Category {

    /** 主键（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名称（唯一） */
    private String name;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
