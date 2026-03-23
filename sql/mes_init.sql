-- ========================================================
-- MES制造执行系统数据库脚本
-- 作者: carels
-- 版本: V9.0
-- 日期: 2026-03-15
-- 版权所有: carels
-- ========================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mes_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mes_system;

-- ========================================================
-- 1. 基础资料模块 (md)
-- ========================================================

-- 物料类型表
CREATE TABLE md_item_type (
    type_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '类型ID',
    type_code VARCHAR(50) NOT NULL COMMENT '类型编码',
    type_name VARCHAR(100) NOT NULL COMMENT '类型名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父类型ID',
    type_attr VARCHAR(20) COMMENT '类型属性: RAW-原材料, SEMI-半成品, PRODUCT-产成品',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料类型表 - carels';

-- 计量单位表
CREATE TABLE md_unit_measure (
    unit_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '单位ID',
    unit_code VARCHAR(50) NOT NULL COMMENT '单位编码',
    unit_name VARCHAR(100) NOT NULL COMMENT '单位名称',
    conversion_rate DECIMAL(10,4) DEFAULT 1.0000 COMMENT '换算率',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_unit_code (unit_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计量单位表 - carels';

-- 物料信息表
CREATE TABLE md_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物料ID',
    item_code VARCHAR(50) NOT NULL COMMENT '物料编码',
    item_name VARCHAR(200) NOT NULL COMMENT '物料名称',
    item_type_id BIGINT COMMENT '物料类型ID',
    item_type_name VARCHAR(100) COMMENT '物料类型名称',
    specification VARCHAR(500) COMMENT '规格型号',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    type_attr VARCHAR(20) DEFAULT 'RAW' COMMENT '物料属性: RAW-原材料, SEMI-半成品, PRODUCT-产成品',
    barcode_type VARCHAR(20) DEFAULT 'NONE' COMMENT '条码类型: NONE-无条码, BATCH-批次条码, SN-序列号',
    warehouse_id BIGINT COMMENT '默认仓库ID',
    warehouse_name VARCHAR(100) COMMENT '默认仓库名称',
    location_id BIGINT COMMENT '默认库位ID',
    location_name VARCHAR(100) COMMENT '默认库位名称',
    safety_stock DECIMAL(18,4) DEFAULT 0 COMMENT '安全库存',
    max_stock DECIMAL(18,4) DEFAULT 0 COMMENT '最大库存',
    min_stock DECIMAL(18,4) DEFAULT 0 COMMENT '最小库存',
    purchase_price DECIMAL(18,4) DEFAULT 0 COMMENT '采购单价',
    sale_price DECIMAL(18,4) DEFAULT 0 COMMENT '销售单价',
    standard_cost DECIMAL(18,4) DEFAULT 0 COMMENT '标准成本',
    shelf_life INT DEFAULT 0 COMMENT '保质期(天)',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    inspect_flag CHAR(1) DEFAULT 'N' COMMENT '检验标志: Y-需要检验, N-不需要',
    inspect_template_id BIGINT COMMENT '默认检验模板ID',
    drawing_no VARCHAR(100) COMMENT '图号',
    brand VARCHAR(100) COMMENT '品牌',
    manufacturer VARCHAR(200) COMMENT '生产厂家',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_item_code (item_code),
    KEY idx_item_name (item_name),
    KEY idx_item_type (item_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料信息表 - carels';

-- 车间表
CREATE TABLE md_workshop (
    workshop_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '车间ID',
    workshop_code VARCHAR(50) NOT NULL COMMENT '车间编码',
    workshop_name VARCHAR(100) NOT NULL COMMENT '车间名称',
    manager_id BIGINT COMMENT '负责人ID',
    manager_name VARCHAR(100) COMMENT '负责人名称',
    phone VARCHAR(20) COMMENT '联系电话',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_workshop_code (workshop_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车间表 - carels';

-- 工作站表
CREATE TABLE md_workstation (
    workstation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工作站ID',
    workstation_code VARCHAR(50) NOT NULL COMMENT '工作站编码',
    workstation_name VARCHAR(100) NOT NULL COMMENT '工作站名称',
    workshop_id BIGINT NOT NULL COMMENT '所属车间ID',
    workshop_name VARCHAR(100) COMMENT '所属车间名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_workstation_code (workstation_code),
    KEY idx_workshop (workshop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作站表 - carels';

-- 客户表
CREATE TABLE md_client (
    client_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    client_code VARCHAR(50) NOT NULL COMMENT '客户编码',
    client_name VARCHAR(200) NOT NULL COMMENT '客户名称',
    contact_name VARCHAR(100) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(500) COMMENT '地址',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_client_code (client_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表 - carels';

-- 供应商表
CREATE TABLE md_vendor (
    vendor_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    vendor_code VARCHAR(50) NOT NULL COMMENT '供应商编码',
    vendor_name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    contact_name VARCHAR(100) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(500) COMMENT '地址',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_vendor_code (vendor_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表 - carels';

-- BOM表
CREATE TABLE md_product_bom (
    bom_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'BOM ID',
    bom_code VARCHAR(50) NOT NULL COMMENT 'BOM编码',
    item_id BIGINT NOT NULL COMMENT '产品ID',
    item_code VARCHAR(50) COMMENT '产品编码',
    item_name VARCHAR(200) COMMENT '产品名称',
    version VARCHAR(20) DEFAULT 'V1.0' COMMENT '版本号',
    quantity DECIMAL(18,4) DEFAULT 1 COMMENT '基准数量',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    is_default CHAR(1) DEFAULT 'N' COMMENT '是否默认: Y-是, N-否',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_bom_code (bom_code),
    KEY idx_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM表 - carels';

-- BOM明细表
CREATE TABLE md_product_bom_line (
    line_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    bom_id BIGINT NOT NULL COMMENT 'BOM ID',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    specification VARCHAR(500) COMMENT '规格型号',
    quantity DECIMAL(18,4) NOT NULL COMMENT '用量',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    sort_no INT DEFAULT 0 COMMENT '排序号',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_bom (bom_id),
    KEY idx_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM明细表 - carels';

-- ========================================================
-- 2. 生产管理模块 (pro)
-- ========================================================

-- 工序表
CREATE TABLE pro_process (
    process_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工序ID',
    process_code VARCHAR(50) NOT NULL COMMENT '工序编码',
    process_name VARCHAR(100) NOT NULL COMMENT '工序名称',
    workshop_id BIGINT COMMENT '所属车间ID',
    workshop_name VARCHAR(100) COMMENT '所属车间名称',
    process_type VARCHAR(20) DEFAULT 'NORMAL' COMMENT '工序类型: NORMAL-普通, INSPECT-检验, PACKAGE-包装',
    standard_hours DECIMAL(10,2) DEFAULT 0 COMMENT '标准工时(小时)',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_process_code (process_code),
    KEY idx_workshop (workshop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工序表 - carels';

-- 工艺路线表
CREATE TABLE pro_route (
    route_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工艺路线ID',
    route_code VARCHAR(50) NOT NULL COMMENT '工艺路线编码',
    route_name VARCHAR(100) NOT NULL COMMENT '工艺路线名称',
    item_id BIGINT COMMENT '适用产品ID',
    item_code VARCHAR(50) COMMENT '适用产品编码',
    item_name VARCHAR(200) COMMENT '适用产品名称',
    version VARCHAR(20) DEFAULT 'V1.0' COMMENT '版本号',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    is_default CHAR(1) DEFAULT 'N' COMMENT '是否默认: Y-是, N-否',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_route_code (route_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工艺路线表 - carels';

-- 工艺路线工序表
CREATE TABLE pro_route_process (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    route_id BIGINT NOT NULL COMMENT '工艺路线ID',
    process_id BIGINT NOT NULL COMMENT '工序ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    process_name VARCHAR(100) COMMENT '工序名称',
    sequence_no INT NOT NULL COMMENT '工序序号',
    workstation_id BIGINT COMMENT '默认工作站ID',
    workstation_name VARCHAR(100) COMMENT '默认工作站名称',
    standard_hours DECIMAL(10,2) DEFAULT 0 COMMENT '标准工时(小时)',
    inspect_flag CHAR(1) DEFAULT 'N' COMMENT '是否检验: Y-是, N-否',
    inspect_type VARCHAR(20) COMMENT '检验类型: FIRST-首检, PATROL-巡检, FINAL-完工检',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_route (route_id),
    KEY idx_process (process_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工艺路线工序表 - carels';

-- 生产工单表
CREATE TABLE pro_workorder (
    workorder_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工单ID',
    workorder_code VARCHAR(50) NOT NULL COMMENT '工单编码',
    workorder_type VARCHAR(20) DEFAULT 'STANDARD' COMMENT '工单类型: STANDARD-标准工单, REWORK-返工工单',
    item_id BIGINT NOT NULL COMMENT '产品ID',
    item_code VARCHAR(50) COMMENT '产品编码',
    item_name VARCHAR(200) COMMENT '产品名称',
    specification VARCHAR(500) COMMENT '规格型号',
    bom_id BIGINT COMMENT 'BOM版本ID',
    route_id BIGINT COMMENT '工艺路线ID',
    route_name VARCHAR(100) COMMENT '工艺路线名称',
    plan_quantity DECIMAL(18,4) NOT NULL COMMENT '计划数量',
    completed_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '完工数量',
    qualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    defective_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '不良数量',
    scrap_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '报废数量',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '工单状态: PENDING-待下达, RELEASED-已下达, PRODUCING-生产中, COMPLETED-已完成, CLOSED-已关闭',
    priority VARCHAR(20) DEFAULT 'NORMAL' COMMENT '优先级: HIGH-高, NORMAL-正常, LOW-低',
    plan_start_time DATETIME COMMENT '计划开始时间',
    plan_end_time DATETIME COMMENT '计划完成时间',
    actual_start_time DATETIME COMMENT '实际开始时间',
    actual_end_time DATETIME COMMENT '实际完成时间',
    workshop_id BIGINT COMMENT '生产车间ID',
    workshop_name VARCHAR(100) COMMENT '生产车间名称',
    team_id BIGINT COMMENT '生产班组ID',
    team_name VARCHAR(100) COMMENT '生产班组名称',
    estimated_hours DECIMAL(10,2) DEFAULT 0 COMMENT '预计工时(小时)',
    actual_hours DECIMAL(10,2) DEFAULT 0 COMMENT '实际工时(小时)',
    client_id BIGINT COMMENT '客户ID',
    client_name VARCHAR(200) COMMENT '客户名称',
    sales_order_no VARCHAR(50) COMMENT '销售订单号',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_workorder_code (workorder_code),
    KEY idx_item (item_id),
    KEY idx_status (status),
    KEY idx_plan_time (plan_start_time, plan_end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产工单表 - carels';

-- 生产任务表
CREATE TABLE pro_task (
    task_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    task_code VARCHAR(50) NOT NULL COMMENT '任务编码',
    workorder_id BIGINT NOT NULL COMMENT '工单ID',
    workorder_code VARCHAR(50) COMMENT '工单编码',
    process_id BIGINT NOT NULL COMMENT '工序ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    process_name VARCHAR(100) COMMENT '工序名称',
    workstation_id BIGINT COMMENT '工作站ID',
    workstation_name VARCHAR(100) COMMENT '工作站名称',
    plan_quantity DECIMAL(18,4) NOT NULL COMMENT '计划数量',
    completed_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '完工数量',
    qualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    defective_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '不良数量',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '任务状态: PENDING-待执行, PROCESSING-执行中, COMPLETED-已完成',
    plan_start_time DATETIME COMMENT '计划开始时间',
    plan_end_time DATETIME COMMENT '计划完成时间',
    actual_start_time DATETIME COMMENT '实际开始时间',
    actual_end_time DATETIME COMMENT '实际完成时间',
    operator_id BIGINT COMMENT '操作员ID',
    operator_name VARCHAR(100) COMMENT '操作员名称',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_task_code (task_code),
    KEY idx_workorder (workorder_id),
    KEY idx_process (process_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产任务表 - carels';

-- 生产报工表
CREATE TABLE pro_feedback (
    feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报工ID',
    feedback_code VARCHAR(50) NOT NULL COMMENT '报工单号',
    workorder_id BIGINT NOT NULL COMMENT '工单ID',
    workorder_code VARCHAR(50) COMMENT '工单编码',
    task_id BIGINT COMMENT '任务ID',
    task_code VARCHAR(50) COMMENT '任务编码',
    process_id BIGINT COMMENT '工序ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    process_name VARCHAR(100) COMMENT '工序名称',
    workstation_id BIGINT COMMENT '工作站ID',
    workstation_name VARCHAR(100) COMMENT '工作站名称',
    quantity DECIMAL(18,4) NOT NULL COMMENT '报工数量',
    qualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    defective_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '不良数量',
    scrap_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '报废数量',
    operator_id BIGINT COMMENT '操作员ID',
    operator_name VARCHAR(100) COMMENT '操作员名称',
    feedback_time DATETIME COMMENT '报工时间',
    status VARCHAR(20) DEFAULT 'SUBMITTED' COMMENT '状态: SUBMITTED-已提交, APPROVED-已审核',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_feedback_code (feedback_code),
    KEY idx_workorder (workorder_id),
    KEY idx_task (task_id),
    KEY idx_feedback_time (feedback_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产报工表 - carels';

-- 安东异常记录表
CREATE TABLE pro_andon_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    record_code VARCHAR(50) NOT NULL COMMENT '异常单号',
    workorder_id BIGINT COMMENT '工单ID',
    workorder_code VARCHAR(50) COMMENT '工单编码',
    workstation_id BIGINT COMMENT '工作站ID',
    workstation_name VARCHAR(100) COMMENT '工作站名称',
    exception_type VARCHAR(50) COMMENT '异常类型: QUALITY-质量问题, MATERIAL-物料短缺, EQUIPMENT-设备故障, OTHER-其他',
    exception_level INT DEFAULT 1 COMMENT '异常级别: 1-一般, 2-严重, 3-紧急',
    exception_desc VARCHAR(500) COMMENT '异常描述',
    reportor_id BIGINT COMMENT '报告人ID',
    reportor_name VARCHAR(100) COMMENT '报告人名称',
    report_time DATETIME COMMENT '报告时间',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(100) COMMENT '处理人名称',
    handle_time DATETIME COMMENT '处理时间',
    handle_result VARCHAR(500) COMMENT '处理结果',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_record_code (record_code),
    KEY idx_workorder (workorder_id),
    KEY idx_status (status),
    KEY idx_report_time (report_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安东异常记录表 - carels';

-- ========================================================
-- 3. 仓储管理模块 (wm)
-- ========================================================

-- 仓库表
CREATE TABLE wm_warehouse (
    warehouse_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '仓库ID',
    warehouse_code VARCHAR(50) NOT NULL COMMENT '仓库编码',
    warehouse_name VARCHAR(100) NOT NULL COMMENT '仓库名称',
    warehouse_type VARCHAR(20) DEFAULT 'RAW' COMMENT '仓库类型: RAW-原材料仓, SEMI-半成品仓, PRODUCT-成品仓, SPARE-备件仓',
    manager_id BIGINT COMMENT '负责人ID',
    manager_name VARCHAR(100) COMMENT '负责人名称',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(500) COMMENT '地址',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_warehouse_code (warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表 - carels';

-- 库区表
CREATE TABLE wm_storage_location (
    location_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库区ID',
    location_code VARCHAR(50) NOT NULL COMMENT '库区编码',
    location_name VARCHAR(100) NOT NULL COMMENT '库区名称',
    warehouse_id BIGINT NOT NULL COMMENT '所属仓库ID',
    warehouse_name VARCHAR(100) COMMENT '所属仓库名称',
    location_type VARCHAR(20) DEFAULT 'NORMAL' COMMENT '库区类型: NORMAL-普通区, BAD-不良品区, QC-待检区',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_location_code (location_code),
    KEY idx_warehouse (warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库区表 - carels';

-- 库位表
CREATE TABLE wm_storage_area (
    area_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库位ID',
    area_code VARCHAR(50) NOT NULL COMMENT '库位编码',
    area_name VARCHAR(100) NOT NULL COMMENT '库位名称',
    location_id BIGINT NOT NULL COMMENT '所属库区ID',
    location_name VARCHAR(100) COMMENT '所属库区名称',
    warehouse_id BIGINT NOT NULL COMMENT '所属仓库ID',
    warehouse_name VARCHAR(100) COMMENT '所属仓库名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_area_code (area_code),
    KEY idx_location (location_id),
    KEY idx_warehouse (warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位表 - carels';

-- 库存现有量表
CREATE TABLE wm_material_stock (
    stock_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    specification VARCHAR(500) COMMENT '规格型号',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    warehouse_name VARCHAR(100) COMMENT '仓库名称',
    location_id BIGINT COMMENT '库区ID',
    location_name VARCHAR(100) COMMENT '库区名称',
    area_id BIGINT COMMENT '库位ID',
    area_name VARCHAR(100) COMMENT '库位名称',
    batch_code VARCHAR(50) COMMENT '批次号',
    quantity_on_hand DECIMAL(18,4) DEFAULT 0 COMMENT '现有量',
    quantity_available DECIMAL(18,4) DEFAULT 0 COMMENT '可用量',
    quantity_reserved DECIMAL(18,4) DEFAULT 0 COMMENT '预留量',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    receive_date DATE COMMENT '入库日期',
    expire_date DATE COMMENT '过期日期',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态: NORMAL-正常, FROZEN-冻结, EXPIRED-过期',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_stock_unique (item_id, warehouse_id, location_id, area_id, batch_code),
    KEY idx_item (item_id),
    KEY idx_warehouse (warehouse_id),
    KEY idx_batch (batch_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存现有量表 - carels';

-- 入库单表
CREATE TABLE wm_recpt (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库单ID',
    recpt_no VARCHAR(50) NOT NULL COMMENT '入库单号',
    recpt_type VARCHAR(20) DEFAULT 'PURCHASE' COMMENT '入库类型: PURCHASE-采购入库, PRODUCT-产品入库, OUTSOURCE-委外入库, MISC-杂项入库',
    vendor_id BIGINT COMMENT '供应商ID',
    vendor_code VARCHAR(50) COMMENT '供应商编码',
    vendor_name VARCHAR(200) COMMENT '供应商名称',
    order_id BIGINT COMMENT '采购订单ID',
    order_no VARCHAR(50) COMMENT '采购订单号',
    source_type VARCHAR(20) COMMENT '来源类型',
    source_id BIGINT COMMENT '来源ID',
    source_no VARCHAR(50) COMMENT '来源单号',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    warehouse_name VARCHAR(100) COMMENT '仓库名称',
    recpt_date DATE COMMENT '入库日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '入库状态: PENDING-待处理, CONFIRMED-已确认, COMPLETED-已完成, CANCELLED-已取消',
    total_amount DECIMAL(18,4) DEFAULT 0 COMMENT '总金额',
    total_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '入库总数量',
    qualified_qty DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    unqualified_qty DECIMAL(18,4) DEFAULT 0 COMMENT '不合格数量',
    currency VARCHAR(10) DEFAULT 'CNY' COMMENT '币种',
    inspector_id BIGINT COMMENT '质检员ID',
    inspector_name VARCHAR(100) COMMENT '质检员名称',
    inspection_date DATE COMMENT '质检日期',
    inspection_remark VARCHAR(500) COMMENT '质检备注',
    audit_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '审核状态: PENDING-待审核, APPROVED-已审核, REJECTED-已拒绝',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_recpt_no (recpt_no),
    KEY idx_vendor (vendor_id),
    KEY idx_status (status),
    KEY idx_recpt_date (recpt_date),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单表 - carels';

-- 入库单明细表
CREATE TABLE wm_recpt_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    recpt_id BIGINT NOT NULL COMMENT '入库单ID',
    item_id2 BIGINT COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    batch_code VARCHAR(50) COMMENT '批次号',
    quantity DECIMAL(18,4) NOT NULL COMMENT '数量',
    unit VARCHAR(100) COMMENT '计量单位',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_recpt (recpt_id),
    KEY idx_item (item_id2),
    KEY idx_batch (batch_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细表 - carels';

-- 出库单表
CREATE TABLE wm_issue_header (
    issue_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '出库单ID',
    issue_code VARCHAR(50) NOT NULL COMMENT '出库单号',
    issue_type VARCHAR(20) DEFAULT 'PRODUCTION' COMMENT '出库类型: PRODUCTION-生产领料, SALES-销售出库, OUTSOURCE-委外发料, MISC-杂项出库',
    workorder_id BIGINT COMMENT '工单ID',
    workorder_code VARCHAR(50) COMMENT '工单编码',
    client_id BIGINT COMMENT '客户ID',
    client_name VARCHAR(200) COMMENT '客户名称',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    warehouse_name VARCHAR(100) COMMENT '仓库名称',
    issue_date DATE COMMENT '出库日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '出库状态: PENDING-待出库, PARTIAL-部分出库, COMPLETED-已完成, CANCELLED-已取消',
    operator_id BIGINT COMMENT '出库人ID',
    operator_name VARCHAR(100) COMMENT '出库人名称',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_issue_code (issue_code),
    KEY idx_workorder (workorder_id),
    KEY idx_status (status),
    KEY idx_issue_date (issue_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单表 - carels';

-- 出库单明细表
CREATE TABLE wm_issue_line (
    line_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    issue_id BIGINT NOT NULL COMMENT '出库单ID',
    line_no INT COMMENT '行号',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    specification VARCHAR(500) COMMENT '规格型号',
    batch_code VARCHAR(50) COMMENT '批次号',
    quantity DECIMAL(18,4) NOT NULL COMMENT '数量',
    quantity_issued DECIMAL(18,4) DEFAULT 0 COMMENT '已出库数量',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    warehouse_id BIGINT COMMENT '仓库ID',
    warehouse_name VARCHAR(100) COMMENT '仓库名称',
    location_id BIGINT COMMENT '库区ID',
    location_name VARCHAR(100) COMMENT '库区名称',
    area_id BIGINT COMMENT '库位ID',
    area_name VARCHAR(100) COMMENT '库位名称',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待出库, COMPLETED-已完成',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_issue (issue_id),
    KEY idx_item (item_id),
    KEY idx_batch (batch_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细表 - carels';

-- 批次表
CREATE TABLE wm_batch (
    batch_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '批次ID',
    batch_code VARCHAR(50) NOT NULL COMMENT '批次号',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    supplier_batch_code VARCHAR(50) COMMENT '供应商批次号',
    production_date DATE COMMENT '生产日期',
    expire_date DATE COMMENT '过期日期',
    quantity DECIMAL(18,4) DEFAULT 0 COMMENT '数量',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    source_type VARCHAR(20) COMMENT '来源类型: PURCHASE-采购, PRODUCTION-生产',
    source_code VARCHAR(50) COMMENT '来源单号',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态: NORMAL-正常, EXPIRED-过期, CONSUMED-已消耗',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_batch_code (batch_code, item_id),
    KEY idx_item (item_id),
    KEY idx_production_date (production_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次表 - carels';

-- ========================================================
-- 4. 质量管理模块 (qc)
-- ========================================================

-- 检验模板表
CREATE TABLE qc_template (
    template_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    inspect_type VARCHAR(20) DEFAULT 'IQC' COMMENT '检验类型: IQC-来料检验, IPQC-过程检验, OQC-出货检验',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_template_code (template_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检验模板表 - carels';

-- 检验项目表
CREATE TABLE qc_index (
    index_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    index_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    index_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    index_type VARCHAR(20) DEFAULT 'QUALITATIVE' COMMENT '项目类型: QUALITATIVE-定性, QUANTITATIVE-定量',
    standard_value VARCHAR(200) COMMENT '标准值',
    max_value DECIMAL(18,4) COMMENT '最大值',
    min_value DECIMAL(18,4) COMMENT '最小值',
    unit_id BIGINT COMMENT '计量单位ID',
    unit_name VARCHAR(100) COMMENT '计量单位名称',
    inspect_method VARCHAR(200) COMMENT '检验方法',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_index_code (index_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检验项目表 - carels';

-- 检验模板项目表
CREATE TABLE qc_template_index (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    index_id BIGINT NOT NULL COMMENT '项目ID',
    index_code VARCHAR(50) COMMENT '项目编码',
    index_name VARCHAR(100) COMMENT '项目名称',
    sort_no INT DEFAULT 0 COMMENT '排序号',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_template (template_id),
    KEY idx_index (index_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检验模板项目表 - carels';

-- 来料检验单表
CREATE TABLE qc_iqc (
    iqc_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '检验单ID',
    iqc_code VARCHAR(50) NOT NULL COMMENT '检验单号',
    recpt_id BIGINT COMMENT '入库单ID',
    recpt_code VARCHAR(50) COMMENT '入库单号',
    vendor_id BIGINT COMMENT '供应商ID',
    vendor_name VARCHAR(200) COMMENT '供应商名称',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    template_id BIGINT COMMENT '检验模板ID',
    batch_code VARCHAR(50) COMMENT '批次号',
    inspect_quantity DECIMAL(18,4) NOT NULL COMMENT '送检数量',
    sample_quantity DECIMAL(18,4) COMMENT '抽样数量',
    qualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    unqualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '不合格数量',
    inspect_result VARCHAR(20) COMMENT '检验结果: PASSED-合格, FAILED-不合格, CONCESSION-特采',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '检验状态: PENDING-待检验, INSPECTING-检验中, COMPLETED-已完成',
    inspector_id BIGINT COMMENT '检验员ID',
    inspector_name VARCHAR(100) COMMENT '检验员名称',
    inspect_date DATE COMMENT '检验日期',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_iqc_code (iqc_code),
    KEY idx_recpt (recpt_id),
    KEY idx_item (item_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='来料检验单表 - carels';

-- 来料检验单明细表
CREATE TABLE qc_iqc_line (
    line_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    iqc_id BIGINT NOT NULL COMMENT '检验单ID',
    line_no INT COMMENT '行号',
    index_id BIGINT COMMENT '检验项目ID',
    index_code VARCHAR(50) COMMENT '项目编码',
    index_name VARCHAR(100) COMMENT '项目名称',
    standard_value VARCHAR(200) COMMENT '标准值',
    max_value DECIMAL(18,4) COMMENT '最大值',
    min_value DECIMAL(18,4) COMMENT '最小值',
    inspect_value VARCHAR(200) COMMENT '检验值',
    inspect_result VARCHAR(20) COMMENT '检验结果: PASSED-合格, FAILED-不合格',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_iqc (iqc_id),
    KEY idx_index (index_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='来料检验单明细表 - carels';

-- 过程检验单表
CREATE TABLE qc_ipqc (
    ipqc_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '检验单ID',
    ipqc_code VARCHAR(50) NOT NULL COMMENT '检验单号',
    workorder_id BIGINT COMMENT '工单ID',
    workorder_code VARCHAR(50) COMMENT '工单编码',
    task_id BIGINT COMMENT '任务ID',
    task_code VARCHAR(50) COMMENT '任务编码',
    process_id BIGINT COMMENT '工序ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    process_name VARCHAR(100) COMMENT '工序名称',
    workstation_id BIGINT COMMENT '工作站ID',
    workstation_name VARCHAR(100) COMMENT '工作站名称',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    template_id BIGINT COMMENT '检验模板ID',
    inspect_type VARCHAR(20) COMMENT '检验类型: FIRST-首检, PATROL-巡检, FINAL-完工检',
    inspect_quantity DECIMAL(18,4) NOT NULL COMMENT '检验数量',
    qualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '合格数量',
    unqualified_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '不合格数量',
    inspect_result VARCHAR(20) COMMENT '检验结果: PASSED-合格, FAILED-不合格',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '检验状态: PENDING-待检验, INSPECTING-检验中, COMPLETED-已完成',
    inspector_id BIGINT COMMENT '检验员ID',
    inspector_name VARCHAR(100) COMMENT '检验员名称',
    inspect_date DATE COMMENT '检验日期',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_ipqc_code (ipqc_code),
    KEY idx_workorder (workorder_id),
    KEY idx_item (item_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='过程检验单表 - carels';

-- 缺陷类型表
CREATE TABLE qc_defect (
    defect_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '缺陷ID',
    defect_code VARCHAR(50) NOT NULL COMMENT '缺陷编码',
    defect_name VARCHAR(100) NOT NULL COMMENT '缺陷名称',
    defect_type VARCHAR(50) COMMENT '缺陷类型',
    severity VARCHAR(20) DEFAULT 'NORMAL' COMMENT '严重程度: MINOR-轻微, NORMAL-一般, MAJOR-严重, CRITICAL-致命',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_defect_code (defect_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缺陷类型表 - carels';

-- 缺陷记录表
CREATE TABLE qc_defect_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    source_type VARCHAR(20) COMMENT '来源类型: IQC-来料, IPQC-过程, OQC-出货',
    source_id BIGINT COMMENT '来源单ID',
    source_code VARCHAR(50) COMMENT '来源单号',
    item_id BIGINT COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    batch_code VARCHAR(50) COMMENT '批次号',
    defect_id BIGINT COMMENT '缺陷类型ID',
    defect_code VARCHAR(50) COMMENT '缺陷编码',
    defect_name VARCHAR(100) COMMENT '缺陷名称',
    defect_quantity DECIMAL(18,4) DEFAULT 0 COMMENT '缺陷数量',
    defect_desc VARCHAR(500) COMMENT '缺陷描述',
    handle_method VARCHAR(200) COMMENT '处理方法',
    handle_result VARCHAR(20) COMMENT '处理结果: RETURN-退货, REWORK-返工, SCRAP-报废, CONCESSION-特采',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_source (source_type, source_id),
    KEY idx_item (item_id),
    KEY idx_defect (defect_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缺陷记录表 - carels';

-- ========================================================
-- 5. 设备管理模块 (dv)
-- ========================================================

-- 设备类型表
CREATE TABLE dv_machinery_type (
    type_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '类型ID',
    type_code VARCHAR(50) NOT NULL COMMENT '类型编码',
    type_name VARCHAR(100) NOT NULL COMMENT '类型名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父类型ID',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备类型表 - carels';

-- 设备表
CREATE TABLE dv_machinery (
    machinery_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '设备ID',
    machinery_code VARCHAR(50) NOT NULL COMMENT '设备编码',
    machinery_name VARCHAR(100) NOT NULL COMMENT '设备名称',
    type_id BIGINT COMMENT '设备类型ID',
    type_name VARCHAR(100) COMMENT '设备类型名称',
    brand VARCHAR(100) COMMENT '品牌',
    model VARCHAR(100) COMMENT '型号',
    specification VARCHAR(500) COMMENT '规格参数',
    serial_no VARCHAR(100) COMMENT '序列号',
    workshop_id BIGINT COMMENT '所属车间ID',
    workshop_name VARCHAR(100) COMMENT '所属车间名称',
    workstation_id BIGINT COMMENT '所属工作站ID',
    workstation_name VARCHAR(100) COMMENT '所属工作站名称',
    purchase_date DATE COMMENT '购置日期',
    purchase_price DECIMAL(18,4) COMMENT '购置价格',
    warranty_expire_date DATE COMMENT '保修到期日',
    status VARCHAR(20) DEFAULT 'IDLE' COMMENT '状态: IDLE-闲置, RUNNING-运行中, MAINTENANCE-维护中, REPAIR-维修中, SCRAP-报废',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_machinery_code (machinery_code),
    KEY idx_type (type_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表 - carels';

-- 点检项目表
CREATE TABLE dv_subject (
    subject_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    subject_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    subject_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    subject_type VARCHAR(20) DEFAULT 'DAILY' COMMENT '点检类型: DAILY-日常, WEEKLY-周检, MONTHLY-月检',
    check_method VARCHAR(200) COMMENT '检查方法',
    standard_value VARCHAR(200) COMMENT '标准值',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_subject_code (subject_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点检项目表 - carels';

-- 点检计划表
CREATE TABLE dv_check_plan (
    plan_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    plan_code VARCHAR(50) NOT NULL COMMENT '计划编码',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    machinery_id BIGINT NOT NULL COMMENT '设备ID',
    machinery_code VARCHAR(50) COMMENT '设备编码',
    machinery_name VARCHAR(100) COMMENT '设备名称',
    subject_id BIGINT NOT NULL COMMENT '点检项目ID',
    subject_code VARCHAR(50) COMMENT '项目编码',
    subject_name VARCHAR(100) COMMENT '项目名称',
    check_cycle VARCHAR(20) COMMENT '点检周期: DAY-每天, WEEK-每周, MONTH-每月',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_plan_code (plan_code),
    KEY idx_machinery (machinery_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点检计划表 - carels';

-- 点检记录表
CREATE TABLE dv_check_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    record_code VARCHAR(50) NOT NULL COMMENT '记录单号',
    plan_id BIGINT COMMENT '点检计划ID',
    machinery_id BIGINT NOT NULL COMMENT '设备ID',
    machinery_code VARCHAR(50) COMMENT '设备编码',
    machinery_name VARCHAR(100) COMMENT '设备名称',
    check_date DATE COMMENT '点检日期',
    check_result VARCHAR(20) COMMENT '点检结果: NORMAL-正常, ABNORMAL-异常',
    checker_id BIGINT COMMENT '点检人ID',
    checker_name VARCHAR(100) COMMENT '点检人名称',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_record_code (record_code),
    KEY idx_machinery (machinery_id),
    KEY idx_check_date (check_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点检记录表 - carels';

-- 点检记录明细表
CREATE TABLE dv_check_record_line (
    line_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    record_id BIGINT NOT NULL COMMENT '点检记录ID',
    subject_id BIGINT NOT NULL COMMENT '点检项目ID',
    subject_code VARCHAR(50) COMMENT '项目编码',
    subject_name VARCHAR(100) COMMENT '项目名称',
    standard_value VARCHAR(200) COMMENT '标准值',
    check_value VARCHAR(200) COMMENT '点检值',
    check_result VARCHAR(20) COMMENT '点检结果: NORMAL-正常, ABNORMAL-异常',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_record (record_id),
    KEY idx_subject (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点检记录明细表 - carels';

-- 维修记录表
CREATE TABLE dv_repair (
    repair_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '维修ID',
    repair_code VARCHAR(50) NOT NULL COMMENT '维修单号',
    machinery_id BIGINT NOT NULL COMMENT '设备ID',
    machinery_code VARCHAR(50) COMMENT '设备编码',
    machinery_name VARCHAR(100) COMMENT '设备名称',
    fault_desc VARCHAR(500) COMMENT '故障描述',
    fault_reason VARCHAR(500) COMMENT '故障原因',
    repair_method VARCHAR(500) COMMENT '维修方法',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    repair_hours DECIMAL(10,2) COMMENT '维修时长(小时)',
    repair_cost DECIMAL(18,4) COMMENT '维修费用',
    repairor_id BIGINT COMMENT '维修人ID',
    repairor_name VARCHAR(100) COMMENT '维修人名称',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待维修, REPAIRING-维修中, COMPLETED-已完成',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_repair_code (repair_code),
    KEY idx_machinery (machinery_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表 - carels';

-- ========================================================
-- 6. 排班管理模块 (cal)
-- ========================================================

-- 日历表
CREATE TABLE cal_calendar (
    calendar_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日历ID',
    calendar_date DATE NOT NULL COMMENT '日期',
    year INT COMMENT '年',
    month INT COMMENT '月',
    day INT COMMENT '日',
    week_day INT COMMENT '星期(1-7)',
    week_of_year INT COMMENT '周数',
    is_workday CHAR(1) DEFAULT 'Y' COMMENT '是否工作日: Y-是, N-否',
    holiday_name VARCHAR(100) COMMENT '节假日名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_calendar_date (calendar_date),
    KEY idx_year_month (year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日历表 - carels';

-- 班次表
CREATE TABLE cal_shift (
    shift_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班次ID',
    shift_code VARCHAR(50) NOT NULL COMMENT '班次编码',
    shift_name VARCHAR(100) NOT NULL COMMENT '班次名称',
    start_time TIME COMMENT '开始时间',
    end_time TIME COMMENT '结束时间',
    rest_start_time TIME COMMENT '休息开始时间',
    rest_end_time TIME COMMENT '休息结束时间',
    work_hours DECIMAL(5,2) COMMENT '工作时长(小时)',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_shift_code (shift_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班次表 - carels';

-- 班组表
CREATE TABLE cal_team (
    team_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班组ID',
    team_code VARCHAR(50) NOT NULL COMMENT '班组编码',
    team_name VARCHAR(100) NOT NULL COMMENT '班组名称',
    workshop_id BIGINT COMMENT '所属车间ID',
    workshop_name VARCHAR(100) COMMENT '所属车间名称',
    leader_id BIGINT COMMENT '班组长ID',
    leader_name VARCHAR(100) COMMENT '班组长名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_team_code (team_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班组表 - carels';

-- 排班计划表
CREATE TABLE cal_plan (
    plan_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    plan_code VARCHAR(50) NOT NULL COMMENT '计划编码',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    plan_date DATE NOT NULL COMMENT '排班日期',
    shift_id BIGINT NOT NULL COMMENT '班次ID',
    shift_code VARCHAR(50) COMMENT '班次编码',
    shift_name VARCHAR(100) COMMENT '班次名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_plan_unique (plan_date, shift_id),
    KEY idx_plan_date (plan_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班计划表 - carels';

-- ========================================================
-- 7. 成本管理模块 (cost)
-- ========================================================

-- 物料成本表
CREATE TABLE cost_material_cost (
    cost_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成本ID',
    item_id BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    standard_cost DECIMAL(18,6) DEFAULT 0 COMMENT '标准成本',
    current_cost DECIMAL(18,6) DEFAULT 0 COMMENT '当前成本',
    avg_cost DECIMAL(18,6) DEFAULT 0 COMMENT '平均成本',
    last_cost DECIMAL(18,6) DEFAULT 0 COMMENT '上次成本',
    currency VARCHAR(10) DEFAULT 'CNY' COMMENT '币种',
    effective_date DATE COMMENT '生效日期',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_item_cost (item_id),
    KEY idx_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料成本表 - carels';

-- 工序价格表
CREATE TABLE cost_process_price (
    price_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '价格ID',
    process_id BIGINT NOT NULL COMMENT '工序ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    process_name VARCHAR(100) COMMENT '工序名称',
    price DECIMAL(18,6) DEFAULT 0 COMMENT '工序单价',
    unit VARCHAR(50) COMMENT '计价单位',
    currency VARCHAR(10) DEFAULT 'CNY' COMMENT '币种',
    effective_date DATE COMMENT '生效日期',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_process_price (process_id),
    KEY idx_process (process_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工序价格表 - carels';

-- ========================================================
-- 8. 系统管理模块 (system)
-- ========================================================

-- 部门表
CREATE TABLE sys_dept (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    ancestors VARCHAR(500) COMMENT '祖级列表',
    code VARCHAR(50) UNIQUE COMMENT '部门编码',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(100) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志: 0-代表存在, 2-代表删除',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表 - carels';

-- 用户表
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    dept_id BIGINT COMMENT '部门ID',
    user_name VARCHAR(100) NOT NULL COMMENT '用户账号',
    nick_name VARCHAR(100) NOT NULL COMMENT '用户昵称',
    user_type VARCHAR(20) DEFAULT '00' COMMENT '用户类型: 00-系统用户',
    email VARCHAR(100) COMMENT '用户邮箱',
    phone VARCHAR(20) COMMENT '手机号码',
    sex CHAR(1) DEFAULT '0' COMMENT '用户性别: 0-男, 1-女, 2-未知',
    avatar VARCHAR(200) COMMENT '头像地址',
    password VARCHAR(100) COMMENT '密码',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志: 0-代表存在, 2-代表删除',
    login_ip VARCHAR(128) COMMENT '最后登录IP',
    login_date DATETIME COMMENT '最后登录时间',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_user_name (user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表 - carels';

-- 角色表
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    role_sort INT COMMENT '显示顺序',
    data_scope CHAR(1) DEFAULT '1' COMMENT '数据范围: 1-全部数据权限, 2-自定数据权限, 3-本部门数据权限, 4-本部门及以下数据权限, 5-仅本人数据权限',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志: 0-代表存在, 2-代表删除',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表 - carels';

-- 菜单表
CREATE TABLE sys_menu (
    menu_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(100) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    is_frame INT DEFAULT 1 COMMENT '是否为外链: 0-是, 1-否',
    is_cache INT DEFAULT 0 COMMENT '是否缓存: 0-缓存, 1-不缓存',
    menu_type CHAR(1) COMMENT '菜单类型: M-目录, C-菜单, F-按钮',
    visible CHAR(1) DEFAULT '0' COMMENT '菜单状态: 0-显示, 1-隐藏',
    status CHAR(1) DEFAULT '0' COMMENT '菜单状态: 0-正常, 1-停用',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(100) COMMENT '菜单图标',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表 - carels';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表 - carels';

-- 角色菜单关联表
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表 - carels';

-- 岗位表
CREATE TABLE sys_post (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    post_code VARCHAR(64) NOT NULL COMMENT '岗位编码',
    post_name VARCHAR(100) NOT NULL COMMENT '岗位名称',
    post_sort INT NOT NULL COMMENT '显示顺序',
    status CHAR(1) NOT NULL COMMENT '状态: 0-正常, 1-停用',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_post_code (post_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表 - carels';

-- 用户岗位关联表
CREATE TABLE sys_user_post (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (user_id, post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户岗位关联表 - carels';

-- 字典类型表
CREATE TABLE sys_dict_type (
    dict_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典主键',
    dict_name VARCHAR(100) DEFAULT '' COMMENT '字典名称',
    dict_type VARCHAR(100) DEFAULT '' COMMENT '字典类型',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表 - carels';

-- 字典数据表
CREATE TABLE sys_dict_data (
    dict_code BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典编码',
    dict_sort INT DEFAULT 0 COMMENT '字典排序',
    dict_label VARCHAR(100) DEFAULT '' COMMENT '字典标签',
    dict_value VARCHAR(100) DEFAULT '' COMMENT '字典键值',
    dict_type VARCHAR(100) DEFAULT '' COMMENT '字典类型',
    css_class VARCHAR(100) COMMENT '样式属性',
    list_class VARCHAR(100) COMMENT '表格回显样式',
    is_default CHAR(1) DEFAULT 'N' COMMENT '是否默认: Y-是, N-否',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-正常, 1-停用',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表 - carels';

-- 参数配置表
CREATE TABLE sys_config (
    config_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '参数主键',
    config_name VARCHAR(100) DEFAULT '' COMMENT '参数名称',
    config_key VARCHAR(100) DEFAULT '' COMMENT '参数键名',
    config_value VARCHAR(500) DEFAULT '' COMMENT '参数键值',
    config_type CHAR(1) DEFAULT 'N' COMMENT '系统内置: Y-是, N-否',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表 - carels';

-- 操作日志表
CREATE TABLE sys_oper_log (
    oper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志主键',
    title VARCHAR(100) DEFAULT '' COMMENT '模块标题',
    business_type INT DEFAULT 0 COMMENT '业务类型: 0-其它, 1-新增, 2-修改, 3-删除, 4-授权, 5-导出, 6-导入, 7-强退, 8-生成代码, 9-清空数据',
    method VARCHAR(200) COMMENT '方法名称',
    request_method VARCHAR(10) COMMENT '请求方式',
    operator_type INT DEFAULT 0 COMMENT '操作类别: 0-其它, 1-后台用户, 2-手机端用户',
    oper_name VARCHAR(50) COMMENT '操作人员',
    dept_name VARCHAR(100) COMMENT '部门名称',
    oper_url VARCHAR(500) COMMENT '请求URL',
    oper_ip VARCHAR(128) COMMENT '主机地址',
    oper_location VARCHAR(255) COMMENT '操作地点',
    oper_param VARCHAR(2000) COMMENT '请求参数',
    json_result VARCHAR(2000) COMMENT '返回参数',
    status INT DEFAULT 0 COMMENT '操作状态: 0-正常, 1-异常',
    error_msg VARCHAR(2000) COMMENT '错误消息',
    oper_time DATETIME COMMENT '操作时间',
    cost_time BIGINT DEFAULT 0 COMMENT '消耗时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表 - carels';

-- 登录日志表
CREATE TABLE sys_logininfor (
    info_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '访问ID',
    user_name VARCHAR(50) COMMENT '用户账号',
    ipaddr VARCHAR(128) COMMENT '登录IP地址',
    login_location VARCHAR(255) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    status CHAR(1) COMMENT '登录状态: 0-成功, 1-失败',
    msg VARCHAR(255) COMMENT '提示消息',
    login_time DATETIME COMMENT '访问时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表 - carels';

-- ========================================================
-- 9. 初始化数据
-- ========================================================

-- 初始化用户数据
INSERT INTO sys_user (user_id, user_name, nick_name, password, status) VALUES 
(1, 'admin', '系统管理员', '$2a$10$7JB720yubVS39v6YdelEs.d5eGXuwJ.k3yO9xV.cI9vZqlHdkK/K', '0');

-- 初始化角色数据
INSERT INTO sys_role (role_id, role_name, role_key, status) VALUES 
(1, '系统管理员', 'admin', '0'),
(2, '生产主管', 'pro_manager', '0'),
(3, '质量主管', 'qc_manager', '0'),
(4, '仓管员', 'warehouse_keeper', '0'),
(5, '操作员', 'operator', '0');

-- 初始化用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 初始化字典类型
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status) VALUES
(1, '用户性别', 'sys_user_sex', '0'),
(2, '系统状态', 'sys_normal_disable', '0'),
(3, '物料属性', 'mes_type_attr', '0'),
(4, '工单状态', 'mes_workorder_status', '0'),
(5, '入库类型', 'mes_recpt_type', '0'),
(6, '出库类型', 'mes_issue_type', '0'),
(7, '检验类型', 'mes_inspect_type', '0'),
(8, '检验结果', 'mes_inspect_result', '0');

-- 初始化字典数据
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, status) VALUES
(1, 1, '男', '0', 'sys_user_sex', '0'),
(2, 2, '女', '1', 'sys_user_sex', '0'),
(3, 3, '未知', '2', 'sys_user_sex', '0'),
(4, 1, '正常', '0', 'sys_normal_disable', '0'),
(5, 2, '停用', '1', 'sys_normal_disable', '0'),
(6, 1, '原材料', 'RAW', 'mes_type_attr', '0'),
(7, 2, '半成品', 'SEMI', 'mes_type_attr', '0'),
(8, 3, '产成品', 'PRODUCT', 'mes_type_attr', '0'),
(9, 1, '待下达', 'PENDING', 'mes_workorder_status', '0'),
(10, 2, '已下达', 'RELEASED', 'mes_workorder_status', '0'),
(11, 3, '生产中', 'PRODUCING', 'mes_workorder_status', '0'),
(12, 4, '已完成', 'COMPLETED', 'mes_workorder_status', '0'),
(13, 5, '已关闭', 'CLOSED', 'mes_workorder_status', '0'),
(14, 1, '采购入库', 'PURCHASE', 'mes_recpt_type', '0'),
(15, 2, '产品入库', 'PRODUCT', 'mes_recpt_type', '0'),
(16, 3, '委外入库', 'OUTSOURCE', 'mes_recpt_type', '0'),
(17, 4, '杂项入库', 'MISC', 'mes_recpt_type', '0'),
(18, 1, '生产领料', 'PRODUCTION', 'mes_issue_type', '0'),
(19, 2, '销售出库', 'SALES', 'mes_issue_type', '0'),
(20, 3, '委外发料', 'OUTSOURCE', 'mes_issue_type', '0'),
(21, 4, '杂项出库', 'MISC', 'mes_issue_type', '0'),
(22, 1, '来料检验', 'IQC', 'mes_inspect_type', '0'),
(23, 2, '过程检验', 'IPQC', 'mes_inspect_type', '0'),
(24, 3, '出货检验', 'OQC', 'mes_inspect_type', '0'),
(25, 1, '合格', 'PASSED', 'mes_inspect_result', '0'),
(26, 2, '不合格', 'FAILED', 'mes_inspect_result', '0'),
(27, 3, '特采', 'CONCESSION', 'mes_inspect_result', '0');

-- 初始化计量单位
INSERT INTO md_unit_measure (unit_id, unit_code, unit_name, status) VALUES
(1, 'PC', '件', '0'),
(2, 'KG', '千克', '0'),
(3, 'M', '米', '0'),
(4, 'SET', '套', '0'),
(5, 'BOX', '箱', '0');

-- 初始化物料类型
INSERT INTO md_item_type (type_id, type_code, type_name, type_attr, status) VALUES
(1, 'RAW', '原材料', 'RAW', '0'),
(2, 'SEMI', '半成品', 'SEMI', '0'),
(3, 'PRODUCT', '产成品', 'PRODUCT', '0'),
(4, 'SPARE', '备件', 'RAW', '0');

-- 初始化仓库
INSERT INTO wm_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, status) VALUES
(1, 'WH01', '原材料仓', 'RAW', '0'),
(2, 'WH02', '半成品仓', 'SEMI', '0'),
(3, 'WH03', '成品仓', 'PRODUCT', '0');

-- 打印完成信息
SELECT 'MES系统数据库初始化完成 - carels版权所有' AS message;
