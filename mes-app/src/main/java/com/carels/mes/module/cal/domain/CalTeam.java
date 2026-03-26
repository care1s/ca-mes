package com.carels.mes.module.cal.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 班组实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CalTeam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 班组ID */
    private Long teamId;

    /** 班组编码 */
    private String teamCode;

    /** 班组名称 */
    private String teamName;

    /** 所属车间ID */
    private Long workshopId;

    /** 所属车间名称 */
    private String workshopName;

    /** 班组长ID */
    private Long leaderId;

    /** 班组长名称 */
    private String leaderName;

    /** 状态: 0-启用, 1-停用 */
    private String status;

    /** 搜索关键字（不入库，仅用于查询） */
    private String keyword;
}
