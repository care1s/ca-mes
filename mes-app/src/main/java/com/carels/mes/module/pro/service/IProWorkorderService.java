package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProWorkorder;

import java.util.List;
import java.util.Map;

/**
 * 生产工单Service接口 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface IProWorkorderService {
    
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
     * 工单下达
     */
    int releaseWorkorder(Long workorderId);
    
    /**
     * 工单关闭
     */
    int closeWorkorder(Long workorderId);
    
    /**
     * 获取工单汇总统计 - 看板用
     */
    Map<String, Object> getWorkorderSummary();
    
    /**
     * 获取工单看板数据
     */
    List<Map<String, Object>> getWorkorderDashboard();

    /**
     * 根据生产计划生成工单
     */
    ProWorkorder createWorkorderFromPlan(Long planId, Long workshopId, String workshopName);
}
