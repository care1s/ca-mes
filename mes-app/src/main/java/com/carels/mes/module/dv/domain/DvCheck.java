package com.carels.mes.module.dv.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DvCheck extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String checkNo;
    private Long machineryId;
    private String machineryCode;
    private String machineryName;
    private String checkType;
    private String checkResult;
    private String status;
    private Long checkerId;
    private String checkerName;
    private Date checkDate;
    private String remark;
}
