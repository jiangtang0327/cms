package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.extend.CategoryExtend;
import com.it.cms.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
    void exportExcel(HttpServletResponse response) throws IOException;
    void importExcel(InputStream inputStream);
}
