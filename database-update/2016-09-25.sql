
ALTER TABLE `express` ADD `batchNo` VARCHAR(16)  NULL  DEFAULT NULL  COMMENT '批次号'  AFTER `expressId`;
ALTER TABLE `ship_order_source` ADD `schoolId` VARCHAR(32)  NULL  DEFAULT NULL  COMMENT '学校 ID'  AFTER `district`;
ALTER TABLE `ship_order` ADD `schoolId` VARCHAR(32)  NULL  DEFAULT NULL  COMMENT '学校 ID'  AFTER `district`;
ALTER TABLE `express` ADD `expressSchoolId` VARCHAR(32)  NULL  DEFAULT NULL  COMMENT '学校 ID'  AFTER `expressMobile`;

INSERT INTO `sys_module` (`sysModuleId`, `moduleName`, `moduleParentId`, `moduleUrl`, `status`, `createTime`, `updateTime`) VALUES (22, '用户统计', NULL, '/user/statistics/piecework', 0, '2016-09-25 16:37:20', '2016-09-25 16:37:20');
INSERT INTO `sys_role_has_sys_module` (`roleId`, `moduleId`) VALUES ('2', '22');

INSERT INTO `sys_module` (`sysModuleId`, `moduleName`, `moduleParentId`, `moduleUrl`, `status`, `createTime`, `updateTime`) VALUES (23, '未打包数据统计', 6, '/admin/statistics/pieceworkUnPackage', 0, '2016-09-26 21:40:28', '2016-09-26 21:40:28');
INSERT INTO `sys_role_has_sys_module` (`roleId`, `moduleId`) VALUES	('1', '23');
