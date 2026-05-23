package com.sheng.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sheng.blog.dto.LoginDTO;
import com.sheng.blog.dto.RegisterDTO;
import com.sheng.blog.entity.User;

import javax.servlet.http.HttpSession;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * 校验用户名唯一性，密码 BCrypt 加密后存储
     *
     * @param dto 注册信息
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录
     * 校验账号密码，登录成功后将用户信息写入 Session
     *
     * @param dto     登录信息
     * @param session HTTP Session
     * @return 登录成功的用户对象
     */
    User login(LoginDTO dto, HttpSession session);

    /**
     * 用户退出
     * 销毁 Session 中的登录态
     *
     * @param session HTTP Session
     */
    void logout(HttpSession session);

    /**
     * 修改个人信息（邮箱、头像）
     *
     * @param userId 用户ID
     * @param email  新邮箱（可为 null）
     * @param avatar 新头像路径（可为 null）
     */
    void updateProfile(Long userId, String email, String avatar);
}
