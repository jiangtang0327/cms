package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Article;
import com.pakho.cms.bean.Category;
import com.pakho.cms.bean.Comment;
import com.pakho.cms.bean.User;
import com.pakho.cms.bean.extend.ArticleExtend;
import com.pakho.cms.bean.vo.ArticleParam;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.ArticleMapper;
import com.pakho.cms.mapper.CategoryMapper;
import com.pakho.cms.mapper.CommentMapper;
import com.pakho.cms.mapper.UserMapper;
import com.pakho.cms.service.ArticleService;
import com.pakho.cms.util.JwtUtil;
import com.pakho.cms.util.ResultCode;
import com.pakho.cms.util.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dgvt
 * @description 针对表【cms_article】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final String REDIS_KEY = "Article_Read_Num";
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean saveOrUpdate(Article article) {
        // 校验参数
        if (article == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        // 校验分类
        Integer categoryId = article.getCategoryId();
        if (categoryId == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || category.getParentId() == null) {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }

        Long id = article.getId();
        // 新增
        if (id == null) {
            String token = JwtUtil.getUserId(JwtUtil.getToken());
            long userId = Long.parseLong(token);
            article.setUserId(userId);
            article.setPublishTime(new Date());
            articleMapper.insert(article);
        }
        // 修改
        else {
            // 校验文章
            Article article1 = articleMapper.selectById(id);
            if (article1 == null) {
                throw new ServiceException(ResultCode.PARAM_IS_INVALID);
            }
            article.setStatus("未审核");
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
        // 参数校验
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        // 查询文章
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        // 审核通过的文章才可以被查询
//        if (!article.getStatus().equals("审核通过")) {
//            throw new ServiceException(ResultCode.DATA_NONE);
//        }

        // 验证作者
        Long authorId = article.getUserId();
        User author = userMapper.selectById(authorId);
        if (authorId == null || author == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }

        // 验证vip和文章是否收费，用户是否是作者
        String token = JwtUtil.getToken();
        // 未登录，判断是否是收费文章
        if (token == null) {
            if (article.getCharged() == 1) {
                throw new ServiceException(ResultCode.USER_NOT_IS_VIP);
            }
        }
        // 已登录
        else {
            String userId = JwtUtil.getUserId(token);
            User user = userMapper.selectById(userId);
            if (user.getIsVip() != 1 && article.getCharged() == 1 && userId.equals(Long.toString(authorId))) {
                throw new ServiceException(ResultCode.USER_NOT_IS_VIP);
            }
        }

        // 查询文章的评论
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getArticleId, id)
                .orderByDesc(Comment::getPublishTime)
                .last("limit 3");
        List<Comment> comments = commentMapper.selectList(qw);

        // 封装文章信息
        ArticleExtend articleExtend = new ArticleExtend();
        BeanUtils.copyProperties(article, articleExtend);
        articleExtend.setAuthor(author);
        articleExtend.setComments(comments);

        // 10.浏览量自增
        articleExtend.setReadNum(redisUtil.increment(REDIS_KEY,article.getId().toString()));

        return articleExtend;
    }

    public IPage<ArticleExtend> query(ArticleParam param) {
        // 创建条件构造器，并查询
        Page<Article> page = new Page<>();
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.hasText(param.getTitle()), Article::getTitle, param.getTitle())
                .eq(param.getCategoryId() != null, Article::getCategoryId, param.getCategoryId())
                .eq(StringUtils.hasText(param.getStatus()), Article::getStatus, param.getStatus())
                .eq(param.getUserId() != null, Article::getUserId, param.getUserId())
                .eq(param.getCharged() != null, Article::getCharged, param.getCharged())
                .gt(param.getStartTime() != null, Article::getPublishTime, param.getStartTime())
                .lt(param.getEndTime() != null, Article::getPublishTime, param.getEndTime());
        articleMapper.selectPage(page, qw);

        List<Article> articles = page.getRecords();
        List<ArticleExtend> articleExtends = new ArrayList<>();

        for (int i = 0; i < articles.size(); i++) {
            // 封装作者信息
            User user = userMapper.selectById(articles.get(i).getUserId());
            // 验证作者,作者不存在则跳过
            if (user == null) {
                continue;
            }
            user.setPassword("******");
            ArticleExtend articleExtend = new ArticleExtend();
            articleExtend.setAuthor(user);

            BeanUtils.copyProperties(articles.get(i), articleExtend);
            articleExtends.add(articleExtend);
        }

        // 封装分页信息
        Page<ArticleExtend> articlePage = new Page<>();
        articlePage.setRecords(articleExtends);
        articlePage.setCurrent(page.getCurrent());
        articlePage.setTotal(page.getTotal());

        return articlePage;
    }
}




