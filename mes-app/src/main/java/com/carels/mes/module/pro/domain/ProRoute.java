package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 工艺路线实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProRoute extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 工艺路线ID */
    private Long routeId;

    /** 工艺路线编码 */
    private String routeCode;

    /** 工艺路线名称 */
    private String routeName;

    /** 适用产品ID */
    private Long itemId;

    /** 适用产品编码 */
    private String itemCode;

    /** 适用产品名称 */
    private String itemName;

    /** 版本号 */
    private String version;

    /** 状态: 0-启用, 1-停用 */
    private String status;

    /** 是否默认: Y-是, N-否 */
    private String isDefault;

    /** 备注 */
    private String remark;

    /** 工艺路线工序列表 */
    private List<ProRouteProcess> routeProcessList;
}
