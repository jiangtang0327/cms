package com.it.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.aop.Logging;
import com.it.cms.bean.User;
import com.it.cms.bean.extend.UserExtend;
import com.it.cms.service.UserService;
import com.it.cms.exception.ServiceException;
import com.it.cms.util.Result;
import com.it.cms.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "用户模块")
@RestController
@RequestMapping("/auth/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping("/getAllUser")
    public Result getAllUser() {
        return Result.success(userService.list());
    }

    @ApiOperation("获取用户个人信息")
    @GetMapping("/info")
    public Result getInfo(@RequestAttribute("userId") Long id) {
        log.info("id:{}", id);
        if (id == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        //2.查找用户
        User user = userService.getById(id);
        if (user == null)
            throw new ServiceException(ResultCode.DATA_NONE);

        return Result.success(user);
    }

    @ApiOperation(value = "新增用户", notes = "username、password必须存在不为空，且username唯一")
    @Logging(value = "新增用户")
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        userService.save(user);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "根据id查找用户", notes = "id必须存在且有效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null)
            throw new ServiceException(ResultCode.DATA_NONE);
        return Result.success(user);
    }

    @ApiOperation(value = "设置用户为Vip", notes = "id存在且有效")
    @Logging(value = "设置用户为Vip")
    @PutMapping("/setVip/{id}")
    public Result setVip(@PathVariable Long id) {
        userService.setVip(id);
        return Result.success("更新成功");
    }


    @ApiOperation(value = "更新用户信息", notes = "id必须存在且有效,如果username存在则必须唯一")
    @Logging(value = "更新用户信息")
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("更新成功");
    }


    @ApiOperation(value = "删除用户", notes = "ids为id数组")
    @Logging(value = "删除用户")
    @DeleteMapping("/deleteByBatch/{ids}")
    public Result deleteByBatch(@PathVariable("ids") List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "分页+条件查询", notes = "包含角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", required = true, defaultValue = "5"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isVip", value = "是否为vip", dataType = "int", paramType = "query")
    })
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize,
                        String username, String status, Integer roleId, Integer isVip) {
        IPage<UserExtend> query = userService.query(pageNum, pageSize, username, status, roleId, isVip);
        return Result.success(query);
    }
}

