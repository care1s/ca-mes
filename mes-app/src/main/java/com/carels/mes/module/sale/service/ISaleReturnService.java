package com.carels.mes.module.sale.service;

import com.carels.mes.module.sale.domain.SaleReturn;

import java.util.List;

/**
 * 销售退货单Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ISaleReturnService {

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
     * 提交退货单
     */
    int submitReturn(Long returnId);

    /**
     * 审核退货单
     */
    int auditReturn(Long returnId);

    /**
     * 入库
     */
    int receiveReturn(Long returnId);

    /**
     * 删除销售退货单
     */
    int deleteSaleReturnById(Long returnId);

    /**
     * 批量删除销售退货单
     */
    int deleteSaleReturnByIds(Long[] returnIds);
}
