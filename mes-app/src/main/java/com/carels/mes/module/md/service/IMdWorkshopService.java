package com.carels.mes.module.md.service;

import com.carels.mes.module.md.domain.MdWorkshop;

import java.util.List;

/**
 * 车间Service接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface IMdWorkshopService {
    
    /**
     * 查询车间列表
     */
    List<MdWorkshop> selectMdWorkshopList(MdWorkshop mdWorkshop);
    
    /**
     * 根据ID查询车间
     */
    MdWorkshop selectMdWorkshopById(Long workshopId);
    
    /**
     * 根据编码查询车间
     */
    MdWorkshop selectMdWorkshopByCode(String workshopCode);
    
    /**
     * 查询所有启用的车间
     */
    List<MdWorkshop> selectAllEnabledWorkshops();
    
    /**
     * 新增车间
     */
    int insertMdWorkshop(MdWorkshop mdWorkshop);
    
    /**
     * 修改车间
     */
    int updateMdWorkshop(MdWorkshop mdWorkshop);
    
    /**
     * 删除车间
     */
    int deleteMdWorkshopById(Long workshopId);
    
    /**
     * 批量删除车间
     */
    int deleteMdWorkshopByIds(Long[] workshopIds);
    
    /**
     * 校验车间编码唯一性
     */
    boolean checkWorkshopCodeUnique(String workshopCode);
    
    /**
     * 校验车间是否可以删除（检查是否有关联的生产线或工作站）
     */
    boolean canDeleteWorkshop(Long workshopId);
}
