package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.domain.QcDefect;
import com.carels.mes.module.qc.mapper.QcDefectMapper;
import com.carels.mes.module.qc.service.IQcDefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcDefectServiceImpl implements IQcDefectService {
    
    @Autowired
    private QcDefectMapper defectMapper;
    
    @Override
    public List<QcDefect> selectQcDefectList(QcDefect defect) {
        return defectMapper.selectQcDefectList(defect);
    }
    
    @Override
    public QcDefect selectQcDefectById(Long defectId) {
        return defectMapper.selectQcDefectById(defectId);
    }
    
    @Override
    public int insertQcDefect(QcDefect defect) {
        defect.setStatus(0);
        return defectMapper.insertQcDefect(defect);
    }
    
    @Override
    public int updateQcDefect(QcDefect defect) {
        return defectMapper.updateQcDefect(defect);
    }
    
    @Override
    public int deleteQcDefectById(Long defectId) {
        return defectMapper.deleteQcDefectById(defectId);
    }
    
    @Override
    public void updateStatus(Long defectId, Integer status) {
        defectMapper.updateStatus(defectId, status);
    }
}
