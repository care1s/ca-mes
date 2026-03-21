package com.carels.mes.module.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 部门实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
public class SysDept {

    /** 部门ID */
    private Long deptId;

    /** 父部门ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门编码 */
    private String code;

    /** 部门名称 */
    private String name;

    /** 显示顺序 */
    private Integer orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 状态: 0-正常, 1-停用 */
    private String status;

    /** 删除标志: 0-存在, 2-删除 */
    private String delFlag;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;
}
