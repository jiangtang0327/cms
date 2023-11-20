package com.it.cms.bean;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName cms_subcomment
 */
@TableName(value ="cms_subcomment")
@Data
public class Subcomment implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Date publishTime;

    /**
     * 
     */
    private Long userId;

    /**
     * 一级评论id
     */
    private Long parentId;

    /**
     * 回复评论id
     */
    private Long replyId;

    /**
     * 
     */
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}