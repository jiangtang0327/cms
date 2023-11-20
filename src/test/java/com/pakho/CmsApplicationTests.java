package com.pakho;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.pakho.cms.CmsApplication;
import com.pakho.cms.bean.Category;
import com.pakho.cms.bean.User;
import com.pakho.cms.mapper.CategoryMapper;
import com.pakho.cms.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

@SpringBootTest(classes = CmsApplication.class)
class CmsApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void contextLoads() {

    }
}
