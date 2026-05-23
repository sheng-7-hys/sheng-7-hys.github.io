package com.sheng.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sheng.blog.entity.Comment;
import com.sheng.blog.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论 Mapper 接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询文章的评论列表（关联评论者信息）
     *
     * @param articleId 文章ID
     * @return 评论 VO 列表
     */
    List<CommentVO> selectCommentVOByArticleId(@Param("articleId") Long articleId);
}
