package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 来料检验单实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcIqc extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 检验单ID */
    private Long iqcId;
    
    /** 检验单号 */
    private String iqcCode;
    
    /** 入库单ID */
    private Long recptId;
    
    /** 入库单号 */
    private String recptCode;
    
    /** 供应商ID */
    private Long vendorId;
    
    /** 供应商名称 */
    private String vendorName;
    
    /** 物料ID */
    private Long itemId;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 检验模板ID */
    private Long templateId;
    
    /** 批次号 */
    private String batchCode;
    
    /** 送检数量 */
    private Double inspectQuantity;
    
    /** 抽样数量 */
    private Double sampleQuantity;
    
    /** 合格数量 */
    private Double qualifiedQuantity;
    
    /** 不合格数量 */
    private Double unqualifiedQuantity;
    
    /** 检验结果: PASSED-合格, FAILED-不合格, CONCESSION-特采 */
    private String inspectResult;
    
    /** 检验状态: PENDING-待检验, INSPECTING-检验中, COMPLETED-已完成 */
    private String status;
    
    /** 检验员ID */
    private Long inspectorId;
    
    /** 检验员名称 */
    private String inspectorName;
    
    /** 检验日期 */
    private Date inspectDate;
    
    /** 备注 */
    private String remark;
    
    /** 检验明细列表 */
    private List<QcIqcLine> lines;
}
