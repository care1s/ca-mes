package com.carels.mes.module.sale.service;

import com.carels.mes.module.sale.domain.SaleDelivery;

import java.util.List;

/**
 * 销售出库单Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ISaleDeliveryService {

    /**
     * 查询销售出库单列表
     */
    List<SaleDelivery> selectSaleDeliveryList(SaleDelivery delivery);

    /**
     * 根据ID查询销售出库单
     */
    SaleDelivery selectSaleDeliveryById(Long deliveryId);

    /**
     * 新增销售出库单
     */
    int insertSaleDelivery(SaleDelivery delivery);

    /**
     * 修改销售出库单
     */
    int updateSaleDelivery(SaleDelivery delivery);

    /**
     * 提交出库单
     */
    int submitDelivery(Long deliveryId);

    /**
     * 审核出库单
     */
    int auditDelivery(Long deliveryId);

    /**
     * 发货
     */
    int shipDelivery(Long deliveryId, String trackingNo, String logisticsCompany);

    /**
     * 完成出库单
     */
    int completeDelivery(Long deliveryId);

    /**
     * 删除销售出库单
     */
    int deleteSaleDeliveryById(Long deliveryId);

    /**
     * 批量删除销售出库单
     */
    int deleteSaleDeliveryByIds(Long[] deliveryIds);
}
