/*
 Navicat Premium Data Transfer

 Source Server         : Chris
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : chris

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 14/04/2019 23:36:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hold_record
-- ----------------------------
DROP TABLE IF EXISTS `hold_record`;
CREATE TABLE `hold_record`  (
  `symbolCode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `symbolName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `open` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `close` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `high` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `low` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `previous_cci` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cci` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `groupID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`symbolCode`, `time`, `groupID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : Chris
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : chris

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 14/04/2019 23:36:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hold_symbol
-- ----------------------------
DROP TABLE IF EXISTS `hold_symbol`;
CREATE TABLE `hold_symbol`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `symbolCode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `symbolName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `buyPrice` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `buyTime` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `isETF` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hold_symbol
-- ----------------------------
INSERT INTO `hold_symbol` VALUES (1, 'SZ000002', '万科A', '32.01', '2019-04-9', 'false');
INSERT INTO `hold_symbol` VALUES (2, 'SZ002415', '海威康视', '34.42', '2019-04-12', 'false');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : Chris
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : chris

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 14/04/2019 23:36:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for monitor_record
-- ----------------------------
DROP TABLE IF EXISTS `monitor_record`;
CREATE TABLE `monitor_record`  (
  `symbolCode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `symbolName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cci` float NULL DEFAULT NULL,
  `time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`symbolCode`, `time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : Chris
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : chris

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 14/04/2019 23:36:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for monitor_symbol
-- ----------------------------
DROP TABLE IF EXISTS `monitor_symbol`;
CREATE TABLE `monitor_symbol`  (
  `symbolCode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `symbolName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`symbolCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monitor_symbol
-- ----------------------------
INSERT INTO `monitor_symbol` VALUES ('SH510050', '50ETF');
INSERT INTO `monitor_symbol` VALUES ('SH510300', '300ETF');
INSERT INTO `monitor_symbol` VALUES ('SH510500', '500ETF');
INSERT INTO `monitor_symbol` VALUES ('SH512000', '券商ETF');
INSERT INTO `monitor_symbol` VALUES ('SH512800', '银行ETF');
INSERT INTO `monitor_symbol` VALUES ('SH518880', '黄金ETF');
INSERT INTO `monitor_symbol` VALUES ('SH600028', '中国石化');
INSERT INTO `monitor_symbol` VALUES ('SH600030', '中信证券');
INSERT INTO `monitor_symbol` VALUES ('SH600036', '招商银行');
INSERT INTO `monitor_symbol` VALUES ('SH600104', '上汽集团');
INSERT INTO `monitor_symbol` VALUES ('SH600196', '复兴医药');
INSERT INTO `monitor_symbol` VALUES ('SH600276', '恒瑞医药');
INSERT INTO `monitor_symbol` VALUES ('SH600309', '万华化学');
INSERT INTO `monitor_symbol` VALUES ('SH600436', '片子癀');
INSERT INTO `monitor_symbol` VALUES ('SH600460', '士兰微');
INSERT INTO `monitor_symbol` VALUES ('SH600519', '贵州茅台');
INSERT INTO `monitor_symbol` VALUES ('SH600547', '山东黄金');
INSERT INTO `monitor_symbol` VALUES ('SH600570', '恒生电子');
INSERT INTO `monitor_symbol` VALUES ('SH600585', '海螺水泥');
INSERT INTO `monitor_symbol` VALUES ('SH600660', '福耀玻璃');
INSERT INTO `monitor_symbol` VALUES ('SH600887', '伊利股份');
INSERT INTO `monitor_symbol` VALUES ('SH601111', '中国国航');
INSERT INTO `monitor_symbol` VALUES ('SH601318', '中国平安');
INSERT INTO `monitor_symbol` VALUES ('SH601398', '工商银行');
INSERT INTO `monitor_symbol` VALUES ('SH601800', '中国交建');
INSERT INTO `monitor_symbol` VALUES ('SH601890', '亚星锚链');
INSERT INTO `monitor_symbol` VALUES ('SH603288', '海天味业');
INSERT INTO `monitor_symbol` VALUES ('SZ000002', '万科A');
INSERT INTO `monitor_symbol` VALUES ('SZ000063', '中兴通讯');
INSERT INTO `monitor_symbol` VALUES ('SZ000088', '盐田港');
INSERT INTO `monitor_symbol` VALUES ('SZ000333', '美的集团');
INSERT INTO `monitor_symbol` VALUES ('SZ000338', '潍柴动力');
INSERT INTO `monitor_symbol` VALUES ('SZ000651', '格力电器');
INSERT INTO `monitor_symbol` VALUES ('SZ000725', '京东方A');
INSERT INTO `monitor_symbol` VALUES ('SZ000858', '五粮液');
INSERT INTO `monitor_symbol` VALUES ('SZ001979', '招商蛇口');
INSERT INTO `monitor_symbol` VALUES ('SZ002007', '华兰生物');
INSERT INTO `monitor_symbol` VALUES ('SZ002027', '分众传媒');
INSERT INTO `monitor_symbol` VALUES ('SZ002138', '顺络电子');
INSERT INTO `monitor_symbol` VALUES ('SZ002415', '海威康视');
INSERT INTO `monitor_symbol` VALUES ('SZ002507', '涪陵榨菜');
INSERT INTO `monitor_symbol` VALUES ('SZ002600', '领益智造');
INSERT INTO `monitor_symbol` VALUES ('SZ002733', '雄韬股份');
INSERT INTO `monitor_symbol` VALUES ('SZ159915', '创业板');
INSERT INTO `monitor_symbol` VALUES ('SZ300015', '爱尔眼科');
INSERT INTO `monitor_symbol` VALUES ('SZ300059', '东方财富');
INSERT INTO `monitor_symbol` VALUES ('SZ300730', '科创信息');

SET FOREIGN_KEY_CHECKS = 1;
