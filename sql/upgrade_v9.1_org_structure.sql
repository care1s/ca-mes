-- ========================================================-- MES系统数据库结构升级脚本-- 版本: V9.1-- 日期: 2026-03-16-- 说明: 添加车间-生产线-工作站灵活组织架构支持-- ========================================================

-- ========================================================-- 1. 修改车间表，添加组织模式配置-- ========================================================ALTER TABLE md_workshop ADD COLUMN org_mode VARCHAR(20) DEFAULT 'SIMPLE' COMMENT '组织模式: SIMPLE-简单模式(车间->工作站), COMPLETE-完整模式(车间->生产线->工作站)' AFTER phone;

-- 更新现有车间数据，默认为简单模式
UPDATE md_workshop SET org_mode = 'SIMPLE' WHERE org_mode IS NULL;

-- ========================================================-- 2. 创建生产线表-- ========================================================CREATE TABLE IF NOT EXISTS md_production_line (
    line_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '生产线ID',
    line_code VARCHAR(50) NOT NULL COMMENT '生产线编码',
    line_name VARCHAR(100) NOT NULL COMMENT '生产线名称',
    workshop_id BIGINT NOT NULL COMMENT '所属车间ID',
    workshop_name VARCHAR(100) COMMENT '所属车间名称',
    line_type VARCHAR(50) COMMENT '生产线类型: ASSEMBLY-装配线, PROCESSING-加工线, PACKING-包装线等',
    capacity DECIMAL(10,2) COMMENT '产能(件/小时)',
    status CHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_line_code (line_code),
    KEY idx_workshop (workshop_id),
    FOREIGN KEY (workshop_id) REFERENCES md_workshop(workshop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产线表 - carels';

-- ========================================================-- 3. 修改工作站表，支持灵活关联-- ========================================================ALTER TABLE md_workstation 
ADD COLUMN production_line_id BIGINT COMMENT '所属生产线ID(仅完整模式使用)' AFTER workshop_name,
ADD COLUMN production_line_name VARCHAR(100) COMMENT '所属生产线名称' AFTER production_line_id;

-- 添加外键约束
ALTER TABLE md_workstation 
ADD CONSTRAINT fk_workstation_line FOREIGN KEY (production_line_id) REFERENCES md_production_line(line_id);

-- ========================================================-- 4. 创建视图，方便查询工作站完整信息-- ========================================================CREATE OR REPLACE VIEW v_workstation_detail AS
SELECT 
    w.*,
    ws.workshop_name,
    ws.org_mode as workshop_org_mode,
    pl.line_name as production_line_name
FROM md_workstation w
LEFT JOIN md_workshop ws ON w.workshop_id = ws.workshop_id
LEFT JOIN md_production_line pl ON w.production_line_id = pl.line_id;

-- ========================================================-- 5. 插入示例数据（可选）-- ========================================================-- 示例：创建一个使用完整模式的车间
-- INSERT INTO md_workshop (workshop_code, workshop_name, org_mode, status) 
-- VALUES ('WS002', '组装车间', 'COMPLETE', '0');

-- 示例：为完整模式车间创建生产线-- INSERT INTO md_production_line (line_code, line_name, workshop_id, line_type, status)-- VALUES ('LINE001', '装配线A', 2, 'ASSEMBLY', '0');

-- 示例：创建关联到生产线的工作站-- INSERT INTO md_workstation (workstation_code, workstation_name, workshop_id, production_line_id, status)-- VALUES ('WS001-A', '装配工位1', 2, 1, '0');
