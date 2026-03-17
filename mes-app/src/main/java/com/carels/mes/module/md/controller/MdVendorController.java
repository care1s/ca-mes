package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdVendor;
import com.carels.mes.module.md.service.IMdVendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商管理Controller - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Slf4j
@RestController
@RequestMapping("/mes/md/vendor")
public class MdVendorController extends BaseController {

    @Autowired
    private IMdVendorService mdVendorService;

    /**
     * 查询供应商列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              MdVendor mdVendor) {
        startPage(pageNum, pageSize);
        try {
            List<MdVendor> list = mdVendorService.selectMdVendorList(mdVendor);
            return getDataTable(list);
        } catch (DataAccessException e) {
            log.error("查询供应商列表失败: {}", e.getMessage());
            return getDataTable(List.of());
        }
    }

    /**
     * 查询所有启用的供应商（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        try {
            return AjaxResult.success(mdVendorService.selectAllEnabledVendors());
        } catch (DataAccessException e) {
            log.error("查询供应商列表失败: {}", e.getMessage());
            return AjaxResult.error("数据库表结构错误，请检查md_vendor表是否存在");
        }
    }

    /**
     * 根据ID查询供应商详情
     */
    @GetMapping("/{vendorId}")
    public AjaxResult getInfo(@PathVariable Long vendorId) {
        return AjaxResult.success(mdVendorService.selectMdVendorById(vendorId));
    }

    /**
     * 根据编码查询供应商
     */
    @GetMapping("/code/{vendorCode}")
    public AjaxResult getByCode(@PathVariable String vendorCode) {
        return AjaxResult.success(mdVendorService.selectMdVendorByCode(vendorCode));
    }

    /**
     * 新增供应商
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdVendor mdVendor) {
        try {
            if (!mdVendorService.checkVendorCodeUnique(mdVendor.getVendorCode())) {
                return AjaxResult.error("新增供应商'" + mdVendor.getVendorCode() + "'失败，供应商编码已存在");
            }
            return toAjax(mdVendorService.insertMdVendor(mdVendor));
        } catch (DataAccessException e) {
            log.error("新增供应商失败: {}", e.getMessage());
            if (e.getMessage().contains("contact_person") || e.getMessage().contains("Unknown column")) {
                return AjaxResult.error("数据库表缺少必要字段，请联系管理员修复");
            }
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改供应商
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdVendor mdVendor) {
        try {
            return toAjax(mdVendorService.updateMdVendor(mdVendor));
        } catch (DataAccessException e) {
            log.error("修改供应商失败: {}", e.getMessage());
            if (e.getMessage().contains("contact_person") || e.getMessage().contains("Unknown column")) {
                return AjaxResult.error("数据库表缺少必要字段，请联系管理员修复");
            }
            return AjaxResult.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{vendorId}")
    public AjaxResult remove(@PathVariable Long vendorId) {
        return toAjax(mdVendorService.deleteMdVendorById(vendorId));
    }

    /**
     * 批量删除供应商
     */
    @DeleteMapping("/batch/{vendorIds}")
    public AjaxResult removeBatch(@PathVariable Long[] vendorIds) {
        return toAjax(mdVendorService.deleteMdVendorByIds(vendorIds));
    }
}
