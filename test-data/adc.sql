/*
 Navicat Premium Data Transfer

 Source Server         : local-docker
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3307
 Source Schema         : adc

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 10/06/2025 21:33:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kb
-- ----------------------------
DROP TABLE IF EXISTS `kb`;
CREATE TABLE `kb`  (
  `kb_id` bigint NOT NULL AUTO_INCREMENT COMMENT '知识库目录ID',
  `kb_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目录名称',
  `kb_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '目录描述',
  `organization_id` bigint NOT NULL COMMENT '所属组织ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级目录ID，NULL 表示顶层目录',
  `level` tinyint NOT NULL DEFAULT 1 COMMENT '目录层级，最大为3',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` bigint NOT NULL COMMENT '创建人',
  PRIMARY KEY (`kb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb
-- ----------------------------
INSERT INTO `kb` VALUES (1, '总部知识库', '总部一级目录', 1, NULL, 1, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);
INSERT INTO `kb` VALUES (2, '技术文档', '技术部一级目录', 2, NULL, 1, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);
INSERT INTO `kb` VALUES (3, 'API文档', '二级目录', 2, 2, 2, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);
INSERT INTO `kb` VALUES (4, '市场资料', '市场部一级目录', 3, NULL, 1, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);
INSERT INTO `kb` VALUES (5, '市场PPT', '市场二级目录', 3, 4, 2, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);
INSERT INTO `kb` VALUES (6, 'Share目录', '共享目录', 2, NULL, 1, 0, '2025-06-09 13:11:49', '2025-06-09 13:11:49', 1);

-- ----------------------------
-- Table structure for kb_file
-- ----------------------------
DROP TABLE IF EXISTS `kb_file`;
CREATE TABLE `kb_file`  (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型，例如 pdf、docx、txt 等',
  `file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件存储地址（或本地路径）',
  `kb_id` bigint NOT NULL COMMENT '所属KB目录ID',
  `organization_id` bigint NOT NULL COMMENT '所属组织ID',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` bigint NOT NULL COMMENT '上传者用户ID',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb_file
-- ----------------------------
INSERT INTO `kb_file` VALUES (100, '技术白皮书.pdf', 'txt', 'local', 3, 2, 0, '2025-06-09 13:13:57', '2025-06-09 13:13:57', 2);
INSERT INTO `kb_file` VALUES (101, 'API说明.docx', 'txt', 'local', 4, 2, 0, '2025-06-09 13:13:57', '2025-06-09 13:13:57', 2);
INSERT INTO `kb_file` VALUES (102, '市场计划.ppt', 'txt', 'local', 5, 3, 0, '2025-06-09 13:13:57', '2025-06-09 13:13:57', 3);

-- ----------------------------
-- Table structure for kb_file_mount
-- ----------------------------
DROP TABLE IF EXISTS `kb_file_mount`;
CREATE TABLE `kb_file_mount`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` bigint NOT NULL COMMENT '文件ID',
  `owner_user_id` bigint NOT NULL COMMENT '拥有者用户ID',
  `kb_id` bigint NOT NULL COMMENT '目标知识库目录ID（挂载点）',
  `is_shared` tinyint NULL DEFAULT 0 COMMENT '是否为分享产生的挂载（0 否，1 是）',
  `is_deleted` tinyint NULL DEFAULT 0,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb_file_mount
-- ----------------------------

-- ----------------------------
-- Table structure for kb_file_share
-- ----------------------------
DROP TABLE IF EXISTS `kb_file_share`;
CREATE TABLE `kb_file_share`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `file_id` bigint NOT NULL COMMENT '被分享的文件ID',
  `to_user_id` bigint NOT NULL COMMENT '被分享的目标用户ID',
  `from_user_id` bigint NOT NULL COMMENT '分享者用户ID',
  `shared_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb_file_share
-- ----------------------------
INSERT INTO `kb_file_share` VALUES (1, 102, 2, 3, '2025-06-09 13:14:19', 0, '2025-06-09 13:14:19', '2025-06-09 13:14:19');

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `admin_id` bigint NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, '总部', NULL, 1, '2025-06-09 13:10:24', '2025-06-09 13:10:24', 0);
INSERT INTO `organization` VALUES (2, '技术部', 1, 2, '2025-06-09 13:10:24', '2025-06-09 13:10:24', 0);
INSERT INTO `organization` VALUES (3, '市场部', 1, 3, '2025-06-09 13:10:24', '2025-06-09 13:10:24', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_id` int NULL DEFAULT NULL,
  `dept_id` int NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upassword` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB' COMMENT 'sm3',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 1, 0, 0, '2025-06-07 05:34:12', '2025-06-07 05:34:12', '207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB');
INSERT INTO `user` VALUES (2, 'alice', 2, 11, 0, '2025-06-09 13:10:10', '2025-06-09 13:10:10', '207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB');
INSERT INTO `user` VALUES (3, 'bob', 2, 12, 0, '2025-06-09 13:10:10', '2025-06-09 13:10:10', '207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB');
INSERT INTO `user` VALUES (4, 'charlie', 2, 13, 0, '2025-06-09 13:10:10', '2025-06-09 13:10:10', '207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB');

SET FOREIGN_KEY_CHECKS = 1;
