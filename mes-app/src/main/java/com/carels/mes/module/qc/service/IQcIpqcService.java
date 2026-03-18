package com.carels.mes.module.qc.service;

import com.carels.mes.module.qc.domain.QcIpqc;

import java.util.List;

/**
 * 过程检验(IPQC)Service接口
 */
public interface IQcIpqcService {
    
    /**
     * 查询IPQC列表
     */
    List<QcIpqc> selectQcIpqcList(QcIpqc ipqc);
    
    /**
     * 根据ID查询IPQC
     */
    QcIpqc selectQcIpqcById(Long id);
    
    /**
     * 新增IPQC
     */
    int insertQcIpqc(QcIpqc ipqc);
    
    /**
     * 修改IPQC
     */
    int updateQcIpqc(QcIpqc ipqc);
    
    /**
     * 删除IPQC
     */
    int deleteQcIpqcById(Long id);
    
    /**
     * 更新检验状态
     */
    void updateStatus(Long id, String status);
}
