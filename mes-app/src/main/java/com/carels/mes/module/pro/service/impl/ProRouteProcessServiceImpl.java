package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProRouteProcess;
import com.carels.mes.module.pro.mapper.ProRouteProcessMapper;
import com.carels.mes.module.pro.service.IProRouteProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工艺路线工序Service实现 - carels
 */
@Service
public class ProRouteProcessServiceImpl implements IProRouteProcessService {

    @Autowired
    private ProRouteProcessMapper routeProcessMapper;

    @Override
    public List<ProRouteProcess> selectByRouteId(Long routeId) {
        return routeProcessMapper.selectByRouteId(routeId);
    }

    @Override
    public ProRouteProcess selectByRouteIdAndSeq(Long routeId, Integer sequenceNo) {
        return routeProcessMapper.selectByRouteIdAndSeq(routeId, sequenceNo);
    }

    @Override
    public int insert(ProRouteProcess routeProcess) {
        return routeProcessMapper.insert(routeProcess);
    }

    @Override
    public int batchInsert(List<ProRouteProcess> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return routeProcessMapper.batchInsert(list);
    }

    @Override
    public int update(ProRouteProcess routeProcess) {
        return routeProcessMapper.update(routeProcess);
    }

    @Override
    public int deleteById(Long id) {
        return routeProcessMapper.deleteById(id);
    }

    @Override
    public int deleteByRouteId(Long routeId) {
        return routeProcessMapper.deleteByRouteId(routeId);
    }

    @Override
    @Transactional
    public int saveRouteProcesses(Long routeId, List<ProRouteProcess> processes, String username) {
        // 1. 删除该工艺路线的所有旧工序配置
        routeProcessMapper.deleteByRouteId(routeId);

        // 2. 如果没有新配置，直接返回
        if (processes == null || processes.isEmpty()) {
            return 0;
        }

        // 3. 设置每个工序的routeId和sequenceNo
        int seqNo = 1;
        for (ProRouteProcess process : processes) {
            process.setRouteId(routeId);
            process.setSequenceNo(seqNo++);
            process.setCreateBy(username);
        }

        // 4. 批量插入新配置
        return routeProcessMapper.batchInsert(processes);
    }
}
