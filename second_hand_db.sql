/*
Navicat MySQL Data Transfer

Source Server         : MYSQL
Source Server Version : 80041
Source Host           : localhost:3306
Source Database       : second_hand_db

Target Server Type    : MYSQL
Target Server Version : 80041
File Encoding         : 65001

Date: 2026-09-24 09:33:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';

-- ----------------------------
-- Records of t_cart
-- ----------------------------
INSERT INTO `t_cart` VALUES ('2', '3', '31', '2026-08-24 01:34:47');
INSERT INTO `t_cart` VALUES ('3', '2', '29', '2026-08-24 02:04:46');
INSERT INTO `t_cart` VALUES ('5', '2', '27', '2026-09-08 16:24:17');
INSERT INTO `t_cart` VALUES ('6', '2', '21', '2026-09-08 16:24:41');
INSERT INTO `t_cart` VALUES ('8', '2', '34', '2026-09-24 08:23:28');

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(30) NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT '0' COMMENT '排序权重',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类表';

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES ('1', '数码', '1', '2026-08-08 15:44:45');
INSERT INTO `t_category` VALUES ('2', '书籍', '2', '2026-08-08 15:44:45');
INSERT INTO `t_category` VALUES ('3', '服饰', '3', '2026-08-08 15:44:45');
INSERT INTO `t_category` VALUES ('4', '生活用品', '4', '2026-08-08 15:44:45');
INSERT INTO `t_category` VALUES ('5', '其他', '5', '2026-08-08 15:44:45');

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL COMMENT '商品ID',
  `from_user_id` int NOT NULL COMMENT '发送者ID',
  `to_user_id` int NOT NULL COMMENT '接收者ID（卖家）',
  `content` text NOT NULL COMMENT '留言内容',
  `reply` text COMMENT '卖家回复',
  `status` tinyint(1) DEFAULT '0' COMMENT '0未回复 1已回复',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_from_user_id` (`from_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='留言表';

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES ('1', '1', '3', '2', '多久能发货', '下午五点前下单当天就发货', '1', '2026-08-24 01:31:05');
INSERT INTO `t_message` VALUES ('2', '27', '2', '1', '有无收纳袋', null, '0', '2026-09-08 16:23:04');
INSERT INTO `t_message` VALUES ('3', '34', '2', '3', '有几个', '20个', '1', '2026-09-24 08:24:38');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `product_id` int NOT NULL COMMENT '商品ID',
  `buyer_id` int NOT NULL COMMENT '买方ID',
  `seller_id` int NOT NULL COMMENT '卖方ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '成交总价',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态:0待付款,1待发货,2待收货,3已完成,4已取消',
  `buyer_remark` varchar(255) DEFAULT NULL COMMENT '买家备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`),
  CONSTRAINT `fk_order_buyer` FOREIGN KEY (`buyer_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `fk_order_product` FOREIGN KEY (`product_id`) REFERENCES `t_product` (`id`),
  CONSTRAINT `fk_order_seller` FOREIGN KEY (`seller_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('1', '20260824013359539', '1', '3', '2', '66.00', '3', '请尽快发货', '2026-08-24 01:33:59', '2026-08-24 01:36:38');
INSERT INTO `t_order` VALUES ('2', '20260824021511193', '16', '2', '4', '29.00', '1', '', '2026-08-24 02:15:11', '2026-08-24 02:15:11');
INSERT INTO `t_order` VALUES ('3', '20260924082549967', '34', '2', '3', '10.00', '3', '', '2026-09-24 08:25:49', '2026-09-24 08:26:32');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `title` varchar(100) NOT NULL COMMENT '商品标题',
  `description` text COMMENT '商品详细描述',
  `price` decimal(10,2) NOT NULL COMMENT '售价',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `category_id` int DEFAULT NULL COMMENT '所属分类ID',
  `seller_id` int NOT NULL COMMENT '发布者ID',
  `images` varchar(500) DEFAULT NULL COMMENT '图片URL(逗号分隔)',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态:0待审核,1出售中,2已下架,3已售出',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_seller_id` (`seller_id`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `t_category` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_product_seller` FOREIGN KEY (`seller_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('1', '洗面奶', '新加坡天然有机个护品牌，清爽控油，全新未拆封', '66.00', '80.00', '4', '2', '/uploads/products/1787503303993_9461.jpg', '3', '18', '2026-08-24 00:41:56', '2026-08-24 02:16:03');
INSERT INTO `t_product` VALUES ('2', 'iPhone 15 Pro Max ', '99新，电池健康度98%，全套包装配件齐全，无任何划痕', '6999.00', '9999.00', '1', '2', '/uploads/products/1787503610477_1564.jpg', '2', '2', '2026-08-24 00:46:53', '2026-09-08 16:12:55');
INSERT INTO `t_product` VALUES ('3', '《深入理解Java虚拟机》周志明 第3版', '9成新，无笔记无划线，经典JVM必读', '45.00', '129.00', '2', '2', '/uploads/products/1787503668849_4180.jpg', '1', '0', '2026-08-24 00:47:51', '2026-08-24 00:51:34');
INSERT INTO `t_product` VALUES ('4', 'Nike Air Force 1 白色板鞋', '宽松版型，纯棉舒适，洗过两次，无变形', '159.00', '459.00', '3', '2', '/uploads/products/1787503711300_6327.jpg', '1', '1', '2026-08-24 00:48:34', '2026-08-24 01:26:12');
INSERT INTO `t_product` VALUES ('5', '宜家 毕利书架 白色', '高1.2m，状态良好，轻微使用痕迹，可拆卸搬运', '199.00', '499.00', '4', '2', '/uploads/products/1787503771946_8172.jpg', '1', '3', '2026-08-24 00:49:44', '2026-08-24 02:16:31');
INSERT INTO `t_product` VALUES ('6', '迪士尼玩偶 星黛露 全新带吊牌', '正品，高约30cm，全新未拆，送礼自用皆可', '159.00', '359.00', '5', '2', '/uploads/products/1787503830658_5238.jpg', '1', '2', '2026-08-24 00:50:33', '2026-08-24 01:16:50');
INSERT INTO `t_product` VALUES ('7', '华为MatePad 11 平板电脑', '8+128G，骁龙865，几乎全新，带原装笔和键盘套', '1899.00', '3299.00', '1', '3', '/uploads/products/1787504062502_6860.jpg', '1', '1', '2026-08-24 00:54:26', '2026-08-24 01:51:04');
INSERT INTO `t_product` VALUES ('8', '《算法导论》CLRS 第三版', '有少量笔记，整体干净，计算机必读经典', '30.00', '128.00', '2', '3', '/uploads/products/1787504107940_3997.jpg', '1', '0', '2026-08-24 00:55:09', '2026-08-24 00:57:43');
INSERT INTO `t_product` VALUES ('9', 'Adidas 三叶草卫衣 黑色', '宽松版型，纯棉舒适，洗过两次，无变形', '159.00', '459.00', '3', '3', '/uploads/products/1787504149089_1688.jpg', '1', '1', '2026-08-24 00:55:52', '2026-08-24 01:26:16');
INSERT INTO `t_product` VALUES ('10', '美的 1.5L 电水壶 304不锈钢', '烧水快速，保温效果很好，用过三个月，基本如新', '49.00', '129.00', '4', '3', '/uploads/products/1787504189613_5497.jpg', '1', '0', '2026-08-24 00:56:34', '2026-08-24 00:57:45');
INSERT INTO `t_product` VALUES ('11', '乐高 积木 法拉利模型 42143', '全新未拆封，盒子有一角轻微压痕，不影响内部', '999.00', '1699.00', '5', '3', '/uploads/products/1787504230597_9456.jpg', '1', '0', '2026-08-24 00:57:15', '2026-08-24 00:57:46');
INSERT INTO `t_product` VALUES ('12', 'AirPods Pro 2 降噪耳机', '国行正品，使用半年，充电仓有轻微使用痕迹，功能完好', '1299.00', '1899.00', '1', '4', '/uploads/products/1787504379503_2394.jpg', '1', '0', '2026-08-24 00:59:41', '2026-08-24 01:03:15');
INSERT INTO `t_product` VALUES ('13', '《Spring实战》第5版', '学习Spring框架必备，几乎全新', '35.00', '99.00', '2', '4', '/uploads/products/1787504426631_2268.jpg', '1', '0', '2026-08-24 01:00:28', '2026-08-24 01:03:16');
INSERT INTO `t_product` VALUES ('14', 'Uniqlo 摇粒绒外套', '保暖百搭，浅灰色，穿过一次，几乎没有痕迹', '99.00', '299.00', '3', '4', '/uploads/products/1787504463180_835.jpg', '1', '1', '2026-08-24 01:01:05', '2026-08-24 01:26:21');
INSERT INTO `t_product` VALUES ('15', '收纳组合 抽屉式储物箱', '半透明材质，全新未使用，可以分类整理各种杂物', '79.00', '199.00', '4', '4', '/uploads/products/1787504520419_7659.jpg', '1', '0', '2026-08-24 01:02:04', '2026-08-24 01:03:17');
INSERT INTO `t_product` VALUES ('16', '鼠标垫', '超大尺寸，边缘锁边工艺，全新未拆封', '29.00', '79.00', '5', '4', '/uploads/products/1787504573163_5541.jpg', '3', '3', '2026-08-24 01:02:54', '2026-08-24 02:15:11');
INSERT INTO `t_product` VALUES ('17', '罗技MX Master 3S 鼠标', '石墨黑，极简设计，人体工学，使用不到一个月', '499.00', '799.00', '1', '1', '/uploads/products/1787504707868_7429.jpg', '1', '0', '2026-08-24 01:05:11', '2026-08-24 01:07:29');
INSERT INTO `t_product` VALUES ('18', '索尼 WH-1000XM5 降噪耳机', '旗舰降噪，极简包装，几乎全新，只拆封试听', '1699.00', '2599.00', '1', '1', '/uploads/products/1787504750491_1756.jpg', '1', '2', '2026-08-24 01:05:52', '2026-08-24 01:26:07');
INSERT INTO `t_product` VALUES ('19', '小米手环 8 Pro', '全新未拆封，黑色，带NFC功能，全面屏大屏手环', '299.00', '499.00', '1', '1', '/uploads/products/1787504838715_3123.jpg', '1', '3', '2026-08-24 01:07:20', '2026-08-24 02:14:20');
INSERT INTO `t_product` VALUES ('20', '《数据结构与算法分析》', '经典教材，代码清晰，页面完好', '25.00', '79.00', '2', '1', '/uploads/products/1787504922088_9652.jpg', '1', '1', '2026-08-24 01:08:44', '2026-09-24 08:36:49');
INSERT INTO `t_product` VALUES ('21', '《三体》全集（三册）', '科幻经典，全新未拆封，精装版', '40.00', '93.00', '2', '1', '/uploads/products/1787504967504_2313.jpg', '1', '4', '2026-08-24 01:09:29', '2026-09-17 09:57:11');
INSERT INTO `t_product` VALUES ('22', '《百年孤独》马尔克斯', '经典文学作品，9成新，无笔记', '20.00', '65.00', '2', '1', '/uploads/products/1787505001747_105.jpg', '1', '1', '2026-08-24 01:10:03', '2026-08-24 01:26:00');
INSERT INTO `t_product` VALUES ('23', 'Levi\'s 501 经典牛仔裤 W32/L32', '原色牛仔裤，版型经典，仅试穿一次', '199.00', '599.00', '3', '1', '/uploads/products/1787505054049_9442.jpg', '1', '3', '2026-08-24 01:10:56', '2026-08-24 01:26:18');
INSERT INTO `t_product` VALUES ('24', 'Converse 1970s 帆布鞋 黑色', '经典款，穿不到五次，保留完好，干净无破损', '249.00', '569.00', '3', '1', '/uploads/products/1787505089862_2670.jpg', '1', '0', '2026-08-24 01:11:32', '2026-08-24 01:16:22');
INSERT INTO `t_product` VALUES ('25', 'FILA 斐乐复古运动鞋 白色', '复古风格，舒适百搭，全新仅试穿', '329.00', '799.00', '3', '1', '/uploads/products/1787505123258_7258.jpg', '1', '3', '2026-08-24 01:12:04', '2026-08-24 01:26:47');
INSERT INTO `t_product` VALUES ('26', '九阳 空气炸锅', '懒人必备，健康无油，使用不超过5次', '189.00', '399.00', '4', '1', '/uploads/products/1787505157048_869.jpg', '1', '1', '2026-08-24 01:12:38', '2026-08-24 01:25:58');
INSERT INTO `t_product` VALUES ('27', '瑜伽垫 加宽加厚', '粉色，很软很舒适，送绑带，全新未使用', '39.00', '99.00', '5', '1', '/uploads/products/1787505216626_4091.jpg', '1', '3', '2026-08-24 01:13:39', '2026-09-08 16:24:15');
INSERT INTO `t_product` VALUES ('28', '小米米家台灯 1S 智能护眼灯', '几乎全新，光线均匀，支持智能控制', '99.00', '199.00', '4', '1', '/uploads/products/1787505259655_2084.jpg', '1', '0', '2026-08-24 01:14:21', '2026-08-24 01:16:25');
INSERT INTO `t_product` VALUES ('29', '天文望远镜 星特朗', '入门级天文望远镜，配件齐全，只用过两次', '399.00', '899.00', '5', '1', '/uploads/products/1787505300855_579.jpg', '1', '2', '2026-08-24 01:15:03', '2026-08-24 02:04:54');
INSERT INTO `t_product` VALUES ('30', '尤克里里 23寸 桃花芯木', '初学者必备，音色温暖，几乎全新，送调音器和包', '199.00', '499.00', '5', '1', '/uploads/products/1787505332596_8693.jpg', '1', '3', '2026-08-24 01:15:34', '2026-09-08 16:04:01');
INSERT INTO `t_product` VALUES ('31', '折叠桌 宿舍床上桌', '实木色，可折叠不占空间，放床上放东西很方便', '25.00', '69.00', '4', '1', '/uploads/products/1787505367392_8977.jpg', '1', '13', '2026-08-24 01:16:12', '2026-09-17 10:10:06');
INSERT INTO `t_product` VALUES ('32', '收纳箱', '大号收纳箱，九成新无破损', '20.00', '40.00', '4', '3', '/uploads/products/1787506757661_9284.jpg', '2', '1', '2026-08-24 01:40:45', '2026-08-24 01:41:37');
INSERT INTO `t_product` VALUES ('33', '宿舍小风扇', '宿舍用小风扇，可以夹在床头，三档风力，USB充电，用了半个学期，功能完好，毕业了便宜出。', '15.00', '45.00', '4', '2', '/uploads/products/1788855469104_367.png', '1', '3', '2026-09-08 16:17:57', '2026-09-24 08:27:27');
INSERT INTO `t_product` VALUES ('34', '宿舍折叠衣架', '宿舍用的折叠衣架，搬寝室时候买的，现在毕业了用不上。展开能挂不少衣服，收起来不占地方，塞柜子缝里就行。用了不到一年，没啥损坏，就是正常使用痕迹。', '10.00', '19.00', '4', '3', '/uploads/products/1789610006865_1895.jpg', '3', '19', '2026-09-17 09:54:16', '2026-09-24 08:26:45');
INSERT INTO `t_product` VALUES ('35', '宿舍床头挂篮', '宿舍床头挂篮，用了大概一学期，质量挺好的，能放手机、书、纸巾啥的，挺能装。因为要毕业了懒得带走，便宜出。没有破损，就是有点落灰，擦擦就行。', '9.00', '25.00', '4', '2', '/uploads/products/1790210007420_388.jpg', '1', '4', '2026-09-24 08:33:33', '2026-09-24 08:37:40');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码(MD5加密)',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) DEFAULT 'user' COMMENT '角色:user/admin',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态:1正常,0封禁',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'admin123', null, null, 'admin', '1', '2026-08-08 15:44:45');
INSERT INTO `t_user` VALUES ('2', 'AA', '123', '1234567890', null, 'user', '1', '2026-08-23 23:58:13');
INSERT INTO `t_user` VALUES ('3', 'BB', '123', '2345678901', null, 'user', '1', '2026-08-24 00:36:34');
INSERT INTO `t_user` VALUES ('4', 'CC', '123', '3456789012', null, 'user', '1', '2026-08-24 00:37:20');
