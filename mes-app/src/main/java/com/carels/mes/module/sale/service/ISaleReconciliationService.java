package com.carels.mes.module.sale.service;

import com.carels.mes.module.sale.domain.SaleReconciliation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售对账Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ISaleReconciliationService {

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
     * 确认对账单
     */
    int confirmReconciliation(Long reconId);

    /**
     * 开票
     */
    int invoiceReconciliation(Long reconId, String invoiceNo, Date invoiceDate);

    /**
     * 收款
     */
    int receivePayment(Long reconId, BigDecimal receivedAmount, Date receiveDate);

    /**
     * 删除销售对账
     */
    int deleteSaleReconciliationById(Long reconId);

    /**
     * 批量删除销售对账
     */
    int deleteSaleReconciliationByIds(Long[] reconIds);

    /**
     * 自动计算对账金额
     */
    void calcReconciliationAmount(Long reconId);
}
