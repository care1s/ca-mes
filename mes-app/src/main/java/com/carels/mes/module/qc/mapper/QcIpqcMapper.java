package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcIpqc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 过程检验(IPQC)Mapper接口
 */
public interface QcIpqcMapper {
    
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
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
