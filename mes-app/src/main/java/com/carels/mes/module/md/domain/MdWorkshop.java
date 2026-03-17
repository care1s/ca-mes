package com.carels.mes.module.md.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车间实体类 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MdWorkshop extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 车间ID */
    private Long workshopId;
    
    /** 车间编码 */
    private String workshopCode;
    
    /** 车间名称 */
    private String workshopName;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人名称 */
    private String managerName;
    
    /** 联系电话 */
    private String phone;
    
    /** 组织模式: SIMPLE-简单模式, COMPLETE-完整模式 */
    private String orgMode;
    
    /** 状态: 0-启用, 1-停用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
