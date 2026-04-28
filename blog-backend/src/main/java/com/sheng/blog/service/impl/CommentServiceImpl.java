package com.sheng.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheng.blog.common.BlogException;
import com.sheng.blog.entity.Comment;
import com.sheng.blog.mapper.CommentMapper;
import com.sheng.blog.service.CommentService;
import com.sheng.blog.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    /**
     * 查询文章的评论列表（含嵌套回复结构）
     * 将平铺的评论列表按 parentId 组装成树形结构
     *
     * @param articleId 文章ID
     * @return 评论 VO 列表（顶层评论含 children 子评论）
     */
    @Override
    public List<CommentVO> listComments(Long articleId) {
        // 查询该文章所有评论（平铺列表）
        List<CommentVO> allComments = baseMapper.selectCommentVOByArticleId(articleId);

        // 按 parentId 分组：key=parentId
        Map<Long, List<CommentVO>> childrenMap = allComments.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() != 0)
                .collect(Collectors.groupingBy(CommentVO::getParentId));

        // 取出顶层评论（parentId == 0），为每个顶层评论附加子评论
        List<CommentVO> topLevel = allComments.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .peek(c -> c.setChildren(childrenMap.getOrDefault(c.getId(), new ArrayList<>())))
                .collect(Collectors.toList());

        return topLevel;
    }

    /**
     * 新增评论
     *
     * @param articleId 文章ID
     * @param userId    评论者ID
     * @param content   评论内容
     * @param parentId  父评论ID（0 表示顶层）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(Long articleId, Long userId, String content, Long parentId) {
        if (content == null || content.isBlank()) {
            throw new BlogException(400, "评论内容不能为空");
        }
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content.trim());
        comment.setParentId(parentId == null ? 0L : parentId);
        save(comment);
        log.info("用户 {} 对文章 {} 发表评论", userId, articleId);
    }
}
