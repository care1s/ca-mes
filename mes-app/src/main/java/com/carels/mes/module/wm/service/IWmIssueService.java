package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmIssue;

import java.util.List;

/**
 * 出库管理Service接口
 */
public interface IWmIssueService {

    List<WmIssue> selectWmIssueList(WmIssue issue);

    WmIssue selectWmIssueById(Long issueId);

    int insertWmIssue(WmIssue issue);

    int updateWmIssue(WmIssue issue);

    int deleteWmIssueById(Long issueId);

    void updateStatus(Long issueId, String status);

    void confirmIssue(Long issueId);

    /**
     * 从销售订单生成出库单
     */
    WmIssue createIssueFromSalesOrder(Long salesOrderId, Long warehouseId);
}
