package com.it.cms.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.aop.Logging;
import com.it.cms.bean.extend.CategoryExtend;
import com.it.cms.bean.Category;
import com.it.cms.service.CategoryService;
import com.it.cms.util.Result;
import com.it.cms.util.excel.CategoryListener;
import com.it.cms.util.excel.CategoryParentIdConverter;
import com.it.cms.util.excel.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "栏目模块")
@RestController
@RequestMapping("/auth/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExcelUtils excelUtils;


    @ApiOperation(value = "新增栏目", notes = "栏目名必须唯一，如果为二级栏目则其父栏目id必须有效")
    @Logging(value = "新增栏目")
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
    @Logging(value = "更新栏目")
    @PostMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @ApiOperation(value = "根据id删除栏目", notes = "id必须存在且有效")
    @Logging(value = "根据id删除栏目")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "批量删除栏目", notes = "需要提供一个或多个id值")
    @Logging(value = "批量删除栏目")
    @DeleteMapping("/deleteByIdAll/{ids}")
    public Result deleteCategoryInBatch(@PathVariable List<Integer> ids) {
        categoryService.deleteInBatch(ids);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "分页查询所有栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, defaultValue = "4", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父栏目id", dataType = "int", paramType = "query")
    })
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize, Integer parentId) {
        IPage<Category> query = categoryService.query(pageNum, pageSize, parentId);
        return Result.success(query);
    }

    @ApiOperation(value = "查询所有1级栏目(含2级)", notes = "不需要分页")
    @GetMapping("/queryAllParent")
    public Result queryAllParent() {
        List<CategoryExtend> categoryExtends = categoryService.queryAllParent();
        return Result.success(categoryExtends);
    }

    @ApiOperation("获取所有父栏目")
    @GetMapping("/queryAllOneLevel")
    public Result queryAllParentWithoutTwo() {
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.isNull(Category::getParentId);
        List<Category> list = categoryService.list(qw);
        return Result.success(list);
    }

    @ApiOperation("导入栏目数据")
    @Logging(value = "导入栏目数据")
    @PostMapping("/import")
    public Result imports(@RequestPart MultipartFile file) {
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.isNull(Category::getParentId);

        CategoryParentIdConverter.list = categoryService.list(qw);

        List<Category> list = excelUtils.importData(file, Category.class, new CategoryListener());

        categoryService.saveBatch(list);

        return Result.success("数据导入成功");
    }

    @ApiOperation("导出栏目数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exports(HttpServletResponse response) {
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.isNull(Category::getParentId);
        CategoryParentIdConverter.list = categoryService.list(qw);
        List<Category> list = categoryService.list();
        excelUtils.exportExcel(response, list, Category.class, "栏目信息表");
    }

}

