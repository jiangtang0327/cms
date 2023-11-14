package com.pakho.cms.web.controller;

import com.pakho.cms.bean.Slideshow;
import com.pakho.cms.service.SlideshowService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "轮播图管理")
@RequestMapping("/slideshow")
@RestController
public class SlideshowController {
    @Autowired
    private SlideshowService slideshowService;

    @ApiOperation(value = "查询所有轮播图")
    @GetMapping("/queryAllEnable")
    public Result queryAllEnable(){
        List<Slideshow> list = slideshowService.list();
        return Result.success(list);
    }
}
