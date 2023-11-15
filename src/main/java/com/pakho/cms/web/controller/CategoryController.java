package com.pakho.cms.web.controller;

import com.pakho.cms.bean.Category;
import com.pakho.cms.service.CategoryService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("根据id查询栏目信息")
    @GetMapping("/getCategoryById/{id}")
    public Result getCategoryById(@PathVariable("id") Integer id) {
        return Result.success(categoryService.getById(id));
    }

    @ApiOperation(value = "更新栏目", notes = "栏目名必须唯一，栏目级别不能改动")
    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

}

