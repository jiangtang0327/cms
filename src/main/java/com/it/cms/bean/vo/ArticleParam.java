package com.it.cms.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.it.cms.bean.Article;
import lombok.Data;
import java.util.Date;

@Data
public class ArticleParam extends Article {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
