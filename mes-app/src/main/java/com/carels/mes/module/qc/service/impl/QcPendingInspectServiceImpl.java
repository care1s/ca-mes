package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.mapper.QcPendingInspectMapper;
import com.carels.mes.module.qc.service.IQcPendingInspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待检任务Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Service
public class QcPendingInspectServiceImpl implements IQcPendingInspectService {

    @Autowired
    private QcPendingInspectMapper pendingInspectMapper;

    @Override
    public Map<String, Object> getPendingInspectDashboard() {
        Map<String, Object> data = new HashMap<>();

        // 统计汇总
        Map<String, Object> summary = pendingInspectMapper.selectPendingStats();
        if (summary == null) {
            summary = new HashMap<>();
            summary.put("iqc", 0);
            summary.put("ipqc", 0);
            summary.put("oqc", 0);
            summary.put("rqc", 0);
            summary.put("total", 0);
            summary.put("urgent", 0);
        }
        data.put("summary", summary);

        // 待检任务列表（按紧急程度排序）
        List<Map<String, Object>> list = pendingInspectMapper.selectPendingList(null);
        data.put("list", list != null ? list : new ArrayList<>());

        return data;
    }

    @Override
    public List<Map<String, Object>> getPendingInspectList(String inspectType) {
        List<Map<String, Object>> list = pendingInspectMapper.selectPendingList(inspectType);
        return list != null ? list : new ArrayList<>();
    }
}
