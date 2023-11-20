package com.it.cms.bean.extend;

import com.it.cms.bean.Subcomment;
import com.it.cms.bean.User;
import lombok.Data;

@Data
public class SubCommentExtend extends Subcomment {
    private User author;
}
