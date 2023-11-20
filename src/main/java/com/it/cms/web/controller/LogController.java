package com.it.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.vo.LogVO;
import com.it.cms.service.LogService;
import com.it.cms.util.excel.ExcelUtils;
import com.it.cms.bean.extend.LogExportParam;
import com.it.cms.bean.vo.LogParam;
import com.it.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "日志模块")
@RestController
@RequestMapping("/auth/log")
public class LogController {
    @Autowired
    private LogService logService;
    @Autowired
    private ExcelUtils excelUtils;
    @ApiOperation(value = "分页+条件查询日志信息", notes = "用户名、时间范围可以为空")
    @PostMapping("/query")
    public Result query(@RequestBody LogParam logParam){
        IPage<LogVO> page = logService.query(logParam);
        return Result.success(page);
    }

    @ApiOperation("导出日志信息")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void export(HttpServletResponse response, LogExportParam logExportParam){
        //1.获取数据
        List<LogVO> list = logService.queryForExport(logExportParam);
        //2.导出数据
        excelUtils.exportExcel(response, list, LogVO.class, "日志信息表");
    }

}
