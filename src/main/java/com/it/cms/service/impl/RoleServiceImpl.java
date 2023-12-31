package com.it.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.cms.bean.Role;
import com.it.cms.service.RoleService;
import com.it.cms.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_role】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




