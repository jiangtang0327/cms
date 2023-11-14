package com.pakho.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName cms_article
 */
@TableName(value ="cms_article")
@Data
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章审核状态
     */
    private String status;

    /**
     * 阅读量
     */
    private Integer readNum;

    /**
     * 点赞量
     */
    private Integer likeNum;

    /**
     * 拉踩量
     */
    private Integer dislikeNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 类别id
     */
    private Integer categoryId;

    /**
     * 是否收费，默认0不收费
     */
    private Integer charged;

    /**
     * 文章删除状态
     */
    private Integer deleted;

    /**
     * 文章发表时间
     */
    private Date publishTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}