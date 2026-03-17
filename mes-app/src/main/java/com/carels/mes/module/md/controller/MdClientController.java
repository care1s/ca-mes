package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdClient;
import com.carels.mes.module.md.service.IMdClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理Controller - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Slf4j
@RestController
@RequestMapping("/mes/md/client")
public class MdClientController extends BaseController {

    @Autowired
    private IMdClientService mdClientService;

    /**
     * 查询客户列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              MdClient mdClient) {
        startPage(pageNum, pageSize);
        try {
            List<MdClient> list = mdClientService.selectMdClientList(mdClient);
            return getDataTable(list);
        } catch (DataAccessException e) {
            log.error("查询客户列表失败: {}", e.getMessage());
            return getDataTable(List.of());
        }
    }

    /**
     * 查询所有启用的客户（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        try {
            return AjaxResult.success(mdClientService.selectAllEnabledClients());
        } catch (DataAccessException e) {
            log.error("查询客户列表失败: {}", e.getMessage());
            return AjaxResult.error("数据库表结构错误，请检查md_client表是否存在");
        }
    }

    /**
     * 根据ID查询客户详情
     */
    @GetMapping("/{clientId}")
    public AjaxResult getInfo(@PathVariable Long clientId) {
        return AjaxResult.success(mdClientService.selectMdClientById(clientId));
    }

    /**
     * 根据编码查询客户
     */
    @GetMapping("/code/{clientCode}")
    public AjaxResult getByCode(@PathVariable String clientCode) {
        return AjaxResult.success(mdClientService.selectMdClientByCode(clientCode));
    }

    /**
     * 新增客户
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdClient mdClient) {
        try {
            if (!mdClientService.checkClientCodeUnique(mdClient.getClientCode())) {
                return AjaxResult.error("新增客户'" + mdClient.getClientCode() + "'失败，客户编码已存在");
            }
            return toAjax(mdClientService.insertMdClient(mdClient));
        } catch (DataAccessException e) {
            log.error("新增客户失败: {}", e.getMessage());
            if (e.getMessage().contains("contact_person") || e.getMessage().contains("Unknown column")) {
                return AjaxResult.error("数据库表缺少必要字段，请执行SQL: ALTER TABLE md_client ADD COLUMN contact_person VARCHAR(50), ADD COLUMN phone VARCHAR(20), ADD COLUMN email VARCHAR(100), ADD COLUMN address VARCHAR(200)");
            }
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改客户
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdClient mdClient) {
        try {
            return toAjax(mdClientService.updateMdClient(mdClient));
        } catch (DataAccessException e) {
            log.error("修改客户失败: {}", e.getMessage());
            if (e.getMessage().contains("contact_person") || e.getMessage().contains("Unknown column")) {
                return AjaxResult.error("数据库表缺少必要字段，请联系管理员修复");
            }
            return AjaxResult.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{clientId}")
    public AjaxResult remove(@PathVariable Long clientId) {
        return toAjax(mdClientService.deleteMdClientById(clientId));
    }

    /**
     * 批量删除客户
     */
    @DeleteMapping("/batch/{clientIds}")
    public AjaxResult removeBatch(@PathVariable Long[] clientIds) {
        return toAjax(mdClientService.deleteMdClientByIds(clientIds));
    }
}
