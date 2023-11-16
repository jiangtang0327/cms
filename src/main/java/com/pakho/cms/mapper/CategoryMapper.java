package com.pakho.cms.mapper;

import com.pakho.cms.bean.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pakho.cms.bean.extend.CategoryExtend;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_category】的数据库操作Mapper
* @createDate 2023-11-14 10:28:41
* @Entity com.pakho.cms.bean.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {
    List<CategoryExtend> queryAllWithCates();
}




