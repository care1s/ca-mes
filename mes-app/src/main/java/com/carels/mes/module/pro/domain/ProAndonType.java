package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安东异常类型实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProAndonType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 类型ID */
    private Long typeId;

    /** 类型编码 */
    private String typeCode;

    /** 类型名称 */
    private String typeName;

    /** 类型级别: 1-低级, 2-中级, 3-高级 */
    private String typeLevel;

    /** 默认处理人ID */
    private Long defaultHandlerId;

    /** 默认处理人名称 */
    private String defaultHandlerName;

    /** 超时时间(分钟) */
    private Integer timeoutMinutes;

    /** 升级级别: 1-班组长, 2-车间主任, 3-生产经理 */
    private Integer escalateLevel;

    /** 颜色标识 */
    private String colorCode;

    /** 状态: 0-启用, 1-禁用 */
    private String status;

    /** 排序号 */
    private Integer sortNo;

    /** 备注 */
    private String remark;
}
