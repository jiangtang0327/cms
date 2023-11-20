package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cms.bean.extend.LogExportParam;
import com.it.cms.bean.vo.LogParam;
import com.it.cms.bean.vo.LogVO;

import java.util.List;

/**
* @author dgvt
* @description 针对表【cms_log】的数据库操作Service
* @createDate 2023-11-14 10:28:41
*/
public interface LogService extends IService<Log> {
    IPage<LogVO> query(LogParam logParam);
    List<LogVO> queryForExport(LogExportParam logExportParam);
}
