-- ========================================================
-- 仓库管理模块 - 仓区表和仓位表
-- 作者: carels
-- 版本: V9.0
-- 日期: 2026-03-18
-- ========================================================

-- 仓区表
CREATE TABLE IF NOT EXISTS `wm_zone` (
    `zone_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓区ID',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_code` VARCHAR(50) DEFAULT NULL COMMENT '仓库编码',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `zone_code` VARCHAR(50) NOT NULL COMMENT '仓区编码',
    `zone_name` VARCHAR(100) NOT NULL COMMENT '仓区名称',
    `zone_type` VARCHAR(20) DEFAULT 'STORAGE' COMMENT '仓区类型: RECEIVE-收货区, SHIP-发货区, STORAGE-存储区, PICK-拣货区, SPECIAL-特殊区',
    `status` VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`zone_id`),
    UNIQUE KEY `uk_zone_code_warehouse` (`zone_code`, `warehouse_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_zone_type` (`zone_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓区表';

-- 仓位表
CREATE TABLE IF NOT EXISTS `wm_location` (
    `location_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓位ID',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_code` VARCHAR(50) DEFAULT NULL COMMENT '仓库编码',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `zone_id` BIGINT NOT NULL COMMENT '仓区ID',
    `zone_code` VARCHAR(50) DEFAULT NULL COMMENT '仓区编码',
    `zone_name` VARCHAR(100) DEFAULT NULL COMMENT '仓区名称',
    `location_code` VARCHAR(50) NOT NULL COMMENT '仓位编码',
    `location_name` VARCHAR(100) NOT NULL COMMENT '仓位名称',
    `aisle_no` VARCHAR(20) DEFAULT NULL COMMENT '巷道号',
    `shelf_no` VARCHAR(20) DEFAULT NULL COMMENT '货架号',
    `layer_no` VARCHAR(20) DEFAULT NULL COMMENT '层号',
    `position_no` VARCHAR(20) DEFAULT NULL COMMENT '位号',
    `capacity` INT DEFAULT 1 COMMENT '容量(托盘数)',
    `used_capacity` INT DEFAULT 0 COMMENT '已用容量',
    `status` VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用, 2-占用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`location_id`),
    UNIQUE KEY `uk_location_code_zone` (`location_code`, `zone_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_zone_id` (`zone_id`),
    KEY `idx_location_code` (`location_code`),
    KEY `idx_aisle_shelf_layer` (`aisle_no`, `shelf_no`, `layer_no`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓位表';

-- ========================================================
-- 可选：添加示例数据
-- ========================================================

-- 示例仓区数据（需要先确保有仓库数据）
-- INSERT INTO `wm_zone` (`warehouse_id`, `warehouse_code`, `warehouse_name`, `zone_code`, `zone_name`, `zone_type`, `status`, `remark`, `create_time`) VALUES
-- (1, 'WH001', '原材料仓', 'Z001', '收货区', 'RECEIVE', '0', '原材料收货区域', NOW()),
-- (1, 'WH001', '原材料仓', 'Z002', '存储区A', 'STORAGE', '0', '原材料存储区域A', NOW()),
-- (1, 'WH001', '原材料仓', 'Z003', '存储区B', 'STORAGE', '0', '原材料存储区域B', NOW()),
-- (1, 'WH001', '原材料仓', 'Z004', '拣货区', 'PICK', '0', '原材料拣货区域', NOW()),
-- (1, 'WH001', '原材料仓', 'Z005', '发货区', 'SHIP', '0', '原材料发货区域', NOW()),
-- (2, 'WH002', '成品仓', 'Z101', '收货区', 'RECEIVE', '0', '成品收货区域', NOW()),
-- (2, 'WH002', '成品仓', 'Z102', '存储区', 'STORAGE', '0', '成品存储区域', NOW()),
-- (2, 'WH002', '成品仓', 'Z103', '发货区', 'SHIP', '0', '成品发货区域', NOW());

-- 示例仓位数据（需要先确保有仓区数据）
-- INSERT INTO `wm_location` (`warehouse_id`, `warehouse_code`, `warehouse_name`, `zone_id`, `zone_code`, `zone_name`, `location_code`, `location_name`, `aisle_no`, `shelf_no`, `layer_no`, `position_no`, `capacity`, `used_capacity`, `status`, `create_time`) VALUES
-- (1, 'WH001', '原材料仓', 2, 'Z002', '存储区A', 'L001001', 'A-01-01-01', '01', '01', '01', '01', 10, 5, '0', NOW()),
-- (1, 'WH001', '原材料仓', 2, 'Z002', '存储区A', 'L001002', 'A-01-01-02', '01', '01', '01', '02', 10, 3, '0', NOW()),
-- (1, 'WH001', '原材料仓', 2, 'Z002', '存储区A', 'L001003', 'A-01-02-01', '01', '01', '02', '01', 10, 0, '0', NOW()),
-- (1, 'WH001', '原材料仓', 3, 'Z003', '存储区B', 'L002001', 'B-01-01-01', '01', '01', '01', '01', 8, 8, '2', NOW()),
-- (2, 'WH002', '成品仓', 7, 'Z102', '存储区', 'L101001', 'C-01-01-01', '01', '01', '01', '01', 12, 6, '0', NOW());
