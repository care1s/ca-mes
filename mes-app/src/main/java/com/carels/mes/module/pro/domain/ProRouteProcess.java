package com.carels.mes.module.pro.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工艺路线工序实体类 - carels
 * 记录工艺路线包含的工序及顺序
 */
@Data
public class ProRouteProcess {

    /** ID */
    private Long id;

    /** 工艺路线ID */
    private Long routeId;

    /** 工序ID */
    private Long processId;

    /** 工序编码 */
    private String processCode;

    /** 工序名称 */
    private String processName;

    /** 工序序号 */
    private Integer sequenceNo;

    /** 默认工作站ID */
    private Long workstationId;

    /** 默认工作站名称 */
    private String workstationName;

    /** 标准工时(小时) */
    private BigDecimal standardHours;

    /** 是否检验: Y-是, N-否 */
    private String inspectFlag;

    /** 检验类型: FIRST-首检, PATROL-巡检, FINAL-完工检 */
    private String inspectType;

    /** 备注 */
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;
}
