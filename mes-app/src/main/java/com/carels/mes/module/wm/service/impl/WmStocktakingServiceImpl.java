package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmStocktaking;
import com.carels.mes.module.wm.mapper.WmStocktakingMapper;
import com.carels.mes.module.wm.service.IWmStocktakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存盘点Service实现
 */
@Service
public class WmStocktakingServiceImpl implements IWmStocktakingService {
    
    @Autowired
    private WmStocktakingMapper stocktakingMapper;
    
    @Override
    public List<WmStocktaking> selectWmStocktakingList(WmStocktaking stocktaking) {
        return stocktakingMapper.selectWmStocktakingList(stocktaking);
    }
    
    @Override
    public WmStocktaking selectWmStocktakingById(Long stocktakingId) {
        return stocktakingMapper.selectWmStocktakingById(stocktakingId);
    }
    
    @Override
    public int insertWmStocktaking(WmStocktaking stocktaking) {
        stocktaking.setStatus(0);
        stocktaking.setCreateTime(LocalDateTime.now());
        stocktaking.setUpdateTime(LocalDateTime.now());
        return stocktakingMapper.insertWmStocktaking(stocktaking);
    }
    
    @Override
    public int updateWmStocktaking(WmStocktaking stocktaking) {
        stocktaking.setUpdateTime(LocalDateTime.now());
        return stocktakingMapper.updateWmStocktaking(stocktaking);
    }
    
    @Override
    public int deleteWmStocktakingById(Long stocktakingId) {
        return stocktakingMapper.deleteWmStocktakingById(stocktakingId);
    }
    
    @Override
    public void startStocktaking(Long stocktakingId) {
        stocktakingMapper.updateStatus(stocktakingId, 1);
    }
    
    @Override
    public void completeStocktaking(Long stocktakingId) {
        stocktakingMapper.updateStatus(stocktakingId, 2);
    }
    
    @Override
    public void cancelStocktaking(Long stocktakingId) {
        stocktakingMapper.updateStatus(stocktakingId, 3);
    }
}
