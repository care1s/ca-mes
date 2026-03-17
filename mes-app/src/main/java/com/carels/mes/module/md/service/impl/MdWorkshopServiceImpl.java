package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdWorkshop;
import com.carels.mes.module.md.mapper.MdWorkshopMapper;
import com.carels.mes.module.md.service.IMdWorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车间Service实现 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Service
public class MdWorkshopServiceImpl implements IMdWorkshopService {
    
    @Autowired
    private MdWorkshopMapper mdWorkshopMapper;
    
    @Override
    public List<MdWorkshop> selectMdWorkshopList(MdWorkshop mdWorkshop) {
        return mdWorkshopMapper.selectMdWorkshopList(mdWorkshop);
    }
    
    @Override
    public MdWorkshop selectMdWorkshopById(Long workshopId) {
        return mdWorkshopMapper.selectMdWorkshopById(workshopId);
    }
    
    @Override
    public MdWorkshop selectMdWorkshopByCode(String workshopCode) {
        return mdWorkshopMapper.selectMdWorkshopByCode(workshopCode);
    }
    
    @Override
    public List<MdWorkshop> selectAllEnabledWorkshops() {
        return mdWorkshopMapper.selectAllEnabledWorkshops();
    }
    
    @Override
    public int insertMdWorkshop(MdWorkshop mdWorkshop) {
        return mdWorkshopMapper.insertMdWorkshop(mdWorkshop);
    }
    
    @Override
    public int updateMdWorkshop(MdWorkshop mdWorkshop) {
        return mdWorkshopMapper.updateMdWorkshop(mdWorkshop);
    }
    
    @Override
    public int deleteMdWorkshopById(Long workshopId) {
        return mdWorkshopMapper.deleteMdWorkshopById(workshopId);
    }
    
    @Override
    public int deleteMdWorkshopByIds(Long[] workshopIds) {
        return mdWorkshopMapper.deleteMdWorkshopByIds(workshopIds);
    }
    
    @Override
    public boolean checkWorkshopCodeUnique(String workshopCode) {
        MdWorkshop workshop = mdWorkshopMapper.selectMdWorkshopByCode(workshopCode);
        return workshop == null;
    }
    
    @Override
    public boolean canDeleteWorkshop(Long workshopId) {
        // 检查是否有关联的生产线
        int lineCount = mdWorkshopMapper.countProductionLinesByWorkshopId(workshopId);
        if (lineCount > 0) {
            return false;
        }
        // 检查是否有关联的工作站
        int workstationCount = mdWorkshopMapper.countWorkstationsByWorkshopId(workshopId);
        if (workstationCount > 0) {
            return false;
        }
        return true;
    }
}
