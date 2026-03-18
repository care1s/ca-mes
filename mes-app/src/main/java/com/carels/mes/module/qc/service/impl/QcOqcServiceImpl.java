package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.domain.QcOqc;
import com.carels.mes.module.qc.mapper.QcOqcMapper;
import com.carels.mes.module.qc.service.IQcOqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcOqcServiceImpl implements IQcOqcService {
    
    @Autowired
    private QcOqcMapper oqcMapper;
    
    @Override
    public List<QcOqc> selectQcOqcList(QcOqc oqc) {
        return oqcMapper.selectQcOqcList(oqc);
    }
    
    @Override
    public QcOqc selectQcOqcById(Long id) {
        return oqcMapper.selectQcOqcById(id);
    }
    
    @Override
    public int insertQcOqc(QcOqc oqc) {
        oqc.setStatus("PENDING");
        return oqcMapper.insertQcOqc(oqc);
    }
    
    @Override
    public int updateQcOqc(QcOqc oqc) {
        return oqcMapper.updateQcOqc(oqc);
    }
    
    @Override
    public int deleteQcOqcById(Long id) {
        return oqcMapper.deleteQcOqcById(id);
    }
    
    @Override
    public void updateStatus(Long id, String status) {
        oqcMapper.updateStatus(id, status);
    }
}
