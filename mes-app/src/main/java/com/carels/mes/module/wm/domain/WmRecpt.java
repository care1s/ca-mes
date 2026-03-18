package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class WmRecpt extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String recptNo;
    private String recptType;
    private String sourceType;
    private Long sourceId;
    private String sourceNo;
    private Long warehouseId;
    private String warehouseName;
    private String status;
    private Date recptDate;
    private String remark;
    private List<WmRecptItem> items;
}
