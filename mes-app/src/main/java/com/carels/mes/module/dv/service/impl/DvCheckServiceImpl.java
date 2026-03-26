package com.carels.mes.module.dv.service.impl;

import com.carels.mes.module.dv.domain.DvCheck;
import com.carels.mes.module.dv.mapper.DvCheckMapper;
import com.carels.mes.module.dv.service.IDvCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 设备点检Service实现
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class DvCheckServiceImpl implements IDvCheckService {

    @Autowired
    private DvCheckMapper checkMapper;

    @Override
    public List<DvCheck> selectDvCheckList(DvCheck check) {
        return checkMapper.selectDvCheckList(check);
    }

    @Override
    public DvCheck selectDvCheckById(Long id) {
        return checkMapper.selectDvCheckById(id);
    }

    @Override
    public int insertDvCheck(DvCheck check) {
        check.setStatus("PENDING");
        return checkMapper.insertDvCheck(check);
    }

    @Override
    public int updateDvCheck(DvCheck check) {
        return checkMapper.updateDvCheck(check);
    }

    @Override
    public int deleteDvCheckById(Long id) {
        return checkMapper.deleteDvCheckById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        checkMapper.updateStatus(id, status);
    }
}
