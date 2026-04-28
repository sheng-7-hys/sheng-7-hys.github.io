package com.sheng.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sheng.blog.entity.Article;
import com.sheng.blog.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章 Mapper 接口
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章（关联分类、作者信息）
     *
     * @param page       分页参数
     * @param categoryId 分类ID（null 表示不过滤）
     * @param isPublish  发布状态（null 表示不过滤）
     * @return 文章 VO 分页结果
     */
    IPage<ArticleVO> selectArticleVOPage(
            Page<ArticleVO> page,
            @Param("categoryId") Long categoryId,
            @Param("isPublish") Integer isPublish
    );

    /**
     * 根据ID查询文章详情（关联分类、作者信息）
     *
     * @param id 文章ID
     * @return 文章 VO
     */
    ArticleVO selectArticleVOById(@Param("id") Long id);
}
