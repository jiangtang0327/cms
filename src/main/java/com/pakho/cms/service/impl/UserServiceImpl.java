package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.User;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.UserMapper;
import com.pakho.cms.service.UserService;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dgvt
 * @description 针对表【cms_user】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, username)
                .eq(User::getPassword, password);
        User user = userMapper.selectOne(qw);
        if (user == null)
            throw new
                    ServiceException(ResultCode.USER_LOGIN_ERROR);
        return user;
    }
}




