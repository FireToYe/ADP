INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000300070002', '1000200030007', '0,1,10002,100020003,1000200030007,', '修改', '40', '', '', '', '0', 'sys:sysExtendWord:edit', '', '0', '1', '2017-12-20 15:00:00', '1', '2017-12-20 17:30:15', '', '0', 'modify', '修改', '修改');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000200030007', '100020003', '0,1,10002,100020003,', '扩展字段', '120', '/sys/extendWord/', NULL, '', '1', NULL, NULL, '0', '1', '2017-12-20 15:00:00', '1', '2017-12-20 15:00:00', NULL, '0', 'Extend Word', '擴展字段', '扩展字段');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000300070001', '1000200030007', '0,1,10002,100020003,1000200030007,', '查看', '30', NULL, NULL, NULL, '0', 'sys:sysExtendWord:view', NULL, '0', '1', '2017-12-20 15:00:00', '1', '2017-12-20 15:00:00', NULL, '0', 'View', '查看', '查看');
update sys_dict set label = '根据组织',label_zh_CN = '根据组织' where type = 'accepter_type' and value = '1' ;

CREATE TABLE IF NOT EXISTS`sys_extend_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '数据库里显示的字段名',
  `display_name` varchar(100) NOT NULL COMMENT '前端页面显示的字段名',
  `type` varchar(50) NOT NULL COMMENT '数据库里对应的字段类型（0文本,1大文本，2日期3数字）',
  `sort` int(10) NOT NULL COMMENT '排序',
  `key` varchar(50) NOT NULL COMMENT '对应的接口路径',
  `comments` varchar(50) DEFAULT NULL COMMENT 'key的描述',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_by` varchar(64) NOT NULL,
  `update_date` datetime NOT NULL,
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `display_name_CN` varchar(100) DEFAULT NULL,
  `display_name_TW` varchar(100) DEFAULT NULL,
  `display_name_UN` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT IGNORE INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `label_en_US`, `label_zh_TW`, `label_zh_CN`) VALUES ('115', 'int', '数字', 'extend_type', '扩展字段类型', '40', '0', '1', '2017-12-20 17:00:00', '1', '2017-12-25 15:48:23', '', '0', 'Number', '數字', '数字');
INSERT IGNORE INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `label_en_US`, `label_zh_TW`, `label_zh_CN`) VALUES ('114', 'datetime', '日期', 'extend_type', '扩展字段类型', '30', '0', '1', '2017-12-20 17:00:00', '1', '2017-12-25 15:48:17', '', '0', 'Date', '日期', '日期');
INSERT IGNORE INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `label_en_US`, `label_zh_TW`, `label_zh_CN`) VALUES ('113', 'text', '大文本', 'extend_type', '扩展字段类型', '20', '0', '1', '2017-12-20 17:00:00', '1', '2017-12-25 15:47:52', '', '0', 'Big Text', '大文本', '大文本');
INSERT IGNORE INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `label_en_US`, `label_zh_TW`, `label_zh_CN`) VALUES ('112', 'varchar', '文本', 'extend_type', '扩展字段类型', '10', '0', '1', '2017-12-20 17:00:00', '1', '2017-12-25 15:47:34', '', '0', 'text', '文本', '文本');
INSERT IGNORE INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `label_en_US`, `label_zh_TW`, `label_zh_CN`) VALUES ('899ae44b80614943886a1922e3627f3c', '4', '即享', 'app_list_app_type', '第三方app名称', '40', '0', '1', '2017-12-28 09:49:19', '1', '2017-12-28 09:49:19', '', '0', 'bac', '即享', '即享');
