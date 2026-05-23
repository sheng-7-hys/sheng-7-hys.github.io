package com.sheng.blog.controller;

import com.sheng.blog.common.BlogException;
import com.sheng.blog.common.Result;
import com.sheng.blog.entity.Category;
import com.sheng.blog.entity.User;
import com.sheng.blog.service.CategoryService;
import com.sheng.blog.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 分类模块控制器
 * 处理分类列表查询、新增、修改、删除请求
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 分类管理页面（需登录，管理员可见）
     * GET /category/manage
     *
     * @param model   模板数据
     * @param session HTTP Session
     * @return 分类管理页模板
     */
    @GetMapping("/category/manage")
    public String managePage(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        // 仅管理员可访问
        if (!Integer.valueOf(1).equals(loginUser.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("loginUser", loginUser);
        return "category/manage";
    }

    /**
     * 新增分类（POST，管理员操作）
     * POST /category/add
     *
     * @param name    分类名称
     * @param session HTTP Session
     * @return 跳转分类管理页
     */
    @PostMapping("/category/add")
    public String addCategory(@RequestParam String name, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null || !Integer.valueOf(1).equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        try {
            categoryService.addCategory(name);
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.listAll());
            model.addAttribute("loginUser", loginUser);
            return "category/manage";
        }
        return "redirect:/category/manage";
    }

    /**
     * 修改分类名称（POST，管理员操作）
     * POST /category/{id}/edit
     *
     * @param id      分类ID
     * @param name    新分类名称
     * @param session HTTP Session
     * @return 跳转分类管理页
     */
    @PostMapping("/category/{id}/edit")
    public String editCategory(@PathVariable Long id, @RequestParam String name,
                               HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null || !Integer.valueOf(1).equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        try {
            categoryService.updateCategory(id, name);
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.listAll());
            model.addAttribute("loginUser", loginUser);
            return "category/manage";
        }
        return "redirect:/category/manage";
    }

    /**
     * 删除分类（POST，管理员操作）
     * POST /category/{id}/delete
     *
     * @param id      分类ID
     * @param session HTTP Session
     * @return 跳转分类管理页
     */
    @PostMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable Long id, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null || !Integer.valueOf(1).equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        try {
            categoryService.deleteCategory(id);
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.listAll());
            model.addAttribute("loginUser", loginUser);
            return "category/manage";
        }
        return "redirect:/category/manage";
    }
}
