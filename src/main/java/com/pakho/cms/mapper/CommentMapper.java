package com.pakho.cms.mapper;

import com.pakho.cms.bean.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
* @author dgvt
* @description 针对表【cms_comment】的数据库操作Mapper
* @createDate 2023-11-14 10:28:41
* @Entity com.pakho.cms.bean.Comment
*/
@Component
public interface CommentMapper extends BaseMapper<Comment> {

}




