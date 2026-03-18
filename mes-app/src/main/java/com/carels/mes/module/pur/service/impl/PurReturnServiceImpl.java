package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurReturn;
import com.carels.mes.module.pur.mapper.PurReturnMapper;
import com.carels.mes.module.pur.service.IPurReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurReturnServiceImpl implements IPurReturnService {
    
    @Autowired
    private PurReturnMapper purReturnMapper;
    
    @Override
    public List<PurReturn> selectPurReturnList(PurReturn purReturn) {
        return purReturnMapper.selectPurReturnList(purReturn);
    }
    
    @Override
    public PurReturn selectPurReturnById(Long returnId) {
        return purReturnMapper.selectPurReturnById(returnId);
    }
    
    @Override
    public int insertPurReturn(PurReturn purReturn) {
        purReturn.setStatus("PENDING");
        return purReturnMapper.insertPurReturn(purReturn);
    }
    
    @Override
    public int updatePurReturn(PurReturn purReturn) {
        return purReturnMapper.updatePurReturn(purReturn);
    }
    
    @Override
    public int deletePurReturnById(Long returnId) {
        return purReturnMapper.deletePurReturnById(returnId);
    }
}
