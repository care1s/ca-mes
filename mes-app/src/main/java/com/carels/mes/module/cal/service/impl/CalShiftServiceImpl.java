package com.carels.mes.module.cal.service.impl;

import com.carels.mes.module.cal.domain.CalShift;
import com.carels.mes.module.cal.mapper.CalShiftMapper;
import com.carels.mes.module.cal.service.ICalShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班次Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Service
public class CalShiftServiceImpl implements ICalShiftService {

    @Autowired
    private CalShiftMapper calShiftMapper;

    @Override
    public List<CalShift> selectCalShiftList(CalShift calShift) {
        return calShiftMapper.selectCalShiftList(calShift);
    }

    @Override
    public List<CalShift> selectAllActiveShifts() {
        return calShiftMapper.selectAllActiveShifts();
    }

    @Override
    public CalShift selectCalShiftById(Long shiftId) {
        return calShiftMapper.selectCalShiftById(shiftId);
    }

    @Override
    public int insertCalShift(CalShift calShift) {
        return calShiftMapper.insertCalShift(calShift);
    }

    @Override
    public int updateCalShift(CalShift calShift) {
        return calShiftMapper.updateCalShift(calShift);
    }

    @Override
    public int deleteCalShiftById(Long shiftId) {
        return calShiftMapper.deleteCalShiftById(shiftId);
    }

    @Override
    public int deleteCalShiftByIds(Long[] shiftIds) {
        return calShiftMapper.deleteCalShiftByIds(shiftIds);
    }

    @Override
    public boolean checkShiftCodeUnique(String shiftCode) {
        return calShiftMapper.checkShiftCodeUnique(shiftCode) == 0;
    }
}
