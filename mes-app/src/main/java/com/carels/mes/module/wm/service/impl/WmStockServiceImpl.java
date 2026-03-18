package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmStock;
import com.carels.mes.module.wm.mapper.WmStockMapper;
import com.carels.mes.module.wm.service.IWmStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WmStockServiceImpl implements IWmStockService {
    
    @Autowired
    private WmStockMapper stockMapper;
    
    @Override
    public List<WmStock> selectWmStockList(WmStock stock) {
        return stockMapper.selectWmStockList(stock);
    }
    
    @Override
    public WmStock selectWmStockById(Long id) {
        return stockMapper.selectWmStockById(id);
    }
    
    @Override
    public int insertWmStock(WmStock stock) {
        return stockMapper.insertWmStock(stock);
    }
    
    @Override
    public int updateWmStock(WmStock stock) {
        return stockMapper.updateWmStock(stock);
    }
    
    @Override
    public int deleteWmStockById(Long id) {
        return stockMapper.deleteWmStockById(id);
    }
}
