-- 出库管理模块添加销售订单关联字段
-- 创建时间: 2026-03-22

-- 添加销售订单关联字段
ALTER TABLE wm_issue
ADD COLUMN sales_order_id BIGINT COMMENT '销售订单ID' AFTER remark,
ADD COLUMN sales_order_no VARCHAR(50) COMMENT '销售订单号' AFTER sales_order_id,
ADD COLUMN client_id BIGINT COMMENT '客户ID' AFTER sales_order_no,
ADD COLUMN client_name VARCHAR(200) COMMENT '客户名称' AFTER client_id;

-- 添加索引
CREATE INDEX idx_sales_order_id ON wm_issue(sales_order_id);
CREATE INDEX idx_sales_order_no ON wm_issue(sales_order_no);
