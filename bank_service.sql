/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80038
Source Host           : localhost:3306
Source Database       : bank_service

Target Server Type    : MYSQL
Target Server Version : 80038
File Encoding         : 65001

Date: 2024-10-11 14:08:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `balance` decimal(15,2) NOT NULL DEFAULT '10000.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_id` (`account_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', 'customer_account_001', 'customer', '9200.00');
INSERT INTO `accounts` VALUES ('2', 'store_account_001', 'store', '10800.00');

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(255) NOT NULL,
  `order_id` varchar(255) NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `customer_account_id` varchar(255) NOT NULL,
  `store_account_id` varchar(255) NOT NULL,
  `type` varchar(10) NOT NULL,
  `status` varchar(10) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of transactions
-- ----------------------------
INSERT INTO `transactions` VALUES ('1', 'a614e986-3459-45c4-99ff-72a0206a5066', '15', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:11:20');
INSERT INTO `transactions` VALUES ('2', '91221e16-751e-47dc-90c0-d76901b1c0b1', '16', '30000.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'FAILURE', '2024-10-11 01:14:04');
INSERT INTO `transactions` VALUES ('3', '5d8b3133-2873-4507-8c1a-31f2b3de094c', '17', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:14:48');
INSERT INTO `transactions` VALUES ('4', 'c23e1cc5-d23a-4b58-99e8-0af2b61b1d0c', '19', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:16:50');
INSERT INTO `transactions` VALUES ('5', '6a04d187-bd52-48b2-a2ce-69c01949a233', '20', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:19:54');
INSERT INTO `transactions` VALUES ('6', '3f8bced7-5a97-4a32-939b-6a88962e263f', '21', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:19:57');
INSERT INTO `transactions` VALUES ('7', 'eae2ece1-055b-4d5c-8083-c74c8b906acf', '22', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:19:58');
INSERT INTO `transactions` VALUES ('8', '0c3c940a-2a79-46ce-bd98-d49ac60b0061', '23', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:19:59');
INSERT INTO `transactions` VALUES ('9', '12ac4ae7-3c20-4e56-a141-cd3a48da8a53', '24', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:20:00');
INSERT INTO `transactions` VALUES ('10', '26326669-eaea-4d9b-96ec-177f088ed97f', '25', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:20:02');
INSERT INTO `transactions` VALUES ('11', 'd49be252-2270-40cf-87d5-1a1b88290d2e', '26', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:52:10');
INSERT INTO `transactions` VALUES ('12', '0fc403a5-f894-4922-b786-d24f0d9df472', '7', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'REFUND', 'SUCCESS', '2024-10-11 01:52:54');
INSERT INTO `transactions` VALUES ('13', '9bf0cb37-1d6a-41c1-89a8-27d8419e424a', '27', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 01:59:58');
INSERT INTO `transactions` VALUES ('14', 'd4e7df38-0776-4f25-a779-ff0376f0f9da', '28', '150.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 02:03:09');
INSERT INTO `transactions` VALUES ('15', '3c9d9215-9374-43ea-8365-0ecb7cc773a7', '29', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 02:13:01');
INSERT INTO `transactions` VALUES ('16', '5eb41f90-2745-4fca-935d-c815ce74766a', '29', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'REFUND', 'SUCCESS', '2024-10-11 02:13:36');
INSERT INTO `transactions` VALUES ('17', 'a3caecb5-d653-4dde-b754-2a7ea5c017b8', '31', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:34:05');
INSERT INTO `transactions` VALUES ('18', '3a82f626-8d0c-4ce8-bd9f-f75208a51088', '32', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:34:22');
INSERT INTO `transactions` VALUES ('19', 'd4c0bc80-cc5f-4c4e-a80a-7c983f71122a', '33', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:34:35');
INSERT INTO `transactions` VALUES ('20', '823c96e8-2887-408b-80f0-fba7465a810f', '34', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:37:09');
INSERT INTO `transactions` VALUES ('21', '2b6d5c28-00eb-4052-9f3f-e830824e8502', '35', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:37:26');
INSERT INTO `transactions` VALUES ('22', 'a606b42f-ae77-499a-8754-f78b04816337', '36', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:39:08');
INSERT INTO `transactions` VALUES ('23', 'a54e643e-8d10-467a-a974-8d62b205cb13', '37', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 05:48:38');
INSERT INTO `transactions` VALUES ('24', 'ad7e9aba-6c66-4136-9b88-2e995a7000e9', '38', '100.00', 'CNY', 'customer_account_001', 'store_account_001', 'PAYMENT', 'SUCCESS', '2024-10-11 06:08:55');
