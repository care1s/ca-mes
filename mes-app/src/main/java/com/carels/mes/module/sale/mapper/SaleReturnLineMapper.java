package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleReturnLine;

import java.util.List;

/**
 * 销售退货单明细Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface SaleReturnLineMapper {

    /**
     * 根据退货单ID查询明细列表
     */
    List<SaleReturnLine> selectLinesByReturnId(Long returnId);

    /**
     * 批量新增退货明细
     */
    int batchInsertLines(List<SaleReturnLine> lines);

    /**
     * 删除退货明细
     */
    int deleteLinesByReturnId(Long returnId);
}
