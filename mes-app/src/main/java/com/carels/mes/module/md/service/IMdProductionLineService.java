package com.carels.mes.module.md.service;

import com.carels.mes.module.md.domain.MdProductionLine;

import java.util.List;

/**
 * 生产线Service接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface IMdProductionLineService {
    
    /**
     * 查询生产线列表
     */
    List<MdProductionLine> selectMdProductionLineList(MdProductionLine mdProductionLine);
    
    /**
     * 根据ID查询生产线
     */
    MdProductionLine selectMdProductionLineById(Long lineId);
    
    /**
     * 根据编码查询生产线
     */
    MdProductionLine selectMdProductionLineByCode(String lineCode);
    
    /**
     * 根据车间ID查询生产线列表
     */
    List<MdProductionLine> selectMdProductionLineByWorkshopId(Long workshopId);
    
    /**
     * 查询所有启用的生产线
     */
    List<MdProductionLine> selectAllEnabledLines();
    
    /**
     * 新增生产线
     */
    int insertMdProductionLine(MdProductionLine mdProductionLine);
    
    /**
     * 修改生产线
     */
    int updateMdProductionLine(MdProductionLine mdProductionLine);
    
    /**
     * 删除生产线
     */
    int deleteMdProductionLineById(Long lineId);
    
    /**
     * 批量删除生产线
     */
    int deleteMdProductionLineByIds(Long[] lineIds);
    
    /**
     * 校验生产线编码唯一性
     */
    boolean checkLineCodeUnique(String lineCode);
    
    /**
     * 校验生产线是否可以删除（检查是否有关联的工作站）
     */
    boolean canDeleteLine(Long lineId);
}
