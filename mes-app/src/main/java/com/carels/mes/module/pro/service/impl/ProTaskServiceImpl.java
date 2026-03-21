package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProTask;
import com.carels.mes.module.pro.mapper.ProTaskMapper;
import com.carels.mes.module.pro.service.IProTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生产任务Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Service
public class ProTaskServiceImpl implements IProTaskService {

    @Autowired
    private ProTaskMapper taskMapper;

    @Override
    public List<ProTask> selectProTaskList(ProTask task) {
        return taskMapper.selectProTaskList(task);
    }

    @Override
    public ProTask selectProTaskById(Long taskId) {
        return taskMapper.selectProTaskById(taskId);
    }

    @Override
    public int insertProTask(ProTask task) {
        return taskMapper.insertProTask(task);
    }

    @Override
    public int updateProTask(ProTask task) {
        return taskMapper.updateProTask(task);
    }

    @Override
    public int deleteProTaskById(Long taskId) {
        return taskMapper.deleteProTaskById(taskId);
    }

    @Override
    public int startTask(Long taskId) {
        return taskMapper.updateStatus(taskId, "PROCESSING");
    }

    @Override
    public int completeTask(Long taskId) {
        return taskMapper.updateStatus(taskId, "COMPLETED");
    }

    @Override
    public Map<String, Object> scanTask(String barcode) {
        return taskMapper.selectTaskByBarcode(barcode);
    }

    @Override
    public List<Map<String, Object>> getPendingTaskList(Long workstationId) {
        List<Map<String, Object>> list = taskMapper.selectPendingTaskList(workstationId);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMyTaskList(Long operatorId) {
        List<Map<String, Object>> list = taskMapper.selectMyTaskList(operatorId);
        return list != null ? list : new ArrayList<>();
    }
}
