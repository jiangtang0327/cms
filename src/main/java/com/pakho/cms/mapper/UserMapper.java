package com.pakho.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pakho.cms.bean.User;
import com.pakho.cms.bean.extend.UserExtend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dgvt
 * @description 针对表【cms_user】的数据库操作Mapper
 * @createDate 2023-11-14 10:28:41
 * @Entity com.pakho.cms.bean.User
 */
@Component
public interface UserMapper extends BaseMapper<User> {
    IPage<UserExtend> queryAllUserWithRole(IPage<UserExtend> page,
                              @Param("username") String username,
                              @Param("status") String status,
                              @Param("roleId") Integer roleId,
                              @Param("isVip") Integer isVip);
}




