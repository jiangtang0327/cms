package com.it;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.cms.CmsApplication;
import com.it.cms.bean.Category;
import com.it.cms.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = CmsApplication.class)
class CmsApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void contextLoads() {
        List<Category> categories = categoryMapper.selectList(null);
        String filename = "C:\\Users\\dgvt\\Desktop\\新建文件夹\\123.xlsx";
        EasyExcel.write(filename,Category.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("数据列表").doWrite(categories);
    }
}
