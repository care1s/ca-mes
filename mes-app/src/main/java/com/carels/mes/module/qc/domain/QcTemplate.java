package com.carels.mes.module.qc.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 检验模板实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QcTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long templateId;

    /** 模板名称 */
    private String templateName;

    /** 模板类型: IQC-来料检验, IPQC-过程检验, OQC-成品检验 */
    private String templateType;

    /** 物料ID */
    private Long itemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 状态: 0-正常, 1-停用 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 检验项目列表 */
    private List<QcTemplateItem> items;
}
