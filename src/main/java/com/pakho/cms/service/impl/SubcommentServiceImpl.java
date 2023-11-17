package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Subcomment;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.service.SubcommentService;
import com.pakho.cms.mapper.SubcommentMapper;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_subcomment】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class SubcommentServiceImpl extends ServiceImpl<SubcommentMapper, Subcomment> implements SubcommentService{

}




