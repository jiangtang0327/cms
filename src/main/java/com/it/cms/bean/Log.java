package com.it.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName cms_log
 */
@TableName(value ="cms_log")
@Data
public class Log implements Serializable {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 访问用户账号
     */
    private String username;

    /**
     * 接口描述信息
     */
    private String businessName;

    /**
     * 请求的地址
     */
    private String requestUrl;

    /**
     * 请求的方式，get post delete put
     */
    private String requestMethod;

    /**
     * ip
     */
    private String ip;

    /**
     * ip来源
     */
    private String source;

    /**
     * 请求接口耗时
     */
    private Long spendTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 请求的参数
     */
    private String paramsJson;

    /**
     * 响应参数
     */
    private String resultJson;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}