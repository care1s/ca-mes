-- MES 新模块数据库表创建脚本
-- 创建日期: 2026-03-25
-- 作者: carels

-- ===========================================
-- 设备运维模块 (dv)
-- ===========================================

-- 设备点检表
CREATE TABLE IF NOT EXISTS `dv_check` (
  `check_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '点检ID',
  `check_no` varchar(50) NOT NULL COMMENT '点检单号',
  `machinery_id` bigint(20) DEFAULT NULL COMMENT '设备ID',
  `machinery_code` varchar(50) DEFAULT NULL COMMENT '设备编码',
  `machinery_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `check_type` varchar(20) DEFAULT NULL COMMENT '点检类型: DAILY-日常, WEEKLY-周检, MONTHLY-月检',
  `check_date` date DEFAULT NULL COMMENT '点检日期',
  `checker_id` bigint(20) DEFAULT NULL COMMENT '点检人ID',
  `checker_name` varchar(50) DEFAULT NULL COMMENT '点检人姓名',
  `check_result` varchar(20) DEFAULT NULL COMMENT '点检结果: NORMAL-正常, ABNORMAL-异常',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-待点检, 1-已完成',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`check_id`),
  KEY `idx_check_no` (`check_no`),
  KEY `idx_machinery` (`machinery_id`),
  KEY `idx_check_date` (`check_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备点检表';

-- 设备维修表
CREATE TABLE IF NOT EXISTS `dv_repair` (
  `repair_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '维修ID',
  `repair_no` varchar(50) NOT NULL COMMENT '维修单号',
  `machinery_id` bigint(20) DEFAULT NULL COMMENT '设备ID',
  `machinery_code` varchar(50) DEFAULT NULL COMMENT '设备编码',
  `machinery_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `repair_type` varchar(20) DEFAULT NULL COMMENT '维修类型: BREAKDOWN-故障维修, MAINTAIN-保养维修',
  `fault_desc` varchar(500) DEFAULT NULL COMMENT '故障描述',
  `repair_status` varchar(10) DEFAULT '0' COMMENT '状态: 0-待维修, 1-维修中, 2-已完成',
  `repairman_id` bigint(20) DEFAULT NULL COMMENT '维修人ID',
  `repairman_name` varchar(50) DEFAULT NULL COMMENT '维修人姓名',
  `assign_time` datetime DEFAULT NULL COMMENT '派工时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `repair_content` varchar(1000) DEFAULT NULL COMMENT '维修内容',
  `repair_cost` decimal(10,2) DEFAULT NULL COMMENT '维修费用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`repair_id`),
  KEY `idx_repair_no` (`repair_no`),
  KEY `idx_machinery` (`machinery_id`),
  KEY `idx_status` (`repair_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备维修表';

-- ===========================================
-- 安东异常模块 (pro_andon)
-- ===========================================

-- 安东异常类型表
CREATE TABLE IF NOT EXISTS `pro_andon_type` (
  `type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `type_code` varchar(50) NOT NULL COMMENT '类型编码',
  `type_name` varchar(100) NOT NULL COMMENT '类型名称',
  `type_level` varchar(20) DEFAULT NULL COMMENT '级别: LOW-低, MEDIUM-中, HIGH-高, CRITICAL-紧急',
  `default_handler_id` bigint(20) DEFAULT NULL COMMENT '默认处理人ID',
  `default_handler_name` varchar(50) DEFAULT NULL COMMENT '默认处理人姓名',
  `timeout_minutes` int(11) DEFAULT NULL COMMENT '超时时间(分钟)',
  `escalate_level` int(11) DEFAULT NULL COMMENT '升级级别',
  `color_code` varchar(20) DEFAULT NULL COMMENT '颜色标识',
  `status` varchar(10) DEFAULT '1' COMMENT '状态: 0-停用, 1-启用',
  `sort_no` int(11) DEFAULT '0' COMMENT '排序号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`type_id`),
  KEY `idx_type_code` (`type_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安东异常类型表';

-- 安东异常记录表
CREATE TABLE IF NOT EXISTS `pro_andon_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_no` varchar(50) NOT NULL COMMENT '记录编号',
  `type_id` bigint(20) DEFAULT NULL COMMENT '类型ID',
  `type_name` varchar(100) DEFAULT NULL COMMENT '类型名称',
  `type_level` varchar(20) DEFAULT NULL COMMENT '级别',
  `workorder_id` bigint(20) DEFAULT NULL COMMENT '工单ID',
  `workorder_no` varchar(50) DEFAULT NULL COMMENT '工单号',
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
  `workstation_id` bigint(20) DEFAULT NULL COMMENT '工位ID',
  `workstation_name` varchar(100) DEFAULT NULL COMMENT '工位名称',
  `description` varchar(500) DEFAULT NULL COMMENT '异常描述',
  `images` varchar(1000) DEFAULT NULL COMMENT '图片URL列表',
  `reporter_id` bigint(20) DEFAULT NULL COMMENT '上报人ID',
  `reporter_name` varchar(50) DEFAULT NULL COMMENT '上报人姓名',
  `report_time` datetime DEFAULT NULL COMMENT '上报时间',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `accept_time` datetime DEFAULT NULL COMMENT '受理时间',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, ACCEPTED-已受理, HANDLING-处理中, COMPLETED-已完成, CLOSED-已关闭',
  `escalate_level` int(11) DEFAULT '0' COMMENT '升级级别',
  `timeout_time` datetime DEFAULT NULL COMMENT '超时时间',
  `close_reason` varchar(500) DEFAULT NULL COMMENT '关闭原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_record_no` (`record_no`),
  KEY `idx_type_id` (`type_id`),
  KEY `idx_workorder` (`workorder_id`),
  KEY `idx_status` (`status`),
  KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安东异常记录表';

-- ===========================================
-- 销售管理模块 (sale)
-- ===========================================

-- 销售出库单表
CREATE TABLE IF NOT EXISTS `sale_delivery` (
  `delivery_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出库单ID',
  `delivery_no` varchar(50) NOT NULL COMMENT '出库单号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '销售订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '销售订单号',
  `client_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `client_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `delivery_date` date DEFAULT NULL COMMENT '出库日期',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `total_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '出库总金额',
  `total_quantity` decimal(18,4) DEFAULT '0.0000' COMMENT '出库总数量',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-草稿, 1-已提交, 2-已审核, 3-已发货, 4-已完成',
  `tracking_no` varchar(100) DEFAULT NULL COMMENT '物流单号',
  `logistics_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `ship_date` date DEFAULT NULL COMMENT '发货日期',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货电话',
  `receiver_address` varchar(500) DEFAULT NULL COMMENT '收货地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`delivery_id`),
  KEY `idx_delivery_no` (`delivery_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_client_id` (`client_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售出库单表';

-- 销售出库单明细表
CREATE TABLE IF NOT EXISTS `sale_delivery_line` (
  `line_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `delivery_id` bigint(20) NOT NULL COMMENT '出库单ID',
  `order_line_id` bigint(20) DEFAULT NULL COMMENT '销售订单明细ID',
  `item_id` bigint(20) NOT NULL COMMENT '物料ID',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `item_name` varchar(100) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(18,4) DEFAULT '0.0000' COMMENT '出库数量',
  `price` decimal(18,4) DEFAULT '0.0000' COMMENT '单价',
  `amount` decimal(18,4) DEFAULT '0.0000' COMMENT '金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `location_id` bigint(20) DEFAULT NULL COMMENT '库位ID',
  `location_name` varchar(100) DEFAULT NULL COMMENT '库位名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`line_id`),
  KEY `idx_delivery_id` (`delivery_id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售出库单明细表';

-- 销售退货单表
CREATE TABLE IF NOT EXISTS `sale_return` (
  `return_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货单ID',
  `return_no` varchar(50) NOT NULL COMMENT '退货单号',
  `delivery_id` bigint(20) DEFAULT NULL COMMENT '原出库单ID',
  `delivery_no` varchar(50) DEFAULT NULL COMMENT '原出库单号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '销售订单ID',
  `client_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `client_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `return_date` date DEFAULT NULL COMMENT '退货日期',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `total_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '退货总金额',
  `total_quantity` decimal(18,4) DEFAULT '0.0000' COMMENT '退货总数量',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-草稿, 1-已提交, 2-已审核, 3-已入库',
  `return_reason` varchar(500) DEFAULT NULL COMMENT '退货原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`return_id`),
  KEY `idx_return_no` (`return_no`),
  KEY `idx_delivery_id` (`delivery_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售退货单表';

-- 销售退货单明细表
CREATE TABLE IF NOT EXISTS `sale_return_line` (
  `line_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `return_id` bigint(20) NOT NULL COMMENT '退货单ID',
  `delivery_line_id` bigint(20) DEFAULT NULL COMMENT '出库明细ID',
  `item_id` bigint(20) NOT NULL COMMENT '物料ID',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `item_name` varchar(100) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(18,4) DEFAULT '0.0000' COMMENT '退货数量',
  `price` decimal(18,4) DEFAULT '0.0000' COMMENT '单价',
  `amount` decimal(18,4) DEFAULT '0.0000' COMMENT '金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `return_reason` varchar(200) DEFAULT NULL COMMENT '退货原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`line_id`),
  KEY `idx_return_id` (`return_id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售退货单明细表';

-- 销售对账表
CREATE TABLE IF NOT EXISTS `sale_reconciliation` (
  `recon_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对账单ID',
  `recon_no` varchar(50) NOT NULL COMMENT '对账单号',
  `client_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `client_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `start_date` date DEFAULT NULL COMMENT '对账开始日期',
  `end_date` date DEFAULT NULL COMMENT '对账结束日期',
  `delivery_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '出库总金额',
  `return_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '退货总金额',
  `receivable_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '应收金额',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-草稿, 1-已确认, 2-已开票, 3-已收款',
  `invoice_no` varchar(100) DEFAULT NULL COMMENT '发票号码',
  `invoice_date` date DEFAULT NULL COMMENT '开票日期',
  `received_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '收款金额',
  `receive_date` date DEFAULT NULL COMMENT '收款日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`recon_id`),
  KEY `idx_recon_no` (`recon_no`),
  KEY `idx_client_id` (`client_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售对账表';

-- ===========================================
-- 成本管理模块 (cost)
-- ===========================================

-- 成本项目表
CREATE TABLE IF NOT EXISTS `cost_item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '成本项目ID',
  `item_code` varchar(50) NOT NULL COMMENT '成本项目编码',
  `item_name` varchar(100) NOT NULL COMMENT '成本项目名称',
  `cost_type` varchar(20) DEFAULT NULL COMMENT '成本类型: 1-直接材料, 2-直接人工, 3-制造费用',
  `allocation_method` varchar(20) DEFAULT NULL COMMENT '分配方式: 1-按工时, 2-按产量, 3-按材料成本',
  `standard_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '标准成本',
  `status` varchar(10) DEFAULT '1' COMMENT '状态: 0-停用, 1-启用',
  `sort_no` int(11) DEFAULT '0' COMMENT '排序号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`item_id`),
  KEY `idx_item_code` (`item_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本项目表';

-- 成本归集表
CREATE TABLE IF NOT EXISTS `cost_collection` (
  `collection_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '归集ID',
  `collection_no` varchar(50) NOT NULL COMMENT '归集单号',
  `item_id` bigint(20) DEFAULT NULL COMMENT '成本项目ID',
  `item_name` varchar(100) DEFAULT NULL COMMENT '成本项目名称',
  `cost_type` varchar(20) DEFAULT NULL COMMENT '成本类型',
  `collection_date` date DEFAULT NULL COMMENT '归集日期',
  `workorder_id` bigint(20) DEFAULT NULL COMMENT '工单ID',
  `workorder_no` varchar(50) DEFAULT NULL COMMENT '工单号',
  `amount` decimal(18,4) DEFAULT '0.0000' COMMENT '金额',
  `work_hours` decimal(10,2) DEFAULT '0.00' COMMENT '工时(小时)',
  `quantity` decimal(18,4) DEFAULT '0.0000' COMMENT '产量',
  `data_source` varchar(20) DEFAULT '1' COMMENT '数据来源: 1-手工录入, 2-系统自动',
  `source_id` bigint(20) DEFAULT NULL COMMENT '数据来源单据ID',
  `source_no` varchar(50) DEFAULT NULL COMMENT '数据来源单据号',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-草稿, 1-已确认',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`collection_id`),
  KEY `idx_collection_no` (`collection_no`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_workorder` (`workorder_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本归集表';

-- 成本核算表
CREATE TABLE IF NOT EXISTS `cost_calculation` (
  `calc_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '核算单ID',
  `calc_no` varchar(50) NOT NULL COMMENT '核算单号',
  `calc_date` date DEFAULT NULL COMMENT '核算日期',
  `calc_month` varchar(20) DEFAULT NULL COMMENT '核算月份',
  `workorder_id` bigint(20) DEFAULT NULL COMMENT '工单ID',
  `workorder_no` varchar(50) DEFAULT NULL COMMENT '工单号',
  `item_id` bigint(20) DEFAULT NULL COMMENT '物料ID',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `item_name` varchar(100) DEFAULT NULL COMMENT '物料名称',
  `completed_qty` decimal(18,4) DEFAULT '0.0000' COMMENT '完工数量',
  `material_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '直接材料成本',
  `labor_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '直接人工成本',
  `overhead_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '制造费用',
  `total_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '总成本',
  `unit_cost` decimal(18,4) DEFAULT '0.0000' COMMENT '单位成本',
  `status` varchar(10) DEFAULT '0' COMMENT '状态: 0-草稿, 1-已核算, 2-已结转',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`calc_id`),
  KEY `idx_calc_no` (`calc_no`),
  KEY `idx_workorder` (`workorder_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本核算表';

-- 成本核算明细表
CREATE TABLE IF NOT EXISTS `cost_calculation_line` (
  `line_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `calc_id` bigint(20) NOT NULL COMMENT '核算单ID',
  `item_id` bigint(20) DEFAULT NULL COMMENT '成本项目ID',
  `item_name` varchar(100) DEFAULT NULL COMMENT '成本项目名称',
  `cost_type` varchar(20) DEFAULT NULL COMMENT '成本类型',
  `amount` decimal(18,4) DEFAULT '0.0000' COMMENT '金额',
  `allocation_rate` decimal(10,4) DEFAULT '0.0000' COMMENT '分配率',
  `allocated_amount` decimal(18,4) DEFAULT '0.0000' COMMENT '分配金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`line_id`),
  KEY `idx_calc_id` (`calc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本核算明细表';

-- ===========================================
-- 工装机具模块 (tm)
-- ===========================================

-- 工装机具表
CREATE TABLE IF NOT EXISTS `tm_tool` (
  `tool_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工装机具ID',
  `tool_code` varchar(50) NOT NULL COMMENT '工装机具编码',
  `tool_name` varchar(100) NOT NULL COMMENT '工装机具名称',
  `tool_type` varchar(20) DEFAULT NULL COMMENT '类型: 1-工装, 2-模具, 3-刀具, 4-量具, 5-辅具',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '制造商',
  `purchase_date` date DEFAULT NULL COMMENT '购买日期',
  `price` decimal(18,4) DEFAULT '0.0000' COMMENT '价格',
  `life_limit` int(11) DEFAULT NULL COMMENT '使用寿命',
  `used_life` int(11) DEFAULT '0' COMMENT '已使用次数/时长',
  `remain_life` int(11) DEFAULT '0' COMMENT '剩余寿命',
  `location` varchar(100) DEFAULT NULL COMMENT '存放位置',
  `status` varchar(10) DEFAULT '1' COMMENT '状态: 0-停用, 1-可用, 2-维修中, 3-报废',
  `last_maintain_date` date DEFAULT NULL COMMENT '上次保养日期',
  `maintain_cycle` int(11) DEFAULT NULL COMMENT '保养周期(天)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tool_id`),
  KEY `idx_tool_code` (`tool_code`),
  KEY `idx_status` (`status`),
  KEY `idx_tool_type` (`tool_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装机具表';

-- 工装机具领用表
CREATE TABLE IF NOT EXISTS `tm_tool_borrow` (
  `borrow_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '领用ID',
  `borrow_no` varchar(50) NOT NULL COMMENT '领用单号',
  `tool_id` bigint(20) NOT NULL COMMENT '工装机具ID',
  `tool_code` varchar(50) DEFAULT NULL COMMENT '工装机具编码',
  `tool_name` varchar(100) DEFAULT NULL COMMENT '工装机具名称',
  `borrower_code` varchar(50) DEFAULT NULL COMMENT '领用人工号',
  `borrower_name` varchar(50) DEFAULT NULL COMMENT '领用人姓名',
  `borrow_date` date DEFAULT NULL COMMENT '领用日期',
  `plan_return_date` date DEFAULT NULL COMMENT '预计归还日期',
  `actual_return_date` date DEFAULT NULL COMMENT '实际归还日期',
  `used_count` int(11) DEFAULT '0' COMMENT '使用次数/时长',
  `status` varchar(10) DEFAULT '1' COMMENT '状态: 1-已领用, 2-已归还',
  `borrow_reason` varchar(200) DEFAULT NULL COMMENT '领用事由',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`borrow_id`),
  KEY `idx_borrow_no` (`borrow_no`),
  KEY `idx_tool_id` (`tool_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装机具领用表';
