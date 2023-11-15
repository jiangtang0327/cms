package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Category;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.CategoryMapper;
import com.pakho.cms.service.CategoryService;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dgvt
 * @description 针对表【cms_category】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public boolean save(Category category) {
        // 获取要插入的category的名称
        String name = category.getName();
        // 创建一个LambdaQueryWrapper对象 qw
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        // 设置查询条件，查询名称等于name的category
        qw.eq(Category::getName, name);
        // 根据qw查询一个category对象 category1
        Category category1 = categoryMapper.selectOne(qw);
        // 如果查询到了category1，抛出DataServiceException异常，异常代码为ResultCode.DATA_EXISTED
        if (category1 != null) {
            throw new ServiceException(ResultCode.DATA_EXISTED);
        }

        // 获取要插入的category的父级id
        Integer parentId = category.getParentId();
        // 如果父级id不为空
        if (parentId != null) {
            // 根据父级id查询一个category对象 category1
            Category category2 = categoryMapper.selectById(parentId);
            // 如果查询不到category2，抛出DataServiceException异常，异常代码为ResultCode.PARENT_ID_NONE
            if (category2 == null) {
                throw new ServiceException(ResultCode.PARENT_ID_NONE);
            }
        }

        // 将category对象插入到数据库，返回插入的记录数
        int insert = categoryMapper.insert(category);
        // 如果插入的记录数大于0，返回true
        if (insert > 0) {
            return true;
        } else {
            // 如果插入的记录数等于0，抛出DataServiceException异常，异常代码为ResultCode.FAILURE
            throw new ServiceException(ResultCode.FAILURE);
        }

    }
}




