package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WmIssueItem {
    private static final long serialVersionUID = 1L;
    private Long itemId;
    private Long issueId;
    private Long itemId2;
    private String itemCode;
    private String itemName;
    private String batchCode;
    private BigDecimal quantity;
    private String unit;
    private String remark;
}
