update sys_dict set sort='10' where id='41';
update sys_dict set sort='20' where id='09c185b78da348bda8a2c8951734c70a';
update sys_dict set sort='30' where id='40';
update sys_dict set sort='40' where id='39';

update sys_user set user_type='3' where user_type is null or user_type =''; 

INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10002000700020002', '1000200070002', '0,1,10002,100020007,1000200070002,', '修改', '60', '', '', '', '1', 'sys:sysMessageSend:edit', '', '0', '1', '2018-01-11 15:43:36', '1', '2018-01-11 15:43:36', '', '0', 'Modify', '修改', '修改');
