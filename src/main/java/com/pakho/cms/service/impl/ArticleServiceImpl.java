package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Article;
import com.pakho.cms.service.ArticleService;
import com.pakho.cms.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_article】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




