package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmLocation;
import java.util.List;

/**
 * 仓位管理Service接口
 */
public interface IWmLocationService {
    
    List<WmLocation> selectWmLocationList(WmLocation location);
    
    WmLocation selectWmLocationById(Long locationId);
    
    List<WmLocation> selectWmLocationByZoneId(Long zoneId);
    
    List<WmLocation> selectWmLocationByWarehouseId(Long warehouseId);
    
    int insertWmLocation(WmLocation location);
    
    int updateWmLocation(WmLocation location);
    
    int deleteWmLocationById(Long locationId);
    
    int deleteWmLocationByIds(Long[] locationIds);
    
    String checkLocationCodeUnique(WmLocation location);
}
