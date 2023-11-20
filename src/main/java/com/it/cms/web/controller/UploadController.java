package com.it.cms.web.controller;

import com.it.cms.aop.Logging;
import com.it.cms.util.UploadUtils;
import com.it.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Api(tags = "文件上传接口")
public class UploadController {
    @Autowired
    private UploadUtils uploadUtils;

    @ApiOperation("文件上传")
    @Logging(value = "文件上传")
    @PostMapping("upload")
    @SneakyThrows //帮助处理 编译时异常
    public Result upload(@RequestPart MultipartFile img){
        return Result.success(uploadUtils.fileToOSS(img));
    }
}
