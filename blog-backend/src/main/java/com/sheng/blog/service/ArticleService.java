package com.sheng.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheng.blog.dto.ArticleDTO;
import com.sheng.blog.entity.Article;
import com.sheng.blog.vo.ArticleVO;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章列表（可按分类、发布状态筛选）
     *
     * @param page       分页参数（当前页、每页数量）
     * @param categoryId 分类ID（null 表示不过滤）
     * @param isPublish  发布状态（null 表示不过滤）
     * @return 文章 VO 分页结果
     */
    Page<ArticleVO> listArticles(Page<ArticleVO> page, Long categoryId, Integer isPublish);

    /**
     * 根据ID查询文章详情，同时自增阅读量
     *
     * @param id 文章ID
     * @return 文章 VO
     */
    ArticleVO getArticleDetail(Long id);

    /**
     * 发布或保存草稿文章
     *
     * @param dto    文章信息
     * @param userId 当前登录用户ID
     */
    void publishArticle(ArticleDTO dto, Long userId);

    /**
     * 编辑文章（仅作者或管理员可操作）
     *
     * @param dto    文章信息（含ID）
     * @param userId 当前登录用户ID
     * @param isAdmin 是否为管理员
     */
    void editArticle(ArticleDTO dto, Long userId, boolean isAdmin);

    /**
     * 删除文章（仅作者或管理员可操作）
     *
     * @param id      文章ID
     * @param userId  当前登录用户ID
     * @param isAdmin 是否为管理员
     */
    void deleteArticle(Long id, Long userId, boolean isAdmin);
}
