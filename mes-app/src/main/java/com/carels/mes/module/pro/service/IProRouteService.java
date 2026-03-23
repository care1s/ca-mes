package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProRoute;

import java.util.List;

/**
 * 工艺路线Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface IProRouteService {

    /**
     * 查询工艺路线列表
     */
    List<ProRoute> selectProRouteList(ProRoute route);

    /**
     * 根据ID查询工艺路线
     */
    ProRoute selectProRouteById(Long routeId);

    /**
     * 根据编码查询工艺路线
     */
    ProRoute selectProRouteByCode(String routeCode);

    /**
     * 根据产品ID查询默认工艺路线
     */
    ProRoute selectDefaultRouteByItemId(Long itemId);

    /**
     * 新增工艺路线
     */
    int insertProRoute(ProRoute route);

    /**
     * 修改工艺路线
     */
    int updateProRoute(ProRoute route);

    /**
     * 删除工艺路线
     */
    int deleteProRouteById(Long routeId);

    /**
     * 批量删除工艺路线
     */
    int deleteProRouteByIds(Long[] routeIds);

    /**
     * 根据产品ID查询工艺路线列表
     */
    List<ProRoute> selectProRouteByItemId(Long itemId);
}
