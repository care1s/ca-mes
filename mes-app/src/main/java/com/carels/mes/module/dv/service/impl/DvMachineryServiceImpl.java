package com.carels.mes.module.dv.service.impl;

import com.carels.mes.module.dv.domain.DvMachinery;
import com.carels.mes.module.dv.mapper.DvMachineryMapper;
import com.carels.mes.module.dv.service.IDvMachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DvMachineryServiceImpl implements IDvMachineryService {
    
    @Autowired
    private DvMachineryMapper machineryMapper;
    
    @Override
    public List<DvMachinery> selectDvMachineryList(DvMachinery machinery) {
        return machineryMapper.selectDvMachineryList(machinery);
    }
    
    @Override
    public DvMachinery selectDvMachineryById(Long id) {
        return machineryMapper.selectDvMachineryById(id);
    }
    
    @Override
    public int insertDvMachinery(DvMachinery machinery) {
        machinery.setStatus("NORMAL");
        return machineryMapper.insertDvMachinery(machinery);
    }
    
    @Override
    public int updateDvMachinery(DvMachinery machinery) {
        return machineryMapper.updateDvMachinery(machinery);
    }
    
    @Override
    public int deleteDvMachineryById(Long id) {
        return machineryMapper.deleteDvMachineryById(id);
    }
    
    @Override
    public void updateStatus(Long id, String status) {
        machineryMapper.updateStatus(id, status);
    }
}
