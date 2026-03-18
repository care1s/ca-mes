package com.carels.mes.module.qc.service;

import com.carels.mes.module.qc.domain.QcIqc;

import java.util.List;

/**
 * 来料检验(IQC)Service接口
 */
public interface IQcIqcService {
    
    /**
     * 查询IQC列表
     */
    List<QcIqc> selectQcIqcList(QcIqc iqc);
    
    /**
     * 根据ID查询IQC
     */
    QcIqc selectQcIqcById(Long iqcId);
    
    /**
     * 新增IQC
     */
    int insertQcIqc(QcIqc iqc);
    
    /**
     * 修改IQC
     */
    int updateQcIqc(QcIqc iqc);
    
    /**
     * 删除IQC
     */
    int deleteQcIqcById(Long iqcId);
    
    /**
     * 更新检验状态
     */
    void updateStatus(Long iqcId, String status);
}
