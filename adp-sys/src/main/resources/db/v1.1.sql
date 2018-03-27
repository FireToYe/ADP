CREATE TABLE `sys_news` (
  `id` varchar(64) NOT NULL COMMENT '编号ID',
  `subject` varchar(200) NOT NULL DEFAULT '' COMMENT '新闻标题',
  `content` mediumtext NOT NULL COMMENT '新闻内容',
  `click_count` int(11) NOT NULL DEFAULT '0' COMMENT '点击数',
  `attachment_id` text COMMENT '附件ID串,多个附件|隔开',
  `attachment_name` text COMMENT '附件名称串，多个附件名称|隔开',
  `type_id` varchar(200) NOT NULL DEFAULT '' COMMENT '新闻类型',
  `publish` char(1) NOT NULL DEFAULT '1' COMMENT '发布标识(0-未发布,1-已发布,2-已终止)',
  `top` varchar(2) NOT NULL DEFAULT '0' COMMENT '是否置顶(0-否,1-是)',
  `subject_color` varchar(8) DEFAULT NULL COMMENT '新闻标题颜色',
  `keyword` varchar(100) DEFAULT NULL COMMENT '内容关键词',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻通告表';

CREATE TABLE `sys_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accepter_id` varchar(64) NOT NULL COMMENT '用户ID',
  `type` varchar(1) DEFAULT '0' COMMENT '消息类型’0‘普通消息''1''系统消息',
  `del_status` varchar(1) DEFAULT '1' COMMENT '消息状态''0''已读''1''未读',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` varchar(1000) NOT NULL COMMENT '消息内容',
  `create_by` varchar(64) NOT NULL COMMENT '消息发送人',
  `create_date` datetime NOT NULL COMMENT '发送时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `source_message_id` int(11) NOT NULL COMMENT '源消息ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_message_send` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 自增',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` varchar(256) NOT NULL COMMENT '内容',
  `target` tinyint(4) NOT NULL COMMENT '目标对象(0全部 1按组织 2按角色 3按会员)',
  `count` int(11) DEFAULT NULL COMMENT '目标人数',
  `type` varchar(1) DEFAULT '0' COMMENT '消息类型''0''普通消息''1''系统消息',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态(0未发送 1已发送)',
  `receivers` text NOT NULL COMMENT '接收人',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息表,发送方';

/*字典字段*/
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('21b107b160684ea58d501cf760f93f4a', '1', '根据组织', 'accepter_type', '发送对象', '20', '0', '1', '2017-11-06 15:21:38', '1', '2017-11-06 16:33:05', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('2355b326633c4351ba8f3906d308cc53', '0', '全部用户', 'accepter_type', '发送对象', '10', '0', '1', '2017-11-06 15:20:54', '1', '2017-11-06 15:24:00', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('acbef31b42524800907a5cf203a4b6dd', '3', '根据用户', 'accepter_type', '发送对象', '40', '0', '1', '2017-11-06 15:22:55', '1', '2017-11-06 16:33:42', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('d106919187fb475784f805edee73791c', '2', '根据角色', 'accepter_type', '发送对象', '30', '0', '1', '2017-11-06 15:22:29', '1', '2017-11-06 16:33:34', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('0b6e1335f60441749809421804f57e1c', '1', '未读', 'msg_status', '消息状态', '10', '0', '1', '2017-11-01 17:23:37', '1', '2017-11-01 17:28:11', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('6b1cef0a182f4a9c87b2097b97927ba7', '0', '已读', 'msg_status', '消息状态', '10', '0', '1', '2017-11-01 17:22:49', '1', '2017-11-01 17:22:49', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('699ac6c5ac914d3399fbb81c49741f9f', '1', '系统消息', 'msg_type', '消息类型', '10', '0', '1', '2017-11-03 09:20:22', '1', '2017-11-03 09:20:22', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('acf6922dc5b14c3cb2df7e33a33ae811', '0', '普通消息', 'msg_type', '消息类型', '10', '0', '1', '2017-11-03 09:21:10', '1', '2017-11-03 09:21:10', '', '0');
/*新增菜单*/
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020007', '10002', '0,1,10002,', '发布管理', '1050', '', '', '', '1', '', '', '0', '1', '2017-11-09 15:56:45', '1', '2017-11-09 15:58:50', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200070001', '100020007', '0,1,10002,100020007,', '新闻通告', '30', '/sys/news/', '', '', '1', '', '', '0', '1', '2017-11-09 15:58:36', '1', '2017-11-09 16:01:39', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000700010001', '1000200070001', '0,1,10002,100020007,1000200070001,', '查看', '30', '', '', '', '1', 'sys:news:view', '', '0', '1', '2017-11-09 15:59:26', '1', '2017-11-09 15:59:26', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000700010002', '1000200070001', '0,1,10002,100020007,1000200070001,', '修改', '60', '', '', '', '1', 'sys:news:edit', '', '0', '1', '2017-11-09 15:59:56', '1', '2017-11-09 15:59:56', '', '0');

INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200070002', '100020007', '0,1,10002,100020007,', '消息管理', '60', '/sys/sysMessageSend', '', '', '1', '', '', '0', '1', '2017-11-09 16:17:24', '1', '2017-11-09 16:18:44', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000700020001', '1000200070002', '0,1,10002,100020007,1000200070002,', '查看', '30', '', '', '', '1', 'sys:sysMessageSend:view', '', '0', '1', '2017-11-09 16:53:27', '1', '2017-11-09 16:53:27', '', '0');


ALTER TABLE sys_app_list ADD extend text COMMENT '扩展内容' AFTER `info_keys`;
