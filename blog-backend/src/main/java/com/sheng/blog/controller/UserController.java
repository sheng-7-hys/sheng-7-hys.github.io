package com.sheng.blog.controller;

import com.sheng.blog.common.BlogException;
import com.sheng.blog.common.Result;
import com.sheng.blog.dto.LoginDTO;
import com.sheng.blog.dto.RegisterDTO;
import com.sheng.blog.entity.User;
import com.sheng.blog.service.UserService;
import com.sheng.blog.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 用户模块控制器
 * 处理注册、登录、退出、个人信息修改等请求
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 展示登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // 已登录则跳转首页
        if (session.getAttribute(UserServiceImpl.SESSION_USER_KEY) != null) {
            return "redirect:/";
        }
        return "user/login";
    }

    /**
     * 处理登录表单提交
     * POST /login
     *
     * @param dto     登录信息（用户名、密码）
     * @param session HTTP Session
     * @param model   模板数据
     * @return 登录成功跳转首页，失败返回登录页并提示错误
     */
    @PostMapping("/login")
    public String login(LoginDTO dto, HttpSession session, Model model) {
        try {
            userService.login(dto, session);
            return "redirect:/";
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("dto", dto);
            return "user/login";
        }
    }

    /**
     * 展示注册页面
     * GET /register
     */
    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute(UserServiceImpl.SESSION_USER_KEY) != null) {
            return "redirect:/";
        }
        return "user/register";
    }

    /**
     * 处理注册表单提交
     * POST /register
     *
     * @param dto     注册信息（用户名、密码、邮箱）
     * @param session HTTP Session
     * @param model   模板数据
     * @return 注册成功跳转登录页，失败返回注册页并提示错误
     */
    @PostMapping("/register")
    public String register(RegisterDTO dto, HttpSession session, Model model) {
        // 简单后端校验
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            model.addAttribute("error", "用户名不能为空");
            model.addAttribute("dto", dto);
            return "user/register";
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            model.addAttribute("error", "密码长度不能少于 6 位");
            model.addAttribute("dto", dto);
            return "user/register";
        }
        try {
            userService.register(dto);
            return "redirect:/login?registered=1";
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("dto", dto);
            return "user/register";
        }
    }

    /**
     * 用户退出
     * GET /logout
     *
     * @param session HTTP Session
     * @return 跳转首页
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/";
    }

    /**
     * 展示个人信息页面
     * GET /profile
     *
     * @param session HTTP Session
     * @param model   模板数据
     * @return 个人信息页面（未登录则跳转登录页）
     */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        // 重新查询最新用户信息
        User user = userService.getById(loginUser.getId());
        user.setPassword(null);
        model.addAttribute("user", user);
        return "user/profile";
    }

    /**
     * 处理个人信息修改表单提交
     * POST /profile
     *
     * @param email   新邮箱
     * @param avatar  新头像 URL
     * @param session HTTP Session
     * @param model   模板数据
     * @return 修改成功刷新页面，失败返回个人信息页并提示错误
     */
    @PostMapping("/profile")
    public String updateProfile(@RequestParam(required = false) String email,
                                @RequestParam(required = false) String avatar,
                                HttpSession session,
                                Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        try {
            userService.updateProfile(loginUser.getId(), email, avatar);
            // 更新 Session 中的用户信息
            User updatedUser = userService.getById(loginUser.getId());
            updatedUser.setPassword(null);
            session.setAttribute(UserServiceImpl.SESSION_USER_KEY, updatedUser);
            return "redirect:/profile?success=1";
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", loginUser);
            return "user/profile";
        }
    }
}
