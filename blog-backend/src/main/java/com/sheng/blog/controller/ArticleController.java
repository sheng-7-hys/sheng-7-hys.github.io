package com.sheng.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sheng.blog.common.BlogException;
import com.sheng.blog.dto.ArticleDTO;
import com.sheng.blog.entity.User;
import com.sheng.blog.service.ArticleService;
import com.sheng.blog.service.CategoryService;
import com.sheng.blog.service.CommentService;
import com.sheng.blog.service.impl.UserServiceImpl;
import com.sheng.blog.vo.ArticleVO;
import com.sheng.blog.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章模块控制器
 * 处理文章列表、详情、发布、编辑、删除请求
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    /**
     * 首页：展示已发布文章列表（分页 + 分类筛选）
     * GET /
     *
     * @param page       当前页（默认 1）
     * @param categoryId 分类ID（可选）
     * @param model      模板数据
     * @return 首页模板
     */
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(required = false) Long categoryId,
                        Model model,
                        HttpSession session) {
        Page<ArticleVO> pageResult = articleService.listArticles(
                new Page<>(page, 10), categoryId, 1);
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("loginUser", session.getAttribute(UserServiceImpl.SESSION_USER_KEY));
        return "index";
    }

    /**
     * 文章归档页：展示所有已发布文章（按年份归档）
     * GET /archives
     *
     * @param model 模板数据
     * @return 归档页模板
     */
    @GetMapping("/archives")
    public String archives(Model model, HttpSession session) {
        Page<ArticleVO> pageResult = articleService.listArticles(
                new Page<>(1, 1000), null, 1);
        // 按年份分组，保持时间倒序（LinkedHashMap 保持插入顺序）
        Map<String, List<ArticleVO>> articlesByYear = pageResult.getRecords().stream()
                .collect(Collectors.groupingBy(
                        a -> String.valueOf(a.getCreateTime().getYear()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        model.addAttribute("articlesByYear", articlesByYear);
        model.addAttribute("loginUser", session.getAttribute(UserServiceImpl.SESSION_USER_KEY));
        return "archives";
    }

    /**
     * 关于我页面
     * GET /about
     *
     * @param model 模板数据
     * @return 关于我模板
     */
    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        model.addAttribute("loginUser", session.getAttribute(UserServiceImpl.SESSION_USER_KEY));
        return "about";
    }

    /**
     * 文章详情页
     * GET /article/{id}
     *
     * @param id    文章ID
     * @param model 模板数据
     * @return 文章详情页模板
     */
    @GetMapping("/article/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        ArticleVO article = articleService.getArticleDetail(id);
        List<CommentVO> comments = commentService.listComments(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("loginUser", session.getAttribute(UserServiceImpl.SESSION_USER_KEY));
        return "article/detail";
    }

    /**
     * 文章发布页（需登录）
     * GET /article/publish
     *
     * @param model   模板数据
     * @param session HTTP Session
     * @return 文章发布页模板
     */
    @GetMapping("/article/publish")
    public String publishPage(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("loginUser", loginUser);
        return "article/publish";
    }

    /**
     * 处理文章发布表单提交
     * POST /article/publish
     *
     * @param dto     文章信息
     * @param session HTTP Session
     * @param model   模板数据
     * @return 发布成功跳转首页，失败返回发布页并提示错误
     */
    @PostMapping("/article/publish")
    public String publish(ArticleDTO dto, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        try {
            articleService.publishArticle(dto, loginUser.getId());
            return "redirect:/";
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.listAll());
            model.addAttribute("dto", dto);
            model.addAttribute("loginUser", loginUser);
            return "article/publish";
        }
    }

    /**
     * 文章编辑页（需登录，且为作者或管理员）
     * GET /article/{id}/edit
     *
     * @param id      文章ID
     * @param session HTTP Session
     * @param model   模板数据
     * @return 文章编辑页模板
     */
    @GetMapping("/article/{id}/edit")
    public String editPage(@PathVariable Long id, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        ArticleVO article = articleService.getArticleDetail(id);
        // 权限校验
        boolean isAdmin = Integer.valueOf(1).equals(loginUser.getRole());
        if (!isAdmin && !article.getUserId().equals(loginUser.getId())) {
            model.addAttribute("error", "无权限编辑此文章");
            return "redirect:/";
        }
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("loginUser", loginUser);
        return "article/edit";
    }

    /**
     * 处理文章编辑表单提交
     * POST /article/{id}/edit
     *
     * @param id      文章ID
     * @param dto     文章信息
     * @param session HTTP Session
     * @param model   模板数据
     * @return 编辑成功跳转文章详情页，失败返回编辑页并提示错误
     */
    @PostMapping("/article/{id}/edit")
    public String edit(@PathVariable Long id, ArticleDTO dto,
                       HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        dto.setId(id);
        boolean isAdmin = Integer.valueOf(1).equals(loginUser.getRole());
        try {
            articleService.editArticle(dto, loginUser.getId(), isAdmin);
            return "redirect:/article/" + id;
        } catch (BlogException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("article", articleService.getArticleDetail(id));
            model.addAttribute("categories", categoryService.listAll());
            model.addAttribute("loginUser", loginUser);
            return "article/edit";
        }
    }

    /**
     * 删除文章（POST 请求，需登录且为作者或管理员）
     * POST /article/{id}/delete
     *
     * @param id      文章ID
     * @param session HTTP Session
     * @return 删除成功跳转首页
     */
    @PostMapping("/article/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        boolean isAdmin = Integer.valueOf(1).equals(loginUser.getRole());
        articleService.deleteArticle(id, loginUser.getId(), isAdmin);
        return "redirect:/";
    }
}
