package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.User;
import com.pakho.cms.service.UserService;
import com.pakho.cms.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_user】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




