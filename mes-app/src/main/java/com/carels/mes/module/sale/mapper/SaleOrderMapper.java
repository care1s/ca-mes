package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleOrder;
import com.carels.mes.module.sale.domain.SaleOrderItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 销售订单Mapper接口
 */
public interface SaleOrderMapper {

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
     * 更新订单状态
     */
    int updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /**
     * 更新交货状态
     */
    int updateDeliveryStatus(@Param("orderId") Long orderId, @Param("deliveryStatus") Integer deliveryStatus);

    /**
     * 关联生产计划
     */
    int relatePlan(@Param("orderId") Long orderId, @Param("planId") Long planId);

    // ==================== 明细操作 ====================

    /**
     * 根据订单ID查询明细列表
     */
    List<SaleOrderItem> selectItemsByOrderId(Long orderId);

    /**
     * 新增订单明细
     */
    int insertSaleOrderItem(SaleOrderItem item);

    /**
     * 批量新增订单明细
     */
    int batchInsertItems(@Param("items") List<SaleOrderItem> items);

    /**
     * 更新订单明细
     */
    int updateSaleOrderItem(SaleOrderItem item);

    /**
     * 删除订单明细
     */
    int deleteItemsByOrderId(Long orderId);

    /**
     * 更新已发货数量
     */
    int updateDeliveredQty(@Param("itemId") Long itemId, @Param("qty") BigDecimal qty);

    /**
     * 查询可出库的订单列表（已审核但未完成）
     */
    List<SaleOrder> selectAvailableOrdersForIssue();
}
