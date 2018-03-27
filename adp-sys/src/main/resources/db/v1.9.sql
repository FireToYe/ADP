CREATE TABLE IF NOT EXISTS `sys_properties` (
  `id` varchar(64) NOT NULL COMMENT '编号ID',
  `pro_key` varchar(200) NOT NULL DEFAULT '' COMMENT '字段key',
  `pro_value` text NOT NULL COMMENT '字段value',
  `modify_by` varchar(11) DEFAULT NULL COMMENT '是否可手动修改',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数表';

INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000300080002', '1000200030008', '0,1,10002,100020003,1000200030008,', '修改', '60', '', '', '', '0', 'sys:sysProperties:edit', '', '0', '1', '2018-01-16 17:17:31', '1', '2018-01-16 17:17:31', '', '0', 'update', '修改', '修改');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000300080001', '1000200030008', '0,1,10002,100020003,1000200030008,', '查看', '30', '', '', '', '0', 'sys:sysProperties:view', '', '0', '1', '2018-01-16 17:17:02', '1', '2018-01-16 17:20:38', '', '0', 'view', '查看', '查看');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000200030008', '100020003', '0,1,10002,100020003,', '参数管理', '1080', '/sys/sysProperties', '', '', '1', '', '', '0', '1', '2018-01-16 17:16:32', '1', '2018-01-16 17:16:32', '', '0', 'Parameter management', '參數管理', '参数管理');

INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000200050003', '100020005', '0,1,10002,100020005,', '系统信息', '90', '/sys/systemInfo/show', '', '', '1', 'sys:systemInfo:view', '', '0', '1', '2018-01-31 11:27:40', '1', '2018-02-02 15:41:46', '', '0', 'System Information', '系統信息', '系统信息');

UPDATE `sys_menu` SET `id`='1000200030002', `parent_id`='100020003', `parent_ids`='0,1,10002,100020003,', `name`='模板管理', `sort`='90', `href`='/sys/printTemplate', `target`='', `icon`='', `is_show`='1', `permission`='', `code`='', `menu_type`='0', `create_by`='1', `create_date`='2017-10-13 08:57:21', `update_by`='1', `update_date`='2017-10-13 08:57:21', `remarks`='', `del_flag`='0', `name_en_US`='Template management', `name_zh_TW`='模板管理', `name_zh_CN`='模板管理' WHERE `id`='1000200030002';

ALTER TABLE sys_news ADD del_status varchar(1) DEFAULT '0' COMMENT '消息状态''1''已读''0''未读';
ALTER TABLE sys_news ADD accepter_id varchar(64) DEFAULT '12';