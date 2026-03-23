package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class WmIssue extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String issueNo;
    private String issueType;
    private String targetType;
    private Long targetId;
    private String targetNo;
    private Long warehouseId;
    private String warehouseName;
    private String status;
    private Date issueDate;
    private String remark;

    // ==================== 销售出库特有字段 ====================
    /** 销售订单ID */
    private Long salesOrderId;

    /** 销售订单号 */
    private String salesOrderNo;

    /** 客户ID */
    private Long clientId;

    /** 客户名称 */
    private String clientName;
    // =========================================================

    private List<WmIssueItem> items;
}
