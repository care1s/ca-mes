-- 生产工单表添加生产计划关联字段
-- 创建时间: 2026-03-22

-- 添加生产计划关联字段
ALTER TABLE pro_workorder
ADD COLUMN plan_id BIGINT COMMENT '生产计划ID' AFTER workorder_code,
ADD COLUMN plan_no VARCHAR(50) COMMENT '生产计划单号' AFTER plan_id;

-- 添加索引
CREATE INDEX idx_plan_id ON pro_workorder(plan_id);

-- ============================================
-- 生产计划生成工单的存储过程
-- ============================================
DELIMITER $$

DROP PROCEDURE IF EXISTS sp_create_workorder_from_plan$$

CREATE PROCEDURE sp_create_workorder_from_plan(
    IN p_plan_id BIGINT,
    IN p_workshop_id BIGINT,
    IN p_workshop_name VARCHAR(100)
)
BEGIN
    DECLARE v_plan_no VARCHAR(50);
    DECLARE v_item_id BIGINT;
    DECLARE v_item_code VARCHAR(50);
    DECLARE v_item_name VARCHAR(200);
    DECLARE v_specification VARCHAR(500);
    DECLARE v_plan_qty DOUBLE;
    DECLARE v_route_id BIGINT;
    DECLARE v_route_name VARCHAR(100);
    DECLARE v_start_date DATE;
    DECLARE v_end_date DATE;

    -- 获取生产计划信息
    SELECT plan_no, item_id, item_code, item_name, specification, plan_qty, start_date, end_date
    INTO v_plan_no, v_item_id, v_item_code, v_item_name, v_specification, v_plan_qty, v_start_date, v_end_date
    FROM pro_plan
    WHERE plan_id = p_plan_id;

    -- 获取产品对应的工艺路线
    SELECT route_id, route_name
    INTO v_route_id, v_route_name
    FROM pro_route
    WHERE item_id = v_item_id AND status = '0'
    LIMIT 1;

    -- 插入工单
    INSERT INTO pro_workorder (
        workorder_code, plan_id, plan_no, workorder_type, item_id, item_code, item_name, specification,
        route_id, route_name, plan_quantity, completed_quantity, qualified_quantity, defective_quantity, scrap_quantity,
        status, priority, plan_start_time, plan_end_time, workshop_id, workshop_name, create_time
    ) VALUES (
        CONCAT('WO', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(FLOOR(RAND() * 10000), 4, '0')),
        p_plan_id, v_plan_no, 'STANDARD', v_item_id, v_item_code, v_item_name, v_specification,
        v_route_id, v_route_name, v_plan_qty, 0, 0, 0, 0,
        'PENDING', 'NORMAL', v_start_date, v_end_date, p_workshop_id, p_workshop_name, NOW()
    );

    -- 返回新生成的工单ID
    SELECT LAST_INSERT_ID() AS new_workorder_id;
END$$

DELIMITER ;
