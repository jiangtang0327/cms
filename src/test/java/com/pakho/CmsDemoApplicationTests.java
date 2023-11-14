package com.pakho;

import com.pakho.cms.CmsDemoApplication;
import com.pakho.cms.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CmsDemoApplication.class)
class CmsDemoApplicationTests {

    @Test
    void contextLoads() {
        String kl = MD5Utils.KL("123123");
        System.out.println("kl = " + kl);
    }

}
