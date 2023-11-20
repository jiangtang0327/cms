package com.it.cms.web.controller;

import com.it.cms.bean.User;
import com.it.cms.bean.vo.LoginParam;
import com.it.cms.service.UserService;
import com.it.cms.util.JwtUtil;
import com.it.cms.util.MD5Utils;
import com.it.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "登录模块")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "需要提供用户名和密码")
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginParam param) {
        //对密码加密与数据库进行比对
        System.out.println("param = " + param);
        User user = userService.login(param.getUsername(), MD5Utils.KL(param.getPassword()));
        //新增token 相关代码
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("username", user.getUsername());
        // 放入isVip不合适，后期充值成功后，用户不会重新登录，token值不变，导致isVip值是错误的
        map.put("isVip", user.getIsVip());
        map.put("roleId", user.getRoleId());
        String token = JwtUtil.generateJwt(map);
        return Result.success(token);
    }

    @ApiOperation(value = "登录", notes = "需要提供用户名和密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "form", dataType = "String", required = true, defaultValue = "jiangtang"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "String", required = true, defaultValue = "123123")
    }) //Post请求 + form提交 + consumes设置表单字符串
    @PostMapping(value = "/login_old", consumes = "application/x-www-form-urlencoded")
    public Result login(String username, @RequestParam("password") String password) {
        User user = userService.login(username, MD5Utils.KL(password));
        return Result.success(user);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

}
