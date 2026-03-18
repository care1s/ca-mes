package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmIssue;
import com.carels.mes.module.wm.domain.WmIssueItem;
import com.carels.mes.module.wm.mapper.WmIssueMapper;
import com.carels.mes.module.wm.service.IWmIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WmIssueServiceImpl implements IWmIssueService {

    @Autowired
    private WmIssueMapper issueMapper;

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
    public void confirmIssue(Long issueId) {
        issueMapper.updateStatus(issueId, "CONFIRMED");
    }
}
