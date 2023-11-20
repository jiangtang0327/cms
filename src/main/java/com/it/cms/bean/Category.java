package com.it.cms.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.it.cms.util.excel.CategoryParentIdConverter;
import com.it.cms.util.excel.DeletedConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "name")//针对栏目名称重新Equals和HashCode方法
@TableName("cms_category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 栏目编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;
    /**
     * 栏目名称
     */
    @ExcelProperty("栏目名称")
    private String name;
    /**
     * 栏目描述
     */
    @ExcelProperty("栏目描述")
    private String description;
    /**
     * 栏目序号
     */
    @ExcelProperty(value = "栏目序号")
    private Integer orderNum;
    /**
     * 栏目删除状态
     */
    @TableLogic
    @ExcelProperty(value = "栏目删除状态", converter = DeletedConverter.class)
    private Integer deleted;
    /**
     * 父栏目id
     */
    @ExcelProperty(value = "父栏目", converter = CategoryParentIdConverter.class)
    private Integer parentId;

    public Category(String name) {
        this.name = name;
    }
}