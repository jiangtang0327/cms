package com.it.cms.mapper;

import com.it.cms.bean.extend.CategoryExtend;
import com.it.cms.bean.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_category】的数据库操作Mapper
* @createDate 2023-11-14 10:28:41
* @Entity Category
*/
@Component
public interface CategoryMapper extends BaseMapper<Category> {
    List<CategoryExtend> queryAllWithCates();
}




