-- pest 农作物病虫害测报与防治调度 -- schema (jia-012)
-- 列名与基线实体契约（@TableName/@TableField）逐列对齐，改列必须同步实体。
-- 库：jia_012

CREATE TABLE IF NOT EXISTS t_pest_effect (
  id bigint NOT NULL COMMENT '主键',
  effect_no varchar(64) DEFAULT NULL COMMENT '评估单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  spray_no varchar(64) DEFAULT NULL COMMENT '关联防治作业单号',
  before_count int DEFAULT NULL COMMENT '防治前虫量(头/百株)',
  after_count int DEFAULT NULL COMMENT '防治后虫量(头/百株)',
  reduce_rate decimal(6,2) DEFAULT NULL COMMENT '虫口减退率(%)',
  assess_date datetime DEFAULT NULL COMMENT '评估日期',
  assess_by varchar(64) DEFAULT NULL COMMENT '评估人',
  effect_status int DEFAULT NULL COMMENT '评估状态 0待评估 1已评估 2已归档',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='防治效果评估单';

CREATE TABLE IF NOT EXISTS t_pest_export (
  id bigint NOT NULL COMMENT '主键',
  export_no varchar(64) DEFAULT NULL COMMENT '导出单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  begin_date datetime DEFAULT NULL COMMENT '起始日期',
  end_date datetime DEFAULT NULL COMMENT '结束日期',
  row_count int DEFAULT NULL COMMENT '导出行数',
  expire_date datetime DEFAULT NULL COMMENT '文件到期时间',
  valid_days int DEFAULT NULL COMMENT '文件有效剩余天数',
  export_status int DEFAULT NULL COMMENT '导出状态 0已生成 1已下载',
  export_by varchar(64) DEFAULT NULL COMMENT '导出人',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测报台账导出记录';

CREATE TABLE IF NOT EXISTS t_pest_forecast (
  id bigint NOT NULL COMMENT '主键',
  forecast_no varchar(64) DEFAULT NULL COMMENT '预报单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  pest_name varchar(64) DEFAULT NULL COMMENT '病虫名称',
  forecast_date datetime DEFAULT NULL COMMENT '预报日期',
  valid_date datetime DEFAULT NULL COMMENT '预报有效期至',
  remain_days int DEFAULT NULL COMMENT '剩余天数',
  forecast_by varchar(64) DEFAULT NULL COMMENT '预报登记人',
  forecast_status int DEFAULT NULL COMMENT '发布状态 0待发布 1已发布 2已关闭',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='防治适期预报单';

CREATE TABLE IF NOT EXISTS t_pest_import (
  id bigint NOT NULL COMMENT '主键',
  batch_no varchar(64) DEFAULT NULL COMMENT '批次号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  total_rows int DEFAULT NULL COMMENT '总行数',
  success_rows int DEFAULT NULL COMMENT '成功行数',
  fail_rows int DEFAULT NULL COMMENT '失败行数',
  fail_rate decimal(6,2) DEFAULT NULL COMMENT '失败率(%)',
  batch_status int DEFAULT NULL COMMENT '批次状态 0导入中 1已完成 2已作废',
  import_by varchar(64) DEFAULT NULL COMMENT '导入人',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='监测数据导入批次';

CREATE TABLE IF NOT EXISTS t_pest_notice (
  id bigint NOT NULL COMMENT '主键',
  notice_no varchar(64) DEFAULT NULL COMMENT '通知单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  notice_title varchar(128) DEFAULT NULL COMMENT '通知标题',
  issue_date datetime DEFAULT NULL COMMENT '下发日期',
  issue_by varchar(64) DEFAULT NULL COMMENT '发布人',
  notice_status int DEFAULT NULL COMMENT '通知状态 0待发布 1已发布 2已终止',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='防治通知单';

CREATE TABLE IF NOT EXISTS t_pest_patrol (
  id bigint NOT NULL COMMENT '主键',
  patrol_no varchar(64) DEFAULT NULL COMMENT '巡查单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  patrol_date datetime DEFAULT NULL COMMENT '巡查日期',
  patrol_type varchar(32) DEFAULT NULL COMMENT '巡查类型 日常巡查/灯诱监测/专项排查',
  patrol_by varchar(64) DEFAULT NULL COMMENT '巡查人',
  patrol_status int DEFAULT NULL COMMENT '巡查进度 0待巡查 1已巡查 2已归档',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测报点巡查记录';

CREATE TABLE IF NOT EXISTS t_pest_rule (
  id bigint NOT NULL COMMENT '主键',
  rule_code varchar(32) DEFAULT NULL COMMENT '规则编号',
  rule_name varchar(64) DEFAULT NULL COMMENT '规则名称',
  dev1_max decimal(6,2) DEFAULT NULL COMMENT '一级上限(病株率%)',
  dev2_max decimal(6,2) DEFAULT NULL COMMENT '二级上限(病株率%)',
  dev3_max decimal(6,2) DEFAULT NULL COMMENT '三级上限(病株率%)',
  status int DEFAULT NULL COMMENT '规则状态 0启用 1停用',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='发生程度分级规则';

CREATE TABLE IF NOT EXISTS t_pest_scan_log (
  id bigint NOT NULL COMMENT '主键',
  scan_no varchar(64) DEFAULT NULL COMMENT '登记流水号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  station_name varchar(64) DEFAULT NULL COMMENT '测报点名称(冗余，以档案为准)',
  survey_no varchar(64) DEFAULT NULL COMMENT '关联调查单号',
  scan_type int DEFAULT NULL COMMENT '登记类型 1现场查询 2现场登记',
  scan_time datetime DEFAULT NULL COMMENT '登记时间',
  valid_date datetime DEFAULT NULL COMMENT '登记有效期至',
  valid_days int DEFAULT NULL COMMENT '有效期剩余天数',
  scan_status int DEFAULT NULL COMMENT '登记状态 0暂存 1已提交',
  scan_by varchar(64) DEFAULT NULL COMMENT '登记人',
  device varchar(64) DEFAULT NULL COMMENT '登记设备',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='移动端现场测报登记';

CREATE TABLE IF NOT EXISTS t_pest_spray (
  id bigint NOT NULL COMMENT '主键',
  spray_no varchar(64) DEFAULT NULL COMMENT '配制单号',
  forecast_id bigint DEFAULT NULL COMMENT '关联预报单ID',
  forecast_no varchar(64) DEFAULT NULL COMMENT '预报单号(冗余，以预报单为准)',
  pesticide_name varchar(64) DEFAULT NULL COMMENT '农药名称',
  dose_volume int DEFAULT NULL COMMENT '配制药液量(升)',
  spray_volume int DEFAULT NULL COMMENT '已喷施量(升)',
  remain_volume int DEFAULT NULL COMMENT '剩余药液量(升)',
  prepare_by varchar(128) DEFAULT NULL COMMENT '配制人',
  spray_status int DEFAULT NULL COMMENT '配制状态 0可继续喷施 1已用完',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='防治作业药液配制台账';

CREATE TABLE IF NOT EXISTS t_pest_stat (
  id bigint NOT NULL COMMENT '主键',
  stat_no varchar(64) DEFAULT NULL COMMENT '统计单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  stat_month varchar(16) DEFAULT NULL COMMENT '统计月份',
  should_count int DEFAULT NULL COMMENT '应防治地块数',
  done_count int DEFAULT NULL COMMENT '已防治地块数',
  cover_rate decimal(6,2) DEFAULT NULL COMMENT '防治覆盖率(%)',
  stat_status int DEFAULT NULL COMMENT '统计状态 0待确认 1已归档',
  stat_by varchar(64) DEFAULT NULL COMMENT '统计人',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='防治覆盖率统计';

CREATE TABLE IF NOT EXISTS t_pest_station (
  id bigint NOT NULL COMMENT '主键',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号',
  station_name varchar(64) DEFAULT NULL COMMENT '测报点名称',
  crop_name varchar(64) DEFAULT NULL COMMENT '监测作物',
  region varchar(64) DEFAULT NULL COMMENT '所属区域',
  area decimal(12,2) DEFAULT NULL COMMENT '监测面积(亩)',
  status int DEFAULT NULL COMMENT '档案状态 0监测中 1已撤点',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测报点档案';

CREATE TABLE IF NOT EXISTS t_pest_survey (
  id bigint NOT NULL COMMENT '主键',
  survey_no varchar(64) DEFAULT NULL COMMENT '调查单号',
  station_id bigint DEFAULT NULL COMMENT '测报点ID',
  station_code varchar(32) DEFAULT NULL COMMENT '测报点编号(冗余，以档案为准)',
  rule_id bigint DEFAULT NULL COMMENT '分级规则ID',
  rule_code varchar(32) DEFAULT NULL COMMENT '规则编号(冗余，以档案为准)',
  survey_rate decimal(8,2) DEFAULT NULL COMMENT '病株率(%)',
  occur_level int DEFAULT NULL COMMENT '发生程度 1轻 2中 3重 4大发生',
  survey_date datetime DEFAULT NULL COMMENT '调查日期',
  survey_by varchar(64) DEFAULT NULL COMMENT '调查人',
  survey_status int DEFAULT NULL COMMENT '复核状态 0待复核 1已复核',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='病虫发生调查';

-- 初始档案数据（验收测试依赖 id=1 启用 / id=2 停用）
INSERT INTO t_pest_station (id, station_code, station_name, crop_name, region, area, status, del_flag, create_by, create_time)
VALUES (1, 'PS-0001', '城郊水稻测报点', '水稻', '华中区', 500.00, 0, 0, 'sys', NOW()),
       (2, 'PS-0002', '老棉田撤点测报点', '棉花', '华中区', 120.00, 1, 0, 'sys', NOW());

INSERT INTO t_pest_rule (id, rule_code, rule_name, dev1_max, dev2_max, dev3_max, status, del_flag, create_by, create_time)
VALUES (1, 'PG-01', '病虫发生程度分级规则', 5.00, 10.00, 15.00, 0, 0, 'sys', NOW()),
       (2, 'PG-02', '停用版发生程度分级规则', 5.00, 10.00, 15.00, 1, 0, 'sys', NOW());

INSERT INTO t_pest_forecast (id, forecast_no, station_id, station_code, pest_name, forecast_date, valid_date, remain_days, forecast_by, forecast_status, del_flag, create_by, create_time)
VALUES (1, 'FC-0001', 1, 'PS-0001', '稻飞虱', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 7, '张测报', 1, 0, 'sys', NOW()),
       (2, 'FC-0002', 1, 'PS-0001', '稻纵卷叶螟', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, '李测报', 2, 0, 'sys', NOW());

-- ----------------------------
-- 菜单与权限（jia-012：分级规则维护 + 病虫发生调查台账）
-- 目录：病虫害测报；管理员角色 488243256161730560 全量授权
-- ----------------------------
INSERT INTO `t_sys_permission` VALUES (7100100000000001000, '病虫害测报', '病虫害测报目录', '', 0, 0, '', 0, 'layui-icon layui-icon-template-1', 10, 0, 'admin', NOW(), NULL, NULL, NULL);

-- 发生程度分级规则
INSERT INTO `t_sys_permission` VALUES (7100100000000001010, '分级规则', '分级规则展示', '/PestRuleController/view', 0, 7100100000000001000, 'pest:pestRule:view', 1, 'layui-icon layui-icon-set', 1, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001011, '分级规则集合', '分级规则集合', '/PestRuleController/list', 0, 7100100000000001010, 'pest:pestRule:list', 2, '', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001012, '分级规则添加', '分级规则添加', '/PestRuleController/add', 0, 7100100000000001010, 'pest:pestRule:add', 2, 'layui-icon layui-icon-add-1', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001013, '分级规则修改', '分级规则修改（含启用停用）', '/PestRuleController/edit', 0, 7100100000000001010, 'pest:pestRule:edit', 2, 'layui-icon layui-icon-edit', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001014, '分级规则删除', '分级规则删除', '/PestRuleController/remove', 0, 7100100000000001010, 'pest:pestRule:remove', 2, 'layui-icon layui-icon-delete', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);

-- 病虫发生调查台账
INSERT INTO `t_sys_permission` VALUES (7100100000000001020, '发生调查', '病虫发生调查展示', '/PestSurveyController/view', 0, 7100100000000001000, 'pest:pestSurvey:view', 1, 'layui-icon layui-icon-note', 2, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001021, '调查记录集合', '调查记录集合', '/PestSurveyController/list', 0, 7100100000000001020, 'pest:pestSurvey:list', 2, '', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001022, '调查记录添加', '调查记录添加', '/PestSurveyController/add', 0, 7100100000000001020, 'pest:pestSurvey:add', 2, 'layui-icon layui-icon-add-1', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001023, '调查记录修改', '调查记录修改', '/PestSurveyController/edit', 0, 7100100000000001020, 'pest:pestSurvey:edit', 2, 'layui-icon layui-icon-edit', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001024, '调查记录复核', '调查记录复核', '/PestSurveyController/review', 0, 7100100000000001020, 'pest:pestSurvey:review', 2, 'layui-icon layui-icon-ok-circle', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission` VALUES (7100100000000001025, '调查记录删除', '调查记录删除', '/PestSurveyController/remove', 0, 7100100000000001020, 'pest:pestSurvey:remove', 2, 'layui-icon layui-icon-delete', NULL, 0, 'admin', NOW(), NULL, NULL, NULL);

-- 管理员角色授权
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001100, 488243256161730560, 7100100000000001000, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001110, 488243256161730560, 7100100000000001010, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001111, 488243256161730560, 7100100000000001011, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001112, 488243256161730560, 7100100000000001012, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001113, 488243256161730560, 7100100000000001013, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001114, 488243256161730560, 7100100000000001014, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001120, 488243256161730560, 7100100000000001020, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001121, 488243256161730560, 7100100000000001021, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001122, 488243256161730560, 7100100000000001022, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001123, 488243256161730560, 7100100000000001023, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001124, 488243256161730560, 7100100000000001024, 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `t_sys_permission_role` VALUES (7100100000000001125, 488243256161730560, 7100100000000001025, 'admin', NOW(), NULL, NULL, NULL);
