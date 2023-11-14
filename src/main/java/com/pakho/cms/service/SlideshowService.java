package com.pakho.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pakho.cms.bean.Slideshow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pakho.cms.mapper.SlideshowMapper;
import org.springframework.beans.factory.annotation.Autowired;

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
