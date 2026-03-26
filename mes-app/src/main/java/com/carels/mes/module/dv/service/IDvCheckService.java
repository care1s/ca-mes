package com.carels.mes.module.dv.service;

import com.carels.mes.module.dv.domain.DvCheck;
import java.util.List;

/**
 * 设备点检Service接口
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface IDvCheckService {

    /**
     * 查询点检列表
     */
    List<DvCheck> selectDvCheckList(DvCheck check);

    /**
     * 根据ID查询点检
     */
    DvCheck selectDvCheckById(Long id);

    /**
     * 新增点检
     */
    int insertDvCheck(DvCheck check);

    /**
     * 修改点检
     */
    int updateDvCheck(DvCheck check);

    /**
     * 删除点检
     */
    int deleteDvCheckById(Long id);

    /**
     * 更新状态
     */
    void updateStatus(Long id, String status);
}
