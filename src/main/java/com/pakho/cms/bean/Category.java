package com.pakho.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName cms_category
 */
@TableName(value ="cms_category")
@Data
public class Category implements Serializable {
    /**
     * 栏目编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 栏目描述
     */
    private String description;

    /**
     * 栏目序号
     */
    private Integer orderNum;

    /**
     * 栏目删除状态
     */
    private Integer deleted;

    /**
     * 父栏目id
     */
    private Integer parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}