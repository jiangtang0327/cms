package com.it.cms.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryExport implements Serializable {
    @ExcelProperty("栏目名称")
    private String name;
    @ExcelProperty("栏目描述")
    private String description;
    @ExcelProperty(value = "栏目序号")
    private Integer orderNum;
    @ExcelProperty(value = "栏目删除状态")
    private String deleted;
    @ExcelProperty(value = "父栏目")
    private String parent;
}
