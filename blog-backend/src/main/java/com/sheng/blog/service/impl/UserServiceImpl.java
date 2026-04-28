package com.sheng.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheng.blog.common.BlogException;
import com.sheng.blog.dto.LoginDTO;
import com.sheng.blog.dto.RegisterDTO;
import com.sheng.blog.entity.User;
import com.sheng.blog.mapper.UserMapper;
import com.sheng.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /** BCrypt 密码加密器 */
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /** Session 中存储当前登录用户的 key */
    public static final String SESSION_USER_KEY = "loginUser";

    /**
     * 用户注册
     * 校验用户名唯一性，密码 BCrypt 加密后存储
     *
     * @param dto 注册信息（用户名、密码、邮箱）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 校验用户名是否已存在
        long count = count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BlogException(400, "用户名已存在，请换一个");
        }

        // 构建用户对象，密码 BCrypt 加密
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(PASSWORD_ENCODER.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        // 默认头像使用 GitHub 头像格式
        user.setAvatar("https://github.com/" + dto.getUsername() + ".png");
        user.setRole(0); // 0-普通用户

        save(user);
        log.info("新用户注册成功：{}", dto.getUsername());
    }

    /**
     * 用户登录
     * 校验账号密码，登录成功后将用户信息写入 Session
     *
     * @param dto     登录信息（用户名、密码）
     * @param session HTTP Session
     * @return 登录成功的用户对象
     */
    @Override
    public User login(LoginDTO dto, HttpSession session) {
        // 根据用户名查询用户
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BlogException(400, "用户名或密码错误");
        }

        // 校验密码（BCrypt 比对）
        if (!PASSWORD_ENCODER.matches(dto.getPassword(), user.getPassword())) {
            throw new BlogException(400, "用户名或密码错误");
        }

        // 写入 Session（不存储密码字段）
        user.setPassword(null);
        session.setAttribute(SESSION_USER_KEY, user);
        log.info("用户登录成功：{}", dto.getUsername());
        return user;
    }

    /**
     * 用户退出
     * 销毁 Session 中的登录态
     *
     * @param session HTTP Session
     */
    @Override
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
    }

    /**
     * 修改个人信息（邮箱、头像）
     *
     * @param userId 用户ID
     * @param email  新邮箱（可为 null）
     * @param avatar 新头像路径（可为 null）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, String email, String avatar) {
        User user = getById(userId);
        if (user == null) {
            throw new BlogException("用户不存在");
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (avatar != null && !avatar.isBlank()) {
            user.setAvatar(avatar);
        }
        updateById(user);
    }
}
