package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProPlan;
import com.carels.mes.module.pro.domain.ProRouteProcess;
import com.carels.mes.module.pro.domain.ProTask;
import com.carels.mes.module.pro.domain.ProWorkorder;
import com.carels.mes.module.pro.mapper.ProRouteProcessMapper;
import com.carels.mes.module.pro.mapper.ProTaskMapper;
import com.carels.mes.module.pro.mapper.ProWorkorderMapper;
import com.carels.mes.module.pro.service.IProPlanService;
import com.carels.mes.module.pro.service.IProWorkorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产工单Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Service
public class ProWorkorderServiceImpl implements IProWorkorderService {

    @Autowired
    private ProWorkorderMapper workorderMapper;

    @Autowired
    private ProTaskMapper taskMapper;

    @Autowired
    private ProRouteProcessMapper routeProcessMapper;

    @Autowired
    private IProPlanService planService;

    @Override
    public List<ProWorkorder> selectProWorkorderList(ProWorkorder workorder) {
        return workorderMapper.selectProWorkorderList(workorder);
    }

    @Override
    public ProWorkorder selectProWorkorderById(Long workorderId) {
        return workorderMapper.selectProWorkorderById(workorderId);
    }

    @Override
    public int insertProWorkorder(ProWorkorder workorder) {
        return workorderMapper.insertProWorkorder(workorder);
    }

    @Override
    public int updateProWorkorder(ProWorkorder workorder) {
        return workorderMapper.updateProWorkorder(workorder);
    }

    @Override
    public int deleteProWorkorderById(Long workorderId) {
        return workorderMapper.deleteProWorkorderById(workorderId);
    }

    @Override
    @Transactional
    public int releaseWorkorder(Long workorderId) {
        // 1. 获取工单详情
        ProWorkorder workorder = workorderMapper.selectProWorkorderById(workorderId);
        if (workorder == null) {
            throw new RuntimeException("工单不存在");
        }
        if (workorder.getRouteId() == null) {
            throw new RuntimeException("工单未设置工艺路线，无法生成任务");
        }

        // 2. 更新工单状态为已下达
        int result = workorderMapper.updateStatus(workorderId, "RELEASED");

        // 3. 根据工艺路线的工序生成任务
        List<ProRouteProcess> routeProcesses = routeProcessMapper.selectByRouteId(workorder.getRouteId());
        if (routeProcesses == null || routeProcesses.isEmpty()) {
            throw new RuntimeException("工艺路线没有配置工序");
        }

        // 计算每个任务的计划开始和结束时间
        Date currentPlanStart = workorder.getPlanStartTime();
        Calendar calendar = Calendar.getInstance();

        for (ProRouteProcess routeProcess : routeProcesses) {
            ProTask task = new ProTask();
            task.setTaskCode(generateTaskCode());
            task.setWorkorderId(workorderId);
            task.setWorkorderCode(workorder.getWorkorderCode());
            task.setProcessId(routeProcess.getProcessId());
            task.setProcessCode(routeProcess.getProcessCode());
            task.setProcessName(routeProcess.getProcessName());
            task.setWorkstationId(routeProcess.getWorkstationId());
            task.setWorkstationName(routeProcess.getWorkstationName());
            task.setPlanQuantity(workorder.getPlanQuantity());
            task.setCompletedQuantity(0.0);
            task.setQualifiedQuantity(0.0);
            task.setDefectiveQuantity(0.0);
            task.setStatus("PENDING");

            // 设置计划时间
            task.setPlanStartTime(currentPlanStart);

            // 根据标准工时计算计划结束时间
            if (routeProcess.getStandardHours() != null && routeProcess.getStandardHours().doubleValue() > 0) {
                calendar.setTime(currentPlanStart);
                calendar.add(Calendar.MINUTE, (int)(routeProcess.getStandardHours().doubleValue() * 60));
                task.setPlanEndTime(calendar.getTime());
            } else {
                // 默认给1小时
                calendar.setTime(currentPlanStart);
                calendar.add(Calendar.HOUR, 1);
                task.setPlanEndTime(calendar.getTime());
            }

            // 下一个任务的开始时间是当前任务的结束时间
            currentPlanStart = task.getPlanEndTime();

            taskMapper.insertProTask(task);
        }

        return result;
    }

    @Override
    public int closeWorkorder(Long workorderId) {
        return workorderMapper.updateStatus(workorderId, "CLOSED");
    }

    @Override
    public Map<String, Object> getWorkorderSummary() {
        Map<String, Object> stats = workorderMapper.selectWorkorderStatusStats();
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("pending", 0);
            stats.put("released", 0);
            stats.put("producing", 0);
            stats.put("completed", 0);
            stats.put("closed", 0);
            stats.put("total", 0);
            stats.put("delayed", 0);
        }
        return stats;
    }

    @Override
    public List<Map<String, Object>> getWorkorderDashboard() {
        List<Map<String, Object>> list = workorderMapper.selectWorkorderDashboardList();
        return list != null ? list : new ArrayList<>();
    }

    @Override
    @Transactional
    public ProWorkorder createWorkorderFromPlan(Long planId, Long workshopId, String workshopName) {
        // 1. 获取生产计划详情
        ProPlan plan = planService.selectProPlanById(planId);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }

        // 2. 创建工单
        ProWorkorder workorder = new ProWorkorder();
        workorder.setWorkorderCode(generateWorkorderCode());
        workorder.setPlanId(planId);
        workorder.setPlanNo(plan.getPlanNo());
        workorder.setWorkorderType("STANDARD");
        workorder.setItemId(plan.getItemId());
        workorder.setItemCode(plan.getItemCode());
        workorder.setItemName(plan.getItemName());
        workorder.setSpecification(plan.getSpecification());
        workorder.setPlanQuantity(plan.getPlanQty());
        workorder.setPriority("NORMAL");

        // 设置时间
        if (plan.getStartDate() != null) {
            workorder.setPlanStartTime(java.sql.Date.valueOf(plan.getStartDate()));
        }
        if (plan.getEndDate() != null) {
            workorder.setPlanEndTime(java.sql.Date.valueOf(plan.getEndDate()));
        }

        // 设置车间
        workorder.setWorkshopId(workshopId);
        workorder.setWorkshopName(workshopName);

        // 设置客户信息（如果计划绑定了销售订单）
        if (plan.getScheduleType() != null && plan.getScheduleType() == 1) {
            workorder.setClientId(plan.getCustomerId());
            workorder.setClientName(plan.getCustomerName());
            workorder.setSalesOrderNo(plan.getSalesOrderNo());
        }

        workorder.setStatus("PENDING");

        // 3. 保存工单
        workorderMapper.insertProWorkorder(workorder);

        return workorder;
    }

    /**
     * 生成工单编码
     */
    private String generateWorkorderCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        return "WO" + dateStr + seq;
    }

    /**
     * 生成任务编码
     */
    private String generateTaskCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        return "TK" + dateStr + seq;
    }
}
