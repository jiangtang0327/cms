package com.it.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.aop.Logging;
import com.it.cms.bean.extend.CommentExtend;
import com.it.cms.bean.extend.SubCommentExtend;
import com.it.cms.bean.vo.CommentDeleteParam;
import com.it.cms.bean.Comment;
import com.it.cms.bean.Subcomment;
import com.it.cms.bean.vo.CommentQueryParam;
import com.it.cms.service.CommentService;
import com.it.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "评论模块")
@RestController
@RequestMapping("/auth/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "新增一级评论", notes = "一级评论直接对文章进行评论")
    @Logging(value = "新增一级评论")
    @PostMapping("/saveComment")
    public Result saveComment(@RequestBody Comment comment) {
        commentService.saveComment(comment);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "新增二级评论", notes = "二级评论是对评论的回复")
    @Logging(value = "新增二级评论")
    @PostMapping("/saveSubComment")
    public Result saveSubComment(@RequestBody Subcomment subcomment) {
        commentService.saveSubComment(subcomment);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "根据id删除评论", notes = "type为1表示1级评论，为2表示2级评论")
    @Logging(value = "根据id删除评论")
    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestBody CommentDeleteParam commentDeleteParam) {
        commentService.deleteById(commentDeleteParam);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "根据id批量删除评论", notes = "type为1表示1级评论，为2表示2级评论")
    @Logging(value = "根据id批量删除评论")
    @DeleteMapping("/deleteByIdAll")
    public Result deleteByIdAll(@RequestBody List<CommentDeleteParam> list) {
        commentService.deleteInBatch(list);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "查询指定1级评论下的所有2级评论", notes = "2级评论含作者")
    @GetMapping("/queryById/{id}/child_comments")
    public Result queryByCommentId(@PathVariable Long id) {
        List<SubCommentExtend> list = commentService.queryByCommentId(id);
        return Result.success(list);
    }

    @ApiOperation(value = "分页查询指定文章下的所有1级评论", notes = "1级评论包含发表人及2条二级评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", required = true, paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, paramType = "query", defaultValue = "4"),
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "long", required = true, paramType = "path")
    })
    @GetMapping("/queryByArticleId/{id}")
    public Result queryByArticleId(Integer pageNum, Integer pageSize, @PathVariable Long id){
        IPage<CommentExtend> page = commentService.queryByArticleId(pageNum, pageSize, id);
        return Result.success(page);
    }

    @ApiOperation(value = "分页+条件查询", notes = "查询条件：关键字、userId、articleId、发表时间范围")
    @PostMapping("/query")
    public Result query(@RequestBody CommentQueryParam commentQueryParam) {
        IPage<CommentExtend> page = commentService.query(commentQueryParam);
        return Result.success(page);
    }


}
