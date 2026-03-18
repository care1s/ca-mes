package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurOrder;

import java.util.List;

public interface IPurOrderService {
    
    List<PurOrder> selectPurOrderList(PurOrder purOrder);
    
    PurOrder selectPurOrderById(Long orderId);
    
    int insertPurOrder(PurOrder purOrder);
    
    int updatePurOrder(PurOrder purOrder);
    
    int deletePurOrderById(Long orderId);
}
