package com.pakho.cms.bean.extend;

import com.pakho.cms.bean.Comment;
import com.pakho.cms.bean.User;
import lombok.Data;

import java.util.List;

@Data
public class CommentExtend extends Comment {
    private User author;
    private List<SubCommentExtend> childComments;
}
