package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmWarning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存预警Mapper接口
 */
public interface WmWarningMapper {
    
    /**
     * 查询预警列表
     */
    List<WmWarning> selectWmWarningList(WmWarning warning);
    
    /**
     * 根据ID查询预警
     */
    WmWarning selectWmWarningById(Long warningId);
    
    /**
     * 新增预警记录
     */
    int insertWmWarning(WmWarning warning);
    
    /**
     * 修改预警记录
     */
    int updateWmWarning(WmWarning warning);
    
    /**
     * 删除预警记录
     */
    int deleteWmWarningById(Long warningId);
    
    /**
     * 批量删除预警记录
     */
    int deleteWmWarningByIds(Long[] warningIds);
    
    /**
     * 更新预警状态
     */
    int updateStatus(@Param("warningId") Long warningId, @Param("status") Integer status);
    
    /**
     * 统计预警数量
     */
    int countWarning(@Param("warningType") Integer warningType, @Param("warningLevel") Integer warningLevel);
}
