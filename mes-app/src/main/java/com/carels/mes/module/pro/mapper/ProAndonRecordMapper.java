package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProAndonRecord;
import java.util.List;

/**
 * 安东异常记录Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ProAndonRecordMapper {

    /**
     * 查询异常记录列表
     */
    List<ProAndonRecord> selectProAndonRecordList(ProAndonRecord record);

    /**
     * 根据ID查询异常记录
     */
    ProAndonRecord selectProAndonRecordById(Long recordId);

    /**
     * 新增异常记录
     */
    int insertProAndonRecord(ProAndonRecord record);

    /**
     * 修改异常记录
     */
    int updateProAndonRecord(ProAndonRecord record);

    /**
     * 删除异常记录
     */
    int deleteProAndonRecordById(Long recordId);

    /**
     * 查询超时未处理的异常记录
     */
    List<ProAndonRecord> selectTimeoutRecords();
}
