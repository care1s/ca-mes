package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购申请实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurRequest extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long requestId;

    /** 申请单号 */
    private String requestCode;

    /** 申请日期 */
    private Date requestDate;

    /** 申请类型: NORMAL-普通, URGENT-紧急 */
    private String requestType;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人 */
    private String applicantName;

    /** 申请部门ID */
    private Long deptId;

    /** 申请部门 */
    private String deptName;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 币种 */
    private String currency;

    /** 状态: DRAFT-草稿, PENDING-待审批, APPROVED-已审批, REJECTED-已拒绝 */
    private String status;

    /** 备注 */
    private String remark;

    /** 申请明细列表 */
    private List<PurRequestItem> items;
}
