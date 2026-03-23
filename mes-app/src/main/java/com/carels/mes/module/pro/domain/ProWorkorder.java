package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 生产工单实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProWorkorder extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 工单ID */
    private Long workorderId;

    /** 工单编码 */
    private String workorderCode;

    /** 生产计划ID */
    private Long planId;

    /** 生产计划单号 */
    private String planNo;

    /** 工单类型: STANDARD-标准工单, Rework-返工工单 */
    private String workorderType;
    
    /** 产品ID */
    private Long itemId;
    
    /** 产品编码 */
    private String itemCode;
    
    /** 产品名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** BOM版本ID */
    private Long bomId;
    
    /** 工艺路线ID */
    private Long routeId;
    
    /** 工艺路线名称 */
    private String routeName;
    
    /** 计划数量 */
    private Double planQuantity;
    
    /** 完工数量 */
    private Double completedQuantity;
    
    /** 合格数量 */
    private Double qualifiedQuantity;
    
    /** 不良数量 */
    private Double defectiveQuantity;
    
    /** 报废数量 */
    private Double scrapQuantity;
    
    /** 工单状态: PENDING-待下达, RELEASED-已下达, PRODUCING-生产中, COMPLETED-已完成, CLOSED-已关闭 */
    private String status;
    
    /** 优先级: HIGH-高, NORMAL-正常, LOW-低 */
    private String priority;
    
    /** 计划开始时间 */
    private Date planStartTime;
    
    /** 计划完成时间 */
    private Date planEndTime;
    
    /** 实际开始时间 */
    private Date actualStartTime;
    
    /** 实际完成时间 */
    private Date actualEndTime;
    
    /** 生产车间ID */
    private Long workshopId;
    
    /** 生产车间名称 */
    private String workshopName;
    
    /** 生产班组ID */
    private Long teamId;
    
    /** 生产班组名称 */
    private String teamName;
    
    /** 预计工时(小时) */
    private Double estimatedHours;
    
    /** 实际工时(小时) */
    private Double actualHours;
    
    /** 客户ID */
    private Long clientId;
    
    /** 客户名称 */
    private String clientName;
    
    /** 销售订单号 */
    private String salesOrderNo;
    
    /** 备注 */
    private String remark;
}
