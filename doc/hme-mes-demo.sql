/*
 Navicat Premium Data Transfer

 Source Server         : 150.158.15.149-3307-腾讯云mysql8-root-lgl2019
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : 150.158.15.149:3307
 Source Schema         : hme-mes

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 03/09/2022 09:02:20
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
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'BOM主信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_bom
-- ----------------------------
INSERT INTO `sp_bom` VALUES ('1564827443160875009', 'BOM101000036', 'PCB101000036', NULL, NULL, NULL, '创建', '供应商A', '0', '2022-08-31 12:08:30', 'admin', '2022-08-31 12:08:30', 'admin');
INSERT INTO `sp_bom` VALUES ('1564827443890683906', 'BOM101000036', 'THREAD001001', NULL, NULL, NULL, '已经审核', '供应商B', '0', '2022-08-31 12:08:30', 'admin', '2022-08-31 12:08:30', 'admin');

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
-- Table structure for sp_com_protocol
-- ----------------------------
DROP TABLE IF EXISTS `sp_com_protocol`;
CREATE TABLE `sp_com_protocol`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '协议名称',
  `protocol_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '协议描述，desc是sl保留关键字',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_com_protocol
-- ----------------------------
INSERT INTO `sp_com_protocol` VALUES ('18665802636', 'tcp', 'tcp', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802637', 'udp', 'udp', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802638', 'mqtt', 'mqtt', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802639', 'modbus', 'modbus', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802640', 'fins-tcp', 'fins-tcp', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802641', 'fins-udp', 'fins-udp', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802642', 'plc-simens', 'plc-simens', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802643', 'coap', 'coap', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802644', 'nb-iot', 'nb-iot', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');
INSERT INTO `sp_com_protocol` VALUES ('18665802645', 'http', 'http', '2022-08-30 10:55:11', 'admin', '2022-08-30 10:55:27', 'admin');

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
  `local_port` int NULL DEFAULT 9600 COMMENT '本地port',
  `interv` int NULL DEFAULT 10 COMMENT '更新数据的时间间隔 秒,interval是保留字不能使用',
  `com_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '4' COMMENT '通讯协议类型\r\n0 tcp   client\r\n1 tcp  server\r\n2 udp  server\r\n3 udp  client\r\n4 fins  client\r\n5 simens client\r\n',
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
INSERT INTO `sp_config` VALUES ('1555232008796508162', '2022-08-05 00:39:40', 'admin', '2022-08-30 13:52:29', 'admin', '磁阻传感器', '192.168.250.50', 9600, 9601, 10, 'fins-udp', 0, 115200, 8, 1, 0);
INSERT INTO `sp_config` VALUES ('1555232008796508163', '2022-08-05 00:39:40', 'admin', '2022-09-02 17:33:31', 'admin', '测速传感器', '192.168.250.50', 9600, 9602, 10, 'modbus', 0, 115200, 8, 1, 0);
INSERT INTO `sp_config` VALUES ('1565633890526351361', '2022-09-02 17:33:02', 'admin', '2022-09-02 17:33:02', 'admin', '加速度传感器', '192.168.250.26', 9400, 9401, 6, 'modbus', 0, 115200, 8, 1, 0);

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
  `group_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班组',
  `line_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产线名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_daily_plan
-- ----------------------------
INSERT INTO `sp_daily_plan` VALUES ('1543692092598038530', 'HME2022070101', '2022-07-17 08:00:00', 30, 90, 1000, 103, 0000000002, 10.30, 99.00, NULL, 0, '2022-07-04 04:24:10', 'admin', '2022-09-02 17:26:56', 'admin', 0, '2022-07-16 13:30:00', '2022-07-16 15:30:00', '2022-07-16 18:30:00', '2022-07-16 20:30:00', '2022-07-16 08:00:00', '2022-07-16 10:00:00', '2022-07-16 10:31:55', 0.00, '2022-07-16 15:40:00', '2022-07-16 17:40:00', '', '', '2022-07-16 10:10:00', '2022-07-16 11:40:00', '磁阻2线1班', '磁阻传感器');
INSERT INTO `sp_daily_plan` VALUES ('1543801556663676930', 'HME2022070401', '2022-07-17 11:38:00', 31, 0, 1000, 30, 0000000004, 3.00, NULL, NULL, 0, '2022-07-04 11:39:08', 'admin', '2022-07-04 11:41:40', 'admin', 0, '2022-07-04 16:38:00', '2022-07-04 11:43:00', '', '', '2022-07-04 11:38:00', '2022-07-04 11:44:00', NULL, 0.00, NULL, NULL, NULL, '2022-07-04 10:18:00', NULL, NULL, NULL, NULL);
INSERT INTO `sp_daily_plan` VALUES ('1548594146030325761', 'DD2022070202', '2022-07-19 08:00:00', 28, 590, 400, 3, 0000000000, 0.75, 100.00, NULL, 0, '2022-07-17 17:03:11', 'admin', '2022-07-19 09:13:05', 'admin', 0, '2022-07-17 13:00:00', '2022-07-17 15:00:00', '2022-07-17 18:30:00', '2022-07-17 20:00:00', '2022-07-17 08:00:00', '2022-07-17 10:00:00', '2022-07-17 19:28:36', 0.00, '2022-07-17 15:10:00', '2022-07-17 17:30:00', '2022-07-17 20:40:00', '2022-07-17 22:00:00', '2022-07-17 10:10:00', '2022-07-17 11:40:00', NULL, NULL);
INSERT INTO `sp_daily_plan` VALUES ('1548632599342252033', 'DD2022070202', '2022-08-03 08:00:00', 28, 5, 400, 321, 0000000001, 80.25, 99.69, NULL, 0, '2022-07-17 19:35:59', 'admin', '2022-08-03 22:57:19', 'lgl', 0, '2022-08-03 13:00:00', '2022-08-03 15:00:00', '2022-08-03 18:30:00', '2022-08-03 20:30:00', '2022-08-03 08:00:00', '2022-08-03 10:00:00', '2022-08-03 22:57:19', 124.50, '2022-08-03 15:10:00', '2022-08-03 17:40:00', NULL, NULL, '2022-08-03 10:10:00', '2022-08-03 11:40:00', NULL, NULL);
INSERT INTO `sp_daily_plan` VALUES ('1562086339223134288', 'DD2022082301', '2022-08-26 09:32:00', 30, 30, 1300, 300, 0000000001, 0.10, 100.00, NULL, 0, '2022-08-23 22:36:20', 'admin', '2022-09-02 10:20:26', 'admin', 0, '2022-08-24 13:00:00', '2022-08-24 15:00:00', NULL, NULL, '2022-08-24 08:00:00', '2022-08-24 10:00:00', '2022-08-24 06:58:48', 0.00, '2022-08-24 15:10:00', '2022-08-24 17:40:00', NULL, NULL, '2022-08-24 10:10:00', '2022-08-24 11:40:00', '磁阻2号线-1班', NULL);
INSERT INTO `sp_daily_plan` VALUES ('1562086339223134299', 'DD2022082401', '2022-09-03 09:32:00', 30, 47, 1400, 497, 0000000004, 35.50, 83.50, NULL, 0, '2022-08-23 22:36:20', 'admin', '2022-09-03 00:48:08', 'admin', 0, '2022-09-03 13:00:00', '2022-09-03 15:00:00', NULL, NULL, '2022-09-03 08:00:00', '2022-09-03 10:00:00', '2022-09-02 10:40:54', 0.00, '2022-09-03 15:10:00', '2022-09-03 17:40:00', NULL, NULL, '2022-09-03 10:10:00', '2022-09-03 11:40:00', '磁阻2线1班', '磁阻传感器');

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
INSERT INTO `sp_device` VALUES ('1559898665796165633', '罗盘-01', '罗盘-01', '华夏智造', '2022-08-17 21:43:18', 'admin', '2022-08-30 02:08:38', 'admin', '0', '0');
INSERT INTO `sp_device` VALUES ('1559905357585858561', 'SMT-01', '贴片机-01', '富士康', '2022-08-17 22:09:54', 'admin', '2022-08-25 11:23:27', 'admin', '0', '0');

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
  `flag` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '0正常运行，1故障，2维修中 3 禁用',
  `bad_time` int NULL DEFAULT 0 COMMENT '本次故障时长(分钟)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_device_maintain
-- ----------------------------
INSERT INTO `sp_device_maintain` VALUES ('1560077833016164354', '罗盘-01', '罗盘-01无法正常工作', 'admin', '', '2022-08-18 09:35:15', 'admin', '2022-09-02 09:52:48', 'admin', '0', '故障', 10);
INSERT INTO `sp_device_maintain` VALUES ('1560079314595254273', 'SMT-01', 'SMT-01 发热', 'admin', '处理中。。。', '2022-08-18 09:41:08', 'admin', '2022-09-02 09:53:11', 'admin', '0', '维修中', 5);
INSERT INTO `sp_device_maintain` VALUES ('1560079314595784263', '绕线机01', '却线', 'admin', 'ok ', '2022-08-18 09:41:08', 'admin', '2022-09-02 09:39:03', 'admin', '0', '正常', 5);
INSERT INTO `sp_device_maintain` VALUES ('1560079314595785574', '绕线机02', '绕线机-02 卡线', 'admin', '处理中。。', '2022-08-18 09:41:08', 'admin', '2022-09-02 09:52:32', 'admin', '0', '维修中', 5);

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
INSERT INTO `sp_flow` VALUES ('1565633560459792386', '加速度传感器', '加速度传感器', '上料->贴片->绕丝->加固->测试->打码->入库', '2022-09-02 17:31:44', 'admin', '2022-09-02 17:31:57', 'admin');

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
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616193703937', '1565633560459792386', '加速度传感器', '', '', '1559594964397264897', '上料', '1559595062946631682', '贴片', 1, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616193703938', '1565633560459792386', '加速度传感器', '1559594964397264897', '上料', '1559595062946631682', '贴片', '1559595229389197314', '绕丝', 2, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616193703939', '1565633560459792386', '加速度传感器', '1559595062946631682', '贴片', '1559595229389197314', '绕丝', '1559595327984701441', '加固', 3, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616193703940', '1565633560459792386', '加速度传感器', '1559595229389197314', '绕丝', '1559595327984701441', '加固', '1559595389871656962', '测试', 4, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616218869761', '1565633560459792386', '加速度传感器', '1559595327984701441', '加固', '1559595389871656962', '测试', '1559595533094555650', '打码', 5, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616218869762', '1565633560459792386', '加速度传感器', '1559595389871656962', '测试', '1559595533094555650', '打码', '1866580263623456', 'RuKu', 6, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');
INSERT INTO `sp_flow_oper_relation` VALUES ('1565633616218869763', '1565633560459792386', '加速度传感器', '1559595533094555650', '打码', '1866580263623456', 'RuKu', '', '', 7, NULL, '2022-09-02 17:31:57', 'admin', '2022-09-02 17:31:57', 'admin');

-- ----------------------------
-- Table structure for sp_global_id
-- ----------------------------
DROP TABLE IF EXISTS `sp_global_id`;
CREATE TABLE `sp_global_id`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `order_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '全局订单编号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_global_id
-- ----------------------------
INSERT INTO `sp_global_id` VALUES ('1336867983196192', 'DD2022082401', '2022-07-24 10:32:10', 'admin', '2022-09-03 00:47:38', 'admin');

-- ----------------------------
-- Table structure for sp_group
-- ----------------------------
DROP TABLE IF EXISTS `sp_group`;
CREATE TABLE `sp_group`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班组名称',
  `leader` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班长',
  `group_count` int NULL DEFAULT 0 COMMENT '班组成员数量',
  `contact` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `update_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最后更新人',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_group
-- ----------------------------
INSERT INTO `sp_group` VALUES ('1564524098210643969', '1组白班', '张辉中', 6, '18834882991', '2022-08-30 16:03:07', 'admin', '2022-08-30 16:22:09', 'admin', '0');
INSERT INTO `sp_group` VALUES ('1564529022264844289', '2组白班', '胡一鸣', 8, '15966896638', '2022-08-30 16:22:41', 'admin', '2022-08-30 16:23:14', 'admin', '0');
INSERT INTO `sp_group` VALUES ('1564529457528741889', '3班-白班', '贺礼', 8, '16677773688', '2022-08-30 16:24:25', 'admin', '2022-08-30 16:24:25', 'admin', '0');
INSERT INTO `sp_group` VALUES ('1564530019699695618', '磁阻2线1班', '吴义迪', 6, '13714501649', '2022-08-30 16:26:39', 'admin', '2022-09-02 17:12:14', 'admin', '0');
INSERT INTO `sp_group` VALUES ('1564530510630395906', '压力传感器1线1班', '赵菁', 6, '18868573399', '2022-08-30 16:28:36', 'admin', '2022-09-02 10:17:19', 'admin', '0');
INSERT INTO `sp_group` VALUES ('1564538664114085889', '加速度传感器1号线1班', '赖小铭', 5, '18864476599', '2022-08-30 17:01:00', 'admin', '2022-09-02 10:17:37', 'admin', '0');

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
INSERT INTO `sp_materile` VALUES ('1564825453416599553', 'PCB101000036', 'pcb', 'PCB101000036#220614#6000#DD20220516002', '220614', NULL, NULL, NULL, NULL, NULL, '磁阻传感器', NULL, '2022-08-31 12:00:36', 'admin', '2022-08-31 12:00:36', 'admin', '0');
INSERT INTO `sp_materile` VALUES ('1564825454205128706', 'PCB101000037', 'pcb', 'PCB101000037#220614#6000#DD20220516002', '220614', NULL, NULL, NULL, NULL, NULL, '测速传感器', NULL, '2022-08-31 12:00:36', 'admin', '2022-08-31 12:00:36', 'admin', '0');
INSERT INTO `sp_materile` VALUES ('1564825454683279361', 'THREAD001001', '铜线', 'THREAD001001#220614#8000#DD20220516002', '220614', NULL, NULL, NULL, NULL, NULL, '磁阻传感器', NULL, '2022-08-31 12:00:36', 'admin', '2022-08-31 12:00:36', 'admin', '0');

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
INSERT INTO `sp_oper` VALUES ('1866580263612345', '采购', '采购', '2022-06-23 14:31:25', 'admin', '2022-09-03 01:12:52', 'admin');
INSERT INTO `sp_oper` VALUES ('1866580263623456', '入库', '入库', '2022-06-23 14:41:41', 'admin', '2022-09-03 01:12:34', 'admin');

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
INSERT INTO `sp_order` VALUES ('1548372368175656961', 'HME2022071801', NULL, 3000, NULL, '测速传感器', NULL, '2022-07-17 08:00:00', '2022-07-20 17:40:00', '订单终结', '2022-07-17 02:21:55', 'admin', '2022-08-30 16:36:39', 'admin', '磁阻变压器', 0, 000000, 0.00, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('1548596501324959745', 'DD2022070202', NULL, 6000, NULL, '测速传感器', NULL, '2022-07-18 08:00:00', '2022-08-07 17:12:00', '创建', '2022-07-17 17:12:32', 'admin', '2022-08-31 12:40:27', 'admin', '磁阻变压器', 1801, 000002, 0.00, '', '', 30.02);
INSERT INTO `sp_order` VALUES ('1562085701286273026', 'DD2022082301', '磁阻传感器', 6300, '1', '测速传感器', NULL, '2022-08-23 08:30:00', '2022-08-27 20:30:00', '创建', '2022-08-23 22:33:48', 'admin', '2022-08-29 23:45:19', 'admin', '磁阻变压器', 1300, 000003, 0.00, '', '', 0.02);
INSERT INTO `sp_order` VALUES ('1562085701286273036', 'DD2022082401', '磁阻传感器', 6400, '1', '磁阻传感器生产工艺', NULL, '2022-08-23 08:30:00', '2022-08-27 20:30:00', '进行中', '2022-08-23 22:33:48', 'admin', '2022-09-02 22:01:47', 'admin', '磁阻变压器', 1497, 000005, 0.00, '', '', 23.39);
INSERT INTO `sp_order` VALUES ('1563134877837058049', 'DD2022082601', NULL, 6000, NULL, '磁阻传感器生产工艺', NULL, '2022-08-26 22:02:00', '2022-09-06 20:02:00', '进行中', '2022-08-26 20:02:51', 'admin', '2022-08-26 20:03:35', 'admin', NULL, 0, 000000, 0.00, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('453453453', 'HE2022062601-zemine', '生产变速传感器2000套', 8000, '1', 'A01装配工序->测试工序', NULL, '2022-06-30 14:41:42', '2022-07-13 20:13:00', '订单结束', '2022-06-30 14:49:32', NULL, '2022-08-16 14:54:37', 'admin', '压力传感器', 3000, 000009, NULL, '', '', 100.00);
INSERT INTO `sp_order` VALUES ('453453489', 'HME2022070401', '生产变速传感器3000套', 3000, '1', 'A01装配工序->测试工序', NULL, '2022-07-04 14:41:42', '2022-07-09 14:41:42', '进行中', '2022-06-30 14:49:32', NULL, '2022-08-16 14:54:50', 'admin', '测速传感器产线', 1000, 000004, NULL, '', '', 33.30);
INSERT INTO `sp_order` VALUES ('453453490', 'HE2022062801', '生产变速传感器3000套', 4000, '1', 'A01装配工序->测试工序', NULL, '2022-07-04 14:41:42', '2022-07-12 14:41:42', '创建', '2022-06-30 14:49:32', NULL, '2022-08-16 14:55:04', 'admin', '高磁阻变压器', 300, 000001, NULL, '', '', 0.00);
INSERT INTO `sp_order` VALUES ('5345345', 'EC2022062002', '生产1000个高磁阻变压器', 1000, '2', 'A01装配工序->测试工序', NULL, '2022-07-01 14:41:42', '2022-07-04 14:41:42', '订单终结', '2022-06-30 14:41:42', NULL, '2022-08-16 14:55:16', 'admin', '高磁阻变压器', 600, 000002, NULL, '', '', 0.00);

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
  `bad_pos` int NULL DEFAULT NULL COMMENT '不良品工位号',
  `position` int NULL DEFAULT 1 COMMENT '产品库位： 0， 车间，1 成品仓库，2 出库',
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '逻辑删除：1 表示删除，0 表示未删除，2 表示禁用',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程id',
  `flow_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sp_product
-- ----------------------------
INSERT INTO `sp_product` VALUES ('1554550413592768514', 'ECG2022080301', '测速传感器', 'BOM101000036', '2022-08-03 03:31:15', 'lgl', '2022-09-03 08:57:50', 'admin', 'ECG2022080301#80301#6000#DD2022080301', '80301', 'DD2022080301', 'OK', 2, 1, '0', '1277600512544583681', '测速传感器');
INSERT INTO `sp_product` VALUES ('1554550539400916994', 'ECG2022080314', NULL, NULL, '2022-08-03 03:31:45', 'lgl', '2022-08-03 03:31:45', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554550665213259778', 'ECG2022080339', NULL, NULL, '2022-08-03 03:32:15', 'lgl', '2022-08-03 03:32:15', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718033986187265', 'ECG2022080343', NULL, NULL, '2022-08-03 14:37:19', 'lgl', '2022-08-03 14:37:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718159777558530', 'ECG2022080329', NULL, NULL, '2022-08-03 14:37:49', 'lgl', '2022-08-03 14:37:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718285543763970', 'ECG2022080311', NULL, NULL, '2022-08-03 14:38:19', 'lgl', '2022-08-03 14:38:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718411473547265', 'ECG2022080363', NULL, NULL, '2022-08-03 14:38:49', 'lgl', '2022-08-03 14:38:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718537202003969', 'ECG2022080324', NULL, NULL, '2022-08-03 14:39:19', 'lgl', '2022-08-03 14:39:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718663077261314', 'ECG2022080309', NULL, NULL, '2022-08-03 14:39:49', 'lgl', '2022-08-03 14:39:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718788918964225', 'ECG2022080322', NULL, NULL, '2022-08-03 14:40:19', 'lgl', '2022-08-03 14:40:19', 'lgl', NULL, NULL, 'DD2022070202', 'NG', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554718914773250049', 'ECG2022080336', NULL, NULL, '2022-08-03 14:40:49', 'lgl', '2022-08-03 14:40:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719040539455489', 'ECG2022080370', NULL, NULL, '2022-08-03 14:41:19', 'lgl', '2022-08-03 14:41:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719166368575489', 'ECG2022080343', NULL, NULL, '2022-08-03 14:41:49', 'lgl', '2022-08-03 14:41:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719292264804354', 'ECG2022080367', NULL, NULL, '2022-08-03 14:42:19', 'lgl', '2022-08-03 14:42:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719418056175618', 'ECG2022080376', NULL, NULL, '2022-08-03 14:42:49', 'lgl', '2022-08-03 14:42:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543441741186', 'ECG2022080303', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'NG', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543851041186', 'BP2022080309', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543851741133', 'ECG2022080301', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543851741186', 'ECG2022080309', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719543851941186', 'BP2022080309', NULL, NULL, '2022-08-03 14:43:19', 'lgl', '2022-08-03 14:43:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719669722804225', 'ECG2022080332', NULL, NULL, '2022-08-03 14:43:49', 'lgl', '2022-08-03 14:43:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719795518369794', 'ECG2022080391', NULL, NULL, '2022-08-03 14:44:19', 'lgl', '2022-08-03 14:44:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554719921393627137', 'ECG2022080320', NULL, NULL, '2022-08-03 14:44:49', 'lgl', '2022-08-03 14:44:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720047172415489', 'ECG2022080316', NULL, NULL, '2022-08-03 14:45:19', 'lgl', '2022-08-03 14:45:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720173056061442', 'ECG2022080304', NULL, NULL, '2022-08-03 14:45:49', 'lgl', '2022-08-03 14:45:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720298818072578', 'ECG2022080314', NULL, NULL, '2022-08-03 14:46:19', 'lgl', '2022-08-03 14:46:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720424718495746', 'ECG2022080357', NULL, NULL, '2022-08-03 14:46:49', 'lgl', '2022-08-03 14:46:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720550526644226', 'ECG2022080384', NULL, NULL, '2022-08-03 14:47:19', 'lgl', '2022-08-03 14:47:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720676309626882', 'ECG2022080358', NULL, NULL, '2022-08-03 14:47:49', 'lgl', '2022-08-03 14:47:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720802142941186', 'ECG2022080335', NULL, NULL, '2022-08-03 14:48:19', 'lgl', '2022-08-03 14:48:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554720928030781441', 'ECG2022080317', NULL, NULL, '2022-08-03 14:48:49', 'lgl', '2022-08-03 14:48:49', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721053859901441', 'ECG2022080391', NULL, NULL, '2022-08-03 14:49:19', 'lgl', '2022-08-03 14:49:19', 'lgl', NULL, NULL, 'DD2022070202', 'OK', NULL, 1, '0', NULL, NULL);
INSERT INTO `sp_product` VALUES ('1554721179626106881', 'ECG2022080351', '', '', '2022-08-03 14:49:49', 'lgl', '2022-08-31 20:10:33', 'admin', '', '', 'DD2022070202', 'OK', 1, 1, '0', '1559443376458342402', '磁阻传感器生产工艺');

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
INSERT INTO `sp_sys_menu` VALUES ('14', 'Digitalplatform\n\n', '可视化平台', '#', '7', '2', 6, '0', 'user:add', 'fa fa-pie-chart', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
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
INSERT INTO `sp_sys_menu` VALUES ('19', 'grup', '班组管理', '#', '7', '2', 5, '0', 'user:add', 'fa fa-desktop', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('191', 'group-list', '班组台账', '/group/list-ui', '19', '3', 1, '0', 'user:add', 'layui-icon-user', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('21', 'datCollect', '数据采集', '#', '8', '2', 1, '0', 'user:add', 'fa fa-cubes', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('211', 'dataollectConfig', '数据采集配置', '/config/list-ui', '21', '3', 1, '0', 'user:add', 'fa fa-flag-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('22', 'mq', '消息队列', '#', '8', '2', 1, '0', 'user:add', 'fa fa-cubes', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
INSERT INTO `sp_sys_menu` VALUES ('221', 'rabbitmq', 'rabbitmq测试', '/rabbitmq/demo', '22', '3', 1, '0', 'user:add', 'fa fa-flag-o', '', '2022-06-29 11:18:29', 'dreamer', '2019-10-18 11:18:29', 'dreamer');
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
INSERT INTO `sp_sys_user` VALUES ('1184019107907227649', '超级管理员', 'admin', '9aa75c4d70930277f59d117ce19188b0', '11', '75039960@qq.com', '18665802636', '0755-12345678', '0', '2020-12-01 00:00:00', '密码必须填写', '选填', '选填', '选填', '选填', '选填', '选填', '选填', '选填', '0', '2020-02-01 16:12:08', 'admin', '2022-08-26 19:33:46', 'admin', 'MTIzNDU2');

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
INSERT INTO `sp_sys_user_role` VALUES ('1563127562836664322', '1184019107907227649', '1185025876737396738', '2022-08-26 19:33:47', 'admin', '2022-08-26 19:33:47', 'admin');

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
