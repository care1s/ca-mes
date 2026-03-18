package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmTransfer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存调拨Mapper接口
 */
public interface WmTransferMapper {
    
    /**
     * 查询调拨列表
     */
    List<WmTransfer> selectWmTransferList(WmTransfer transfer);
    
    /**
     * 根据ID查询调拨单
     */
    WmTransfer selectWmTransferById(Long transferId);
    
    /**
     * 根据单号查询调拨单
     */
    WmTransfer selectWmTransferByNo(String transferNo);
    
    /**
     * 新增调拨单
     */
    int insertWmTransfer(WmTransfer transfer);
    
    /**
     * 修改调拨单
     */
    int updateWmTransfer(WmTransfer transfer);
    
    /**
     * 删除调拨单
     */
    int deleteWmTransferById(Long transferId);
    
    /**
     * 批量删除调拨单
     */
    int deleteWmTransferByIds(Long[] transferIds);
    
    /**
     * 更新调拨状态
     */
    int updateStatus(@Param("transferId") Long transferId, @Param("status") Integer status);
}
