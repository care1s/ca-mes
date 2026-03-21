package com.carels.mes.module.qc.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 待检任务Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface QcPendingInspectMapper {

    /**
     * 获取待检任务统计
     */
    Map<String, Object> selectPendingStats();

    /**
     * 获取待检任务列表
     */
    List<Map<String, Object>> selectPendingList(@Param("inspectType") String inspectType);
}
