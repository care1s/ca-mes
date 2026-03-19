package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmLocation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 仓位管理Mapper接口
 */
public interface WmLocationMapper {
    
    List<WmLocation> selectWmLocationList(WmLocation location);
    
    WmLocation selectWmLocationById(Long locationId);
    
    List<WmLocation> selectWmLocationByZoneId(Long zoneId);
    
    List<WmLocation> selectWmLocationByWarehouseId(Long warehouseId);
    
    int insertWmLocation(WmLocation location);
    
    int updateWmLocation(WmLocation location);
    
    int deleteWmLocationById(Long locationId);
    
    int deleteWmLocationByIds(Long[] locationIds);
    
    WmLocation checkLocationCodeUnique(@Param("locationCode") String locationCode, @Param("zoneId") Long zoneId);
}
