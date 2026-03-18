-- 检查表是否存在
SHOW TABLES LIKE 'md_client';

-- 如果表存在，查看当前结构
DESCRIBE md_client;

-- 如果缺少字段，添加字段
ALTER TABLE md_client 
ADD COLUMN IF NOT EXISTS contact_person VARCHAR(50) COMMENT '联系人',
ADD COLUMN IF NOT EXISTS phone VARCHAR(20) COMMENT '联系电话',
ADD COLUMN IF NOT EXISTS email VARCHAR(100) COMMENT '邮箱',
ADD COLUMN IF NOT EXISTS address VARCHAR(200) COMMENT '地址';

-- 如果表不存在，创建新表
CREATE TABLE IF NOT EXISTS md_client (
    client_id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    client_code         VARCHAR(50) NOT NULL COMMENT '客户编码',
    client_name         VARCHAR(100) NOT NULL COMMENT '客户名称',
    contact_person      VARCHAR(50) COMMENT '联系人',
    phone               VARCHAR(20) COMMENT '联系电话',
    email               VARCHAR(100) COMMENT '邮箱',
    address             VARCHAR(200) COMMENT '地址',
    status              VARCHAR(1) DEFAULT '0' COMMENT '状态: 0-启用, 1-停用',
    remark              VARCHAR(500) COMMENT '备注',
    create_by           VARCHAR(64) COMMENT '创建者',
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64) COMMENT '更新者',
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_client_code (client_code)
) COMMENT '客户管理表';

-- 创建索引
CREATE INDEX idx_client_name ON md_client(client_name);
CREATE INDEX idx_client_status ON md_client(status);
