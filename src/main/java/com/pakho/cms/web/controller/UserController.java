package com.pakho.cms.web.controller;

import com.pakho.cms.bean.User;
import com.pakho.cms.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取全部用户")
    @GetMapping("")
    public List<User> findAll() {
        List<User> list = userService.list();
        return list;
    }

}
