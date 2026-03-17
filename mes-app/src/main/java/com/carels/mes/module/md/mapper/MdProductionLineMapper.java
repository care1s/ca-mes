package com.carels.mes.module.md.mapper;

import com.carels.mes.module.md.domain.MdProductionLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产线Mapper接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface MdProductionLineMapper {
    
    /**
     * 查询生产线列表
     */
    List<MdProductionLine> selectMdProductionLineList(MdProductionLine mdProductionLine);
    
    /**
     * 根据ID查询生产线
     */
    MdProductionLine selectMdProductionLineById(@Param("lineId") Long lineId);
    
    /**
     * 根据编码查询生产线
     */
    MdProductionLine selectMdProductionLineByCode(@Param("lineCode") String lineCode);
    
    /**
     * 根据车间ID查询生产线列表
     */
    List<MdProductionLine> selectMdProductionLineByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 查询所有启用的生产线（用于下拉选择）
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
    int deleteMdProductionLineById(@Param("lineId") Long lineId);
    
    /**
     * 批量删除生产线
     */
    int deleteMdProductionLineByIds(Long[] lineIds);
    
    /**
     * 检查生产线下是否有工作站
     */
    int countWorkstationsByLineId(@Param("lineId") Long lineId);
}
