-- ========================================
-- 非现场开户管理平台 - 数据库初始化脚本
-- ========================================

CREATE DATABASE IF NOT EXISTS opening_cms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE opening_cms;

-- ----------------------------------------
-- 系统用户表
-- ----------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（MD5加密）',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `role` VARCHAR(20) NOT NULL DEFAULT 'operator' COMMENT '角色: admin-管理员, auditor-审核员, operator-操作员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_username` (`username`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------------------
-- 客户信息表
-- ----------------------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customer_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '客户编号',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `id_type` VARCHAR(20) NOT NULL DEFAULT 'ID_CARD' COMMENT '证件类型: ID_CARD-身份证, PASSPORT-护照, OTHER-其他',
    `id_no` VARCHAR(50) NOT NULL COMMENT '证件号码',
    `gender` CHAR(1) COMMENT '性别: M-男, F-女',
    `birth_date` DATE COMMENT '出生日期',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `address` VARCHAR(255) COMMENT '联系地址',
    `occupation` VARCHAR(50) COMMENT '职业',
    `nationality` VARCHAR(50) DEFAULT '中国' COMMENT '国籍',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '客户状态: 0-待审核, 1-审核通过, 2-审核拒绝, 3-已冻结',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态: 0-待审核, 1-审核中, 2-已通过, 3-已拒绝',
    `id_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '身份认证: 0-未认证, 1-已认证',
    `face_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '人脸认证: 0-未认证, 1-已认证',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_by` BIGINT COMMENT '创建人ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_customer_no` (`customer_no`),
    INDEX `idx_id_no` (`id_no`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`),
    INDEX `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

-- ----------------------------------------
-- 账户表
-- ----------------------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `account_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '账户编号',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `account_type` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '账户类型: NORMAL-普通账户, VIP-VIP账户',
    `balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    `frozen_balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '冻结余额',
    `available_balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
    `currency` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '账户状态: 0-待激活, 1-正常, 2-冻结, 3-销户',
    `open_way` VARCHAR(20) NOT NULL DEFAULT 'ONLINE' COMMENT '开户方式: ONLINE-线上, OFFLINE-线下',
    `open_channel` VARCHAR(50) COMMENT '开户渠道: APP, WEB, H5等',
    `open_time` DATETIME COMMENT '开户时间',
    `close_time` DATETIME COMMENT '销户时间',
    `last_trans_time` DATETIME COMMENT '最后交易时间',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_account_no` (`account_no`),
    INDEX `idx_customer_id` (`customer_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

-- ----------------------------------------
-- 审核记录表
-- ----------------------------------------
DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `audit_type` VARCHAR(20) NOT NULL COMMENT '审核类型: CUSTOMER_INFO-客户信息, ID_VERIFY-身份认证, OPEN_ACCOUNT-开户审核',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态: 0-待审核, 1-审核通过, 2-审核拒绝',
    `audit_result` VARCHAR(500) COMMENT '审核意见',
    `audit_by` BIGINT COMMENT '审核人ID',
    `audit_time` DATETIME COMMENT '审核时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_customer_id` (`customer_id`),
    INDEX `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核记录表';

-- ----------------------------------------
-- 营业部表
-- ----------------------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dept_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '营业部编号',
    `dept_name` VARCHAR(100) NOT NULL COMMENT '营业部名称',
    `address` VARCHAR(255) NOT NULL COMMENT '营业部地址',
    `phone` VARCHAR(20) NOT NULL COMMENT '营业部电话',
    `introduction` VARCHAR(500) COMMENT '营业部介绍',
    `province` VARCHAR(50) NOT NULL COMMENT '所在省',
    `city` VARCHAR(50) NOT NULL COMMENT '所在市',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
    `create_by` BIGINT COMMENT '创建人ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_dept_no` (`dept_no`),
    INDEX `idx_dept_name` (`dept_name`),
    INDEX `idx_province` (`province`),
    INDEX `idx_city` (`city`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营业部表';

-- ----------------------------------------
-- 操作日志表
-- ----------------------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT COMMENT '操作人ID',
    `username` VARCHAR(50) COMMENT '操作人用户名',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作内容',
    `method` VARCHAR(200) COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `ip` VARCHAR(50) COMMENT '操作IP',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-成功, 0-失败',
    `error_msg` VARCHAR(500) COMMENT '错误信息',
    `cost_time` INT COMMENT '耗时（毫秒）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------------------
-- 初始化数据
-- ----------------------------------------

-- 系统用户 (密码为 MD5(admin123) = 0192023a7bbd73250516f069df18b500)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role`, `status`, `phone`, `email`) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 'admin', 1, '13800000001', 'admin@opening.com'),
('auditor01', '0192023a7bbd73250516f069df18b500', '审核员01', 'auditor', 1, '13800000002', 'auditor01@opening.com'),
('operator01', '0192023a7bbd73250516f069df18b500', '操作员01', 'operator', 1, '13800000003', 'operator01@opening.com');

-- 示例客户数据
INSERT INTO `customer` (`customer_no`, `real_name`, `id_type`, `id_no`, `gender`, `birth_date`, `phone`, `email`, `address`, `occupation`, `status`, `audit_status`, `id_verified`, `face_verified`) VALUES
('C202606180001', '张三', 'ID_CARD', '110101199001011234', 'M', '1990-01-01', '13900001111', 'zhangsan@example.com', '北京市朝阳区建国路88号', 'IT工程师', 1, 2, 1, 1),
('C202606180002', '李四', 'ID_CARD', '310101199205152345', 'F', '1992-05-15', '13900002222', 'lisi@example.com', '上海市浦东新区陆家嘴环路1000号', '金融分析师', 1, 2, 1, 1),
('C202606180003', '王五', 'ID_CARD', '440101198812203456', 'M', '1988-12-20', '13900003333', 'wangwu@example.com', '广州市天河区珠江新城', '教师', 0, 0, 0, 0),
('C202606180004', '赵六', 'ID_CARD', '510101199508084567', 'F', '1995-08-08', '13900004444', 'zhaoliu@example.com', '成都市武侯区天府大道', '医生', 0, 1, 1, 0);

-- 示例账户数据
INSERT INTO `account` (`account_no`, `customer_id`, `account_type`, `balance`, `frozen_balance`, `available_balance`, `currency`, `status`, `open_way`, `open_channel`, `open_time`) VALUES
('A202606180001', 1, 'NORMAL', 100000.00, 0.00, 100000.00, 'CNY', 1, 'ONLINE', 'WEB', '2026-06-10 09:30:00'),
('A202606180002', 1, 'VIP', 500000.00, 10000.00, 490000.00, 'CNY', 1, 'ONLINE', 'APP', '2026-06-12 14:20:00'),
('A202606180003', 2, 'NORMAL', 50000.00, 0.00, 50000.00, 'CNY', 1, 'ONLINE', 'H5', '2026-06-15 10:45:00');

-- 示例审核记录
INSERT INTO `audit_record` (`customer_id`, `audit_type`, `audit_status`, `audit_result`, `audit_by`, `audit_time`) VALUES
(1, 'CUSTOMER_INFO', 1, '客户信息完整，身份认证通过', 2, '2026-06-10 09:25:00'),
(1, 'OPEN_ACCOUNT', 1, '开户申请审核通过', 2, '2026-06-10 09:28:00'),
(2, 'CUSTOMER_INFO', 1, '资料齐全，审核通过', 2, '2026-06-15 10:40:00');

-- 示例营业部数据
INSERT INTO `department` (`dept_no`, `dept_name`, `address`, `phone`, `introduction`, `province`, `city`, `status`, `create_by`) VALUES
('D202606180001', '北京朝阳营业部', '北京市朝阳区建国路88号SOHO现代城A座1层', '010-88888888', '北京朝阳营业部成立于2010年，是公司在华北地区的核心营业部，致力于为客户提供专业、高效的金融服务。', '北京市', '北京市', 1, 1),
('D202606180002', '上海浦东营业部', '上海市浦东新区陆家嘴环路1000号恒生银行大厦5层', '021-66666666', '上海浦东营业部位于金融中心陆家嘴，拥有专业的服务团队，为客户提供全方位的投资理财服务。', '上海市', '上海市', 1, 1),
('D202606180003', '广州天河营业部', '广州市天河区珠江新城华夏路10号富力中心3层', '020-33333333', '广州天河营业部是公司在华南地区的重要网点，服务范围覆盖珠三角地区，为客户提供优质的金融服务体验。', '广东省', '广州市', 1, 1),
('D202606180004', '深圳南山营业部', '深圳市南山区科技园南区深南大道9996号松日鼎盛大厦8层', '0755-22222222', '深圳南山营业部紧邻科技园，专注服务科创企业和高净值客户，提供创新型金融产品和服务。', '广东省', '深圳市', 1, 1),
('D202606180005', '成都武侯营业部', '成都市武侯区天府大道北段1480号拉德方斯大厦6层', '028-88888888', '成都武侯营业部是公司在西南地区的重要布局，为四川及周边地区客户提供专业的投资理财服务。', '四川省', '成都市', 0, 1);
