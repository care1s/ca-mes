package com.carels.mes.module.sale.service;

import com.carels.mes.module.sale.domain.SaleOrder;

import java.util.List;

/**
 * 销售订单Service接口
 */
public interface ISaleOrderService {

    /**
     * 查询订单列表
     */
    List<SaleOrder> selectSaleOrderList(SaleOrder order);

    /**
     * 根据ID查询订单
     */
    SaleOrder selectSaleOrderById(Long orderId);

    /**
     * 根据单号查询订单
     */
    SaleOrder selectSaleOrderByNo(String orderNo);

    /**
     * 新增订单
     */
    int insertSaleOrder(SaleOrder order);

    /**
     * 修改订单
     */
    int updateSaleOrder(SaleOrder order);

    /**
     * 删除订单
     */
    int deleteSaleOrderById(Long orderId);

    /**
     * 批量删除订单
     */
    int deleteSaleOrderByIds(Long[] orderIds);

    /**
     * 提交订单
     */
    void submitOrder(Long orderId);

    /**
     * 审核订单
     */
    void auditOrder(Long orderId);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);

    /**
     * 完成订单
     */
    void completeOrder(Long orderId);

    /**
     * 关联生产计划
     */
    void relatePlan(Long orderId, Long planId);

    /**
     * 查询可出库的订单列表
     */
    List<SaleOrder> selectAvailableOrdersForIssue();
}
