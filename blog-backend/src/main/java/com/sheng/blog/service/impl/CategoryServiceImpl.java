package com.sheng.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheng.blog.common.BlogException;
import com.sheng.blog.entity.Article;
import com.sheng.blog.entity.Category;
import com.sheng.blog.mapper.ArticleMapper;
import com.sheng.blog.mapper.CategoryMapper;
import com.sheng.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    /**
     * 查询所有分类列表（按创建时间升序）
     *
     * @return 分类列表
     */
    @Override
    public List<Category> listAll() {
        return list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getCreateTime));
    }

    /**
     * 新增分类（校验分类名唯一）
     *
     * @param name 分类名称
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(String name) {
        // 校验分类名是否已存在
        long count = count(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, name));
        if (count > 0) {
            throw new BlogException(400, "分类名称已存在");
        }
        Category category = new Category();
        category.setName(name);
        save(category);
        log.info("新增分类：{}", name);
    }

    /**
     * 修改分类名称（校验新名称唯一）
     *
     * @param id   分类ID
     * @param name 新分类名称
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Long id, String name) {
        // 校验新名称是否与其他分类冲突
        long count = count(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, name)
                .ne(Category::getId, id));
        if (count > 0) {
            throw new BlogException(400, "分类名称已存在");
        }
        Category category = getById(id);
        if (category == null) {
            throw new BlogException(404, "分类不存在");
        }
        category.setName(name);
        updateById(category);
        log.info("修改分类 ID {} 名称为：{}", id, name);
    }

    /**
     * 删除分类（若有文章关联则拒绝删除）
     *
     * @param id 分类ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 检查是否有文章使用该分类
        long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, id));
        if (articleCount > 0) {
            throw new BlogException(400, "该分类下还有文章，无法删除");
        }
        removeById(id);
        log.info("删除分类 ID：{}", id);
    }
}
