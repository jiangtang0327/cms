package com.pakho.cms.web.controller;

import com.pakho.cms.bean.User;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.service.UserService;
import com.pakho.cms.util.Result;
import com.pakho.cms.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "用户模块")
@RestController
@RequestMapping("/auth/user")
public class UserController {
    @Autowired
    private UserService userService;

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

}

