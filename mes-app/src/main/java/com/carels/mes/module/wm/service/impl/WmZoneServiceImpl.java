package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmZone;
import com.carels.mes.module.wm.mapper.WmZoneMapper;
import com.carels.mes.module.wm.service.IWmZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 仓区管理Service实现
 */
@Service
public class WmZoneServiceImpl implements IWmZoneService {
    
    @Autowired
    private WmZoneMapper zoneMapper;
    
    @Override
    public List<WmZone> selectWmZoneList(WmZone zone) {
        return zoneMapper.selectWmZoneList(zone);
    }
    
    @Override
    public WmZone selectWmZoneById(Long zoneId) {
        return zoneMapper.selectWmZoneById(zoneId);
    }
    
    @Override
    public List<WmZone> selectWmZoneByWarehouseId(Long warehouseId) {
        return zoneMapper.selectWmZoneByWarehouseId(warehouseId);
    }
    
    @Override
    public int insertWmZone(WmZone zone) {
        return zoneMapper.insertWmZone(zone);
    }
    
    @Override
    public int updateWmZone(WmZone zone) {
        return zoneMapper.updateWmZone(zone);
    }
    
    @Override
    public int deleteWmZoneById(Long zoneId) {
        return zoneMapper.deleteWmZoneById(zoneId);
    }
    
    @Override
    public int deleteWmZoneByIds(Long[] zoneIds) {
        return zoneMapper.deleteWmZoneByIds(zoneIds);
    }
    
    @Override
    public String checkZoneCodeUnique(WmZone zone) {
        WmZone info = zoneMapper.checkZoneCodeUnique(zone.getZoneCode(), zone.getWarehouseId());
        if (info != null && !info.getZoneId().equals(zone.getZoneId())) {
            return "1";
        }
        return "0";
    }
}
