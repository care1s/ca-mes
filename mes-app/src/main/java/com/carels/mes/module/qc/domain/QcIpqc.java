package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 过程检验(IPQC)实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcIpqc extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 检验ID */
    private Long id;

    /** 检验单号 */
    private String inspectNo;

    /** 关联工单ID */
    private Long workorderId;

    /** 关联工单号 */
    private String workorderNo;

    /** 工序ID */
    private Long processId;

    /** 工序名称 */
    private String processName;

    /** 检验数量 */
    private BigDecimal inspectQty;

    /** 合格数量 */
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    private BigDecimal unqualifiedQty;

    /** 检验结果: PASS-合格, FAIL-不合格 */
    private String inspectResult;

    /** 检验状态: PENDING-待检验, INSPECTING-检验中, COMPLETED-已完成 */
    private String status;

    /** 检验类型: FIRST-首检, PATROL-巡检, FINAL-完工检 */
    private String inspectType;

    /** 检验员ID */
    private Long inspectorId;

    /** 检验员名称 */
    private String inspectorName;

    /** 检验日期 */
    private Date inspectDate;

    /** 备注 */
    private String remark;

    /** 检验明细列表 */
    private List<QcIpqcItem> items;
}
