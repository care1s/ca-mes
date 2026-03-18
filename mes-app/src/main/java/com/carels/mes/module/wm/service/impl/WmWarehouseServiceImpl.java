package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmWarehouse;
import com.carels.mes.module.wm.mapper.WmWarehouseMapper;
import com.carels.mes.module.wm.service.IWmWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmWarehouseServiceImpl implements IWmWarehouseService {
    
    @Autowired
    private WmWarehouseMapper warehouseMapper;
    
    @Override
    public List<WmWarehouse> selectWmWarehouseList(WmWarehouse warehouse) {
        return warehouseMapper.selectWmWarehouseList(warehouse);
    }
    
    @Override
    public WmWarehouse selectWmWarehouseById(Long warehouseId) {
        return warehouseMapper.selectWmWarehouseById(warehouseId);
    }
    
    @Override
    public int insertWmWarehouse(WmWarehouse warehouse) {
        warehouse.setStatus("0");
        return warehouseMapper.insertWmWarehouse(warehouse);
    }
    
    @Override
    public int updateWmWarehouse(WmWarehouse warehouse) {
        return warehouseMapper.updateWmWarehouse(warehouse);
    }
    
    @Override
    public int deleteWmWarehouseById(Long warehouseId) {
        return warehouseMapper.deleteWmWarehouseById(warehouseId);
    }
    
    @Override
    public void updateStatus(Long warehouseId, String status) {
        warehouseMapper.updateStatus(warehouseId, status);
    }
}
