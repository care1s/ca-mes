-- 生产计划表添加产品字段
-- 创建时间: 2026-03-22

-- 添加产品相关字段
ALTER TABLE pro_plan
ADD COLUMN item_id BIGINT COMMENT '产品ID（物料ID）' AFTER customer_name,
ADD COLUMN item_code VARCHAR(50) COMMENT '产品编码' AFTER item_id,
ADD COLUMN item_name VARCHAR(200) COMMENT '产品名称' AFTER item_code,
ADD COLUMN specification VARCHAR(500) COMMENT '规格型号' AFTER item_name;
