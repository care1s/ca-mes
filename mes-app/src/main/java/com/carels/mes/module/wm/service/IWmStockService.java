package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmStock;
import java.util.List;

public interface IWmStockService {
    List<WmStock> selectWmStockList(WmStock stock);
    WmStock selectWmStockById(Long id);
    int insertWmStock(WmStock stock);
    int updateWmStock(WmStock stock);
    int deleteWmStockById(Long id);
}
