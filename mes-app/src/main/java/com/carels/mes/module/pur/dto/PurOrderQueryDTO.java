package com.carels.mes.module.pur.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurOrderQueryDTO {
    private String orderNo;
    private String requestNo;
    private Long vendorId;
    private Integer orderStatus;
    private Integer auditStatus;
    private LocalDate orderDateStart;
    private LocalDate orderDateEnd;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
