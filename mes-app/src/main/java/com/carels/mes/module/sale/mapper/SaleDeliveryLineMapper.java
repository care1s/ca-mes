package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleDeliveryLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售出库单明细Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface SaleDeliveryLineMapper {

    /**
     * 根据出库单ID查询明细列表
     */
    List<SaleDeliveryLine> selectLinesByDeliveryId(Long deliveryId);

    /**
     * 根据明细ID查询
     */
    SaleDeliveryLine selectLineById(Long lineId);

    /**
     * 批量新增出库明细
     */
    int batchInsertLines(List<SaleDeliveryLine> lines);

    /**
     * 修改出库明细
     */
    int updateLine(SaleDeliveryLine line);

    /**
     * 删除出库明细
     */
    int deleteLineById(Long lineId);

    /**
     * 根据出库单ID删除所有明细
     */
    int deleteLinesByDeliveryId(Long deliveryId);

    /**
     * 批量删除明细
     */
    int deleteLinesByIds(Long[] lineIds);
}
