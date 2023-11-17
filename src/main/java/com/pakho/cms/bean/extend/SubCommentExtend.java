package com.pakho.cms.bean.extend;

import com.pakho.cms.bean.Subcomment;
import com.pakho.cms.bean.User;
import lombok.Data;

@Data
public class SubCommentExtend extends Subcomment {
    private User author;
}
