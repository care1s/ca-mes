-- 检查表是否存在
SELECT TABLE_NAME FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'md_client';

-- 如果表存在，查看所有字段
DESCRIBE md_client;

-- 如果字段缺失，一次性添加所有字段
ALTER TABLE md_client 
ADD COLUMN contact_person VARCHAR(50) NULL COMMENT '联系人',
ADD COLUMN phone VARCHAR(20) NULL COMMENT '联系电话',
ADD COLUMN email VARCHAR(100) NULL COMMENT '邮箱',
ADD COLUMN address VARCHAR(200) NULL COMMENT '地址';
