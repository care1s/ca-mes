package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产计划Mapper接口
 */
public interface ProPlanMapper {
    
    /**
     * 查询计划列表
     */
    List<ProPlan> selectProPlanList(ProPlan plan);
    
    /**
     * 根据ID查询计划
     */
    ProPlan selectProPlanById(Long planId);
    
    /**
     * 根据单号查询计划
     */
    ProPlan selectProPlanByNo(String planNo);
    
    /**
     * 新增计划
     */
    int insertProPlan(ProPlan plan);
    
    /**
     * 修改计划
     */
    int updateProPlan(ProPlan plan);
    
    /**
     * 删除计划
     */
    int deleteProPlanById(Long planId);
    
    /**
     * 批量删除计划
     */
    int deleteProPlanByIds(Long[] planIds);
    
    /**
     * 更新计划状态
     */
    int updateStatus(@Param("planId") Long planId, @Param("status") Integer status);
}
