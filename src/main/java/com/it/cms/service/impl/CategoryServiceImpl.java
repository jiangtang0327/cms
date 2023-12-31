package com.it.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.cms.bean.Article;
import com.it.cms.bean.Category;
import com.it.cms.bean.User;
import com.it.cms.bean.extend.CategoryExtend;
import com.it.cms.bean.vo.CategoryExport;
import com.it.cms.exception.ServiceException;
import com.it.cms.mapper.ArticleMapper;
import com.it.cms.mapper.CategoryMapper;
import com.it.cms.mapper.UserMapper;
import com.it.cms.service.CategoryService;
import com.it.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;

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
        if (category.getOrderNum() == null)
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
        // 判断id是否为空
        if (category.getId() == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Integer id = category.getId();
        Category categoryOld = categoryMapper.selectById(id);

        // 栏目级别不能改动
        if (categoryOld.getParentId() != null && category.getParentId() == null) {
            throw new ServiceException(ResultCode.CATEGORY_LEVEL_ERROR);
        }
        if (categoryOld.getParentId() == null && category.getParentId() != null) {
            throw new ServiceException(ResultCode.CATEGORY_LEVEL_ERROR);
        }

        // 判断名称是否修改
        if (!categoryOld.getName().equals(category.getName())) {
            String name = category.getName();
            LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
            qw.eq(Category::getName, name);
            Category category1 = categoryMapper.selectOne(qw);
            if (category1 != null) {
                throw new ServiceException(ResultCode.DATA_EXISTED);
            }
        }

        // 判断父级id是否有效
        Integer parentId = category.getParentId();
        if (parentId != null) {
            Category category2 = categoryMapper.selectById(parentId);
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
        // 判断id
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Category category = categoryMapper.selectById(id);
        System.out.println("category = " + category);
        if (category == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        // 父级栏目
        if (category.getParentId() == null) {
            // 判断该栏目是否存在子节点
            LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
            qw.eq(Category::getParentId, id);
            List<Category> categories = categoryMapper.selectList(qw);
            if (categories.size() > 0) {
                throw new ServiceException(ResultCode.FAILURE);
            }
        }
        // 子栏目
        else {
            // 判断该栏目是否存在文章
            LambdaQueryWrapper<Article> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(Article::getCategoryId, id);
            List<Article> articles = articleMapper.selectList(qw1);

            // 如果栏目底下有文章,则判断用户是否注销
            if (articles.size() > 0) {
                for (int i = 0; i < articles.size(); i++) {
                    Long userId = articles.get(i).getUserId();
                    User user = userMapper.selectById(userId);
                    if (user.getDeleted() == 0) {
                        throw new ServiceException(ResultCode.CATEGORY_EXIST_ARTICLE);
                    }
                }
            }
        }
        // 删除该栏目
        int i = categoryMapper.deleteById(id);
        if (i <= 0) {
            throw new ServiceException(ResultCode.FAILURE);
        }
        return true;
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        int j = 0;
        for (int i = 0; i < ids.size(); i++) {
            try {
                boolean b = this.removeById(ids.get(i));
                if (b) {
                    j++;
                }
            } catch (ServiceException e) {
            }
        }
        if (j <= 0) {
            throw new ServiceException(ResultCode.FAILURE);
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

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Category> categories = categoryMapper.selectList(null);
        List<CategoryExport> categoryExports = new ArrayList<>();
        categories.forEach(category -> {
            CategoryExport categoryExport = new CategoryExport();
            categoryExport.setDescription(category.getDescription());
            categoryExport.setName(category.getName());
            categoryExport.setOrderNum(category.getOrderNum());
            if (category.getParentId() != null)
                categoryExport.setParent(categoryMapper.selectById(category.getParentId()).getName());
            categoryExport.setDeleted(category.getDeleted() == 0 ? "未被删除" : "已被删除");
            categoryExports.add(categoryExport);
        });
        //设置编码格式
        response.setCharacterEncoding("utf-8");
        //设置导出文件名称（避免乱码）
        String fileName = URLEncoder.encode("栏目信息".concat(".xlsx"), "UTF-8");
        //设置内容类型
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("content-type", "application/octet-stream");
        //设置响应的编码格式
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        EasyExcel.write(outputStream, CategoryExport.class)
                .sheet("Sheet1") // 设置Excel的sheet名称
                .doWrite(categoryExports); // 写入数据源
    }

    @Override
    public void importExcel(InputStream inputStream) {
        // 读取Excel文件
        List<CategoryExport> categoryExports = EasyExcel.read(inputStream)
                .head(CategoryExport.class)
                .doReadAllSync();
        // 转换成父实体类
        List<Category> categories1 = new ArrayList<>();
        categoryExports.forEach(categoryExport -> {
            Category category = new Category();
            category.setName(categoryExport.getName());
            category.setDescription(categoryExport.getDescription());
            category.setOrderNum(categoryExport.getOrderNum());
            category.setDeleted("已被删除".equals(categoryExport.getDeleted()) ? 1 : 0);
            if (categoryExport.getParent() == null) {
                category.setParentId(null);
                categories1.add(category);
            }
        });

        //检查名字是否重复
        List<Category> categories11 = checkRepetition(categories1);
        saveBatch(categories11);

        // 转换成子实体类
        List<Category> categories2 = new ArrayList<>();
        categoryExports.forEach(categoryExport -> {
            Category category = new Category();
            category.setName(categoryExport.getName());
            category.setDescription(categoryExport.getDescription());
            category.setOrderNum(categoryExport.getOrderNum());
            category.setDeleted("已被删除".equals(categoryExport.getDeleted()) ? 1 : 0);
            if (categoryExport.getParent() != null) {
                //查询父栏目id
                category.setParentId(lambdaQuery().eq(Category::getName, categoryExport.getParent()).one().getId());
                categories2.add(category);
            }
        });
        //检查名字是否重复
        List<Category> categories22 = checkRepetition(categories2);
        saveBatch(categories22);
    }

    private List<Category> checkRepetition(List<Category> list) {
        List<Category> categoryList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(null);
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        list.forEach(t -> {
            if (!names.contains(t.getName())) {
                categoryList.add(t);
            }
        });
        return categoryList;
    }

}




