package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.domain.QcIpqc;
import com.carels.mes.module.qc.mapper.QcIpqcMapper;
import com.carels.mes.module.qc.service.IQcIpqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcIpqcServiceImpl implements IQcIpqcService {
    
    @Autowired
    private QcIpqcMapper ipqcMapper;
    
    @Override
    public List<QcIpqc> selectQcIpqcList(QcIpqc ipqc) {
        return ipqcMapper.selectQcIpqcList(ipqc);
    }
    
    @Override
    public QcIpqc selectQcIpqcById(Long id) {
        return ipqcMapper.selectQcIpqcById(id);
    }
    
    @Override
    public int insertQcIpqc(QcIpqc ipqc) {
        ipqc.setStatus("PENDING");
        return ipqcMapper.insertQcIpqc(ipqc);
    }
    
    @Override
    public int updateQcIpqc(QcIpqc ipqc) {
        return ipqcMapper.updateQcIpqc(ipqc);
    }
    
    @Override
    public int deleteQcIpqcById(Long id) {
        return ipqcMapper.deleteQcIpqcById(id);
    }
    
    @Override
    public void updateStatus(Long id, String status) {
        ipqcMapper.updateStatus(id, status);
    }
}
