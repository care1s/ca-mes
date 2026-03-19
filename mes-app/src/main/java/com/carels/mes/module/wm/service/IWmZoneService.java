package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmZone;
import java.util.List;

/**
 * 仓区管理Service接口
 */
public interface IWmZoneService {
    
    List<WmZone> selectWmZoneList(WmZone zone);
    
    WmZone selectWmZoneById(Long zoneId);
    
    List<WmZone> selectWmZoneByWarehouseId(Long warehouseId);
    
    int insertWmZone(WmZone zone);
    
    int updateWmZone(WmZone zone);
    
    int deleteWmZoneById(Long zoneId);
    
    int deleteWmZoneByIds(Long[] zoneIds);
    
    String checkZoneCodeUnique(WmZone zone);
}
