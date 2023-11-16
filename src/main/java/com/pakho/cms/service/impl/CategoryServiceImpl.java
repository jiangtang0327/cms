package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Article;
import com.pakho.cms.bean.Category;
import com.pakho.cms.bean.extend.CategoryExtend;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.ArticleMapper;
import com.pakho.cms.mapper.CategoryMapper;
import com.pakho.cms.service.CategoryService;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author dgvt
 * @description 针对表【cms_category】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public boolean save(Category category) {
        if (category.getName() == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
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
        if(category.getOrderNum()==null)
            category.setOrderNum(0);

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

    @Override
    public boolean updateById(Category category) {
        if (category.getId() == null) {
            // 如果category的id为空，则抛出参数不能为空的异常
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        // 将category的id赋值给id变量
        Integer id = category.getId();
        // 根据id在categoryMapper中查询categoryOld对象
        Category categoryOld = categoryMapper.selectById(id);

        if (categoryOld.getParentId() != null && category.getParentId() == null) {
            throw new ServiceException(ResultCode.FAILURE);
        }
        if (categoryOld.getParentId() == null && category.getParentId() != null) {
            throw new ServiceException(ResultCode.FAILURE);
        }


        if (!categoryOld.getName().equals(category.getName())) {
            // 获取要更新的category的名称
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

        int i = categoryMapper.updateById(category);
        if (i <= 0) {
            throw new ServiceException(ResultCode.FAILURE);
        }

        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(Category::getParentId, id);
        List<Category> categories = categoryMapper.selectList(qw);
        if (categories.size() > 0) {
            throw new ServiceException(ResultCode.FAILURE);
        }

        Category category = categoryMapper.selectById(id);

        if (category.getParentId() == null) {
            LambdaQueryWrapper<Article> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(Article::getCategoryId, id);
            List<Article> articles = articleMapper.selectList(qw1);
            if (articles.size() > 0) {
                throw new ServiceException(ResultCode.FAILURE);
            }
        }
        int i = categoryMapper.deleteById(id);
        if (i <= 0) {
            throw new ServiceException(ResultCode.FAILURE);
        }
        return true;
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            this.removeById(ids.get(i));
        }
    }

    @Override
    public IPage<Category> query(Integer pageNum, Integer pageSize, Integer parentId) {
        if (pageNum == null || pageNum <= 0 || pageSize == null || pageSize <= 0)
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);

        IPage<Category> p = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();

        qw.eq(parentId != null, Category::getParentId, parentId);
        qw.orderByAsc(Category::getParentId)
                .orderByAsc(Category::getOrderNum);
        categoryMapper.selectPage(p, qw);

        return p;
    }

    @Override
    public List<CategoryExtend> queryAllParent() {
        List<CategoryExtend> categoryExtends = categoryMapper.queryAllWithCates();
        return categoryExtends;
    }
}




