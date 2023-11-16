package com.pakho.cms.web.controller;

import com.pakho.cms.bean.Article;
import com.pakho.cms.service.ArticleService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "文章管理模块")
@RequestMapping("/auth/article")
@RestController
public class ArticleController {

}
