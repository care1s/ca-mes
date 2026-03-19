package com.carels.mes.module.wm.service.impl;

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
import java.util.List;
import java.util.UUID;

@Service
public class WmIssueServiceImpl implements IWmIssueService {

    @Autowired
    private WmIssueMapper issueMapper;

    @Autowired
    private WmStockMapper stockMapper;

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
}
