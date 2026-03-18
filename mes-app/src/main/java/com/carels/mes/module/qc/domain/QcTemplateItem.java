package com.carels.mes.module.qc.domain;

import lombok.Data;

/**
 * 检验模板项目实体类
 */
@Data
public class QcTemplateItem {

    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long itemId;

    /** 模板ID */
    private Long templateId;

    /** 检验项目 */
    private String inspectItem;

    /** 检验标准 */
    private String standardValue;

    /** 检验方法 */
    private String inspectMethod;

    /** 排序号 */
    private Integer orderNum;

    /** 备注 */
    private String remark;
}
