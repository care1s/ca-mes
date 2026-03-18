package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验模板Mapper接口
 */
public interface QcTemplateMapper {
    
    /**
     * 查询模板列表
     */
    List<QcTemplate> selectQcTemplateList(QcTemplate template);
    
    /**
     * 根据ID查询模板
     */
    QcTemplate selectQcTemplateById(Long templateId);
    
    /**
     * 新增模板
     */
    int insertQcTemplate(QcTemplate template);
    
    /**
     * 修改模板
     */
    int updateQcTemplate(QcTemplate template);
    
    /**
     * 删除模板
     */
    int deleteQcTemplateById(Long templateId);
    
    /**
     * 更新状态
     */
    int updateStatus(@Param("templateId") Long templateId, @Param("status") Integer status);
}
