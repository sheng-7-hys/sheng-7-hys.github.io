package com.sheng.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User {

    /** 主键（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（唯一、非空） */
    private String username;

    /** 密码（BCrypt 加密存储） */
    private String password;

    /** 邮箱（可选） */
    private String email;

    /** 头像路径 */
    private String avatar;

    /** 用户角色：0-普通用户，1-管理员 */
    private Integer role;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
