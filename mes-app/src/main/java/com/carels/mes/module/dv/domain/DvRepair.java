package com.carels.mes.module.dv.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DvRepair extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String repairNo;
    private Long machineryId;
    private String machineryCode;
    private String machineryName;
    private String repairType;
    private String faultDescription;
    private String repairContent;
    private BigDecimal repairCost;
    private String status;
    private Long repairmanId;
    private String repairmanName;
    private Date repairDate;
    private String remark;
}
