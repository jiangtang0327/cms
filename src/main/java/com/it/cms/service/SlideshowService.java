package com.it.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it.cms.bean.Slideshow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 87764
* @description 针对表【cms_slideshow】的数据库操作Service
* @createDate 2023-11-14 09:52:30
*/
public interface SlideshowService extends IService<Slideshow> {
    List<Slideshow> queryAllEnable();
    IPage<Slideshow> queryAll(Integer page, Integer pageSize, String status, String desc);

}
