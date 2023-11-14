package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.User;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.UserMapper;
import com.pakho.cms.service.UserService;
import com.pakho.cms.util.MD5Utils;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public boolean save(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //null
        if (username == null || password == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        username = username.trim();
        password = password.trim();
        //空字符串
        if ("".equals(username) || "".equals(password))
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        user.setUsername(username);
        user.setPassword(MD5Utils.KL(password));
        user.setRegisterTime(new Date());

        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user1 != null)
            throw new ServiceException(ResultCode.USER_HAS_EXISTED);
        else
            return userMapper.insert(user) > 0;
    }

    @Override
    public void setVip(Long id) {
        User user = userMapper.selectById(id);
        if (user == null)
            throw new ServiceException(ResultCode.DATA_NONE);
        if (user.getIsVip() == 1)
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        User u = new User();
        u.setId(id);
        u.setIsVip(1);
        long milliseconds = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L; // 一个月的毫秒数
        Date date = new Date(milliseconds);
        u.setExpiresTime(date);
        userMapper.updateById(u);
    }

    @Override
    public boolean updateById(User user) {
        //1.id判断（非空且有效存在）
        Long id = user.getId();
        User dbUser = userMapper.selectById(id);
        if (id == null || dbUser == null)
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);

        String newName = user.getUsername();
        String oldName = dbUser.getUsername();

        if (newName != null && !newName.equals(oldName)) {
            String trimName = newName.trim();
            if ("".equals(trimName))
                throw new ServiceException(ResultCode.PARAM_IS_INVALID);
            user.setUsername(trimName);

            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.eq(User::getUsername, trimName);
            if (userMapper.selectOne(qw) != null)
                throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);
            LambdaUpdateWrapper<User> uq = new LambdaUpdateWrapper<>();
            uq.set(User::getUsername, newName).eq(User::getId, id);
            int i = userMapper.update(dbUser, uq);
            if (i > 0)
                return true;
        }
        return false;
    }
}




