package com.carels.mes.module.pro.mapper;

import com.carels.mes.module.pro.domain.ProMaterialReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料需求Mapper接口
 */
public interface ProMaterialReqMapper {
    
    /**
     * 查询需求列表
     */
    List<ProMaterialReq> selectProMaterialReqList(ProMaterialReq req);
    
    /**
     * 根据ID查询需求单
     */
    ProMaterialReq selectProMaterialReqById(Long reqId);
    
    /**
     * 根据单号查询需求单
     */
    ProMaterialReq selectProMaterialReqByNo(String reqNo);
    
    /**
     * 新增需求单
     */
    int insertProMaterialReq(ProMaterialReq req);
    
    /**
     * 修改需求单
     */
    int updateProMaterialReq(ProMaterialReq req);
    
    /**
     * 删除需求单
     */
    int deleteProMaterialReqById(Long reqId);
    
    /**
     * 批量删除需求单
     */
    int deleteProMaterialReqByIds(Long[] reqIds);
    
    /**
     * 更新需求单状态
     */
    int updateStatus(@Param("reqId") Long reqId, @Param("status") Integer status);
}
