package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProRouteProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工艺路线工序Mapper接口 - carels
 */
public interface ProRouteProcessMapper {

    /**
     * 根据工艺路线ID查询工序列表
     */
    List<ProRouteProcess> selectByRouteId(Long routeId);

    /**
     * 根据工艺路线ID和工序序号查询
     */
    ProRouteProcess selectByRouteIdAndSeq(@Param("routeId") Long routeId, @Param("sequenceNo") Integer sequenceNo);

    /**
     * 新增工艺路线工序
     */
    int insert(ProRouteProcess routeProcess);

    /**
     * 批量新增工艺路线工序
     */
    int batchInsert(@Param("list") List<ProRouteProcess> list);

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
}
