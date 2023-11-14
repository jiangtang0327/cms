package com.pakho.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pakho.cms.mapper")
public class CmsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsDemoApplication.class, args);
    }

}
