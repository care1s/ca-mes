package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmIssue;
import com.carels.mes.module.wm.domain.WmIssueItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库管理Mapper接口
 */
public interface WmIssueMapper {

    List<WmIssue> selectWmIssueList(WmIssue issue);

    WmIssue selectWmIssueById(Long issueId);

    int insertWmIssue(WmIssue issue);

    int updateWmIssue(WmIssue issue);

    int deleteWmIssueById(Long issueId);

    int updateStatus(@Param("issueId") Long issueId, @Param("status") String status);

    // 明细操作
    List<WmIssueItem> selectWmIssueItemsByIssueId(Long issueId);

    int insertWmIssueItem(WmIssueItem item);

    int updateWmIssueItem(WmIssueItem item);

    int deleteWmIssueItemsByIssueId(Long issueId);

    int deleteWmIssueItemById(Long itemId);
}
