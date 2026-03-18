package com.carels.mes.module.dv.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DvMachinery extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String machineryCode;
    private String machineryName;
    private String machineryType;
    private String brand;
    private String model;
    private String serialNumber;
    private Long workshopId;
    private String workshopName;
    private Long productionLineId;
    private String productionLineName;
    private Date purchaseDate;
    private BigDecimal purchasePrice;
    private String supplier;
    private String status;
    private String remark;
}
