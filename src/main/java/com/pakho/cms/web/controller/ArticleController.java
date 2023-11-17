package com.pakho.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pakho.cms.bean.Article;
import com.pakho.cms.bean.extend.ArticleExtend;
import com.pakho.cms.bean.extend.ArticleParam;
import com.pakho.cms.service.ArticleService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "文章管理模块")
@RequestMapping("/auth/article")
@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "文章修改或新增", notes = "id存在为修改，不存在为新增")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Article article) {
        articleService.saveOrUpdate(article);
        return Result.success();
    }

    @ApiOperation(value = "审核文章", notes = "文章id必须有效,status: 审核通过、审核未通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "审核状态", required = true, dataType = "String")
    })
    @PostMapping("/reviewArticle")
    public Result reviewArticle(Long id, String status){
        articleService.reviewArticle(id, status);
        return Result.success("审核完成");
    }

    @ApiOperation(value = "删除根据ids文章", notes = "传递的多个用户id，至少有1个存在且有效")
    @DeleteMapping ("/deleteByBatch/{ids}")
    public Result deleteInBatch(@PathVariable List<Long> ids){
        articleService.deleteInBatch(ids);
        return Result.success();
    }

    @ApiOperation(value = "根据id查询文章", notes = "文章要包含3条一级评论")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable Long id){
        ArticleExtend articleExtend = articleService.queryById(id);
        return Result.success(articleExtend);
    }

    @ApiOperation(value = "分页+条件查询文章", notes = "")
    @PostMapping("/query")
    public Result queryById(@RequestBody ArticleParam articleParam){
        IPage<ArticleExtend> page = articleService.query(articleParam);
        return Result.success(page);
    }
}
