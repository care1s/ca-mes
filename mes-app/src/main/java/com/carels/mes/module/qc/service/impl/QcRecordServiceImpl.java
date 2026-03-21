package com.carels.mes.module.qc.service.impl;

import com.carels.mes.module.qc.domain.QcRecord;
import com.carels.mes.module.qc.mapper.QcRecordMapper;
import com.carels.mes.module.qc.service.IQcRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检验记录Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Service
public class QcRecordServiceImpl implements IQcRecordService {

    @Autowired
    private QcRecordMapper qcRecordMapper;

    @Override
    public List<QcRecord> selectQcRecordList(QcRecord record) {
        return qcRecordMapper.selectQcRecordList(record);
    }

    @Override
    public QcRecord selectQcRecordById(Long recordId) {
        return qcRecordMapper.selectQcRecordById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertQcRecord(QcRecord record) {
        // 生成检验单号
        record.setRecordCode(generateRecordCode(record.getQcType()));
        record.setStatus("PENDING");
        return qcRecordMapper.insertQcRecord(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateQcRecord(QcRecord record) {
        return qcRecordMapper.updateQcRecord(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteQcRecordById(Long recordId) {
        return qcRecordMapper.deleteQcRecordById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int startInspect(Long recordId) {
        return qcRecordMapper.updateStatus(recordId, "PROCESSING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitResult(Long recordId, Double qualifiedQty, Double defectiveQty,
                                             String defectDesc, String defectReason, String handleMethod) {
        QcRecord record = qcRecordMapper.selectQcRecordById(recordId);
        if (record == null) {
            throw new RuntimeException("检验记录不存在");
        }

        // 判断检验结果
        String result = (defectiveQty != null && defectiveQty > 0) ? "FAIL" : "PASS";

        // 更新检验结果
        qcRecordMapper.updateResult(recordId, result, qualifiedQty, defectiveQty);

        // 更新其他字段
        QcRecord updateRecord = new QcRecord();
        updateRecord.setRecordId(recordId);
        updateRecord.setDefectDesc(defectDesc);
        updateRecord.setDefectReason(defectReason);
        updateRecord.setHandleMethod(handleMethod);
        qcRecordMapper.updateQcRecord(updateRecord);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("recordId", recordId);
        resultMap.put("result", result);
        resultMap.put("qualifiedQty", qualifiedQty);
        resultMap.put("defectiveQty", defectiveQty);
        resultMap.put("status", "COMPLETED");

        return resultMap;
    }

    @Override
    public Map<String, Object> scanQcTask(String barcode, String qcType) {
        return qcRecordMapper.selectQcTaskByBarcode(barcode, qcType);
    }

    @Override
    public List<Map<String, Object>> getPendingQcList(String qcType, Long inspectorId) {
        List<Map<String, Object>> list = qcRecordMapper.selectPendingQcList(qcType, inspectorId);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMyQcList(Long inspectorId) {
        List<Map<String, Object>> list = qcRecordMapper.selectMyQcList(inspectorId);
        return list != null ? list : new java.util.ArrayList<>();
    }

    /**
     * 生成检验单号
     */
    private String generateRecordCode(String qcType) {
        String prefix = qcType != null ? qcType : "QC";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String randomStr = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + randomStr;
    }
}
