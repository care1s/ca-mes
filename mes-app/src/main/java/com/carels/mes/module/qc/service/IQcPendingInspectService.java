package com.carels.mes.module.qc.service;

import java.util.List;
import java.util.Map;

/**
 * 待检任务Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface IQcPendingInspectService {

    /**
     * 获取待检任务看板数据
     */
    Map<String, Object> getPendingInspectDashboard();

    /**
     * 获取待检任务列表
     */
    List<Map<String, Object>> getPendingInspectList(String inspectType);
}
