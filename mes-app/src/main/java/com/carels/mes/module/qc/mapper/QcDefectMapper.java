package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcDefect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 缺陷管理Mapper接口
 */
public interface QcDefectMapper {
    
    /**
     * 查询缺陷列表
     */
    List<QcDefect> selectQcDefectList(QcDefect defect);
    
    /**
     * 根据ID查询缺陷
     */
    QcDefect selectQcDefectById(Long defectId);
    
    /**
     * 新增缺陷
     */
    int insertQcDefect(QcDefect defect);
    
    /**
     * 修改缺陷
     */
    int updateQcDefect(QcDefect defect);
    
    /**
     * 删除缺陷
     */
    int deleteQcDefectById(Long defectId);
    
    /**
     * 更新状态
     */
    int updateStatus(@Param("defectId") Long defectId, @Param("status") Integer status);
}
