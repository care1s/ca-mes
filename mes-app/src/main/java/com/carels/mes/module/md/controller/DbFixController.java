package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 数据库表自动修复Controller
 */
@Slf4j
@RestController
@RequestMapping("/mes/md/db-fix")
public class DbFixController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 自动修复客户表
     */
    @PostConstruct
    public void autoFixClientTable() {
        try {
            log.info("检查并修复客户表结构...");
            
            // 检查表是否存在
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'md_client'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                // 表不存在，创建新表
                log.info("客户表不存在，正在创建...");
                String createTable = "CREATE TABLE md_client (" +
                        "client_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '客户ID', " +
                        "client_code VARCHAR(50) NOT NULL COMMENT '客户编码', " +
                        "client_name VARCHAR(100) NOT NULL COMMENT '客户名称', " +
                        "contact_person VARCHAR(50) NULL COMMENT '联系人', " +
                        "phone VARCHAR(20) NULL COMMENT '联系电话', " +
                        "email VARCHAR(100) NULL COMMENT '邮箱', " +
                        "address VARCHAR(200) NULL COMMENT '地址', " +
                        "status VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_client_code (client_code)" +
                        ") COMMENT '客户管理表'";
                jdbcTemplate.execute(createTable);
                
                // 创建索引
                jdbcTemplate.execute("CREATE INDEX idx_client_name ON md_client(client_name)");
                jdbcTemplate.execute("CREATE INDEX idx_client_status ON md_client(status)");
                
                log.info("客户表创建成功！");
            } else {
                // 表存在，检查并添加缺失的字段
                log.info("客户表存在，检查字段...");
                
                // 检查contact_person字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_client ADD COLUMN contact_person VARCHAR(50) NULL COMMENT '联系人'");
                    log.info("添加字段: contact_person");
                } catch (Exception e) {
                    log.debug("字段contact_person已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查phone字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_client ADD COLUMN phone VARCHAR(20) NULL COMMENT '联系电话'");
                    log.info("添加字段: phone");
                } catch (Exception e) {
                    log.debug("字段phone已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查email字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_client ADD COLUMN email VARCHAR(100) NULL COMMENT '邮箱'");
                    log.info("添加字段: email");
                } catch (Exception e) {
                    log.debug("字段email已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查address字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_client ADD COLUMN address VARCHAR(200) NULL COMMENT '地址'");
                    log.info("添加字段: address");
                } catch (Exception e) {
                    log.debug("字段address已存在或添加失败: {}", e.getMessage());
                }
                
                log.info("客户表字段检查完成");
            }
        } catch (Exception e) {
            log.error("自动修复客户表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 手动修复接口
     */
    @GetMapping("/client")
    public AjaxResult fixClientTable() {
        try {
            autoFixClientTable();
            return AjaxResult.success("客户表修复成功");
        } catch (Exception e) {
            return AjaxResult.error("修复失败: " + e.getMessage());
        }
    }
    
    /**
     * 自动修复供应商表
     */
    @PostConstruct
    public void autoFixVendorTable() {
        try {
            log.info("检查并修复供应商表结构...");
            
            // 检查表是否存在
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'md_vendor'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                // 表不存在，创建新表
                log.info("供应商表不存在，正在创建...");
                String createTable = "CREATE TABLE md_vendor (" +
                        "vendor_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '供应商ID', " +
                        "vendor_code VARCHAR(50) NOT NULL COMMENT '供应商编码', " +
                        "vendor_name VARCHAR(100) NOT NULL COMMENT '供应商名称', " +
                        "contact_person VARCHAR(50) NULL COMMENT '联系人', " +
                        "phone VARCHAR(20) NULL COMMENT '联系电话', " +
                        "email VARCHAR(100) NULL COMMENT '邮箱', " +
                        "address VARCHAR(200) NULL COMMENT '地址', " +
                        "status VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_vendor_code (vendor_code)" +
                        ") COMMENT '供应商管理表'";
                jdbcTemplate.execute(createTable);
                
                // 创建索引
                jdbcTemplate.execute("CREATE INDEX idx_vendor_name ON md_vendor(vendor_name)");
                jdbcTemplate.execute("CREATE INDEX idx_vendor_status ON md_vendor(status)");
                
                log.info("供应商表创建成功！");
            } else {
                // 表存在，检查并添加缺失的字段
                log.info("供应商表存在，检查字段...");
                
                // 检查contact_person字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_vendor ADD COLUMN contact_person VARCHAR(50) NULL COMMENT '联系人'");
                    log.info("添加字段: contact_person");
                } catch (Exception e) {
                    log.debug("字段contact_person已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查phone字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_vendor ADD COLUMN phone VARCHAR(20) NULL COMMENT '联系电话'");
                    log.info("添加字段: phone");
                } catch (Exception e) {
                    log.debug("字段phone已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查email字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_vendor ADD COLUMN email VARCHAR(100) NULL COMMENT '邮箱'");
                    log.info("添加字段: email");
                } catch (Exception e) {
                    log.debug("字段email已存在或添加失败: {}", e.getMessage());
                }
                
                // 检查address字段
                try {
                    jdbcTemplate.execute("ALTER TABLE md_vendor ADD COLUMN address VARCHAR(200) NULL COMMENT '地址'");
                    log.info("添加字段: address");
                } catch (Exception e) {
                    log.debug("字段address已存在或添加失败: {}", e.getMessage());
                }
                
                log.info("供应商表字段检查完成");
            }
        } catch (Exception e) {
            log.error("自动修复供应商表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 手动修复供应商表接口
     */
    @GetMapping("/vendor")
    public AjaxResult fixVendorTable() {
        try {
            autoFixVendorTable();
            return AjaxResult.success("供应商表修复成功");
        } catch (Exception e) {
            return AjaxResult.error("修复失败: " + e.getMessage());
        }
    }
}
