package com.sheng.blog.controller;

import com.sheng.blog.common.BlogException;
import com.sheng.blog.entity.User;
import com.sheng.blog.service.CommentService;
import com.sheng.blog.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 评论模块控制器
 * 处理评论的新增请求
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 新增评论（需登录）
     * POST /comment/add
     *
     * @param articleId 文章ID
     * @param content   评论内容
     * @param parentId  父评论ID（可选，默认 0 为顶层评论）
     * @param session   HTTP Session
     * @return 跳转回文章详情页
     */
    @PostMapping("/comment/add")
    public String addComment(@RequestParam Long articleId,
                             @RequestParam String content,
                             @RequestParam(required = false, defaultValue = "0") Long parentId,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute(UserServiceImpl.SESSION_USER_KEY);
        if (loginUser == null) {
            return "redirect:/login";
        }
        try {
            commentService.addComment(articleId, loginUser.getId(), content, parentId);
        } catch (BlogException e) {
            log.warn("评论新增失败：{}", e.getMessage());
        }
        return "redirect:/article/" + articleId;
    }
}
