/*
修改 MySQL 的 sql_mode,关闭 only full group by
mysql> show global variables like 'sql_mode';
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
| Variable_name | Value                                                                                                                                     |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
| sql_mode      | ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)

mysql> set global sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
Query OK, 0 rows affected (0.00 sec)

mysql> show global variables like 'sql_mode';
+---------------+------------------------------------------------------------------------------------------------------------------------+
| Variable_name | Value                                                                                                                  |
+---------------+------------------------------------------------------------------------------------------------------------------------+
| sql_mode      | STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION |
+---------------+------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.01 sec)
*/

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema logistics_kemis
-- -----------------------------------------------------


-- -----------------------------------------------------
-- Schema logistics_kemis
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `logistics_kemis` DEFAULT CHARACTER SET utf8 ;
USE `logistics_kemis` ;

-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_role` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_role` (
  `sysRoleId` INT(11) NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(20) NULL DEFAULT NULL COMMENT '角色名称',
  `roleDesc` VARCHAR(32) NULL DEFAULT NULL COMMENT '角色描述',
  `roleParentId` INT(11) NULL COMMENT '父角色',
  `permissionName` VARCHAR(32) NOT NULL DEFAULT 'ROLE_DEFAULT' COMMENT '权限标识',
  `status` TINYINT(4) NOT NULL DEFAULT '0',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sysRoleId`),
  UNIQUE INDEX `roleName_UNIQUE` (`roleName` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '系统角色表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_user` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_user` (
  `sysUserId` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(20) NULL DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(60) NULL DEFAULT NULL COMMENT '密码',
  `userCode` VARCHAR(16) NOT NULL COMMENT '代码-用于扫码登录',
  `realName` VARCHAR(20) NULL DEFAULT NULL COMMENT '姓名',
  `mobile` VARCHAR(16) NULL DEFAULT NULL COMMENT '手机号码',
  `email` VARCHAR(20) NULL DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(128) NULL DEFAULT NULL COMMENT '地址',
  `avatar` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户头像地址',
  `loginCount` INT(11) NULL DEFAULT '0' COMMENT '登录次数',
  `lastLogin` DATETIME NULL DEFAULT NULL COMMENT '上次登录时间',
  `status` TINYINT(4) NOT NULL DEFAULT '0',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sysUserId`),
  INDEX `idx_code` (`userCode` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '系统用户表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`ship_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`ship_order` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`ship_order` (
  `shipOrderId` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batchNo` VARCHAR(16) NOT NULL COMMENT '批次号',
  `userId` VARCHAR(32) NULL COMMENT '用户ID',
  `userName` VARCHAR(255) NULL COMMENT '用户姓名',
  `province` VARCHAR(32) NULL COMMENT '省',
  `city` VARCHAR(32) NULL COMMENT '市',
  `district` VARCHAR(64) NULL DEFAULT '0' COMMENT '区',
  `schoolId` VARCHAR(32) NULL COMMENT '学校 ID',
  `school` VARCHAR(64) NULL COMMENT '学校',
  `theClass` VARCHAR(32) NULL COMMENT '班级',
  `consignee` VARCHAR(64) NULL COMMENT '收货人',
  `mobile` VARCHAR(16) NULL COMMENT '收货人电话号码',
  `delivery` VARCHAR(16) NULL DEFAULT '普通快递' COMMENT '配送方式： EMS，普通快递',
  `address` VARCHAR(255) NULL COMMENT '地址',
  `totalCount` SMALLINT(8) NULL DEFAULT 0 COMMENT '奖品总数',
  `unitCredits` VARCHAR(16) NULL DEFAULT '学豆' COMMENT '积分单位： 学豆，园丁豆',
  `subject` VARCHAR(32) NULL COMMENT '教师科目',
  `campusAmbassador` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否校园大使 0 否 1 是',
  `expressId` BIGINT(20) NULL COMMENT '快递单ID',
  `printExpress` BIT(1) NOT NULL DEFAULT 0 COMMENT '是打印过快递单',
  `outExpress` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否导出过快递单',
  `outVerify` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否导出校验老师单',
  `teacherTag` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否是老师的标记',
  `bigPackage` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否大包裹',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态 数据导入时 老师默认值 0 学生默认值 1，老师单校验后值为1。仅值为1的可以打印快递单发货。值为2已发货，3已签收，4异常件。',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`shipOrderId`),
  INDEX `idx_user` (`userId` ASC, `batchNo` ASC, `province` ASC, `city` ASC, `district` ASC, `school` ASC, `theClass` ASC, `consignee` ASC),
  INDEX `idx_expressId` (`expressId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '发货单表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`ship_order_goods`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`ship_order_goods` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`ship_order_goods` (
  `shipOrderGoodsId` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shipOrderId` BIGINT(20) UNSIGNED NOT NULL COMMENT '发货单ID',
  `goodsName` VARCHAR(128) NOT NULL COMMENT '物品名称',
  `goodsCount` SMALLINT(8) NOT NULL DEFAULT 1 COMMENT '物品数量',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`shipOrderGoodsId`),
  INDEX `fk_orderId_idx` (`shipOrderId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '发货单物品表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_user_has_sys_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_user_has_sys_role` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_user_has_sys_role` (
  `userId` INT(11) NOT NULL COMMENT '用户角色对应表',
  `roleId` INT(11) NOT NULL,
  PRIMARY KEY (`userId`, `roleId`),
  INDEX `fk_user_role_sys_role_idx` (`roleId` ASC),
  INDEX `fk_user_role_sys_user_idx` (`userId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_module`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_module` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_module` (
  `sysModuleId` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `moduleName` VARCHAR(32) NOT NULL COMMENT '模块名称',
  `moduleParentId` INT(11) NULL DEFAULT NULL COMMENT '父角色',
  `moduleUrl` VARCHAR(64) NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0',
  `createTime` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sysModuleId`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '系统模块表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_role_has_sys_module`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_role_has_sys_module` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_role_has_sys_module` (
  `roleId` INT(11) NOT NULL,
  `moduleId` INT(11) NOT NULL,
  PRIMARY KEY (`roleId`, `moduleId`),
  INDEX `fk_role_module_sys_module_idx` (`moduleId` ASC),
  INDEX `fk_role_module_sys_role_idx` (`roleId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `logistics_kemis`.`work_process`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`work_process` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`work_process` (
  `workProcessId` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` INT(11) NOT NULL COMMENT '系统用户ID',
  `expressNumber` VARCHAR(64) NOT NULL COMMENT '物流单号ß',
  `workProcess` ENUM('cutOrder', 'pickingGoods', 'package', 'wrapp') NOT NULL DEFAULT 'cutOrder' COMMENT '工序 cutOrder 划单 pickingGoods 捡货 wrapp 包装 package 打包',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`workProcessId`),
  INDEX `fk_userId_idx` (`userId` ASC),
  UNIQUE INDEX `expressNumber_workProcess_UNIQUE` (`workProcess` ASC, `expressNumber` ASC, `status` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '工序表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`replace_keywords`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`replace_keywords` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`replace_keywords` (
  `replaceKeywordsId` INT(11) NOT NULL AUTO_INCREMENT COMMENT '关键字ID',
  `keyword` VARCHAR(128) NOT NULL COMMENT '关键字',
  `type` ENUM('goods', 'address', 'bigPackage') NOT NULL DEFAULT 'goods' COMMENT '关键字类型',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`replaceKeywordsId`),
  UNIQUE INDEX `keyword_UNIQUE` (`keyword` ASC, `type` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '替换关键字表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`exception_express`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`exception_express` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`exception_express` (
  `exceptionExpressId` BIGINT(20) UNSIGNED NOT NULL COMMENT '主键',
  `shipOrderId` BIGINT(20) UNSIGNED NOT NULL COMMENT '批次号',
  `mode` ENUM('refund', 'process', 'reissue') NOT NULL DEFAULT 'process' COMMENT '处理方式',
  `province` VARCHAR(32) NOT NULL COMMENT '省',
  `city` VARCHAR(32) NOT NULL COMMENT '市',
  `district` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT '区',
  `school` VARCHAR(64) NOT NULL COMMENT '学校',
  `theClass` VARCHAR(32) NOT NULL COMMENT '班级',
  `consignee` VARCHAR(64) NOT NULL COMMENT '收货人',
  `mobile` VARCHAR(16) NOT NULL COMMENT '收货人电话号码',
  `delivery` VARCHAR(16) NOT NULL DEFAULT '普通快递' COMMENT '配送方式： EMS，普通快递',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `expressId` BIGINT(20) NULL COMMENT '快递单ID',
  `expressNumber` VARCHAR(64) NULL COMMENT '物流单号',
  `expressCompany` VARCHAR(64) NULL COMMENT '物流公司',
  `printExpress` BIT(1) NOT NULL DEFAULT 0 COMMENT '是打印过快递单',
  `outExpress` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否导出过快递单',
  `outVerify` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否导出校验老师单',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态 默认值 1。仅值为1的可以打印快递单发货。值为2已发货，3已签收，4异常件。',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`exceptionExpressId`),
  INDEX `fk_shipOrderId_idx` (`shipOrderId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '异常件处理表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`upload_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`upload_file` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`upload_file` (
  `uploadFileId` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batchNo` VARCHAR(8) NOT NULL COMMENT '批次号',
  `fileName` VARCHAR(64) NOT NULL COMMENT '文件名',
  `fileSuffix` VARCHAR(16) NOT NULL COMMENT '文件后缀',
  `fileGuid` VARCHAR(36) NOT NULL COMMENT '文件全球唯一码',
  `fileUrl` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '文件地址',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uploadFileId`),
  UNIQUE INDEX `idx_guid` (`fileGuid` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '发货单文件上传记录表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`sys_configuration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`sys_configuration` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`sys_configuration` (
  `propertyKey` VARCHAR(128) NOT NULL COMMENT '键',
  `propertyValue` VARCHAR(128) NULL COMMENT '值',
  `desc` VARCHAR(255) NULL COMMENT '描述',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`propertyKey`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '系统配置表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`ship_order_source`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`ship_order_source` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`ship_order_source` (
  `shipOrderSourceId` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batchNo` VARCHAR(16) NOT NULL COMMENT '批次号',
  `userId` VARCHAR(32) NULL COMMENT '用户ID',
  `userName` VARCHAR(255) NULL COMMENT '用户姓名',
  `province` VARCHAR(32) NULL COMMENT '省',
  `city` VARCHAR(32) NULL COMMENT '市',
  `district` VARCHAR(64) NULL DEFAULT '0' COMMENT '区',
  `schoolId` VARCHAR(32) NULL COMMENT '学校 ID',
  `school` VARCHAR(64) NULL COMMENT '学校',
  `theClass` VARCHAR(32) NULL COMMENT '班级',
  `consignee` VARCHAR(64) NULL COMMENT '收货人',
  `mobile` VARCHAR(16) NULL COMMENT '收货人电话号码',
  `delivery` VARCHAR(16) NULL DEFAULT '普通快递' COMMENT '配送方式： EMS，普通快递',
  `shipGoods` TEXT NULL COMMENT '奖品明细 ',
  `address` VARCHAR(255) NULL COMMENT '地址',
  `totalCount` SMALLINT(8) NULL DEFAULT 0 COMMENT '奖品总数',
  `unitCredits` VARCHAR(16) NULL DEFAULT '学豆' COMMENT '积分单位： 学豆，园丁豆',
  `subject` VARCHAR(32) NULL COMMENT '教师科目',
  `campusAmbassador` BIT(1) NULL DEFAULT b'0' COMMENT '是否校园大使 0 否 1 是',
  `expressId` BIGINT(20) NULL COMMENT '快递单ID',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态 数据导入时 默认值 0 默认值 处理后值为 1',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`shipOrderSourceId`),
  INDEX `idx_user` (`userId` ASC, `batchNo` ASC, `expressId` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '发货单原始数据表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`express`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`express` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`express` (
  `expressId` BIGINT(20) NOT NULL COMMENT '快递单ID',
  `batchNo` VARCHAR(16) NOT NULL COMMENT '批次号',
  `expressCompany` VARCHAR(64) NULL COMMENT '物流公司',
  `expressNumber` VARCHAR(64) NULL COMMENT '物流单号',
  `type` VARCHAR(64) NULL COMMENT '类型',
  `delivery` VARCHAR(16) NULL DEFAULT '普通快递' COMMENT '配送方式： EMS，普通快递',
  `exportTag` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否导回',
  `price` INT(11) NULL COMMENT '价格，单位分',
  `weight` VARCHAR(45) NULL COMMENT '重量',
  `expressConsignee` VARCHAR(64) NULL COMMENT '收货人',
  `expressMobile` VARCHAR(16) NULL COMMENT '收货人电话',
  `expressSchoolId` VARCHAR(32) NULL COMMENT '学校 ID',
  `expressSchool` VARCHAR(45) NULL COMMENT '学校名称',
  `expressProvince` VARCHAR(32) NULL COMMENT '省',
  `expressCity` VARCHAR(32) NULL COMMENT '市',
  `expressDistrict` VARCHAR(45) NULL COMMENT '区',
  `expressAddress` VARCHAR(255) NULL COMMENT '详细地址',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态 ',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`expressId`),
  INDEX `expressNumber` (`expressNumber` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '物流信息表';


-- -----------------------------------------------------
-- Table `logistics_kemis`.`export_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistics_kemis`.`export_file` ;

CREATE TABLE IF NOT EXISTS `logistics_kemis`.`export_file` (
  `exportFileId` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batchNo` VARCHAR(8) NOT NULL COMMENT '批次号',
  `fileName` VARCHAR(255) NOT NULL COMMENT '文件名',
  `fileSuffix` VARCHAR(16) NOT NULL COMMENT '文件后缀',
  `fileGuid` VARCHAR(36) NOT NULL COMMENT '文件全球唯一码',
  `fileUrl` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '文件地址',
  `type` ENUM('delivery', 'order', 'deliveryResult') NULL COMMENT '文件类型：delivery 快递单 order 发货单 deliveryResult 发货结果',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`exportFileId`),
  UNIQUE INDEX `idx_guid` (`fileGuid` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '文件导出记录表';



-- -----------------------------------------------------
-- 请勿手工写入数据 供 Spring security remember-me 功能使用
-- Table `logistics_kemis`.`persistent_logins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `persistent_logins`;

CREATE TABLE `persistent_logins` (
  `username`  VARCHAR(64) NOT NULL,
  `series`    VARCHAR(64) NOT NULL,
  `token`     VARCHAR(64) NOT NULL,
  `last_used` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
