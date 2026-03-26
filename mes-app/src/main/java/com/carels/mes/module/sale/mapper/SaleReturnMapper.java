package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleReturn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售退货单Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface SaleReturnMapper {

    /**
     * 查询销售退货单列表
     */
    List<SaleReturn> selectSaleReturnList(SaleReturn saleReturn);

    /**
     * 根据ID查询销售退货单
     */
    SaleReturn selectSaleReturnById(Long returnId);

    /**
     * 新增销售退货单
     */
    int insertSaleReturn(SaleReturn saleReturn);

    /**
     * 修改销售退货单
     */
    int updateSaleReturn(SaleReturn saleReturn);

    /**
     * 修改退货单状态
     */
    int updateStatus(@Param("returnId") Long returnId, @Param("status") String status);

    /**
     * 删除销售退货单
     */
    int deleteSaleReturnById(Long returnId);

    /**
     * 批量删除销售退货单
     */
    int deleteSaleReturnByIds(Long[] returnIds);
}
