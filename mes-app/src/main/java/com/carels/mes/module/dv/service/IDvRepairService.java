package com.carels.mes.module.dv.service;

import com.carels.mes.module.dv.domain.DvRepair;
import java.math.BigDecimal;
import java.util.List;

/**
 * 设备维修Service接口
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface IDvRepairService {

    /**
     * 查询维修列表
     */
    List<DvRepair> selectDvRepairList(DvRepair repair);

    /**
     * 根据ID查询维修
     */
    DvRepair selectDvRepairById(Long id);

    /**
     * 新增维修
     */
    int insertDvRepair(DvRepair repair);

    /**
     * 修改维修
     */
    int updateDvRepair(DvRepair repair);

    /**
     * 删除维修
     */
    int deleteDvRepairById(Long id);

    /**
     * 更新状态
     */
    void updateStatus(Long id, String status);

    /**
     * 派工
     */
    void assignRepair(Long id, Long repairmanId, String repairmanName);

    /**
     * 完成维修
     */
    void completeRepair(Long id, String repairContent, BigDecimal repairCost);
}
