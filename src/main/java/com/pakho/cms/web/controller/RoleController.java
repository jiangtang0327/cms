package com.pakho.cms.web.controller;

import com.pakho.cms.service.RoleService;
import com.pakho.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "角色模块")
@RequestMapping("/auth/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation("获取所有角色信息")
    @GetMapping("/getAll")
    public Result getAll() {
        return Result.success(roleService.list());
    }

}
