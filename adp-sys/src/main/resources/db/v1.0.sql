 
-- ----------------------------
-- Table structure for sys_app_apply
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_app_apply` (
  `open_id` varchar(32) NOT NULL COMMENT 'openid',
  `app_id` varchar(32) NOT NULL COMMENT 'appid',
  `apply_info` varchar(2000) DEFAULT '' COMMENT '申请信息(json)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态(0-默认，1-已审核，2-审核不通过)',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `relate_user` varchar(64) DEFAULT NULL COMMENT '关联用户',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app授权申请表';

-- ----------------------------
-- Records of sys_app_apply
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_list
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_app_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(32) NOT NULL COMMENT 'appid',
  `app_secret` varchar(32) NOT NULL COMMENT 'app_secret',
  `token` varchar(32) DEFAULT NULL COMMENT 'token',
  `redirect` varchar(255) DEFAULT NULL COMMENT '跳转连接',
  `app_name` varchar(255) DEFAULT NULL COMMENT 'app名称',
  `app_type` tinyint(4) DEFAULT NULL COMMENT '类型(1-微信,2-QQ,3-微博)',
  `info_keys` varchar(2000) DEFAULT NULL COMMENT '申请内容字段(json)',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_list_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='app清单表';

-- ----------------------------
-- Records of sys_app_list
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_version
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_app_version` (
  `appName` varchar(255) NOT NULL COMMENT '应用项目名称',
  `version` varchar(20) NOT NULL COMMENT '当前版本',
  `uptime` datetime DEFAULT NULL COMMENT '升级时间',
  PRIMARY KEY (`appName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用版本管理表';

-- ----------------------------
-- Records of sys_app_version
-- ----------------------------

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_area` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `type` char(1) DEFAULT NULL COMMENT '区域类型',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_area_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_area_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域表';

-- ----------------------------
-- Records of sys_area
-- ----------------------------
INSERT INTO `sys_area` VALUES ('1', '0', '0,', '中国', '10', '100000', '1', '1', '2013-05-27 08:00:00', 'b2ab6cd9c1254289a9562d884c7ad050', '2017-10-20 10:42:48', 'd', '0');

-- ----------------------------
-- Table structure for sys_cache_version
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_cache_version` (
  `name` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_cache_version
-- ----------------------------
INSERT INTO `sys_cache_version` VALUES ('area_cache_version', '1508480689141');
INSERT INTO `sys_cache_version` VALUES ('dict_cache_version', '1508467505781');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_dict` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` decimal(10,0) NOT NULL COMMENT '排序（升序）',
  `parent_id` varchar(64) DEFAULT '0' COMMENT '父级编号',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`) USING BTREE,
  KEY `sys_dict_label` (`label`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('03f40b55a73c4f95b4dde83634b36357', '2', '已发货', 'srm_delivery_status', '发货状态', '20', '0', '1', '2017-09-29 09:17:26', '1', '2017-09-29 09:17:26', '', '0');
INSERT INTO `sys_dict` VALUES ('071272354d024d639b946dcfcc9e310b', 'ORACLE12G', 'ORACLE12G', 'iccn_ica_db_version', '数据库版本', '40', '0', '1', '2017-09-08 17:26:55', '1', '2017-09-08 17:26:55', '', '0');
INSERT INTO `sys_dict` VALUES ('07902cd627754c1493104a5e540d10ad', '6', '6:完工入库', 'srm_gentime', '条码产生时机', '60', '0', '1', '2017-09-06 13:19:09', '1', '2017-09-06 13:19:09', '', '0');
INSERT INTO `sys_dict` VALUES ('08824bdbf898474d8edcdaaa107af015', 'ORACLE10G', 'ORACLE10G', 'iccn_ica_db_version', '数据库版本', '20', '0', '1', '2017-09-08 17:26:37', '1', '2017-09-08 17:26:37', '数据库连接管理-数据库版本', '0');
INSERT INTO `sys_dict` VALUES ('0e8057b81091462ab2d4cf84c3732013', '2', '委外采购', 'srm_purchase_type', 'srm采购性质', '20', '0', '1', '2017-09-27 09:34:06', '1', '2017-09-27 10:00:15', '', '0');
INSERT INTO `sys_dict` VALUES ('0f024c5929264c0197f6022b1ad0865b', '3', '3:单件级', 'srm_item_type', '物料条码类型', '30', '0', '1', '2017-09-06 09:18:22', '1', '2017-09-06 12:54:52', '物料条码类型', '0');
INSERT INTO `sys_dict` VALUES ('0f3ce189a7d04590b67f0470f4f83dcc', '3', 'POJO方式执行', 'srm_execute_type', '定时任务执行方式', '30', '0', '1', '2017-09-26 10:21:05', '1', '2017-09-26 10:21:05', '', '0');
INSERT INTO `sys_dict` VALUES ('1', '0', '正常', 'del_flag', '删除标记', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('10', 'yellow', '黄色', 'color', '颜色值', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('105', '1', '会议通告\0\0', 'oa_notify_type', '通知通告类型', '10', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('106', '2', '奖惩通告\0\0', 'oa_notify_type', '通知通告类型', '20', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('107', '3', '活动通告\0\0', 'oa_notify_type', '通知通告类型', '30', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('108', '0', '草稿', 'oa_notify_status', '通知通告状态', '10', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('109', '1', '发布', 'oa_notify_status', '通知通告状态', '20', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('11', 'orange', '橙色', 'color', '颜色值', '50', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('110', '0', '未读', 'oa_notify_read', '通知通告状态', '10', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('111', '1', '已读', 'oa_notify_read', '通知通告状态', '20', '0', '1', '2013-11-08 08:00:00', '1', '2013-11-08 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('12', 'default', '默认主题', 'theme', '主题方案', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('12f72dc4d6c946d9badc18cf040b7aa8', '1', '物流', 'logistics_mode', '物流方式', '10', '0', '1', '2017-08-31 12:01:07', '1', '2017-08-31 12:01:07', '', '0');
INSERT INTO `sys_dict` VALUES ('13', 'cerulean', '天蓝主题', 'theme', '主题方案', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('14', 'readable', '橙色主题', 'theme', '主题方案', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('148a508bcce64e03bc4accddf987a06d', '3', '其他', 'field_type', '栏位类型', '30', '0', '1', '2017-10-12 20:01:24', '1', '2017-10-12 20:01:24', '', '0');
INSERT INTO `sys_dict` VALUES ('14c68909718a4e5dada650e1deb21125', 'C', 'C', 'srmSeqbase_level', 'C级别', '30', '0', '1', '2017-09-12 17:42:42', '1', '2017-09-12 17:42:42', '', '0');
INSERT INTO `sys_dict` VALUES ('15', 'united', '红色主题', 'theme', '主题方案', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('16', 'flat', 'Flat主题', 'theme', '主题方案', '60', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('17', '1', '国家', 'sys_area_type', '区域类型', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('18', '2', '省份、直辖市', 'sys_area_type', '区域类型', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('182a555408834d669e95c7c3c7a6a1d1', 'MMdd', 'MMdd', 'srmSeqsubsection_date_type', '日期格式', '30', '0', '1', '2017-09-14 13:59:07', '1', '2017-09-14 13:59:07', '', '0');
INSERT INTO `sys_dict` VALUES ('19', '3', '地市', 'sys_area_type', '区域类型', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('1b95cd3d772e42059e34767e721df79d', '1', 'SRM菜单', 'menu_type', '菜单类型', '10', '0', '1', '2017-09-04 17:36:02', '1', '2017-09-04 17:36:02', '菜单类型', '0');
INSERT INTO `sys_dict` VALUES ('1d56263d72ec4a9c9d4d2ac4edd1747a', '3', '已收货', 'srm_delivery_status', '发货状态', '30', '0', '1', '2017-09-29 09:17:44', '1', '2017-09-29 09:17:44', '', '0');
INSERT INTO `sys_dict` VALUES ('2', '1', '删除', 'del_flag', '删除标记', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('20', '4', '区县', 'sys_area_type', '区域类型', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('21', '1', '公司', 'sys_office_type', '机构类型', '60', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('21b203782bee4b64ad96df7b2edbd30a', '3', '微博', 'app_list_app_type', '第三方app名称', '30', '0', '1', '2017-09-28 11:24:29', '1', '2017-09-28 11:24:29', '', '0');
INSERT INTO `sys_dict` VALUES ('22', '2', '部门', 'sys_office_type', '机构类型', '70', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('23', '3', '小组', 'sys_office_type', '机构类型', '80', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('24', '4', '其它', 'sys_office_type', '机构类型', '90', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('24304f2f16144016a317739756f6aa00', '3', '3:IQC检验', 'srm_gentime', '条码产生时机', '30', '0', '1', '2017-09-06 13:17:53', '1', '2017-09-06 13:17:53', '条码产生时机', '0');
INSERT INTO `sys_dict` VALUES ('25', '1', '综合部', 'sys_office_common', '快捷通用部门', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('250f8fb2ceeb4df49af992ec3cef39c2', '0', '0:失败', 'ica_log_is_success', 'ica日志请求是否成功', '20', '0', '1', '2017-09-18 15:21:30', '1', '2017-09-18 15:34:07', '', '0');
INSERT INTO `sys_dict` VALUES ('26', '2', '开发部', 'sys_office_common', '快捷通用部门', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('2680dabf8e2f427eb444c15b5f9dcfea', 'M', 'M月', 'srmSeqbase_cycle', '循环级别', '30', '0', '1', '2017-09-12 16:18:44', '1', '2017-09-12 17:57:37', '', '0');
INSERT INTO `sys_dict` VALUES ('27', '3', '人力部', 'sys_office_common', '快捷通用部门', '50', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('28', '1', '一级', 'sys_office_grade', '机构等级', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('29', '2', '二级', 'sys_office_grade', '机构等级', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('2a67382cd5dc45b8850bdc619f94edb8', '2', '流水号', 'field_type', '栏位类型', '20', '0', '1', '2017-10-12 20:01:11', '1', '2017-10-12 20:01:11', '', '0');
INSERT INTO `sys_dict` VALUES ('2dbc7c327fbd4badbde9b81714253ba1', 'MYSQL', 'MYSQL', 'iccn_ica_db_type', '数据库类型', '10', '0', '1', '2017-09-08 17:25:07', '1', '2017-09-08 17:25:07', '数据库连接管理-数据库类型', '0');
INSERT INTO `sys_dict` VALUES ('2ee43f8b7bcc4f5d8097b0932dcfa238', 'B', 'B', 'srmSeqbase_level', 'B级别', '20', '0', '1', '2017-09-12 17:42:21', '1', '2017-09-12 17:42:21', '', '0');
INSERT INTO `sys_dict` VALUES ('3', '1', '显示', 'show_hide', '显示/隐藏', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('30', '3', '三级', 'sys_office_grade', '机构等级', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('3051283df5734c2783f833241637dfa1', '2', 'QQ', 'app_list_app_type', '第三方app名称', '20', '0', '1', '2017-09-28 11:24:22', '1', '2017-09-28 11:24:22', '', '0');
INSERT INTO `sys_dict` VALUES ('31', '4', '四级', 'sys_office_grade', '机构等级', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('32', '1', '所有数据', 'sys_data_scope', '数据范围', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('32137067006e47aca9ae01f439350504', 'S', 'S', 'srmSeqbase_level', 'S级别', '40', '0', '1', '2017-09-12 17:43:07', '1', '2017-09-12 17:43:07', '', '0');
INSERT INTO `sys_dict` VALUES ('33', '2', '所在公司及以下数据', 'sys_data_scope', '数据范围', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('34', '3', '所在公司数据', 'sys_data_scope', '数据范围', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('345d3bc4ed8149b48563481e2d63d798', '2', 'springbean方式', 'dc_job_execute_type', '定时任务执行方式', '20', '0', '1', '2017-10-11 14:47:48', '1', '2017-10-11 14:47:48', '', '0');
INSERT INTO `sys_dict` VALUES ('35', '4', '所在部门及以下数据', 'sys_data_scope', '数据范围', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('36', '5', '所在部门数据', 'sys_data_scope', '数据范围', '50', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('37', '8', '仅本人数据', 'sys_data_scope', '数据范围', '90', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('37cb6828b58c4b88b0bb27b99e3c45a2', '1', '微信', 'app_list_app_type', '第三方app名称', '10', '0', '1', '2017-09-28 11:24:12', '1', '2017-09-28 11:24:12', '', '0');
INSERT INTO `sys_dict` VALUES ('38', '9', '按明细设置', 'sys_data_scope', '数据范围', '100', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('381fd25c35a445019e0352cb8b41052e', '1', 'ica请求方式', 'dc_job_execute_type', '定时任务执行方式', '10', '0', '1', '2017-10-11 14:47:21', '1', '2017-10-11 14:47:21', '', '0');
INSERT INTO `sys_dict` VALUES ('39', '1', '系统管理', 'sys_user_type', '用户类型', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('3c1c78d5d5274ddba6b96e80eefca817', '1', 'sql执行', 'srm_execute_type', '定时任务执行方式', '10', '0', '1', '2017-09-26 10:20:22', '1', '2017-09-26 10:20:22', '', '0');
INSERT INTO `sys_dict` VALUES ('4', '0', '隐藏', 'show_hide', '显示/隐藏', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('40', '2', '部门经理', 'sys_user_type', '用户类型', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('41', '3', '普通用户', 'sys_user_type', '用户类型', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('42', 'basic', '基础主题', 'cms_theme', '站点主题', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('420728d1849b419ab951af28ad04be7b', '02', '02传入日期类型，默认当前时间', 'srmSeqsubsection_type', '分段类型是当前时间', '20', '0', '1', '2017-09-12 18:04:49', '1', '2017-09-12 18:04:49', '', '0');
INSERT INTO `sys_dict` VALUES ('425676d262e442b68a2eb9c0ae5a6a80', 'Y', 'Y:是', 'srm_yes_no', 'srm通用是否', '10', '0', '1', '2017-09-06 14:49:07', '1', '2017-09-06 14:51:16', '', '0');
INSERT INTO `sys_dict` VALUES ('437a146079b24901aa181936e7a4ab8d', 'N', 'N:非边线发料', 'srm_islineslid', '是否边线仓', '20', '0', '1', '2017-09-06 13:22:17', '1', '2017-09-06 13:35:50', '', '0');
INSERT INTO `sys_dict` VALUES ('45', 'article', '文章模型', 'cms_module', '栏目模型', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('4694b5640b484626902388579d72649d', 'SQLSERVER', 'SQLSERVER', 'iccn_ica_db_type', '数据库类型', '30', '0', '1', '2017-09-08 17:25:30', '1', '2017-09-08 17:25:30', '', '0');
INSERT INTO `sys_dict` VALUES ('48', 'link', '链接模型', 'cms_module', '栏目模型', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('4db6d2fb0940412c99c899790209fc7e', '4', '预先采购', 'srm_purchase_type', 'srm采购性质', '40', '0', '1', '2017-09-27 09:34:46', '1', '2017-09-27 10:00:29', '', '0');
INSERT INTO `sys_dict` VALUES ('4e32af2db5b04af9b27c05ac4e2ac2de', 'N', 'N:不管控', 'srm_fifocontr', '先进先出管控', '20', '0', '1', '2017-09-06 13:12:45', '1', '2017-09-06 13:12:45', '先进先出管控', '0');
INSERT INTO `sys_dict` VALUES ('5', '1', '是', 'yes_no', '是/否', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('50', '0', '默认展现方式', 'cms_show_modes', '展现方式', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('51', '1', '首栏目内容列表', 'cms_show_modes', '展现方式', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('510f69b2516d4afbba107fc54afab62d', 'N', 'N不按照日期循环', 'srmSeqbase_cycle', '循环级别', '50', '0', '1', '2017-09-12 16:19:28', '1', '2017-09-12 17:57:53', '', '0');
INSERT INTO `sys_dict` VALUES ('51714c94330e4c06a2bb9267396f7027', 'SQLSERVER2008', 'SQLSERVER2008', 'iccn_ica_db_version', '数据库版本', '60', '0', '1', '2017-09-08 17:27:15', '1', '2017-09-08 17:27:15', '数据库连接管理-数据库版本', '0');
INSERT INTO `sys_dict` VALUES ('52', '2', '栏目第一条内容', 'cms_show_modes', '展现方式', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('5219e3a58a974d4a9cdd6469883d5fea', '1', '1:物料级', 'srm_item_type', '物料条码类型', '10', '0', '1', '2017-09-06 09:15:56', '1', '2017-09-06 12:54:34', '物料条码类型', '0');
INSERT INTO `sys_dict` VALUES ('53', '0', '发布', 'cms_del_flag', '内容状态', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('54', '1', '删除', 'cms_del_flag', '内容状态', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('55', '2', '审核', 'cms_del_flag', '内容状态', '15', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('56', '1', '首页焦点图', 'cms_posid', '推荐位', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('57', '2', '栏目页文章推荐', 'cms_posid', '推荐位', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('58', '1', '咨询', 'cms_guestbook', '留言板分类', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('59', '2', '建议', 'cms_guestbook', '留言板分类', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('5937926398d049d680ee7be692c224fd', '2', '快递', 'logistics_mode', '物流方式', '20', '0', '1', '2017-08-31 12:01:39', '1', '2017-08-31 12:01:39', '', '0');
INSERT INTO `sys_dict` VALUES ('5a0e15026adf489687c48eaa3ce03ca7', 'D', 'D日', 'srmSeqbase_cycle', '循环级别', '40', '0', '1', '2017-09-12 16:18:58', '1', '2017-09-12 17:57:45', '', '0');
INSERT INTO `sys_dict` VALUES ('5e7c2549c0f04438b142d27db4037e62', 'ORACLE', 'ORACLE', 'iccn_ica_db_type', '数据库类型', '20', '0', '1', '2017-09-08 17:25:19', '1', '2017-09-08 17:25:19', '数据库连接管理-数据库类型', '0');
INSERT INTO `sys_dict` VALUES ('6', '0', '否', 'yes_no', '是/否', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('60', '3', '投诉', 'cms_guestbook', '留言板分类', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('61', '4', '其它', 'cms_guestbook', '留言板分类', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('62', '1', '公休', 'oa_leave_type', '请假类型', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('63', '2', '病假', 'oa_leave_type', '请假类型', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('64', '3', '事假', 'oa_leave_type', '请假类型', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('65', '4', '调休', 'oa_leave_type', '请假类型', '40', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('66', '5', '婚假', 'oa_leave_type', '请假类型', '60', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('67', '1', '接入日志', 'sys_log_type', '日志类型', '30', '0', '1', '2013-06-03 08:00:00', '1', '2013-06-03 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('68', '2', '异常日志', 'sys_log_type', '日志类型', '40', '0', '1', '2013-06-03 08:00:00', '1', '2013-06-03 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('685252bad62e43b8b4b20a964f5182ca', 'Y', 'Y', 'select', '选择', '10', '0', '1', '2017-09-06 22:36:22', '1', '2017-09-06 22:36:22', '', '0');
INSERT INTO `sys_dict` VALUES ('6b8e7746e4ea45659c639585f36e1a03', '1', '客户', 'app_apply_type', 'app授权申请类型', '10', '0', '1', '2017-10-11 11:41:59', '1', '2017-10-11 11:41:59', '', '0');
INSERT INTO `sys_dict` VALUES ('6eba60cd7c5d42948e6e942acab58bd1', '05', '05传入流水号范围', 'srmSeqsubsection_type', '分段类型是流水号的范围', '50', '0', '1', '2017-09-12 18:06:59', '1', '2017-09-12 18:06:59', '', '0');
INSERT INTO `sys_dict` VALUES ('7', 'red', '红色', 'color', '颜色值', '10', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('70a032e2d1a440f09f91ee57e2a21bb8', '03', '03流水号', 'srmSeqsubsection_type', '分段类型是流水号', '30', '0', '1', '2017-09-12 18:05:26', '1', '2017-09-12 18:05:26', '', '0');
INSERT INTO `sys_dict` VALUES ('724fdafca0c645b0adcf857e8d5614a7', '99', '99定义字符', 'srmSeqsubsection_type', '分段类型是定义的字符', '60', '0', '1', '2017-09-12 18:07:26', '1', '2017-09-12 18:07:26', '', '0');
INSERT INTO `sys_dict` VALUES ('787932bfbfdd4fd9894b620b60f74f77', '3', 'VMI采购', 'order_type', '单据性质', '30', '0', '1', '2017-08-14 10:25:09', '1', '2017-08-14 10:25:09', '', '0');
INSERT INTO `sys_dict` VALUES ('7c30167986b0411f8d62182984714a9a', 'X', '已作废', 'srm_delivery_status', '发货状态', '40', '0', '1', '2017-09-29 09:20:06', '1', '2017-09-29 09:20:06', '', '0');
INSERT INTO `sys_dict` VALUES ('8', 'green', '绿色', 'color', '颜色值', '20', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('84d497dac18e488bbad990bc5d467e89', '4', '4:采购入库', 'srm_gentime', '条码产生时机', '40', '0', '1', '2017-09-06 13:18:31', '1', '2017-09-06 13:18:31', '条码产生时机', '0');
INSERT INTO `sys_dict` VALUES ('86e8be60c57046bda5bcb72c85311e4c', 'yyyyMMdd', 'yyyyMMdd', 'srmSeqsubsection_date_type', '日期格式', '20', '0', '1', '2017-09-14 13:58:31', '1', '2017-09-14 13:58:31', '', '0');
INSERT INTO `sys_dict` VALUES ('88e404101238469fb9d06b3bbc1f92b0', 'Y', '已审核', 'srm_delivery_status', '发货状态', '50', '0', '1', '2017-09-29 09:20:22', '1', '2017-09-29 09:20:22', '', '0');
INSERT INTO `sys_dict` VALUES ('8915e0851c51417ab2db9da6ed70d9cf', '1', '固定值', 'field_type', '栏位类型', '10', '0', '1', '2017-10-12 20:00:35', '1', '2017-10-12 20:00:35', '', '0');
INSERT INTO `sys_dict` VALUES ('8ae353dbb0484ac7bab0ba74016d45de', '0', '未处理', 'app_apply_status', 'app授权状态', '10', '0', '1', '2017-09-28 12:27:57', '1', '2017-10-11 15:07:20', '', '0');
INSERT INTO `sys_dict` VALUES ('8ceb4673f3fb4329affda8541712f0f7', '1', '一般订单', 'order_type', '单据性质', '10', '0', '1', '2017-08-14 10:24:06', '1', '2017-10-19 17:00:37', '单据性质', '0');
INSERT INTO `sys_dict` VALUES ('9', 'blue', '蓝色', 'color', '颜色值', '30', '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('93e3260c2857445d8bb59eb263401f31', '3', 'VMI采购', 'srm_purchase_type', 'srm采购性质', '30', '0', '1', '2017-09-27 09:34:31', '1', '2017-09-27 10:00:23', '', '0');
INSERT INTO `sys_dict` VALUES ('96', '1', '男', 'sex', '性别', '10', '0', '1', '2013-10-28 08:00:00', '1', '2013-10-28 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('97', '2', '女', 'sex', '性别', '20', '0', '1', '2013-10-28 08:00:00', '1', '2013-10-28 08:00:00', null, '0');
INSERT INTO `sys_dict` VALUES ('9933c602ced34c7f896a818ea948c092', '1', '已审核', 'app_apply_status', 'app授权状态', '20', '0', '1', '2017-09-28 12:28:06', '1', '2017-09-28 12:28:06', '', '0');
INSERT INTO `sys_dict` VALUES ('99d6d57bfa1e4d73882d508edab15230', 'yyMMdd', 'yyMMdd', 'srmSeqsubsection_date_type', '日期格式', '10', '0', '1', '2017-09-14 13:57:59', '1', '2017-09-14 13:57:59', '', '0');
INSERT INTO `sys_dict` VALUES ('9b1f5e9bbe3a41dd8137f0e695488be6', '5', '5:工单作业', 'srm_gentime', '条码产生时机', '50', '0', '1', '2017-09-06 13:18:50', '1', '2017-09-06 13:18:50', '条码产生时机', '0');
INSERT INTO `sys_dict` VALUES ('a236c55f67be476fbb4830167d508dfe', '2', '智能物流APP', 'menu_type', '菜单类型', '10', '0', '1', '2017-09-04 17:36:33', '1', '2017-09-04 17:36:33', '菜单类型', '0');
INSERT INTO `sys_dict` VALUES ('a38a4f411c8d4f6dbe89121ea11306dd', '1', '1:成功', 'ica_log_is_success', 'ica日志请求是否成功', '10', '0', '1', '2017-09-18 15:21:19', '1', '2017-09-18 15:34:00', '', '0');
INSERT INTO `sys_dict` VALUES ('a53c3c1a68974442b5dfbbcfd6057f49', '1', '1:采购作业', 'srm_gentime', '条码产生时机', '10', '0', '1', '2017-09-06 13:16:23', '1', '2017-09-06 13:16:23', '条码产生时机', '0');
INSERT INTO `sys_dict` VALUES ('a63244280e70460a82be29f77127f1d8', '2', '业务员', 'app_apply_type', 'app授权申请类型', '20', '0', '1', '2017-10-11 11:42:26', '1', '2017-10-11 11:42:26', '', '0');
INSERT INTO `sys_dict` VALUES ('a66b11765dca426ba36a6cbad7da554e', '2', '来料加工', 'order_type', '单据性质', '20', '0', '1', '2017-08-14 10:24:42', '1', '2017-10-19 17:00:47', '', '0');
INSERT INTO `sys_dict` VALUES ('a7cf6112acaf40ad9ca9aca6baf531dc', 'ORACLE11G', 'ORACLE11G', 'iccn_ica_db_version', '数据库版本', '30', '0', '1', '2017-09-08 17:26:47', '1', '2017-09-08 17:26:47', '数据库连接管理-数据库版本', '0');
INSERT INTO `sys_dict` VALUES ('aabcdce73ce54a40b6e43d57f7ade996', 'Y', 'Y年', 'srmSeqbase_cycle', '循环级别', '20', '0', '1', '2017-09-12 16:18:16', '1', '2017-09-12 17:57:27', '', '0');
INSERT INTO `sys_dict` VALUES ('afb7211b6c1142f089fbe86baf51f0fc', 'SQLSERVER2005', 'SQLSERVER2005', 'iccn_ica_db_version', '数据库版本', '50', '0', '1', '2017-09-08 17:27:08', '1', '2017-09-08 17:27:08', '数据库连接管理-数据库版本', '0');
INSERT INTO `sys_dict` VALUES ('aff1a9497f914d0d9114551e10d07159', '01', '01传入值', 'srmSeqsubsection_type', '分段类型是传进来的', '10', '0', '1', '2017-09-12 18:03:34', '1', '2017-09-12 18:03:34', '', '0');
INSERT INTO `sys_dict` VALUES ('b39666b1e2384915b811b80e4e4ab450', '3', 'javaBean执行方式', 'dc_job_execute_type', '定时任务执行方式', '30', '0', '1', '2017-10-11 14:48:02', '1', '2017-10-11 14:48:02', '', '0');
INSERT INTO `sys_dict` VALUES ('b5c76645375042a2a1bec535f5e50e51', '0', '0：关闭', 'iccn_ica_status', '是否启用', '20', '0', '1', '2017-09-08 16:39:49', '1', '2017-09-08 16:51:10', '系统管理-数据库连接状态', '0');
INSERT INTO `sys_dict` VALUES ('b60c9170b9524835ae509482849726e7', '4', '预先采购', 'order_type', '单据性质', '40', '0', '1', '2017-08-14 10:25:38', '1', '2017-08-14 10:25:38', '', '0');
INSERT INTO `sys_dict` VALUES ('bd251e5944ba43c7bf2480e0b90247e7', 'Y', 'Y:边线发料', 'srm_islineslid', '是否边线仓', '10', '0', '1', '2017-09-06 13:21:45', '1', '2017-09-06 13:35:45', '', '0');
INSERT INTO `sys_dict` VALUES ('c302ebe7066445a6994ed64c1ad9870b', '2', '2:采购收货', 'srm_gentime', '条码产生时机', '20', '0', '1', '2017-09-06 13:17:14', '1', '2017-09-06 13:17:14', '条码产生时机', '0');
INSERT INTO `sys_dict` VALUES ('c337e44a1cfe4072b1584f63e0a1a705', '04', '04定义的日期字符', 'srmSeqsubsection_type', '分段类型是自己定义的日期字符', '40', '0', '1', '2017-09-12 18:06:04', '1', '2017-09-12 18:06:24', '', '0');
INSERT INTO `sys_dict` VALUES ('cc55e74625c8406a8f9c826cb2f68992', '4', '供应商送货', 'logistics_mode', '物流方式', '40', '0', '1', '2017-08-31 12:02:10', '1', '2017-08-31 12:02:10', '', '0');
INSERT INTO `sys_dict` VALUES ('cfee5b2b9f674f6ebf58433e170f673c', '1', '1：启用', 'iccn_ica_status', '是否启用', '10', '0', '1', '2017-09-08 16:39:21', '1', '2017-09-08 16:50:53', '系统管理-数据库连接管理状态', '0');
INSERT INTO `sys_dict` VALUES ('d397f0e1aa6043c19d6d9016bf9fc111', '2', '审核不通过', 'app_apply_status', 'app授权状态', '30', '0', '1', '2017-09-28 12:28:14', 'b2ab6cd9c1254289a9562d884c7ad050', '2017-10-20 10:45:05', '', '0');
INSERT INTO `sys_dict` VALUES ('d6109821338c4c0db3c3390e8fa2be70', 'A', 'A', 'srmSeqbase_level', 'A级别', '10', '0', '1', '2017-09-12 17:41:57', '1', '2017-09-12 17:41:57', '', '0');
INSERT INTO `sys_dict` VALUES ('d789779d25ae4f47be19c8e3f90a1020', 'aas', 'aas', 'srmSeqbase_cycle', '循环级别', '10', '0', '1', '2017-09-12 16:17:23', '1', '2017-09-12 16:31:42', '', '0');
INSERT INTO `sys_dict` VALUES ('d8343bfbe489493f9bb762a287f989bd', '3', '自提', 'logistics_mode', '物流方式', '30', '0', '1', '2017-08-31 12:01:51', '1', '2017-08-31 12:01:51', '', '0');
INSERT INTO `sys_dict` VALUES ('d8f2335ea8824d60bc612a19fb010e00', 'Y', 'Y:有', 'srm_effectcode', '资料有效码', '10', '0', '1', '2017-09-06 13:33:03', '1', '2017-09-06 13:33:32', '', '0');
INSERT INTO `sys_dict` VALUES ('e08a4be690c7427f9c889e14d644998a', 'N', 'N:否', 'srm_yes_no', 'srm通用是否', '20', '0', '1', '2017-09-06 14:51:54', '1', '2017-09-06 14:51:54', '', '0');
INSERT INTO `sys_dict` VALUES ('e124d96dbcfc4795ab6c9b7aea09bd45', 'N', 'N', 'select', '选择', '10', '0', '1', '2017-09-06 22:36:45', '1', '2017-09-06 22:36:45', '', '0');
INSERT INTO `sys_dict` VALUES ('e48225bbd39c487dbbc38dd772a26e0d', '1', '一般采购', 'srm_purchase_type', 'srm采购性质', '10', '0', '1', '2017-09-27 09:32:49', '1', '2017-09-27 10:00:08', '', '0');
INSERT INTO `sys_dict` VALUES ('e9782931eedc4f3fb26fc18fd501b981', 'Y', 'Y:管控', 'srm_fifocontr', '先进先出管控', '10', '0', '1', '2017-09-06 13:11:55', '1', '2017-09-06 13:11:55', '先进先出管控', '0');
INSERT INTO `sys_dict` VALUES ('ebf9d158ac4f4b39ba61c82c1757503f', 'MYSQL5.X', 'MYSQL5.X', 'iccn_ica_db_version', '数据库版本', '10', '0', '1', '2017-09-08 17:26:30', '1', '2017-09-08 17:26:30', '数据库连接管理-数据库版本', '0');
INSERT INTO `sys_dict` VALUES ('ef6aa65e502d4d13a06cf6bb74bc74b8', '2', '2:批次级', 'srm_item_type', '物料条码类型', '20', '0', '1', '2017-09-06 09:17:38', '1', '2017-09-06 12:54:44', '物料条码类型', '0');
INSERT INTO `sys_dict` VALUES ('f2b53a084ab046faaa143192ca03ac39', '0', '管理菜单', 'menu_type', '菜单类型', '10', '0', '1', '2017-09-04 17:34:01', '1', '2017-09-04 17:34:01', '菜单类型', '0');
INSERT INTO `sys_dict` VALUES ('f6b6bf99fe0a4a84923ec283f59907c6', '1', '1允许重复', 'srmseqsubsection_allowrep', '是否允许重复', '20', '0', '1', '2017-09-22 11:02:07', '1', '2017-09-22 11:02:07', '', '0');
INSERT INTO `sys_dict` VALUES ('f7ddf3b1e35d4938b8b5840d68dfc92c', '0', '0不允许重复', 'srmseqsubsection_allowrep', '是否允许重复', '10', '0', '1', '2017-09-22 11:01:50', '1', '2017-09-22 11:01:50', '', '0');
INSERT INTO `sys_dict` VALUES ('f8a3589d3b894b77b69d506f296f9a17', 'N', 'N:无', 'srm_effectcode', '资料有效码', '20', '0', '1', '2017-09-06 13:33:18', '1', '2017-09-06 13:33:40', '', '0');
INSERT INTO `sys_dict` VALUES ('f926155f748c49a7aa366d0c677cad5b', '1', '开立', 'srm_delivery_status', '发货状态', '10', '0', '1', '2017-09-29 09:16:59', '1', '2017-09-29 09:16:59', '', '0');
INSERT INTO `sys_dict` VALUES ('fefa90b8a61d48bebd1229b16b1fdda9', '2', 'SpringBean执行', 'srm_execute_type', '定时任务执行方式', '20', '0', '1', '2017-09-26 10:20:45', '1', '2017-09-26 10:20:45', '', '0');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_log` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `exception` text COMMENT '异常信息',
  `request_params` text COMMENT '请求参数',
  `result` mediumtext COMMENT '返回结果',
  `modules` varchar(255) DEFAULT NULL COMMENT '模组',
  `log_level` varchar(1) DEFAULT NULL COMMENT '日志级别',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`) USING BTREE,
  KEY `sys_log_request_uri` (`request_uri`) USING BTREE,
  KEY `sys_log_type` (`type`) USING BTREE,
  KEY `sys_log_create_date` (`create_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

-- ----------------------------
-- Table structure for sys_mdict
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_mdict` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_mdict_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_mdict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='多级字典表';

-- ----------------------------
-- Records of sys_mdict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` char(1) NOT NULL COMMENT '是否在菜单中显示',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `code` varchar(255) DEFAULT NULL COMMENT 'app模组',
  `menu_type` int(4) DEFAULT '0' COMMENT '菜单类型',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_menu_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_menu_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
 INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002', '1', '0,1,', '系统设置', '900', '', '', 'gear (alias)', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-25 14:03:51', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020001', '10002', '0,1,10002,', '机构用户', '970', '', '', 'user', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-17 09:33:48', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200010001', '100020001', '0,1,10002,100020001,', '区域管理', '50', '/sys/area/', NULL, 'th', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100010001', '1000200010001', '0,1,10002,100020001,1000200010001,', '查看', '30', NULL, NULL, NULL, '0', 'sys:area:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100010002', '1000200010001', '0,1,10002,100020001,1000200010001,', '修改', '40', NULL, NULL, NULL, '0', 'sys:area:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200010002', '100020001', '0,1,10002,100020001,', '机构管理', '40', '/sys/office/', NULL, 'th-large', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100020001', '1000200010002', '0,1,10002,100020001,1000200010002,', '查看', '30', NULL, NULL, NULL, '0', 'sys:office:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100020002', '1000200010002', '0,1,10002,100020001,1000200010002,', '修改', '40', NULL, NULL, NULL, '0', 'sys:office:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200010003', '100020001', '0,1,10002,100020001,', '用户管理', '30', '/sys/user/index', NULL, 'user', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100030001', '1000200010003', '0,1,10002,100020001,1000200010003,', '查看', '30', NULL, NULL, NULL, '0', 'sys:user:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000100030002', '1000200010003', '0,1,10002,100020001,1000200010003,', '修改', '40', NULL, NULL, NULL, '0', 'sys:user:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020002', '10002', '0,1,10002,', '关于帮助', '990', '', '', 'hand-paper-o', '0', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-25 14:00:17', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200020001', '100020002', '0,1,10002,100020002,', '官方首页', '30', 'http://jeesite.com', '_blank', 'link', '0', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-25 14:00:45', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020003', '10002', '0,1,10002,', '系统设置', '980', '', '', 'gear (alias)', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-25 14:04:11', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200030001', '100020003', '0,1,10002,100020003,', '字典管理', '60', '/sys/dict/', NULL, 'th-list', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300010001', '1000200030001', '0,1,10002,100020003,1000200030001,', '查看', '30', NULL, NULL, NULL, '0', 'sys:dict:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300010002', '1000200030001', '0,1,10002,100020003,1000200030001,', '修改', '40', NULL, NULL, NULL, '0', 'sys:dict:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200030002', '100020003', '0,1,10002,100020003,', '打印模板管理', '90', '/sys/printTemplate', '', '', '1', '', '', '0', '1', '2017-10-13 08:57:21', '1', '2017-10-13 08:57:21', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300020001', '1000200030002', '0,1,10002,100020003,1000200030002,', '修改', '60', '', '', '', '1', 'sys:printTemplate:edit', '', '0', '1', '2017-10-13 11:36:56', '1', '2017-10-13 11:36:56', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300020002', '1000200030002', '0,1,10002,100020003,1000200030002,', '查看', '30', '', '', '', '0', 'sys:printTemplate:view', '', '0', '1', '2017-10-13 11:36:34', '1', '2017-10-13 11:36:34', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200030003', '100020003', '0,1,10002,100020003,', '菜单管理', '30', '/sys/menu/', NULL, 'list-alt', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300030001', '1000200030003', '0,1,10002,100020003,1000200030003,', '查看', '30', NULL, NULL, NULL, '0', 'sys:menu:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300030002', '1000200030003', '0,1,10002,100020003,1000200030003,', '修改', '40', NULL, NULL, NULL, '0', 'sys:menu:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200030004', '100020003', '0,1,10002,100020003,', '角色管理', '50', '/sys/role/', NULL, 'lock', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300040001', '1000200030004', '0,1,10002,100020003,1000200030004,', '查看', '30', NULL, NULL, NULL, '0', 'sys:role:view', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000300040002', '1000200030004', '0,1,10002,100020003,1000200030004,', '修改', '40', NULL, NULL, NULL, '0', 'sys:role:edit', NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020004', '10002', '0,1,10002,', '日志查询', '985', '', '', 'search', '1', '', '', '0', '1', '2013-06-03 08:00:00', '1', '2017-09-25 13:53:32', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200040001', '100020004', '0,1,10002,100020004,', '日志开关', '70', '/sys/log/changeState', '', '', '1', 'sys:log:edit', '', '0', '1', '2017-10-12 16:45:31', '1', '2017-10-12 16:45:31', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200040002', '100020004', '0,1,10002,100020004,', '日志查询', '30', '/sys/log', NULL, 'pencil', '1', 'sys:log:view', NULL, '0', '1', '2013-06-03 08:00:00', '1', '2013-06-03 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200040003', '100020004', '0,1,10002,100020004,', '连接池监视', '40', '/../druid', '', '', '0', '', '', '0', '1', '2013-10-18 08:00:00', '1', '2017-10-13 15:02:53', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020005', '10002', '0,1,10002,', '应用管理', '1020', '', '', '', '1', '', '', '0', '1', '2017-10-13 17:07:28', '1', '2017-10-13 17:07:28', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200050001', '100020005', '0,1,10002,100020005,', '应用列表', '30', '/sys/sysAppVersion/list', '', '', '1', '', '', '0', '1', '2017-10-13 17:08:48', '1', '2017-10-15 22:10:00', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000500010001', '1000200050001', '0,1,10002,100020005,1000200050001,', '修改权限', '60', '', '', '', '0', 'sys:sysAppVersion:edit', '', '0', '1', '2017-10-15 22:11:22', '1', '2017-10-16 09:04:19', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000500010002', '1000200050001', '0,1,10002,100020005,1000200050001,', '查看权限', '30', '', '', '', '0', 'sys:sysAppVersion:view', '', '0', '1', '2017-10-15 22:10:43', '1', '2017-10-16 09:04:07', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100020006', '10002', '0,1,10002,', 'APP管理', '990', '/sys/appApply', '', '', '1', '', '', '0', '1', '2017-09-28 11:10:02', '1', '2017-10-13 17:06:57', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200060001', '100020006', '0,1,10002,100020006,', 'app清单', '30', '/sys/sysAppList', '', '', '1', '', '', '0', '1', '2017-09-28 11:10:27', '1', '2017-10-13 17:07:07', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000600010001', '1000200060001', '0,1,10002,100020006,1000200060001,', '修改权限', '60', '', '', '', '0', 'sys:sysAppList:edit', '', '0', '1', '2017-09-28 11:11:41', '1', '2017-09-28 11:14:04', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000600010002', '1000200060001', '0,1,10002,100020006,1000200060001,', '查看权限', '30', '', '', '', '0', 'sys:sysAppList:view', '', '0', '1', '2017-09-28 11:11:14', '1', '2017-09-28 11:11:14', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000200060002', '100020006', '0,1,10002,100020006,', 'APP授权管理', '60', '/sys/appApply', '', '', '1', '', '', '0', '1', '2017-09-28 12:37:04', '1', '2017-10-13 17:07:17', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000600020001', '1000200060002', '0,1,10002,100020006,1000200060002,', '查看权限', '30', '', '', '', '0', 'sys:appApply:view', '', '0', '1', '2017-09-28 12:37:19', '1', '2017-09-28 12:37:19', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10002000600020002', '1000200060002', '0,1,10002,100020006,1000200060002,', '修改权限', '60', '', '', '', '0', 'sys:appApply:edit', '', '0', '1', '2017-09-28 12:37:43', '1', '2017-09-28 12:37:43', '', '0');
 


INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10003', '1', '0,1,', '我的面板', '100', '', '', 'info', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-25 14:01:41', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100030001', '10003', '0,1,10003,', '个人信息', '30', '', '', 'users', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-09-22 17:16:01', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000300010001', '100030001', '0,1,10003,100030001,', '个人信息', '30', '/sys/user/info', NULL, 'user', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000300010002', '100030001', '0,1,10003,100030001,', '修改密码', '40', '/sys/user/modifyPwd', NULL, 'lock', '1', NULL, NULL, '0', '1', '2013-05-27 08:00:00', '1', '2013-05-27 08:00:00', NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100030003', '10003', '0,1,10003,', '文件管理', '90', '', '', 'file', '0', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-17 09:35:36', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000300030002', '100030003', '0,1,10003,100030003,', '文件管理', '90', '/../static/ckfinder/ckfinder.html', '', 'folder-open', '1', '', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-13 09:58:25', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10003000300020001', '1000300030002', '0,1,10003,100030003,1000300030002,', '查看', '30', '', '', '', '1', 'cms:ckfinder:view', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-13 09:59:13', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10003000300020002', '1000300030002', '0,1,10003,100030003,1000300030002,', '上传', '40', '', '', '', '1', 'cms:ckfinder:upload', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-13 09:59:22', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10003000300020003', '1000300030002', '0,1,10003,100030003,1000300030002,', '修改', '50', '', '', '', '1', 'cms:ckfinder:edit', '', '0', '1', '2013-05-27 08:00:00', '1', '2017-10-13 09:59:28', '', '0');

-- ----------------------------
-- Table structure for sys_office
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_office` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `area_id` varchar(64) NOT NULL COMMENT '归属区域',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `type` char(1) NOT NULL COMMENT '机构类型',
  `grade` char(1) NOT NULL COMMENT '机构等级',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `USEABLE` varchar(64) DEFAULT NULL COMMENT '是否启用',
  `PRIMARY_PERSON` varchar(64) DEFAULT NULL COMMENT '主负责人',
  `DEPUTY_PERSON` varchar(64) DEFAULT NULL COMMENT '副负责人',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_office_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_office_del_flag` (`del_flag`) USING BTREE,
  KEY `sys_office_type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

-- ----------------------------
-- Records of sys_office
-- ----------------------------
INSERT INTO `sys_office` VALUES ('0887715d748e44919b497d1dff0b50ae', '0', '0,', '智互联（供应商）', '30', '1', 'P1001', '1', '1', '', '', '', '', '', '', '1', '', '', '1', '2017-08-11 16:21:07', '7d70bff90566484b8a8f89d9875e1903', '2017-10-20 14:57:04', '南京图尔克光电传感器有限公司', '0');
INSERT INTO `sys_office` VALUES ('1', '0', '0,', '我的公司', '10', '1', '100000', '1', '1', '', '', '', '', '', '', '1', '', '', '1', '2013-05-27 08:00:00', '7d70bff90566484b8a8f89d9875e1903', '2017-10-20 14:55:34', '', '0');
INSERT INTO `sys_office` VALUES ('2', '1', '0,1,', '综合部门', '10', '1', '100001', '2', '1', '', '', '', '', '', '', '1', 'ben', 'ben', '1', '2013-05-27 08:00:00', '7d70bff90566484b8a8f89d9875e1903', '2017-10-20 14:49:31', '', '0');

-- ----------------------------
-- Table structure for sys_print_template
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_print_template` (
  `id` varchar(64) NOT NULL,
  `template_code` varchar(64) NOT NULL COMMENT '模板标识',
  `template_content` longtext NOT NULL COMMENT '模板内容',
  `template_params` longtext NOT NULL COMMENT '测试参数',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL COMMENT '删除标识',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '启用标识',
  `name` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='打印模板';

-- ----------------------------
-- Records of sys_print_template
-- ----------------------------
INSERT INTO `sys_print_template` VALUES ('2', '2', '2', '2', '1', '2000-01-23 00:00:00', '1', '2000-01-23 00:00:00', '1', null, '1', null, null);
INSERT INTO `sys_print_template` VALUES ('2323', 'code', 'neirong', '2', '1', '2017-10-12 15:58:09', '1', '2017-10-12 15:58:12', '1', null, '0', '名称', '描述');
INSERT INTO `sys_print_template` VALUES ('3', '2', '2', '2', '1', '2000-01-23 00:00:00', '1', '2000-01-23 00:00:00', '1', null, '0', '1', null);
INSERT INTO `sys_print_template` VALUES ('4', 'delivery_print_label', '&lt;!DOCTYPE html&gt;\r\n&lt;html lang=&quot;zh&quot;&gt;\r\n&lt;head&gt;\r\n    &lt;meta charset=&quot;utf-8&quot; /&gt;\r\n    &lt;title&gt;打印&lt;/title&gt;\r\n\r\n	&lt;style type=&quot;text/css&quot;&gt;	\r\n\r\n		@page{\r\n		\r\n			size:100mm 80mm;\r\n			margin-top:0.12in;\r\n			margin-left:0.24in;\r\n			margin-right:0.24in;\r\n			margin-bottom:0.04in;\r\n		}\r\n\r\n		body{margin: 0 0 0 0;font-family: SimSun;}\r\n\r\n		div#main{margin: 0 0 0 0; width:320px;}\r\n		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}\r\n		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}\r\n		h1, h2 {\r\n			font-weight: bold;\r\n			padding: 0;\r\n			line-height: 1.2;\r\n			clear: left;\r\n		}\r\n		\r\n		.showDiv1{\r\n			padding-top:15px;\r\n			height:40px;\r\n		}\r\n		\r\n		.showDiv2{\r\n			padding-top:3px;\r\n			table-layout:fixed;\r\n			width:310px;\r\n			word-break:break-strict; \r\n			height:40px;\r\n		}\r\n		.showDiv3{\r\n			padding-top:3px;\r\n			width:310px;\r\n			height:40px;\r\n		}\r\n		.qrDiv{\r\n			position:absolute;\r\n    		top: 0px;\r\n    		right: -10px;\r\n    		z-index: -1;\r\n		}\r\n		span{\r\n			word-break: keep-all;\r\n		}\r\n	&lt;/style&gt;\r\n&lt;/head&gt;\r\n&lt;body&gt;\r\n    &lt;div id=&quot;main&quot; &gt;\r\n		#foreach ($detail in $list)	\r\n		&lt;div style=&quot;position:relative;&quot;&gt;\r\n		&lt;div class=&quot;showDiv1&quot;&gt;数量：${detail.oneNumber}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;料号：${detail.itemNo}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv3&quot;&gt;品名：${detail.itemName}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;规格：$PrintUtils.pdfPrintManulWrap2($detail.itemSpec,24)&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;供应商：${detail.supplierName}(${detail.supplierNo})&lt;/div&gt;\r\n		&lt;div class=&quot;qrDiv&quot;&gt;\r\n			&lt;img class=&quot;qrcode&quot; src=&quot;&quot; value=&quot;${detail.qrCodeValue}&quot; width=&quot;90px&quot; /&gt;\r\n		&lt;/div&gt;\r\n		&lt;/div&gt;\r\n		&lt;br/&gt;\r\n		&lt;div style=&quot;page-break-after: always;&quot;&gt;&lt;/div&gt;\r\n		#end\r\n	&lt;/div&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;', '{\r\n	&quot;list&quot;: [\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		},\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		}\r\n	]\r\n}', '1', '2000-01-23 00:00:00', '1', '2017-10-18 15:42:17', '0', null, '1', '发货单物料标签打印100*80mm', '发货单物料标签打印100*80mm');
INSERT INTO `sys_print_template` VALUES ('4ecc735411034a239252837e60253e0b', '12', '1', '1', '1', '2017-10-13 11:06:04', '1', '2017-10-13 11:08:13', '1', null, '0', '1', '23');
INSERT INTO `sys_print_template` VALUES ('7072259334094bde93836d8941978c04', '12', '2233', '12', '1', '2017-10-13 11:32:48', '1', '2017-10-13 12:39:48', '1', null, '0', '12', '');
INSERT INTO `sys_print_template` VALUES ('8254968da9ef4a0e8109773f5eecf774', 'delivery_print', '&lt;!DOCTYPE html&gt;\r\n&lt;html lang=&quot;zh&quot;&gt;\r\n&lt;head&gt;\r\n    &lt;meta charset=&quot;utf-8&quot; /&gt;\r\n    &lt;title&gt;打印模板&lt;/title&gt;\r\n	&lt;style type=&quot;text/css&quot;&gt;		\r\n		body{margin: 0 auto;font-family: SimSun;}\r\n		div#main{margin: 5px 5px 0 5px; width:700px;}\r\n		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}\r\n		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}\r\n		h1, h2 {\r\n			font-weight: bold;\r\n			padding: 0;\r\n			line-height: 1.2;\r\n			clear: left;\r\n		}\r\n		\r\n		table.detail{\r\n			width:98%;\r\n			border-collapse: collapse;\r\n			empty-cells: show;\r\n			border-spacing: 0;\r\n			margin:10px 0 0 0;\r\n			border-width:0;\r\n		}\r\n		table.detail td{\r\n			padding: .3em .5em;\r\n			margin: 0;\r\n			vertical-align: top;\r\n			border-width:0;\r\n		}\r\n		table.detail td span.tit{\r\n			font-weight: bold;\r\n		}\r\n		table.detail td span.val{\r\n			border-width:0 0 1px 0;\r\n			border-bottom: 1px solid #CCC;\r\n			font-size: 14px;\r\n		}\r\n		table.detail .col1{width:220px;}\r\n		table.detail .col2{width:220px;}\r\n		table.detail .col3{width:220px;}\r\n		\r\n		table.list{\r\n			width:98%;\r\n			border-collapse: collapse;\r\n			empty-cells: show;\r\n			border-spacing: 0;\r\n			margin:10px 0 0 0;\r\n			border: 1px solid #CCC;\r\n			table-layout:fixed; word-break:break-strict; \r\n		}\r\n		table.list th{font-weight: bold;background-color: #EEE;}\r\n		table.list th,td{\r\n			padding: .3em .5em;\r\n			margin: 0;\r\n			vertical-align: top;\r\n			border: 1px solid #CCC;\r\n		}\r\n		table.list td{\r\n			font-size: 14px;\r\n		}\r\n		table.list .col1{width:15%;}\r\n		table.list .col2{width:25%;}\r\n		table.list .col3{width:10%;}\r\n		table.list .col4{width:10%;text-align: right;}\r\n		table.list .col5{width:10%;text-align: right;}\r\n		table.list .col6{width:10%;text-align: right;}\r\n		table.list .col7{width:20%;}\r\n		table.list .col8{width:8%;}\r\n		table.list .col9{width:8%;text-align:center}\r\n		\r\n		.qrcodeDiv{\r\n			position: absolute;\r\n    		right: 20px;\r\n    		top: 0px;\r\n		}\r\n		.titleDiv{\r\n			text-align: center;\r\n    		font-size: 30px;\r\n    		position: inherit;\r\n    		font-weight: bold;\r\n		}\r\n		.headDiv{\r\n			position: inherit;\r\n    		height: 80px\r\n		}\r\n	&lt;/style&gt;\r\n&lt;/head&gt;\r\n&lt;body&gt;\r\n    &lt;div id=&quot;main&quot;&gt;\r\n		&lt;div class=&quot;headDiv&quot;&gt;\r\n		&lt;div class=&quot;titleDiv&quot;&gt;送货单&lt;/div&gt;\r\n		&lt;div class=&quot;qrcodeDiv&quot;&gt;\r\n			&lt;img class=&quot;qrcode&quot; src=&quot;&quot; value=&quot;${DeliveryBodyHead.deliveryNo}&quot; width=&quot;120px&quot; /&gt;\r\n		&lt;/div&gt;\r\n		&lt;/div&gt;\r\n		&lt;table class=&quot;detail&quot;&gt;\r\n			&lt;tr&gt;\r\n				&lt;td class=&quot;col2&quot;&gt;&lt;span class=&quot;tit&quot;&gt;送货单号：&lt;/span&gt;&lt;span class=&quot;val&quot;&gt;${DeliveryBodyHead.deliveryNo}&lt;/span&gt;&lt;/td&gt;\r\n				&lt;td class=&quot;col2&quot;&gt;&lt;span class=&quot;tit&quot;&gt;送货日期：&lt;/span&gt;&lt;span class=&quot;val&quot;&gt;${DeliveryBodyHead.deliveryDate}&lt;/span&gt;&lt;/td&gt;\r\n			&lt;/tr&gt;\r\n			&lt;tr&gt;\r\n				&lt;td class=&quot;col2&quot; colspan=&quot;2&quot;&gt;&lt;span class=&quot;tit&quot;&gt;供应商：&lt;/span&gt;&lt;span class=&quot;val&quot;&gt;${DeliveryBodyHead.supplierNo}(${DeliveryBodyHead.supplierName})&lt;/span&gt;&lt;/td&gt;\r\n			&lt;/tr&gt;\r\n			&lt;tr&gt;\r\n				&lt;td class=&quot;col2&quot; colspan=&quot;2&quot;&gt;&lt;span class=&quot;tit&quot;&gt;送货地址：&lt;/span&gt;&lt;span class=&quot;val&quot;&gt;${DeliveryBodyHead.receiptAddress}&lt;/span&gt;&lt;/td&gt;\r\n			&lt;/tr&gt;\r\n		&lt;/table&gt;\r\n		\r\n		&lt;table class=&quot;list&quot;&gt;\r\n			&lt;thead&gt;\r\n				&lt;tr &gt;\r\n					&lt;th class=&quot;col9&quot;&gt;项次&lt;/th&gt;\r\n					&lt;th class=&quot;col2&quot;&gt;采购单号&lt;/th&gt;\r\n					&lt;th class=&quot;col1&quot;&gt;料件编号&lt;/th&gt;\r\n					&lt;th class=&quot;col1&quot;&gt;料件名称&lt;/th&gt;\r\n					&lt;th class=&quot;col1&quot;&gt;规格&lt;/th&gt;\r\n					&lt;th class=&quot;col8&quot;&gt;单位&lt;/th&gt;\r\n					&lt;th class=&quot;col1&quot;&gt;送货数量&lt;/th&gt;\r\n					\r\n				&lt;/tr&gt;\r\n			&lt;/thead&gt;\r\n			&lt;tbody&gt;\r\n				#foreach ($element in $DeliveryBodyList)\r\n				&lt;tr &gt;\r\n					&lt;td class=&quot;col9&quot;&gt;${element.deliverySeq}&lt;/td&gt;\r\n					&lt;td class=&quot;col2&quot;&gt;${element.purchaseNo}&lt;/td&gt;\r\n					&lt;td class=&quot;col1&quot;&gt;$PrintUtils.pdfPrintManulWrap2($element.itemNo,12)&lt;/td&gt;\r\n					&lt;td class=&quot;col1&quot;&gt;$PrintUtils.pdfPrintManulWrap2($element.itemName,12)&lt;/td&gt;\r\n					&lt;td class=&quot;col1&quot;&gt;$PrintUtils.pdfPrintManulWrap2($element.itemSpec,12)&lt;/td&gt;\r\n					&lt;td class=&quot;col8&quot;&gt;$!{element.unitNo}&lt;/td&gt;\r\n					&lt;td class=&quot;col1&quot;&gt;$!{element.deliveryQty}&lt;/td&gt;\r\n				&lt;/tr&gt;\r\n				#end\r\n			&lt;/tbody&gt;\r\n		&lt;/table&gt;\r\n		&lt;br/&gt;\r\n	&lt;/div&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;', '{}', '1', '2017-10-13 10:44:24', '1', '2017-10-18 15:27:35', '0', null, '1', '发货单打印', '发货单打印');
INSERT INTO `sys_print_template` VALUES ('a2fa74b2cb51463999c2f68b25dcf4e0', '12', '12', '{}', '1', '2017-10-13 11:31:00', '1', '2017-10-13 12:40:23', '1', null, '1', '12', 'misdf');
INSERT INTO `sys_print_template` VALUES ('b92a856df77949b3944b88e84a3d23bd', 'delivery_print_label', '&lt;!DOCTYPE html&gt;\r\n&lt;html lang=&quot;zh&quot;&gt;\r\n&lt;head&gt;\r\n    &lt;meta charset=&quot;utf-8&quot; /&gt;\r\n    &lt;title&gt;打印&lt;/title&gt;\r\n\r\n	&lt;style type=&quot;text/css&quot;&gt;	\r\n\r\n		@page{\r\n			size:60mm 65mm;\r\n			margin-top:0.14in;\r\n			margin-left:0.14in;\r\n			margin-right:0.04in;\r\n			margin-bottom:0.04in;\r\n		}\r\n\r\n		body{margin: 0 0 0 0;font-family: SimSun;font-size:13px}\r\n\r\n		div#main{margin: 0 0 0 0; width:190px;}\r\n		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}\r\n		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}\r\n		h1, h2 {\r\n			font-weight: bold;\r\n			padding: 0;\r\n			line-height: 1.2;\r\n			clear: left;\r\n		}\r\n		\r\n		.showDiv1{\r\n			padding-top:5px;\r\n			height:37px;\r\n		}\r\n		\r\n		.showDiv2{\r\n			padding-top:1px;\r\n			table-layout:fixed;\r\n			width:180px;\r\n			word-break:break-strict; \r\n			height:37px;\r\n		}\r\n		.showDiv3{\r\n			width:180px;\r\n			height:37px;\r\n		}\r\n		.qrDiv{\r\n			position:absolute;\r\n    		top: 0px;\r\n    		right: -10px;\r\n    		z-index: -1;\r\n		}\r\n		span{\r\n			word-break: keep-all;\r\n		}\r\n	&lt;/style&gt;\r\n&lt;/head&gt;\r\n&lt;body&gt;\r\n    &lt;div id=&quot;main&quot;&gt;\r\n		#foreach ($detail in $list)	\r\n		&lt;div style=&quot;position:relative;&quot;&gt;\r\n		&lt;div class=&quot;showDiv1&quot;&gt;数量：${detail.oneNumber}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;料号：${detail.itemNo}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv3&quot;&gt;品名：${detail.itemName}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;规格：$PrintUtils.pdfPrintManulWrap2($detail.itemSpec,24)&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;供应商：${detail.supplierName}(${detail.supplierNo})&lt;/div&gt;\r\n		&lt;div class=&quot;qrDiv&quot;&gt;\r\n			&lt;img class=&quot;qrcode&quot; src=&quot;&quot; value=&quot;${detail.qrCodeValue}&quot; width=&quot;60px&quot; height=&quot;60px&quot; /&gt;\r\n		&lt;/div&gt;\r\n		&lt;/div&gt;\r\n		&lt;br/&gt;\r\n		&lt;div style=&quot;page-break-after: always;&quot;&gt;&lt;/div&gt;\r\n		#end\r\n	&lt;/div&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;', '{\r\n	&quot;list&quot;: [\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		},\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商水电费科雷嘉的科技时代风口浪尖打开房间打开房间&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		}\r\n	]\r\n}', '1', '2017-10-18 15:50:51', '1', '2017-10-18 15:50:51', '0', null, '0', '发货单物料标签打印60*65mm', '发货单物料标签打印60*65mm');
INSERT INTO `sys_print_template` VALUES ('dc43d0118a094e768058c64ea412be3c', 'barcode_print_label', '&lt;!DOCTYPE html&gt;\r\n&lt;html lang=&quot;zh&quot;&gt;\r\n&lt;head&gt;\r\n    &lt;meta charset=&quot;utf-8&quot; /&gt;\r\n    &lt;title&gt;打印&lt;/title&gt;\r\n\r\n	&lt;style type=&quot;text/css&quot;&gt;	\r\n\r\n		@page{\r\n			size:100mm 75mm;\r\n			margin-top:0.08in;\r\n			margin-left:0.34in;\r\n			margin-right:0.24in;\r\n			margin-bottom:0.04in;\r\n		}\r\n\r\n		body{margin: 0 0 0 0;font-family: SimSun;}\r\n\r\n		div#main{margin: 0 0 0 0; width:320px;}\r\n		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}\r\n		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}\r\n		h1, h2 {\r\n			font-weight: bold;\r\n			padding: 0;\r\n			line-height: 1.2;\r\n			clear: left;\r\n		}\r\n		\r\n		.showDiv1{\r\n			padding-top:15px;\r\n			height:25px;\r\n		}\r\n		\r\n		.showDiv2{\r\n			padding-top:3px;\r\n			table-layout:fixed;\r\n			width:310px;\r\n			word-break:break-strict; \r\n			height:25px;\r\n		}\r\n		.showDiv3{\r\n			padding-top:3px;\r\n			width:310px;\r\n			height:35px;\r\n		}\r\n		.qrDiv{\r\n		position:absolute;\r\n    		top: 0px;\r\n    		right: 0px;\r\n    		z-index: -1;\r\n		}\r\n		span{\r\n			word-break: keep-all;\r\n		}\r\n	&lt;/style&gt;\r\n&lt;/head&gt;\r\n&lt;body&gt;\r\n    &lt;div id=&quot;main&quot;&gt;\r\n		#foreach ($barcode in $list)	\r\n		&lt;div style=&quot;position:relative;&quot;&gt;\r\n		&lt;div class=&quot;showDiv1&quot;&gt;供应商：${barcode.vBarcode}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;日期：${barcode.date}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;料号：${barcode.itemNo}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv3&quot;&gt;规格：$PrintUtils.pdfPrintManulWrap2($barcode.itemSpec,24)&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;批号：${barcode.lotNo}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;数量：${barcode.curNumber}/${barcode.number}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot; style=&quot;float:left&quot;&gt;箱数：${barcode.curBox}/${barcode.box}&lt;/div&gt;\r\n		&lt;div class=&quot;qrDiv&quot;&gt;\r\n			&lt;img class=&quot;qrcode&quot; src=&quot;&quot; value=&quot;${barcode.barcode}&quot; width=&quot;100px&quot; /&gt;\r\n		&lt;/div&gt;\r\n		&lt;/div&gt;\r\n		&lt;br/&gt;\r\n		&lt;div style=&quot;page-break-after: always;&quot;&gt;&lt;/div&gt;\r\n		#end\r\n	&lt;/div&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;', '{\r\n	&quot;list&quot;: [\r\n		{\r\n			&quot;vBarcode&quot;: 12,\r\n			&quot;date&quot;:&quot;2017-09-15&quot;,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;curNumber&quot;:&quot;5&quot;,\r\n			&quot;number&quot;:&quot;5&quot;,\r\n			&quot;box&quot;:&quot;5&quot;,\r\n			&quot;curBox&quot;:&quot;5&quot;,\r\n			&quot;barcode&quot;: &quot;QWE&quot;\r\n		}]\r\n}', '1', '2017-10-18 16:04:19', '1', '2017-10-18 16:27:57', '0', null, '1', '条码打印', '');
INSERT INTO `sys_print_template` VALUES ('e50780bc01154a71be2d8cf5080f1540', 'delivery_print_label', '&lt;!DOCTYPE html&gt;\r\n&lt;html lang=&quot;zh&quot;&gt;\r\n&lt;head&gt;\r\n    &lt;meta charset=&quot;utf-8&quot; /&gt;\r\n    &lt;title&gt;打印&lt;/title&gt;\r\n\r\n	&lt;style type=&quot;text/css&quot;&gt;	\r\n\r\n		@page{\r\n			size:100mm 75mm;\r\n			margin-top:0.04in;\r\n			margin-left:0.24in;\r\n			margin-right:0.24in;\r\n			margin-bottom:0.04in;\r\n		}\r\n\r\n		body{margin: 0 0 0 0;font-family: SimSun;}\r\n\r\n		div#main{margin: 0 0 0 0; width:320px;}\r\n		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}\r\n		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}\r\n		h1, h2 {\r\n			font-weight: bold;\r\n			padding: 0;\r\n			line-height: 1.2;\r\n			clear: left;\r\n		}\r\n		\r\n		.showDiv1{\r\n			padding-top:15px;\r\n			height:35px;\r\n		}\r\n		\r\n		.showDiv2{\r\n			padding-top:3px;\r\n			table-layout:fixed;\r\n			width:310px;\r\n			word-break:break-strict; \r\n			height:35px;\r\n		}\r\n		.showDiv3{\r\n			padding-top:3px;\r\n			width:310px;\r\n			height:35px;\r\n		}\r\n		.qrDiv{\r\n			position:absolute;\r\n    		top: 0px;\r\n    		right: -10px;\r\n    		z-index: -1;\r\n		}\r\n		span{\r\n			word-break: keep-all;\r\n		}\r\n	&lt;/style&gt;\r\n&lt;/head&gt;\r\n&lt;body&gt;\r\n    &lt;div id=&quot;main&quot;&gt;\r\n		#foreach ($detail in $list)	\r\n		&lt;div style=&quot;position:relative;&quot;&gt;\r\n		&lt;div class=&quot;showDiv1&quot;&gt;数量：${detail.oneNumber}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;料号：${detail.itemNo}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv3&quot;&gt;品名：${detail.itemName}&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;规格：$PrintUtils.pdfPrintManulWrap2($detail.itemSpec,24)&lt;/div&gt;\r\n		&lt;div class=&quot;showDiv2&quot;&gt;供应商：${detail.supplierName}(${detail.supplierNo})&lt;/div&gt;\r\n		&lt;div class=&quot;qrDiv&quot;&gt;\r\n			&lt;img class=&quot;qrcode&quot; src=&quot;&quot; value=&quot;${detail.qrCodeValue}&quot; width=&quot;90px&quot; /&gt;\r\n		&lt;/div&gt;\r\n		&lt;/div&gt;\r\n		&lt;br/&gt;\r\n		&lt;div style=&quot;page-break-after: always;&quot;&gt;&lt;/div&gt;\r\n		#end\r\n	&lt;/div&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;', '{\r\n	&quot;list&quot;: [\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格各顾各的符&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		},\r\n		{\r\n			&quot;oneNumber&quot;: 12,\r\n			&quot;itemNo&quot;: &quot;编号&quot;,\r\n			&quot;itemName&quot;: &quot;名称&quot;,\r\n			&quot;itemSpec&quot;: &quot;规格各顾各的符&quot;,\r\n			&quot;supplierName&quot;: &quot;供应商&quot;,\r\n			&quot;supplierNo&quot;: &quot;供应商编号&quot;,\r\n			&quot;lotNo&quot;: &quot;批号&quot;,\r\n			&quot;qrCodeValue&quot;: &quot;QWE&quot;\r\n		}\r\n	]\r\n}', '1', '2017-10-13 16:26:16', '1', '2017-10-18 15:38:04', '0', null, '0', '发货单物料标签打印100*75mm', '发货单物料标签打印100*75mm');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `office_id` varchar(64) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `role_type` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围',
  `is_sys` varchar(64) DEFAULT NULL COMMENT '是否系统数据',
  `useable` varchar(64) DEFAULT NULL COMMENT '是否可用',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_role_del_flag` (`del_flag`) USING BTREE,
  KEY `sys_role_enname` (`enname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '2', '系统管理员', 'dept', 'assignment', '1', '1', '1', '1', '2013-05-27 08:00:00', '1', '2017-10-20 15:54:29', '', '0');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_role_menu` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '10001');
INSERT INTO `sys_role_menu` VALUES ('1', '100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '100010003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000100030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10001000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10002');
INSERT INTO `sys_role_menu` VALUES ('1', '100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200010003');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000100030002');
INSERT INTO `sys_role_menu` VALUES ('1', '100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200020001');
INSERT INTO `sys_role_menu` VALUES ('1', '100020003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200030002');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200030003');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300030002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200030004');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300040001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000300040002');
INSERT INTO `sys_role_menu` VALUES ('1', '100020004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200040002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200040003');
INSERT INTO `sys_role_menu` VALUES ('1', '100020005');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200050001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000500010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000500010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100020006');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200060001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000600010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000600010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000200060002');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000600020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10002000600020002');
INSERT INTO `sys_role_menu` VALUES ('1', '10003');
INSERT INTO `sys_role_menu` VALUES ('1', '100030001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100030003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000300030002');
INSERT INTO `sys_role_menu` VALUES ('1', '10003000300020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10003000300020002');
INSERT INTO `sys_role_menu` VALUES ('1', '10003000300020003');
INSERT INTO `sys_role_menu` VALUES ('1', '10005');
INSERT INTO `sys_role_menu` VALUES ('1', '100050001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '100050003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500030002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500030003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500030004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500030005');
INSERT INTO `sys_role_menu` VALUES ('1', '10005000300050001');
INSERT INTO `sys_role_menu` VALUES ('1', '100050006');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060005');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060006');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060007');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060008');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500060009');
INSERT INTO `sys_role_menu` VALUES ('1', '100050007');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500070001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500070003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500070004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500070005');
INSERT INTO `sys_role_menu` VALUES ('1', '100050010');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500100003');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001000030001');
INSERT INTO `sys_role_menu` VALUES ('1', '100050012');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500120001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001200010001');
INSERT INTO `sys_role_menu` VALUES ('1', '100050014');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500140001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001400010001');
INSERT INTO `sys_role_menu` VALUES ('1', '100050015');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500150001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001500010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500150002');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001500020001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500150003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500150004');
INSERT INTO `sys_role_menu` VALUES ('1', '100050017');
INSERT INTO `sys_role_menu` VALUES ('1', '1000500170001');
INSERT INTO `sys_role_menu` VALUES ('1', '10005001700010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006');
INSERT INTO `sys_role_menu` VALUES ('1', '100060001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010005');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600010006');
INSERT INTO `sys_role_menu` VALUES ('1', '100060002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600020002');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000200020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000200020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600020003');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000200030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000200030002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040002');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040004');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400040001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400040002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040005');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400050001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000400050002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600040006');
INSERT INTO `sys_role_menu` VALUES ('1', '100060005');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600050001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600050002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060006');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600060001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000600010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000600010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060007');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600070001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000700010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000700010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060008');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600080001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000800010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000800010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060009');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090003');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900030001');
INSERT INTO `sys_role_menu` VALUES ('1', '100060009000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '100060009000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090004');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900040001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090006');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900060001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900060002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600090008');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900080001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006000900080002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060010');
INSERT INTO `sys_role_menu` VALUES ('1', '100060012');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600120001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001200010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001200010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600120002');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001200020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001200020002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060013');
INSERT INTO `sys_role_menu` VALUES ('1', '100060014');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600140001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001400010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10006001400010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100060015');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600150001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600150002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000600150003');
INSERT INTO `sys_role_menu` VALUES ('1', '10009');
INSERT INTO `sys_role_menu` VALUES ('1', '100090001');
INSERT INTO `sys_role_menu` VALUES ('1', '1000900010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000900010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '100090002');
INSERT INTO `sys_role_menu` VALUES ('1', '1000900020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000200010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000200010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100090003');
INSERT INTO `sys_role_menu` VALUES ('1', '1000900030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000300010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000300010002');
INSERT INTO `sys_role_menu` VALUES ('1', '100090004');
INSERT INTO `sys_role_menu` VALUES ('1', '1000900040001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000400010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10009000400010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10011');
INSERT INTO `sys_role_menu` VALUES ('1', '100110001');
INSERT INTO `sys_role_menu` VALUES ('1', '1001100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100010001');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100010003');
INSERT INTO `sys_role_menu` VALUES ('1', '1001100010002');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1001100010003');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100030001');
INSERT INTO `sys_role_menu` VALUES ('1', '10011000100030002');
INSERT INTO `sys_role_menu` VALUES ('1', '100110002');
INSERT INTO `sys_role_menu` VALUES ('1', '1001100020001');
INSERT INTO `sys_role_menu` VALUES ('1', '1001100020002');
INSERT INTO `sys_role_menu` VALUES ('1', '10020');
INSERT INTO `sys_role_menu` VALUES ('1', '100200001');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000010001');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000010002');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000010003');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000010004');
INSERT INTO `sys_role_menu` VALUES ('1', '100200002');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020001');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020002');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020003');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020004');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020005');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020006');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020007');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020008');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020009');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020010');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020011');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020012');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020013');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020014');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000020015');
INSERT INTO `sys_role_menu` VALUES ('1', '100200003');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030001');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030002');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030003');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030004');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030005');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030006');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030007');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000030008');
INSERT INTO `sys_role_menu` VALUES ('1', '100200004');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040001');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040002');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040003');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040004');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040005');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040006');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040007');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040008');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040009');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040010');
INSERT INTO `sys_role_menu` VALUES ('1', '1002000040011');

-- ----------------------------
-- Table structure for sys_role_office
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_role_office` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `office_id` varchar(64) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`role_id`,`office_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-机构';

-- ----------------------------
-- Records of sys_role_office
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `company_id` varchar(64) NOT NULL COMMENT '归属公司',
  `office_id` varchar(64) NOT NULL COMMENT '归属部门',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `no` varchar(100) DEFAULT NULL COMMENT '工号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `department_name` varchar(255) DEFAULT NULL,
  `department_no` varchar(50) DEFAULT NULL COMMENT 'erp 部门编码',
  `language` varchar(255) DEFAULT NULL COMMENT '语言',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `ware` varchar(255) DEFAULT NULL COMMENT '默认仓库',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型',
  `photo` varchar(1000) DEFAULT NULL COMMENT '用户头像',
  `login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `employee_name` varchar(255) DEFAULT NULL COMMENT 'erp 员工姓名',
  `employee_no` varchar(255) DEFAULT NULL COMMENT 'erp员工编号',
  `site` varchar(255) DEFAULT NULL,
  `ent` varchar(255) DEFAULT NULL,
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `login_flag` varchar(64) DEFAULT NULL COMMENT '是否可登录',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_user_office_id` (`office_id`) USING BTREE,
  KEY `sys_user_login_name` (`login_name`) USING BTREE,
  KEY `sys_user_company_id` (`company_id`) USING BTREE,
  KEY `sys_user_update_date` (`update_date`) USING BTREE,
  KEY `sys_user_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', '2', 'admin', '07d51742949ca3be9b2ce02e544988cd3ee3b1cb58f9ea83a5b1a926', '0001', '系统管理员admin', 'sn48@126.com', '智物流', 'qqq', null, '867511', 'Z1W1', '12345678', '', '/userfiles/1/images/photo/2017/09/timg.jpg', '172.31.74.154', '智物流', 'tiptop', 'DSCNJ', '99', '2017-10-20 16:17:06', '1', '1', '2013-05-27 08:00:00', '1', '2017-10-13 10:04:13', '最高管理员11122', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `sys_user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
