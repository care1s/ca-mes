package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 检验记录实体类 - carels
 * 支持IQC(来料检验)/IPQC(过程检验)/OQC(出货检验)
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 检验记录ID */
    private Long recordId;

    /** 检验单号 */
    private String recordCode;

    /** 检验类型: IQC-来料检验, IPQC-过程检验, OQC-出货检验 */
    private String qcType;

    /** 工单ID */
    private Long workorderId;

    /** 工单编码 */
    private String workorderCode;

    /** 任务ID */
    private Long taskId;

    /** 物料ID */
    private Long itemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String specification;

    /** 供应商ID(IQC) */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 报检数量 */
    private Double inspectQuantity;

    /** 检验数量 */
    private Double checkedQuantity;

    /** 合格数量 */
    private Double qualifiedQuantity;

    /** 不合格数量 */
    private Double defectiveQuantity;

    /** 检验结果: PASS-合格, FAIL-不合格, PENDING-待检 */
    private String result;

    /** 检验状态: PENDING-待检验, PROCESSING-检验中, COMPLETED-已完成 */
    private String status;

    /** 检验标准ID */
    private Long standardId;

    /** 不良项描述 */
    private String defectDesc;

    /** 不良原因 */
    private String defectReason;

    /** 处理措施 */
    private String handleMethod;

    /** 检验时间 */
    private Date inspectTime;

    /** 检验员ID */
    private Long inspectorId;

    /** 检验员名称 */
    private String inspectorName;

    /** 备注 */
    private String remark;
}
