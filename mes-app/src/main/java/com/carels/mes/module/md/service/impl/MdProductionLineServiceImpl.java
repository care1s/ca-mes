package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdProductionLine;
import com.carels.mes.module.md.mapper.MdProductionLineMapper;
import com.carels.mes.module.md.service.IMdProductionLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生产线Service实现 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Service
public class MdProductionLineServiceImpl implements IMdProductionLineService {
    
    @Autowired
    private MdProductionLineMapper mdProductionLineMapper;
    
    @Override
    public List<MdProductionLine> selectMdProductionLineList(MdProductionLine mdProductionLine) {
        return mdProductionLineMapper.selectMdProductionLineList(mdProductionLine);
    }
    
    @Override
    public MdProductionLine selectMdProductionLineById(Long lineId) {
        return mdProductionLineMapper.selectMdProductionLineById(lineId);
    }
    
    @Override
    public MdProductionLine selectMdProductionLineByCode(String lineCode) {
        return mdProductionLineMapper.selectMdProductionLineByCode(lineCode);
    }
    
    @Override
    public List<MdProductionLine> selectMdProductionLineByWorkshopId(Long workshopId) {
        return mdProductionLineMapper.selectMdProductionLineByWorkshopId(workshopId);
    }
    
    @Override
    public List<MdProductionLine> selectAllEnabledLines() {
        return mdProductionLineMapper.selectAllEnabledLines();
    }
    
    @Override
    public int insertMdProductionLine(MdProductionLine mdProductionLine) {
        return mdProductionLineMapper.insertMdProductionLine(mdProductionLine);
    }
    
    @Override
    public int updateMdProductionLine(MdProductionLine mdProductionLine) {
        return mdProductionLineMapper.updateMdProductionLine(mdProductionLine);
    }
    
    @Override
    public int deleteMdProductionLineById(Long lineId) {
        return mdProductionLineMapper.deleteMdProductionLineById(lineId);
    }
    
    @Override
    public int deleteMdProductionLineByIds(Long[] lineIds) {
        return mdProductionLineMapper.deleteMdProductionLineByIds(lineIds);
    }
    
    @Override
    public boolean checkLineCodeUnique(String lineCode) {
        MdProductionLine line = mdProductionLineMapper.selectMdProductionLineByCode(lineCode);
        return line == null;
    }
    
    @Override
    public boolean canDeleteLine(Long lineId) {
        // 检查是否有关联的工作站
        int workstationCount = mdProductionLineMapper.countWorkstationsByLineId(lineId);
        return workstationCount == 0;
    }
}
