package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleDelivery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售出库单Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface SaleDeliveryMapper {

    /**
     * 查询销售出库单列表
     */
    List<SaleDelivery> selectSaleDeliveryList(SaleDelivery delivery);

    /**
     * 根据ID查询销售出库单
     */
    SaleDelivery selectSaleDeliveryById(Long deliveryId);

    /**
     * 根据出库单号查询
     */
    SaleDelivery selectSaleDeliveryByNo(String deliveryNo);

    /**
     * 新增销售出库单
     */
    int insertSaleDelivery(SaleDelivery delivery);

    /**
     * 修改销售出库单
     */
    int updateSaleDelivery(SaleDelivery delivery);

    /**
     * 修改出库单状态
     */
    int updateStatus(@Param("deliveryId") Long deliveryId, @Param("status") String status);

    /**
     * 删除销售出库单
     */
    int deleteSaleDeliveryById(Long deliveryId);

    /**
     * 批量删除销售出库单
     */
    int deleteSaleDeliveryByIds(Long[] deliveryIds);
}
