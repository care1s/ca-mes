package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProWorkorder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 生产工单Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface ProWorkorderMapper {

    /**
     * 查询工单列表
     */
    List<ProWorkorder> selectProWorkorderList(ProWorkorder workorder);

    /**
     * 根据ID查询工单
     */
    ProWorkorder selectProWorkorderById(Long workorderId);

    /**
     * 新增工单
     */
    int insertProWorkorder(ProWorkorder workorder);

    /**
     * 修改工单
     */
    int updateProWorkorder(ProWorkorder workorder);

    /**
     * 删除工单
     */
    int deleteProWorkorderById(Long workorderId);

    /**
     * 批量删除工单
     */
    int deleteProWorkorderByIds(Long[] workorderIds);

    /**
     * 更新工单状态
     */
    int updateStatus(@Param("workorderId") Long workorderId, @Param("status") String status);

    /**
     * 获取工单状态统计 - 看板用
     */
    Map<String, Object> selectWorkorderStatusStats();

    /**
     * 获取工单看板列表
     */
    List<Map<String, Object>> selectWorkorderDashboardList();

    /**
     * 获取延期工单列表
     */
    List<Map<String, Object>> selectDelayedWorkorderList();

    /**
     * 根据计划ID查询已存在工单的计划数量总和
     */
    Double selectSumPlanQuantityByPlanId(Long planId);
}
