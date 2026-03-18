package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcIqc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 来料检验(IQC)Mapper接口
 */
public interface QcIqcMapper {
    
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
    int updateStatus(@Param("iqcId") Long iqcId, @Param("status") String status);
}
