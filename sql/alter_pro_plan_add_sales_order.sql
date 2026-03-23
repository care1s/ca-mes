-- 生产计划表添加销售订单绑定字段
-- 创建时间: 2026-03-22

-- 添加排单类型字段: 0-自身排单, 1-绑定销售订单
ALTER TABLE pro_plan
ADD COLUMN schedule_type TINYINT DEFAULT 0 COMMENT '排单类型: 0-自身排单, 1-绑定销售订单' AFTER status;

-- 添加销售订单ID字段
ALTER TABLE pro_plan
ADD COLUMN sales_order_id BIGINT COMMENT '销售订单ID' AFTER schedule_type;

-- 添加销售订单号字段
ALTER TABLE pro_plan
ADD COLUMN sales_order_no VARCHAR(50) COMMENT '销售订单号' AFTER sales_order_id;

-- 添加客户ID字段
ALTER TABLE pro_plan
ADD COLUMN customer_id BIGINT COMMENT '客户ID' AFTER sales_order_no;

-- 添加客户名称字段
ALTER TABLE pro_plan
ADD COLUMN customer_name VARCHAR(200) COMMENT '客户名称' AFTER customer_id;
