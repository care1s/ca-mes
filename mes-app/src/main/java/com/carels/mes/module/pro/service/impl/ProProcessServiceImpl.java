package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProProcess;
import com.carels.mes.module.pro.mapper.ProProcessMapper;
import com.carels.mes.module.pro.service.IProProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Service
public class ProProcessServiceImpl implements IProProcessService {

    @Autowired
    private ProProcessMapper processMapper;

    @Override
    public List<ProProcess> selectProProcessList(ProProcess process) {
        return processMapper.selectProProcessList(process);
    }

    @Override
    public ProProcess selectProProcessById(Long processId) {
        return processMapper.selectProProcessById(processId);
    }

    @Override
    public ProProcess selectProProcessByCode(String processCode) {
        return processMapper.selectProProcessByCode(processCode);
    }

    @Override
    public int insertProProcess(ProProcess process) {
        process.setStatus("0");
        return processMapper.insertProProcess(process);
    }

    @Override
    public int updateProProcess(ProProcess process) {
        return processMapper.updateProProcess(process);
    }

    @Override
    public int deleteProProcessById(Long processId) {
        return processMapper.deleteProProcessById(processId);
    }

    @Override
    public int deleteProProcessByIds(Long[] processIds) {
        return processMapper.deleteProProcessByIds(processIds);
    }

    @Override
    public void enableProcess(Long processId) {
        processMapper.updateStatus(processId, "0");
    }

    @Override
    public void disableProcess(Long processId) {
        processMapper.updateStatus(processId, "1");
    }

    @Override
    public List<ProProcess> selectByWorkshopId(Long workshopId) {
        return processMapper.selectByWorkshopId(workshopId);
    }
}
