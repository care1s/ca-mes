package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 生产任务Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface ProTaskMapper {

    /**
     * 查询任务列表
     */
    List<ProTask> selectProTaskList(ProTask task);

    /**
     * 根据ID查询任务
     */
    ProTask selectProTaskById(Long taskId);

    /**
     * 根据任务编码查询
     */
    ProTask selectProTaskByCode(String taskCode);

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
     * 更新任务状态
     */
    int updateStatus(@Param("taskId") Long taskId, @Param("status") String status);

    /**
     * 更新完工数量
     */
    int updateCompletedQty(@Param("taskId") Long taskId, @Param("qty") Double qty);

    /**
     * 扫码查询任务（移动端）
     */
    Map<String, Object> selectTaskByBarcode(@Param("barcode") String barcode);

    /**
     * 获取待执行的任务列表（移动端）
     */
    List<Map<String, Object>> selectPendingTaskList(@Param("workstationId") Long workstationId);

    /**
     * 获取我的任务列表（移动端）
     */
    List<Map<String, Object>> selectMyTaskList(@Param("operatorId") Long operatorId);

    /**
     * 根据工单ID删除任务
     */
    int deleteByWorkorderId(Long workorderId);
}
