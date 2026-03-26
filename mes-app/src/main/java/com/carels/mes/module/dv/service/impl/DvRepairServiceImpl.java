package com.carels.mes.module.dv.service.impl;

import com.carels.mes.module.dv.domain.DvRepair;
import com.carels.mes.module.dv.mapper.DvRepairMapper;
import com.carels.mes.module.dv.service.IDvRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设备维修Service实现
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class DvRepairServiceImpl implements IDvRepairService {

    @Autowired
    private DvRepairMapper repairMapper;

    @Override
    public List<DvRepair> selectDvRepairList(DvRepair repair) {
        return repairMapper.selectDvRepairList(repair);
    }

    @Override
    public DvRepair selectDvRepairById(Long id) {
        return repairMapper.selectDvRepairById(id);
    }

    @Override
    public int insertDvRepair(DvRepair repair) {
        repair.setStatus("PENDING");
        return repairMapper.insertDvRepair(repair);
    }

    @Override
    public int updateDvRepair(DvRepair repair) {
        return repairMapper.updateDvRepair(repair);
    }

    @Override
    public int deleteDvRepairById(Long id) {
        return repairMapper.deleteDvRepairById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        repairMapper.updateStatus(id, status);
    }

    @Override
    public void assignRepair(Long id, Long repairmanId, String repairmanName) {
        DvRepair repair = repairMapper.selectDvRepairById(id);
        if (repair != null) {
            repair.setRepairmanId(repairmanId);
            repair.setRepairmanName(repairmanName);
            repair.setStatus("ASSIGNED");
            repairMapper.updateDvRepair(repair);
        }
    }

    @Override
    public void completeRepair(Long id, String repairContent, BigDecimal repairCost) {
        DvRepair repair = repairMapper.selectDvRepairById(id);
        if (repair != null) {
            repair.setRepairContent(repairContent);
            repair.setRepairCost(repairCost);
            repair.setStatus("COMPLETED");
            repair.setRepairDate(new Date());
            repairMapper.updateDvRepair(repair);
        }
    }
}
