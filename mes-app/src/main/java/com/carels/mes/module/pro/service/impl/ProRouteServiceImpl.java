package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProRoute;
import com.carels.mes.module.pro.mapper.ProRouteMapper;
import com.carels.mes.module.pro.service.IProRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工艺路线Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Service
public class ProRouteServiceImpl implements IProRouteService {

    @Autowired
    private ProRouteMapper routeMapper;

    @Override
    public List<ProRoute> selectProRouteList(ProRoute route) {
        return routeMapper.selectProRouteList(route);
    }

    @Override
    public ProRoute selectProRouteById(Long routeId) {
        return routeMapper.selectProRouteById(routeId);
    }

    @Override
    public ProRoute selectProRouteByCode(String routeCode) {
        return routeMapper.selectProRouteByCode(routeCode);
    }

    @Override
    public ProRoute selectDefaultRouteByItemId(Long itemId) {
        return routeMapper.selectDefaultRouteByItemId(itemId);
    }

    @Override
    public int insertProRoute(ProRoute route) {
        return routeMapper.insertProRoute(route);
    }

    @Override
    public int updateProRoute(ProRoute route) {
        return routeMapper.updateProRoute(route);
    }

    @Override
    public int deleteProRouteById(Long routeId) {
        return routeMapper.deleteProRouteById(routeId);
    }

    @Override
    public int deleteProRouteByIds(Long[] routeIds) {
        return routeMapper.deleteProRouteByIds(routeIds);
    }

    @Override
    public List<ProRoute> selectProRouteByItemId(Long itemId) {
        return routeMapper.selectProRouteByItemId(itemId);
    }
}
