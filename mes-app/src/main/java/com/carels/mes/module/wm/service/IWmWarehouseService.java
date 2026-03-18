package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmWarehouse;

import java.util.List;

/**
 * 仓库管理Service接口
 */
public interface IWmWarehouseService {
    
    List<WmWarehouse> selectWmWarehouseList(WmWarehouse warehouse);
    
    WmWarehouse selectWmWarehouseById(Long warehouseId);
    
    int insertWmWarehouse(WmWarehouse warehouse);
    
    int updateWmWarehouse(WmWarehouse warehouse);
    
    int deleteWmWarehouseById(Long warehouseId);
    
    void updateStatus(Long warehouseId, String status);
}
