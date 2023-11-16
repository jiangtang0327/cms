package com.pakho.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pakho.cms.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pakho.cms.bean.extend.CategoryExtend;
import com.pakho.cms.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_category】的数据库操作Service
* @createDate 2023-11-14 10:28:41
*/
public interface CategoryService extends IService<Category> {
    void deleteInBatch(List<Integer> ids);
    IPage<Category> query(Integer pageNum, Integer pageSize, Integer parentId);
    List<CategoryExtend> queryAllParent();
}
