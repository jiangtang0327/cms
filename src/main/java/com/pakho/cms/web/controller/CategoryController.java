package com.pakho.cms.web.controller;

import com.pakho.cms.bean.Category;
import com.pakho.cms.service.CategoryService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "栏目模块")
@RestController
@RequestMapping("/auth/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "新增栏目", notes = "栏目名必须唯一，如果为二级栏目则其父栏目id必须有效")
    @PostMapping("/save")
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }
}
