package com.carels.mes.module.sale.service.impl;

import com.carels.mes.module.sale.domain.SaleDelivery;
import com.carels.mes.module.sale.domain.SaleDeliveryLine;
import com.carels.mes.module.sale.mapper.SaleDeliveryMapper;
import com.carels.mes.module.sale.mapper.SaleDeliveryLineMapper;
import com.carels.mes.module.sale.service.ISaleDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 销售出库单Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class SaleDeliveryServiceImpl implements ISaleDeliveryService {

    @Autowired
    private SaleDeliveryMapper deliveryMapper;

    @Autowired
    private SaleDeliveryLineMapper lineMapper;

    @Override
    public List<SaleDelivery> selectSaleDeliveryList(SaleDelivery delivery) {
        return deliveryMapper.selectSaleDeliveryList(delivery);
    }

    @Override
    public SaleDelivery selectSaleDeliveryById(Long deliveryId) {
        SaleDelivery delivery = deliveryMapper.selectSaleDeliveryById(deliveryId);
        if (delivery != null) {
            List<SaleDeliveryLine> lines = lineMapper.selectLinesByDeliveryId(deliveryId);
            delivery.setLines(lines);
        }
        return delivery;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSaleDelivery(SaleDelivery delivery) {
        delivery.setCreateTime(new Date());
        delivery.setCreateBy("admin");
        delivery.setStatus("0"); // 草稿状态
        delivery.setDeliveryNo(generateDeliveryNo());

        // 计算总金额和总数量
        calcTotal(delivery);

        int rows = deliveryMapper.insertSaleDelivery(delivery);

        // 插入明细
        if (rows > 0 && delivery.getLines() != null && !delivery.getLines().isEmpty()) {
            for (SaleDeliveryLine line : delivery.getLines()) {
                line.setDeliveryId(delivery.getDeliveryId());
            }
            lineMapper.batchInsertLines(delivery.getLines());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSaleDelivery(SaleDelivery delivery) {
        delivery.setUpdateTime(new Date());
        delivery.setUpdateBy("admin");

        // 计算总金额和总数量
        calcTotal(delivery);

        int rows = deliveryMapper.updateSaleDelivery(delivery);

        // 删除旧明细，插入新明细
        if (rows > 0 && delivery.getLines() != null) {
            lineMapper.deleteLinesByDeliveryId(delivery.getDeliveryId());
            for (SaleDeliveryLine line : delivery.getLines()) {
                line.setDeliveryId(delivery.getDeliveryId());
            }
            lineMapper.batchInsertLines(delivery.getLines());
        }
        return rows;
    }

    @Override
    public int submitDelivery(Long deliveryId) {
        return deliveryMapper.updateStatus(deliveryId, "1"); // 已提交
    }

    @Override
    public int auditDelivery(Long deliveryId) {
        return deliveryMapper.updateStatus(deliveryId, "2"); // 已审核
    }

    @Override
    public int shipDelivery(Long deliveryId, String trackingNo, String logisticsCompany) {
        SaleDelivery delivery = new SaleDelivery();
        delivery.setDeliveryId(deliveryId);
        delivery.setTrackingNo(trackingNo);
        delivery.setLogisticsCompany(logisticsCompany);
        delivery.setShipDate(new Date());
        delivery.setUpdateTime(new Date());
        delivery.setUpdateBy("admin");
        delivery.setStatus("3"); // 已发货
        return deliveryMapper.updateSaleDelivery(delivery);
    }

    @Override
    public int completeDelivery(Long deliveryId) {
        return deliveryMapper.updateStatus(deliveryId, "4"); // 已完成
    }

    @Override
    public int deleteSaleDeliveryById(Long deliveryId) {
        // 先删除明细
        lineMapper.deleteLinesByDeliveryId(deliveryId);
        return deliveryMapper.deleteSaleDeliveryById(deliveryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSaleDeliveryByIds(Long[] deliveryIds) {
        for (Long deliveryId : deliveryIds) {
            lineMapper.deleteLinesByDeliveryId(deliveryId);
        }
        return deliveryMapper.deleteSaleDeliveryByIds(deliveryIds);
    }

    /**
     * 生成出库单号
     */
    private String generateDeliveryNo() {
        String prefix = "CK";
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }

    /**
     * 计算总金额和总数量
     */
    private void calcTotal(SaleDelivery delivery) {
        if (delivery.getLines() == null || delivery.getLines().isEmpty()) {
            return;
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (SaleDeliveryLine line : delivery.getLines()) {
            // 计算金额
            if (line.getQuantity() != null && line.getPrice() != null) {
                line.setAmount(line.getQuantity().multiply(line.getPrice()));
            }
            if (line.getAmount() != null) {
                totalAmount = totalAmount.add(line.getAmount());
            }
            if (line.getQuantity() != null) {
                totalQuantity = totalQuantity.add(line.getQuantity());
            }
        }
        delivery.setTotalAmount(totalAmount);
        delivery.setTotalQuantity(totalQuantity);
    }
}
