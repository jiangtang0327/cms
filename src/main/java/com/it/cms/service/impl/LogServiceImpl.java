package com.it.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.it.cms.bean.Log;
import com.it.cms.bean.extend.LogExportParam;
import com.it.cms.bean.vo.LogParam;
import com.it.cms.bean.vo.LogVO;
import com.it.cms.exception.ServiceException;
import com.it.cms.mapper.LogMapper;
import com.it.cms.service.LogService;
import com.it.cms.util.BeanCopyUtils;
import com.it.cms.util.Result;
import com.it.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author dgvt
 * @description 针对表【cms_log】的数据库操作Service实现
 * @createDate 2023-11-14 10:28:41
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private Gson gson;

    @Override
    public IPage<LogVO> query(LogParam logParam) {
        // 校验参数
        if (logParam == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        if (logParam.getPageNum() == null || logParam.getPageSize() == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 构造查询条件
        Page<Log> page = new Page<>(logParam.getPageNum(), logParam.getPageSize());
        LambdaQueryWrapper<Log> qw = new LambdaQueryWrapper<>();
        qw.gt(logParam.getStartTime() != null, Log::getCreateTime, logParam.getStartTime())
                .lt(logParam.getEndTime() != null, Log::getCreateTime, logParam.getEndTime())
                .like(StringUtils.hasText(logParam.getUsername()), Log::getUsername, logParam.getUsername())
                .like(StringUtils.hasText(logParam.getRequestUrl()), Log::getRequestUrl, logParam.getRequestUrl());

        // 执行查询
        logMapper.selectPage(page, qw);

        // 转换结果
        IPage<LogVO> logVOIPage = BeanCopyUtils.copyPage(page, LogVO.class);

        // 处理结果
        List<LogVO> list = logVOIPage.getRecords();
        list.forEach(logVO -> {
            Result result = gson.fromJson(logVO.getResultJson(), Result.class);
            logVO.setCode(result.getCode());
            logVO.setMsg(result.getMsg());
            logVO.setResultJson(null);
        });

        return logVOIPage;
    }

    @Override
    public List<LogVO> queryForExport(LogExportParam logExportParam) {
        // 校验参数
        if (logExportParam == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        if (logExportParam.getCount() == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 构造查询条件
        LambdaQueryWrapper<Log> qw = new LambdaQueryWrapper<>();
        qw.gt(logExportParam.getStartTime() != null, Log::getCreateTime, logExportParam.getStartTime())
                .lt(logExportParam.getEndTime() != null, Log::getCreateTime, logExportParam.getEndTime())
                .like(StringUtils.hasText(logExportParam.getUsername()), Log::getUsername, logExportParam.getUsername())
                .like(StringUtils.hasText(logExportParam.getRequestUrl()), Log::getRequestUrl, logExportParam.getRequestUrl())
                .last(Objects.nonNull(logExportParam.getCount()), "limit " + logExportParam.getCount());

        List<Log> logList = logMapper.selectList(qw);

        //Bean拷贝
        List<LogVO> logVOList = BeanCopyUtils.copyBeanList(logList, LogVO.class);

        logVOList.forEach(logVO -> {
            Result result = gson.fromJson(logVO.getResultJson(), Result.class);
            logVO.setCode(result.getCode());
            logVO.setMsg(result.getMsg());
            logVO.setResultJson(null);
        });

        return logVOList;
    }
}




