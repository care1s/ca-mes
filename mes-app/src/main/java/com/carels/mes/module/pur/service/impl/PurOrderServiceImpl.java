package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurOrder;
import com.carels.mes.module.pur.mapper.PurOrderMapper;
import com.carels.mes.module.pur.service.IPurOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurOrderServiceImpl implements IPurOrderService {
    
    @Autowired
    private PurOrderMapper purOrderMapper;
    
    @Override
    public List<PurOrder> selectPurOrderList(PurOrder purOrder) {
        return purOrderMapper.selectPurOrderList(purOrder);
    }
    
    @Override
    public PurOrder selectPurOrderById(Long orderId) {
        return purOrderMapper.selectPurOrderById(orderId);
    }
    
    @Override
    public int insertPurOrder(PurOrder purOrder) {
        purOrder.setOrderStatus("DRAFT");
        return purOrderMapper.insertPurOrder(purOrder);
    }
    
    @Override
    public int updatePurOrder(PurOrder purOrder) {
        return purOrderMapper.updatePurOrder(purOrder);
    }
    
    @Override
    public int deletePurOrderById(Long orderId) {
        return purOrderMapper.deletePurOrderById(orderId);
    }
}
