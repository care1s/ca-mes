package com.carels.mes.module.sale.mapper;

import com.carels.mes.module.sale.domain.SaleReconciliation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售对账Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface SaleReconciliationMapper {

    /**
     * 查询销售对账列表
     */
    List<SaleReconciliation> selectSaleReconciliationList(SaleReconciliation reconciliation);

    /**
     * 根据ID查询销售对账
     */
    SaleReconciliation selectSaleReconciliationById(Long reconId);

    /**
     * 新增销售对账
     */
    int insertSaleReconciliation(SaleReconciliation reconciliation);

    /**
     * 修改销售对账
     */
    int updateSaleReconciliation(SaleReconciliation reconciliation);

    /**
     * 修改对账状态
     */
    int updateStatus(@Param("reconId") Long reconId, @Param("status") String status);

    /**
     * 删除销售对账
     */
    int deleteSaleReconciliationById(Long reconId);

    /**
     * 批量删除销售对账
     */
    int deleteSaleReconciliationByIds(Long[] reconIds);
}
