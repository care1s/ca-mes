package com.carels.mes.module.qc.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 来料检验明细实体类
 */
@Data
public class QcIqcItem {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 检验ID */
    private Long inspectId;

    /** 检验项目 */
    private String inspectItem;

    /** 检验标准 */
    private String standardValue;

    /** 实测值 */
    private String actualValue;

    /** 检验结果: PASS-合格, FAIL-不合格 */
    private String result;

    /** 备注 */
    private String remark;
}
