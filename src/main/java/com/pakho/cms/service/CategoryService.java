package com.pakho.cms.service;

import com.pakho.cms.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pakho.cms.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @author dgvt
* @description 针对表【cms_category】的数据库操作Service
* @createDate 2023-11-14 10:28:41
*/
public interface CategoryService extends IService<Category> {

}
