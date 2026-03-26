package com.carels.mes.module.sale.service.impl;



import com.carels.mes.module.sale.domain.SaleReconciliation;
import com.carels.mes.module.sale.mapper.SaleReconciliationMapper;
import com.carels.mes.module.sale.service.ISaleReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售对账Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class SaleReconciliationServiceImpl implements ISaleReconciliationService {

    @Autowired
    private SaleReconciliationMapper reconciliationMapper;

    @Override
    public List<SaleReconciliation> selectSaleReconciliationList(SaleReconciliation reconciliation) {
        return reconciliationMapper.selectSaleReconciliationList(reconciliation);
    }

    @Override
    public SaleReconciliation selectSaleReconciliationById(Long reconId) {
        return reconciliationMapper.selectSaleReconciliationById(reconId);
    }

    @Override
    public int insertSaleReconciliation(SaleReconciliation reconciliation) {
        reconciliation.setCreateTime(new Date());
        reconciliation.setCreateBy("admin");
        reconciliation.setStatus("0"); // 草稿状态
        reconciliation.setReconNo(generateReconNo());
        return reconciliationMapper.insertSaleReconciliation(reconciliation);
    }

    @Override
    public int updateSaleReconciliation(SaleReconciliation reconciliation) {
        reconciliation.setUpdateTime(new Date());
        reconciliation.setUpdateBy("admin");
        return reconciliationMapper.updateSaleReconciliation(reconciliation);
    }

    @Override
    public int confirmReconciliation(Long reconId) {
        // 先计算金额
        calcReconciliationAmount(reconId);
        return reconciliationMapper.updateStatus(reconId, "1"); // 已确认
    }

    @Override
    public int invoiceReconciliation(Long reconId, String invoiceNo, Date invoiceDate) {
        SaleReconciliation reconciliation = new SaleReconciliation();
        reconciliation.setReconId(reconId);
        reconciliation.setInvoiceNo(invoiceNo);
        reconciliation.setInvoiceDate(invoiceDate);
        reconciliation.setUpdateTime(new Date());
        reconciliation.setUpdateBy("admin");
        reconciliation.setStatus("2"); // 已开票
        return reconciliationMapper.updateSaleReconciliation(reconciliation);
    }

    @Override
    public int receivePayment(Long reconId, BigDecimal receivedAmount, Date receiveDate) {
        SaleReconciliation reconciliation = new SaleReconciliation();
        reconciliation.setReconId(reconId);
        reconciliation.setReceivedAmount(receivedAmount);
        reconciliation.setReceiveDate(receiveDate);
        reconciliation.setUpdateTime(new Date());
        reconciliation.setUpdateBy("admin");
        reconciliation.setStatus("3"); // 已收款
        return reconciliationMapper.updateSaleReconciliation(reconciliation);
    }

    @Override
    public int deleteSaleReconciliationById(Long reconId) {
        return reconciliationMapper.deleteSaleReconciliationById(reconId);
    }

    @Override
    public int deleteSaleReconciliationByIds(Long[] reconIds) {
        return reconciliationMapper.deleteSaleReconciliationByIds(reconIds);
    }

    @Override
    public void calcReconciliationAmount(Long reconId) {
        SaleReconciliation reconciliation = reconciliationMapper.selectSaleReconciliationById(reconId);
        if (reconciliation == null) {
            return;
        }
        // 计算应收金额 = 出库金额 - 退货金额
        BigDecimal deliveryAmount = reconciliation.getDeliveryAmount() != null ? reconciliation.getDeliveryAmount() : BigDecimal.ZERO;
        BigDecimal returnAmount = reconciliation.getReturnAmount() != null ? reconciliation.getReturnAmount() : BigDecimal.ZERO;
        BigDecimal receivableAmount = deliveryAmount.subtract(returnAmount);
        reconciliation.setReceivableAmount(receivableAmount);
        reconciliationMapper.updateSaleReconciliation(reconciliation);
    }

    /**
     * 生成对账单号
     */
    private String generateReconNo() {
        String prefix = "DZ";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }
}
