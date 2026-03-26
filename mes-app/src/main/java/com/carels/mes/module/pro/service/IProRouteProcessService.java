package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProRouteProcess;

import java.util.List;

/**
 * 工艺路线工序Service接口 - carels
 */
public interface IProRouteProcessService {

    /**
     * 根据工艺路线ID查询工序列表
     */
    List<ProRouteProcess> selectByRouteId(Long routeId);

    /**
     * 根据工艺路线ID和工序序号查询
     */
    ProRouteProcess selectByRouteIdAndSeq(Long routeId, Integer sequenceNo);

    /**
     * 新增工艺路线工序
     */
    int insert(ProRouteProcess routeProcess);

    /**
     * 批量新增工艺路线工序
     */
    int batchInsert(List<ProRouteProcess> list);

    /**
     * 修改工艺路线工序
     */
    int update(ProRouteProcess routeProcess);

    /**
     * 删除工艺路线工序
     */
    int deleteById(Long id);

    /**
     * 根据工艺路线ID删除所有工序
     */
    int deleteByRouteId(Long routeId);

    /**
     * 保存工艺路线的工序配置（先删除旧配置，再保存新配置）
     */
    int saveRouteProcesses(Long routeId, List<ProRouteProcess> processes, String username);
}
