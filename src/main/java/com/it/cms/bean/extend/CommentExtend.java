package com.it.cms.bean.extend;

import com.it.cms.bean.Comment;
import com.it.cms.bean.User;
import lombok.Data;

import java.util.List;

@Data
public class CommentExtend extends Comment {
    private User author;
    private List<SubCommentExtend> childComments;
}
