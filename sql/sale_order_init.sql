-- 销售订单模块数据库表
-- 创建时间: 2026-03-22

-- ============================================
-- 销售订单主表
-- ============================================
DROP TABLE IF EXISTS sale_order_item;
DROP TABLE IF EXISTS sale_order_issue;
DROP TABLE IF EXISTS sale_order;

CREATE TABLE sale_order (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    order_date DATE NOT NULL COMMENT '订单日期',

    -- 客户信息
    client_id BIGINT NOT NULL COMMENT '客户ID',
    client_code VARCHAR(50) COMMENT '客户编码',
    client_name VARCHAR(200) COMMENT '客户名称',

    -- 交货信息
    delivery_date DATE COMMENT '交货日期',
    delivery_address VARCHAR(500) COMMENT '交货地址',
    contact_person VARCHAR(100) COMMENT '联系人',
    contact_phone VARCHAR(50) COMMENT '联系电话',

    -- 金额信息
    total_amount DECIMAL(18,4) DEFAULT 0 COMMENT '订单总金额',
    tax_amount DECIMAL(18,4) DEFAULT 0 COMMENT '税额',
    discount_amount DECIMAL(18,4) DEFAULT 0 COMMENT '折扣金额',
    payable_amount DECIMAL(18,4) DEFAULT 0 COMMENT '应付金额',

    -- 状态信息
    status TINYINT DEFAULT 0 COMMENT '订单状态: 0-草稿, 1-已提交, 2-已审核, 3-生产中, 4-部分出库, 5-已完成, 6-已取消',
    delivery_status TINYINT DEFAULT 0 COMMENT '交货状态: 0-未发货, 1-部分发货, 2-已发货',

    -- 关联信息
    related_plan_id BIGINT COMMENT '关联生产计划ID',

    -- 备注
    remark VARCHAR(500) COMMENT '备注',

    -- 创建更新信息
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_order_no (order_no),
    INDEX idx_client_id (client_id),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date),
    INDEX idx_delivery_date (delivery_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单主表 - carels';

-- ============================================
-- 销售订单明细表
-- ============================================
CREATE TABLE sale_order_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',

    -- 物料信息
    item_id2 BIGINT NOT NULL COMMENT '物料ID',
    item_code VARCHAR(50) COMMENT '物料编码',
    item_name VARCHAR(200) COMMENT '物料名称',
    specification VARCHAR(500) COMMENT '规格型号',
    unit VARCHAR(50) COMMENT '单位',

    -- 数量金额
    quantity DECIMAL(18,4) NOT NULL COMMENT '订单数量',
    delivered_qty DECIMAL(18,4) DEFAULT 0 COMMENT '已发货数量',
    remain_qty DECIMAL(18,4) DEFAULT 0 COMMENT '剩余数量',
    unit_price DECIMAL(18,4) NOT NULL COMMENT '单价',
    total_price DECIMAL(18,4) DEFAULT 0 COMMENT '总价',

    -- 状态
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-已完成, 2-已取消',

    -- 备注
    remark VARCHAR(500) COMMENT '备注',

    -- 创建信息
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_order_id (order_id),
    INDEX idx_item_id (item_id2),

    FOREIGN KEY (order_id) REFERENCES sale_order(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单明细表 - carels';

-- ============================================
-- 销售出库关联表（记录销售订单与出库单的关系）
-- ============================================
CREATE TABLE sale_order_issue (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '销售订单ID',
    order_no VARCHAR(50) COMMENT '销售订单号',
    issue_id BIGINT NOT NULL COMMENT '出库单ID',
    issue_no VARCHAR(50) COMMENT '出库单号',

    -- 出库信息
    item_id BIGINT COMMENT '物料ID',
    quantity DECIMAL(18,4) COMMENT '出库数量',

    -- 创建信息
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_order_id (order_id),
    INDEX idx_issue_id (issue_id),
    INDEX idx_item_id (item_id),

    FOREIGN KEY (order_id) REFERENCES sale_order(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单出库关联表 - carels';

-- ============================================
-- 插入字典数据（可选，如果字典表支持）
-- ============================================
-- 销售订单状态字典
-- INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, status) VALUES
-- (0, '草稿', '0', 'sale_order_status', '0'),
-- (1, '已提交', '1', 'sale_order_status', '0'),
-- (2, '已审核', '2', 'sale_order_status', '0'),
-- (3, '生产中', '3', 'sale_order_status', '0'),
-- (4, '部分出库', '4', 'sale_order_status', '0'),
-- (5, '已完成', '5', 'sale_order_status', '0'),
-- (6, '已取消', '6', 'sale_order_status', '0');

-- 交货状态字典
-- INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, status) VALUES
-- (0, '未发货', '0', 'delivery_status', '0'),
-- (1, '部分发货', '1', 'delivery_status', '0'),
-- (2, '已发货', '2', 'delivery_status', '0');
