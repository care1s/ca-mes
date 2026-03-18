package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProMaterialReq;
import com.carels.mes.module.pro.mapper.ProMaterialReqMapper;
import com.carels.mes.module.pro.service.IProMaterialReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料需求Service实现
 */
@Service
public class ProMaterialReqServiceImpl implements IProMaterialReqService {
    
    @Autowired
    private ProMaterialReqMapper reqMapper;
    
    @Override
    public List<ProMaterialReq> selectProMaterialReqList(ProMaterialReq req) {
        return reqMapper.selectProMaterialReqList(req);
    }
    
    @Override
    public ProMaterialReq selectProMaterialReqById(Long reqId) {
        return reqMapper.selectProMaterialReqById(reqId);
    }
    
    @Override
    public int insertProMaterialReq(ProMaterialReq req) {
        req.setStatus(0);
        req.setCreateTime(LocalDateTime.now());
        req.setUpdateTime(LocalDateTime.now());
        return reqMapper.insertProMaterialReq(req);
    }
    
    @Override
    public int updateProMaterialReq(ProMaterialReq req) {
        req.setUpdateTime(LocalDateTime.now());
        return reqMapper.updateProMaterialReq(req);
    }
    
    @Override
    public int deleteProMaterialReqById(Long reqId) {
        return reqMapper.deleteProMaterialReqById(reqId);
    }
    
    @Override
    public void submitReq(Long reqId) {
        reqMapper.updateStatus(reqId, 1);
    }
    
    @Override
    public void approveReq(Long reqId) {
        reqMapper.updateStatus(reqId, 2);
    }
    
    @Override
    public void issueMaterial(Long reqId) {
        reqMapper.updateStatus(reqId, 3);
    }
    
    @Override
    public void completeReq(Long reqId) {
        reqMapper.updateStatus(reqId, 4);
    }
    
    @Override
    public void cancelReq(Long reqId) {
        reqMapper.updateStatus(reqId, 5);
    }
}
