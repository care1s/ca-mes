package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmWarehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库管理Mapper接口
 */
public interface WmWarehouseMapper {
    
    List<WmWarehouse> selectWmWarehouseList(WmWarehouse warehouse);
    
    WmWarehouse selectWmWarehouseById(Long warehouseId);
    
    int insertWmWarehouse(WmWarehouse warehouse);
    
    int updateWmWarehouse(WmWarehouse warehouse);
    
    int deleteWmWarehouseById(Long warehouseId);
    
    int updateStatus(@Param("warehouseId") Long warehouseId, @Param("status") String status);
}
