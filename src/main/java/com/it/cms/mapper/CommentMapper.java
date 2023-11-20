package com.it.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.cms.bean.Comment;
import org.springframework.stereotype.Component;

/**
 * @author dgvt
 * @description 针对表【cms_comment】的数据库操作Mapper
 * @createDate 2023-11-14 10:28:41
 * @Entity Comment
 */
@Component
public interface CommentMapper extends BaseMapper<Comment> {
}




