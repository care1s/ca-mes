package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 缺陷管理实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcDefect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 缺陷ID */
    private Long defectId;

    /** 缺陷编码 */
    private String defectCode;

    /** 缺陷名称 */
    private String defectName;

    /** 缺陷类型: APPEARANCE-外观, SIZE-尺寸, FUNCTION-功能, MATERIAL-材料 */
    private String defectType;

    /** 缺陷等级: CRITICAL-致命, MAJOR-严重, MINOR-轻微 */
    private String defectLevel;

    /** 所属工序 */
    private String processName;

    /** 状态: 0-正常, 1-停用 */
    private Integer status;

    /** 备注 */
    private String remark;
}
