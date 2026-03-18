-- 采购管理模块数据库表 - carels
-- @version V9.1
-- @date 2026-03-17

-- ============================================
-- 1. 采购申请表
-- ============================================
CREATE TABLE IF NOT EXISTS pur_request (
    request_id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    request_code        VARCHAR(50) NOT NULL COMMENT '申请单号',
    request_date        DATE NOT NULL COMMENT '申请日期',
    request_type        VARCHAR(20) DEFAULT 'NORMAL' COMMENT '申请类型: NORMAL-普通, URGENT-紧急',
    applicant_id        BIGINT COMMENT '申请人ID',
    applicant_name      VARCHAR(50) COMMENT '申请人',
    dept_id             BIGINT COMMENT '申请部门ID',
    dept_name           VARCHAR(100) COMMENT '申请部门',
    total_amount        DECIMAL(15,2) DEFAULT 0 COMMENT '总金额',
    currency            VARCHAR(3) DEFAULT 'CNY' COMMENT '币种',
    status              VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PENDING-待审批, APPROVED-已审批, REJECTED-已拒绝',
    remark              VARCHAR(500) COMMENT '备注',
    create_by           VARCHAR(64) COMMENT '创建者',
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64) COMMENT '更新者',
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_request_code (request_code)
) COMMENT '采购申请表';

-- 采购申请明细表
CREATE TABLE IF NOT EXISTS pur_request_item (
    item_id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    request_id          BIGINT NOT NULL COMMENT '申请ID',
    item_code           VARCHAR(50) COMMENT '物料编码',
    item_name           VARCHAR(200) COMMENT '物料名称',
    item_spec           VARCHAR(500) COMMENT '规格型号',
    unit                VARCHAR(20) COMMENT '单位',
    quantity            DECIMAL(15,3) NOT NULL COMMENT '数量',
    required_date       DATE COMMENT '需求日期',
    remark              VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (request_id) REFERENCES pur_request(request_id) ON DELETE CASCADE
) COMMENT '采购申请明细表';

-- ============================================
-- 2. 采购订单表
-- ============================================
CREATE TABLE IF NOT EXISTS pur_order (
    order_id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_code          VARCHAR(50) NOT NULL COMMENT '订单编号',
    order_date          DATE NOT NULL COMMENT '订单日期',
    vendor_id           BIGINT COMMENT '供应商ID',
    vendor_code         VARCHAR(50) COMMENT '供应商编码',
    vendor_name         VARCHAR(100) COMMENT '供应商名称',
    contact_person      VARCHAR(50) COMMENT '联系人',
    phone               VARCHAR(20) COMMENT '联系电话',
    request_id          BIGINT COMMENT '关联申请ID',
    request_code        VARCHAR(50) COMMENT '关联申请单号',
    total_amount        DECIMAL(15,2) DEFAULT 0 COMMENT '订单总金额',
    tax_rate            DECIMAL(5,2) DEFAULT 13 COMMENT '税率(%)',
    currency            VARCHAR(3) DEFAULT 'CNY' COMMENT '币种',
    delivery_date       DATE COMMENT '交货日期',
    delivery_address    VARCHAR(200) COMMENT '交货地址',
    status              VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, CONFIRMED-已确认, PARTIAL-部分到货, COMPLETED-已完成, CANCELLED-已取消',
    remark              VARCHAR(500) COMMENT '备注',
    create_by           VARCHAR(64) COMMENT '创建者',
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64) COMMENT '更新者',
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_code (order_code)
) COMMENT '采购订单表';

-- 采购订单明细表
CREATE TABLE IF NOT EXISTS pur_order_item (
    item_id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id            BIGINT NOT NULL COMMENT '订单ID',
    item_code           VARCHAR(50) COMMENT '物料编码',
    item_name           VARCHAR(200) COMMENT '物料名称',
    item_spec           VARCHAR(500) COMMENT '规格型号',
    unit                VARCHAR(20) COMMENT '单位',
    quantity            DECIMAL(15,3) NOT NULL COMMENT '采购数量',
    received_qty        DECIMAL(15,3) DEFAULT 0 COMMENT '已收货数量',
    price               DECIMAL(15,4) NOT NULL COMMENT '单价',
    amount              DECIMAL(15,2) NOT NULL COMMENT '金额',
    delivery_date       DATE COMMENT '交货日期',
    remark              VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (order_id) REFERENCES pur_order(order_id) ON DELETE CASCADE
) COMMENT '采购订单明细表';

-- ============================================
-- 3. 采购入库表
-- ============================================
CREATE TABLE IF NOT EXISTS pur_receipt (
    receipt_id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库ID',
    receipt_code        VARCHAR(50) NOT NULL COMMENT '入库单号',
    receipt_date        DATE NOT NULL COMMENT '入库日期',
    order_id            BIGINT COMMENT '关联订单ID',
    order_code          VARCHAR(50) COMMENT '关联订单号',
    vendor_id           BIGINT COMMENT '供应商ID',
    vendor_name         VARCHAR(100) COMMENT '供应商名称',
    warehouse_id        BIGINT COMMENT '入库仓库ID',
    warehouse_name      VARCHAR(100) COMMENT '入库仓库',
    total_amount        DECIMAL(15,2) DEFAULT 0 COMMENT '入库金额',
    status              VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待质检, INSPECTING-质检中, PASSED-质检通过, REJECTED-质检不通过',
    inspector_id        BIGINT COMMENT '质检员ID',
    inspector_name      VARCHAR(50) COMMENT '质检员',
    inspect_result      VARCHAR(20) COMMENT '质检结果: PASS-合格, REJECT-不合格',
    inspect_remark      VARCHAR(500) COMMENT '质检备注',
    remark              VARCHAR(500) COMMENT '备注',
    create_by           VARCHAR(64) COMMENT '创建者',
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64) COMMENT '更新者',
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_receipt_code (receipt_code)
) COMMENT '采购入库表';

-- 采购入库明细表
CREATE TABLE IF NOT EXISTS pur_receipt_item (
    item_id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    receipt_id          BIGINT NOT NULL COMMENT '入库ID',
    order_item_id       BIGINT COMMENT '关联订单明细ID',
    item_code           VARCHAR(50) COMMENT '物料编码',
    item_name           VARCHAR(200) COMMENT '物料名称',
    item_spec           VARCHAR(500) COMMENT '规格型号',
    unit                VARCHAR(20) COMMENT '单位',
    quantity            DECIMAL(15,3) NOT NULL COMMENT '入库数量',
    qualified_qty       DECIMAL(15,3) DEFAULT 0 COMMENT '合格数量',
    unqualified_qty     DECIMAL(15,3) DEFAULT 0 COMMENT '不合格数量',
    batch_no            VARCHAR(50) COMMENT '批次号',
    production_date     DATE COMMENT '生产日期',
    expiry_date         DATE COMMENT '有效期至',
    remark              VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (receipt_id) REFERENCES pur_receipt(receipt_id) ON DELETE CASCADE
) COMMENT '采购入库明细表';

-- ============================================
-- 4. 采购退货表
-- ============================================
CREATE TABLE IF NOT EXISTS pur_return (
    return_id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '退货ID',
    return_code         VARCHAR(50) NOT NULL COMMENT '退货单号',
    return_date         DATE NOT NULL COMMENT '退货日期',
    receipt_id          BIGINT COMMENT '关联入库ID',
    receipt_code        VARCHAR(50) COMMENT '关联入库单号',
    order_id            BIGINT COMMENT '关联订单ID',
    order_code          VARCHAR(50) COMMENT '关联订单号',
    vendor_id           BIGINT COMMENT '供应商ID',
    vendor_name         VARCHAR(100) COMMENT '供应商名称',
    return_type         VARCHAR(20) DEFAULT 'QUALITY' COMMENT '退货类型: QUALITY-质量问题, WRONG-发错货, EXCESS-多发货, OTHER-其他',
    total_amount        DECIMAL(15,2) DEFAULT 0 COMMENT '退货金额',
    status              VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, CONFIRMED-已确认, COMPLETED-已完成',
    remark              VARCHAR(500) COMMENT '备注',
    create_by           VARCHAR(64) COMMENT '创建者',
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64) COMMENT '更新者',
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_return_code (return_code)
) COMMENT '采购退货表';

-- 采购退货明细表
CREATE TABLE IF NOT EXISTS pur_return_item (
    item_id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    return_id           BIGINT NOT NULL COMMENT '退货ID',
    receipt_item_id     BIGINT COMMENT '关联入库明细ID',
    item_code           VARCHAR(50) COMMENT '物料编码',
    item_name           VARCHAR(200) COMMENT '物料名称',
    item_spec           VARCHAR(500) COMMENT '规格型号',
    unit                VARCHAR(20) COMMENT '单位',
    quantity            DECIMAL(15,3) NOT NULL COMMENT '退货数量',
    price               DECIMAL(15,4) COMMENT '单价',
    amount              DECIMAL(15,2) COMMENT '金额',
    batch_no            VARCHAR(50) COMMENT '批次号',
    return_reason       VARCHAR(200) COMMENT '退货原因',
    remark              VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (return_id) REFERENCES pur_return(return_id) ON DELETE CASCADE
) COMMENT '采购退货明细表';

-- ============================================
-- 创建索引
-- ============================================
CREATE INDEX idx_pur_request_date ON pur_request(request_date);
CREATE INDEX idx_pur_request_status ON pur_request(status);
CREATE INDEX idx_pur_order_date ON pur_order(order_date);
CREATE INDEX idx_pur_order_vendor ON pur_order(vendor_id);
CREATE INDEX idx_pur_order_status ON pur_order(status);
CREATE INDEX idx_pur_receipt_date ON pur_receipt(receipt_date);
CREATE INDEX idx_pur_receipt_order ON pur_receipt(order_id);
CREATE INDEX idx_pur_return_date ON pur_return(return_date);
CREATE INDEX idx_pur_return_receipt ON pur_return(receipt_id);
