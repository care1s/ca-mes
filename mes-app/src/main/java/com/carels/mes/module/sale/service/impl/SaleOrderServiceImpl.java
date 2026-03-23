package com.carels.mes.module.sale.service.impl;

import com.carels.mes.module.sale.domain.SaleOrder;
import com.carels.mes.module.sale.domain.SaleOrderItem;
import com.carels.mes.module.sale.mapper.SaleOrderMapper;
import com.carels.mes.module.sale.service.ISaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 销售订单Service实现
 */
@Service
public class SaleOrderServiceImpl implements ISaleOrderService {

    @Autowired
    private SaleOrderMapper orderMapper;

    @Override
    public List<SaleOrder> selectSaleOrderList(SaleOrder order) {
        return orderMapper.selectSaleOrderList(order);
    }

    @Override
    public SaleOrder selectSaleOrderById(Long orderId) {
        SaleOrder order = orderMapper.selectSaleOrderById(orderId);
        if (order != null) {
            List<SaleOrderItem> items = orderMapper.selectItemsByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    @Override
    public SaleOrder selectSaleOrderByNo(String orderNo) {
        return orderMapper.selectSaleOrderByNo(orderNo);
    }

    @Override
    @Transactional
    public int insertSaleOrder(SaleOrder order) {
        // 生成订单号
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            order.setOrderNo(generateOrderNo());
        }
        order.setStatus(0);
        order.setDeliveryStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 计算金额
        calculateAmount(order);

        int result = orderMapper.insertSaleOrder(order);

        // 保存明细
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (SaleOrderItem item : order.getItems()) {
                item.setOrderId(order.getOrderId());
                item.setDeliveredQty(BigDecimal.ZERO);
                item.setRemainQty(item.getQuantity());
                item.setStatus(0);
                item.setCreateTime(LocalDateTime.now());
            }
            orderMapper.batchInsertItems(order.getItems());
        }

        return result;
    }

    @Override
    @Transactional
    public int updateSaleOrder(SaleOrder order) {
        order.setUpdateTime(LocalDateTime.now());

        // 重新计算金额
        calculateAmount(order);

        int result = orderMapper.updateSaleOrder(order);

        // 更新明细
        if (order.getItems() != null) {
            // 先删除旧明细
            orderMapper.deleteItemsByOrderId(order.getOrderId());
            // 插入新明细
            for (SaleOrderItem item : order.getItems()) {
                item.setOrderId(order.getOrderId());
                if (item.getDeliveredQty() == null) {
                    item.setDeliveredQty(BigDecimal.ZERO);
                }
                if (item.getRemainQty() == null) {
                    item.setRemainQty(item.getQuantity());
                }
                item.setCreateTime(LocalDateTime.now());
            }
            orderMapper.batchInsertItems(order.getItems());
        }

        return result;
    }

    @Override
    @Transactional
    public int deleteSaleOrderById(Long orderId) {
        // 先删除明细
        orderMapper.deleteItemsByOrderId(orderId);
        return orderMapper.deleteSaleOrderById(orderId);
    }

    @Override
    @Transactional
    public int deleteSaleOrderByIds(Long[] orderIds) {
        for (Long orderId : orderIds) {
            orderMapper.deleteItemsByOrderId(orderId);
        }
        return orderMapper.deleteSaleOrderByIds(orderIds);
    }

    @Override
    public void submitOrder(Long orderId) {
        orderMapper.updateStatus(orderId, 1);
    }

    @Override
    public void auditOrder(Long orderId) {
        orderMapper.updateStatus(orderId, 2);
    }

    @Override
    public void cancelOrder(Long orderId) {
        orderMapper.updateStatus(orderId, 6);
    }

    @Override
    public void completeOrder(Long orderId) {
        orderMapper.updateStatus(orderId, 5);
        orderMapper.updateDeliveryStatus(orderId, 2);
    }

    @Override
    public void relatePlan(Long orderId, Long planId) {
        orderMapper.relatePlan(orderId, planId);
        // 更新状态为生产中
        orderMapper.updateStatus(orderId, 3);
    }

    @Override
    public List<SaleOrder> selectAvailableOrdersForIssue() {
        return orderMapper.selectAvailableOrdersForIssue();
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String prefix = "SO";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String seq = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        return prefix + dateStr + seq;
    }

    /**
     * 计算订单金额
     */
    private void calculateAmount(SaleOrder order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SaleOrderItem item : order.getItems()) {
            // 计算明细总价
            if (item.getQuantity() != null && item.getUnitPrice() != null) {
                BigDecimal itemTotal = item.getQuantity().multiply(item.getUnitPrice());
                item.setTotalPrice(itemTotal);
                totalAmount = totalAmount.add(itemTotal);
            }
        }

        order.setTotalAmount(totalAmount);

        // 计算应付金额（总金额 + 税额 - 折扣）
        BigDecimal taxAmount = order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        order.setPayableAmount(totalAmount.add(taxAmount).subtract(discountAmount));
    }
}
