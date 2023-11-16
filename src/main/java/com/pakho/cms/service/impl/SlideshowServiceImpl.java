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

import java.util.Date;
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
//        System.out.println("desc = " + desc);
        IPage<Slideshow> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.hasText(status), Slideshow::getStatus, status)
                .like(StringUtils.hasText(desc), Slideshow::getDescription, desc);
        IPage<Slideshow> slideshowIPage = slideshowMapper.selectPage(p, qw);
        return slideshowIPage;
    }

    @Override
    public boolean saveOrUpdate(Slideshow slideshow) {
        if (slideshow.getUrl()==null){
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Integer id = slideshow.getId();
        //1.判断轮播图url是否唯一
        String url = slideshow.getUrl();
        boolean flag = false;
        if (url != null) {
//判断是否是原来的轮播图url
            if (id != null) {
                Slideshow oldSlideshow = slideshowMapper.selectById(id);
                if (oldSlideshow != null && url.equals(oldSlideshow.getUrl())) {
                    flag = true;
                }
            }
            //判断url是否唯一
            if (!flag) {
                LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
                qw.eq(Slideshow::getUrl, url);
                Slideshow s = slideshowMapper.selectOne(qw);
                if (s != null)
                    throw new ServiceException(ResultCode.SLIDESHOW_URL_EXISTED);
                // 重置图片url更新时间
                slideshow.setUploadTime(new Date());
            }
        }
        if (id == null) {
            //2.新增操作
            if (slideshow.getStatus() == null)
                slideshow.setStatus("启用");
            int insert = slideshowMapper.insert(slideshow);
            if (insert > 0)
                return true;
        } else {
            //3.更新操作
            //3.1 判断当前轮播图是否有效
            Slideshow s = slideshowMapper.selectById(id);
            if (s == null)
                throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);
            //3.2 更新操作
            int i = slideshowMapper.updateById(slideshow);
            if (i > 0)
                return true;
        }
        return false;
    }


}




