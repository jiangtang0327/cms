package com.pakho.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.pakho.cms.bean.Slideshow;
import com.pakho.cms.service.SlideshowService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "轮播图管理")
@RequestMapping("/slideshow")
@RestController
public class SlideshowController {
    @Autowired
    private SlideshowService slideshowService;

    @ApiOperation(value = "查询所有可用的轮播图")
    @GetMapping("/queryAllEnable")
    public Result queryAllEnable() {
        List<Slideshow> list = slideshowService.queryAllEnable();
        return Result.success(list);
    }

    @ApiOperation(value = "根据条件查询轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, defaultValue = "4"),
            @ApiImplicitParam(name = "status", value = "状态值"),
            @ApiImplicitParam(name = "desc", value = "描述信息")
    })
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize, String status, String desc) {
        IPage<Slideshow> slideshowIPage = slideshowService.queryAll(pageNum, pageSize, status, desc);
        return Result.success(slideshowIPage);

    }

    @ApiOperation(value = "根据id查询轮播图信息",notes = "用于更新时的数据回显")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable Integer id) {
        return Result.success(slideshowService.getById(id));
    }

    @ApiOperation(value = "新增或更新轮播图", notes = "slideshow参数包含id值则为更新，不包含i为新增")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Slideshow slideshow) {
        slideshowService.saveOrUpdate(slideshow);
        return Result.success("操作成功");
    }

    @ApiOperation(value = "批量删除轮播图", notes = "需要提供多个id值")
    @DeleteMapping("/deleteByBatch/{ids}")
    public Result deleteSlideshowInBatch(@PathVariable List<Integer>ids){
        log.info("ids: {}",ids);
        boolean b = slideshowService.removeByIds(ids);
        return Result.success();
    }




}
