package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Article;
import com.pakho.cms.bean.Comment;
import com.pakho.cms.bean.Subcomment;
import com.pakho.cms.bean.User;
import com.pakho.cms.bean.extend.CommentExtend;
import com.pakho.cms.bean.extend.SubCommentExtend;
import com.pakho.cms.bean.vo.CommentDeleteParam;
import com.pakho.cms.bean.vo.CommentQueryParam;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.ArticleMapper;
import com.pakho.cms.mapper.CommentMapper;
import com.pakho.cms.mapper.SubcommentMapper;
import com.pakho.cms.mapper.UserMapper;
import com.pakho.cms.service.CommentService;
import com.pakho.cms.util.JwtUtil;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dgvt
 * @description 针对表【cms_comment】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SubcommentMapper subcommentMapper;

    @Override
    public void saveComment(Comment comment) {
        // 验证comment是否为空
        if (comment == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

        // 验证文章是否存在
        Long articleId = comment.getArticleId();
        Article article = articleMapper.selectById(articleId);
        if (articleId == null || article == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        // 验证作者是否存在
        Long authorId = article.getUserId();
        if (authorId == null || !userMapper.selectById(authorId).getStatus().equals("启用"))
            throw new ServiceException(ResultCode.AUTHOR_NOT_EXIST);

        // 验证该登录用户是否存在
        String token = JwtUtil.getToken();
        // 验证token是否存在
        if (token == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        } else {
            // 验证userId是否存在
            String userId = JwtUtil.getUserId(token);
            if (userId == null) {
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
            // 验证用户是否存在，并且状态是否为启用
            User user = userMapper.selectById(userId);
            if (user == null || user.getStatus().equals("禁用")) {
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
        }
        //设置时间
        comment.setPublishTime(new Date());
        //插入
        commentMapper.insert(comment);
    }


    public void saveSubComment(Subcomment subcomment) {
        // 校验subcomment
        if (subcomment == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 校验parentId,以及一级评论是否存在
        Long parentId = subcomment.getParentId();
        if (parentId == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        Comment comment = commentMapper.selectById(parentId);
        if (comment == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        // 验证文章是否存在
        Long articleId = comment.getArticleId();
        if (articleId == null)
            throw new ServiceException(ResultCode.DATA_NONE);
        Article article = articleMapper.selectById(articleId);
        if (article == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        // 验证作者是否存在
        Long authorId = article.getUserId();
        if (authorId == null || !userMapper.selectById(authorId).getStatus().equals("启用"))
            throw new ServiceException(ResultCode.AUTHOR_NOT_EXIST);

        // 验证回复是否存在
        Long replyId = subcomment.getReplyId();
        if (replyId != null) {

            Subcomment reply = subcommentMapper.selectById(replyId);
            if (reply == null)
                throw new ServiceException(ResultCode.DATA_NONE);
        }

        // 验证该登录用户是否存在
        String token = JwtUtil.getToken();
        // 验证token是否存在
        if (token == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        } else {
            // 验证userId是否存在
            String userId = JwtUtil.getUserId(token);
            if (userId == null) {
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
            // 验证用户是否存在，并且状态是否为启用
            User user = userMapper.selectById(userId);
            if (user == null || user.getStatus().equals("禁用")) {
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
        }

        //设置时间
        subcomment.setPublishTime(new Date());

        //插入
        subcommentMapper.insert(subcomment);
    }

    @Override
    public void deleteById(CommentDeleteParam commentDeleteParam) {
        // 校验参数
        if (commentDeleteParam == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        Long id = commentDeleteParam.getId();
        Integer type = commentDeleteParam.getType();
        if (id == null || type == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        if (type == 1) {
            // 校验一级评论是否存在
            Comment comment = commentMapper.selectById(id);
            if (comment == null)
                throw new ServiceException(ResultCode.DATA_NONE);
            commentMapper.deleteById(id);
        } else if (type == 2) {
            // 校验二级评论是否存在
            Subcomment subcomment = subcommentMapper.selectById(id);
            if (subcomment == null)
                throw new ServiceException(ResultCode.DATA_NONE);
            subcommentMapper.deleteById(id);
        } else {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
    }

    @Override
    public void deleteInBatch(List<CommentDeleteParam> list) {
        int i = 0;
        for (CommentDeleteParam commentDeleteParam : list) {
            try {
                deleteById(commentDeleteParam);
                i++;
            } catch (ServiceException e) {
            }
        }
        if (i <= 0)
            throw new ServiceException(ResultCode.FAILURE);
    }

    @Override
    public List<SubCommentExtend> queryByCommentId(Long id) {
        // 校验id
        if (id == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 校验一级评论是否存在
        Comment comment = commentMapper.selectById(id);
        if (comment == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        // 校验文章是否存在
        Long articleId = comment.getArticleId();
        if (articleId == null)
            throw new ServiceException(ResultCode.DATA_NONE);
        Article article = articleMapper.selectById(articleId);
        if (article == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        // 获取二级评论
        LambdaQueryWrapper<Subcomment> qw = new LambdaQueryWrapper<Subcomment>().eq(Subcomment::getParentId, id)
                .orderByAsc(Subcomment::getPublishTime);
        List<Subcomment> subcomments = subcommentMapper.selectList(qw);

        List<SubCommentExtend> list = new ArrayList<>();

        for (Subcomment subcomment : subcomments) {
            // 封装subcomment
            SubCommentExtend subCommentExtend = new SubCommentExtend();
            BeanUtils.copyProperties(subcomment, subCommentExtend);

            Long userId = subcomment.getUserId();
            if (userId == null)
                continue;
            User user = userMapper.selectById(userId);
            if (user == null)
                continue;
            user.setPassword("******");
            subCommentExtend.setAuthor(user);

            list.add(subCommentExtend);
        }

        return list;
    }

    @Override
    public IPage<CommentExtend> queryByArticleId(Integer pageNum, Integer pageSize, Long id) {
        // 校验参数
        if (id == null || pageNum == null || pageSize == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 校验文章是否存在
        Article article = articleMapper.selectById(id);
        if (article == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        // 分页获取一级评论
//        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));
        LambdaQueryWrapper<Comment> eq = new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id);
        IPage<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum, pageSize), eq);
        List<Comment> comments = commentPage.getRecords();

        List<CommentExtend> commentExtendList = new ArrayList<>();
        for (Comment comment : comments) {
            // 获取作者
            Long userId = comment.getUserId();
            if (userId == null)
                continue;
            User user = userMapper.selectById(userId);
            // 获取二级评论
            List<SubCommentExtend> list = queryByCommentId(comment.getId());

            // 封装评论
            CommentExtend commentExtend = new CommentExtend();
            BeanUtils.copyProperties(comment, commentExtend);
            user.setPassword("******");
            commentExtend.setAuthor(user);
            commentExtend.setChildComments(list);
            commentExtendList.add(commentExtend);
        }

        // 封装分页
        IPage<CommentExtend> page = new Page<>();
        page.setRecords(commentExtendList);
        page.setCurrent(commentPage.getCurrent());
        page.setTotal(commentPage.getTotal());
        page.setPages(commentPage.getPages());
        page.setSize(commentPage.getSize());
        return page;
    }

    @Override
    public IPage<CommentExtend> query(CommentQueryParam commentQueryParam) {
        if (commentQueryParam == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        if (commentQueryParam.getPageNum() == null || commentQueryParam.getPageSize() == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 分页获取一级评论
        IPage<Comment> commentPage = new Page<>();
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.hasText(commentQueryParam.getKeyword()), Comment::getContent, commentQueryParam.getKeyword())
                .eq(commentQueryParam.getUserId() != null, Comment::getUserId, commentQueryParam.getUserId())
                .eq(commentQueryParam.getArticleId() != null, Comment::getArticleId, commentQueryParam.getArticleId())
                .gt(commentQueryParam.getStartTime() != null, Comment::getPublishTime, commentQueryParam.getStartTime())
                .lt(commentQueryParam.getEndTime() != null, Comment::getPublishTime, commentQueryParam.getEndTime());
        commentMapper.selectPage(commentPage, qw);
        List<Comment> comments = commentPage.getRecords();

        List<CommentExtend> commentExtendList = new ArrayList<>();
        for (Comment comment : comments) {
            // 获取作者
            Long userId = comment.getUserId();
            if (userId == null)
                continue;
            User user = userMapper.selectById(userId);
            // 获取二级评论
            List<SubCommentExtend> list = queryByCommentId(comment.getId());

            // 封装评论
            CommentExtend commentExtend = new CommentExtend();
            BeanUtils.copyProperties(comment, commentExtend);
            user.setPassword("******");
            commentExtend.setAuthor(user);
            commentExtend.setChildComments(list);
            commentExtendList.add(commentExtend);
        }

        // 封装分页
        IPage<CommentExtend> page = new Page<>();
        page.setRecords(commentExtendList);
        page.setCurrent(commentPage.getCurrent());
        page.setTotal(commentPage.getTotal());
        page.setPages(commentPage.getPages());
        page.setSize(commentPage.getSize());
        return page;
    }
}




