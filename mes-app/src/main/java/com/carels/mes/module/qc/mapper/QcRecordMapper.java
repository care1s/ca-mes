package com.carels.mes.module.qc.mapper;

import com.carels.mes.module.qc.domain.QcRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检验记录Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface QcRecordMapper {

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
     * 更新检验状态
     */
    int updateStatus(@Param("recordId") Long recordId, @Param("status") String status);

    /**
     * 更新检验结果
     */
    int updateResult(@Param("recordId") Long recordId, @Param("result") String result,
                     @Param("qualifiedQty") Double qualifiedQty, @Param("defectiveQty") Double defectiveQty);

    /**
     * 扫码查询检验任务
     */
    Map<String, Object> selectQcTaskByBarcode(@Param("barcode") String barcode, @Param("qcType") String qcType);

    /**
     * 获取待检验列表（移动端）
     */
    List<Map<String, Object>> selectPendingQcList(@Param("qcType") String qcType, @Param("inspectorId") Long inspectorId);

    /**
     * 获取我的检验列表（移动端）
     */
    List<Map<String, Object>> selectMyQcList(@Param("inspectorId") Long inspectorId);
}
