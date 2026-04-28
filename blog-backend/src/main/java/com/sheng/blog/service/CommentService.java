package com.sheng.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sheng.blog.entity.Comment;
import com.sheng.blog.vo.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查询文章的评论列表（含嵌套回复）
     *
     * @param articleId 文章ID
     * @return 评论 VO 列表（顶层评论含子评论）
     */
    List<CommentVO> listComments(Long articleId);

    /**
     * 新增评论
     *
     * @param articleId 文章ID
     * @param userId    评论者ID
     * @param content   评论内容
     * @param parentId  父评论ID（0 表示顶层）
     */
    void addComment(Long articleId, Long userId, String content, Long parentId);
}
