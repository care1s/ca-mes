package com.carels.mes.module.qc.service;

import com.carels.mes.module.qc.domain.QcRecord;

import java.util.List;
import java.util.Map;

/**
 * 检验记录Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface IQcRecordService {

    /**
     * 查询检验记录列表
     */
    List<QcRecord> selectQcRecordList(QcRecord record);

    /**
     * 根据ID查询检验记录
     */
    QcRecord selectQcRecordById(Long recordId);

    /**
     * 新增检验记录
     */
    int insertQcRecord(QcRecord record);

    /**
     * 修改检验记录
     */
    int updateQcRecord(QcRecord record);

    /**
     * 删除检验记录
     */
    int deleteQcRecordById(Long recordId);

    /**
     * 开始检验
     */
    int startInspect(Long recordId);

    /**
     * 提交检验结果
     */
    Map<String, Object> submitResult(Long recordId, Double qualifiedQty, Double defectiveQty,
                                      String defectDesc, String defectReason, String handleMethod);

    /**
     * 扫码查询检验任务
     */
    Map<String, Object> scanQcTask(String barcode, String qcType);

    /**
     * 获取待检验列表
     */
    List<Map<String, Object>> getPendingQcList(String qcType, Long inspectorId);

    /**
     * 获取我的检验列表
     */
    List<Map<String, Object>> getMyQcList(Long inspectorId);
}
