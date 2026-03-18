package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurReturn;

import java.util.List;

public interface IPurReturnService {
    
    List<PurReturn> selectPurReturnList(PurReturn purReturn);
    
    PurReturn selectPurReturnById(Long returnId);
    
    int insertPurReturn(PurReturn purReturn);
    
    int updatePurReturn(PurReturn purReturn);
    
    int deletePurReturnById(Long returnId);
}
