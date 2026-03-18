package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmStock;
import java.util.List;

public interface WmStockMapper {
    List<WmStock> selectWmStockList(WmStock stock);
    WmStock selectWmStockById(Long id);
    int insertWmStock(WmStock stock);
    int updateWmStock(WmStock stock);
    int deleteWmStockById(Long id);
}
