package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProProcess;

import java.util.List;

/**
 * 工序Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface IProProcessService {

    /**
     * 查询工序列表
     */
    List<ProProcess> selectProProcessList(ProProcess process);

    /**
     * 根据ID查询工序
     */
    ProProcess selectProProcessById(Long processId);

    /**
     * 根据编码查询工序
     */
    ProProcess selectProProcessByCode(String processCode);

    /**
     * 新增工序
     */
    int insertProProcess(ProProcess process);

    /**
     * 修改工序
     */
    int updateProProcess(ProProcess process);

    /**
     * 删除工序
     */
    int deleteProProcessById(Long processId);

    /**
     * 批量删除工序
     */
    int deleteProProcessByIds(Long[] processIds);

    /**
     * 启用工序
     */
    void enableProcess(Long processId);

    /**
     * 停用工序
     */
    void disableProcess(Long processId);

    /**
     * 根据车间ID查询工序列表
     */
    List<ProProcess> selectByWorkshopId(Long workshopId);
}
