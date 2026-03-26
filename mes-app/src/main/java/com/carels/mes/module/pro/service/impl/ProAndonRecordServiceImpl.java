package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProAndonRecord;
import com.carels.mes.module.pro.domain.ProAndonType;
import com.carels.mes.module.pro.mapper.ProAndonRecordMapper;
import com.carels.mes.module.pro.mapper.ProAndonTypeMapper;
import com.carels.mes.module.pro.service.IProAndonRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 安东异常记录Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class ProAndonRecordServiceImpl implements IProAndonRecordService {

    @Autowired
    private ProAndonRecordMapper andonRecordMapper;

    @Autowired
    private ProAndonTypeMapper andonTypeMapper;

    @Override
    public List<ProAndonRecord> selectProAndonRecordList(ProAndonRecord record) {
        return andonRecordMapper.selectProAndonRecordList(record);
    }

    @Override
    public ProAndonRecord selectProAndonRecordById(Long recordId) {
        return andonRecordMapper.selectProAndonRecordById(recordId);
    }

    @Override
    public int reportAndon(ProAndonRecord record) {
        record.setStatus("PENDING");
        record.setReportTime(new Date());
        record.setEscalateLevel(1);

        // 计算超时时间
        ProAndonType type = andonTypeMapper.selectProAndonTypeById(record.getTypeId());
        if (type != null && type.getTimeoutMinutes() != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, type.getTimeoutMinutes());
            record.setTimeoutTime(cal.getTime());
        }

        return andonRecordMapper.insertProAndonRecord(record);
    }

    @Override
    public int acceptAndon(Long recordId, Long handlerId, String handlerName) {
        ProAndonRecord record = andonRecordMapper.selectProAndonRecordById(recordId);
        if (record != null) {
            record.setHandlerId(handlerId);
            record.setHandlerName(handlerName);
            record.setStatus("ACCEPTED");
            record.setAcceptTime(new Date());
            return andonRecordMapper.updateProAndonRecord(record);
        }
        return 0;
    }

    @Override
    public int handleAndon(Long recordId, String handleResult) {
        ProAndonRecord record = andonRecordMapper.selectProAndonRecordById(recordId);
        if (record != null) {
            record.setStatus("HANDLING");
            record.setHandleResult(handleResult);
            record.setHandleTime(new Date());
            return andonRecordMapper.updateProAndonRecord(record);
        }
        return 0;
    }

    @Override
    public int completeAndon(Long recordId) {
        ProAndonRecord record = andonRecordMapper.selectProAndonRecordById(recordId);
        if (record != null) {
            record.setStatus("COMPLETED");
            record.setCompleteTime(new Date());
            return andonRecordMapper.updateProAndonRecord(record);
        }
        return 0;
    }

    @Override
    public int closeAndon(Long recordId, String closeReason) {
        ProAndonRecord record = andonRecordMapper.selectProAndonRecordById(recordId);
        if (record != null) {
            record.setStatus("CLOSED");
            record.setCloseReason(closeReason);
            record.setCompleteTime(new Date());
            return andonRecordMapper.updateProAndonRecord(record);
        }
        return 0;
    }

    @Override
    public int escalateAndon(Long recordId, Integer escalateLevel) {
        ProAndonRecord record = andonRecordMapper.selectProAndonRecordById(recordId);
        if (record != null) {
            record.setEscalateLevel(escalateLevel);
            record.setStatus("ESCALATED");
            return andonRecordMapper.updateProAndonRecord(record);
        }
        return 0;
    }

    @Override
    public List<ProAndonRecord> selectTimeoutRecords() {
        return andonRecordMapper.selectTimeoutRecords();
    }

    @Override
    public void autoEscalateTimeoutRecords() {
        List<ProAndonRecord> timeoutRecords = selectTimeoutRecords();
        for (ProAndonRecord record : timeoutRecords) {
            Integer currentLevel = record.getEscalateLevel();
            if (currentLevel < 3) { // 最大升级级别为3
                escalateAndon(record.getRecordId(), currentLevel + 1);
            }
        }
    }
}
