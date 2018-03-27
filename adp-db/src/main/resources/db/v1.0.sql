INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000900010002', '1000200090001', '0,1,10002,100020009,1000200090001,', '修改', '60', '', '', '', '0', 'db:dbBackupScheme:edit', '', '0', '1', '2017-12-19 11:22:56', '1', '2017-12-19 11:22:56', '', '0', 'Modify permissions', '修改權限', '修改');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000900010001', '1000200090001', '0,1,10002,100020009,1000200090001,', '查看权限', '30', '', '', '', '0', 'db:dbBackupScheme:view', '', '0', '1', '2017-12-18 11:42:29', '1', '2017-12-18 11:44:35', '', '0', 'View permissions', '查看權限', '查看权限');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000200090002', '100020009', '0,1,10002,100020009,', '备份记录', '60', '/db/dbBackupScheme/history', '', '', '1', '', '', '0', '1', '2017-12-18 11:35:11', '1', '2017-12-19 16:44:18', '', '0', 'Backup Record', '備份記錄', '备份记录');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000200090001', '100020009', '0,1,10002,100020009,', '备份方案', '30', '/db/dbBackupScheme/list', '', '', '1', '', '', '0', '1', '2017-12-18 11:34:30', '1', '2017-12-18 11:34:30', '', '0', 'Backup Scheme', '備份方案', '备份方案');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('100020009', '10002', '0,1,10002,', '数据备份', '1080', '', '', '', '1', '', '', '0', '1', '2017-12-18 11:30:04', '1', '2017-12-18 11:32:33', '', '0', 'Data backup', '數據備份', '数据备份');

CREATE TABLE IF NOT  EXISTS `db_backup_scheme` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `scheme_name` varchar(32) NOT NULL COMMENT '方案名称',
  `backup_tables` varchar(1000) NOT NULL COMMENT '备份表(用,号分隔)',
  `extend` text COMMENT '扩展内容',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `job_id` varchar(20) DEFAULT NULL COMMENT '任务ID',
  `job_status` varchar(10) DEFAULT NULL COMMENT '任务状态',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `save_count` varchar(20) DEFAULT NULL COMMENT '保留文件数',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库备份方案';




