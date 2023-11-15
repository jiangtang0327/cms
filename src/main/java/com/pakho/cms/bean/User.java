package com.pakho.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName cms_user
 */
@TableName(value ="cms_user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 是否为会员
     */
    private Integer isVip;

    /**
     * 会员到期时间
     */
    private Date expiresTime;

    /**
     * 用户删除状态
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}