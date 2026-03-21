package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurOrder;

import java.util.List;

public interface IPurOrderService {
    
    List<PurOrder> selectPurOrderList(PurOrder purOrder);
    
    PurOrder selectPurOrderById(Long orderId);
    
    int insertPurOrder(PurOrder purOrder);
    
    int updatePurOrder(PurOrder purOrder);
    
    int deletePurOrderById(Long orderId);

    /**
     * 根据采购申请生成采购订单
     * @param requestId 采购申请ID
     * @return 生成的订单ID
     */
    Long createOrderFromRequest(Long requestId);
}
