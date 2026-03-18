package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.domain.QcIqc;
import com.carels.mes.module.qc.mapper.QcIqcMapper;
import com.carels.mes.module.qc.service.IQcIqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcIqcServiceImpl implements IQcIqcService {
    
    @Autowired
    private QcIqcMapper iqcMapper;
    
    @Override
    public List<QcIqc> selectQcIqcList(QcIqc iqc) {
        return iqcMapper.selectQcIqcList(iqc);
    }
    
    @Override
    public QcIqc selectQcIqcById(Long iqcId) {
        return iqcMapper.selectQcIqcById(iqcId);
    }
    
    @Override
    public int insertQcIqc(QcIqc iqc) {
        iqc.setStatus("PENDING");
        return iqcMapper.insertQcIqc(iqc);
    }
    
    @Override
    public int updateQcIqc(QcIqc iqc) {
        return iqcMapper.updateQcIqc(iqc);
    }
    
    @Override
    public int deleteQcIqcById(Long iqcId) {
        return iqcMapper.deleteQcIqcById(iqcId);
    }
    
    @Override
    public void updateStatus(Long iqcId, String status) {
        iqcMapper.updateStatus(iqcId, status);
    }
}
