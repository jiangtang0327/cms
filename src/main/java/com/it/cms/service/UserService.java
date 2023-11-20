package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cms.bean.extend.UserExtend;
import com.it.cms.bean.User;

/**
 * @author dgvt
 * @description 针对表【cms_user】的数据库操作Service
 * @createDate 2023-11-14 10:28:41
 */
public interface UserService extends IService<User> {
    User login(String username, String password);
    void setVip(Long id);
    IPage<UserExtend> query(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip);
}
