package com.carels.mes.module.md.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工作站实体类 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MdWorkstation extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 工作站ID */
    private Long workstationId;
    
    /** 工作站编码 */
    private String workstationCode;
    
    /** 工作站名称 */
    private String workstationName;
    
    /** 所属车间ID */
    private Long workshopId;
    
    /** 所属车间名称 */
    private String workshopName;
    
    /** 所属生产线ID（仅完整模式使用） */
    private Long productionLineId;
    
    /** 所属生产线名称 */
    private String productionLineName;
    
    /** 状态: 0-启用, 1-停用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
