package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmZone;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 仓区管理Mapper接口
 */
public interface WmZoneMapper {
    
    List<WmZone> selectWmZoneList(WmZone zone);
    
    WmZone selectWmZoneById(Long zoneId);
    
    List<WmZone> selectWmZoneByWarehouseId(Long warehouseId);
    
    int insertWmZone(WmZone zone);
    
    int updateWmZone(WmZone zone);
    
    int deleteWmZoneById(Long zoneId);
    
    int deleteWmZoneByIds(Long[] zoneIds);
    
    WmZone checkZoneCodeUnique(@Param("zoneCode") String zoneCode, @Param("warehouseId") Long warehouseId);
}
