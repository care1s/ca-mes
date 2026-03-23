package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产报工Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface ProFeedbackMapper {

    /**
     * 查询报工列表
     */
    List<ProFeedback> selectProFeedbackList(ProFeedback feedback);

    /**
     * 根据ID查询报工
     */
    ProFeedback selectProFeedbackById(Long feedbackId);

    /**
     * 新增报工
     */
    int insertProFeedback(ProFeedback feedback);

    /**
     * 修改报工
     */
    int updateProFeedback(ProFeedback feedback);

    /**
     * 删除报工
     */
    int deleteProFeedbackById(Long feedbackId);

    /**
     * 批量删除报工
     */
    int deleteProFeedbackByIds(Long[] feedbackIds);

    /**
     * 根据任务ID查询报工列表
     */
    List<ProFeedback> selectProFeedbackByTaskId(Long taskId);

    /**
     * 审核报工
     */
    int approveFeedback(@Param("feedbackId") Long feedbackId, @Param("status") String status);
}
