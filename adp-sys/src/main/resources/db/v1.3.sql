ALTER TABLE `sys_message` add `attachment_ids`  varchar(512) DEFAULT '' COMMENT '附件路径字符串';
ALTER TABLE `sys_message` add `attachment_names`  varchar(512) DEFAULT '' COMMENT '附件路名称字符串';


ALTER TABLE `sys_message_send` add `attachment_ids`  varchar(512) DEFAULT '' COMMENT '附件路径字符串';
ALTER TABLE `sys_message_send` add `attachment_names`  varchar(512) DEFAULT '' COMMENT '附件路名称字符串';