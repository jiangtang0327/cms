package com.pakho.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pakho.cms.bean.Slideshow;
import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.mapper.SlideshowMapper;
import com.pakho.cms.service.SlideshowService;
import com.pakho.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 87764
 * @description 针对表【cms_slideshow】的数据库操作Service实现
 * @createDate 2023-11-14 09:52:30
 */
@Service
public class SlideshowServiceImpl extends ServiceImpl<SlideshowMapper, Slideshow> implements SlideshowService {
    @Autowired
    private SlideshowMapper slideshowMapper;

    @Override
    public List<Slideshow> queryAllEnable() {
        LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
        qw.eq(Slideshow::getStatus, "启用");
        qw.orderByAsc(Slideshow::getUploadTime);
        List<Slideshow> slideshows = slideshowMapper.selectList(qw);
        if (slideshows == null || slideshows.size() == 0)
            throw new ServiceException(ResultCode.DATA_NONE);

        return slideshows;
    }

    @Override
    public IPage<Slideshow> queryAll(Integer page, Integer pageSize, String status, String desc) {
        IPage<Slideshow> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.hasText(status), Slideshow::getStatus, status)
                .like(StringUtils.hasText(desc), Slideshow::getDescription, desc);
        IPage<Slideshow> slideshowIPage = slideshowMapper.selectPage(p, qw);
        if (slideshowIPage.getTotal() == 0)
            throw new ServiceException(ResultCode.DATA_NONE);
        return slideshowIPage;
    }
}




