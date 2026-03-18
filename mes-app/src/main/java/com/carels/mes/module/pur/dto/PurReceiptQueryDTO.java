package com.carels.mes.module.pur.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurReceiptQueryDTO {
    private String receiptNo;
    private String orderNo;
    private Long vendorId;
    private Integer status;
    private Integer auditStatus;
    private Integer stockInStatus;
    private LocalDate receiptDateStart;
    private LocalDate receiptDateEnd;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
