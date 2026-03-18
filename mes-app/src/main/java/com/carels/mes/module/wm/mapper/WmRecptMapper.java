package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmRecpt;
import com.carels.mes.module.wm.domain.WmRecptItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库管理Mapper接口
 */
public interface WmRecptMapper {

    List<WmRecpt> selectWmRecptList(WmRecpt recpt);

    WmRecpt selectWmRecptById(Long recptId);

    int insertWmRecpt(WmRecpt recpt);

    int updateWmRecpt(WmRecpt recpt);

    int deleteWmRecptById(Long recptId);

    int updateStatus(@Param("recptId") Long recptId, @Param("status") String status);

    // 明细操作
    List<WmRecptItem> selectWmRecptItemsByRecptId(Long recptId);

    int insertWmRecptItem(WmRecptItem item);

    int updateWmRecptItem(WmRecptItem item);

    int deleteWmRecptItemsByRecptId(Long recptId);

    int deleteWmRecptItemById(Long itemId);
}
