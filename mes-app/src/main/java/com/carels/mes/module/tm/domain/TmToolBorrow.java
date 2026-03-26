package com.carels.mes.module.tm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 工装机具领用归还实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TmToolBorrow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 领用ID */
    private Long borrowId;

    /** 领用单号 */
    private String borrowNo;

    /** 工装机具ID */
    private Long toolId;

    /** 工装机具编码 */
    private String toolCode;

    /** 工装机具名称 */
    private String toolName;

    /** 领用人工号 */
    private String borrowerCode;

    /** 领用人姓名 */
    private String borrowerName;

    /** 领用日期 */
    private Date borrowDate;

    /** 预计归还日期 */
    private Date planReturnDate;

    /** 实际归还日期 */
    private Date actualReturnDate;

    /** 使用次数/时长 */
    private Integer usedCount;

    /** 状态: 1-已领用, 2-已归还 */
    private String status;

    /** 领用事由 */
    private String borrowReason;

    /** 备注 */
    private String remark;
}
