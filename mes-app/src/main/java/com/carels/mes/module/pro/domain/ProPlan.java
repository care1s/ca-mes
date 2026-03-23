package com.carels.mes.module.pro.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产计划实体类
 */
@Data
public class ProPlan {
    
    /** 计划ID */
    private Long planId;
    
    /** 计划单号 */
    private String planNo;
    
    /** 计划名称 */
    private String planName;
    
    /** 计划类型: 0-周计划, 1-月计划, 2-季度计划 */
    private Integer planType;
    
    /** 计划开始日期 */
    private LocalDate startDate;
    
    /** 计划结束日期 */
    private LocalDate endDate;
    
    /** 计划状态: 0-草稿, 1-已发布, 2-执行中, 3-已完成, 4-已取消 */
    private Integer status;

    /** 排单类型: 0-自身排单, 1-绑定销售订单 */
    private Integer scheduleType;

    /** 销售订单ID */
    private Long salesOrderId;

    /** 销售订单号 */
    private String salesOrderNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 产品ID（物料ID） */
    private Long itemId;

    /** 产品编码 */
    private String itemCode;

    /** 产品名称 */
    private String itemName;

    /** 规格型号 */
    private String specification;

    /** 计划产量 */
    private Double planQty;
    
    /** 实际产量 */
    private Double actualQty;
    
    /** 完成率 */
    private Double completionRate;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人姓名 */
    private String managerName;
    
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
