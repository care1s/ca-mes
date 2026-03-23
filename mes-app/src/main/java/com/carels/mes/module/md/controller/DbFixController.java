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
    
    /**
     * 自动修复采购申请表
     */
    @PostConstruct
    public void autoFixPurRequestTable() {
        try {
            log.info("检查并修复采购申请表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pur_request'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("采购申请表不存在，正在创建...");
                String createTable = "CREATE TABLE pur_request (" +
                        "request_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '申请ID', " +
                        "request_code VARCHAR(50) NOT NULL COMMENT '申请单号', " +
                        "request_date DATE NOT NULL COMMENT '申请日期', " +
                        "request_type VARCHAR(20) DEFAULT 'NORMAL' COMMENT '申请类型', " +
                        "applicant_id BIGINT NULL COMMENT '申请人ID', " +
                        "applicant_name VARCHAR(50) NULL COMMENT '申请人姓名', " +
                        "dept_id BIGINT NULL COMMENT '部门ID', " +
                        "dept_name VARCHAR(50) NULL COMMENT '部门名称', " +
                        "total_amount DECIMAL(18,2) DEFAULT 0 COMMENT '总金额', " +
                        "currency VARCHAR(10) DEFAULT 'CNY' COMMENT '币种', " +
                        "status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿,PENDING-待审批,APPROVED-已审批,REJECTED-已拒绝', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_request_code (request_code)" +
                        ") COMMENT '采购申请主表'";
                jdbcTemplate.execute(createTable);

                jdbcTemplate.execute("CREATE INDEX idx_request_date ON pur_request(request_date)");
                jdbcTemplate.execute("CREATE INDEX idx_applicant_id ON pur_request(applicant_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON pur_request(status)");

                log.info("采购申请表创建成功！");
            } else {
                log.info("采购申请表已存在，检查并修复status字段长度...");
                try {
                    // 修改status字段长度
                    jdbcTemplate.execute("ALTER TABLE pur_request MODIFY COLUMN status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿,PENDING-待审批,APPROVED-已审批,REJECTED-已拒绝'");
                    log.info("status字段已修改为VARCHAR(20)");
                } catch (Exception e) {
                    log.debug("status字段修改失败或已正确: {}", e.getMessage());
                }
            }

            // 创建明细表
            autoFixPurRequestItemTable();

        } catch (Exception e) {
            log.error("自动修复采购申请表失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 自动修复采购申请明细表
     */
    private void autoFixPurRequestItemTable() {
        try {
            log.info("检查并修复采购申请明细表结构...");

            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pur_request_item'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);

            if (tableCount == null || tableCount == 0) {
                log.info("采购申请明细表不存在，正在创建...");
                String createTable = "CREATE TABLE pur_request_item (" +
                        "item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID', " +
                        "request_id BIGINT NOT NULL COMMENT '申请ID', " +
                        "item_id_ref BIGINT NULL COMMENT '物料ID', " +
                        "item_code VARCHAR(50) NOT NULL COMMENT '物料编码', " +
                        "item_name VARCHAR(100) NOT NULL COMMENT '物料名称', " +
                        "item_spec VARCHAR(200) NULL COMMENT '规格型号', " +
                        "unit VARCHAR(20) NULL COMMENT '单位', " +
                        "quantity DECIMAL(18,4) DEFAULT 0 COMMENT '数量', " +
                        "price DECIMAL(18,4) DEFAULT 0 COMMENT '单价', " +
                        "amount DECIMAL(18,2) DEFAULT 0 COMMENT '金额', " +
                        "required_date DATE NULL COMMENT '需求日期', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "KEY idx_request_id (request_id)" +
                        ") COMMENT '采购申请明细表'";
                jdbcTemplate.execute(createTable);

                jdbcTemplate.execute("CREATE INDEX idx_item_code ON pur_request_item(item_code)");

                log.info("采购申请明细表创建成功！");
            } else {
                log.info("采购申请明细表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复采购申请明细表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复采购订单表
     */
    @PostConstruct
    public void autoFixPurOrderTable() {
        try {
            log.info("检查并修复采购订单表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pur_order'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("采购订单表不存在，正在创建...");
                String createTable = "CREATE TABLE pur_order (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID', " +
                        "order_no VARCHAR(50) NOT NULL COMMENT '订单编号', " +
                        "request_id BIGINT NULL COMMENT '申请ID', " +
                        "request_no VARCHAR(50) NULL COMMENT '申请单号', " +
                        "vendor_id BIGINT NOT NULL COMMENT '供应商ID', " +
                        "vendor_code VARCHAR(50) NULL COMMENT '供应商编码', " +
                        "vendor_name VARCHAR(100) NULL COMMENT '供应商名称', " +
                        "order_date DATE NOT NULL COMMENT '订单日期', " +
                        "delivery_date DATE NULL COMMENT '交货日期', " +
                        "total_amount DECIMAL(18,2) DEFAULT 0 COMMENT '订单总金额', " +
                        "total_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '订单总数量', " +
                        "currency VARCHAR(10) DEFAULT 'CNY' COMMENT '币种', " +
                        "order_status INT DEFAULT 0 COMMENT '订单状态: 0-草稿,1-已确认,2-部分收货,3-已完成,4-已取消', " +
                        "audit_status INT DEFAULT 0 COMMENT '审批状态: 0-草稿,1-待审批,2-已通过,3-已拒绝', " +
                        "contact_name VARCHAR(50) NULL COMMENT '联系人', " +
                        "contact_phone VARCHAR(20) NULL COMMENT '联系电话', " +
                        "delivery_address VARCHAR(200) NULL COMMENT '送货地址', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_order_no (order_no)" +
                        ") COMMENT '采购订单主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_order_date ON pur_order(order_date)");
                jdbcTemplate.execute("CREATE INDEX idx_vendor_id ON pur_order(vendor_id)");
                jdbcTemplate.execute("CREATE INDEX idx_order_status ON pur_order(order_status)");
                jdbcTemplate.execute("CREATE INDEX idx_audit_status ON pur_order(audit_status)");
                
                log.info("采购订单表创建成功！");
            } else {
                log.info("采购订单表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复采购订单表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复采购入库表
     */
    @PostConstruct
    public void autoFixPurReceiptTable() {
        try {
            log.info("检查并修复采购入库表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pur_receipt'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("采购入库表不存在，正在创建...");
                String createTable = "CREATE TABLE pur_receipt (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '入库ID', " +
                        "receipt_no VARCHAR(50) NOT NULL COMMENT '入库单号', " +
                        "order_id BIGINT NOT NULL COMMENT '订单ID', " +
                        "order_no VARCHAR(50) NULL COMMENT '订单编号', " +
                        "vendor_id BIGINT NOT NULL COMMENT '供应商ID', " +
                        "vendor_code VARCHAR(50) NULL COMMENT '供应商编码', " +
                        "vendor_name VARCHAR(100) NULL COMMENT '供应商名称', " +
                        "receipt_date DATE NOT NULL COMMENT '入库日期', " +
                        "total_amount DECIMAL(18,2) DEFAULT 0 COMMENT '入库总金额', " +
                        "total_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '入库总数量', " +
                        "qualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '合格数量', " +
                        "unqualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '不合格数量', " +
                        "warehouse_id BIGINT NULL COMMENT '仓库ID', " +
                        "warehouse_name VARCHAR(50) NULL COMMENT '仓库名称', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-草稿,1-质检中,2-已完成', " +
                        "audit_status INT DEFAULT 0 COMMENT '审批状态: 0-草稿,1-待审批,2-已通过,3-已拒绝', " +
                        "stock_in_status INT DEFAULT 0 COMMENT '入库状态: 0-未入库,1-已入库', " +
                        "inspector_id BIGINT NULL COMMENT '质检员ID', " +
                        "inspector_name VARCHAR(50) NULL COMMENT '质检员姓名', " +
                        "inspection_date DATETIME NULL COMMENT '质检日期', " +
                        "inspection_remark VARCHAR(500) NULL COMMENT '质检备注', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_receipt_no (receipt_no)" +
                        ") COMMENT '采购入库主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_receipt_date ON pur_receipt(receipt_date)");
                jdbcTemplate.execute("CREATE INDEX idx_order_id ON pur_receipt(order_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON pur_receipt(status)");
                jdbcTemplate.execute("CREATE INDEX idx_stock_in_status ON pur_receipt(stock_in_status)");
                
                log.info("采购入库表创建成功！");
            } else {
                log.info("采购入库表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复采购入库表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复采购退货表
     */
    @PostConstruct
    public void autoFixPurReturnTable() {
        try {
            log.info("检查并修复采购退货表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pur_return'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("采购退货表不存在，正在创建...");
                String createTable = "CREATE TABLE pur_return (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '退货ID', " +
                        "return_no VARCHAR(50) NOT NULL COMMENT '退货单号', " +
                        "receipt_id BIGINT NULL COMMENT '关联入库ID', " +
                        "receipt_no VARCHAR(50) NULL COMMENT '关联入库单号', " +
                        "vendor_id BIGINT NULL COMMENT '供应商ID', " +
                        "vendor_code VARCHAR(50) NULL COMMENT '供应商编码', " +
                        "vendor_name VARCHAR(100) NULL COMMENT '供应商名称', " +
                        "return_date DATE NOT NULL COMMENT '退货日期', " +
                        "total_amount DECIMAL(18,2) DEFAULT 0 COMMENT '退货金额', " +
                        "total_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '退货总数量', " +
                        "return_reason VARCHAR(200) NULL COMMENT '退货原因', " +
                        "status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, CONFIRMED-已确认, COMPLETED-已完成', " +
                        "audit_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '审核状态: PENDING-待审核, APPROVED-已审核, REJECTED-已驳回', " +
                        "stock_out_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '出库状态: PENDING-待出库, PARTIAL-部分出库, COMPLETED-已完成', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_return_no (return_no)" +
                        ") COMMENT '采购退货主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_return_date ON pur_return(return_date)");
                jdbcTemplate.execute("CREATE INDEX idx_receipt_id ON pur_return(receipt_id)");
                jdbcTemplate.execute("CREATE INDEX idx_vendor_id ON pur_return(vendor_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON pur_return(status)");
                
                log.info("采购退货表创建成功！");
            } else {
                log.info("采购退货表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复采购退货表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复生产计划表
     */
    @PostConstruct
    public void autoFixProPlanTable() {
        try {
            log.info("检查并修复生产计划表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pro_plan'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("生产计划表不存在，正在创建...");
                String createTable = "CREATE TABLE pro_plan (" +
                        "plan_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '计划ID', " +
                        "plan_no VARCHAR(50) NOT NULL COMMENT '计划单号', " +
                        "plan_name VARCHAR(100) NOT NULL COMMENT '计划名称', " +
                        "plan_type INT DEFAULT 0 COMMENT '计划类型: 0-周计划,1-月计划,2-季度计划', " +
                        "start_date DATE NOT NULL COMMENT '开始日期', " +
                        "end_date DATE NOT NULL COMMENT '结束日期', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-草稿,1-已发布,2-执行中,3-已完成,4-已取消', " +
                        "plan_qty DECIMAL(18,2) DEFAULT 0 COMMENT '计划产量', " +
                        "actual_qty DECIMAL(18,2) DEFAULT 0 COMMENT '实际产量', " +
                        "completion_rate DECIMAL(5,2) DEFAULT 0 COMMENT '完成率', " +
                        "manager_id BIGINT NULL COMMENT '负责人ID', " +
                        "manager_name VARCHAR(50) NULL COMMENT '负责人姓名', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_plan_no (plan_no)" +
                        ") COMMENT '生产计划主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_start_date ON pro_plan(start_date)");
                jdbcTemplate.execute("CREATE INDEX idx_end_date ON pro_plan(end_date)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON pro_plan(status)");
                
                log.info("生产计划表创建成功！");
            } else {
                log.info("生产计划表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复生产计划表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复库存盘点表
     */
    @PostConstruct
    public void autoFixWmStocktakingTable() {
        try {
            log.info("检查并修复库存盘点表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_stocktaking'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("库存盘点表不存在，正在创建...");
                String createTable = "CREATE TABLE wm_stocktaking (" +
                        "stocktaking_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '盘点ID', " +
                        "stocktaking_no VARCHAR(50) NOT NULL COMMENT '盘点单号', " +
                        "warehouse_id BIGINT NULL COMMENT '仓库ID', " +
                        "warehouse_name VARCHAR(100) NULL COMMENT '仓库名称', " +
                        "stocktaking_type INT DEFAULT 0 COMMENT '盘点类型: 0-全盘,1-抽盘,2-动碰盘', " +
                        "stocktaking_date DATE NOT NULL COMMENT '盘点日期', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-草稿,1-盘点中,2-已完成,3-已取消', " +
                        "stocktaker_id BIGINT NULL COMMENT '盘点人ID', " +
                        "stocktaker_name VARCHAR(50) NULL COMMENT '盘点人姓名', " +
                        "plan_count INT DEFAULT 0 COMMENT '计划盘点数量', " +
                        "actual_count INT DEFAULT 0 COMMENT '实际盘点数量', " +
                        "profit_count INT DEFAULT 0 COMMENT '盘盈数量', " +
                        "loss_count INT DEFAULT 0 COMMENT '盘亏数量', " +
                        "diff_amount DECIMAL(18,2) DEFAULT 0 COMMENT '差异金额', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_stocktaking_no (stocktaking_no)" +
                        ") COMMENT '库存盘点主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_stocktaking_date ON wm_stocktaking(stocktaking_date)");
                jdbcTemplate.execute("CREATE INDEX idx_warehouse_id ON wm_stocktaking(warehouse_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON wm_stocktaking(status)");
                
                log.info("库存盘点表创建成功！");
            } else {
                log.info("库存盘点表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复库存盘点表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复库存调拨表
     */
    @PostConstruct
    public void autoFixWmTransferTable() {
        try {
            log.info("检查并修复库存调拨表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_transfer'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("库存调拨表不存在，正在创建...");
                String createTable = "CREATE TABLE wm_transfer (" +
                        "transfer_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '调拨ID', " +
                        "transfer_no VARCHAR(50) NOT NULL COMMENT '调拨单号', " +
                        "from_warehouse_id BIGINT NULL COMMENT '调出仓库ID', " +
                        "from_warehouse_name VARCHAR(100) NULL COMMENT '调出仓库名称', " +
                        "to_warehouse_id BIGINT NULL COMMENT '调入仓库ID', " +
                        "to_warehouse_name VARCHAR(100) NULL COMMENT '调入仓库名称', " +
                        "transfer_date DATE NOT NULL COMMENT '调拨日期', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-草稿,1-待出库,2-已出库,3-已入库,4-已取消', " +
                        "transfer_type INT DEFAULT 0 COMMENT '调拨类型: 0-普通调拨,1-紧急调拨', " +
                        "total_qty DECIMAL(18,2) DEFAULT 0 COMMENT '总数量', " +
                        "total_count INT DEFAULT 0 COMMENT '总件数', " +
                        "applicant_id BIGINT NULL COMMENT '申请人ID', " +
                        "applicant_name VARCHAR(50) NULL COMMENT '申请人姓名', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_transfer_no (transfer_no)" +
                        ") COMMENT '库存调拨主表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_transfer_date ON wm_transfer(transfer_date)");
                jdbcTemplate.execute("CREATE INDEX idx_from_warehouse_id ON wm_transfer(from_warehouse_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON wm_transfer(status)");
                
                log.info("库存调拨表创建成功！");
            } else {
                log.info("库存调拨表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复库存调拨表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复系统用户表
     */
    @PostConstruct
    public void autoFixSysUserTable() {
        try {
            log.info("检查并修复系统用户表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_user'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("系统用户表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_user (" +
                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID', " +
                        "username VARCHAR(50) NOT NULL COMMENT '用户名', " +
                        "password VARCHAR(100) NOT NULL COMMENT '密码', " +
                        "nickname VARCHAR(50) NULL COMMENT '昵称', " +
                        "real_name VARCHAR(50) NULL COMMENT '真实姓名', " +
                        "gender INT DEFAULT 0 COMMENT '性别: 0-男, 1-女', " +
                        "phone VARCHAR(20) NULL COMMENT '手机号', " +
                        "email VARCHAR(100) NULL COMMENT '邮箱', " +
                        "avatar VARCHAR(200) NULL COMMENT '头像', " +
                        "dept_id BIGINT NULL COMMENT '部门ID', " +
                        "dept_name VARCHAR(50) NULL COMMENT '部门名称', " +
                        "post VARCHAR(50) NULL COMMENT '岗位', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用', " +
                        "user_type INT DEFAULT 0 COMMENT '用户类型: 0-普通用户, 1-管理员', " +
                        "login_ip VARCHAR(50) NULL COMMENT '最后登录IP', " +
                        "login_time DATETIME NULL COMMENT '最后登录时间', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_username (username)" +
                        ") COMMENT '系统用户表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_dept_id ON sys_user(dept_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON sys_user(status)");
                
                // 插入默认管理员账号
                String insertAdmin = "INSERT INTO sys_user (username, password, nickname, real_name, user_type, status, create_by) " +
                        "VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', '系统管理员', 1, 0, 'system')";
                jdbcTemplate.execute(insertAdmin);
                
                log.info("系统用户表创建成功！默认管理员账号: admin/123456");
            } else {
                log.info("系统用户表已存在，检查字段...");
                // 检查并添加缺失字段
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN username VARCHAR(50) NOT NULL COMMENT '用户名' AFTER user_id");
                } catch (Exception e) {
                    log.debug("字段username已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN password VARCHAR(100) NOT NULL COMMENT '密码' AFTER username");
                } catch (Exception e) {
                    log.debug("字段password已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN nickname VARCHAR(50) NULL COMMENT '昵称' AFTER password");
                } catch (Exception e) {
                    log.debug("字段nickname已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN real_name VARCHAR(50) NULL COMMENT '真实姓名' AFTER nickname");
                } catch (Exception e) {
                    log.debug("字段real_name已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN gender INT DEFAULT 0 COMMENT '性别: 0-男, 1-女' AFTER real_name");
                } catch (Exception e) {
                    log.debug("字段gender已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN phone VARCHAR(20) NULL COMMENT '手机号' AFTER gender");
                } catch (Exception e) {
                    log.debug("字段phone已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN email VARCHAR(100) NULL COMMENT '邮箱' AFTER phone");
                } catch (Exception e) {
                    log.debug("字段email已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN avatar VARCHAR(200) NULL COMMENT '头像' AFTER email");
                } catch (Exception e) {
                    log.debug("字段avatar已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN dept_id BIGINT NULL COMMENT '部门ID' AFTER avatar");
                } catch (Exception e) {
                    log.debug("字段dept_id已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN dept_name VARCHAR(50) NULL COMMENT '部门名称' AFTER dept_id");
                } catch (Exception e) {
                    log.debug("字段dept_name已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN post VARCHAR(50) NULL COMMENT '岗位' AFTER dept_name");
                } catch (Exception e) {
                    log.debug("字段post已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN status INT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用' AFTER post");
                } catch (Exception e) {
                    log.debug("字段status已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN user_type INT DEFAULT 0 COMMENT '用户类型: 0-普通用户, 1-管理员' AFTER status");
                } catch (Exception e) {
                    log.debug("字段user_type已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN login_ip VARCHAR(50) NULL COMMENT '最后登录IP' AFTER user_type");
                } catch (Exception e) {
                    log.debug("字段login_ip已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN login_time DATETIME NULL COMMENT '最后登录时间' AFTER login_ip");
                } catch (Exception e) {
                    log.debug("字段login_time已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_user ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注' AFTER login_time");
                } catch (Exception e) {
                    log.debug("字段remark已存在或添加失败: {}", e.getMessage());
                }
                log.info("系统用户表字段检查完成");
            }
        } catch (Exception e) {
            log.error("自动修复系统用户表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复系统角色表
     */
    @PostConstruct
    public void autoFixSysRoleTable() {
        try {
            log.info("检查并修复系统角色表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_role'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("系统角色表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_role (" +
                        "role_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID', " +
                        "role_name VARCHAR(50) NOT NULL COMMENT '角色名称', " +
                        "role_code VARCHAR(50) NOT NULL COMMENT '角色编码', " +
                        "order_num INT DEFAULT 0 COMMENT '显示顺序', " +
                        "data_scope INT DEFAULT 1 COMMENT '数据范围: 1-全部, 2-本部门, 3-本部门及以下, 4-仅本人', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_role_code (role_code)" +
                        ") COMMENT '系统角色表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_order_num ON sys_role(order_num)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON sys_role(status)");
                
                // 插入默认角色
                jdbcTemplate.execute("INSERT INTO sys_role (role_name, role_code, order_num, status, create_by) VALUES ('超级管理员', 'admin', 1, 0, 'system')");
                jdbcTemplate.execute("INSERT INTO sys_role (role_name, role_code, order_num, status, create_by) VALUES ('普通用户', 'user', 2, 0, 'system')");
                
                log.info("系统角色表创建成功！");
            } else {
                log.info("系统角色表已存在，检查字段...");
                // 检查并添加缺失字段
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN role_name VARCHAR(50) NOT NULL COMMENT '角色名称' AFTER role_id");
                } catch (Exception e) {
                    log.debug("字段role_name已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN role_code VARCHAR(50) NOT NULL COMMENT '角色编码' AFTER role_name");
                } catch (Exception e) {
                    log.debug("字段role_code已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN order_num INT DEFAULT 0 COMMENT '显示顺序' AFTER role_code");
                } catch (Exception e) {
                    log.debug("字段order_num已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN data_scope INT DEFAULT 1 COMMENT '数据范围' AFTER order_num");
                } catch (Exception e) {
                    log.debug("字段data_scope已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN status INT DEFAULT 0 COMMENT '状态' AFTER data_scope");
                } catch (Exception e) {
                    log.debug("字段status已存在或添加失败: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE sys_role ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注' AFTER status");
                } catch (Exception e) {
                    log.debug("字段remark已存在或添加失败: {}", e.getMessage());
                }
                log.info("系统角色表字段检查完成");
            }
        } catch (Exception e) {
            log.error("自动修复系统角色表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复系统菜单表
     */
    @PostConstruct
    public void autoFixSysMenuTable() {
        try {
            log.info("检查并修复系统菜单表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_menu'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("系统菜单表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_menu (" +
                        "menu_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID', " +
                        "menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称', " +
                        "parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID', " +
                        "order_num INT DEFAULT 0 COMMENT '显示顺序', " +
                        "path VARCHAR(200) NULL COMMENT '路由地址', " +
                        "component VARCHAR(200) NULL COMMENT '组件路径', " +
                        "menu_type VARCHAR(1) DEFAULT 'C' COMMENT '菜单类型: M-目录, C-菜单, F-按钮', " +
                        "visible INT DEFAULT 0 COMMENT '菜单状态: 0-显示, 1-隐藏', " +
                        "status INT DEFAULT 0 COMMENT '菜单状态: 0-正常, 1-停用', " +
                        "perms VARCHAR(100) NULL COMMENT '权限标识', " +
                        "icon VARCHAR(100) NULL COMMENT '菜单图标', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "INDEX idx_parent_id (parent_id), " +
                        "INDEX idx_order_num (order_num), " +
                        "INDEX idx_status (status)" +
                        ") COMMENT '系统菜单表'";
                jdbcTemplate.execute(createTable);
                
                log.info("系统菜单表创建成功！");
            } else {
                log.info("系统菜单表已存在，检查字段...");
                // 检查并添加缺失字段
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称'"); } catch (Exception e) { log.debug("字段menu_name已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID'"); } catch (Exception e) { log.debug("字段parent_id已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN order_num INT DEFAULT 0 COMMENT '显示顺序'"); } catch (Exception e) { log.debug("字段order_num已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN path VARCHAR(200) NULL COMMENT '路由地址'"); } catch (Exception e) { log.debug("字段path已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN component VARCHAR(200) NULL COMMENT '组件路径'"); } catch (Exception e) { log.debug("字段component已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN menu_type VARCHAR(1) DEFAULT 'C' COMMENT '菜单类型'"); } catch (Exception e) { log.debug("字段menu_type已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN visible INT DEFAULT 0 COMMENT '显示状态'"); } catch (Exception e) { log.debug("字段visible已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN status INT DEFAULT 0 COMMENT '菜单状态'"); } catch (Exception e) { log.debug("字段status已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN perms VARCHAR(100) NULL COMMENT '权限标识'"); } catch (Exception e) { log.debug("字段perms已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN icon VARCHAR(100) NULL COMMENT '菜单图标'"); } catch (Exception e) { log.debug("字段icon已存在: {}", e.getMessage()); }
                try { jdbcTemplate.execute("ALTER TABLE sys_menu ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注'"); } catch (Exception e) { log.debug("字段remark已存在: {}", e.getMessage()); }
                log.info("系统菜单表字段检查完成");
            }
        } catch (Exception e) {
            log.error("自动修复系统菜单表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复用户角色关联表
     */
    @PostConstruct
    public void autoFixSysUserRoleTable() {
        try {
            log.info("检查并修复用户角色关联表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_user_role'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("用户角色关联表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_user_role (" +
                        "user_id BIGINT NOT NULL COMMENT '用户ID', " +
                        "role_id BIGINT NOT NULL COMMENT '角色ID', " +
                        "PRIMARY KEY (user_id, role_id), " +
                        "INDEX idx_role_id (role_id)" +
                        ") COMMENT '用户角色关联表'";
                jdbcTemplate.execute(createTable);
                
                // 为管理员分配超级管理员角色
                jdbcTemplate.execute("INSERT INTO sys_user_role (user_id, role_id) SELECT 1, 1 FROM sys_user WHERE username = 'admin'");
                
                log.info("用户角色关联表创建成功！");
            } else {
                log.info("用户角色关联表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复用户角色关联表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复角色菜单关联表
     */
    @PostConstruct
    public void autoFixSysRoleMenuTable() {
        try {
            log.info("检查并修复角色菜单关联表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_role_menu'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("角色菜单关联表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_role_menu (" +
                        "role_id BIGINT NOT NULL COMMENT '角色ID', " +
                        "menu_id BIGINT NOT NULL COMMENT '菜单ID', " +
                        "PRIMARY KEY (role_id, menu_id), " +
                        "INDEX idx_menu_id (menu_id)" +
                        ") COMMENT '角色菜单关联表'";
                jdbcTemplate.execute(createTable);
                
                log.info("角色菜单关联表创建成功！");
            } else {
                log.info("角色菜单关联表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复角色菜单关联表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 创建设备台账表
     */
    @PostConstruct
    public void autoFixDvMachineryTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'dv_machinery'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE dv_machinery (id BIGINT AUTO_INCREMENT PRIMARY KEY, machinery_code VARCHAR(50) NOT NULL, machinery_name VARCHAR(100) NOT NULL, machinery_type VARCHAR(50), brand VARCHAR(50), model VARCHAR(50), serial_number VARCHAR(50), workshop_id BIGINT, workshop_name VARCHAR(100), production_line_id BIGINT, production_line_name VARCHAR(100), purchase_date DATE, purchase_price DECIMAL(18,2), supplier VARCHAR(100), status VARCHAR(20) DEFAULT 'NORMAL', remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_machinery_code (machinery_code)) COMMENT '设备台账表'");
                log.info("设备台账表创建成功！");
            }
        } catch (Exception e) {
            log.debug("设备台账表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建设备点检表
     */
    @PostConstruct
    public void autoFixDvCheckTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'dv_check'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE dv_check (id BIGINT AUTO_INCREMENT PRIMARY KEY, check_no VARCHAR(50) NOT NULL, machinery_id BIGINT, machinery_code VARCHAR(50), machinery_name VARCHAR(100), check_type VARCHAR(20), check_result VARCHAR(20), status VARCHAR(20) DEFAULT 'PENDING', checker_id BIGINT, checker_name VARCHAR(50), check_date DATETIME, remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_check_no (check_no)) COMMENT '设备点检表'");
                log.info("设备点检表创建成功！");
            }
        } catch (Exception e) {
            log.debug("设备点检表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建设备维修表
     */
    @PostConstruct
    public void autoFixDvRepairTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'dv_repair'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE dv_repair (id BIGINT AUTO_INCREMENT PRIMARY KEY, repair_no VARCHAR(50) NOT NULL, machinery_id BIGINT, machinery_code VARCHAR(50), machinery_name VARCHAR(100), repair_type VARCHAR(20), fault_description VARCHAR(500), repair_content VARCHAR(500), repair_cost DECIMAL(18,2), status VARCHAR(20) DEFAULT 'PENDING', repairman_id BIGINT, repairman_name VARCHAR(50), repair_date DATETIME, remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_repair_no (repair_no)) COMMENT '设备维修表'");
                log.info("设备维修表创建成功！");
            }
        } catch (Exception e) {
            log.debug("设备维修表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建仓库管理表
     */
    @PostConstruct
    public void autoFixWmWarehouseTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_warehouse'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_warehouse (warehouse_id BIGINT AUTO_INCREMENT PRIMARY KEY, warehouse_code VARCHAR(50) NOT NULL, warehouse_name VARCHAR(100) NOT NULL, warehouse_type VARCHAR(20), manager_id BIGINT, manager_name VARCHAR(50), phone VARCHAR(20), address VARCHAR(200), status VARCHAR(1) DEFAULT '0', remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_warehouse_code (warehouse_code)) COMMENT '仓库管理表'");
                log.info("仓库管理表创建成功！");
            } else {
                // 检查并添加缺失的字段
                try {
                    jdbcTemplate.execute("ALTER TABLE wm_warehouse ADD COLUMN phone VARCHAR(20)");
                    log.info("添加字段: phone");
                } catch (Exception e) {
                    log.debug("字段phone已存在: {}", e.getMessage());
                }
                try {
                    jdbcTemplate.execute("ALTER TABLE wm_warehouse ADD COLUMN address VARCHAR(200)");
                    log.info("添加字段: address");
                } catch (Exception e) {
                    log.debug("字段address已存在: {}", e.getMessage());
                }
                // 修改status字段类型
                try {
                    jdbcTemplate.execute("ALTER TABLE wm_warehouse MODIFY COLUMN status VARCHAR(1) DEFAULT '0'");
                    log.info("修改字段status类型为VARCHAR(1)");
                } catch (Exception e) {
                    log.debug("字段status修改失败: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.debug("仓库管理表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建库存记录表
     */
    @PostConstruct
    public void autoFixWmStockTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_stock'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_stock (id BIGINT AUTO_INCREMENT PRIMARY KEY, warehouse_id BIGINT, warehouse_name VARCHAR(100), item_id BIGINT, item_code VARCHAR(50), item_name VARCHAR(100), batch_code VARCHAR(50), quantity DECIMAL(18,2) DEFAULT 0, available_qty DECIMAL(18,2) DEFAULT 0, locked_qty DECIMAL(18,2) DEFAULT 0, unit VARCHAR(20), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_stock (warehouse_id, item_id, batch_code)) COMMENT '库存记录表'");
                log.info("库存记录表创建成功！");
            }
        } catch (Exception e) {
            log.debug("库存记录表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建入库记录表及明细表
     */
    @PostConstruct
    public void autoFixWmRecptTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_recpt'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_recpt (id BIGINT AUTO_INCREMENT PRIMARY KEY, recpt_no VARCHAR(50) NOT NULL, recpt_type VARCHAR(20), source_type VARCHAR(20), source_id BIGINT, source_no VARCHAR(50), warehouse_id BIGINT, warehouse_name VARCHAR(100), status VARCHAR(20) DEFAULT 'PENDING', recpt_date DATETIME, remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_recpt_no (recpt_no)) COMMENT '入库记录表'");
                log.info("入库记录表创建成功！");
            }
            
            // 创建入库明细表
            String checkItemTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_recpt_item'";
            Integer itemTableCount = jdbcTemplate.queryForObject(checkItemTable, Integer.class);
            if (itemTableCount == null || itemTableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_recpt_item (item_id BIGINT AUTO_INCREMENT PRIMARY KEY, recpt_id BIGINT NOT NULL, item_id2 BIGINT, item_code VARCHAR(50), item_name VARCHAR(100), batch_code VARCHAR(50), quantity DECIMAL(18,2) DEFAULT 0, unit VARCHAR(20), remark VARCHAR(500), INDEX idx_recpt_id (recpt_id)) COMMENT '入库明细表'");
                log.info("入库明细表创建成功！");
            }
        } catch (Exception e) {
            log.debug("入库记录表/明细表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建出库记录表及明细表
     */
    @PostConstruct
    public void autoFixWmIssueTable() {
        try {
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_issue'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            if (tableCount == null || tableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_issue (id BIGINT AUTO_INCREMENT PRIMARY KEY, issue_no VARCHAR(50) NOT NULL, issue_type VARCHAR(20), target_type VARCHAR(20), target_id BIGINT, target_no VARCHAR(50), warehouse_id BIGINT, warehouse_name VARCHAR(100), status VARCHAR(20) DEFAULT 'PENDING', issue_date DATETIME, remark VARCHAR(500), create_by VARCHAR(64), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_by VARCHAR(64), update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY uk_issue_no (issue_no)) COMMENT '出库记录表'");
                log.info("出库记录表创建成功！");
            }
            
            // 创建出库明细表
            String checkItemTable = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_issue_item'";
            Integer itemTableCount = jdbcTemplate.queryForObject(checkItemTable, Integer.class);
            if (itemTableCount == null || itemTableCount == 0) {
                jdbcTemplate.execute("CREATE TABLE wm_issue_item (item_id BIGINT AUTO_INCREMENT PRIMARY KEY, issue_id BIGINT NOT NULL, item_id2 BIGINT, item_code VARCHAR(50), item_name VARCHAR(100), batch_code VARCHAR(50), quantity DECIMAL(18,2) DEFAULT 0, unit VARCHAR(20), remark VARCHAR(500), INDEX idx_issue_id (issue_id)) COMMENT '出库明细表'");
                log.info("出库明细表创建成功！");
            }
        } catch (Exception e) {
            log.debug("出库记录表/明细表已存在或创建失败: {}", e.getMessage());
        }
    }
    
    /**
     * 自动修复来料检验(IQC)表
     */
    @PostConstruct
    public void autoFixQcIqcTable() {
        try {
            log.info("检查并修复来料检验表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'qc_iqc'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("来料检验表不存在，正在创建...");
                String createTable = "CREATE TABLE qc_iqc (" +
                        "iqc_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '检验单ID', " +
                        "iqc_code VARCHAR(50) NOT NULL COMMENT '检验单号', " +
                        "recpt_id BIGINT NULL COMMENT '入库单ID', " +
                        "recpt_code VARCHAR(50) NULL COMMENT '入库单号', " +
                        "vendor_id BIGINT NULL COMMENT '供应商ID', " +
                        "vendor_name VARCHAR(100) NULL COMMENT '供应商名称', " +
                        "item_id BIGINT NULL COMMENT '物料ID', " +
                        "item_code VARCHAR(50) NULL COMMENT '物料编码', " +
                        "item_name VARCHAR(100) NULL COMMENT '物料名称', " +
                        "template_id BIGINT NULL COMMENT '检验模板ID', " +
                        "batch_code VARCHAR(50) NULL COMMENT '批次号', " +
                        "inspect_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '送检数量', " +
                        "sample_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '抽样数量', " +
                        "qualified_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '合格数量', " +
                        "unqualified_quantity DECIMAL(18,2) DEFAULT 0 COMMENT '不合格数量', " +
                        "inspect_result VARCHAR(20) NULL COMMENT '检验结果', " +
                        "status VARCHAR(20) DEFAULT 'PENDING' COMMENT '检验状态', " +
                        "inspector_id BIGINT NULL COMMENT '检验员ID', " +
                        "inspector_name VARCHAR(50) NULL COMMENT '检验员名称', " +
                        "inspect_date DATETIME NULL COMMENT '检验日期', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_iqc_code (iqc_code)" +
                        ") COMMENT '来料检验表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_recpt_id ON qc_iqc(recpt_id)");
                jdbcTemplate.execute("CREATE INDEX idx_vendor_id ON qc_iqc(vendor_id)");
                jdbcTemplate.execute("CREATE INDEX idx_item_id ON qc_iqc(item_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON qc_iqc(status)");
                
                log.info("来料检验表创建成功！");
            } else {
                log.info("来料检验表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复来料检验表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复过程检验(IPQC)表
     */
    @PostConstruct
    public void autoFixQcIpqcTable() {
        try {
            log.info("检查并修复过程检验表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'qc_ipqc'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("过程检验表不存在，正在创建...");
                String createTable = "CREATE TABLE qc_ipqc (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '检验ID', " +
                        "inspect_no VARCHAR(50) NOT NULL COMMENT '检验单号', " +
                        "workorder_id BIGINT NULL COMMENT '工单ID', " +
                        "workorder_no VARCHAR(50) NULL COMMENT '工单号', " +
                        "process_id BIGINT NULL COMMENT '工序ID', " +
                        "process_name VARCHAR(50) NULL COMMENT '工序名称', " +
                        "inspect_qty DECIMAL(18,2) DEFAULT 0 COMMENT '检验数量', " +
                        "qualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '合格数量', " +
                        "unqualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '不合格数量', " +
                        "inspect_result VARCHAR(20) NULL COMMENT '检验结果', " +
                        "status VARCHAR(20) DEFAULT 'PENDING' COMMENT '检验状态', " +
                        "inspect_type VARCHAR(20) NULL COMMENT '检验类型', " +
                        "inspector_id BIGINT NULL COMMENT '检验员ID', " +
                        "inspector_name VARCHAR(50) NULL COMMENT '检验员名称', " +
                        "inspect_date DATETIME NULL COMMENT '检验日期', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_inspect_no (inspect_no)" +
                        ") COMMENT '过程检验表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_workorder_id ON qc_ipqc(workorder_id)");
                jdbcTemplate.execute("CREATE INDEX idx_process_id ON qc_ipqc(process_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON qc_ipqc(status)");
                
                log.info("过程检验表创建成功！");
            } else {
                log.info("过程检验表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复过程检验表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复成品检验(OQC)表
     */
    @PostConstruct
    public void autoFixQcOqcTable() {
        try {
            log.info("检查并修复成品检验表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'qc_oqc'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("成品检验表不存在，正在创建...");
                String createTable = "CREATE TABLE qc_oqc (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '检验ID', " +
                        "inspect_no VARCHAR(50) NOT NULL COMMENT '检验单号', " +
                        "workorder_id BIGINT NULL COMMENT '工单ID', " +
                        "workorder_no VARCHAR(50) NULL COMMENT '工单号', " +
                        "client_id BIGINT NULL COMMENT '客户ID', " +
                        "client_name VARCHAR(100) NULL COMMENT '客户名称', " +
                        "item_id BIGINT NULL COMMENT '物料ID', " +
                        "item_code VARCHAR(50) NULL COMMENT '物料编码', " +
                        "item_name VARCHAR(100) NULL COMMENT '物料名称', " +
                        "inspect_qty DECIMAL(18,2) DEFAULT 0 COMMENT '检验数量', " +
                        "qualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '合格数量', " +
                        "unqualified_qty DECIMAL(18,2) DEFAULT 0 COMMENT '不合格数量', " +
                        "inspect_result VARCHAR(20) NULL COMMENT '检验结果', " +
                        "status VARCHAR(20) DEFAULT 'PENDING' COMMENT '检验状态', " +
                        "inspector_id BIGINT NULL COMMENT '检验员ID', " +
                        "inspector_name VARCHAR(50) NULL COMMENT '检验员名称', " +
                        "inspect_date DATETIME NULL COMMENT '检验日期', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_inspect_no (inspect_no)" +
                        ") COMMENT '成品检验表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_workorder_id ON qc_oqc(workorder_id)");
                jdbcTemplate.execute("CREATE INDEX idx_client_id ON qc_oqc(client_id)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON qc_oqc(status)");
                
                log.info("成品检验表创建成功！");
            } else {
                log.info("成品检验表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复成品检验表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复缺陷管理表
     */
    @PostConstruct
    public void autoFixQcDefectTable() {
        try {
            log.info("检查并修复缺陷管理表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'qc_defect'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("缺陷管理表不存在，正在创建...");
                String createTable = "CREATE TABLE qc_defect (" +
                        "defect_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '缺陷ID', " +
                        "defect_code VARCHAR(50) NOT NULL COMMENT '缺陷编码', " +
                        "defect_name VARCHAR(100) NOT NULL COMMENT '缺陷名称', " +
                        "defect_type VARCHAR(50) NULL COMMENT '缺陷类型', " +
                        "defect_level VARCHAR(50) NULL COMMENT '缺陷等级', " +
                        "process_name VARCHAR(50) NULL COMMENT '所属工序', " +
                        "status INT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_defect_code (defect_code)" +
                        ") COMMENT '缺陷管理表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_defect_type ON qc_defect(defect_type)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON qc_defect(status)");
                
                log.info("缺陷管理表创建成功！");
            } else {
                log.info("缺陷管理表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复缺陷管理表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复仓区表
     */
    @PostConstruct
    public void autoFixWmZoneTable() {
        try {
            log.info("检查并修复仓区表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_zone'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("仓区表不存在，正在创建...");
                String createTable = "CREATE TABLE wm_zone (" +
                        "zone_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '仓区ID', " +
                        "warehouse_id BIGINT NOT NULL COMMENT '仓库ID', " +
                        "warehouse_code VARCHAR(50) NULL COMMENT '仓库编码', " +
                        "warehouse_name VARCHAR(100) NULL COMMENT '仓库名称', " +
                        "zone_code VARCHAR(50) NOT NULL COMMENT '仓区编码', " +
                        "zone_name VARCHAR(100) NOT NULL COMMENT '仓区名称', " +
                        "zone_type VARCHAR(20) DEFAULT 'STORAGE' COMMENT '仓区类型: RECEIVE-收货区, SHIP-发货区, STORAGE-存储区, PICK-拣货区, SPECIAL-特殊区', " +
                        "status VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_zone_code_warehouse (zone_code, warehouse_id)" +
                        ") COMMENT '仓区表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_warehouse_id ON wm_zone(warehouse_id)");
                jdbcTemplate.execute("CREATE INDEX idx_zone_type ON wm_zone(zone_type)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON wm_zone(status)");
                
                log.info("仓区表创建成功！");
            } else {
                log.info("仓区表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复仓区表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 自动修复仓位表
     */
    @PostConstruct
    public void autoFixWmLocationTable() {
        try {
            log.info("检查并修复仓位表结构...");
            
            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'wm_location'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                log.info("仓位表不存在，正在创建...");
                String createTable = "CREATE TABLE wm_location (" +
                        "location_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '仓位ID', " +
                        "warehouse_id BIGINT NOT NULL COMMENT '仓库ID', " +
                        "warehouse_code VARCHAR(50) NULL COMMENT '仓库编码', " +
                        "warehouse_name VARCHAR(100) NULL COMMENT '仓库名称', " +
                        "zone_id BIGINT NOT NULL COMMENT '仓区ID', " +
                        "zone_code VARCHAR(50) NULL COMMENT '仓区编码', " +
                        "zone_name VARCHAR(100) NULL COMMENT '仓区名称', " +
                        "location_code VARCHAR(50) NOT NULL COMMENT '仓位编码', " +
                        "location_name VARCHAR(100) NOT NULL COMMENT '仓位名称', " +
                        "aisle_no VARCHAR(20) NULL COMMENT '巷道号', " +
                        "shelf_no VARCHAR(20) NULL COMMENT '货架号', " +
                        "layer_no VARCHAR(20) NULL COMMENT '层号', " +
                        "position_no VARCHAR(20) NULL COMMENT '位号', " +
                        "capacity INT DEFAULT 1 COMMENT '容量(托盘数)', " +
                        "used_capacity INT DEFAULT 0 COMMENT '已用容量', " +
                        "status VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用, 2-占用', " +
                        "remark VARCHAR(500) NULL COMMENT '备注', " +
                        "create_by VARCHAR(64) NULL COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) NULL COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "UNIQUE KEY uk_location_code_zone (location_code, zone_id)" +
                        ") COMMENT '仓位表'";
                jdbcTemplate.execute(createTable);
                
                jdbcTemplate.execute("CREATE INDEX idx_warehouse_id ON wm_location(warehouse_id)");
                jdbcTemplate.execute("CREATE INDEX idx_zone_id ON wm_location(zone_id)");
                jdbcTemplate.execute("CREATE INDEX idx_location_code ON wm_location(location_code)");
                jdbcTemplate.execute("CREATE INDEX idx_aisle_shelf_layer ON wm_location(aisle_no, shelf_no, layer_no)");
                jdbcTemplate.execute("CREATE INDEX idx_status ON wm_location(status)");
                
                log.info("仓位表创建成功！");
            } else {
                log.info("仓位表已存在");
            }
        } catch (Exception e) {
            log.error("自动修复仓位表失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 自动修复部门表 - 添加 code 和 remark 字段
     */
    @PostConstruct
    public void autoFixSysDeptTable() {
        try {
            log.info("检查并修复部门表结构...");

            String checkTable = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_dept'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTable, Integer.class);

            if (tableCount == null || tableCount == 0) {
                log.info("部门表不存在，正在创建...");
                String createTable = "CREATE TABLE sys_dept (" +
                        "dept_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID', " +
                        "parent_id BIGINT DEFAULT 0 COMMENT '父部门ID', " +
                        "ancestors VARCHAR(500) COMMENT '祖级列表', " +
                        "code VARCHAR(50) UNIQUE COMMENT '部门编码', " +
                        "dept_name VARCHAR(100) NOT NULL COMMENT '部门名称', " +
                        "order_num INT DEFAULT 0 COMMENT '显示顺序', " +
                        "leader VARCHAR(100) COMMENT '负责人', " +
                        "phone VARCHAR(20) COMMENT '联系电话', " +
                        "email VARCHAR(100) COMMENT '邮箱', " +
                        "status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用', " +
                        "del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志: 0-代表存在, 2-代表删除', " +
                        "create_by VARCHAR(64) COMMENT '创建者', " +
                        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', " +
                        "update_by VARCHAR(64) COMMENT '更新者', " +
                        "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', " +
                        "remark VARCHAR(500) COMMENT '备注'" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表 - carels'";
                jdbcTemplate.execute(createTable);

                // 添加默认部门
                jdbcTemplate.execute("INSERT INTO sys_dept (code, dept_name, status, create_by) VALUES " +
                        "('DEPT001', '采购部', '0', 'system'), " +
                        "('DEPT002', '生产部', '0', 'system'), " +
                        "('DEPT003', '质量部', '0', 'system'), " +
                        "('DEPT004', '仓储部', '0', 'system'), " +
                        "('DEPT005', '设备部', '0', 'system')");

                log.info("部门表创建成功！");
            } else {
                log.info("部门表已存在，检查并修复字段...");

                // 检查并添加 code 字段
                try {
                    String checkCode = "SELECT COUNT(*) FROM information_schema.COLUMNS " +
                            "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_dept' AND COLUMN_NAME = 'code'";
                    Integer codeCount = jdbcTemplate.queryForObject(checkCode, Integer.class);
                    if (codeCount == null || codeCount == 0) {
                        jdbcTemplate.execute("ALTER TABLE sys_dept ADD COLUMN code VARCHAR(50) UNIQUE COMMENT '部门编码'");
                        log.info("code 字段添加成功");
                    }
                } catch (Exception e) {
                    log.debug("code 字段已存在或添加失败: {}", e.getMessage());
                }

                // 检查并添加 remark 字段
                try {
                    String checkRemark = "SELECT COUNT(*) FROM information_schema.COLUMNS " +
                            "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_dept' AND COLUMN_NAME = 'remark'";
                    Integer remarkCount = jdbcTemplate.queryForObject(checkRemark, Integer.class);
                    if (remarkCount == null || remarkCount == 0) {
                        jdbcTemplate.execute("ALTER TABLE sys_dept ADD COLUMN remark VARCHAR(500) COMMENT '备注'");
                        log.info("remark 字段添加成功");
                    }
                } catch (Exception e) {
                    log.debug("remark 字段已存在或添加失败: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("自动修复部门表失败: {}", e.getMessage(), e);
        }
    }
}
