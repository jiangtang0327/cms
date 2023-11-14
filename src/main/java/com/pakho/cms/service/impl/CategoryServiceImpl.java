package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Category;
import com.pakho.cms.service.CategoryService;
import com.pakho.cms.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author dgvt
* @description 针对表【cms_category】的数据库操作Service实现
* @createDate 2023-11-14 10:28:41
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




