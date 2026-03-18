package com.carels.mes.module.pur.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurReturnQueryDTO {
    private String returnNo;
    private String receiptNo;
    private Long vendorId;
    private Integer status;
    private Integer auditStatus;
    private Integer stockOutStatus;
    private LocalDate returnDateStart;
    private LocalDate returnDateEnd;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
