package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProRoute;
import com.carels.mes.module.pro.service.IProRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工艺路线Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/pro/route")
public class ProRouteController extends BaseController {

    @Autowired
    private IProRouteService routeService;

    /**
     * 查询工艺路线列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProRoute route) {
        startPage();
        List<ProRoute> list = routeService.selectProRouteList(route);
        return getDataTable(list);
    }

    /**
     * 根据ID查询工艺路线详情
     */
    @GetMapping("/{routeId}")
    public AjaxResult getInfo(@PathVariable Long routeId) {
        return AjaxResult.success(routeService.selectProRouteById(routeId));
    }

    /**
     * 根据编码查询工艺路线
     */
    @GetMapping("/byCode/{routeCode}")
    public AjaxResult getByCode(@PathVariable String routeCode) {
        return AjaxResult.success(routeService.selectProRouteByCode(routeCode));
    }

    /**
     * 根据产品ID查询工艺路线列表
     */
    @GetMapping("/byItem/{itemId}")
    public AjaxResult getByItem(@PathVariable Long itemId) {
        List<ProRoute> list = routeService.selectProRouteByItemId(itemId);
        return AjaxResult.success(list);
    }

    /**
     * 查询默认工艺路线
     */
    @GetMapping("/default/{itemId}")
    public AjaxResult getDefault(@PathVariable Long itemId) {
        return AjaxResult.success(routeService.selectDefaultRouteByItemId(itemId));
    }

    /**
     * 新增工艺路线
     */
    @PostMapping
    public AjaxResult add(@RequestBody ProRoute route) {
        return toAjax(routeService.insertProRoute(route));
    }

    /**
     * 修改工艺路线
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ProRoute route) {
        return toAjax(routeService.updateProRoute(route));
    }

    /**
     * 删除工艺路线
     */
    @DeleteMapping("/{routeId}")
    public AjaxResult remove(@PathVariable Long routeId) {
        return toAjax(routeService.deleteProRouteById(routeId));
    }

    /**
     * 批量删除工艺路线
     */
    @DeleteMapping("/batch/{routeIds}")
    public AjaxResult removeBatch(@PathVariable Long[] routeIds) {
        return toAjax(routeService.deleteProRouteByIds(routeIds));
    }
}
