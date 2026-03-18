package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcOqc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成品检验(OQC)Mapper接口
 */
public interface QcOqcMapper {
    
    /**
     * 查询OQC列表
     */
    List<QcOqc> selectQcOqcList(QcOqc oqc);
    
    /**
     * 根据ID查询OQC
     */
    QcOqc selectQcOqcById(Long id);
    
    /**
     * 新增OQC
     */
    int insertQcOqc(QcOqc oqc);
    
    /**
     * 修改OQC
     */
    int updateQcOqc(QcOqc oqc);
    
    /**
     * 删除OQC
     */
    int deleteQcOqcById(Long id);
    
    /**
     * 更新检验状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
