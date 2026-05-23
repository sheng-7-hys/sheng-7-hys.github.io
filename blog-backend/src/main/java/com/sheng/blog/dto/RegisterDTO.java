package com.sheng.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册 DTO
 */
@Data
public class RegisterDTO {

    /** 用户名（唯一、非空） */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度需在 2-20 之间")
    private String username;

    /** 密码（明文，注册时 BCrypt 加密） */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度不能少于 6 位")
    private String password;

    /** 邮箱（可选） */
    private String email;
}
