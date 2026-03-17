package com.carels.mes.module.md.mapper;

import com.carels.mes.module.md.domain.MdWorkshop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车间Mapper接口 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
public interface MdWorkshopMapper {
    
    /**
     * 查询车间列表
     */
    List<MdWorkshop> selectMdWorkshopList(MdWorkshop mdWorkshop);
    
    /**
     * 根据ID查询车间
     */
    MdWorkshop selectMdWorkshopById(@Param("workshopId") Long workshopId);
    
    /**
     * 根据编码查询车间
     */
    MdWorkshop selectMdWorkshopByCode(@Param("workshopCode") String workshopCode);
    
    /**
      * 查询所有启用的车间（用于下拉选择）
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
    int deleteMdWorkshopById(@Param("workshopId") Long workshopId);
    
    /**
     * 批量删除车间
     */
    int deleteMdWorkshopByIds(Long[] workshopIds);
    
    /**
     * 检查车间下是否有生产线
     */
    int countProductionLinesByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 检查车间下是否有工作站
     */
    int countWorkstationsByWorkshopId(@Param("workshopId") Long workshopId);
}
