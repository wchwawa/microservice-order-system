/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80038
Source Host           : localhost:3306
Source Database       : store_db

Target Server Type    : MYSQL
Target Server Version : 80038
File Encoding         : 65001

Date: 2024-10-11 14:08:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `warehouse_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`),
  CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of inventory
-- ----------------------------
INSERT INTO `inventory` VALUES ('1', '1', '1', '24');
INSERT INTO `inventory` VALUES ('2', '1', '2', '299');
INSERT INTO `inventory` VALUES ('3', '2', '1', '20');
INSERT INTO `inventory` VALUES ('4', '2', '3', '40');

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `price` double NOT NULL,
  `warehouse_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  KEY `warehouse_id` (`warehouse_id`),
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `orderitem_ibfk_3` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of orderitem
-- ----------------------------
INSERT INTO `orderitem` VALUES ('1', '5', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('2', '6', '1', '47', '100', '1');
INSERT INTO `orderitem` VALUES ('3', '6', '1', '13', '100', '2');
INSERT INTO `orderitem` VALUES ('4', '7', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('5', '8', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('6', '9', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('7', '10', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('8', '11', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('9', '12', '2', '1', '150', '1');
INSERT INTO `orderitem` VALUES ('10', '13', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('11', '14', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('12', '15', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('14', '17', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('16', '19', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('17', '20', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('18', '21', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('19', '22', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('20', '23', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('21', '24', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('22', '25', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('23', '26', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('24', '27', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('25', '28', '2', '1', '150', '1');
INSERT INTO `orderitem` VALUES ('26', '29', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('28', '31', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('29', '32', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('30', '33', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('31', '34', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('32', '35', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('33', '36', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('34', '37', '1', '1', '100', '1');
INSERT INTO `orderitem` VALUES ('35', '38', '1', '1', '100', '1');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', '1', '100.00', 'cancelled', '2024-10-10 09:16:23');
INSERT INTO `orders` VALUES ('2', '1', '750.00', 'cancelled', '2024-10-10 09:16:31');
INSERT INTO `orders` VALUES ('3', '1', '100.00', 'cancelled', '2024-10-10 09:39:43');
INSERT INTO `orders` VALUES ('4', '1', '100.00', 'cancelled', '2024-10-10 09:52:10');
INSERT INTO `orders` VALUES ('5', '1', '100.00', 'cancelled', '2024-10-10 15:43:47');
INSERT INTO `orders` VALUES ('6', '1', '6000.00', 'cancelled', '2024-10-10 15:43:57');
INSERT INTO `orders` VALUES ('7', '1', '100.00', 'cancelled', '2024-10-10 16:13:25');
INSERT INTO `orders` VALUES ('8', '1', '100.00', 'cancelled', '2024-10-10 16:13:41');
INSERT INTO `orders` VALUES ('9', '1', '100.00', 'paid', '2024-10-10 16:14:03');
INSERT INTO `orders` VALUES ('10', '1', '100.00', 'paid', '2024-10-10 16:15:56');
INSERT INTO `orders` VALUES ('11', '1', '100.00', 'paid', '2024-10-10 16:21:03');
INSERT INTO `orders` VALUES ('12', '1', '150.00', 'paid', '2024-10-10 16:21:22');
INSERT INTO `orders` VALUES ('13', '1', '100.00', 'paid', '2024-10-10 16:27:13');
INSERT INTO `orders` VALUES ('14', '1', '100.00', 'paid', '2024-10-10 16:44:19');
INSERT INTO `orders` VALUES ('15', '1', '100.00', 'paid', '2024-10-11 01:11:19');
INSERT INTO `orders` VALUES ('17', '1', '100.00', 'paid', '2024-10-11 01:14:48');
INSERT INTO `orders` VALUES ('19', '1', '100.00', 'paid', '2024-10-11 01:16:50');
INSERT INTO `orders` VALUES ('20', '1', '100.00', 'paid', '2024-10-11 01:19:54');
INSERT INTO `orders` VALUES ('21', '1', '100.00', 'paid', '2024-10-11 01:19:57');
INSERT INTO `orders` VALUES ('22', '1', '100.00', 'paid', '2024-10-11 01:19:58');
INSERT INTO `orders` VALUES ('23', '1', '100.00', 'paid', '2024-10-11 01:19:59');
INSERT INTO `orders` VALUES ('24', '1', '100.00', 'paid', '2024-10-11 01:20:00');
INSERT INTO `orders` VALUES ('25', '1', '100.00', 'paid', '2024-10-11 01:20:02');
INSERT INTO `orders` VALUES ('26', '1', '100.00', 'paid', '2024-10-11 01:52:10');
INSERT INTO `orders` VALUES ('27', '1', '100.00', 'paid', '2024-10-11 01:59:58');
INSERT INTO `orders` VALUES ('28', '1', '150.00', 'paid', '2024-10-11 02:03:09');
INSERT INTO `orders` VALUES ('29', '1', '100.00', 'cancelled', '2024-10-11 02:13:01');
INSERT INTO `orders` VALUES ('31', '1', '100.00', 'paid', '2024-10-11 05:34:05');
INSERT INTO `orders` VALUES ('32', '1', '100.00', 'paid', '2024-10-11 05:34:22');
INSERT INTO `orders` VALUES ('33', '1', '100.00', 'paid', '2024-10-11 05:34:35');
INSERT INTO `orders` VALUES ('34', '1', '100.00', 'paid', '2024-10-11 05:37:09');
INSERT INTO `orders` VALUES ('35', '1', '100.00', 'paid', '2024-10-11 05:37:26');
INSERT INTO `orders` VALUES ('36', '1', '100.00', 'paid', '2024-10-11 05:39:08');
INSERT INTO `orders` VALUES ('37', '1', '100.00', 'paid', '2024-10-11 05:48:38');
INSERT INTO `orders` VALUES ('38', '1', '100.00', 'paid', '2024-10-11 06:08:55');

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES ('1', 'item A', 'This is description of item A', '100.00');
INSERT INTO `products` VALUES ('2', 'item B', 'This is description of item B', '150.00');
INSERT INTO `products` VALUES ('3', 'item C', 'This is description of item C', '200.00');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'customer', '$2a$10$u1VblnmzPqjhbcJq0ZAESOZxO.gJyefHCXFkVf.9wCS8VcqnO4cb6', 'customer@example.com', '2024-10-10 05:26:31');
INSERT INTO `users` VALUES ('2', '1313', '$2a$10$aj7suK2sZn7n/1S3FkfK6.0TY4F9JJGCB0svkMqiIYhc/MNlo/Yay', '1111@kkk.com', '2024-10-10 09:27:42');

-- ----------------------------
-- Table structure for warehouses
-- ----------------------------
DROP TABLE IF EXISTS `warehouses`;
CREATE TABLE `warehouses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of warehouses
-- ----------------------------
INSERT INTO `warehouses` VALUES ('1', 'warehouse 1', 'location 1');
INSERT INTO `warehouses` VALUES ('2', 'warehouse 2', 'location 2');
