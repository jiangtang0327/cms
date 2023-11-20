package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.extend.ArticleExtend;
import com.it.cms.bean.vo.ArticleParam;
import com.it.cms.bean.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_article】的数据库操作Service
* @createDate 2023-11-14 10:28:41
*/
public interface ArticleService extends IService<Article> {
    void reviewArticle(Long id, String status);
    void deleteInBatch(List<Long> ids);
    ArticleExtend queryById(Long id);
    IPage<ArticleExtend> query(ArticleParam articleParam);
}
