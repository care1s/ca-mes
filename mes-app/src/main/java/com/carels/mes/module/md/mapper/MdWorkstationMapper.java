package com.carels.mes.module.md.mapper;

import com.carels.mes.module.md.domain.MdWorkstation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作站Mapper接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface MdWorkstationMapper {
    
    /**
     * 查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationList(MdWorkstation mdWorkstation);
    
    /**
     * 根据ID查询工作站
     */
    MdWorkstation selectMdWorkstationById(@Param("workstationId") Long workstationId);
    
    /**
     * 根据编码查询工作站
     */
    MdWorkstation selectMdWorkstationByCode(@Param("workstationCode") String workstationCode);
    
    /**
     * 根据车间ID查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 根据生产线ID查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationByLineId(@Param("lineId") Long lineId);
    
    /**
     * 查询所有启用的工作站（用于下拉选择）
     */
    List<MdWorkstation> selectAllEnabledWorkstations();
    
    /**
     * 新增工作站
     */
    int insertMdWorkstation(MdWorkstation mdWorkstation);
    
    /**
     * 修改工作站
     */
    int updateMdWorkstation(MdWorkstation mdWorkstation);
    
    /**
     * 删除工作站
     */
    int deleteMdWorkstationById(@Param("workstationId") Long workstationId);
    
    /**
     * 批量删除工作站
     */
    int deleteMdWorkstationByIds(Long[] workstationIds);
    
    /**
     * 根据车间ID删除工作站
     */
    int deleteMdWorkstationByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 根据生产线ID删除工作站
     */
    int deleteMdWorkstationByLineId(@Param("lineId") Long lineId);
}
