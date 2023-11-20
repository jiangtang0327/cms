package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.extend.CommentExtend;
import com.it.cms.bean.extend.SubCommentExtend;
import com.it.cms.bean.vo.CommentDeleteParam;
import com.it.cms.bean.vo.CommentQueryParam;
import com.it.cms.bean.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cms.bean.Subcomment;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_comment】的数据库操作Service
* @createDate 2023-11-14 10:28:41
*/
public interface CommentService extends IService<Comment> {
    void saveComment(Comment comment);
    void saveSubComment(Subcomment subcomment);
    void deleteById(CommentDeleteParam commentDeleteParam);
    void deleteInBatch(List<CommentDeleteParam> list);
    List<SubCommentExtend> queryByCommentId(Long id);
    IPage<CommentExtend> queryByArticleId(Integer pageNum, Integer pageSize, Long id);
    IPage<CommentExtend> query(CommentQueryParam commentQueryParam);
}
