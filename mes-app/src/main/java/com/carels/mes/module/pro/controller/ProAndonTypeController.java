package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.pro.domain.ProAndonType;
import com.carels.mes.module.pro.service.IProAndonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安东异常类型Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/pro/andon/type")
public class ProAndonTypeController extends BaseController {

    @Autowired
    private IProAndonTypeService andonTypeService;

    @GetMapping("/list")
    public TableDataInfo list(ProAndonType type) {
        startPage();
        List<ProAndonType> list = andonTypeService.selectProAndonTypeList(type);
        return getDataTable(list);
    }

    @GetMapping("/{typeId}")
    public AjaxResult getInfo(@PathVariable Long typeId) {
        return AjaxResult.success(andonTypeService.selectProAndonTypeById(typeId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody ProAndonType type) {
        return toAjax(andonTypeService.insertProAndonType(type));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ProAndonType type) {
        return toAjax(andonTypeService.updateProAndonType(type));
    }

    @DeleteMapping("/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds) {
        int rows = 0;
        for (Long typeId : typeIds) {
            rows += andonTypeService.deleteProAndonTypeById(typeId);
        }
        return toAjax(rows);
    }
}
