package com.it.cms.bean.extend;

import com.it.cms.bean.Article;
import com.it.cms.bean.Comment;
import com.it.cms.bean.User;
import lombok.Data;

import java.util.List;

@Data
public class ArticleExtend extends Article {
    //包含3条一级评论
    private List<Comment> comments;
    //新增文章作者
    private User author;
}
