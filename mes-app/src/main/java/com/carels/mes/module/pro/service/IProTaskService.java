package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProTask;

import java.util.List;
import java.util.Map;

/**
 * 生产任务Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface IProTaskService {

    /**
     * 查询任务列表
     */
    List<ProTask> selectProTaskList(ProTask task);

    /**
     * 根据ID查询任务
     */
    ProTask selectProTaskById(Long taskId);

    /**
     * 新增任务
     */
    int insertProTask(ProTask task);

    /**
     * 修改任务
     */
    int updateProTask(ProTask task);

    /**
     * 删除任务
     */
    int deleteProTaskById(Long taskId);

    /**
     * 开始任务
     */
    int startTask(Long taskId);

    /**
     * 完成任务
     */
    int completeTask(Long taskId);

    /**
     * 扫码查询任务
     */
    Map<String, Object> scanTask(String barcode);

    /**
     * 获取待执行的任务列表
     */
    List<Map<String, Object>> getPendingTaskList(Long workstationId);

    /**
     * 获取我的任务列表
     */
    List<Map<String, Object>> getMyTaskList(Long operatorId);
}
