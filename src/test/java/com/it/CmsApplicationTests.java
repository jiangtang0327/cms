package com.it;

import com.it.cms.CmsApplication;
import com.it.cms.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CmsApplication.class)
class CmsApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void contextLoads() {

    }
}
