package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdWorkshop;
import com.carels.mes.module.md.domain.MdWorkstation;
import com.carels.mes.module.md.mapper.MdWorkshopMapper;
import com.carels.mes.module.md.mapper.MdWorkstationMapper;
import com.carels.mes.module.md.service.IMdWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作站Service实现 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Service
public class MdWorkstationServiceImpl implements IMdWorkstationService {
    
    @Autowired
    private MdWorkstationMapper mdWorkstationMapper;
    
    @Autowired
    private MdWorkshopMapper mdWorkshopMapper;
    
    @Override
    public List<MdWorkstation> selectMdWorkstationList(MdWorkstation mdWorkstation) {
        return mdWorkstationMapper.selectMdWorkstationList(mdWorkstation);
    }
    
    @Override
    public MdWorkstation selectMdWorkstationById(Long workstationId) {
        return mdWorkstationMapper.selectMdWorkstationById(workstationId);
    }
    
    @Override
    public MdWorkstation selectMdWorkstationByCode(String workstationCode) {
        return mdWorkstationMapper.selectMdWorkstationByCode(workstationCode);
    }
    
    @Override
    public List<MdWorkstation> selectMdWorkstationByWorkshopId(Long workshopId) {
        return mdWorkstationMapper.selectMdWorkstationByWorkshopId(workshopId);
    }
    
    @Override
    public List<MdWorkstation> selectMdWorkstationByLineId(Long lineId) {
        return mdWorkstationMapper.selectMdWorkstationByLineId(lineId);
    }
    
    @Override
    public List<MdWorkstation> selectAllEnabledWorkstations() {
        return mdWorkstationMapper.selectAllEnabledWorkstations();
    }
    
    @Override
    public int insertMdWorkstation(MdWorkstation mdWorkstation) {
        // 检查车间模式，如果是简单模式则清空生产线信息
        clearProductionLineIfSimpleMode(mdWorkstation);
        return mdWorkstationMapper.insertMdWorkstation(mdWorkstation);
    }
    
    @Override
    public int updateMdWorkstation(MdWorkstation mdWorkstation) {
        // 检查车间模式，如果是简单模式则清空生产线信息
        clearProductionLineIfSimpleMode(mdWorkstation);
        return mdWorkstationMapper.updateMdWorkstation(mdWorkstation);
    }
    
    /**
     * 如果车间是简单模式，清空生产线相关信息
     */
    private void clearProductionLineIfSimpleMode(MdWorkstation mdWorkstation) {
        if (mdWorkstation.getWorkshopId() != null) {
            MdWorkshop workshop = mdWorkshopMapper.selectMdWorkshopById(mdWorkstation.getWorkshopId());
            if (workshop != null && "SIMPLE".equals(workshop.getOrgMode())) {
                // 简单模式下清空生产线信息
                mdWorkstation.setProductionLineId(null);
                mdWorkstation.setProductionLineName(null);
            }
        }
    }
    
    @Override
    public int deleteMdWorkstationById(Long workstationId) {
        return mdWorkstationMapper.deleteMdWorkstationById(workstationId);
    }
    
    @Override
    public int deleteMdWorkstationByIds(Long[] workstationIds) {
        return mdWorkstationMapper.deleteMdWorkstationByIds(workstationIds);
    }
    
    @Override
    public boolean checkWorkstationCodeUnique(String workstationCode) {
        MdWorkstation workstation = mdWorkstationMapper.selectMdWorkstationByCode(workstationCode);
        return workstation == null;
    }
}
