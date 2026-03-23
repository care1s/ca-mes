package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.sale.domain.SaleOrder;
import com.carels.mes.module.sale.domain.SaleOrderItem;
import com.carels.mes.module.sale.service.ISaleOrderService;
import com.carels.mes.module.wm.domain.WmIssue;
import com.carels.mes.module.wm.domain.WmIssueItem;
import com.carels.mes.module.wm.domain.WmStock;
import com.carels.mes.module.wm.mapper.WmIssueMapper;
import com.carels.mes.module.wm.mapper.WmStockMapper;
import com.carels.mes.module.wm.service.IWmIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WmIssueServiceImpl implements IWmIssueService {

    @Autowired
    private WmIssueMapper issueMapper;

    @Autowired
    private WmStockMapper stockMapper;

    @Autowired
    private ISaleOrderService saleOrderService;

    @Override
    public List<WmIssue> selectWmIssueList(WmIssue issue) {
        return issueMapper.selectWmIssueList(issue);
    }

    @Override
    public WmIssue selectWmIssueById(Long issueId) {
        return issueMapper.selectWmIssueById(issueId);
    }

    @Override
    @Transactional
    public int insertWmIssue(WmIssue issue) {
        // 生成出库单号
        if (issue.getIssueNo() == null || issue.getIssueNo().isEmpty()) {
            issue.setIssueNo("ISSUE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        // 设置默认状态
        if (issue.getStatus() == null || issue.getStatus().isEmpty()) {
            issue.setStatus("PENDING");
        }
        int rows = issueMapper.insertWmIssue(issue);
        // 插入明细
        if (issue.getItems() != null && !issue.getItems().isEmpty()) {
            for (WmIssueItem item : issue.getItems()) {
                item.setIssueId(issue.getId());
                issueMapper.insertWmIssueItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateWmIssue(WmIssue issue) {
        int rows = issueMapper.updateWmIssue(issue);
        // 更新明细：先删除再插入
        if (issue.getItems() != null) {
            issueMapper.deleteWmIssueItemsByIssueId(issue.getId());
            for (WmIssueItem item : issue.getItems()) {
                item.setIssueId(issue.getId());
                issueMapper.insertWmIssueItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteWmIssueById(Long issueId) {
        // 先删除明细
        issueMapper.deleteWmIssueItemsByIssueId(issueId);
        return issueMapper.deleteWmIssueById(issueId);
    }

    @Override
    public void updateStatus(Long issueId, String status) {
        issueMapper.updateStatus(issueId, status);
    }

    @Override
    @Transactional
    public void confirmIssue(Long issueId) {
        // 1. 更新出库单状态为已确认
        issueMapper.updateStatus(issueId, "CONFIRMED");
        
        // 2. 获取出库单详情（包含明细）
        WmIssue issue = issueMapper.selectWmIssueById(issueId);
        if (issue == null || issue.getItems() == null || issue.getItems().isEmpty()) {
            return;
        }
        
        // 3. 遍历明细，扣减库存
        for (WmIssueItem item : issue.getItems()) {
            // 检查库存记录是否存在（使用 itemId2 作为物料ID）
            WmStock stockQuery = new WmStock();
            stockQuery.setWarehouseId(issue.getWarehouseId());
            stockQuery.setItemId(item.getItemId2());
            stockQuery.setBatchCode(item.getBatchCode());
            
            List<WmStock> existStocks = stockMapper.selectWmStockList(stockQuery);
            
            if (existStocks != null && !existStocks.isEmpty()) {
                // 库存记录存在，扣减数量
                WmStock existStock = existStocks.get(0);
                BigDecimal newQty = existStock.getQuantity().subtract(item.getQuantity());
                BigDecimal newAvailQty = existStock.getAvailableQty().subtract(item.getQuantity());
                
                // 确保库存不为负数
                if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                    newQty = BigDecimal.ZERO;
                }
                if (newAvailQty.compareTo(BigDecimal.ZERO) < 0) {
                    newAvailQty = BigDecimal.ZERO;
                }
                
                existStock.setQuantity(newQty);
                existStock.setAvailableQty(newAvailQty);
                stockMapper.updateWmStock(existStock);
            }
            // 如果库存记录不存在，不处理（可能是数据不一致）
        }
    }

    @Override
    @Transactional
    public WmIssue createIssueFromSalesOrder(Long salesOrderId, Long warehouseId) {
        // 1. 获取销售订单详情
        SaleOrder saleOrder = saleOrderService.selectSaleOrderById(salesOrderId);
        if (saleOrder == null) {
            throw new RuntimeException("销售订单不存在");
        }
        if (saleOrder.getItems() == null || saleOrder.getItems().isEmpty()) {
            throw new RuntimeException("销售订单没有明细");
        }

        // 2. 创建出库单
        WmIssue issue = new WmIssue();
        issue.setIssueNo("ISSUE-SO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        issue.setIssueType("SALES"); // 销售出库
        issue.setTargetType("SALES_ORDER");
        issue.setTargetId(salesOrderId);
        issue.setTargetNo(saleOrder.getOrderNo());
        issue.setWarehouseId(warehouseId);
        issue.setStatus("PENDING");
        issue.setIssueDate(new Date());
        issue.setSalesOrderId(salesOrderId);
        issue.setSalesOrderNo(saleOrder.getOrderNo());
        issue.setClientId(saleOrder.getClientId());
        issue.setClientName(saleOrder.getClientName());
        issue.setRemark("由销售订单【" + saleOrder.getOrderNo() + "】生成");

        // 3. 创建出库明细
        List<WmIssueItem> issueItems = new ArrayList<>();
        for (SaleOrderItem saleItem : saleOrder.getItems()) {
            // 只出库剩余数量大于0的物料
            if (saleItem.getRemainQty() != null && saleItem.getRemainQty().compareTo(BigDecimal.ZERO) > 0) {
                WmIssueItem issueItem = new WmIssueItem();
                issueItem.setItemId2(saleItem.getItemId2());
                issueItem.setItemCode(saleItem.getItemCode());
                issueItem.setItemName(saleItem.getItemName());
                issueItem.setBatchCode(""); // 批次号可为空
                issueItem.setQuantity(saleItem.getRemainQty()); // 出库剩余数量
                issueItem.setUnit(saleItem.getUnit());
                issueItem.setRemark("销售订单明细");
                issueItems.add(issueItem);
            }
        }

        if (issueItems.isEmpty()) {
            throw new RuntimeException("销售订单所有物料已出库完毕");
        }

        issue.setItems(issueItems);

        // 4. 保存出库单
        insertWmIssue(issue);

        // 5. 更新销售订单状态为"部分出库"
        saleOrderService.updateSaleOrder(saleOrder);

        return issue;
    }
}
