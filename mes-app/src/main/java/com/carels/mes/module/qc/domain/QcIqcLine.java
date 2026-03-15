package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 来料检验单明细实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcIqcLine extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 明细ID */
    private Long lineId;
    
    /** 检验单ID */
    private Long iqcId;
    
    /** 行号 */
    private Integer lineNo;
    
    /** 检验项目ID */
    private Long indexId;
    
    /** 项目编码 */
    private String indexCode;
    
    /** 项目名称 */
    private String indexName;
    
    /** 标准值 */
    private String standardValue;
    
    /** 最大值 */
    private Double maxValue;
    
    /** 最小值 */
    private Double minValue;
    
    /** 检验值 */
    private String inspectValue;
    
    /** 检验结果: PASSED-合格, FAILED-不合格 */
    private String inspectResult;
    
    /** 备注 */
    private String remark;
}
