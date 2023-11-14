package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Log;
import com.pakho.cms.service.LogService;
import com.pakho.cms.mapper.LogMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_log】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements LogService{

}




