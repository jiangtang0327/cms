package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Comment;
import com.pakho.cms.service.CommentService;
import com.pakho.cms.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_comment】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




