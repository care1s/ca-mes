package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProWorkorder;
import com.carels.mes.module.pro.mapper.ProWorkorderMapper;
import com.carels.mes.module.pro.service.IProWorkorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public int releaseWorkorder(Long workorderId) {
        return workorderMapper.updateStatus(workorderId, "RELEASED");
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
}
