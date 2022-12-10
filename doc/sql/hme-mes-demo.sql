/*
 Navicat Premium Data Transfer

 Source Server         : 150.158.15.149-3307-腾讯云mysql8-www.dreamm.net
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : 150.158.15.150:3307
 Source Schema         : hme-mes

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 18/08/2022 11:47:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sp_bom
-- ----------------------------
DROP TABLE IF EXISTS `sp_bom`;
CREATE TABLE `sp_bom`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `bom_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'bom编号',
  `materiel_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物料ID',
  `materiel_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物料描述',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `version_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本号',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'BOM状态 creat创建 pass审核通过 ',
  `factory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工厂',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'BOM主信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_bom
-- ----------------------------
INSERT INTO `sp_bom` VALUES ('1268447170115383298', 'bom0036', 'EC101000036', 'pcb', '', '1', NULL, NULL, '0', '2020-06-04 15:39:07', 'admin', '2022-08-17 11:20:59', 'admin');
INSERT INTO `sp_bom` VALUES ('1547629583436812289', 'bom0036', 'BP66767676776', '三极管', '', '1', NULL, NULL, '0', '2022-07-15 01:10:21', 'admin', '2022-08-03 12:07:04', 'admin');
INSERT INTO `sp_bom` VALUES ('1554691706948067329', 'bom0036', 'zemine220802', 'zemine', '', '1', NULL, NULL, '0', '2022-08-03 12:52:42', 'admin', '2022-08-17 14:07:52', 'admin');

-- ----------------------------
-- Table structure for sp_bom_item
-- ----------------------------
DROP TABLE IF EXISTS `sp_bom_item`;
CREATE TABLE `sp_bom_item`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `bom_head_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'bom编号',
  `materiel_item_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料ID',
  `materiel_item_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料描述',
  `line_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行号',
  `item_num` decimal(10, 0) NULL DEFAULT 0 COMMENT '用量',
  `item_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子项基本单位',
  `oper_typer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属工序类型',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'BOM子项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_bom_item
-- ----------------------------

-- ----------------------------
-- Table structure for sp_config
-- ----------------------------
DROP TABLE IF EXISTS `sp_config`;
CREATE TABLE `sp_config`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `line_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产线id',
  `server_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '127.0.0.1' COMMENT '服务器ip',
  `server_port` int NULL DEFAULT 9600 COMMENT '服务器port',
  `interv` int NULL DEFAULT 10 COMMENT '更新数据的时间间隔 秒,interval是保留字不能使用',
  `type` int NULL DEFAULT 4 COMMENT '通讯协议类型\r\n0 tcp   client\r\n1 tcp  server\r\n2 udp  server\r\n3 udp  client\r\n4 fins  client\r\n5 simens client\r\n',
  `use_comm` tinyint NULL DEFAULT 0 COMMENT '是否使用串口0 不适用， 1 使用',
  `bandrate` int NULL DEFAULT 115200 COMMENT '波特率',
  `data_bits` int UNSIGNED NULL DEFAULT 8 COMMENT '数据位',
  `stop_bits` int NULL DEFAULT 1 COMMENT '停止位',
  `check_bit` int NULL DEFAULT 0 COMMENT '0 奇校验，1偶校验',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_config
-- ----------------------------
INSERT INTO `sp_config` VALUES ('1555232008796508162', '2022-08-05 00:39:40', 'admin', '2022-08-17 10:31:19', 'admin', '磁阻传感器', '127.0.0.1', 9600, 10, 4, 0, 115200, 8, 1, 0);
INSERT INTO `sp_config` VALUES ('1559718515662008322', '2022-08-17 09:47:27', 'admin', '2022-08-17 10:25:53', 'admin', '测速传感器', '127.0.0.1', 9600, 10, 4, 0, 115200, 8, 1, 0);

-- ----------------------------
-- Table structure for sp_daily_plan
-- ----------------------------
DROP TABLE IF EXISTS `sp_daily_plan`;
CREATE TABLE `sp_daily_plan`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT 'id',
  `order_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编码',
  `plan_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计划日期',
  `piece_time` int NULL DEFAULT 30 COMMENT '每件产品耗时 ，以秒为单位 (这个是设定的标准)',
  `real_piece_time` int NULL DEFAULT 30 COMMENT '实际每件耗时(平均每秒耗时,不是最近一件产品的耗时，也不是最近一段时间内的制造一件产品的耗时)',
  `plan_qty` int NULL DEFAULT 0 COMMENT '计划产量',
  `maked_qty` int NULL DEFAULT 0 COMMENT '当前产量',
  `bad_qty` int(10) UNSIGNED ZEROFILL NULL DEFAULT 0000000000 COMMENT '次品数量',
  `finish_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '当日完成率 ',
  `pass_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '一次通过率',
  `hour_qty` int NULL DEFAULT NULL COMMENT '每个小时计划生产产品数',
  `minute_qty` int NULL DEFAULT 0 COMMENT '每分钟计划生产产品数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后更新人',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `afternoon_start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下午开始时间 默认  下午一点',
  `afternoon_end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下午结束时间',
  `evening_start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '夜班开始时间',
  `evening_end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '夜班结束时间',
  `morning_start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '早班开始时间',
  `morning_end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '早班结束时间',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最近一件产品生产时间',
  `expect_finish_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '理论完成率 =当日实际消耗时间/当日排产有效时间,',
  `afternoon_start1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下午1开始时间 默认  下午一点',
  `afternoon_end1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下午1结束时间',
  `evening_start1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '夜班开始时间',
  `evening_end1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '夜班结束时间',
  `morning_start1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '早班开始时间',
  `morning_end1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '早班结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_daily_plan
-- ----------------------------
INSERT INTO `sp_daily_plan` VALUES ('1543692092598038530', 'HME2022070101', '2022-07-17 08:00:00', 30, 28, 1000, 103, 0000000002, 10.30, 99.00, NULL, 0, '2022-07-04 04:24:10', 'admin', '2022-07-17 16:50:09', 'admin', 0, '2022-07-16 13:30:00', '2022-07-16 15:30:00', '2022-07-16 18:30:00', '2022-07-16 20:30:00', '2022-07-16 08:00:00', '2022-07-16 10:00:00', '2022-07-16 10:31:55', 0.00, '2022-07-16 15:40:00', '2022-07-16 17:40:00', '', '', '2022-07-16 10:10:00', '2022-07-16 11:40:00');
INSERT INTO `sp_daily_plan` VALUES ('1543801556663676930', 'HME2022070401', '2022-07-17 11:38:00', 31, 0, 1000, 30, 0000000004, 3.00, NULL, NULL, 0, '2022-07-04 11:39:08', 'admin', '2022-07-04 11:41:40', 'admin', 0, '2022-07-04 16:38:00', '2022-07-04 11:43:00', '', '', '2022-07-04 11:38:00', '2022-07-04 11:44:00', NULL, 0.00, NULL, NULL, NULL, '2022-07-04 10:18:00', NULL, NULL);
INSERT INTO `sp_daily_plan` VALUES ('1548594146030325761', 'DD2022070202', '2022-07-19 08:00:00', 28, 590, 400, 3, 0000000000, 0.75, 100.00, NULL, 0, '2022-07-17 17:03:11', 'admin', '2022-07-19 09:13:05', 'admin', 0, '2022-07-17 13:00:00', '2022-07-17 15:00:00', '2022-07-17 18:30:00', '2022-07-17 20:00:00', '2022-07-17 08:00:00', '2022-07-17 10:00:00', '2022-07-17 19:28:36', 0.00, '2022-07-17 15:10:00', '2022-07-17 17:30:00', '2022-07-17 20:40:00', '2022-07-17 22:00:00', '2022-07-17 10:10:00', '2022-07-17 11:40:00');
INSERT INTO `sp_daily_plan` VALUES ('1548632599342252033', 'DD2022070202', '2022-08-03 08:00:00', 28, 5, 400, 321, 0000000001, 80.25, 99.69, NULL, 0, '2022-07-17 19:35:59', 'admin', '2022-08-03 22:57:19', 'lgl', 0, '2022-08-03 13:00:00', '2022-08-03 15:00:00', '2022-08-03 18:30:00', '2022-08-03 20:30:00', '2022-08-03 08:00:00', '2022-08-03 10:00:00', '2022-08-03 22:57:19', 124.50, '2022-08-03 15:10:00', '2022-08-03 17:40:00', NULL, NULL, '2022-08-03 10:10:00', '2022-08-03 11:40:00');

-- ----------------------------
-- Table structure for sp_device
-- ----------------------------
DROP TABLE IF EXISTS `sp_device`;
CREATE TABLE `sp_device`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `device` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备描述',
  `supplier` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '供应商',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '设备状态: 0正常运行，1故障，2维修中 3 未启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_device
-- ----------------------------
INSERT INTO `sp_device` VALUES ('1559898665796165633', '罗盘-01', '罗盘-01', '华夏智造', '2022-08-17 21:43:18', 'admin', '2022-08-17 22:11:01', 'admin', '0', '0');
INSERT INTO `sp_device` VALUES ('1559905357585858561', 'SMT-01', '贴片机-01', '富士康', '2022-08-17 22:09:54', 'admin', '2022-08-17 22:09:54', 'admin', '0', '0');

-- ----------------------------
-- Table structure for sp_device_activation
-- ----------------------------
DROP TABLE IF EXISTS `sp_device_activation`;
CREATE TABLE `sp_device_activation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `device` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `supplier` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '供应商',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `plan_time` int NULL DEFAULT 480 COMMENT '计划负载时长(已经扣除中间正常休息时间)',
  `work_time` int NULL DEFAULT 1 COMMENT '实际负载时长=plan_time-bad_time-wait_time',
  `bad_time` int NULL DEFAULT 0 COMMENT '故障时长(统计当日的,从维修信息表中统计)',
  `activation_rate` float(5, 2) NULL DEFAULT 100.00 COMMENT '设备稼动率=work_time/plan_time  %100',
  `wait_time` int NULL DEFAULT 0 COMMENT '等待时长(统计当日的,这个 不好统计)',
  `flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '设备状态: 0正常运行，1故障，2维修中 3 未启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_device_activation
-- ----------------------------
INSERT INTO `sp_device_activation` VALUES ('1559898665796165633', '罗盘-01', '华夏智造', '2022-08-17 21:43:18', 'admin', '2022-08-18 11:44:25', 'admin', '0', 480, 480, 0, 100.00, 0, '0');
INSERT INTO `sp_device_activation` VALUES ('1559905357585858561', 'SMT-01', '富士康', '2022-08-17 22:09:54', 'admin', '2022-08-18 11:44:03', 'admin', '0', 480, 380, 30, 80.00, NULL, '0');

-- ----------------------------
-- Table structure for sp_device_maintain
-- ----------------------------
DROP TABLE IF EXISTS `sp_device_maintain`;
CREATE TABLE `sp_device_maintain`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '主键id',
  `device` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `error_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '故障描述',
  `dealer` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理者',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '0正常运行，1故障，2维修中 3 禁用',
  `bad_time` int NULL DEFAULT 0 COMMENT '本次故障时长(分钟)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_device_maintain
-- ----------------------------
INSERT INTO `sp_device_maintain` VALUES ('1560077833016164354', '罗盘-01', '无法正常工作', 'admin', 'recovery  ', '2022-08-18 09:35:15', 'admin', '2022-08-18 09:39:27', 'admin', '0', '1', 10);
INSERT INTO `sp_device_maintain` VALUES ('1560079314595254273', 'SMT-01', '发热', 'admin', 'ok ', '2022-08-18 09:41:08', 'admin', '2022-08-18 09:43:03', 'admin', '0', '0', 5);

-- ----------------------------
-- Table structure for sp_factroy
-- ----------------------------
DROP TABLE IF EXISTS `sp_factroy`;
CREATE TABLE `sp_factroy`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `factory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `factory_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工厂表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_factroy
-- ----------------------------
INSERT INTO `sp_factroy` VALUES ('1336542027055136', 'dream-shop', 'www.dreammm.net', '2020-03-12 15:22:02', 'admin', '2020-03-13 10:15:54', 'admin');
INSERT INTO `sp_factroy` VALUES ('1336542027089009', 'hme-shop', 'www.dreammm.net', '2020-03-12 15:22:02', 'admin', '2020-03-13 10:15:54', 'admin');

-- ----------------------------
-- Table structure for sp_flow
-- ----------------------------
DROP TABLE IF EXISTS `sp_flow`;
CREATE TABLE `sp_flow`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `flow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程',
  `flow_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '线体描述',
  `process` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程绘制 A——>B——>C',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_flow
-- ----------------------------
INSERT INTO `sp_flow` VALUES ('1277176874674663425', 'AT', 'A01装配工序->测试工序', '装配工序->测试工序', '2020-06-28 17:47:50', 'admin', '2022-06-29 20:47:39', 'admin');
INSERT INTO `sp_flow` VALUES ('1277600512544583681', '测速传感器', '测速传感器', '装配工序->测试工序->包装工序', '2020-06-29 21:51:14', 'admin', '2022-08-17 01:45:44', 'admin');
INSERT INTO `sp_flow` VALUES ('1278528234456330242', 'dc001', '斗车', '装配工序->测试工序->包装工序', '2020-07-02 11:17:40', 'admin', '2020-07-02 11:17:40', 'admin');
INSERT INTO `sp_flow` VALUES ('1539862494408048641', 'caigou', '采购->入库', '采购->入库', '2022-06-23 14:46:43', 'admin', '2022-06-29 20:46:32', 'admin');
INSERT INTO `sp_flow` VALUES ('1559443376458342402', '磁阻传感器', '磁阻传感器生产工艺', '上料->贴片->绕丝->加固->测试->打码', '2022-08-16 15:34:09', 'admin', '2022-08-17 01:41:24', 'admin');

-- ----------------------------
-- Table structure for sp_flow_oper_relation
-- ----------------------------
DROP TABLE IF EXISTS `sp_flow_oper_relation`;
CREATE TABLE `sp_flow_oper_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程ID',
  `flow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程代码',
  `per_oper_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前道工序ID',
  `per_oper` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前道工序代码',
  `oper_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前工序ID',
  `oper` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前工序\r\n',
  `next_oper_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下道工序ID',
  `next_oper` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下道工序',
  `sort_num` int NOT NULL COMMENT '排序',
  `oper_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工序类型（首道工序firstOper;最后一道工序lastOper）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `flow_id_index`(`flow_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流程与工序关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_flow_oper_relation
-- ----------------------------
INSERT INTO `sp_flow_oper_relation` VALUES ('1275430361636253697', '1275430361590116354', '002', '', '', '1336864489340960', 'ASY-01', '1336864575324192', 'APK-01', 1, NULL, '2020-06-23 22:07:49', 'admin', '2020-06-23 22:07:49', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1275430361636253698', '1275430361590116354', '002', '1336864489340960', 'ASY-01', '1336864575324192', 'APK-01', '', '', 2, NULL, '2020-06-23 22:07:49', 'admin', '2020-06-23 22:07:49', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1278145622248239105', '1278145622063689729', '1212', '', '', '1336864489340960', 'ASY-01', '1336864575324192', 'APK-01', 1, NULL, '2020-07-01 09:57:18', 'admin', '2020-07-01 09:57:18', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1278145622248239106', '1278145622063689729', '1212', '1336864489340960', 'ASY-01', '1336864575324192', 'APK-01', '', '', 2, NULL, '2020-07-01 09:57:18', 'admin', '2020-07-01 09:57:18', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1278528234506661890', '1278528234456330242', 'dc001', '', '', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', 1, NULL, '2020-07-02 11:17:40', 'admin', '2020-07-02 11:17:40', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1278528234506661891', '1278528234456330242', 'dc001', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', '1336864575324192', 'APK-01', 2, NULL, '2020-07-02 11:17:40', 'admin', '2020-07-02 11:17:40', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1278528234506661892', '1278528234456330242', 'dc001', '1336864537575456', 'TST-02', '1336864575324192', 'APK-01', '', '', 3, NULL, '2020-07-02 11:17:40', 'admin', '2020-07-02 11:17:40', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1542127376155201537', '1539862494408048641', 'caigou', '', '', '1866580263612345', 'CaiGou', '1866580263623456', 'RuKu', 1, NULL, '2022-06-29 20:46:32', 'admin', '2022-06-29 20:46:32', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1542127376155201538', '1539862494408048641', 'caigou', '1866580263612345', 'CaiGou', '1866580263623456', 'RuKu', '', '', 2, NULL, '2022-06-29 20:46:32', 'admin', '2022-06-29 20:46:32', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1542127657261649922', '1277176874674663425', 'AT', '', '', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', 1, NULL, '2022-06-29 20:47:40', 'admin', '2022-06-29 20:47:40', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1542127657328758786', '1277176874674663425', 'AT', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', '', '', 2, NULL, '2022-06-29 20:47:40', 'admin', '2022-06-29 20:47:40', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887106', '1559443376458342402', '磁阻传感器', '', '', '1559594964397264897', '上料', '1559595062946631682', '贴片', 1, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887107', '1559443376458342402', '磁阻传感器', '1559594964397264897', '上料', '1559595062946631682', '贴片', '1559595229389197314', '绕丝', 2, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887108', '1559443376458342402', '磁阻传感器', '1559595062946631682', '贴片', '1559595229389197314', '绕丝', '1559595327984701441', '加固', 3, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887109', '1559443376458342402', '磁阻传感器', '1559595229389197314', '绕丝', '1559595327984701441', '加固', '1559595389871656962', '测试', 4, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887110', '1559443376458342402', '磁阻传感器', '1559595327984701441', '加固', '1559595389871656962', '测试', '1559595533094555650', '打码', 5, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559596197635887111', '1559443376458342402', '磁阻传感器', '1559595389871656962', '测试', '1559595533094555650', '打码', '', '', 6, NULL, '2022-08-17 01:41:24', 'admin', '2022-08-17 01:41:24', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559597285848059906', '1277600512544583681', '测速传感器', '', '', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', 1, NULL, '2022-08-17 01:45:44', 'admin', '2022-08-17 01:45:44', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559597285848059907', '1277600512544583681', '测速传感器', '1336864489340960', 'ASY-01', '1336864537575456', 'TST-02', '1336864575324192', 'APK-01', 2, NULL, '2022-08-17 01:45:44', 'admin', '2022-08-17 01:45:44', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1559597285848059908', '1277600512544583681', '测速传感器', '1336864537575456', 'TST-02', '1336864575324192', 'APK-01', '', '', 3, NULL, '2022-08-17 01:45:44', 'admin', '2022-08-17 01:45:44', 'admin');

-- ----------------------------
-- Table structure for sp_global_id
-- ----------------------------
DROP TABLE IF EXISTS `sp_global_id`;
CREATE TABLE `sp_global_id`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `order_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '全局订单编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_global_id
-- ----------------------------
INSERT INTO `sp_global_id` VALUES ('1336867983196192', 'DD2022070202', '2022-07-24 10:32:10', 'admin', '2022-08-03 14:29:33', 'admin');

-- ----------------------------
-- Table structure for sp_line
-- ----------------------------
DROP TABLE IF EXISTS `sp_line`;
CREATE TABLE `sp_line`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `line` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生产线',
  `line_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '线体描述',
  `process_section` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工序段代号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_line
-- ----------------------------
INSERT INTO `sp_line` VALUES ('1336867983196192', 'HME-GCZ-01', '磁阻变压器', '从vv', '2020-03-14 10:32:10', 'admin', '2020-06-14 02:20:09', 'admin');
INSERT INTO `sp_line` VALUES ('1336868041916448', 'HME-CE-01', '测速传感器产线', 'TST', '2020-03-14 10:32:38', 'admin', '2020-03-14 10:32:38', 'admin');
INSERT INTO `sp_line` VALUES ('1336868662673440', 'HME-YL01', '压力传感器', 'ASY', '2020-03-14 10:37:34', 'admin', '2020-06-16 11:47:04', 'admin');

-- ----------------------------
-- Table structure for sp_materile
-- ----------------------------
DROP TABLE IF EXISTS `sp_materile`;
CREATE TABLE `sp_materile`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `materiel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料编码',
  `materiel_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物料描述',
  `qrcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二维码/条形码',
  `batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '批次',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基本单位',
  `product_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品组',
  `mat_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物料类型',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '型号',
  `size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '尺寸',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程',
  `flow_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后更新人',
  `is_deleted` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础物料表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_materile
-- ----------------------------
INSERT INTO `sp_materile` VALUES ('1540737736928063490', 'EC101000036', 'pcb', 'EC101000036#220614#6000#CG20220516002', '220614', '个', '', 'FG', '', '', '1539862494408048641', '采购->入库', '2022-06-26 00:44:37', 'admin', '2022-07-15 01:07:37', 'admin', '0');
INSERT INTO `sp_materile` VALUES ('1541297876609679361', 'BP66767676776', '三极管', 'BP66767676776#220621#222#222#3444444', '220621', '', NULL, '', '', '', '', NULL, '2022-06-27 13:50:24', 'admin', '2022-06-27 21:47:24', 'admin', '0');
INSERT INTO `sp_materile` VALUES ('1554389111192829954', 'zemine220802', '', 'zemine220802#220621#222#222#3444444', '220621', NULL, NULL, '', '', NULL, '', NULL, '2022-08-02 16:50:18', 'admin', '2022-08-02 16:50:18', 'admin', '0');

-- ----------------------------
-- Table structure for sp_oper
-- ----------------------------
DROP TABLE IF EXISTS `sp_oper`;
CREATE TABLE `sp_oper`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `oper` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工序\r\n',
  `oper_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工序描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_oper
-- ----------------------------
INSERT INTO `sp_oper` VALUES ('1336864489340960', 'ASY-01', '装配工序', '2020-03-14 10:04:24', 'admin', '2020-03-14 10:04:24', 'admin');
INSERT INTO `sp_oper` VALUES ('1336864537575456', 'TST-02', '测试工序', '2020-03-14 10:04:47', 'admin', '2020-03-14 10:04:47', 'admin');
INSERT INTO `sp_oper` VALUES ('1336864575324192', 'APK-01', '包装工序', '2020-03-14 10:05:05', 'admin', '2020-03-14 10:05:05', 'admin');
INSERT INTO `sp_oper` VALUES ('1336864613072928', 'TST-01', '集成测试工序', '2020-03-14 10:05:23', 'admin', '2020-03-14 10:05:23', 'admin');
INSERT INTO `sp_oper` VALUES ('1336868360683552', 'HJ-01', '焊接', '2020-03-14 10:35:10', 'admin', '2020-03-14 10:35:10', 'admin');
INSERT INTO `sp_oper` VALUES ('1336868452958240', 'FJ-01', '封胶工序', '2020-03-14 10:35:54', 'admin', '2020-03-14 10:35:54', 'admin');
INSERT INTO `sp_oper` VALUES ('1336868507484192', 'JS-01', '加酸工序', '2020-03-14 10:36:20', 'admin', '2020-03-14 10:36:20', 'admin');
INSERT INTO `sp_oper` VALUES ('1336868562010144', 'QX-01', '清洗工序', '2020-03-14 10:36:46', 'admin', '2020-03-14 10:36:46', 'admin');
INSERT INTO `sp_oper` VALUES ('1337248255574048', 'RK-01', '入库工序', '2020-03-16 12:54:18', 'admin', '2020-03-16 12:54:18', 'admin');
INSERT INTO `sp_oper` VALUES ('1559594964397264897', '上料', '上料', '2022-08-17 01:36:30', 'admin', '2022-08-17 01:36:30', 'admin');
INSERT INTO `sp_oper` VALUES ('1559595062946631682', '贴片', '贴片', '2022-08-17 01:36:54', 'admin', '2022-08-17 01:36:54', 'admin');
INSERT INTO `sp_oper` VALUES ('1559595229389197314', '绕丝', '绕丝', '2022-08-17 01:37:33', 'admin', '2022-08-17 01:37:33', 'admin');
INSERT INTO `sp_oper` VALUES ('1559595327984701441', '加固', '加固', '2022-08-17 01:37:57', 'admin', '2022-08-17 01:37:57', 'admin');
INSERT INTO `sp_oper` VALUES ('1559595389871656962', '测试', '测试', '2022-08-17 01:38:11', 'admin', '2022-08-17 01:38:11', 'admin');
INSERT INTO `sp_oper` VALUES ('1559595533094555650', '打码', '打码', '2022-08-17 01:38:46', 'admin', '2022-08-17 01:38:46', 'admin');
INSERT INTO `sp_oper` VALUES ('1559936454659051521', '扫码', '扫码', '2022-08-18 00:13:28', 'admin', '2022-08-18 00:13:28', 'admin');
INSERT INTO `sp_oper` VALUES ('1866580263612345', 'CaiGou', '采购', '2022-06-23 14:31:25', 'admin', '2022-08-17 01:32:52', 'admin');
INSERT INTO `sp_oper` VALUES ('1866580263623456', 'RuKu', '入库', '2022-06-23 14:41:41', 'admin', '2022-06-23 14:42:03', 'admin');

-- ----------------------------
-- Table structure for sp_order
-- ----------------------------
DROP TABLE IF EXISTS `sp_order`;
CREATE TABLE `sp_order`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0.00' COMMENT '主键id',
  `order_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '工单编号',
  `order_description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '工单描述',
  `plan_qty` int UNSIGNED NULL DEFAULT 0 COMMENT '计划产品数量',
  `order_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单类型 P 量产 A验证 F返工 ',
  `flow` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '工艺流程',
  `product` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品类型',
  `plan_start_time` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计划结束时间',
  `status` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '创建' COMMENT '1,创建 2 进行中，3订单结束，4订单终结',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后更新人',
  `line` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '生产线',
  `maked_qty` int NULL DEFAULT 0 COMMENT '已经生产数量',
  `bad_qty` int(6) UNSIGNED ZEROFILL NULL DEFAULT 000000 COMMENT '不合格数量',
  `pass_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '质检通过率',
  `qrcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单二维码,可以从二维码解析出多个字段，依赖于客户的具体需求，保留字段',
  `memo` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '备注',
  `finish_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '订单完成率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_order
-- ----------------------------
INSERT INTO `sp_order` VALUES ('1542841377960964098', 'HME2022071501', NULL, 6000, NULL, '磁阻传感器生产工艺', NULL, '2022-07-31 20:02:01', '2022-08-19 20:02:01', '创建', '2022-07-01 20:03:44', 'admin', '2022-08-17 01:42:32', 'admin', '高磁阻变压器', 0, 000000, 0.00, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('1542898360709758978', 'HME2022070101', NULL, 4000, NULL, '测速传感器', NULL, '2022-08-01 23:49:00', '2022-08-10 23:49:00', '创建', '2022-07-01 23:50:10', 'admin', '2022-08-17 01:47:04', 'admin', '高磁阻变压器', 1021, 000003, 98.00, '', 'memo', 25.53);
INSERT INTO `sp_order` VALUES ('1548372368175656961', 'HME2022071801', NULL, 3000, NULL, '斗车', NULL, '2022-07-17 08:00:00', '2022-07-20 17:40:00', '订单终结', '2022-07-17 02:21:55', 'admin', '2022-08-16 14:54:16', 'admin', '磁阻变压器', 0, 000000, 0.00, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('1548596501324959745', 'DD2022070202', NULL, 6000, NULL, '磁阻传感器生产工艺', NULL, '2022-07-18 08:00:00', '2022-08-07 17:12:00', '创建', '2022-07-17 17:12:32', 'admin', '2022-08-17 01:42:05', 'admin', '磁阻变压器', 1800, 000002, 0.00, '', '', 30.00);
INSERT INTO `sp_order` VALUES ('453453453', 'HE2022062601-zemine', '生产变速传感器2000套', 8000, '1', 'A01装配工序->测试工序', NULL, '2022-06-30 14:41:42', '2022-07-13 20:13:00', '订单结束', '2022-06-30 14:49:32', NULL, '2022-08-16 14:54:37', 'admin', '压力传感器', 3000, 000009, NULL, '', '', 100.00);
INSERT INTO `sp_order` VALUES ('453453489', 'HME2022070401', '生产变速传感器3000套', 3000, '1', 'A01装配工序->测试工序', NULL, '2022-07-04 14:41:42', '2022-07-09 14:41:42', '进行中', '2022-06-30 14:49:32', NULL, '2022-08-16 14:54:50', 'admin', '测速传感器产线', 1000, 000004, NULL, '', '', 33.30);
INSERT INTO `sp_order` VALUES ('453453490', 'HE2022062801', '生产变速传感器3000套', 4000, '1', 'A01装配工序->测试工序', NULL, '2022-07-04 14:41:42', '2022-07-12 14:41:42', '创建', '2022-06-30 14:49:32', NULL, '2022-08-16 14:55:04', 'admin', '高磁阻变压器', 300, 000001, NULL, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('5345345', 'EC2022062002', '生产1000个高磁阻变压器', 2400, '2', 'A01装配工序->测试工序', NULL, '2022-07-01 14:41:42', '2022-07-04 14:41:42', '订单终结', '2022-06-30 14:41:42', NULL, '2022-08-18 00:25:23', 'admin', '高磁阻变压器', 600, 000002, NULL, '', '', 25.00);

-- ----------------------------
-- Table structure for sp_product
-- ----------------------------
DROP TABLE IF EXISTS `sp_product`;
CREATE TABLE `sp_product`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `product_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品名称',
  `bom_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'bom code',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `qrcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品二维码',
  `batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品批次',
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `quality` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'ok' COMMENT '产品品质: 0 合格，1 不合格 ',
  `position` int NULL DEFAULT 1 COMMENT '产品库位： 0， 车间，1 成品仓库，2 出库',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程id',
  `flow_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_product
-- ----------------------------
INSERT INTO `sp_product` VALUES ('1541622307823448066', 'EC101000036', 'shat', 'bom0036', '2022-07-16 11:19:35', 'admin', '2022-08-17 01:49:48', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', 'ok', 1, '0', '1559443376458342402', '磁阻传感器生产工艺');
INSERT INTO `sp_product` VALUES ('1541622307823448067', 'EC101000037', '', 'bom0037', '2022-06-28 11:19:35', 'admin', '2022-07-15 01:03:02', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', 'ok', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1541622307823448068', 'EC101000038', '', 'bom0038', '2022-06-28 11:20:35', 'admin', '2022-07-13 14:43:43', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', '00', 1, '2', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1541622307823448069', 'EC101000039', '', 'bom0039', '2022-06-28 11:20:36', 'admin', '2022-07-08 10:16:05', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', '00', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1541622307823448070', 'EC101000040', '', 'bom0040', '2022-06-28 11:20:35', 'admin', '2022-07-08 10:16:05', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', '00', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1541622307823448071', 'EC101000041', '', 'bom0041', '2022-06-28 11:20:41', 'admin', '2022-07-08 10:16:05', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', '00', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548023569603280897', 'EC101000050', '', 'BOM0050', '2022-07-16 03:15:55', 'admin', '2022-07-16 03:15:55', 'admin', 'EC101000036#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1277176874674663425', 'A01装配工序->测试工序');
INSERT INTO `sp_product` VALUES ('1548027249475895298', 'EC101000051', '', 'BOM0051', '2022-07-16 03:30:32', 'admin', '2022-07-16 03:30:32', 'admin', 'EC101000051#220614#6000#HME2022070101', '220614', 'HME2022070101', 'NG', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548089167909679106', 'EC101000052', '', 'BOM0052', '2022-07-16 07:36:35', 'admin', '2022-07-16 07:36:35', 'admin', 'EC101000052#220614#6000#HME2022070101', '220614', 'HME2022070101', 'ok', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548115820752797698', 'EC101000053', '', 'BOM0053', '2022-07-16 09:22:29', 'admin', '2022-07-16 09:22:29', 'admin', 'EC101000053#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548118282196877314', 'EC101000054', '', 'BOM0054', '2022-07-16 09:32:16', 'admin', '2022-07-16 09:32:16', 'admin', 'EC101000054#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548118631548846081', 'EC101000055', '', 'BOM0055', '2022-07-16 09:33:39', 'admin', '2022-07-16 09:33:39', 'admin', 'EC101000055#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548119000324636673', 'EC101000056', '', 'BOM0056', '2022-07-16 09:35:07', 'admin', '2022-07-16 09:35:07', 'admin', 'EC101000056#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548119109003247617', 'EC101000057', '', 'BOM0057', '2022-07-16 09:35:33', 'admin', '2022-07-16 09:35:33', 'admin', 'EC101000057#220614#6000#HME2022070101', '220614', 'HME2022070101', '良好', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548121577263083522', 'EC101000055', '', 'BOM55', '2022-07-16 09:45:21', 'admin', '2022-07-16 09:45:21', 'admin', 'EC101000053#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '1539862494408048641', '采购->入库');
INSERT INTO `sp_product` VALUES ('1548122981407010817', 'EC101000058', '', 'BOM0058', '2022-07-16 09:50:56', 'admin', '2022-07-16 09:50:56', 'admin', 'EC101000058#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548126048076611586', 'EC101000059', '', 'BOM0059', '2022-07-16 10:03:07', 'admin', '2022-07-16 10:03:07', 'admin', 'EC101000059#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548129350705115138', 'EC101000060', '', 'BOM0060', '2022-07-16 10:16:15', 'admin', '2022-07-16 10:16:15', 'admin', 'EC101000060#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548131642330214402', 'EC101000061', '', 'BOM0061', '2022-07-16 10:25:21', 'admin', '2022-07-16 10:25:21', 'admin', 'EC101000061#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548133285654310913', 'EC101000062', '', 'BOM0062', '2022-07-16 10:31:53', 'admin', '2022-07-16 10:31:53', 'admin', 'EC101000062#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548600857432625153', 'EC101000066', '', 'BOM0066', '2022-07-17 17:29:51', 'admin', '2022-07-17 17:29:51', 'admin', 'EC101000066#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548602176922914817', 'EC101000071', '', 'BOM0071', '2022-07-17 17:35:05', 'admin', '2022-07-17 17:35:05', 'admin', 'EC101000071#220614#6000#DD2022070202', '220614', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548603419699384321', '37XU0400022', '', 'bom0400022', '2022-07-17 17:40:02', 'admin', '2022-07-17 17:40:02', 'admin', '37XU0400022#220717#6000#DD2022070202', '220717', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548628213215952897', '37XU0400023', '', '', '2022-07-17 19:18:33', 'admin', '2022-07-17 19:18:33', 'admin', '37XU0400023#220717#6000#DD2022070202', '220717', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548630715692908546', '37XU0400024', '', 'BOM0400024', '2022-07-17 19:28:29', 'admin', '2022-07-17 19:28:29', 'admin', '37XU0400024#220717#6000#DD2022070202', '220717', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1548848106481811458', '37XU0400025', ' 磁阻变压器', 'BOM-37XU0400025', '2022-07-18 09:52:19', 'admin', '2022-07-18 16:44:27', 'admin', '37XU0400025#220717#6000#DD2022070202', '220717', 'DD2022070202', 'ok', 1, '0', '1278528234456330242', '斗车');
INSERT INTO `sp_product` VALUES ('1551956050090070017', 'EC101000064', '', '', '2022-07-26 23:42:11', 'admin', '2022-07-26 23:42:11', 'admin', 'EC101000064#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1551965576075878402', 'EC101000065', '', '', '2022-07-27 00:21:25', 'admin', '2022-07-27 00:21:25', 'admin', 'EC101000064#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1551970562390302721', 'EC101000070', '', '', '2022-07-27 00:39:51', 'admin', '2022-07-27 00:39:51', 'admin', 'EC101000070#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1551978580221829122', 'EC101000071', '', '', '2022-07-27 01:11:43', 'admin', '2022-07-27 01:11:43', 'admin', 'EC101000071#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1551981864298934274', 'EC101000074', '', '', '2022-07-27 01:24:46', 'admin', '2022-07-27 01:24:46', 'admin', 'EC101000074#220614#6000#HME2022070101', '220614', 'HME2022070101', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1552567984481067009', 'EC101000075', '', '', '2022-07-28 16:13:47', 'lgl', '2022-07-28 16:13:47', 'lgl', 'EC101000075#220614#6000#HME2022070101', '220614', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1552568229453586434', 'EC101000075', '', '', '2022-07-28 16:14:46', 'lgl', '2022-07-28 16:14:46', 'lgl', 'EC101000075#220614#6000#HME2022070101', '220614', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1552568510736195585', 'EC101000076', '', '', '2022-07-28 16:15:53', 'lgl', '2022-07-28 16:15:53', 'lgl', 'EC101000075#220614#6000#HME2022070101', '220614', 'DD2022070202', '', 1, '0', '', NULL);
INSERT INTO `sp_product` VALUES ('1552594187900338178', 'DD123123123', NULL, NULL, '2022-07-28 17:57:55', 'lgl', '2022-07-28 17:57:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552671175050715137', '60t4yh74o1', NULL, NULL, '2022-07-28 23:03:50', 'lgl', '2022-07-28 23:03:50', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552673139062886402', 'twmbwwv3bv', NULL, NULL, '2022-07-28 23:11:38', 'lgl', '2022-07-28 23:11:38', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552674458494476290', '1ay2ovnjhi', NULL, NULL, '2022-07-28 23:16:53', 'lgl', '2022-07-28 23:16:53', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552674548009312258', '73vkfh536f', NULL, NULL, '2022-07-28 23:17:14', 'lgl', '2022-07-28 23:17:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552674673783906305', 'g6rq4exf3u', NULL, NULL, '2022-07-28 23:17:44', 'lgl', '2022-07-28 23:17:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552674799604637698', 'vgazqa37gj', NULL, NULL, '2022-07-28 23:18:14', 'lgl', '2022-07-28 23:18:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552674925479895041', 'yum0jqbeoj', NULL, NULL, '2022-07-28 23:18:44', 'lgl', '2022-07-28 23:18:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675051283849218', 'u99jymnibv', NULL, NULL, '2022-07-28 23:19:14', 'lgl', '2022-07-28 23:19:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675177125552130', 'msqahd701b', NULL, NULL, '2022-07-28 23:19:44', 'lgl', '2022-07-28 23:19:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675302946283521', '4l465v5smh', NULL, NULL, '2022-07-28 23:20:14', 'lgl', '2022-07-28 23:20:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675429144502274', 'nokdwhsfee', NULL, NULL, '2022-07-28 23:20:44', 'lgl', '2022-07-28 23:20:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675554549997569', 'tr248pc0ao', NULL, NULL, '2022-07-28 23:21:14', 'lgl', '2022-07-28 23:21:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675680370728961', 'pgtig1nqzf', NULL, NULL, '2022-07-28 23:21:44', 'lgl', '2022-07-28 23:21:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675806283735041', 'wutel8jj9y', NULL, NULL, '2022-07-28 23:22:14', 'lgl', '2022-07-28 23:22:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552675932087689218', '77aj3tmsyc', NULL, NULL, '2022-07-28 23:22:44', 'lgl', '2022-07-28 23:22:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676057925197825', '99f29epdn4', NULL, NULL, '2022-07-28 23:23:14', 'lgl', '2022-07-28 23:23:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676183771095041', 'fva7ehdlhq', NULL, NULL, '2022-07-28 23:23:44', 'lgl', '2022-07-28 23:23:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676309562466305', 'fpq3reqf9z', NULL, NULL, '2022-07-28 23:24:14', 'lgl', '2022-07-28 23:24:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676435366420481', '9ja4u92nd9', NULL, NULL, '2022-07-28 23:24:44', 'lgl', '2022-07-28 23:24:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676561216512001', 'mze2sxuhkn', NULL, NULL, '2022-07-28 23:25:14', 'lgl', '2022-07-28 23:25:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676687137906690', 'qshn78x2oh', NULL, NULL, '2022-07-28 23:25:44', 'lgl', '2022-07-28 23:25:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676812874752001', '564489hfju', NULL, NULL, '2022-07-28 23:26:14', 'lgl', '2022-07-28 23:26:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552676938712260610', 'fn4ciyfxsr', NULL, NULL, '2022-07-28 23:26:44', 'lgl', '2022-07-28 23:26:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677064512020481', 'hg8twkvs6k', NULL, NULL, '2022-07-28 23:27:14', 'lgl', '2022-07-28 23:27:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677190353723394', '4yrsyibuxt', NULL, NULL, '2022-07-28 23:27:44', 'lgl', '2022-07-28 23:27:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677316329644034', 'l38ip2go79', NULL, NULL, '2022-07-28 23:28:14', 'lgl', '2022-07-28 23:28:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677442028740610', 'fc709unijv', NULL, NULL, '2022-07-28 23:28:44', 'lgl', '2022-07-28 23:28:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677568503783425', '25pkx62b0w', NULL, NULL, '2022-07-28 23:29:14', 'lgl', '2022-07-28 23:29:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677693716340738', 'ghw7vqcuin', NULL, NULL, '2022-07-28 23:29:44', 'lgl', '2022-07-28 23:29:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677819503517698', '6o7gqqvew6', NULL, NULL, '2022-07-28 23:30:14', 'lgl', '2022-07-28 23:30:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552677945353609218', '67y3wbggaa', NULL, NULL, '2022-07-28 23:30:44', 'lgl', '2022-07-28 23:30:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552678071212089345', 'ga6vcry8ym', NULL, NULL, '2022-07-28 23:31:14', 'lgl', '2022-07-28 23:31:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552678196982489090', '2ie304kmbz', NULL, NULL, '2022-07-28 23:31:44', 'lgl', '2022-07-28 23:31:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552678322857746433', 'c5csgno7b4', NULL, NULL, '2022-07-28 23:32:14', 'lgl', '2022-07-28 23:32:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1552678448691060738', '1uepwc4ots', NULL, NULL, '2022-07-28 23:32:44', 'lgl', '2022-07-28 23:32:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554547519472676865', 'DD2022080363', NULL, NULL, '2022-08-03 03:19:45', 'lgl', '2022-08-03 03:19:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554547645368905729', 'DD2022080361', NULL, NULL, '2022-08-03 03:20:15', 'lgl', '2022-08-03 03:20:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554547771206414337', 'DD2022080352', NULL, NULL, '2022-08-03 03:20:45', 'lgl', '2022-08-03 03:20:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554547896993591297', 'DD2022080378', NULL, NULL, '2022-08-03 03:21:15', 'lgl', '2022-08-03 03:21:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548022826905602', 'DD2022080377', NULL, NULL, '2022-08-03 03:21:45', 'lgl', '2022-08-03 03:21:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548148647636993', 'DD2022080391', NULL, NULL, '2022-08-03 03:22:15', 'lgl', '2022-08-03 03:22:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548274506117122', 'DD2022080385', NULL, NULL, '2022-08-03 03:22:45', 'lgl', '2022-08-03 03:22:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548400331042818', 'DD2022080321', NULL, NULL, '2022-08-03 03:23:15', 'lgl', '2022-08-03 03:23:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548526139191297', 'DD2022080354', NULL, NULL, '2022-08-03 03:23:45', 'lgl', '2022-08-03 03:23:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548651934756866', 'DD2022080325', NULL, NULL, '2022-08-03 03:24:15', 'lgl', '2022-08-03 03:24:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548777839374338', 'DD2022080368', NULL, NULL, '2022-08-03 03:24:45', 'lgl', '2022-08-03 03:24:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554548903655911425', 'DD2022080394', NULL, NULL, '2022-08-03 03:25:15', 'lgl', '2022-08-03 03:25:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549029497614337', 'DD2022080387', NULL, NULL, '2022-08-03 03:25:45', 'lgl', '2022-08-03 03:25:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549155288985601', 'DD2022080308', NULL, NULL, '2022-08-03 03:26:15', 'lgl', '2022-08-03 03:26:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549281155854338', 'DD2022080362', NULL, NULL, '2022-08-03 03:26:45', 'lgl', '2022-08-03 03:26:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549406951419905', 'DD2022080356', NULL, NULL, '2022-08-03 03:27:15', 'lgl', '2022-08-03 03:27:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549532788928514', 'DD2022080390', NULL, NULL, '2022-08-03 03:27:45', 'lgl', '2022-08-03 03:27:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549658559328257', 'DD2022080385', NULL, NULL, '2022-08-03 03:28:15', 'lgl', '2022-08-03 03:28:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549784468140034', 'DD2022080358', NULL, NULL, '2022-08-03 03:28:45', 'lgl', '2022-08-03 03:28:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554549910318231554', 'DD2022080322', NULL, NULL, '2022-08-03 03:29:15', 'lgl', '2022-08-03 03:29:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550036097019905', 'DD2022080394', NULL, NULL, '2022-08-03 03:29:45', 'lgl', '2022-08-03 03:29:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550161892585474', 'DD2022080338', NULL, NULL, '2022-08-03 03:30:15', 'lgl', '2022-08-03 03:30:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550287797202945', 'DD2022080338', NULL, NULL, '2022-08-03 03:30:45', 'lgl', '2022-08-03 03:30:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550413592768514', 'DD2022080367', NULL, NULL, '2022-08-03 03:31:15', 'lgl', '2022-08-03 03:31:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550539400916994', 'DD2022080314', NULL, NULL, '2022-08-03 03:31:45', 'lgl', '2022-08-03 03:31:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550665213259778', 'DD2022080339', NULL, NULL, '2022-08-03 03:32:15', 'lgl', '2022-08-03 03:32:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550791088517122', 'DD2022080317', NULL, NULL, '2022-08-03 03:32:45', 'lgl', '2022-08-03 03:32:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550916955385858', 'DD2022080313', NULL, NULL, '2022-08-03 03:33:15', 'lgl', '2022-08-03 03:33:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551042746757122', 'DD2022080358', NULL, NULL, '2022-08-03 03:33:45', 'lgl', '2022-08-03 03:33:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551168521351170', 'DD2022080376', NULL, NULL, '2022-08-03 03:34:15', 'lgl', '2022-08-03 03:34:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551294363054081', 'DD2022080378', NULL, NULL, '2022-08-03 03:34:45', 'lgl', '2022-08-03 03:34:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551420225728513', 'DD2022080368', NULL, NULL, '2022-08-03 03:35:15', 'lgl', '2022-08-03 03:35:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551546067431426', 'DD2022080351', NULL, NULL, '2022-08-03 03:35:45', 'lgl', '2022-08-03 03:35:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551671871385602', 'DD2022080394', NULL, NULL, '2022-08-03 03:36:15', 'lgl', '2022-08-03 03:36:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551797696311297', 'DD2022080375', NULL, NULL, '2022-08-03 03:36:45', 'lgl', '2022-08-03 03:36:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554551923504459778', 'DD2022080377', NULL, NULL, '2022-08-03 03:37:15', 'lgl', '2022-08-03 03:37:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552049362939906', 'DD2022080372', NULL, NULL, '2022-08-03 03:37:45', 'lgl', '2022-08-03 03:37:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552175175282690', 'DD2022080381', NULL, NULL, '2022-08-03 03:38:15', 'lgl', '2022-08-03 03:38:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552301046345729', 'DD2022080352', NULL, NULL, '2022-08-03 03:38:45', 'lgl', '2022-08-03 03:38:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552426900631554', 'DD2022080358', NULL, NULL, '2022-08-03 03:39:15', 'lgl', '2022-08-03 03:39:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552552683614210', 'DD2022080328', NULL, NULL, '2022-08-03 03:39:45', 'lgl', '2022-08-03 03:39:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552678462402562', 'DD2022080379', NULL, NULL, '2022-08-03 03:40:15', 'lgl', '2022-08-03 03:40:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552804362825729', 'DD2022080310', NULL, NULL, '2022-08-03 03:40:45', 'lgl', '2022-08-03 03:40:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554552930158391298', 'DD2022080350', NULL, NULL, '2022-08-03 03:41:15', 'lgl', '2022-08-03 03:41:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553056042037250', 'DD2022080316', NULL, NULL, '2022-08-03 03:41:45', 'lgl', '2022-08-03 03:41:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553181812436994', 'DD2022080303', NULL, NULL, '2022-08-03 03:42:15', 'lgl', '2022-08-03 03:42:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553307683500034', 'DD2022080379', NULL, NULL, '2022-08-03 03:42:45', 'lgl', '2022-08-03 03:42:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553433432928258', 'DD2022080304', NULL, NULL, '2022-08-03 03:43:15', 'lgl', '2022-08-03 03:43:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553559287214081', 'DD2022080300', NULL, NULL, '2022-08-03 03:43:45', 'lgl', '2022-08-03 03:43:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553685137305602', 'DD2022080347', NULL, NULL, '2022-08-03 03:44:15', 'lgl', '2022-08-03 03:44:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553810928676866', 'DD2022080390', NULL, NULL, '2022-08-03 03:44:45', 'lgl', '2022-08-03 03:44:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554553936795545602', 'DD2022080337', NULL, NULL, '2022-08-03 03:45:15', 'lgl', '2022-08-03 03:45:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554554062658220034', 'DD2022080336', NULL, NULL, '2022-08-03 03:45:45', 'lgl', '2022-08-03 03:45:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554554188487340034', 'DD2022080317', NULL, NULL, '2022-08-03 03:46:15', 'lgl', '2022-08-03 03:46:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554554314333237250', 'DD2022080314', NULL, NULL, '2022-08-03 03:46:45', 'lgl', '2022-08-03 03:46:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554554440078471169', 'DD2022080368', NULL, NULL, '2022-08-03 03:47:15', 'lgl', '2022-08-03 03:47:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554554565924368385', 'DD2022080349', NULL, NULL, '2022-08-03 03:47:45', 'lgl', '2022-08-03 03:47:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554599981671919617', 'DD2022080393', NULL, NULL, '2022-08-03 06:48:13', 'lgl', '2022-08-03 06:48:13', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600002278535170', 'DD2022080337', NULL, NULL, '2022-08-03 06:48:18', 'lgl', '2022-08-03 06:48:18', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600022285365250', 'DD2022080392', NULL, NULL, '2022-08-03 06:48:23', 'lgl', '2022-08-03 06:48:23', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600037804290049', 'DD2022080315', NULL, NULL, '2022-08-03 06:48:27', 'lgl', '2022-08-03 06:48:27', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600047946117122', 'DD2022080391', NULL, NULL, '2022-08-03 06:48:29', 'lgl', '2022-08-03 06:48:29', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600057936949250', 'DD2022080396', NULL, NULL, '2022-08-03 06:48:32', 'lgl', '2022-08-03 06:48:32', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600068967968770', 'DD2022080336', NULL, NULL, '2022-08-03 06:48:34', 'lgl', '2022-08-03 06:48:34', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600080024154114', 'DD2022080333', NULL, NULL, '2022-08-03 06:48:37', 'lgl', '2022-08-03 06:48:37', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600092053417986', 'DD2022080300', NULL, NULL, '2022-08-03 06:48:40', 'lgl', '2022-08-03 06:48:40', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600102602092545', 'DD2022080377', NULL, NULL, '2022-08-03 06:48:42', 'lgl', '2022-08-03 06:48:42', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600114442612738', 'DD2022080304', NULL, NULL, '2022-08-03 06:48:45', 'lgl', '2022-08-03 06:48:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600125968560130', 'DD2022080347', NULL, NULL, '2022-08-03 06:48:48', 'lgl', '2022-08-03 06:48:48', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600136118775809', 'DD2022080372', NULL, NULL, '2022-08-03 06:48:50', 'lgl', '2022-08-03 06:48:50', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600149083369474', 'DD2022080337', NULL, NULL, '2022-08-03 06:48:53', 'lgl', '2022-08-03 06:48:53', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600160055668737', 'DD2022080321', NULL, NULL, '2022-08-03 06:48:56', 'lgl', '2022-08-03 06:48:56', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600174555377666', 'DD2022080391', NULL, NULL, '2022-08-03 06:48:59', 'lgl', '2022-08-03 06:48:59', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600185045331970', 'DD2022080309', NULL, NULL, '2022-08-03 06:49:02', 'lgl', '2022-08-03 06:49:02', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600194868391938', 'DD2022080349', NULL, NULL, '2022-08-03 06:49:04', 'lgl', '2022-08-03 06:49:04', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600204498513921', 'DD2022080319', NULL, NULL, '2022-08-03 06:49:06', 'lgl', '2022-08-03 06:49:06', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600214543872002', 'DD2022080382', NULL, NULL, '2022-08-03 06:49:09', 'lgl', '2022-08-03 06:49:09', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600225243541505', 'DD2022080374', NULL, NULL, '2022-08-03 06:49:11', 'lgl', '2022-08-03 06:49:11', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600235095961602', 'DD2022080365', NULL, NULL, '2022-08-03 06:49:14', 'lgl', '2022-08-03 06:49:14', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600245418143746', 'DD2022080307', NULL, NULL, '2022-08-03 06:49:16', 'lgl', '2022-08-03 06:49:16', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600255039877121', 'DD2022080305', NULL, NULL, '2022-08-03 06:49:19', 'lgl', '2022-08-03 06:49:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600265072652290', 'DD2022080394', NULL, NULL, '2022-08-03 06:49:21', 'lgl', '2022-08-03 06:49:21', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600275424194561', 'DD2022080301', NULL, NULL, '2022-08-03 06:49:23', 'lgl', '2022-08-03 06:49:23', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600284991401986', 'DD2022080385', NULL, NULL, '2022-08-03 06:49:26', 'lgl', '2022-08-03 06:49:26', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600294885765121', 'DD2022080367', NULL, NULL, '2022-08-03 06:49:28', 'lgl', '2022-08-03 06:49:28', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600305031786497', 'DD2022080366', NULL, NULL, '2022-08-03 06:49:30', 'lgl', '2022-08-03 06:49:30', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600314880012290', 'DD2022080310', NULL, NULL, '2022-08-03 06:49:33', 'lgl', '2022-08-03 06:49:33', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600324849872897', 'DD2022080332', NULL, NULL, '2022-08-03 06:49:35', 'lgl', '2022-08-03 06:49:35', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600334605824002', 'DD2022080394', NULL, NULL, '2022-08-03 06:49:38', 'lgl', '2022-08-03 06:49:38', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600344626016258', 'DD2022080335', NULL, NULL, '2022-08-03 06:49:40', 'lgl', '2022-08-03 06:49:40', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600354386161665', 'DD2022080391', NULL, NULL, '2022-08-03 06:49:42', 'lgl', '2022-08-03 06:49:42', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600364003700738', 'DD2022080335', NULL, NULL, '2022-08-03 06:49:45', 'lgl', '2022-08-03 06:49:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600373981949953', 'DD2022080391', NULL, NULL, '2022-08-03 06:49:47', 'lgl', '2022-08-03 06:49:47', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600385109438465', 'DD2022080302', NULL, NULL, '2022-08-03 06:49:50', 'lgl', '2022-08-03 06:49:50', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600395293208577', 'DD2022080397', NULL, NULL, '2022-08-03 06:49:52', 'lgl', '2022-08-03 06:49:52', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600405200154625', 'DD2022080387', NULL, NULL, '2022-08-03 06:49:54', 'lgl', '2022-08-03 06:49:54', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600414154993665', 'DD2022080322', NULL, NULL, '2022-08-03 06:49:56', 'lgl', '2022-08-03 06:49:56', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554600423130804226', 'DD2022080375', NULL, NULL, '2022-08-03 06:49:59', 'lgl', '2022-08-03 06:49:59', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554641732025593857', 'DD2022080331', NULL, NULL, '2022-08-03 09:34:07', 'lgl', '2022-08-03 09:34:07', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715017321115650', 'DD2022080384', NULL, NULL, '2022-08-03 14:25:20', 'lgl', '2022-08-03 14:25:20', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715139866095617', 'DD2022080371', NULL, NULL, '2022-08-03 14:25:49', 'lgl', '2022-08-03 14:25:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715265644883970', 'DD2022080304', NULL, NULL, '2022-08-03 14:26:19', 'lgl', '2022-08-03 14:26:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715391486586882', 'DD2022080373', NULL, NULL, '2022-08-03 14:26:49', 'lgl', '2022-08-03 14:26:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715517294735362', 'DD2022080340', NULL, NULL, '2022-08-03 14:27:19', 'lgl', '2022-08-03 14:27:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715643140632578', 'DD2022080301', NULL, NULL, '2022-08-03 14:27:49', 'lgl', '2022-08-03 14:27:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715768999112706', 'DD2022080338', NULL, NULL, '2022-08-03 14:28:19', 'lgl', '2022-08-03 14:28:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554715894845009922', 'DD2022080334', NULL, NULL, '2022-08-03 14:28:49', 'lgl', '2022-08-03 14:28:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716020653158401', 'DD2022080378', NULL, NULL, '2022-08-03 14:29:19', 'lgl', '2022-08-03 14:29:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716146465501186', 'DD2022080307', NULL, NULL, '2022-08-03 14:29:49', 'lgl', '2022-08-03 14:29:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716272315592705', 'DD2022080312', NULL, NULL, '2022-08-03 14:30:19', 'lgl', '2022-08-03 14:30:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716398119546882', 'DD2022080343', NULL, NULL, '2022-08-03 14:30:49', 'lgl', '2022-08-03 14:30:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716523978027010', 'DD2022080394', NULL, NULL, '2022-08-03 14:31:19', 'lgl', '2022-08-03 14:31:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716649819729921', 'DD2022080321', NULL, NULL, '2022-08-03 14:31:49', 'lgl', '2022-08-03 14:31:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716775636267010', 'DD2022080337', NULL, NULL, '2022-08-03 14:32:19', 'lgl', '2022-08-03 14:32:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554716901482164226', 'DD2022080338', NULL, NULL, '2022-08-03 14:32:49', 'lgl', '2022-08-03 14:32:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717027286118401', 'DD2022080340', NULL, NULL, '2022-08-03 14:33:19', 'lgl', '2022-08-03 14:33:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717153119432706', 'DD2022080376', NULL, NULL, '2022-08-03 14:33:49', 'lgl', '2022-08-03 14:33:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717278994690050', 'DD2022080363', NULL, NULL, '2022-08-03 14:34:19', 'lgl', '2022-08-03 14:34:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717404819615746', 'DD2022080322', NULL, NULL, '2022-08-03 14:34:49', 'lgl', '2022-08-03 14:34:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717530623569921', 'DD2022080354', NULL, NULL, '2022-08-03 14:35:19', 'lgl', '2022-08-03 14:35:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717656444301314', 'DD2022080394', NULL, NULL, '2022-08-03 14:35:49', 'lgl', '2022-08-03 14:35:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717782265032706', 'DD2022080361', NULL, NULL, '2022-08-03 14:36:19', 'lgl', '2022-08-03 14:36:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554717908123512834', 'DD2022080320', NULL, NULL, '2022-08-03 14:36:49', 'lgl', '2022-08-03 14:36:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718033986187265', 'DD2022080343', NULL, NULL, '2022-08-03 14:37:19', 'lgl', '2022-08-03 14:37:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718159777558530', 'DD2022080329', NULL, NULL, '2022-08-03 14:37:49', 'lgl', '2022-08-03 14:37:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718285543763970', 'DD2022080311', NULL, NULL, '2022-08-03 14:38:19', 'lgl', '2022-08-03 14:38:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718411473547265', 'DD2022080363', NULL, NULL, '2022-08-03 14:38:49', 'lgl', '2022-08-03 14:38:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718537202003969', 'DD2022080324', NULL, NULL, '2022-08-03 14:39:19', 'lgl', '2022-08-03 14:39:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718663077261314', 'DD2022080309', NULL, NULL, '2022-08-03 14:39:49', 'lgl', '2022-08-03 14:39:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718788918964225', 'DD2022080322', NULL, NULL, '2022-08-03 14:40:19', 'lgl', '2022-08-03 14:40:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718914773250049', 'DD2022080336', NULL, NULL, '2022-08-03 14:40:49', 'lgl', '2022-08-03 14:40:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719040539455489', 'DD2022080370', NULL, NULL, '2022-08-03 14:41:19', 'lgl', '2022-08-03 14:41:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719166368575489', 'DD2022080343', NULL, NULL, '2022-08-03 14:41:49', 'lgl', '2022-08-03 14:41:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719292264804354', 'DD2022080367', NULL, NULL, '2022-08-03 14:42:19', 'lgl', '2022-08-03 14:42:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719418056175618', 'DD2022080376', NULL, NULL, '2022-08-03 14:42:49', 'lgl', '2022-08-03 14:42:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543851741186', 'DD2022080309', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719669722804225', 'DD2022080332', NULL, NULL, '2022-08-03 14:43:49', 'lgl', '2022-08-03 14:43:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719795518369794', 'DD2022080391', NULL, NULL, '2022-08-03 14:44:19', 'lgl', '2022-08-03 14:44:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719921393627137', 'DD2022080320', NULL, NULL, '2022-08-03 14:44:49', 'lgl', '2022-08-03 14:44:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720047172415489', 'DD2022080316', NULL, NULL, '2022-08-03 14:45:19', 'lgl', '2022-08-03 14:45:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720173056061442', 'DD2022080304', NULL, NULL, '2022-08-03 14:45:49', 'lgl', '2022-08-03 14:45:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720298818072578', 'DD2022080314', NULL, NULL, '2022-08-03 14:46:19', 'lgl', '2022-08-03 14:46:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720424718495746', 'DD2022080357', NULL, NULL, '2022-08-03 14:46:49', 'lgl', '2022-08-03 14:46:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720550526644226', 'DD2022080384', NULL, NULL, '2022-08-03 14:47:19', 'lgl', '2022-08-03 14:47:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720676309626882', 'DD2022080358', NULL, NULL, '2022-08-03 14:47:49', 'lgl', '2022-08-03 14:47:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720802142941186', 'DD2022080335', NULL, NULL, '2022-08-03 14:48:19', 'lgl', '2022-08-03 14:48:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720928030781441', 'DD2022080317', NULL, NULL, '2022-08-03 14:48:49', 'lgl', '2022-08-03 14:48:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721053859901441', 'DD2022080391', NULL, NULL, '2022-08-03 14:49:19', 'lgl', '2022-08-03 14:49:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721179626106881', 'DD2022080351', NULL, NULL, '2022-08-03 14:49:49', 'lgl', '2022-08-03 14:49:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721305455226881', 'DD2022080367', NULL, NULL, '2022-08-03 14:50:19', 'lgl', '2022-08-03 14:50:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721431267569666', 'DD2022080382', NULL, NULL, '2022-08-03 14:50:49', 'lgl', '2022-08-03 14:50:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721557147021313', 'DD2022080322', NULL, NULL, '2022-08-03 14:51:19', 'lgl', '2022-08-03 14:51:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721683022278658', 'DD2022080311', NULL, NULL, '2022-08-03 14:51:49', 'lgl', '2022-08-03 14:51:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721808822038529', 'DD2022080389', NULL, NULL, '2022-08-03 14:52:19', 'lgl', '2022-08-03 14:52:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721934621798401', 'DD2022080395', NULL, NULL, '2022-08-03 14:52:49', 'lgl', '2022-08-03 14:52:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722060421558274', 'DD2022080334', NULL, NULL, '2022-08-03 14:53:19', 'lgl', '2022-08-03 14:53:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722186242289665', 'DD2022080388', NULL, NULL, '2022-08-03 14:53:49', 'lgl', '2022-08-03 14:53:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722312096575490', 'DD2022080380', NULL, NULL, '2022-08-03 14:54:19', 'lgl', '2022-08-03 14:54:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722437971832833', 'DD2022080358', NULL, NULL, '2022-08-03 14:54:49', 'lgl', '2022-08-03 14:54:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722563771592706', 'DD2022080337', NULL, NULL, '2022-08-03 14:55:19', 'lgl', '2022-08-03 14:55:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722689651044354', 'DD2022080302', NULL, NULL, '2022-08-03 14:55:49', 'lgl', '2022-08-03 14:55:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722815421444097', 'DD2022080390', NULL, NULL, '2022-08-03 14:56:19', 'lgl', '2022-08-03 14:56:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554722941242175489', 'DD2022080330', NULL, NULL, '2022-08-03 14:56:49', 'lgl', '2022-08-03 14:56:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723067083878401', 'DD2022080315', NULL, NULL, '2022-08-03 14:57:19', 'lgl', '2022-08-03 14:57:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723192896221186', 'DD2022080312', NULL, NULL, '2022-08-03 14:57:49', 'lgl', '2022-08-03 14:57:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723318758895617', 'DD2022080359', NULL, NULL, '2022-08-03 14:58:19', 'lgl', '2022-08-03 14:58:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723444617375746', 'DD2022080310', NULL, NULL, '2022-08-03 14:58:49', 'lgl', '2022-08-03 14:58:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723570391969794', 'DD2022080357', NULL, NULL, '2022-08-03 14:59:19', 'lgl', '2022-08-03 14:59:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723696225284097', 'DD2022080366', NULL, NULL, '2022-08-03 14:59:49', 'lgl', '2022-08-03 14:59:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723822054404097', 'DD2022080348', NULL, NULL, '2022-08-03 15:00:19', 'lgl', '2022-08-03 15:00:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554723947938050049', 'DD2022080355', NULL, NULL, '2022-08-03 15:00:49', 'lgl', '2022-08-03 15:00:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724073691672577', 'DD2022080352', NULL, NULL, '2022-08-03 15:01:19', 'lgl', '2022-08-03 15:01:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724199583707137', 'DD2022080320', NULL, NULL, '2022-08-03 15:01:49', 'lgl', '2022-08-03 15:01:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724325375078402', 'DD2022080361', NULL, NULL, '2022-08-03 15:02:19', 'lgl', '2022-08-03 15:02:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724451241947137', 'DD2022080310', NULL, NULL, '2022-08-03 15:02:49', 'lgl', '2022-08-03 15:02:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724577037512705', 'DD2022080347', NULL, NULL, '2022-08-03 15:03:19', 'lgl', '2022-08-03 15:03:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724702921158658', 'DD2022080354', NULL, NULL, '2022-08-03 15:03:49', 'lgl', '2022-08-03 15:03:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724828741890049', 'DD2022080340', NULL, NULL, '2022-08-03 15:04:19', 'lgl', '2022-08-03 15:04:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554724954562621442', 'DD2022080373', NULL, NULL, '2022-08-03 15:04:49', 'lgl', '2022-08-03 15:04:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725080383352833', 'DD2022080391', NULL, NULL, '2022-08-03 15:05:19', 'lgl', '2022-08-03 15:05:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725206246027266', 'DD2022080385', NULL, NULL, '2022-08-03 15:05:49', 'lgl', '2022-08-03 15:05:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725331978678273', 'DD2022080343', NULL, NULL, '2022-08-03 15:06:19', 'lgl', '2022-08-03 15:06:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725457858129921', 'DD2022080323', NULL, NULL, '2022-08-03 15:06:49', 'lgl', '2022-08-03 15:06:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725583708221441', 'DD2022080335', NULL, NULL, '2022-08-03 15:07:19', 'lgl', '2022-08-03 15:07:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725709487009794', 'DD2022080325', NULL, NULL, '2022-08-03 15:07:49', 'lgl', '2022-08-03 15:07:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725835379044353', 'DD2022080359', NULL, NULL, '2022-08-03 15:08:19', 'lgl', '2022-08-03 15:08:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554725961136861185', 'DD2022080301', NULL, NULL, '2022-08-03 15:08:49', 'lgl', '2022-08-03 15:08:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726086982758401', 'DD2022080311', NULL, NULL, '2022-08-03 15:09:19', 'lgl', '2022-08-03 15:09:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726212807684098', 'DD2022080392', NULL, NULL, '2022-08-03 15:09:49', 'lgl', '2022-08-03 15:09:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726338594861058', 'DD2022080369', NULL, NULL, '2022-08-03 15:10:19', 'lgl', '2022-08-03 15:10:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726464474312705', 'DD2022080369', NULL, NULL, '2022-08-03 15:10:49', 'lgl', '2022-08-03 15:10:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726590286655489', 'DD2022080368', NULL, NULL, '2022-08-03 15:11:19', 'lgl', '2022-08-03 15:11:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726716086415362', 'DD2022080320', NULL, NULL, '2022-08-03 15:11:49', 'lgl', '2022-08-03 15:11:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726841911341058', 'DD2022080336', NULL, NULL, '2022-08-03 15:12:19', 'lgl', '2022-08-03 15:12:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554726967807569922', 'DD2022080342', NULL, NULL, '2022-08-03 15:12:49', 'lgl', '2022-08-03 15:12:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727093590552578', 'DD2022080384', NULL, NULL, '2022-08-03 15:13:19', 'lgl', '2022-08-03 15:13:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727219440644097', 'DD2022080313', NULL, NULL, '2022-08-03 15:13:49', 'lgl', '2022-08-03 15:13:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727345282347009', 'DD2022080352', NULL, NULL, '2022-08-03 15:14:19', 'lgl', '2022-08-03 15:14:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727471132438529', 'DD2022080389', NULL, NULL, '2022-08-03 15:14:49', 'lgl', '2022-08-03 15:14:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727596902838273', 'DD2022080385', NULL, NULL, '2022-08-03 15:15:19', 'lgl', '2022-08-03 15:15:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727722786484225', 'DD2022080332', NULL, NULL, '2022-08-03 15:15:49', 'lgl', '2022-08-03 15:15:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727848628187137', 'DD2022080399', NULL, NULL, '2022-08-03 15:16:19', 'lgl', '2022-08-03 15:16:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554727974432141313', 'DD2022080314', NULL, NULL, '2022-08-03 15:16:49', 'lgl', '2022-08-03 15:16:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728100257067010', 'DD2022080310', NULL, NULL, '2022-08-03 15:17:19', 'lgl', '2022-08-03 15:17:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728226107158530', 'DD2022080373', NULL, NULL, '2022-08-03 15:17:49', 'lgl', '2022-08-03 15:17:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728351911112706', 'DD2022080351', NULL, NULL, '2022-08-03 15:18:19', 'lgl', '2022-08-03 15:18:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728477719261185', 'DD2022080381', NULL, NULL, '2022-08-03 15:18:49', 'lgl', '2022-08-03 15:18:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728603573547009', 'DD2022080312', NULL, NULL, '2022-08-03 15:19:19', 'lgl', '2022-08-03 15:19:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728729385889793', 'DD2022080327', NULL, NULL, '2022-08-03 15:19:49', 'lgl', '2022-08-03 15:19:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728855177261057', 'DD2022080307', NULL, NULL, '2022-08-03 15:20:19', 'lgl', '2022-08-03 15:20:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554728981086072833', 'DD2022080361', NULL, NULL, '2022-08-03 15:20:49', 'lgl', '2022-08-03 15:20:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729106831306753', 'DD2022080382', NULL, NULL, '2022-08-03 15:21:19', 'lgl', '2022-08-03 15:21:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729232698175489', 'DD2022080308', NULL, NULL, '2022-08-03 15:21:49', 'lgl', '2022-08-03 15:21:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729358573432833', 'DD2022080352', NULL, NULL, '2022-08-03 15:22:19', 'lgl', '2022-08-03 15:22:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729484339638274', 'DD2022080315', NULL, NULL, '2022-08-03 15:22:49', 'lgl', '2022-08-03 15:22:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729610177146882', 'DD2022080379', NULL, NULL, '2022-08-03 15:23:19', 'lgl', '2022-08-03 15:23:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729736044015618', 'DD2022080398', NULL, NULL, '2022-08-03 15:23:49', 'lgl', '2022-08-03 15:23:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729861835386881', 'DD2022080354', NULL, NULL, '2022-08-03 15:24:19', 'lgl', '2022-08-03 15:24:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554729987685478401', 'DD2022080358', NULL, NULL, '2022-08-03 15:24:49', 'lgl', '2022-08-03 15:24:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730113489432578', 'DD2022080350', NULL, NULL, '2022-08-03 15:25:19', 'lgl', '2022-08-03 15:25:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730239368884226', 'DD2022080346', NULL, NULL, '2022-08-03 15:25:49', 'lgl', '2022-08-03 15:25:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730365193809921', 'DD2022080377', NULL, NULL, '2022-08-03 15:26:19', 'lgl', '2022-08-03 15:26:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730491022929922', 'DD2022080339', NULL, NULL, '2022-08-03 15:26:49', 'lgl', '2022-08-03 15:26:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730616927547394', 'DD2022080330', NULL, NULL, '2022-08-03 15:27:19', 'lgl', '2022-08-03 15:27:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730742668587009', 'DD2022080366', NULL, NULL, '2022-08-03 15:27:49', 'lgl', '2022-08-03 15:27:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730868531261442', 'DD2022080378', NULL, NULL, '2022-08-03 15:28:19', 'lgl', '2022-08-03 15:28:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554730994347798530', 'DD2022080359', NULL, NULL, '2022-08-03 15:28:49', 'lgl', '2022-08-03 15:28:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731120114003969', 'DD2022080385', NULL, NULL, '2022-08-03 15:29:19', 'lgl', '2022-08-03 15:29:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731246010232833', 'DD2022080333', NULL, NULL, '2022-08-03 15:29:49', 'lgl', '2022-08-03 15:29:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731371826769921', 'DD2022080349', NULL, NULL, '2022-08-03 15:30:19', 'lgl', '2022-08-03 15:30:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731497618141186', 'DD2022080322', NULL, NULL, '2022-08-03 15:30:49', 'lgl', '2022-08-03 15:30:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731623493398530', 'DD2022080387', NULL, NULL, '2022-08-03 15:31:19', 'lgl', '2022-08-03 15:31:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731749330907137', 'DD2022080357', NULL, NULL, '2022-08-03 15:31:49', 'lgl', '2022-08-03 15:31:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554731875118084097', 'DD2022080312', NULL, NULL, '2022-08-03 15:32:19', 'lgl', '2022-08-03 15:32:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732000955592705', 'DD2022080338', NULL, NULL, '2022-08-03 15:32:49', 'lgl', '2022-08-03 15:32:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732126767935490', 'DD2022080349', NULL, NULL, '2022-08-03 15:33:19', 'lgl', '2022-08-03 15:33:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732252609638401', 'DD2022080371', NULL, NULL, '2022-08-03 15:33:49', 'lgl', '2022-08-03 15:33:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732378417786881', 'DD2022080362', NULL, NULL, '2022-08-03 15:34:19', 'lgl', '2022-08-03 15:34:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732504280461313', 'DD2022080396', NULL, NULL, '2022-08-03 15:34:49', 'lgl', '2022-08-03 15:34:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732630487068673', 'DD2022080393', NULL, NULL, '2022-08-03 15:35:19', 'lgl', '2022-08-03 15:35:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732755905146882', 'DD2022080303', NULL, NULL, '2022-08-03 15:35:49', 'lgl', '2022-08-03 15:35:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554732881755238401', 'DD2022080394', NULL, NULL, '2022-08-03 15:36:19', 'lgl', '2022-08-03 15:36:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733007630495745', 'DD2022080344', NULL, NULL, '2022-08-03 15:36:49', 'lgl', '2022-08-03 15:36:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733133409284098', 'DD2022080318', NULL, NULL, '2022-08-03 15:37:19', 'lgl', '2022-08-03 15:37:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733259267764226', 'DD2022080337', NULL, NULL, '2022-08-03 15:37:49', 'lgl', '2022-08-03 15:37:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733385088495617', 'DD2022080385', NULL, NULL, '2022-08-03 15:38:19', 'lgl', '2022-08-03 15:38:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733510926004226', 'DD2022080323', NULL, NULL, '2022-08-03 15:38:49', 'lgl', '2022-08-03 15:38:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733636708986882', 'DD2022080386', NULL, NULL, '2022-08-03 15:39:19', 'lgl', '2022-08-03 15:39:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733762621992962', 'DD2022080371', NULL, NULL, '2022-08-03 15:39:49', 'lgl', '2022-08-03 15:39:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554733888430141441', 'DD2022080365', NULL, NULL, '2022-08-03 15:40:19', 'lgl', '2022-08-03 15:40:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734014322176001', 'DD2022080324', NULL, NULL, '2022-08-03 15:40:49', 'lgl', '2022-08-03 15:40:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734140054827009', 'DD2022080302', NULL, NULL, '2022-08-03 15:41:19', 'lgl', '2022-08-03 15:41:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734265930084354', 'DD2022080348', NULL, NULL, '2022-08-03 15:41:49', 'lgl', '2022-08-03 15:41:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734391692095490', 'DD2022080374', NULL, NULL, '2022-08-03 15:42:19', 'lgl', '2022-08-03 15:42:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734517508632578', 'DD2022080393', NULL, NULL, '2022-08-03 15:42:49', 'lgl', '2022-08-03 15:42:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734643383889921', 'DD2022080333', NULL, NULL, '2022-08-03 15:43:19', 'lgl', '2022-08-03 15:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734769217204225', 'DD2022080377', NULL, NULL, '2022-08-03 15:43:49', 'lgl', '2022-08-03 15:43:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554734895029547010', 'DD2022080387', NULL, NULL, '2022-08-03 15:44:19', 'lgl', '2022-08-03 15:44:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735020825112577', 'DD2022080384', NULL, NULL, '2022-08-03 15:44:49', 'lgl', '2022-08-03 15:44:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735146738118658', 'DD2022080358', NULL, NULL, '2022-08-03 15:45:19', 'lgl', '2022-08-03 15:45:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735272550461442', 'DD2022080356', NULL, NULL, '2022-08-03 15:45:49', 'lgl', '2022-08-03 15:45:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735398358609922', 'DD2022080314', NULL, NULL, '2022-08-03 15:46:19', 'lgl', '2022-08-03 15:46:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735524187729922', 'DD2022080394', NULL, NULL, '2022-08-03 15:46:49', 'lgl', '2022-08-03 15:46:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735650016849922', 'DD2022080370', NULL, NULL, '2022-08-03 15:47:19', 'lgl', '2022-08-03 15:47:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735775862747138', 'DD2022080335', NULL, NULL, '2022-08-03 15:47:49', 'lgl', '2022-08-03 15:47:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554735901675089921', 'DD2022080310', NULL, NULL, '2022-08-03 15:48:19', 'lgl', '2022-08-03 15:48:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736027466461186', 'DD2022080380', NULL, NULL, '2022-08-03 15:48:49', 'lgl', '2022-08-03 15:48:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736153278803970', 'DD2022080350', NULL, NULL, '2022-08-03 15:49:19', 'lgl', '2022-08-03 15:49:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736279133089793', 'DD2022080321', NULL, NULL, '2022-08-03 15:49:49', 'lgl', '2022-08-03 15:49:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736404974792705', 'DD2022080359', NULL, NULL, '2022-08-03 15:50:19', 'lgl', '2022-08-03 15:50:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736530829078530', 'DD2022080380', NULL, NULL, '2022-08-03 15:50:49', 'lgl', '2022-08-03 15:50:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736656637227009', 'DD2022080343', NULL, NULL, '2022-08-03 15:51:19', 'lgl', '2022-08-03 15:51:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736782470541313', 'DD2022080356', NULL, NULL, '2022-08-03 15:51:49', 'lgl', '2022-08-03 15:51:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554736908333215746', 'DD2022080301', NULL, NULL, '2022-08-03 15:52:19', 'lgl', '2022-08-03 15:52:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737034095226881', 'DD2022080351', NULL, NULL, '2022-08-03 15:52:49', 'lgl', '2022-08-03 15:52:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737159957901313', 'DD2022080358', NULL, NULL, '2022-08-03 15:53:19', 'lgl', '2022-08-03 15:53:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737285795409921', 'DD2022080387', NULL, NULL, '2022-08-03 15:53:49', 'lgl', '2022-08-03 15:53:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737411595169794', 'DD2022080380', NULL, NULL, '2022-08-03 15:54:19', 'lgl', '2022-08-03 15:54:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737537420095490', 'DD2022080305', NULL, NULL, '2022-08-03 15:54:49', 'lgl', '2022-08-03 15:54:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737663265992705', 'DD2022080309', NULL, NULL, '2022-08-03 15:55:19', 'lgl', '2022-08-03 15:55:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737789082529793', 'DD2022080307', NULL, NULL, '2022-08-03 15:55:49', 'lgl', '2022-08-03 15:55:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554737914928427010', 'DD2022080333', NULL, NULL, '2022-08-03 15:56:19', 'lgl', '2022-08-03 15:56:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738040778518529', 'DD2022080301', NULL, NULL, '2022-08-03 15:56:49', 'lgl', '2022-08-03 15:56:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738166704107521', 'DD2022080360', NULL, NULL, '2022-08-03 15:57:19', 'lgl', '2022-08-03 15:57:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738292373843970', 'DD2022080397', NULL, NULL, '2022-08-03 15:57:49', 'lgl', '2022-08-03 15:57:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738418253295617', 'DD2022080359', NULL, NULL, '2022-08-03 15:58:19', 'lgl', '2022-08-03 15:58:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738544128552961', 'DD2022080305', NULL, NULL, '2022-08-03 15:58:49', 'lgl', '2022-08-03 15:58:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738669919924226', 'DD2022080326', NULL, NULL, '2022-08-03 15:59:19', 'lgl', '2022-08-03 15:59:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738795732267010', 'DD2022080361', NULL, NULL, '2022-08-03 15:59:49', 'lgl', '2022-08-03 15:59:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554738921548804097', 'DD2022080366', NULL, NULL, '2022-08-03 16:00:19', 'lgl', '2022-08-03 16:00:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739047436644353', 'DD2022080336', NULL, NULL, '2022-08-03 16:00:49', 'lgl', '2022-08-03 16:00:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739173257375745', 'DD2022080379', NULL, NULL, '2022-08-03 16:01:19', 'lgl', '2022-08-03 16:01:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739299048747009', 'DD2022080340', NULL, NULL, '2022-08-03 16:01:49', 'lgl', '2022-08-03 16:01:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739424915615746', 'DD2022080332', NULL, NULL, '2022-08-03 16:02:19', 'lgl', '2022-08-03 16:02:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739550681821185', 'DD2022080311', NULL, NULL, '2022-08-03 16:02:49', 'lgl', '2022-08-03 16:02:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739676519329794', 'DD2022080350', NULL, NULL, '2022-08-03 16:03:19', 'lgl', '2022-08-03 16:03:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739802369421313', 'DD2022080394', NULL, NULL, '2022-08-03 16:03:49', 'lgl', '2022-08-03 16:03:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554739928164986882', 'DD2022080397', NULL, NULL, '2022-08-03 16:04:19', 'lgl', '2022-08-03 16:04:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740053998301186', 'DD2022080329', NULL, NULL, '2022-08-03 16:04:49', 'lgl', '2022-08-03 16:04:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740179865169921', 'DD2022080376', NULL, NULL, '2022-08-03 16:05:19', 'lgl', '2022-08-03 16:05:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740305685901313', 'DD2022080324', NULL, NULL, '2022-08-03 16:05:49', 'lgl', '2022-08-03 16:05:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740431527604225', 'DD2022080367', NULL, NULL, '2022-08-03 16:06:19', 'lgl', '2022-08-03 16:06:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740557310586881', 'DD2022080341', NULL, NULL, '2022-08-03 16:06:49', 'lgl', '2022-08-03 16:06:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740683181649922', 'DD2022080315', NULL, NULL, '2022-08-03 16:07:19', 'lgl', '2022-08-03 16:07:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740809002381314', 'DD2022080320', NULL, NULL, '2022-08-03 16:07:49', 'lgl', '2022-08-03 16:07:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554740934848278530', 'DD2022080397', NULL, NULL, '2022-08-03 16:08:19', 'lgl', '2022-08-03 16:08:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741060648038402', 'DD2022080353', NULL, NULL, '2022-08-03 16:08:49', 'lgl', '2022-08-03 16:08:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741186489741313', 'DD2022080355', NULL, NULL, '2022-08-03 16:09:19', 'lgl', '2022-08-03 16:09:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741312331444226', 'DD2022080381', NULL, NULL, '2022-08-03 16:09:49', 'lgl', '2022-08-03 16:09:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741438156369922', 'DD2022080301', NULL, NULL, '2022-08-03 16:10:19', 'lgl', '2022-08-03 16:10:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741564006461441', 'DD2022080351', NULL, NULL, '2022-08-03 16:10:49', 'lgl', '2022-08-03 16:10:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741689797832705', 'DD2022080363', NULL, NULL, '2022-08-03 16:11:19', 'lgl', '2022-08-03 16:11:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741815681478658', 'DD2022080396', NULL, NULL, '2022-08-03 16:11:49', 'lgl', '2022-08-03 16:11:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554741941464461314', 'DD2022080325', NULL, NULL, '2022-08-03 16:12:19', 'lgl', '2022-08-03 16:12:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742067297775618', 'DD2022080397', NULL, NULL, '2022-08-03 16:12:49', 'lgl', '2022-08-03 16:12:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742193139478530', 'DD2022080317', NULL, NULL, '2022-08-03 16:13:19', 'lgl', '2022-08-03 16:13:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742318964404226', 'DD2022080321', NULL, NULL, '2022-08-03 16:13:49', 'lgl', '2022-08-03 16:13:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742444776747010', 'DD2022080314', NULL, NULL, '2022-08-03 16:14:19', 'lgl', '2022-08-03 16:14:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742570626838529', 'DD2022080347', NULL, NULL, '2022-08-03 16:14:49', 'lgl', '2022-08-03 16:14:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742696380461057', 'DD2022080320', NULL, NULL, '2022-08-03 16:15:19', 'lgl', '2022-08-03 16:15:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742822255718401', 'DD2022080354', NULL, NULL, '2022-08-03 16:15:49', 'lgl', '2022-08-03 16:15:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554742948105809921', 'DD2022080356', NULL, NULL, '2022-08-03 16:16:19', 'lgl', '2022-08-03 16:16:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743073926541314', 'DD2022080378', NULL, NULL, '2022-08-03 16:16:49', 'lgl', '2022-08-03 16:16:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743199722106881', 'DD2022080350', NULL, NULL, '2022-08-03 16:17:19', 'lgl', '2022-08-03 16:17:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743325572198401', 'DD2022080359', NULL, NULL, '2022-08-03 16:17:49', 'lgl', '2022-08-03 16:17:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743451405512706', 'DD2022080346', NULL, NULL, '2022-08-03 16:18:19', 'lgl', '2022-08-03 16:18:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743577259798529', 'DD2022080306', NULL, NULL, '2022-08-03 16:18:49', 'lgl', '2022-08-03 16:18:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743703055364098', 'DD2022080312', NULL, NULL, '2022-08-03 16:19:19', 'lgl', '2022-08-03 16:19:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743828947398658', 'DD2022080327', NULL, NULL, '2022-08-03 16:19:49', 'lgl', '2022-08-03 16:19:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554743954684243969', 'DD2022080337', NULL, NULL, '2022-08-03 16:20:19', 'lgl', '2022-08-03 16:20:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744080546918402', 'DD2022080341', NULL, NULL, '2022-08-03 16:20:49', 'lgl', '2022-08-03 16:20:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744206405398530', 'DD2022080352', NULL, NULL, '2022-08-03 16:21:19', 'lgl', '2022-08-03 16:21:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744332259684354', 'DD2022080365', NULL, NULL, '2022-08-03 16:21:49', 'lgl', '2022-08-03 16:21:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744458076221442', 'DD2022080398', NULL, NULL, '2022-08-03 16:22:19', 'lgl', '2022-08-03 16:22:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744583834038273', 'DD2022080375', NULL, NULL, '2022-08-03 16:22:49', 'lgl', '2022-08-03 16:22:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744709751238657', 'DD2022080363', NULL, NULL, '2022-08-03 16:23:19', 'lgl', '2022-08-03 16:23:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744835538415617', 'DD2022080388', NULL, NULL, '2022-08-03 16:23:49', 'lgl', '2022-08-03 16:23:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554744961380118529', 'DD2022080325', NULL, NULL, '2022-08-03 16:24:19', 'lgl', '2022-08-03 16:24:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745087196655618', 'DD2022080388', NULL, NULL, '2022-08-03 16:24:49', 'lgl', '2022-08-03 16:24:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745213017387009', 'DD2022080366', NULL, NULL, '2022-08-03 16:25:19', 'lgl', '2022-08-03 16:25:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745338821341186', 'DD2022080397', NULL, NULL, '2022-08-03 16:25:49', 'lgl', '2022-08-03 16:25:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745464637878273', 'DD2022080348', NULL, NULL, '2022-08-03 16:26:19', 'lgl', '2022-08-03 16:26:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745590492164098', 'DD2022080344', NULL, NULL, '2022-08-03 16:26:49', 'lgl', '2022-08-03 16:26:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745716388392962', 'DD2022080324', NULL, NULL, '2022-08-03 16:27:19', 'lgl', '2022-08-03 16:27:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745842171375617', 'DD2022080375', NULL, NULL, '2022-08-03 16:27:49', 'lgl', '2022-08-03 16:27:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554745968029855745', 'DD2022080388', NULL, NULL, '2022-08-03 16:28:19', 'lgl', '2022-08-03 16:28:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746093842198530', 'DD2022080383', NULL, NULL, '2022-08-03 16:28:49', 'lgl', '2022-08-03 16:28:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746219637764097', 'DD2022080321', NULL, NULL, '2022-08-03 16:29:19', 'lgl', '2022-08-03 16:29:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746345454301185', 'DD2022080381', NULL, NULL, '2022-08-03 16:29:49', 'lgl', '2022-08-03 16:29:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746471300198402', 'DD2022080341', NULL, NULL, '2022-08-03 16:30:19', 'lgl', '2022-08-03 16:30:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746597129318402', 'DD2022080365', NULL, NULL, '2022-08-03 16:30:49', 'lgl', '2022-08-03 16:30:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746722979409922', 'DD2022080342', NULL, NULL, '2022-08-03 16:31:19', 'lgl', '2022-08-03 16:31:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746848774975489', 'DD2022080334', NULL, NULL, '2022-08-03 16:31:49', 'lgl', '2022-08-03 16:31:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554746974625067010', 'DD2022080334', NULL, NULL, '2022-08-03 16:32:19', 'lgl', '2022-08-03 16:32:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747100429021186', 'DD2022080381', NULL, NULL, '2022-08-03 16:32:49', 'lgl', '2022-08-03 16:32:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747226258141186', 'DD2022080329', NULL, NULL, '2022-08-03 16:33:19', 'lgl', '2022-08-03 16:33:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747352133398529', 'DD2022080364', NULL, NULL, '2022-08-03 16:33:49', 'lgl', '2022-08-03 16:33:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747477912186882', 'DD2022080325', NULL, NULL, '2022-08-03 16:34:19', 'lgl', '2022-08-03 16:34:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747603820998658', 'DD2022080312', NULL, NULL, '2022-08-03 16:34:49', 'lgl', '2022-08-03 16:34:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747729545261058', 'DD2022080354', NULL, NULL, '2022-08-03 16:35:19', 'lgl', '2022-08-03 16:35:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747855433101313', 'DD2022080344', NULL, NULL, '2022-08-03 16:35:49', 'lgl', '2022-08-03 16:35:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554747981237055490', 'DD2022080349', NULL, NULL, '2022-08-03 16:36:19', 'lgl', '2022-08-03 16:36:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748107078758402', 'DD2022080303', NULL, NULL, '2022-08-03 16:36:49', 'lgl', '2022-08-03 16:36:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748232916267010', 'DD2022080321', NULL, NULL, '2022-08-03 16:37:19', 'lgl', '2022-08-03 16:37:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748358774747137', 'DD2022080393', NULL, NULL, '2022-08-03 16:37:49', 'lgl', '2022-08-03 16:37:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748484582895617', 'DD2022080325', NULL, NULL, '2022-08-03 16:38:19', 'lgl', '2022-08-03 16:38:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748610403627009', 'DD2022080368', NULL, NULL, '2022-08-03 16:38:49', 'lgl', '2022-08-03 16:38:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748736241135618', 'DD2022080311', NULL, NULL, '2022-08-03 16:39:19', 'lgl', '2022-08-03 16:39:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748862045089794', 'DD2022080385', NULL, NULL, '2022-08-03 16:39:49', 'lgl', '2022-08-03 16:39:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554748987861626881', 'DD2022080331', NULL, NULL, '2022-08-03 16:40:19', 'lgl', '2022-08-03 16:40:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749113745272833', 'DD2022080385', NULL, NULL, '2022-08-03 16:40:49', 'lgl', '2022-08-03 16:40:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749239549227009', 'DD2022080321', NULL, NULL, '2022-08-03 16:41:19', 'lgl', '2022-08-03 16:41:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749365390929922', 'DD2022080314', NULL, NULL, '2022-08-03 16:41:49', 'lgl', '2022-08-03 16:41:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749491186495489', 'DD2022080320', NULL, NULL, '2022-08-03 16:42:19', 'lgl', '2022-08-03 16:42:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749617078530050', 'DD2022080380', NULL, NULL, '2022-08-03 16:42:49', 'lgl', '2022-08-03 16:42:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749742857318402', 'DD2022080320', NULL, NULL, '2022-08-03 16:43:19', 'lgl', '2022-08-03 16:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749868719992833', 'DD2022080379', NULL, NULL, '2022-08-03 16:43:49', 'lgl', '2022-08-03 16:43:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554749994532335617', 'DD2022080306', NULL, NULL, '2022-08-03 16:44:19', 'lgl', '2022-08-03 16:44:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750120369844226', 'DD2022080384', NULL, NULL, '2022-08-03 16:44:49', 'lgl', '2022-08-03 16:44:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750246169604098', 'DD2022080326', NULL, NULL, '2022-08-03 16:45:19', 'lgl', '2022-08-03 16:45:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750372011307009', 'DD2022080344', NULL, NULL, '2022-08-03 16:45:49', 'lgl', '2022-08-03 16:45:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750497840427010', 'DD2022080362', NULL, NULL, '2022-08-03 16:46:19', 'lgl', '2022-08-03 16:46:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750623665352705', 'DD2022080318', NULL, NULL, '2022-08-03 16:46:49', 'lgl', '2022-08-03 16:46:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750749926486017', 'DD2022080397', NULL, NULL, '2022-08-03 16:47:19', 'lgl', '2022-08-03 16:47:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554750875306815489', 'DD2022080399', NULL, NULL, '2022-08-03 16:47:49', 'lgl', '2022-08-03 16:47:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751001140129794', 'DD2022080342', NULL, NULL, '2022-08-03 16:48:19', 'lgl', '2022-08-03 16:48:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751126986027009', 'DD2022080354', NULL, NULL, '2022-08-03 16:48:49', 'lgl', '2022-08-03 16:48:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751252831924225', 'DD2022080304', NULL, NULL, '2022-08-03 16:49:19', 'lgl', '2022-08-03 16:49:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751378707181570', 'DD2022080371', NULL, NULL, '2022-08-03 16:49:49', 'lgl', '2022-08-03 16:49:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751504490164225', 'DD2022080371', NULL, NULL, '2022-08-03 16:50:19', 'lgl', '2022-08-03 16:50:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751630344450050', 'DD2022080311', NULL, NULL, '2022-08-03 16:50:49', 'lgl', '2022-08-03 16:50:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751756144209921', 'DD2022080309', NULL, NULL, '2022-08-03 16:51:19', 'lgl', '2022-08-03 16:51:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554751881935581185', 'DD2022080389', NULL, NULL, '2022-08-03 16:51:49', 'lgl', '2022-08-03 16:51:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752007777284097', 'DD2022080379', NULL, NULL, '2022-08-03 16:52:19', 'lgl', '2022-08-03 16:52:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752133610598401', 'DD2022080351', NULL, NULL, '2022-08-03 16:52:49', 'lgl', '2022-08-03 16:52:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752259410358274', 'DD2022080376', NULL, NULL, '2022-08-03 16:53:19', 'lgl', '2022-08-03 16:53:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752385247866882', 'DD2022080392', NULL, NULL, '2022-08-03 16:53:49', 'lgl', '2022-08-03 16:53:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752511127318529', 'DD2022080342', NULL, NULL, '2022-08-03 16:54:19', 'lgl', '2022-08-03 16:54:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752636935467009', 'DD2022080388', NULL, NULL, '2022-08-03 16:54:49', 'lgl', '2022-08-03 16:54:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752762760392705', 'DD2022080306', NULL, NULL, '2022-08-03 16:55:19', 'lgl', '2022-08-03 16:55:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554752888652427266', 'DD2022080380', NULL, NULL, '2022-08-03 16:55:49', 'lgl', '2022-08-03 16:55:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753014406049793', 'DD2022080328', NULL, NULL, '2022-08-03 16:56:19', 'lgl', '2022-08-03 16:56:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753140247752705', 'DD2022080329', NULL, NULL, '2022-08-03 16:56:49', 'lgl', '2022-08-03 16:56:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753266047512577', 'DD2022080350', NULL, NULL, '2022-08-03 16:57:19', 'lgl', '2022-08-03 16:57:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753391926964226', 'DD2022080398', NULL, NULL, '2022-08-03 16:57:49', 'lgl', '2022-08-03 16:57:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753517684781058', 'DD2022080352', NULL, NULL, '2022-08-03 16:58:19', 'lgl', '2022-08-03 16:58:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753643589398529', 'DD2022080358', NULL, NULL, '2022-08-03 16:58:49', 'lgl', '2022-08-03 16:58:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753769363992577', 'DD2022080320', NULL, NULL, '2022-08-03 16:59:19', 'lgl', '2022-08-03 16:59:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554753895264415746', 'DD2022080335', NULL, NULL, '2022-08-03 16:59:49', 'lgl', '2022-08-03 16:59:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754021101924354', 'DD2022080388', NULL, NULL, '2022-08-03 17:00:19', 'lgl', '2022-08-03 17:00:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754146893295618', 'DD2022080383', NULL, NULL, '2022-08-03 17:00:49', 'lgl', '2022-08-03 17:00:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754272701444097', 'DD2022080349', NULL, NULL, '2022-08-03 17:01:19', 'lgl', '2022-08-03 17:01:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754398576701442', 'DD2022080371', NULL, NULL, '2022-08-03 17:01:49', 'lgl', '2022-08-03 17:01:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754524326129665', 'DD2022080319', NULL, NULL, '2022-08-03 17:02:19', 'lgl', '2022-08-03 17:02:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754650197192705', 'DD2022080371', NULL, NULL, '2022-08-03 17:02:49', 'lgl', '2022-08-03 17:02:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754775984369666', 'DD2022080324', NULL, NULL, '2022-08-03 17:03:19', 'lgl', '2022-08-03 17:03:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554754901880598529', 'DD2022080334', NULL, NULL, '2022-08-03 17:03:49', 'lgl', '2022-08-03 17:03:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755027676164098', 'DD2022080393', NULL, NULL, '2022-08-03 17:04:19', 'lgl', '2022-08-03 17:04:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755153555615746', 'DD2022080328', NULL, NULL, '2022-08-03 17:04:49', 'lgl', '2022-08-03 17:04:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755279313432577', 'DD2022080391', NULL, NULL, '2022-08-03 17:05:19', 'lgl', '2022-08-03 17:05:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755405205467138', 'DD2022080354', NULL, NULL, '2022-08-03 17:05:49', 'lgl', '2022-08-03 17:05:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755530967478273', 'DD2022080347', NULL, NULL, '2022-08-03 17:06:19', 'lgl', '2022-08-03 17:06:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755656872095746', 'DD2022080346', NULL, NULL, '2022-08-03 17:06:49', 'lgl', '2022-08-03 17:06:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755782650884097', 'DD2022080308', NULL, NULL, '2022-08-03 17:07:19', 'lgl', '2022-08-03 17:07:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554755908509364225', 'DD2022080320', NULL, NULL, '2022-08-03 17:07:49', 'lgl', '2022-08-03 17:07:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756034355261442', 'DD2022080302', NULL, NULL, '2022-08-03 17:08:19', 'lgl', '2022-08-03 17:08:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756160146632706', 'DD2022080390', NULL, NULL, '2022-08-03 17:08:49', 'lgl', '2022-08-03 17:08:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756285979947009', 'DD2022080302', NULL, NULL, '2022-08-03 17:09:19', 'lgl', '2022-08-03 17:09:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756411775512578', 'DD2022080320', NULL, NULL, '2022-08-03 17:09:49', 'lgl', '2022-08-03 17:09:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756537617215489', 'DD2022080316', NULL, NULL, '2022-08-03 17:10:19', 'lgl', '2022-08-03 17:10:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756663446335489', 'DD2022080392', NULL, NULL, '2022-08-03 17:10:49', 'lgl', '2022-08-03 17:10:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756789292232706', 'DD2022080333', NULL, NULL, '2022-08-03 17:11:19', 'lgl', '2022-08-03 17:11:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554756915112964097', 'DD2022080371', NULL, NULL, '2022-08-03 17:11:49', 'lgl', '2022-08-03 17:11:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757041340542977', 'DD2022080343', NULL, NULL, '2022-08-03 17:12:19', 'lgl', '2022-08-03 17:12:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757166800564226', 'DD2022080331', NULL, NULL, '2022-08-03 17:12:49', 'lgl', '2022-08-03 17:12:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757292642267138', 'DD2022080347', NULL, NULL, '2022-08-03 17:13:19', 'lgl', '2022-08-03 17:13:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757418429444097', 'DD2022080331', NULL, NULL, '2022-08-03 17:13:49', 'lgl', '2022-08-03 17:13:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757544266952706', 'DD2022080374', NULL, NULL, '2022-08-03 17:14:19', 'lgl', '2022-08-03 17:14:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757670112849922', 'DD2022080320', NULL, NULL, '2022-08-03 17:14:49', 'lgl', '2022-08-03 17:14:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757795920998402', 'DD2022080390', NULL, NULL, '2022-08-03 17:15:19', 'lgl', '2022-08-03 17:15:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554757921787867137', 'DD2022080369', NULL, NULL, '2022-08-03 17:15:49', 'lgl', '2022-08-03 17:15:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758047579238402', 'DD2022080353', NULL, NULL, '2022-08-03 17:16:19', 'lgl', '2022-08-03 17:16:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758173433524226', 'DD2022080376', NULL, NULL, '2022-08-03 17:16:49', 'lgl', '2022-08-03 17:16:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758299149398017', 'DD2022080347', NULL, NULL, '2022-08-03 17:17:19', 'lgl', '2022-08-03 17:17:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758425125318657', 'DD2022080354', NULL, NULL, '2022-08-03 17:17:49', 'lgl', '2022-08-03 17:17:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758550946050050', 'DD2022080377', NULL, NULL, '2022-08-03 17:18:19', 'lgl', '2022-08-03 17:18:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758676733227010', 'DD2022080350', NULL, NULL, '2022-08-03 17:18:49', 'lgl', '2022-08-03 17:18:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758802528792577', 'DD2022080371', NULL, NULL, '2022-08-03 17:19:19', 'lgl', '2022-08-03 17:19:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554758928408244225', 'DD2022080391', NULL, NULL, '2022-08-03 17:19:49', 'lgl', '2022-08-03 17:19:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759054191226881', 'DD2022080384', NULL, NULL, '2022-08-03 17:20:19', 'lgl', '2022-08-03 17:20:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759180079067138', 'DD2022080367', NULL, NULL, '2022-08-03 17:20:49', 'lgl', '2022-08-03 17:20:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759305866244097', 'DD2022080388', NULL, NULL, '2022-08-03 17:21:19', 'lgl', '2022-08-03 17:21:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759431691169793', 'DD2022080383', NULL, NULL, '2022-08-03 17:21:49', 'lgl', '2022-08-03 17:21:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759557537067009', 'DD2022080324', NULL, NULL, '2022-08-03 17:22:19', 'lgl', '2022-08-03 17:22:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759683387158529', 'DD2022080312', NULL, NULL, '2022-08-03 17:22:49', 'lgl', '2022-08-03 17:22:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759809170141186', 'DD2022080356', NULL, NULL, '2022-08-03 17:23:19', 'lgl', '2022-08-03 17:23:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554759935045398529', 'DD2022080380', NULL, NULL, '2022-08-03 17:23:49', 'lgl', '2022-08-03 17:23:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760060857741314', 'DD2022080384', NULL, NULL, '2022-08-03 17:24:19', 'lgl', '2022-08-03 17:24:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760186691055617', 'DD2022080354', NULL, NULL, '2022-08-03 17:24:49', 'lgl', '2022-08-03 17:24:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760312528564226', 'DD2022080397', NULL, NULL, '2022-08-03 17:25:19', 'lgl', '2022-08-03 17:25:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760438332518402', 'DD2022080338', NULL, NULL, '2022-08-03 17:25:49', 'lgl', '2022-08-03 17:25:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760564207775746', 'DD2022080326', NULL, NULL, '2022-08-03 17:26:19', 'lgl', '2022-08-03 17:26:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760689973981185', 'DD2022080352', NULL, NULL, '2022-08-03 17:26:49', 'lgl', '2022-08-03 17:26:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760815811489793', 'DD2022080316', NULL, NULL, '2022-08-03 17:27:19', 'lgl', '2022-08-03 17:27:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554760941695135745', 'DD2022080345', NULL, NULL, '2022-08-03 17:27:49', 'lgl', '2022-08-03 17:27:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761067520061442', 'DD2022080394', NULL, NULL, '2022-08-03 17:28:19', 'lgl', '2022-08-03 17:28:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761193286266882', 'DD2022080325', NULL, NULL, '2022-08-03 17:28:49', 'lgl', '2022-08-03 17:28:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761319161524225', 'DD2022080368', NULL, NULL, '2022-08-03 17:29:19', 'lgl', '2022-08-03 17:29:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761444969672705', 'DD2022080328', NULL, NULL, '2022-08-03 17:29:49', 'lgl', '2022-08-03 17:29:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761570807181313', 'DD2022080380', NULL, NULL, '2022-08-03 17:30:19', 'lgl', '2022-08-03 17:30:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761696577581058', 'DD2022080342', NULL, NULL, '2022-08-03 17:30:49', 'lgl', '2022-08-03 17:30:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761822503170049', 'DD2022080310', NULL, NULL, '2022-08-03 17:31:19', 'lgl', '2022-08-03 17:31:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554761948328095745', 'DD2022080307', NULL, NULL, '2022-08-03 17:31:49', 'lgl', '2022-08-03 17:31:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762074085912578', 'DD2022080359', NULL, NULL, '2022-08-03 17:32:19', 'lgl', '2022-08-03 17:32:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762199977947138', 'DD2022080337', NULL, NULL, '2022-08-03 17:32:49', 'lgl', '2022-08-03 17:32:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762325786095617', 'DD2022080320', NULL, NULL, '2022-08-03 17:33:19', 'lgl', '2022-08-03 17:33:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762451585855489', 'DD2022080348', NULL, NULL, '2022-08-03 17:33:49', 'lgl', '2022-08-03 17:33:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762577427558401', 'DD2022080317', NULL, NULL, '2022-08-03 17:34:19', 'lgl', '2022-08-03 17:34:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762703734829058', 'DD2022080358', NULL, NULL, '2022-08-03 17:34:49', 'lgl', '2022-08-03 17:34:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762829102575618', 'DD2022080376', NULL, NULL, '2022-08-03 17:35:19', 'lgl', '2022-08-03 17:35:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554762954889752577', 'DD2022080300', NULL, NULL, '2022-08-03 17:35:49', 'lgl', '2022-08-03 17:35:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763080731455489', 'DD2022080353', NULL, NULL, '2022-08-03 17:36:19', 'lgl', '2022-08-03 17:36:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763206610907138', 'DD2022080306', NULL, NULL, '2022-08-03 17:36:49', 'lgl', '2022-08-03 17:36:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763332452610050', 'DD2022080393', NULL, NULL, '2022-08-03 17:37:19', 'lgl', '2022-08-03 17:37:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763458243981313', 'DD2022080377', NULL, NULL, '2022-08-03 17:37:49', 'lgl', '2022-08-03 17:37:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763584089878529', 'DD2022080382', NULL, NULL, '2022-08-03 17:38:19', 'lgl', '2022-08-03 17:38:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763709944164353', 'DD2022080321', NULL, NULL, '2022-08-03 17:38:49', 'lgl', '2022-08-03 17:38:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763835735535617', 'DD2022080301', NULL, NULL, '2022-08-03 17:39:19', 'lgl', '2022-08-03 17:39:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554763961522712578', 'DD2022080394', NULL, NULL, '2022-08-03 17:39:49', 'lgl', '2022-08-03 17:39:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764087364415490', 'DD2022080355', NULL, NULL, '2022-08-03 17:40:19', 'lgl', '2022-08-03 17:40:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764213235478530', 'DD2022080374', NULL, NULL, '2022-08-03 17:40:49', 'lgl', '2022-08-03 17:40:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764339035238402', 'DD2022080377', NULL, NULL, '2022-08-03 17:41:19', 'lgl', '2022-08-03 17:41:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764464876941314', 'DD2022080314', NULL, NULL, '2022-08-03 17:41:49', 'lgl', '2022-08-03 17:41:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764590710255618', 'DD2022080356', NULL, NULL, '2022-08-03 17:42:19', 'lgl', '2022-08-03 17:42:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554764716539375617', 'DD2022080335', NULL, NULL, '2022-08-03 17:42:49', 'lgl', '2022-08-03 17:42:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554827407198969858', 'DD2022080353', NULL, NULL, '2022-08-03 21:51:56', 'lgl', '2022-08-03 21:51:56', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554827530188546050', 'DD2022080306', NULL, NULL, '2022-08-03 21:52:25', 'lgl', '2022-08-03 21:52:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554827655996694530', 'DD2022080341', NULL, NULL, '2022-08-03 21:52:55', 'lgl', '2022-08-03 21:52:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554827781876146177', 'DD2022080365', NULL, NULL, '2022-08-03 21:53:25', 'lgl', '2022-08-03 21:53:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554827907713654786', 'DD2022080369', NULL, NULL, '2022-08-03 21:53:55', 'lgl', '2022-08-03 21:53:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828033530191873', 'DD2022080394', NULL, NULL, '2022-08-03 21:54:25', 'lgl', '2022-08-03 21:54:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828159329951746', 'DD2022080350', NULL, NULL, '2022-08-03 21:54:55', 'lgl', '2022-08-03 21:54:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828285121323010', 'DD2022080303', NULL, NULL, '2022-08-03 21:55:25', 'lgl', '2022-08-03 21:55:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828410958831617', 'DD2022080320', NULL, NULL, '2022-08-03 21:55:55', 'lgl', '2022-08-03 21:55:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828536800534529', 'DD2022080326', NULL, NULL, '2022-08-03 21:56:25', 'lgl', '2022-08-03 21:56:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828662659014657', 'DD2022080353', NULL, NULL, '2022-08-03 21:56:55', 'lgl', '2022-08-03 21:56:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828788517494786', 'DD2022080395', NULL, NULL, '2022-08-03 21:57:25', 'lgl', '2022-08-03 21:57:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554828914275311618', 'DD2022080376', NULL, NULL, '2022-08-03 21:57:55', 'lgl', '2022-08-03 21:57:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829040121208833', 'DD2022080379', NULL, NULL, '2022-08-03 21:58:25', 'lgl', '2022-08-03 21:58:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829165937745921', 'DD2022080309', NULL, NULL, '2022-08-03 21:58:55', 'lgl', '2022-08-03 21:58:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829291808808962', 'DD2022080341', NULL, NULL, '2022-08-03 21:59:25', 'lgl', '2022-08-03 21:59:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829417625346049', 'DD2022080335', NULL, NULL, '2022-08-03 21:59:55', 'lgl', '2022-08-03 21:59:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829543437688833', 'DD2022080359', NULL, NULL, '2022-08-03 22:00:25', 'lgl', '2022-08-03 22:00:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829669346500609', 'DD2022080334', NULL, NULL, '2022-08-03 22:00:55', 'lgl', '2022-08-03 22:00:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829795163037697', 'DD2022080317', NULL, NULL, '2022-08-03 22:01:25', 'lgl', '2022-08-03 22:01:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554829920925048833', 'DD2022080338', NULL, NULL, '2022-08-03 22:01:55', 'lgl', '2022-08-03 22:01:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830046791917569', 'DD2022080356', NULL, NULL, '2022-08-03 22:02:25', 'lgl', '2022-08-03 22:02:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830172633620481', 'DD2022080379', NULL, NULL, '2022-08-03 22:02:55', 'lgl', '2022-08-03 22:02:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830298424991745', 'DD2022080372', NULL, NULL, '2022-08-03 22:03:25', 'lgl', '2022-08-03 22:03:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830424270888961', 'DD2022080300', NULL, NULL, '2022-08-03 22:03:55', 'lgl', '2022-08-03 22:03:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830550100008961', 'DD2022080373', NULL, NULL, '2022-08-03 22:04:25', 'lgl', '2022-08-03 22:04:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830675929128961', 'DD2022080353', NULL, NULL, '2022-08-03 22:04:55', 'lgl', '2022-08-03 22:04:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830801720500225', 'DD2022080351', NULL, NULL, '2022-08-03 22:05:25', 'lgl', '2022-08-03 22:05:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554830927558008833', 'DD2022080340', NULL, NULL, '2022-08-03 22:05:55', 'lgl', '2022-08-03 22:05:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831053416488962', 'DD2022080388', NULL, NULL, '2022-08-03 22:06:25', 'lgl', '2022-08-03 22:06:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831179216248833', 'DD2022080308', NULL, NULL, '2022-08-03 22:06:55', 'lgl', '2022-08-03 22:06:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831305036980225', 'DD2022080304', NULL, NULL, '2022-08-03 22:07:25', 'lgl', '2022-08-03 22:07:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831430899654658', 'DD2022080310', NULL, NULL, '2022-08-03 22:07:55', 'lgl', '2022-08-03 22:07:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831556745551873', 'DD2022080388', NULL, NULL, '2022-08-03 22:08:25', 'lgl', '2022-08-03 22:08:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831682583060481', 'DD2022080325', NULL, NULL, '2022-08-03 22:08:55', 'lgl', '2022-08-03 22:08:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831808374431746', 'DD2022080330', NULL, NULL, '2022-08-03 22:09:25', 'lgl', '2022-08-03 22:09:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554831934186774530', 'DD2022080377', NULL, NULL, '2022-08-03 22:09:55', 'lgl', '2022-08-03 22:09:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832060062031873', 'DD2022080319', NULL, NULL, '2022-08-03 22:10:25', 'lgl', '2022-08-03 22:10:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832185865986049', 'DD2022080356', NULL, NULL, '2022-08-03 22:10:55', 'lgl', '2022-08-03 22:10:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832311703494658', 'DD2022080389', NULL, NULL, '2022-08-03 22:11:25', 'lgl', '2022-08-03 22:11:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832437549391873', 'DD2022080346', NULL, NULL, '2022-08-03 22:11:55', 'lgl', '2022-08-03 22:11:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832563344957442', 'DD2022080347', NULL, NULL, '2022-08-03 22:12:25', 'lgl', '2022-08-03 22:12:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832689165688833', 'DD2022080351', NULL, NULL, '2022-08-03 22:12:55', 'lgl', '2022-08-03 22:12:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832815019974657', 'DD2022080327', NULL, NULL, '2022-08-03 22:13:25', 'lgl', '2022-08-03 22:13:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554832940840706049', 'DD2022080311', NULL, NULL, '2022-08-03 22:13:55', 'lgl', '2022-08-03 22:13:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833066669826050', 'DD2022080364', NULL, NULL, '2022-08-03 22:14:25', 'lgl', '2022-08-03 22:14:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833192486363137', 'DD2022080342', NULL, NULL, '2022-08-03 22:14:55', 'lgl', '2022-08-03 22:14:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833318357426178', 'DD2022080311', NULL, NULL, '2022-08-03 22:15:25', 'lgl', '2022-08-03 22:15:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833444211712002', 'DD2022080377', NULL, NULL, '2022-08-03 22:15:55', 'lgl', '2022-08-03 22:15:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833569998888961', 'DD2022080365', NULL, NULL, '2022-08-03 22:16:25', 'lgl', '2022-08-03 22:16:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833695865757697', 'DD2022080383', NULL, NULL, '2022-08-03 22:16:55', 'lgl', '2022-08-03 22:16:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833821669711873', 'DD2022080305', NULL, NULL, '2022-08-03 22:17:25', 'lgl', '2022-08-03 22:17:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554833947494637570', 'DD2022080351', NULL, NULL, '2022-08-03 22:17:55', 'lgl', '2022-08-03 22:17:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834073286008833', 'DD2022080324', NULL, NULL, '2022-08-03 22:18:25', 'lgl', '2022-08-03 22:18:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834199102545922', 'DD2022080307', NULL, NULL, '2022-08-03 22:18:55', 'lgl', '2022-08-03 22:18:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834324977803266', 'DD2022080397', NULL, NULL, '2022-08-03 22:19:25', 'lgl', '2022-08-03 22:19:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834450790146050', 'DD2022080384', NULL, NULL, '2022-08-03 22:19:55', 'lgl', '2022-08-03 22:19:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834576615071746', 'DD2022080358', NULL, NULL, '2022-08-03 22:20:25', 'lgl', '2022-08-03 22:20:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834702427414529', 'DD2022080320', NULL, NULL, '2022-08-03 22:20:55', 'lgl', '2022-08-03 22:20:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834828281700353', 'DD2022080330', NULL, NULL, '2022-08-03 22:21:25', 'lgl', '2022-08-03 22:21:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554834954135986177', 'DD2022080363', NULL, NULL, '2022-08-03 22:21:55', 'lgl', '2022-08-03 22:21:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835079889608706', 'DD2022080321', NULL, NULL, '2022-08-03 22:22:25', 'lgl', '2022-08-03 22:22:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835205794226178', 'DD2022080363', NULL, NULL, '2022-08-03 22:22:55', 'lgl', '2022-08-03 22:22:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835331593986049', 'DD2022080394', NULL, NULL, '2022-08-03 22:23:25', 'lgl', '2022-08-03 22:23:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835457448271874', 'DD2022080337', NULL, NULL, '2022-08-03 22:23:55', 'lgl', '2022-08-03 22:23:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835583231254529', 'DD2022080337', NULL, NULL, '2022-08-03 22:24:25', 'lgl', '2022-08-03 22:24:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835709064568833', 'DD2022080343', NULL, NULL, '2022-08-03 22:24:55', 'lgl', '2022-08-03 22:24:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835834939826177', 'DD2022080327', NULL, NULL, '2022-08-03 22:25:25', 'lgl', '2022-08-03 22:25:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554835960756363265', 'DD2022080372', NULL, NULL, '2022-08-03 22:25:55', 'lgl', '2022-08-03 22:25:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836086614843394', 'DD2022080344', NULL, NULL, '2022-08-03 22:26:25', 'lgl', '2022-08-03 22:26:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836212381048834', 'DD2022080384', NULL, NULL, '2022-08-03 22:26:55', 'lgl', '2022-08-03 22:26:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836338243723265', 'DD2022080374', NULL, NULL, '2022-08-03 22:27:25', 'lgl', '2022-08-03 22:27:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836464026705922', 'DD2022080331', NULL, NULL, '2022-08-03 22:27:55', 'lgl', '2022-08-03 22:27:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836589927129089', 'DD2022080388', NULL, NULL, '2022-08-03 22:28:25', 'lgl', '2022-08-03 22:28:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836715668168705', 'DD2022080356', NULL, NULL, '2022-08-03 22:28:55', 'lgl', '2022-08-03 22:28:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836841564397570', 'DD2022080342', NULL, NULL, '2022-08-03 22:29:25', 'lgl', '2022-08-03 22:29:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554836967385128962', 'DD2022080354', NULL, NULL, '2022-08-03 22:29:55', 'lgl', '2022-08-03 22:29:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837093222637569', 'DD2022080386', NULL, NULL, '2022-08-03 22:30:25', 'lgl', '2022-08-03 22:30:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837219055951874', 'DD2022080343', NULL, NULL, '2022-08-03 22:30:55', 'lgl', '2022-08-03 22:30:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837344893460481', 'DD2022080322', NULL, NULL, '2022-08-03 22:31:25', 'lgl', '2022-08-03 22:31:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837470718386177', 'DD2022080331', NULL, NULL, '2022-08-03 22:31:55', 'lgl', '2022-08-03 22:31:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837596555894786', 'DD2022080362', NULL, NULL, '2022-08-03 22:32:25', 'lgl', '2022-08-03 22:32:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837722380820481', 'DD2022080333', NULL, NULL, '2022-08-03 22:32:55', 'lgl', '2022-08-03 22:32:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837848214134786', 'DD2022080307', NULL, NULL, '2022-08-03 22:33:25', 'lgl', '2022-08-03 22:33:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554837974005506049', 'DD2022080348', NULL, NULL, '2022-08-03 22:33:55', 'lgl', '2022-08-03 22:33:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838099847208962', 'DD2022080347', NULL, NULL, '2022-08-03 22:34:25', 'lgl', '2022-08-03 22:34:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838225659551745', 'DD2022080387', NULL, NULL, '2022-08-03 22:34:55', 'lgl', '2022-08-03 22:34:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838351530614786', 'DD2022080329', NULL, NULL, '2022-08-03 22:35:25', 'lgl', '2022-08-03 22:35:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838477330374658', 'DD2022080308', NULL, NULL, '2022-08-03 22:35:55', 'lgl', '2022-08-03 22:35:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838603197243394', 'DD2022080387', NULL, NULL, '2022-08-03 22:36:25', 'lgl', '2022-08-03 22:36:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838728946671618', 'DD2022080329', NULL, NULL, '2022-08-03 22:36:55', 'lgl', '2022-08-03 22:36:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838854855483394', 'DD2022080374', NULL, NULL, '2022-08-03 22:37:25', 'lgl', '2022-08-03 22:37:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554838980646854657', 'DD2022080351', NULL, NULL, '2022-08-03 22:37:55', 'lgl', '2022-08-03 22:37:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839106501140482', 'DD2022080375', NULL, NULL, '2022-08-03 22:38:25', 'lgl', '2022-08-03 22:38:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839232284123137', 'DD2022080381', NULL, NULL, '2022-08-03 22:38:55', 'lgl', '2022-08-03 22:38:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839358121631746', 'DD2022080352', NULL, NULL, '2022-08-03 22:39:25', 'lgl', '2022-08-03 22:39:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839483925585921', 'DD2022080374', NULL, NULL, '2022-08-03 22:39:55', 'lgl', '2022-08-03 22:39:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839609826009089', 'DD2022080395', NULL, NULL, '2022-08-03 22:40:25', 'lgl', '2022-08-03 22:40:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839735634157570', 'DD2022080318', NULL, NULL, '2022-08-03 22:40:55', 'lgl', '2022-08-03 22:40:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839861463277570', 'DD2022080372', NULL, NULL, '2022-08-03 22:41:25', 'lgl', '2022-08-03 22:41:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554839987258843137', 'DD2022080315', NULL, NULL, '2022-08-03 22:41:55', 'lgl', '2022-08-03 22:41:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840113046020097', 'DD2022080396', NULL, NULL, '2022-08-03 22:42:25', 'lgl', '2022-08-03 22:42:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840238942248962', 'DD2022080394', NULL, NULL, '2022-08-03 22:42:55', 'lgl', '2022-08-03 22:42:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840364771368962', 'DD2022080391', NULL, NULL, '2022-08-03 22:43:25', 'lgl', '2022-08-03 22:43:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840490642432001', 'DD2022080311', NULL, NULL, '2022-08-03 22:43:55', 'lgl', '2022-08-03 22:43:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840616450580481', 'DD2022080396', NULL, NULL, '2022-08-03 22:44:25', 'lgl', '2022-08-03 22:44:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840742267117569', 'DD2022080358', NULL, NULL, '2022-08-03 22:44:55', 'lgl', '2022-08-03 22:44:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840868075266050', 'DD2022080301', NULL, NULL, '2022-08-03 22:45:25', 'lgl', '2022-08-03 22:45:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554840993887608834', 'DD2022080369', NULL, NULL, '2022-08-03 22:45:55', 'lgl', '2022-08-03 22:45:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841119754477569', 'DD2022080379', NULL, NULL, '2022-08-03 22:46:25', 'lgl', '2022-08-03 22:46:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841245587791873', 'DD2022080396', NULL, NULL, '2022-08-03 22:46:55', 'lgl', '2022-08-03 22:46:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841371391746050', 'DD2022080393', NULL, NULL, '2022-08-03 22:47:25', 'lgl', '2022-08-03 22:47:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841497233448962', 'DD2022080370', NULL, NULL, '2022-08-03 22:47:55', 'lgl', '2022-08-03 22:47:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841623066763266', 'DD2022080392', NULL, NULL, '2022-08-03 22:48:25', 'lgl', '2022-08-03 22:48:25', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554841748887494658', 'DD2022080351', NULL, NULL, '2022-08-03 22:48:55', 'lgl', '2022-08-03 22:48:55', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843714317377538', 'DD2022080334', NULL, NULL, '2022-08-03 22:56:44', 'lgl', '2022-08-03 22:56:44', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843744726081537', 'DD2022080377', NULL, NULL, '2022-08-03 22:56:51', 'lgl', '2022-08-03 22:56:51', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843768142880770', 'DD2022080391', NULL, NULL, '2022-08-03 22:56:57', 'lgl', '2022-08-03 22:56:57', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843801110110210', 'DD2022080349', NULL, NULL, '2022-08-03 22:57:04', 'lgl', '2022-08-03 22:57:04', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843817182683138', 'DD2022080386', NULL, NULL, '2022-08-03 22:57:08', 'lgl', '2022-08-03 22:57:08', 'lgl', NULL, NULL, 'DD2022070202', 'OK', 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554843849277497346', 'DD2022080397', '', '', '2022-08-03 22:57:16', 'lgl', '2022-08-04 22:38:31', 'admin', '', '', 'DD2022070202', 'OK', 1, '0', '1277176874674663425', 'A01装配工序->测试工序');

-- ----------------------------
-- Table structure for sp_sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_department`;
CREATE TABLE `sp_sys_department`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sort_num` int NOT NULL,
  `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_department
-- ----------------------------

-- ----------------------------
-- Table structure for sp_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_dict`;
CREATE TABLE `sp_sys_dict`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据值',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '描述',
  `sort_num` int NOT NULL COMMENT '排序（升序）',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '父级id',
  `is_deleted` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sp_sys_dict_name`(`type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_dict
-- ----------------------------
INSERT INTO `sp_sys_dict` VALUES ('1337618042191904', '成品', 'FG', 'material_type', '物料类型', 2, '\"\"', '0', '2020-03-18 13:53:06', 'admin', '2020-03-18 13:53:06', 'admin');
INSERT INTO `sp_sys_dict` VALUES ('1337618163826720', '半成品', 'PG', 'material_type', '物料类型', 3, '\"\"', '0', '2020-03-18 13:54:04', 'admin', '2020-03-18 13:54:04', 'admin');
INSERT INTO `sp_sys_dict` VALUES ('1337618837012512', '个', 'PCS', 'ORDER_UNIT', '生产单位', 1, '\"\"', '0', '2020-03-18 13:59:25', 'admin', '2020-03-18 13:59:41', 'admin');
INSERT INTO `sp_sys_dict` VALUES ('1337618939772960', '箱', 'BOX', 'ORDER_UNIT', '生产单位', 2, '\"\"', '0', '2020-03-18 14:00:14', 'admin', '2020-03-18 14:00:14', 'admin');

-- ----------------------------
-- Table structure for sp_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_menu`;
CREATE TABLE `sp_sys_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单URL',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父菜单ID，一级菜单设为0',
  `grade` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '层级：1级、2级、3级......',
  `sort_num` int NOT NULL COMMENT '排序',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型：0 目录；1 菜单；2 按钮',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '授权(多个用逗号分隔，如：sys:menu:list,sys:menu:create)',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '菜单图标',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sp_sys_menu_name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `idx_sp_sys_menu_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_menu
-- ----------------------------
INSERT INTO `sp_sys_menu` VALUES ('10', 'system', '系统管理', '#', '7', '2', 1, '0', 'user:add', 'fa fa-gears', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('101', 'menu', '菜单管理', '/admin/sys/menu/list-ui', '10', '3', 1, '0', 'user:add', 'fa fa-bars', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('102', 'user', '用户管理', '/admin/sys/user/list-ui', '10', '3', 2, '0', 'user:add', 'fa fa-user', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('103', 'role', '角色管理', '/admin/sys/role/list-ui', '10', '3', 3, '0', 'user:add', 'fa fa-child', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('104', 'department', '部门管理', '/admin/sys/department/list-ui', '10', '3', 4, '0', 'user:add', 'fa fa-sitemap', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('105', 'basedata', '基础数据配置平台', '/basedata/manager/list-ui', '10', '3', 5, '0', 'user:add', 'fa fa-cog', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('106', 'basedatamanager', '基础数据维护', '/basedata/manager/item/list-ui', '10', '3', 6, '0', 'user:add', 'fa fa-database', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('12', 'order', '计划管理', '', '7', '2', 4, '0', 'user:add', 'fa fa-calendar', '', '2022-06-29 11:18:29', 'dreamer', '2021-02-21 14:59:56', 'admin');
INSERT INTO `sp_sys_menu` VALUES ('121', 'orderRelease', '工单维护', '/order/list-ui', '12', '3', 1, '0', 'user:add', 'fa fa-flag-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('122', 'orderToday', '每日计划', '/daily/plan/list-ui', '12', '3', 1, '0', 'user:add', 'fa fa-cog', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('13', 'materiel', '物料管理', '#', '7', '2', 2, '0', 'user:add', 'fa fa-cubes', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('131', 'matdef', '物料维护', '/basedata/materile/list-ui', '13', '3', 1, '0', 'user:add', 'fa fa-microchip', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('14', 'Digitalplatform\n\n', '数字化平台', '#', '7', '2', 6, '0', 'user:add', 'fa fa-pie-chart', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('142', 'bigscreen2', '智慧大屏', '/admin/welcome-ui', '14', '3', 1, '0', 'user:add', 'fa fa-desktop', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('15', 'ProcessManage', '工艺序列管理', '#', '7', '2', 3, '0', 'user:add', 'fa fa-wrench', '', '2022-06-29 11:18:29', 'dreamer', '2022-07-15 01:19:46', 'admin');
INSERT INTO `sp_sys_menu` VALUES ('151', 'flowProcess', '工艺路线管理', '/basedata/flow/process/list-ui', '15', '3', 1, '0', 'user:add', 'fa fa-retweet', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('152', 'bom', '工艺BOM管理', '/technology/bom/list-ui', '15', '3', 2, '0', 'user:add', 'fa fa-file-text-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('153', 'operation', '工艺操作管理', '/basedata/operation/list-ui', '15', '3', 4, '0', 'user:add', 'fa fa-retweet', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('16', 'product', '产品管理', '#', '7', '2', 5, '0', 'user:add', 'fa fa-industry', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('161', 'generalSnProcess', 'SN通用过程采集', '/generalSnProcess', '16000', '3', 1, '0', 'user:add', 'fa fa-product-hunt', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('162', 'productProcess', '产品维护', '/product/list-ui', '16', '3', 1, '0', 'user:add', 'fa fa-microchip', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('17', 'DigitalSimulation', '数字孪生', '#', '1-', '2', 7, '0', 'user:add', 'fa fa-ravelry', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('171', 'DigitalSimulationFrom', '数字仿真3D仓库', '/digital/simulation/list-ui', '17-', '3', 1, '0', 'user:add', 'fa fa-codepen', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('18', 'device', '设备管理', '#', '7', '2', 5, '0', 'user:add', 'fa fa-address-book', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('181', 'deviceAcount', '设备台账', '/device/device/list-ui', '18', '3', 1, '0', 'user:add', 'fa fa-retweet', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('182', 'deviceMaintan', '设备维护', '/device/maintain/list-ui', '18', '3', 2, '0', 'user:add', 'fa fa-codepen', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('183', 'deviceAcativation', '时间稼动率', '/device/activation/list-ui', '18', '3', 3, '0', 'user:add', 'fa fa-wrench', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('21', 'datCollect', '数据采集', '#', '8', '2', 1, '0', 'user:add', 'fa fa-wrench', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('211', 'dataollectConfig', '数据采集配置', '/config/list-ui', '21', '3', 1, '0', 'user:add', 'fa fa-flag-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('22', 'mq', '消息队列', '#', '8', '2', 1, '0', 'user:add', 'fa fa-cubes', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('221', 'rabbitmq', 'rabbitmq测试', '/rabbitmq/demo', '22', '3', 1, '0', 'user:add', 'fa fa-microchip', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('222', 'rabbitmq-admin', 'Rabbitmq管理', '/rabbitmq/admin', '22', '3', 1, '0', 'user:add', 'fa fa-flag-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('7', 'currency', '常规管理', '#', '0', '1', 1, '0', 'user:add', 'fa fa-address-book', '', '2022-05-30 11:18:29', 'admin', '2020-03-13 14:07:09', 'admin');
INSERT INTO `sp_sys_menu` VALUES ('8', 'component', '现场数据采集', '#', '0', '1', 1, '0', 'user:add', 'fa fa-lemon-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');

-- ----------------------------
-- Table structure for sp_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_role`;
CREATE TABLE `sp_sys_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '角色描述',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sp_sys_role_name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `idx_sp_sys_role_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_role
-- ----------------------------
INSERT INTO `sp_sys_role` VALUES ('1185025876737396738', '超级管理员', 'admin', '超级管理员', '0', '2020-10-18 10:52:40', 'dremer', '2020-03-13 14:06:43', 'admin');
INSERT INTO `sp_sys_role` VALUES ('1232532514523213826', '体验者123', 'experience', '体验者', '0', '2020-02-26 13:07:05', 'admin', '2020-06-03 15:05:59', 'admin');

-- ----------------------------
-- Table structure for sp_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_role_menu`;
CREATE TABLE `sp_sys_role_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `menu_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色对应的菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_role_menu
-- ----------------------------
INSERT INTO `sp_sys_role_menu` VALUES ('1', '1185025876737396738', '1', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('2', '1185025876737396738', '2', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('3', '1185025876737396738', '3', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('4', '1185025876737396738', '101', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('5', '1185025876737396738', '102', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('6', '1185025876737396738', '103', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');
INSERT INTO `sp_sys_role_menu` VALUES ('7', '1185025876737396738', '104', '2019-10-28 14:51:44', 'admin', '2019-10-28 14:51:56', 'admin');

-- ----------------------------
-- Table structure for sp_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_user`;
CREATE TABLE `sp_sys_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '部门id',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '邮箱',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '固定电话',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别(0:女;1:男;2:其他)',
  `birthday` datetime NULL DEFAULT NULL COMMENT '出生年月日',
  `pic_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '图片id，对应sys_file表中的id',
  `id_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '身份证',
  `hobby` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '爱好',
  `province` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '省份',
  `city` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '城市',
  `district` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '区县',
  `street` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '街道',
  `street_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '门牌号',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\"\"' COMMENT '描述',
  `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `pwd_shadow` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码shadow',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sp_sys_user_username`(`username` ASC) USING BTREE COMMENT '用户名唯一索引',
  UNIQUE INDEX `idx_sp_sys_user_mobile`(`mobile` ASC) USING BTREE COMMENT '用户手机号唯一索引',
  INDEX `idx_sp_sys_user_email`(`email` ASC) USING BTREE COMMENT '用户邮箱唯一索引',
  INDEX `idx_sp_sys_user_id_card`(`id_card` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_user
-- ----------------------------
INSERT INTO `sp_sys_user` VALUES ('1184019107907227640', '超级管理员', 'lgl', 'cad4a723b0b6e40b413edb9b693d8883', '11', '2219992847@qq.com', '13714501649', '44', '0', NULL, '55', '66', '77', '88', '99', '10', '11', '12', '13', '0', '2019-10-15 16:12:08', 'SongPeng', '2020-03-24 11:08:22', 'admin', 'MTg2NjU4MDI2MzY');
INSERT INTO `sp_sys_user` VALUES ('1184019107907227649', '超级管理员', 'admin', '9aa75c4d70930277f59d117ce19188b0', '11', '75039960@qq.com', '18665802636', '0755-12345678', '0', '2020-12-01 00:00:00', '密码必须填写', '选填', '选填', '选填', '选填', '选填', '选填', '选填', '选填', '0', '2020-02-01 16:12:08', 'admin', '2022-08-08 16:00:01', 'admin', 'MTIzNDU2');

-- ----------------------------
-- Table structure for sp_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_sys_user_role`;
CREATE TABLE `sp_sys_user_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户对应的角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_sys_user_role
-- ----------------------------
INSERT INTO `sp_sys_user_role` VALUES ('1267739082731270146', '1266201180838801409', '1336542182244384', '2020-06-02 16:45:25', 'admin', '2022-06-02 16:45:25', 'admin');
INSERT INTO `sp_sys_user_role` VALUES ('1280381244774002690', '1276512902757724162', '1232532514523213826', '2020-07-07 14:00:52', 'admin', '2022-07-07 14:00:52', 'admin');
INSERT INTO `sp_sys_user_role` VALUES ('1556550782988517377', '1184019107907227649', '1185025876737396738', '2022-08-08 16:00:01', 'admin', '2022-08-08 16:00:01', 'admin');

-- ----------------------------
-- Table structure for sp_table_manager
-- ----------------------------
DROP TABLE IF EXISTS `sp_table_manager`;
CREATE TABLE `sp_table_manager`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名称',
  `table_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '\"\"' COMMENT '授权(多个用逗号分隔，如：sys:menu:list,sys:menu:create)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index1`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主数据通用管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_table_manager
-- ----------------------------
INSERT INTO `sp_table_manager` VALUES ('1283020801696837633', 'sp_bom', '', '2020-07-14 20:49:31', 'admin', '2022-06-10 14:21:23', 'admin', '0', '\"\"');

-- ----------------------------
-- Table structure for sp_table_manager_item
-- ----------------------------
DROP TABLE IF EXISTS `sp_table_manager_item`;
CREATE TABLE `sp_table_manager_item`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `table_name_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名称id',
  `field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段',
  `field_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段描述',
  `must_fill` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否必填',
  `sort_num` int NOT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主数据基础数据明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_table_manager_item
-- ----------------------------
INSERT INTO `sp_table_manager_item` VALUES ('1535145079321333761', '1283020801696837633', 'materiel_desc', '888', 'Y', 1, '2022-06-10 14:21:23', 'admin', '2022-06-10 14:21:23', 'admin');
INSERT INTO `sp_table_manager_item` VALUES ('1535145079480717314', '1283020801696837633', 'materiel_code', '999', 'N', 2, '2022-06-10 14:21:23', 'admin', '2022-06-10 14:21:23', 'admin');

-- ----------------------------
-- Table structure for sp_test
-- ----------------------------
DROP TABLE IF EXISTS `sp_test`;
CREATE TABLE `sp_test`  (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_test
-- ----------------------------

-- ----------------------------
-- Table structure for sp_work_shop
-- ----------------------------
DROP TABLE IF EXISTS `sp_work_shop`;
CREATE TABLE `sp_work_shop`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `work_shop` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车间',
  `work_shop_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车间描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工作车间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_work_shop
-- ----------------------------
INSERT INTO `sp_work_shop` VALUES ('1336875254022176', 'GCZ-01', '高磁阻变压器生产车间', '2022-06-29 11:29:57', 'admin', '2022-06-29 10:52:39', 'admin');
INSERT INTO `sp_work_shop` VALUES ('1336875591663648', 'CS-01', '测速传感器', '2022-06-29 11:32:38', 'admin', '2022-06-29 11:32:38', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
