package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface PurOrderMapper {

    /**
     * 查询采购订单列表
     */
    List<PurOrder> selectPurOrderList(PurOrder purOrder);

    /**
     * 根据ID查询采购订单
     */
    PurOrder selectPurOrderById(Long orderId);

    /**
     * 根据单号查询采购订单
     */
    PurOrder selectPurOrderByCode(String orderCode);

    /**
     * 查询供应商的未完成订单
     */
    List<PurOrder> selectPendingOrdersByVendor(Long vendorId);

    /**
     * 新增采购订单
     */
    int insertPurOrder(PurOrder purOrder);

    /**
     * 修改采购订单
     */
    int updatePurOrder(PurOrder purOrder);

    /**
     * 删除采购订单
     */
    int deletePurOrderById(Long orderId);

    /**
     * 批量删除采购订单
     */
    int deletePurOrderByIds(Long[] orderIds);

    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);
}
