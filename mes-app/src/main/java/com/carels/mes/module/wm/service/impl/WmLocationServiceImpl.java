package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmLocation;
import com.carels.mes.module.wm.mapper.WmLocationMapper;
import com.carels.mes.module.wm.service.IWmLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 仓位管理Service实现
 */
@Service
public class WmLocationServiceImpl implements IWmLocationService {
    
    @Autowired
    private WmLocationMapper locationMapper;
    
    @Override
    public List<WmLocation> selectWmLocationList(WmLocation location) {
        return locationMapper.selectWmLocationList(location);
    }
    
    @Override
    public WmLocation selectWmLocationById(Long locationId) {
        return locationMapper.selectWmLocationById(locationId);
    }
    
    @Override
    public List<WmLocation> selectWmLocationByZoneId(Long zoneId) {
        return locationMapper.selectWmLocationByZoneId(zoneId);
    }
    
    @Override
    public List<WmLocation> selectWmLocationByWarehouseId(Long warehouseId) {
        return locationMapper.selectWmLocationByWarehouseId(warehouseId);
    }
    
    @Override
    public int insertWmLocation(WmLocation location) {
        return locationMapper.insertWmLocation(location);
    }
    
    @Override
    public int updateWmLocation(WmLocation location) {
        return locationMapper.updateWmLocation(location);
    }
    
    @Override
    public int deleteWmLocationById(Long locationId) {
        return locationMapper.deleteWmLocationById(locationId);
    }
    
    @Override
    public int deleteWmLocationByIds(Long[] locationIds) {
        return locationMapper.deleteWmLocationByIds(locationIds);
    }
    
    @Override
    public String checkLocationCodeUnique(WmLocation location) {
        WmLocation info = locationMapper.checkLocationCodeUnique(location.getLocationCode(), location.getZoneId());
        if (info != null && !info.getLocationId().equals(location.getLocationId())) {
            return "1";
        }
        return "0";
    }
}
