package com.sheng.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheng.blog.common.BlogException;
import com.sheng.blog.dto.ArticleDTO;
import com.sheng.blog.entity.Article;
import com.sheng.blog.mapper.ArticleMapper;
import com.sheng.blog.service.ArticleService;
import com.sheng.blog.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章服务实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    /**
     * 分页查询文章列表（可按分类、发布状态筛选）
     *
     * @param page       分页参数
     * @param categoryId 分类ID（null 表示不过滤）
     * @param isPublish  发布状态（null 表示不过滤）
     * @return 文章 VO 分页结果
     */
    @Override
    public Page<ArticleVO> listArticles(Page<ArticleVO> page, Long categoryId, Integer isPublish) {
        IPage<ArticleVO> result = baseMapper.selectArticleVOPage(page, categoryId, isPublish);
        return (Page<ArticleVO>) result;
    }

    /**
     * 根据ID查询文章详情，同时自增阅读量
     *
     * @param id 文章ID
     * @return 文章 VO
     */
    @Override
    public ArticleVO getArticleDetail(Long id) {
        // 自增阅读量（直接更新数据库，忽略并发竞争，计数允许轻微误差）
        Article article = getById(id);
        if (article == null) {
            throw new BlogException(404, "文章不存在");
        }
        article.setViewCount(article.getViewCount() + 1);
        updateById(article);

        // 查询完整 VO（含分类名、作者名）
        return baseMapper.selectArticleVOById(id);
    }

    /**
     * 发布或保存草稿文章
     *
     * @param dto    文章信息
     * @param userId 当前登录用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(ArticleDTO dto, Long userId) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setCategoryId(dto.getCategoryId());
        article.setUserId(userId);
        article.setViewCount(0);
        article.setIsPublish(dto.getIsPublish());
        save(article);
        log.info("用户 {} 发布文章：{}", userId, dto.getTitle());
    }

    /**
     * 编辑文章（仅作者或管理员可操作）
     *
     * @param dto     文章信息（含ID）
     * @param userId  当前登录用户ID
     * @param isAdmin 是否为管理员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editArticle(ArticleDTO dto, Long userId, boolean isAdmin) {
        Article article = getById(dto.getId());
        if (article == null) {
            throw new BlogException(404, "文章不存在");
        }
        // 权限校验：仅作者或管理员可编辑
        if (!isAdmin && !article.getUserId().equals(userId)) {
            throw new BlogException(403, "无权限编辑此文章");
        }

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setCategoryId(dto.getCategoryId());
        article.setIsPublish(dto.getIsPublish());
        updateById(article);
        log.info("用户 {} 编辑文章 ID：{}", userId, dto.getId());
    }

    /**
     * 删除文章（仅作者或管理员可操作）
     *
     * @param id      文章ID
     * @param userId  当前登录用户ID
     * @param isAdmin 是否为管理员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id, Long userId, boolean isAdmin) {
        Article article = getById(id);
        if (article == null) {
            throw new BlogException(404, "文章不存在");
        }
        // 权限校验：仅作者或管理员可删除
        if (!isAdmin && !article.getUserId().equals(userId)) {
            throw new BlogException(403, "无权限删除此文章");
        }

        removeById(id);
        log.info("用户 {} 删除文章 ID：{}", userId, id);
    }
}
