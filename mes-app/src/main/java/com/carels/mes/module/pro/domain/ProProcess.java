package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 工序实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProProcess extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 工序ID */
    private Long processId;

    /** 工序编码 */
    private String processCode;

    /** 工序名称 */
    private String processName;

    /** 所属车间ID */
    private Long workshopId;

    /** 所属车间名称 */
    private String workshopName;

    /** 工序类型: NORMAL-普通, INSPECT-检验, PACKAGE-包装 */
    private String processType;

    /** 标准工时(小时) */
    private BigDecimal standardHours;

    /** 状态: 0-启用, 1-停用 */
    private String status;

    /** 备注 */
    private String remark;
}
