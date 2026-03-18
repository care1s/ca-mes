package com.carels.mes.module.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统用户实体类
 */
@Data
public class SysUser {
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 昵称 */
    private String nickname;
    
    /** 真实姓名 */
    private String realName;
    
    /** 性别: 0-男, 1-女 */
    private Integer gender;
    
    /** 手机号 */
    private String phone;
    
    /** 邮箱 */
    private String email;
    
    /** 头像 */
    private String avatar;
    
    /** 部门ID */
    private Long deptId;
    
    /** 部门名称 */
    private String deptName;
    
    /** 岗位 */
    private String post;
    
    /** 状态: 0-正常, 1-停用 */
    private Integer status;
    
    /** 用户类型: 0-普通用户, 1-管理员 */
    private Integer userType;
    
    /** 最后登录IP */
    private String loginIp;
    
    /** 最后登录时间 */
    private LocalDateTime loginTime;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
