INSERT INTO `sys_menu` VALUES ('100050018', '10005', '0,1,10005,', '权限管理', '5300', '', '', '', '1', '', '', '1', '1', '2017-11-23 18:36:02', '1', '2017-11-23 18:36:02', '', '0');
INSERT INTO `sys_menu` VALUES ('1000500180001', '100050018', '0,1,10005,100050018,', '用户管理', '30', '/manage/user', '', '', '1', '', '', '1', '1', '2017-11-23 18:36:41', '1', '2017-11-24 09:37:42', '', '0');
INSERT INTO `sys_menu` VALUES ('1000500180002', '100050018', '0,1,10005,100050018,', '角色管理', '60', '/manage/role', '', '', '1', '', '', '1', '1', '2017-11-23 18:37:36', '1', '2017-11-23 18:37:52', '', '0');


ALTER TABLE `sys_message` CHANGE `del_status` `del_status` varchar(1) DEFAULT '0' COMMENT '消息状态''1''已读''0''未读';
update sys_dict set value = '0',sort = 10 where id = '0b6e1335f60441749809421804f57e1c';
update sys_dict set value = '1',sort = 20 where id = '6b1cef0a182f4a9c87b2097b97927ba7';