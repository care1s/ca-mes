package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmRecpt;
import com.carels.mes.module.wm.domain.WmRecptItem;
import com.carels.mes.module.wm.domain.WmStock;
import com.carels.mes.module.wm.mapper.WmRecptMapper;
import com.carels.mes.module.wm.mapper.WmStockMapper;
import com.carels.mes.module.wm.service.IWmRecptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WmRecptServiceImpl implements IWmRecptService {

    @Autowired
    private WmRecptMapper recptMapper;

    @Autowired
    private WmStockMapper stockMapper;

    @Override
    public List<WmRecpt> selectWmRecptList(WmRecpt recpt) {
        return recptMapper.selectWmRecptList(recpt);
    }

    @Override
    public WmRecpt selectWmRecptById(Long recptId) {
        return recptMapper.selectWmRecptById(recptId);
    }

    @Override
    @Transactional
    public int insertWmRecpt(WmRecpt recpt) {
        // 生成入库单号
        if (recpt.getRecptNo() == null || recpt.getRecptNo().isEmpty()) {
            recpt.setRecptNo("RECPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        // 设置默认状态
        if (recpt.getStatus() == null || recpt.getStatus().isEmpty()) {
            recpt.setStatus("PENDING");
        }
        int rows = recptMapper.insertWmRecpt(recpt);
        // 插入明细
        if (recpt.getItems() != null && !recpt.getItems().isEmpty()) {
            for (WmRecptItem item : recpt.getItems()) {
                item.setRecptId(recpt.getId());
                recptMapper.insertWmRecptItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateWmRecpt(WmRecpt recpt) {
        int rows = recptMapper.updateWmRecpt(recpt);
        // 更新明细：先删除再插入
        if (recpt.getItems() != null) {
            recptMapper.deleteWmRecptItemsByRecptId(recpt.getId());
            for (WmRecptItem item : recpt.getItems()) {
                item.setRecptId(recpt.getId());
                recptMapper.insertWmRecptItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteWmRecptById(Long recptId) {
        // 先删除明细
        recptMapper.deleteWmRecptItemsByRecptId(recptId);
        return recptMapper.deleteWmRecptById(recptId);
    }

    @Override
    public void updateStatus(Long recptId, String status) {
        recptMapper.updateStatus(recptId, status);
    }

    @Override
    @Transactional
    public void confirmRecpt(Long recptId) {
        // 1. 更新入库单状态为已确认
        recptMapper.updateStatus(recptId, "CONFIRMED");
        
        // 2. 获取入库单详情（包含明细）
        WmRecpt recpt = recptMapper.selectWmRecptById(recptId);
        if (recpt == null || recpt.getItems() == null || recpt.getItems().isEmpty()) {
            return;
        }
        
        // 3. 遍历明细，更新库存表
        for (WmRecptItem item : recpt.getItems()) {
            // 检查库存记录是否存在（使用 itemId2 作为物料ID）
            WmStock stockQuery = new WmStock();
            stockQuery.setWarehouseId(recpt.getWarehouseId());
            stockQuery.setItemId(item.getItemId2());
            stockQuery.setBatchCode(item.getBatchCode());
            
            List<WmStock> existStocks = stockMapper.selectWmStockList(stockQuery);
            
            if (existStocks != null && !existStocks.isEmpty()) {
                // 库存记录存在，更新数量
                WmStock existStock = existStocks.get(0);
                BigDecimal newQty = existStock.getQuantity().add(item.getQuantity());
                BigDecimal newAvailQty = existStock.getAvailableQty().add(item.getQuantity());
                
                existStock.setQuantity(newQty);
                existStock.setAvailableQty(newAvailQty);
                stockMapper.updateWmStock(existStock);
            } else {
                // 库存记录不存在，创建新记录
                WmStock newStock = new WmStock();
                newStock.setWarehouseId(recpt.getWarehouseId());
                newStock.setWarehouseName(recpt.getWarehouseName());
                newStock.setItemId(item.getItemId2());
                newStock.setItemCode(item.getItemCode());
                newStock.setItemName(item.getItemName());
                newStock.setBatchCode(item.getBatchCode());
                newStock.setQuantity(item.getQuantity());
                newStock.setAvailableQty(item.getQuantity());
                newStock.setLockedQty(BigDecimal.ZERO);
                newStock.setUnit(item.getUnit());
                
                stockMapper.insertWmStock(newStock);
            }
        }
    }
}
