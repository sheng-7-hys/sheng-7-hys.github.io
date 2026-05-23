package com.sheng.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sheng.blog.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询所有分类列表
     *
     * @return 分类列表
     */
    List<Category> listAll();

    /**
     * 新增分类
     *
     * @param name 分类名称
     */
    void addCategory(String name);

    /**
     * 修改分类名称
     *
     * @param id   分类ID
     * @param name 新分类名称
     */
    void updateCategory(Long id, String name);

    /**
     * 删除分类（若有文章关联则拒绝删除）
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
}
