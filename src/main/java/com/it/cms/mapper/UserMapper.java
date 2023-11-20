package com.it.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.extend.UserExtend;
import com.it.cms.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author dgvt
 * @description 针对表【cms_user】的数据库操作Mapper
 * @createDate 2023-11-14 10:28:41
 * @Entity User
 */
@Component
public interface UserMapper extends BaseMapper<User> {
    IPage<UserExtend> queryAllUserWithRole(IPage<UserExtend> page,
                                           @Param("username") String username,
                                           @Param("status") String status,
                                           @Param("roleId") Integer roleId,
                                           @Param("isVip") Integer isVip);
}




