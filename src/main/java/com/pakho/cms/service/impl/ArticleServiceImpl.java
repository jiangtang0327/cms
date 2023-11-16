package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Article;
import com.pakho.cms.bean.Category;
import com.pakho.cms.bean.extend.ArticleExtend;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.ArticleMapper;
import com.pakho.cms.mapper.CategoryMapper;
import com.pakho.cms.mapper.CommentMapper;
import com.pakho.cms.service.ArticleService;
import com.pakho.cms.util.JwtUtil;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author dgvt
 * @description 针对表【cms_article】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean saveOrUpdate(Article article) {
        if (article == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Integer categoryId = article.getCategoryId();
        if (categoryId == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Category category = categoryMapper.selectById(categoryId);
        if (category == null || category.getParentId() == null) {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }

        Long id = article.getId();
        if (id == null) {
            long userId = Long.parseLong(JwtUtil.getUserId(getToken()));
            article.setUserId(userId);
            article.setPublishTime(new Date());
            articleMapper.insert(article);
        } else {
            Category category1 = categoryMapper.selectById(id);
            if (category1 == null) {
                throw new ServiceException(ResultCode.PARAM_IS_INVALID);
            }
            articleMapper.updateById(article);
        }
        return true;
    }

    @Override
    public void reviewArticle(Long id, String status) {
        if (id == null || status == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        article.setStatus(status);
        articleMapper.updateById(article);

    }

    @Override
    public void deleteInBatch(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        int i = articleMapper.deleteBatchIds(ids);
        if (i == 0) {
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    @Override
    public ArticleExtend queryById(Long id) {
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        return null;
    }

    private String getToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); // 获取当前请求的属性
        HttpServletRequest request = requestAttributes.getRequest(); // 获取当前请求
        String token = request.getHeader("Authorization");
        return token;
    }
}




