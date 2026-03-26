package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProAndonRecord;
import java.util.List;

/**
 * 安东异常记录Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface IProAndonRecordService {

    /**
     * 查询异常记录列表
     */
    List<ProAndonRecord> selectProAndonRecordList(ProAndonRecord record);

    /**
     * 根据ID查询异常记录
     */
    ProAndonRecord selectProAndonRecordById(Long recordId);

    /**
     * 上报异常
     */
    int reportAndon(ProAndonRecord record);

    /**
     * 接单
     */
    int acceptAndon(Long recordId, Long handlerId, String handlerName);

    /**
     * 处理异常
     */
    int handleAndon(Long recordId, String handleResult);

    /**
     * 完成异常
     */
    int completeAndon(Long recordId);

    /**
     * 关闭异常
     */
    int closeAndon(Long recordId, String closeReason);

    /**
     * 升级异常
     */
    int escalateAndon(Long recordId, Integer escalateLevel);

    /**
     * 查询超时异常
     */
    List<ProAndonRecord> selectTimeoutRecords();

    /**
     * 自动升级超时异常
     */
    void autoEscalateTimeoutRecords();
}
