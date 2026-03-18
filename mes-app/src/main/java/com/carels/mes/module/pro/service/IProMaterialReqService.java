package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProMaterialReq;

import java.util.List;

/**
 * 物料需求Service接口
 */
public interface IProMaterialReqService {
    
    /**
     * 查询需求列表
     */
    List<ProMaterialReq> selectProMaterialReqList(ProMaterialReq req);
    
    /**
     * 根据ID查询需求单
     */
    ProMaterialReq selectProMaterialReqById(Long reqId);
    
    /**
     * 新增需求单
     */
    int insertProMaterialReq(ProMaterialReq req);
    
    /**
     * 修改需求单
     */
    int updateProMaterialReq(ProMaterialReq req);
    
    /**
     * 删除需求单
     */
    int deleteProMaterialReqById(Long reqId);
    
    /**
     * 提交审核
     */
    void submitReq(Long reqId);
    
    /**
     * 审核通过
     */
    void approveReq(Long reqId);
    
    /**
     * 发料确认
     */
    void issueMaterial(Long reqId);
    
    /**
     * 完成需求单
     */
    void completeReq(Long reqId);
    
    /**
     * 取消需求单
     */
    void cancelReq(Long reqId);
}
