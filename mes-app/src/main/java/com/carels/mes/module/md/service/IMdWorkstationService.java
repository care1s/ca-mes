package com.carels.mes.module.md.service;

import com.carels.mes.module.md.domain.MdWorkstation;

import java.util.List;

/**
 * 工作站Service接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface IMdWorkstationService {
    
    /**
     * 查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationList(MdWorkstation mdWorkstation);
    
    /**
     * 根据ID查询工作站
     */
    MdWorkstation selectMdWorkstationById(Long workstationId);
    
    /**
     * 根据编码查询工作站
     */
    MdWorkstation selectMdWorkstationByCode(String workstationCode);
    
    /**
     * 根据车间ID查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationByWorkshopId(Long workshopId);
    
    /**
     * 根据生产线ID查询工作站列表
     */
    List<MdWorkstation> selectMdWorkstationByLineId(Long lineId);
    
    /**
     * 查询所有启用的工作站
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
    int deleteMdWorkstationById(Long workstationId);
    
    /**
     * 批量删除工作站
     */
    int deleteMdWorkstationByIds(Long[] workstationIds);
    
    /**
     * 校验工作站编码唯一性
     */
    boolean checkWorkstationCodeUnique(String workstationCode);
}
