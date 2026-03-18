package com.carels.mes.module.pro.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 物料需求实体类
 */
@Data
public class ProMaterialReq {
    
    /** 需求ID */
    private Long reqId;
    
    /** 需求单号 */
    private String reqNo;
    
    /** 关联计划ID */
    private Long planId;
    
    /** 关联计划单号 */
    private String planNo;
    
    /** 需求日期 */
    private LocalDate reqDate;
    
    /** 需求类型: 0-计划需求, 1-紧急需求, 2-补料需求 */
    private Integer reqType;
    
    /** 状态: 0-草稿, 1-待审核, 2-已审核, 3-已发料, 4-已完成, 5-已取消 */
    private Integer status;
    
    /** 申请人ID */
    private Long applicantId;
    
    /** 申请人姓名 */
    private String applicantName;
    
    /** 申请部门 */
    private String deptName;
    
    /** 总物料种类数 */
    private Integer itemCount;
    
    /** 总需求数量 */
    private Double totalQty;
    
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
