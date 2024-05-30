# ************************************************************
# CRMEB JAVA 外贸版多商户 v1.1 sql
# 安装完后
# 平台端登录 admin / 000000
# 商户端登录 stivepeim@outlook.com / 000000
# 商城端登录 18292417675 / Crmeb@123456
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table eb_activity_product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_activity_product`;

CREATE TABLE `eb_activity_product` (
  `aid` int(11) NOT NULL COMMENT '活动id',
  `pro_id` int(11) NOT NULL COMMENT '商品id',
  `pro_image` varchar(256) NOT NULL DEFAULT '' COMMENT '活动商品图片',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  PRIMARY KEY (`aid`,`pro_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='活动商品关联表';

LOCK TABLES `eb_activity_product` WRITE;
/*!40000 ALTER TABLE `eb_activity_product` DISABLE KEYS */;

INSERT INTO `eb_activity_product` (`aid`, `pro_id`, `pro_image`, `sort`)
VALUES
	(1,6,'',999),
	(1,7,'',999),
	(1,8,'',999),
	(1,9,'',999),
	(1,10,'',999),
	(2,6,'',999),
	(2,8,'',999),
	(2,9,'',999),
	(2,10,'',999),
	(3,4,'',999),
	(3,5,'',999),
	(3,6,'',999),
	(3,7,'',999),
	(3,8,'',999);

/*!40000 ALTER TABLE `eb_activity_product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_bill
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_bill`;

CREATE TABLE `eb_bill` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户uid',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户id',
  `link_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联id',
  `order_no` varchar(32) NOT NULL DEFAULT '' COMMENT '关联订单',
  `pm` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '0 = 支出 1 = 获得',
  `amount` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '金额',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '类型：pay_order-订单支付,refund_order-订单退款',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `pm` (`pm`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='帐单表';



# Dump of table eb_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_category`;

CREATE TABLE `eb_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `path` varchar(255) NOT NULL DEFAULT '/0/' COMMENT '路径',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `type` smallint(2) DEFAULT '1' COMMENT '类型，2 附件分类， 4 设置分类，6 配置分类， ',
  `url` varchar(255) DEFAULT '' COMMENT '地址',
  `extra` text COMMENT '扩展字段 Jsos格式',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态, 1正常，0失效',
  `sort` int(5) NOT NULL DEFAULT '99999' COMMENT '排序',
  `owner` int(11) DEFAULT '-1' COMMENT '分类所属：-1平台，商户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `status+pid` (`pid`,`status`) USING BTREE,
  KEY `id+status+url` (`path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分类表';

LOCK TABLES `eb_category` WRITE;
/*!40000 ALTER TABLE `eb_category` DISABLE KEYS */;

INSERT INTO `eb_category` (`id`, `pid`, `path`, `name`, `type`, `url`, `extra`, `status`, `sort`, `owner`, `create_time`, `update_time`)
VALUES
	(81,0,'/0/','管理端后台配置',6,'pcAdmin config','64',1,1,-1,'2020-05-20 10:02:57','2022-06-10 17:38:16'),
	(93,81,'/0/81/','站点配置',6,'站点配置','64',1,1,-1,'2020-05-21 11:04:20','2022-04-28 14:20:15'),
	(94,100,'/0/100/','客服配置',6,'云智服','76',1,1,-1,'2020-05-21 11:04:37','2022-04-28 14:20:15'),
	(95,0,'/0/','商城配置',6,'商城配置','139',1,2,-1,'2020-05-21 11:10:20','2022-04-28 14:20:15'),
	(96,95,'/0/95/','商城基础配置',6,'商城基础配置','77',1,1,-1,'2020-05-21 11:10:40','2022-04-28 14:20:15'),
	(100,0,'/0/','应用配置',6,'应用配置',NULL,1,1,-1,'2020-05-21 12:31:49','2022-04-28 14:20:16'),
	(103,0,'/0/','支付配置',6,'支付配置','140',1,1,-1,'2020-05-21 12:33:36','2022-04-28 14:20:16'),
	(108,0,'/0/','文件上传配置',6,'文件上传配置',NULL,1,1,-1,'2020-05-21 12:35:16','2022-04-28 14:20:16'),
	(109,108,'/0/108/','基础配置',6,'基础配置','108',1,1,-1,'2020-05-21 12:35:28','2022-04-28 14:20:16'),
	(110,108,'/0/108/','阿里云配置',6,'阿里云配置','81',1,1,-1,'2020-05-21 12:36:01','2022-04-28 14:20:16'),
	(111,108,'/0/108/','七牛云配置',6,'七牛云配置','82',1,1,-1,'2020-05-21 12:36:12','2022-04-28 14:20:16'),
	(112,108,'/0/108/','腾讯云配置',6,'腾讯云配置','83',1,1,-1,'2020-05-21 12:36:22','2022-04-28 14:20:16'),
	(500,0,'/0/','第三方接口设置',6,'short_letter_switch',NULL,1,1,-1,'2020-12-10 10:58:25','2022-04-28 14:20:16'),
	(501,500,'/0/500/','短信配置',6,'short_letter_switch','111',1,1,-1,'2020-12-10 10:59:08','2022-04-28 14:20:16'),
	(503,500,'/0/500/','阿里云物流查询',6,'logistics_select','128',1,1,-1,'2020-12-10 11:00:51','2022-04-28 14:20:17'),
	(693,500,'/0/500/','小票打印(易联云)',6,'yilianyun','143',1,1,-1,'2021-11-27 16:10:47','2022-04-28 14:20:17'),
	(720,0,'/0/','金刚区',2,'url',NULL,0,3,-1,'2021-12-25 10:33:41','2022-04-22 12:06:36'),
	(741,103,'/0/103/','Paypal支付',6,'Paypal','147',1,1,-1,'2022-01-22 16:14:49','2022-04-28 14:20:17'),
	(742,100,'/0/100/','Facebook',6,'Meta(Facebook)','144',1,1,-1,'2022-01-22 16:17:29','2022-04-28 14:20:17'),
	(743,100,'/0/100/','Twitter',6,'Twitter','145',1,1,-1,'2022-01-22 16:17:55','2022-04-28 14:20:17'),
	(744,100,'/0/100/','Goggle',6,'Goggle','146',1,1,-1,'2022-01-22 16:18:16','2022-04-28 14:20:17'),
	(745,100,'/0/100/','游客',6,'visitor','148',1,1,-1,'2022-02-11 18:30:48','2022-04-28 14:20:17'),
	(752,100,'/0/100/','商品复制',6,'商品复制','122',1,1,-1,'2022-03-31 09:44:40','2022-04-28 14:20:17'),
	(768,0,'/0/','logo',2,'url',NULL,0,1,-1,'2022-04-25 12:04:44','2022-04-28 12:24:52'),
	(772,0,'/0/','大图首页',2,'url',NULL,0,1,-1,'2022-04-29 15:47:11','2022-04-29 15:47:11'),
	(774,0,'/0/','商品图片',2,'url',NULL,0,1,-1,'2022-05-11 17:28:31','2022-05-11 17:28:31'),
	(781,0,'/0/','商品图片',2,'url',NULL,0,1,3,'2022-05-13 16:28:59','2022-05-13 16:28:59'),
	(782,0,'/0/','Test',2,'url',NULL,0,1,3,'2022-05-13 16:31:28','2022-05-13 16:31:28'),
	(783,781,'/0/781/','蔬菜',2,'url',NULL,0,1,3,'2022-05-13 16:32:25','2022-05-13 16:32:25'),
	(787,95,'/0/95/','PC商城配置',6,'PC商城配置','163',1,1,-1,'2022-05-23 17:32:35','2022-05-23 17:40:42'),
	(791,0,'/0/','商品分类',2,'url',NULL,0,1,-1,'2022-06-06 14:28:46','2022-06-06 14:28:46'),
	(792,791,'/0/791/','CONSUMER ELECTRONICS',2,'url',NULL,0,1,-1,'2022-06-06 14:29:11','2022-06-06 14:29:11'),
	(793,791,'/0/791/','APPAREL & LINGERIE',2,'url',NULL,0,1,-1,'2022-06-06 14:29:28','2022-06-06 14:29:28'),
	(794,791,'/0/791/','HOME APPLIANCE',2,'url',NULL,0,1,-1,'2022-06-06 14:29:41','2022-06-06 14:29:41'),
	(795,791,'/0/791/','SPORTS & OUTDOORS',2,'url',NULL,0,1,-1,'2022-06-06 14:29:55','2022-06-06 14:29:55'),
	(796,791,'/0/791/','HOME & PET SUPPLIES',2,'url',NULL,0,1,-1,'2022-06-06 14:30:08','2022-06-06 14:30:08'),
	(797,792,'/0/791/792/','Accessories',2,'url',NULL,0,1,-1,'2022-06-06 14:31:01','2022-06-06 14:31:01'),
	(798,792,'/0/791/792/','Entertainments',2,'url',NULL,0,1,-1,'2022-06-06 14:47:20','2022-06-06 14:47:20'),
	(799,792,'/0/791/792/','Protectors',2,'url',NULL,0,1,-1,'2022-06-06 14:47:36','2022-06-06 14:47:36'),
	(800,792,'/0/791/792/','Communication',2,'url',NULL,0,1,-1,'2022-06-06 14:47:48','2022-06-06 14:47:48'),
	(801,792,'/0/791/792/','Connected health',2,'url',NULL,0,1,-1,'2022-06-06 14:48:01','2022-06-06 14:48:01'),
	(802,792,'/0/791/792/','Gaming',2,'url',NULL,0,1,-1,'2022-06-06 14:48:15','2022-06-06 14:48:15'),
	(803,793,'/0/791/793/','Pajamas & socks',2,'url',NULL,0,1,-1,'2022-06-06 14:49:12','2022-06-06 14:49:12'),
	(804,793,'/0/791/793/','Apparel & accessories',2,'url',NULL,0,1,-1,'2022-06-06 14:49:40','2022-06-06 14:49:40'),
	(805,793,'/0/791/793/','Bottoms',2,'url',NULL,0,1,-1,'2022-06-06 14:50:08','2022-06-06 14:50:08'),
	(806,793,'/0/791/793/','Lingerie',2,'url',NULL,0,1,-1,'2022-06-06 14:50:23','2022-06-06 14:50:23'),
	(807,793,'/0/791/793/','Dress',2,'url',NULL,0,1,-1,'2022-06-06 14:50:36','2022-06-06 14:50:36'),
	(808,793,'/0/791/793/','T-shirt',2,'url',NULL,0,1,-1,'2022-06-06 14:50:48','2022-06-06 14:50:48'),
	(809,794,'/0/791/794/','Personal care',2,'url',NULL,0,1,-1,'2022-06-06 14:51:13','2022-06-06 14:51:13'),
	(810,794,'/0/791/794/','Living room',2,'url',NULL,0,1,-1,'2022-06-06 14:51:28','2022-06-06 14:51:28'),
	(811,794,'/0/791/794/','Dessert',2,'url',NULL,0,1,-1,'2022-06-06 14:51:41','2022-06-06 14:51:41'),
	(812,794,'/0/791/794/','Beverage',2,'url',NULL,0,1,-1,'2022-06-06 14:51:55','2022-06-06 14:51:55'),
	(813,794,'/0/791/794/','Processor',2,'url',NULL,0,1,-1,'2022-06-06 14:52:08','2022-06-06 14:52:08'),
	(814,794,'/0/791/794/','Top sales',2,'url',NULL,0,1,-1,'2022-06-06 14:52:22','2022-06-06 14:52:22'),
	(815,795,'/0/791/795/','Water sports',2,'url',NULL,0,1,-1,'2022-06-06 14:52:37','2022-06-06 14:52:37'),
	(816,795,'/0/791/795/','Yoga suit',2,'url',NULL,0,1,-1,'2022-06-06 14:52:53','2022-06-06 14:52:53'),
	(817,795,'/0/791/795/','Team Sports',2,'url',NULL,0,1,-1,'2022-06-06 14:53:49','2022-06-06 14:53:49'),
	(818,795,'/0/791/795/','Outdoor sports',2,'url',NULL,0,1,-1,'2022-06-06 14:54:22','2022-06-06 14:54:22'),
	(819,795,'/0/791/795/','Fitness & body building',2,'url',NULL,0,1,-1,'2022-06-06 14:54:48','2022-06-06 14:54:48'),
	(820,795,'/0/791/795/','Top sales SP',2,'url',NULL,0,1,-1,'2022-06-06 14:55:29','2022-06-06 14:55:29'),
	(821,796,'/0/791/796/','Pet supplies',2,'url',NULL,0,1,-1,'2022-06-06 14:56:59','2022-06-06 14:56:59'),
	(822,796,'/0/791/796/','Bedding',2,'url',NULL,0,1,-1,'2022-06-06 14:57:31','2022-06-06 14:57:31'),
	(823,796,'/0/791/796/','Organize',2,'url',NULL,0,1,-1,'2022-06-06 14:57:59','2022-06-06 14:57:59'),
	(824,796,'/0/791/796/','Cookware',2,'url',NULL,0,1,-1,'2022-06-06 14:58:25','2022-06-06 14:58:25'),
	(825,796,'/0/791/796/','Yiwu industry hub',2,'url',NULL,0,1,-1,'2022-06-06 14:59:03','2022-06-06 14:59:03'),
	(826,796,'/0/791/796/','Top sales HP',2,'url',NULL,0,1,-1,'2022-06-06 15:02:02','2022-06-06 15:02:02'),
	(827,103,'/0/103/','Stripe支付',6,'Stripe支付','164',1,1,-1,'2022-06-06 17:31:02','2022-06-06 17:31:10'),
	(828,0,'/0/','Cactray',2,'url',NULL,0,1,4,'2022-06-07 10:17:18','2022-06-07 10:17:18'),
	(829,0,'/0/','Products',2,'url',NULL,0,1,4,'2022-06-07 10:24:39','2022-06-07 10:24:39'),
	(830,829,'/0/829/','musicCap',2,'url',NULL,0,1,4,'2022-06-07 10:24:44','2022-06-07 10:24:44'),
	(831,834,'/0/834/','PC 首页轮播图',2,'url',NULL,0,1,-1,'2022-06-07 10:49:07','2022-06-08 09:44:50'),
	(832,829,'/0/829/','miniBag',2,'url',NULL,0,1,4,'2022-06-07 12:19:57','2022-06-07 12:19:57'),
	(833,829,'/0/829/','Backpack',2,'url',NULL,0,1,4,'2022-06-07 12:30:44','2022-06-07 12:30:44'),
	(834,0,'/0/','PC 版面素材',2,'url',NULL,0,1,-1,'2022-06-08 09:44:34','2022-06-08 09:44:34'),
	(835,834,'/0/834/','PC 活动banner',2,'url',NULL,0,1,-1,'2022-06-08 09:45:49','2022-06-08 09:45:49'),
	(836,834,'/0/834/','PC 店铺街',2,'url',NULL,0,1,-1,'2022-06-08 09:46:07','2022-06-08 09:46:07'),
	(837,829,'/0/829/','Durable men',2,'url',NULL,0,1,4,'2022-06-08 10:30:31','2022-06-08 10:30:31'),
	(838,829,'/0/829/','Mens Stainless',2,'url',NULL,0,1,4,'2022-06-08 10:36:35','2022-06-08 10:36:35'),
	(839,829,'/0/829/','Noise Cancelling',2,'url',NULL,0,1,4,'2022-06-08 10:48:29','2022-06-08 10:48:29'),
	(840,829,'/0/829/','Best Sellers In Ear ',2,'url',NULL,0,1,4,'2022-06-08 10:51:41','2022-06-08 10:51:41'),
	(841,0,'/0/','店铺素材',2,'url',NULL,0,1,4,'2022-06-08 11:07:01','2022-06-08 11:07:01'),
	(842,0,'/0/','推荐竖图',2,'url',NULL,0,1,-1,'2022-06-08 15:12:10','2022-06-08 15:12:10'),
	(843,829,'/0/829/','Pool Hi-Gloss',2,'url',NULL,0,1,4,'2022-06-08 18:24:43','2022-06-08 18:24:43'),
	(844,0,'/0/','商户背景',2,'url',NULL,0,1,2,'2022-06-16 11:41:24','2022-06-16 11:41:24'),
	(845,0,'/0/','门店素材',2,'url',NULL,0,9,1,'2022-06-17 17:25:39','2022-06-17 17:25:39'),
	(846,0,'/0/','商品素材',2,'url',NULL,0,8,1,'2022-06-17 17:25:51','2022-06-17 17:25:51'),
	(847,0,'/0/','订单状态',2,'url',NULL,0,1,-1,'2022-06-18 15:38:56','2022-06-18 15:38:56'),
	(848,0,'/0/','Banner',2,'url',NULL,0,1,-1,'2022-06-18 15:52:11','2022-06-18 15:52:11'),
	(849,0,'/0/','分类',2,'url',NULL,0,1,2,'2022-06-18 18:05:05','2022-06-18 18:05:05');

INSERT INTO `eb_category` (`pid`, `path`, `name`, `type`, `url`, `extra`, `status`, `sort`, `owner`, `create_time`, `update_time`)
VALUES
	(0, '/0/', '商品图', 2, 'url', NULL, 0, 1, 15, '2023-08-04 15:14:16', '2023-08-04 15:14:16'),
	(0, '/0/', '个人头像图片', 2, 'url', NULL, 0, 2, 15, '2023-08-04 15:14:29', '2023-08-04 15:14:39'),
	(103, '/0/103/', '微信支付', 6, 'wechatPay', '165', 1, 1, -1, '2023-08-07 09:59:49', '2023-08-07 10:00:20'),
	(0, '/0/', '服饰', 2, 'url', NULL, 0, 1, 22, '2023-08-15 14:53:50', '2023-08-15 14:53:50');


/*!40000 ALTER TABLE `eb_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_email_template
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_email_template`;

CREATE TABLE `eb_email_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject` varchar(30) NOT NULL DEFAULT '' COMMENT '主题',
  `text` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件正文',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件说明',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '类型',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='邮件模板表';

LOCK TABLES `eb_email_template` WRITE;
/*!40000 ALTER TABLE `eb_email_template` DISABLE KEYS */;

INSERT INTO `eb_email_template` (`id`, `subject`, `text`, `title`, `type`, `status`, `create_time`)
VALUES
	(1,'This is your Crmeb verificatio','Please enter this verification code on Crmeb when prompted {code}','公共验证码','验证码',1,'2022-02-17 10:43:43'),
	(2,'Crmeb pay success','you have successfully paid {price} in the foreign trade version of crmeb, the order number is: {orderNo}','支付成功通知','通知',1,'2022-02-17 12:04:22'),
	(3,'Crmeb order deliver','Your order {orderNo} has been shipped','订单发货通知','通知',1,'2022-02-17 12:26:39'),
	(4,'Merhcnat audit success','Your merchant has been successfully reviewed, login password is : {password}','商户申请审核成功通知','通知',1,'2022-02-17 12:26:39');

/*!40000 ALTER TABLE `eb_email_template` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_express
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_express`;

CREATE TABLE `eb_express` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '快递公司id',
  `code` varchar(50) NOT NULL DEFAULT '' COMMENT '快递公司简称',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '快递公司全称',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否显示',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否可用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE,
  KEY `is_show` (`is_show`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='快递公司表';

LOCK TABLES `eb_express` WRITE;
/*!40000 ALTER TABLE `eb_express` DISABLE KEYS */;

INSERT INTO `eb_express` (`id`, `code`, `name`, `sort`, `is_show`, `status`)
VALUES
	(1,'JTKD','捷特快递',0,1,1),
	(2,'ESHIPPER','EShipper',0,1,1),
	(3,'APLUSEX','Aplus物流',0,1,1),
	(4,'EXFRESH','安鲜达',0,1,1),
	(5,'XFEX','信丰快递',0,1,1),
	(6,'GLS','GLS',0,1,1),
	(7,'IHGYZ','韩国邮政',0,1,1),
	(8,'MHKD','民航快递',0,1,1),
	(9,'ZY_YJSD','友家速递(UCS)',0,1,1),
	(10,'YIMIDIDA','壹米滴答',0,1,1),
	(11,'ZY_TCM','通诚美中快递',0,1,1),
	(12,'ZY_OZF','欧洲疯',0,1,1),
	(13,'FKD','飞康达',0,1,1),
	(14,'YODEL','YODEL',0,1,1),
	(15,'CJKD','城际快递',0,1,1),
	(16,'RDSE','瑞典邮政',0,1,1),
	(17,'JITU','极兔速递',0,1,1),
	(18,'ZY_UCS','UCS合众快递',0,1,1),
	(19,'DHL_EN','DHL',0,1,1),
	(20,'AAE','AAE全球专递',0,1,1),
	(21,'IMTNKEMS','马提尼克EMS',0,1,1),
	(22,'ADAPOST','安达速递',0,1,1),
	(23,'AJ','安捷快递',0,1,1),
	(24,'ICKY','出口易',0,1,1),
	(25,'IQTWL','全通物流',0,1,1),
	(26,'SURE','速尔快递',0,1,1),
	(27,'AT','奥地利邮政',0,1,1),
	(28,'IAMYZ','阿曼邮政',0,1,1),
	(29,'ZY_AG','爱购转运',0,1,1),
	(30,'IAFHYZ','阿富汗邮政',0,1,1),
	(31,'IMLXYEMS','马来西亚EMS',0,1,1),
	(32,'JYWL','佳怡物流',0,1,1),
	(33,'ISTALBYZ','沙特阿拉伯邮政',0,1,1),
	(34,'DHL','DHL',0,1,1),
	(35,'ZTE','众通快递',0,1,1),
	(36,'BN','笨鸟国际',0,1,1),
	(37,'ZY_BT','百通物流',0,1,1),
	(38,'SFWL','盛丰物流',0,1,1),
	(39,'IALQDYZ','奥兰群岛邮政',0,1,1),
	(40,'NEDA','能达速递',0,1,1),
	(41,'BR','巴西邮政',0,1,1),
	(42,'IAEBNYYZ','阿尔巴尼亚邮政',0,1,1),
	(43,'SFC','三态速递',0,1,1),
	(44,'IWLYZ','文莱邮政',0,1,1),
	(45,'BDT','八达通',0,1,1),
	(46,'ZTO','中通快递',0,1,1),
	(47,'ZY_BL','百利快递',0,1,1),
	(48,'IGDLPDYZ','瓜德罗普岛邮政',0,1,1),
	(49,'ZY_BM','斑马物流',0,1,1),
	(50,'ZY_BH','贝海速递',0,1,1),
	(51,'IZBLTYZ','直布罗陀邮政',0,1,1),
	(52,'CA','加拿大邮政',0,1,1),
	(53,'SUNING','苏宁',0,1,1),
	(54,'CG','程光',0,1,1),
	(55,'IALBYZ','阿鲁巴邮政',0,1,1),
	(56,'BEL','比利时邮政',0,1,1),
	(57,'ZY_HCYD','皓晨优递',0,1,1),
	(58,'JGSD','京广速递',0,1,1),
	(59,'ILTWYZ','立陶宛邮政',0,1,1),
	(60,'ACS','ACS雅仕快递',0,1,1),
	(61,'HFWL','汇丰物流',0,1,1),
	(62,'ZY_CM','策马转运',0,1,1),
	(63,'ZY_EFS','EFS POST',0,1,1),
	(64,'ZY_XDKD','迅达快递',0,1,1),
	(65,'KYSY','跨越速运',0,1,1),
	(66,'FAST','快捷快递',0,1,1),
	(67,'DK','丹麦邮政',0,1,1),
	(68,'ILTWYYZ','拉脱维亚邮政',0,1,1),
	(69,'ADP','ADP Express Tracking',0,1,1),
	(70,'APAC','APAC',0,1,1),
	(71,'IBDLGYZ','波多黎各邮政',0,1,1),
	(72,'MLWL','明亮物流',0,1,1),
	(73,'RFD','如风达',0,1,1),
	(74,'YXKD','亿翔快递',0,1,1),
	(75,'IYNYZ','越南邮政',0,1,1),
	(76,'YTO','圆通速递',0,1,1),
	(77,'BILUYOUZHE','秘鲁邮政',0,1,1),
	(78,'IBLYZ','巴林邮政',0,1,1),
	(79,'IADLSQDYZ','安的列斯群岛邮政',0,1,1),
	(80,'IHSKSTYZ','哈萨克斯坦邮政',0,1,1),
	(81,'IMTNKYZ','马提尼克邮政',0,1,1),
	(82,'SHT','世华通物流',0,1,1),
	(83,'ZY_FD','飞碟快递',0,1,1),
	(84,'AJWL','安捷物流',0,1,1),
	(85,'AUSTRALIA','Australia Post Tracking',0,1,1),
	(86,'IELTLYYZ','厄立特里亚邮政',0,1,1),
	(87,'IASSDYZ','阿森松岛邮政',0,1,1),
	(88,'YFEX','越丰物流',0,1,1),
	(89,'EMS','EMS',0,1,1),
	(90,'ZTO56','中通快运(物流)',0,1,1),
	(91,'PCA','PCA Express',0,1,1),
	(92,'ZY_AUSE','澳世速递',0,1,1),
	(93,'ITGYZ','泰国邮政',0,1,1),
	(94,'JIUYE','九曳供应链',0,1,1),
	(95,'JXD','急先达',0,1,1),
	(96,'ZY_FLSD','风雷速递',0,1,1),
	(97,'SDWL','上大物流',0,1,1),
	(98,'WJWL','万家物流',0,1,1),
	(99,'YZPY','邮政快递包裹',0,1,1),
	(100,'IXGLDNYYZ','新喀里多尼亚邮政',0,1,1),
	(101,'IBLWYYZ','玻利维亚邮政',0,1,1),
	(102,'ZY_MST','美速通',0,1,1),
	(103,'HYLSD','好来运快递',0,1,1),
	(104,'ZY_FX','风行快递',0,1,1),
	(105,'ZY_FY','飞洋快递',0,1,1),
	(106,'DHLGM','DHL Global Mail',0,1,1),
	(107,'BHT','BHT快递',0,1,1),
	(108,'ISDYZ','苏丹邮政',0,1,1),
	(109,'IYMYZ','也门邮政',0,1,1),
	(110,'STWL','速腾快递',0,1,1),
	(111,'ZY_JDZY','骏达转运',0,1,1),
	(112,'GSD','共速达',0,1,1),
	(113,'GD','冠达',0,1,1),
	(114,'JYM','加运美',0,1,1),
	(115,'ZY_FG','飞鸽快递',0,1,1),
	(116,'ZY_HC','皓晨快递',0,1,1),
	(117,'ZY_AZY','澳转运',0,1,1),
	(118,'IBLNYZ','黎巴嫩邮政',0,1,1),
	(119,'IMLXYYZ','马来西亚邮政',0,1,1),
	(120,'ONWAY','昂威物流',0,1,1),
	(121,'BCWELT','BCWELT',0,1,1),
	(122,'ZY_XGX','新干线快递',0,1,1),
	(123,'JGWL','景光物流',0,1,1),
	(124,'IAJYZ','埃及邮政',0,1,1),
	(125,'ZY_LPZ','领跑者快递',0,1,1),
	(126,'ZY_XIYJ','西邮寄',0,1,1),
	(127,'ZYKD','众邮快递',0,1,1),
	(128,'IXLYZ','希腊邮政',0,1,1),
	(129,'PJKD','品骏快递',0,1,1),
	(130,'CSCY','长沙创一',0,1,1),
	(131,'IWZBKSTEMS','乌兹别克斯坦EMS',0,1,1),
	(132,'IBELSYZ','白俄罗斯邮政',0,1,1),
	(133,'GTO','国通快递',0,1,1),
	(134,'ZY_MXZY','美西转运',0,1,1),
	(135,'USPS','USPS美国邮政',0,1,1),
	(136,'ZY_YQ','云骑快递',0,1,1),
	(137,'IMLQSYZ','毛里求斯邮政',0,1,1),
	(138,'ZY_SONIC','Sonic-Ex速递',0,1,1),
	(139,'HQSY','环球速运',0,1,1),
	(140,'HXLWL','华夏龙物流',0,1,1),
	(141,'AOTSD','澳天速运',0,1,1),
	(142,'CCES','CCES快递',0,1,1),
	(143,'IHSYZ','黑山邮政',0,1,1),
	(144,'IYLYZ','伊朗邮政',0,1,1),
	(145,'ZY_HTAO','360hitao转运',0,1,1),
	(146,'ANGUILAYOU','安圭拉邮政',0,1,1),
	(147,'UPS','UPS',0,1,1),
	(148,'IASEBYYZ','埃塞俄比亚邮政',0,1,1),
	(149,'IGSDLJYZ','哥斯达黎加邮政',0,1,1),
	(150,'IXJPYZ','新加坡邮政',0,1,1),
	(151,'TNT','TNT快递',0,1,1),
	(152,'IE','爱尔兰邮政',0,1,1),
	(153,'IYMNYYZ','亚美尼亚邮政',0,1,1),
	(154,'FEDEX','FEDEX联邦(国内件）',0,1,1),
	(155,'IYDNXYYZ','印度尼西亚邮政',0,1,1),
	(156,'ITEQYZ','土耳其邮政',0,1,1),
	(157,'IAEJLYYZ','阿尔及利亚邮政',0,1,1),
	(158,'FTD','富腾达',0,1,1),
	(159,'DPD','DPD',0,1,1),
	(160,'JD','京东物流',0,1,1),
	(161,'CITY100','城市100',0,1,1),
	(162,'EMSGJ','EMS国际',0,1,1),
	(163,'AMAZON','亚马逊',0,1,1),
	(164,'SBWL','盛邦物流',0,1,1),
	(165,'IBJLYYZ','保加利亚邮政',0,1,1),
	(166,'ZY_HXKD','海星桥快递',0,1,1),
	(167,'JP','日本邮政',0,1,1),
	(168,'CDSTKY','成都善途速运',0,1,1),
	(169,'DIANNIAO','丹鸟快递',0,1,1),
	(170,'COE','COE快递',0,1,1),
	(171,'IWKLEMS','乌克兰EMS',0,1,1),
	(172,'WXWL','万象物流',0,1,1),
	(173,'ZY_XDSY','信达速运',0,1,1),
	(174,'QQYZ','全球邮政',0,1,1),
	(175,'FASTGO','速派快递',0,1,1),
	(176,'IBTD','宝通达',0,1,1),
	(177,'JLDT','嘉里物流',0,1,1),
	(178,'XYT','希优特',0,1,1),
	(179,'IWLGYZ','乌拉圭邮政',0,1,1),
	(180,'LB','龙邦快递',0,1,1),
	(181,'PANEX','泛捷快递',0,1,1),
	(182,'ZY_MGZY','美国转运',0,1,1),
	(183,'IMXGYZ','墨西哥邮政',0,1,1),
	(184,'MAXEEDEXPRESS','澳洲迈速快递',0,1,1),
	(185,'SAWL','圣安物流',0,1,1),
	(186,'ZY_YPW','云畔网',0,1,1),
	(187,'IGDLPDEMS','瓜德罗普岛EMS',0,1,1),
	(188,'DJ56','东骏快捷',0,1,1),
	(189,'ZY_FXSD','风行速递',0,1,1),
	(190,'YJWL','云聚物流',0,1,1),
	(191,'ISLFKYZ','斯洛伐克邮政',0,1,1),
	(192,'IBHYZ','波黑邮政',0,1,1),
	(193,'IBJSTYZ','巴基斯坦邮政',0,1,1),
	(194,'MB','民邦快递',0,1,1),
	(195,'ZY_BDA','八达网',0,1,1),
	(196,'YTKD','运通快递',0,1,1),
	(197,'IAGTYZ','阿根廷邮政',0,1,1),
	(198,'ZY_RDGJ','润东国际快线',0,1,1),
	(199,'ZY_SCS','SCS国际物流',0,1,1),
	(200,'ZY_BYECO','贝易购',0,1,1),
	(201,'IKTEYZ','卡塔尔邮政',0,1,1),
	(202,'IZLYZ','智利邮政',0,1,1),
	(203,'ILSBYZ','卢森堡邮政',0,1,1),
	(204,'ANE','安能物流',0,1,1),
	(205,'NF','南方',0,1,1),
	(206,'ILBYYZ','利比亚邮政',0,1,1),
	(207,'NL','荷兰邮政',0,1,1),
	(208,'IBYB','贝邮宝',0,1,1),
	(209,'RFEX','瑞丰速递',0,1,1),
	(210,'ZY_BEE','蜜蜂速递',0,1,1),
	(211,'YFHEX','原飞航物流',0,1,1),
	(212,'YUNDX','运东西',0,1,1),
	(213,'ASTEXPRESS','安世通快递',0,1,1),
	(214,'EWE','EWE',0,1,1),
	(215,'INRLYYZ','尼日利亚邮政',0,1,1),
	(216,'ZY_YSW','易送网',0,1,1),
	(217,'AOL','AOL（澳通）',0,1,1),
	(218,'IXJPEMS','新加坡EMS',0,1,1),
	(219,'ZY_YSSD','优晟速递',0,1,1),
	(220,'HPTEX','海派通物流公司',0,1,1),
	(221,'hq568','华强物流',0,1,1),
	(222,'ZY_QQEX','QQ-EX',0,1,1),
	(223,'IJEJSSTYZ','吉尔吉斯斯坦邮政',0,1,1),
	(224,'JAD','捷安达',0,1,1),
	(225,'IADLYYZ','澳大利亚邮政',0,1,1),
	(226,'SWCH','瑞士邮政',0,1,1),
	(227,'IKNDYYZ','克罗地亚邮政',0,1,1),
	(228,'IMLGYZ','摩洛哥邮政',0,1,1),
	(229,'IYSLYZ','以色列邮政',0,1,1),
	(230,'CTG','联合运通',0,1,1),
	(231,'BUDANYOUZH','不丹邮政',0,1,1),
	(232,'ZY_HJSD','豪杰速递',0,1,1),
	(233,'ZTKY','中铁快运',0,1,1),
	(234,'ITNSYZ','突尼斯邮政',0,1,1),
	(235,'ZY_HFMZ','汇丰美中速递',0,1,1),
	(236,'ITLNDHDBGE','特立尼达和多巴哥EMS',0,1,1),
	(237,'SUBIDA','速必达物流',0,1,1),
	(238,'QFKD','全峰快递',0,1,1),
	(239,'ZY_BOZ','败欧洲',0,1,1),
	(240,'ZY_HYSD','海悦速递',0,1,1),
	(241,'ZY_ZCSD','至诚速递',0,1,1),
	(242,'STO','申通快递',0,1,1),
	(243,'IBOLYZ','波兰邮政',0,1,1),
	(244,'IYWWL','燕文物流',0,1,1),
	(245,'RRS','日日顺物流',0,1,1),
	(246,'IWZBKSTYZ','乌兹别克斯坦邮政',0,1,1),
	(247,'YUEDANYOUZ','约旦邮政',0,1,1),
	(248,'ZY_LBZY','联邦转运FedRoad',0,1,1),
	(249,'ARAMEX','Aramex',0,1,1),
	(250,'ZY_TSZ','唐三藏转运',0,1,1),
	(251,'YXWL','宇鑫物流',0,1,1),
	(252,'ZY_QMT','全美通',0,1,1),
	(253,'ZENY','增益快递',0,1,1),
	(254,'DPEX','DPEX',0,1,1),
	(255,'IGLLYZ','格陵兰邮政',0,1,1),
	(256,'GTONG','广通',0,1,1),
	(257,'FEDEX_GJ','FEDEX联邦(国际件）',0,1,1),
	(258,'YFSD','亚风快递',0,1,1),
	(259,'UAPEX','全一快递',0,1,1),
	(260,'ONTRAC','ONTRAC',0,1,1),
	(261,'GJEYB','国际e邮宝',0,1,1),
	(262,'IXFLWL','小飞龙物流',0,1,1),
	(263,'AOMENYZ','澳门邮政',0,1,1),
	(264,'ZY_AOZ','爱欧洲',0,1,1),
	(265,'ZY_HTKE','365海淘客',0,1,1),
	(266,'HUISEN','汇森快运',0,1,1),
	(267,'QRT','全日通快递',0,1,1),
	(268,'SF','顺丰速运',0,1,1),
	(269,'IYGYZ','英国邮政',0,1,1),
	(270,'LHT','联昊通速递',0,1,1),
	(271,'INWYZ','挪威邮政',0,1,1),
	(272,'ZY_CTM','赤兔马转运',0,1,1),
	(273,'ZY_HXSY','华兴速运',0,1,1),
	(274,'ZY_SDKD','速达快递',0,1,1),
	(275,'ZY_RT','瑞天快递',0,1,1),
	(276,'ST','速通物流',0,1,1),
	(277,'ZY_TN','滕牛快递',0,1,1),
	(278,'IMJLGEMS','孟加拉国EMS',0,1,1),
	(279,'D4PX','递四方速递',0,1,1),
	(280,'ZY_TM','天马转运',0,1,1),
	(281,'ZY_TJ','天际快递',0,1,1),
	(282,'ZHQKD','汇强快递',0,1,1),
	(283,'IGJESD','俄速递',0,1,1),
	(284,'IBUY8','爱拜物流',0,1,1),
	(285,'SXJD','顺心捷达',0,1,1),
	(286,'ZYWL','中邮物流',0,1,1),
	(287,'ZY_ST','上腾快递',0,1,1),
	(288,'IDFWL','达方物流',0,1,1),
	(289,'IELSYZ','俄罗斯邮政',0,1,1),
	(290,'AYCA','澳邮专线',0,1,1),
	(291,'IDGYZ','德国邮政',0,1,1),
	(292,'INFYZ','南非邮政',0,1,1),
	(293,'JJKY','佳吉快运',0,1,1),
	(294,'UC','优速快递',0,1,1),
	(295,'IBDYZ','冰岛邮政',0,1,1),
	(296,'IXLYYZ','叙利亚邮政',0,1,1),
	(297,'ZY_OEJ','欧e捷',0,1,1),
	(298,'ZY_TPAK','TrakPak',0,1,1),
	(299,'IKNYYZ','肯尼亚邮政',0,1,1),
	(300,'ZY_TX','同心快递',0,1,1),
	(301,'ZY_TY','天翼快递',0,1,1),
	(302,'YDH','义达国际物流',0,1,1),
	(303,'HGLL','黑狗冷链',0,1,1),
	(304,'ZY_ESONG','宜送转运',0,1,1),
	(305,'PADTF','平安达腾飞快递',0,1,1),
	(306,'GDEMS','广东邮政',0,1,1),
	(307,'YDT','易达通',0,1,1),
	(308,'IXYLYZ','匈牙利邮政',0,1,1),
	(309,'YAMA','日本大和运输(Yamato)',0,1,1),
	(310,'ISEWDYZ','萨尔瓦多邮政',0,1,1),
	(311,'BTWL','百世快运',0,1,1),
	(312,'ADODOXOM','澳多多国际速递',0,1,1),
	(313,'ZY_MBZY','明邦转运',0,1,1),
	(314,'IGLBYYZ','哥伦比亚邮政',0,1,1),
	(315,'IMEDFYZ','马尔代夫邮政',0,1,1),
	(316,'ZMKMEX','芝麻开门',0,1,1),
	(317,'ZY_TTHT','天天海淘',0,1,1),
	(318,'YD56','韵达快运',0,1,1),
	(319,'ILKKD','林克快递',0,1,1),
	(320,'JYKD','晋越快递',0,1,1),
	(321,'IHLY','互联易',0,1,1),
	(322,'ZY_WDCS','文达国际DCS',0,1,1),
	(323,'IALYYZ','阿联酋邮政',0,1,1),
	(324,'ZY_ETD','ETD',0,1,1),
	(325,'ZY_YQWL','一柒物流',0,1,1),
	(326,'SDEZ','速递e站',0,1,1),
	(327,'IKTDWEMS','科特迪瓦EMS',0,1,1),
	(328,'YADEX','源安达快递',0,1,1),
	(329,'ZY_XJ','信捷转运',0,1,1),
	(330,'AXD','安信达快递',0,1,1),
	(331,'IMEDWYZ','摩尔多瓦邮政',0,1,1),
	(332,'IXBYYZ','西班牙邮政',0,1,1),
	(333,'ZY_XF','先锋快递',0,1,1),
	(334,'XJ','新杰物流',0,1,1),
	(335,'ZY_XC','星辰快递',0,1,1),
	(336,'HLWL','恒路物流',0,1,1),
	(337,'ZY_TPY','太平洋快递',0,1,1),
	(338,'ITSNYYZ','坦桑尼亚邮政',0,1,1),
	(339,'IKTDWYZ','科特迪瓦邮政',0,1,1),
	(340,'IJNYZ','加纳邮政',0,1,1),
	(341,'YD','韵达速递',0,1,1),
	(342,'QXT','全信通',0,1,1),
	(343,'ILMNYYZ','罗马尼亚邮政',0,1,1),
	(344,'IWKLYZ','乌克兰邮政',0,1,1),
	(345,'XBWL','新邦物流',0,1,1),
	(346,'IYDYZ','印度邮政',0,1,1),
	(347,'ZY_JH','久禾快递',0,1,1),
	(348,'ZJS','宅急送',0,1,1),
	(349,'ZY_RTSD','瑞天速递',0,1,1),
	(350,'ZY_OZGO','欧洲GO',0,1,1),
	(351,'ZY_JD','时代转运',0,1,1),
	(352,'ZY_TZH','同舟快递',0,1,1),
	(353,'MKGJ','美快国际物流',0,1,1),
	(354,'IWDMLYZ','危地马拉邮政',0,1,1),
	(355,'XCWL','迅驰物流',0,1,1),
	(356,'ZY_JA','君安快递',0,1,1),
	(357,'IPTYYZ','葡萄牙邮政',0,1,1),
	(358,'IXPSJ','夏浦世纪',0,1,1),
	(359,'ZY_YTUSA','运淘美国',0,1,1),
	(360,'ILZDSDYZ','列支敦士登邮政',0,1,1),
	(361,'TAIWANYZ','台湾邮政',0,1,1),
	(362,'ZY_JDKD','骏达快递',0,1,1),
	(363,'IYDLYZ','意大利邮政',0,1,1),
	(364,'DTWL','大田物流',0,1,1),
	(365,'ZY_SOHO','SOHO苏豪国际',0,1,1),
	(366,'IXXLYZ','新西兰邮政',0,1,1),
	(367,'IJBBWYZ','津巴布韦邮政',0,1,1),
	(368,'HOAU','天地华宇',0,1,1),
	(369,'HTKY','百世快递',0,1,1),
	(370,'ZY_AXO','AXO',0,1,1),
	(371,'ISEWYYZ','塞尔维亚邮政',0,1,1),
	(372,'ZY_HDB','海带宝',0,1,1),
	(373,'BFAY','八方安运',0,1,1),
	(374,'GJYZ','国际邮政包裹',0,1,1),
	(375,'BQXHM','北青小红帽',0,1,1),
	(376,'IASBJYZ','阿塞拜疆邮政',0,1,1),
	(377,'IQQKD','全球快递',0,1,1),
	(378,'CNPEX','CNPEX中邮快递',0,1,1),
	(379,'IMQDYZ','马其顿邮政',0,1,1),
	(380,'ZY_JHT','金海淘',0,1,1),
	(381,'HEMA','河马动力',0,1,1),
	(382,'ANTS','ANTS',0,1,1),
	(383,'YCWL','远成物流',0,1,1),
	(384,'AUEXPRESS','澳邮中国快运',0,1,1),
	(385,'IYTG','易通关',0,1,1),
	(386,'ZY_MJ','美嘉快递',0,1,1),
	(387,'DSWL','D速物流',0,1,1),
	(388,'IJPZYZ','柬埔寨邮政',0,1,1),
	(389,'ZY_TWC','TWC转运世界',0,1,1),
	(390,'DBL','德邦',0,1,1),
	(391,'IWGDYZ','乌干达邮政',0,1,1),
	(392,'ZY_LX','龙象快递',0,1,1),
	(393,'CJGJ','长江国际快递',0,1,1),
	(394,'IAGLYZ','安哥拉邮政',0,1,1),
	(395,'ISPLSYZ','塞浦路斯邮政',0,1,1),
	(396,'ISLWNYYZ','斯洛文尼亚邮政',0,1,1),
	(397,'IBCWNYZ','博茨瓦纳邮政',0,1,1),
	(398,'HHTT','天天快递',0,1,1),
	(399,'ZTWL','中铁物流',0,1,1),
	(400,'IASNYYZ','爱沙尼亚邮政',0,1,1),
	(401,'ZY_MZ','168 美中快递',0,1,1),
	(402,'BHGJ','贝海国际',0,1,1),
	(403,'ISNJEYZ','塞内加尔邮政',0,1,1),
	(404,'SAD','赛澳递',0,1,1),
	(405,'IHHWL','华翰物流',0,1,1),
	(406,'IXPWL','夏浦物流',0,1,1),
	(407,'ZY_CUL','CUL中美速递',0,1,1),
	(408,'UEQ','UEQ Express',0,1,1),
	(409,'ZY_DYW','德运网',0,1,1),
	(410,'IBMDYZ','百慕达邮政',0,1,1),
	(411,'HXWL','豪翔物流',0,1,1),
	(412,'IBLSD','便利速递',0,1,1),
	(413,'IFTWL','飞特物流',0,1,1),
	(414,'UEX','UEX',0,1,1),
	(415,'SHWL','盛辉物流',0,1,1),
	(416,'IJKYZ','捷克邮政',0,1,1),
	(417,'ZY_LZWL','量子物流',0,1,1),
	(418,'ZY_HTONG','华通快运',0,1,1),
	(419,'ZY_YGKD','优购快递',0,1,1),
	(420,'BFDF','百福东方',0,1,1),
	(421,'WJK','万家康',0,1,1),
	(422,'DHL_GLB','DHL全球',0,1,1),
	(423,'IMETYZ','马耳他邮政',0,1,1),
	(424,'GTSD','高铁速递',0,1,1),
	(425,'IEGDEYZ','厄瓜多尔邮政',0,1,1),
	(426,'ZY_SFZY','四方转运',0,1,1),
	(427,'ZY_DGHT','德国海淘之家',0,1,1),
	(428,'HOTSCM','鸿桥供应链',0,1,1),
	(429,'AUSEXPRESS','澳世速递',0,1,1),
	(430,'ZY_HTCUN','海淘村',0,1,1),
	(431,'ANXL','安迅物流',0,1,1);

/*!40000 ALTER TABLE `eb_express` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_marketing_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_marketing_activity`;

CREATE TABLE `eb_marketing_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `name` varchar(50) NOT NULL DEFAULT '0' COMMENT '活动名称',
  `is_open` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启：0-未开启，1-已开启',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '活动展示类型：1-轮播列表，2-大小格，3-大图模式',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0未删除1已删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `banner` varchar(2000) NOT NULL DEFAULT '' COMMENT '活动banner',
  `instruction` varchar(255) NOT NULL DEFAULT '' COMMENT '活动简介',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='营销活动表';

LOCK TABLES `eb_marketing_activity` WRITE;
/*!40000 ALTER TABLE `eb_marketing_activity` DISABLE KEYS */;

INSERT INTO `eb_marketing_activity` (`id`, `name`, `is_open`, `type`, `sort`, `is_del`, `create_time`, `update_time`, `banner`, `instruction`)
VALUES
	(1,'Last Line',1,2,1,0,'2022-06-18 18:11:09','2022-06-18 18:11:09','[\"crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png\",\"crmebimage/public/product/2022/06/18/524910ddd9c6482c8c70e1e6029cd18endhup3y4gl.png\",\"crmebimage/public/product/2022/06/18/dce92e38b7564e1abafcd206865dc688ed1tgfx0tt.png\",\"crmebimage/public/product/2022/06/18/a31a093ce2804693b0fbaadef9d5527945a8t1m54h.png\",\"crmebimage/public/product/2022/06/18/d29fb6cc07d94aa0af516075ccc3cca1b7wcnmr38d.png\"]','Last Line'),
	(2,'DaXiaoGe',1,2,1,0,'2022-06-18 18:14:44','2022-06-18 18:14:44','[\"crmebimage/public/product/2022/06/18/91ac2a1b069b477dbbac13fea837a785y6chifhvv3.jpg\",\"crmebimage/public/product/2022/06/18/8fc10d9bbec747fc978199e8ba731fe873054yexp7.jpg\",\"crmebimage/public/product/2022/06/18/79e3eb9aaf184975a4079f8cad402e7ena2xzu5urm.jpg\",\"crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png\"]','DaXiaoGe'),
	(3,'Top List',1,1,1,0,'2022-06-18 18:15:51','2022-06-18 18:16:56','[\"crmebimage/public/product/2022/06/18/115ca3ee301d41a6824365b393f76590kj66zndb4e.png\",\"crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png\",\"crmebimage/public/product/2022/06/18/997b9a0d96aa409f86b61e8935212436gazj63iupp.png\",\"crmebimage/public/product/2022/06/18/f293b290922f4ccbab84d70f2880c914sty4qif2eg.png\",\"crmebimage/public/product/2022/06/18/6c48e6761825426d83c4315e139dcc58qdotn3725v.png\"]','Top List');

/*!40000 ALTER TABLE `eb_marketing_activity` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_master_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_master_order`;

CREATE TABLE `eb_master_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '主订单号',
  `uid` int(11) unsigned NOT NULL COMMENT '用户id',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `real_name` varchar(32) NOT NULL COMMENT '收货人姓名',
  `user_phone` varchar(18) NOT NULL COMMENT '收货人电话',
  `user_address` varchar(100) NOT NULL COMMENT '详细地址',
  `total_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单商品总数',
  `pro_total_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品总价',
  `total_postage` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '邮费',
  `total_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总价',
  `pay_postage` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '支付邮费',
  `pay_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
  `coupon_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '优惠券id',
  `coupon_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '优惠券金额',
  `paid` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `pay_type` varchar(32) NOT NULL COMMENT '支付方式:paypal',
  `pay_channel` varchar(20) NOT NULL DEFAULT '' COMMENT '支付渠道：pc,mobile',
  `mark` varchar(512) NOT NULL DEFAULT '' COMMENT '用户备注',
  `is_cancel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否取消',
  `out_trade_no` varchar(32) DEFAULT '' COMMENT 'PAYPAL商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号',
  `redirect` varchar(500) DEFAULT '' COMMENT '支付重定向地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='主订单表';



# 转储表 eb_merchant
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant`;

CREATE TABLE `eb_merchant` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商户ID',
  `name` varchar(50) NOT NULL COMMENT '商户名称',
  `category_id` int(11) NOT NULL COMMENT '商户分类ID',
  `type_id` int(11) NOT NULL COMMENT '商户类型ID',
  `real_name` varchar(50) NOT NULL COMMENT '商户姓名',
  `email` varchar(255) NOT NULL COMMENT '商户邮箱',
  `phone` varchar(255) NOT NULL DEFAULT '' COMMENT '商户手机号',
  `handling_fee` int(3) NOT NULL COMMENT '手续费(%)',
  `keywords` varchar(255) NOT NULL DEFAULT '' COMMENT '商户关键字',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '商户地址',
  `is_self` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否自营：0-自营，1-非自营',
  `is_recommend` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否推荐:0-不推荐，1-推荐',
  `is_switch` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '商户开关:0-关闭，1-开启',
  `product_switch` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '商品审核开关:0-关闭，1-开启',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `sort` int(4) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `qualification_picture` varchar(2000) NOT NULL DEFAULT '' COMMENT '资质图片',
  `back_image` varchar(255) NOT NULL DEFAULT '' COMMENT '商户背景图',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '商户头像',
  `street_back_image` varchar(255) NOT NULL DEFAULT '' COMMENT '商户街背景图',
  `intro` varchar(255) NOT NULL DEFAULT '' COMMENT '商户简介',
  `create_type` varchar(255) NOT NULL COMMENT '商户创建类型：admin-管理员创建，apply-商户入驻申请',
  `create_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建商户管理员ID',
  `admin_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联管理账号ID',
  `copy_product_num` int(11) NOT NULL DEFAULT '0' COMMENT '复制商品数量',
  `balance` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商户余额',
  `star_level` int(2) NOT NULL DEFAULT '4' COMMENT '商户星级1-5',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pc_banner` varchar(2000) NOT NULL DEFAULT '' COMMENT 'pcBanner',
  `pc_back_image` varchar(500) NOT NULL DEFAULT '' COMMENT 'pc背景图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户表';

LOCK TABLES `eb_merchant` WRITE;
/*!40000 ALTER TABLE `eb_merchant` DISABLE KEYS */;

INSERT INTO `eb_merchant` (`id`, `name`, `category_id`, `type_id`, `real_name`, `email`, `phone`, `handling_fee`, `keywords`, `address`, `is_self`, `is_recommend`, `is_switch`, `product_switch`, `remark`, `sort`, `qualification_picture`, `back_image`, `avatar`, `street_back_image`, `intro`, `create_type`, `create_id`, `admin_id`, `copy_product_num`, `balance`, `star_level`, `is_del`, `create_time`, `update_time`, `pc_banner`, `pc_back_image`)
VALUES
	(2,'大粽子的杂货店',5,4,'大粽子','stivepeim@outlook.com','18292417675',2,'大粽子','曲江池桥洞',1,1,1,0,'',1,'[\"crmebimage/public/product/2022/06/18/7629f5f69e7540619b9ead71d00cd50f0fa68ab9b3.jpg\"]','crmebimage/public/operation/2022/06/20/53d232f8b7714e6a8322cb513e7a59d18udnr8q24c.jpg','crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png','crmebimage/public/operation/2022/06/20/d69b8bb418c74e1f9497a54fa10ddbdd1uxlu7sm61.jpg','杂货铺，也就是想要啥有啥','admin',3,21,56,0.00,4,0,'2022-06-16 11:30:04','2023-05-19 12:13:59','[\"crmebimage/public/operation/2022/06/20/82f27cd31c8f437c83e1426b04cd95a88vzp1tlcj9.jpg\",\"crmebimage/public/operation/2022/06/20/e981ae8cc8e94c56987be5f22cda3d8b7ahfm8qfby.jpg\",\"crmebimage/public/operation/2022/06/20/81657874c2984c5cbffc5f6e4ccb81ackl6jglnp9f.jpg\",\"crmebimage/public/operation/2022/06/20/4a20fe1498bd438180d962eb5a5d0718tlljyy1kq6.jpg\"]','crmebimage/public/operation/2022/06/20/d69b8bb418c74e1f9497a54fa10ddbdd1uxlu7sm61.jpg');

/*!40000 ALTER TABLE `eb_merchant` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_merchant_apply
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_apply`;

CREATE TABLE `eb_merchant_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `uid` int(11) NOT NULL COMMENT '申请用户ID',
  `name` varchar(50) NOT NULL COMMENT '商户名称',
  `category_id` int(11) NOT NULL COMMENT '商户分类ID',
  `type_id` int(11) NOT NULL COMMENT '商户类型ID',
  `account` varchar(255) NOT NULL DEFAULT '' COMMENT '商户账号',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '登录密码',
  `real_name` varchar(50) NOT NULL COMMENT '商户姓名',
  `email` varchar(255) NOT NULL COMMENT '商户邮箱',
  `phone` varchar(255) NOT NULL DEFAULT '' COMMENT '商户手机号',
  `handling_fee` int(3) NOT NULL COMMENT '手续费(%)',
  `keywords` varchar(255) NOT NULL DEFAULT '' COMMENT '商户关键字',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '商户地址',
  `is_self` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否自营：0-自营，1-非自营',
  `is_recommend` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否推荐:0-不推荐，1-推荐',
  `audit_status` int(2) unsigned NOT NULL DEFAULT '1' COMMENT '审核状态：1-待审核，2-审核通过，3-审核拒绝',
  `denial_reason` varchar(255) NOT NULL DEFAULT '' COMMENT '拒绝原因',
  `auditor_id` int(11) NOT NULL DEFAULT '0' COMMENT '审核员ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `qualification_picture` varchar(2000) NOT NULL DEFAULT '' COMMENT '资质图片',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户申请表';



# Dump of table eb_merchant_bill
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_bill`;

CREATE TABLE `eb_merchant_bill` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父级id（平台流水id）',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户uid',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户id',
  `link_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联id',
  `order_no` varchar(32) NOT NULL DEFAULT '' COMMENT '关联订单',
  `pm` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '0 = 支出 1 = 获得',
  `amount` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '金额',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '类型：pay_order-订单支付,refund_order-订单退款',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  KEY `pm` (`pm`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户帐单表';



# Dump of table eb_merchant_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_category`;

CREATE TABLE `eb_merchant_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `handling_fee` int(3) NOT NULL COMMENT '手续费(%)',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户分类表';

LOCK TABLES `eb_merchant_category` WRITE;
/*!40000 ALTER TABLE `eb_merchant_category` DISABLE KEYS */;

INSERT INTO `eb_merchant_category` (`id`, `name`, `handling_fee`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,'贵重珠宝、首饰零售',3,0,'2022-03-09 09:39:52','2022-03-09 09:39:52'),
	(2,'玻璃器皿和水晶饰品店',2,0,'2022-03-09 09:46:07','2022-03-09 09:46:07'),
	(3,'古玩复制店',1,0,'2022-03-09 09:50:59','2022-03-09 09:50:59'),
	(4,'工艺美术商店',3,0,'2022-03-10 14:23:14','2022-03-10 14:23:14'),
	(5,'奢侈品零售',2,0,'2022-04-22 09:59:53','2022-04-22 09:59:53'),
	(6,'男女及儿童制服和服装',3,0,'2022-04-22 09:58:14','2022-04-22 09:58:14');

/*!40000 ALTER TABLE `eb_merchant_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_merchant_daily_statement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_daily_statement`;

CREATE TABLE `eb_merchant_daily_statement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户id',
  `order_income_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单收入金额',
  `order_pay_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付总金额',
  `order_num` int(10) NOT NULL DEFAULT '0' COMMENT '订单支付笔数',
  `handling_fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台手续费',
  `payout_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '支出总金额',
  `payout_num` int(10) NOT NULL DEFAULT '0' COMMENT '支出笔数',
  `refund_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台退款金额',
  `refund_num` int(10) NOT NULL DEFAULT '0' COMMENT '退款笔数',
  `income_expenditure` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商户日收支',
  `data_date` varchar(12) NOT NULL COMMENT '日期：年-月-日',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `data_date` (`data_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户日帐单表';



# Dump of table eb_merchant_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_info`;

CREATE TABLE `eb_merchant_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商户信息ID',
  `mer_id` int(11) NOT NULL COMMENT '商户ID',
  `transfer_type` varchar(20) NOT NULL DEFAULT 'bank' COMMENT '转账类型:bank-银行卡',
  `transfer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '转账姓名',
  `transfer_bank` varchar(50) NOT NULL DEFAULT '' COMMENT '转账银行',
  `transfer_bank_card` varchar(30) NOT NULL DEFAULT '' COMMENT '转账银行卡号',
  `alert_stock` int(5) NOT NULL DEFAULT '0' COMMENT '警戒库存',
  `service_type` varchar(10) NOT NULL DEFAULT '' COMMENT '客服类型：H5-H5链接、phone-电话、message-Message,email-邮箱',
  `service_link` varchar(255) NOT NULL DEFAULT '' COMMENT '客服H5链接',
  `service_phone` varchar(100) NOT NULL DEFAULT '' COMMENT '客服电话',
  `service_message` varchar(100) NOT NULL DEFAULT '' COMMENT '客服Message',
  `service_email` varchar(255) NOT NULL DEFAULT '' COMMENT '客服邮箱',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户信息表';

LOCK TABLES `eb_merchant_info` WRITE;
/*!40000 ALTER TABLE `eb_merchant_info` DISABLE KEYS */;

INSERT INTO `eb_merchant_info` (`id`, `mer_id`, `transfer_type`, `transfer_name`, `transfer_bank`, `transfer_bank_card`, `alert_stock`, `service_type`, `service_link`, `service_phone`, `service_message`, `service_email`, `create_time`, `update_time`)
VALUES
	(1,1,'bank','静静','花旗银行','123123123123123123',50,'email','https://cschat.antcloud.com.cn/index.htm?tntInstId=jm7_c46J&scene=SCE01197657','1','https://m.me/peim.stive','2595519485@qq.com','2022-06-15 11:42:55','2022-06-15 14:28:03'),
	(2,2,'bank','大粽子','中国银行','611086532836485358437',0,'phone','','18292417675','peim_stive','stivepeim@outlook.com','2022-06-16 11:30:04','2022-06-16 11:44:14'),
	(3,3,'bank','','','',0,'phone','','17729197751','17729197751','588888888888@qq.com','2022-06-18 12:20:23','2022-06-18 14:51:03'),
	(4,4,'bank','','','',0,'','','','','','2022-06-18 12:21:35','2022-06-18 12:21:35');

/*!40000 ALTER TABLE `eb_merchant_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_merchant_month_statement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_month_statement`;

CREATE TABLE `eb_merchant_month_statement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户id',
  `order_income_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单收入金额',
  `order_pay_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付总金额',
  `order_num` int(10) NOT NULL DEFAULT '0' COMMENT '订单支付笔数',
  `handling_fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台手续费',
  `payout_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '支出总金额',
  `payout_num` int(10) NOT NULL DEFAULT '0' COMMENT '支出笔数',
  `refund_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台退款金额',
  `refund_num` int(10) NOT NULL DEFAULT '0' COMMENT '退款笔数',
  `income_expenditure` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商户月收支',
  `data_date` varchar(12) NOT NULL COMMENT '日期：年-月',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `data_date` (`data_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户月帐单表';

LOCK TABLES `eb_merchant_month_statement` WRITE;
/*!40000 ALTER TABLE `eb_merchant_month_statement` DISABLE KEYS */;

INSERT INTO `eb_merchant_month_statement` (`id`, `mer_id`, `order_income_amount`, `order_pay_amount`, `order_num`, `handling_fee`, `payout_amount`, `payout_num`, `refund_amount`, `refund_num`, `income_expenditure`, `data_date`)
VALUES
	(1,1,0.00,0.00,0,0.00,0.00,0,0.00,0,0.00,'2022-06'),
	(2,2,0.00,0.00,0,0.00,0.00,0,0.00,0,0.00,'2022-06'),
	(3,3,0.00,0.00,0,0.00,0.00,0,0.00,0,0.00,'2022-06'),
	(4,4,0.00,0.00,0,0.00,0.00,0,0.00,0,0.00,'2022-06');

/*!40000 ALTER TABLE `eb_merchant_month_statement` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_merchant_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_merchant_type`;

CREATE TABLE `eb_merchant_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '类型名称',
  `info` varchar(500) NOT NULL COMMENT '类型要求说明',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户类型表';

LOCK TABLES `eb_merchant_type` WRITE;
/*!40000 ALTER TABLE `eb_merchant_type` DISABLE KEYS */;

INSERT INTO `eb_merchant_type` (`id`, `name`, `info`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,'官方旗舰店','官方有效营业执照与联系人工作证、身份证1',0,'2022-03-10 15:13:23','2022-03-10 15:13:23'),
	(2,'厂家直营店','厂家营业执照与联系人工作证、身份证',0,'2022-03-10 15:13:53','2022-03-10 15:13:53'),
	(3,'专卖店','官方有效营业执照与联系人工作证、身份证122222',0,'2022-03-10 20:08:18','2022-03-10 20:08:18'),
	(4,'旗舰店','营业执照、安全卫生许可证',0,'2022-05-11 12:12:19','2022-05-11 12:12:19'),
	(5,'加工厂','联系人方式',1,'2022-05-11 12:14:13','2022-05-11 12:14:13');

/*!40000 ALTER TABLE `eb_merchant_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_order_logistics
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_order_logistics`;

CREATE TABLE `eb_order_logistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `exp_no` varchar(30) DEFAULT NULL COMMENT '快递单号',
  `exp_code` varchar(30) DEFAULT NULL COMMENT '快递公司编号',
  `exp_name` varchar(30) DEFAULT NULL COMMENT '快递公司名称',
  `courier` varchar(30) DEFAULT NULL COMMENT '快递员 或 快递站(没有则为空)',
  `courier_phone` varchar(30) DEFAULT NULL COMMENT '快递员电话 (没有则为空)',
  `track_time` varchar(30) DEFAULT NULL COMMENT '最后的轨迹时间',
  `logistics_info` text COMMENT '快递物流轨迹（JSON）AcceptStation-快递中转站，终点站，AcceptTime-事件时间',
  `state` int(2) DEFAULT '0' COMMENT '物流状态：-1：单号或快递公司代码错误, 0：暂无轨迹， 1：快递收件(揽件)，2：在途中,3：签收,4：问题件 5.疑难件 6.退件签收',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `reason` varchar(255) DEFAULT NULL COMMENT '提示信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='快递记录表';



# Dump of table eb_platform_daily_statement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_platform_daily_statement`;

CREATE TABLE `eb_platform_daily_statement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `order_pay_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付总金额',
  `total_order_num` int(10) NOT NULL DEFAULT '0' COMMENT '总订单支付笔数',
  `merchant_order_num` int(10) NOT NULL DEFAULT '0' COMMENT '分订单支付笔数',
  `handling_fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '日手续费收入',
  `payout_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '支出总金额',
  `payout_num` int(10) NOT NULL DEFAULT '0' COMMENT '支出笔数',
  `merchant_transfer_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商户分账金额',
  `merchant_transfer_num` int(10) NOT NULL DEFAULT '0' COMMENT '商户分账笔数',
  `refund_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台退款金额',
  `refund_num` int(10) NOT NULL DEFAULT '0' COMMENT '退款笔数',
  `income_expenditure` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台日收支',
  `data_date` varchar(12) NOT NULL COMMENT '日期：年-月-日',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `data_date` (`data_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='平台日帐单表';

LOCK TABLES `eb_platform_daily_statement` WRITE;
/*!40000 ALTER TABLE `eb_platform_daily_statement` DISABLE KEYS */;

INSERT INTO `eb_platform_daily_statement` (`id`, `order_pay_amount`, `total_order_num`, `merchant_order_num`, `handling_fee`, `payout_amount`, `payout_num`, `merchant_transfer_amount`, `merchant_transfer_num`, `refund_amount`, `refund_num`, `income_expenditure`, `data_date`)
VALUES
	(1,0.00,0,0,0.00,0.00,0,0.00,0,0.00,0,0.00,'2022-06-16'),
	(2,0.00,0,0,0.00,0.00,0,0.00,0,0.00,0,0.00,'2022-06-17'),
	(3,0.00,0,0,0.00,0.00,0,0.00,0,0.00,0,0.00,'2022-06-18'),
	(4,0.00,0,0,0.00,0.00,0,0.00,0,0.00,0,0.00,'2022-06-19'),
	(5,0.00,0,0,0.00,0.00,0,0.00,0,0.00,0,0.00,'2022-06-20');

/*!40000 ALTER TABLE `eb_platform_daily_statement` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_platform_month_statement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_platform_month_statement`;

CREATE TABLE `eb_platform_month_statement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '帐单id',
  `order_pay_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付总金额',
  `total_order_num` int(10) NOT NULL DEFAULT '0' COMMENT '总订单支付笔数',
  `merchant_order_num` int(10) NOT NULL DEFAULT '0' COMMENT '分订单支付笔数',
  `handling_fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '月手续费收入',
  `payout_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '支出总金额',
  `payout_num` int(10) NOT NULL DEFAULT '0' COMMENT '支出笔数',
  `merchant_transfer_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商户分账金额',
  `merchant_transfer_num` int(10) NOT NULL DEFAULT '0' COMMENT '商户分账笔数',
  `refund_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台退款金额',
  `refund_num` int(10) NOT NULL DEFAULT '0' COMMENT '退款笔数',
  `income_expenditure` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '平台月收支',
  `data_date` varchar(12) NOT NULL COMMENT '日期：年-月',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `data_date` (`data_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='平台月帐单表';



# Dump of table eb_product_brand
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_product_brand`;

CREATE TABLE `eb_product_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `is_del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品品牌表';

LOCK TABLES `eb_product_brand` WRITE;
/*!40000 ALTER TABLE `eb_product_brand` DISABLE KEYS */;

INSERT INTO `eb_product_brand` (`id`, `name`, `icon`, `sort`, `is_show`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,'PORTS','crmebimage/public/store/2022/06/18/a535246301f44d3f8d511ac3e2dbe34454i3ti6t57.jpeg',1,1,1,'2022-06-15 14:13:12','2022-06-18 16:48:26'),
	(2,'cake','crmebimage/public/store/2022/06/18/b153883e27dc47c9a13b8fa77f50b8a8ytr7zhk933.png',1,1,0,'2022-06-18 15:10:38','2022-06-18 15:10:38'),
	(3,'LAUREN Ralph Lauren','crmebimage/public/store/2022/06/18/2b621f5bfc2248b7976b065ffb26c3e8wv5kovrs0m.jpg',1,1,0,'2022-06-18 17:20:18','2022-06-18 17:20:18');

/*!40000 ALTER TABLE `eb_product_brand` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_product_brand_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_product_brand_category`;

CREATE TABLE `eb_product_brand_category` (
  `bid` int(11) NOT NULL COMMENT '品牌id',
  `cid` int(11) NOT NULL COMMENT '分类id',
  PRIMARY KEY (`bid`,`cid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品品牌分类关联表';

LOCK TABLES `eb_product_brand_category` WRITE;
/*!40000 ALTER TABLE `eb_product_brand_category` DISABLE KEYS */;

INSERT INTO `eb_product_brand_category` (`bid`, `cid`)
VALUES
	(2,23),
	(2,24),
	(2,26),
	(2,27),
	(2,29),
	(2,30),
	(2,32),
	(2,33),
	(2,35),
	(2,36),
	(2,38),
	(2,39),
	(2,82),
	(2,83),
	(2,84),
	(2,85),
	(2,86),
	(2,87),
	(3,97);

/*!40000 ALTER TABLE `eb_product_brand_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_product_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_product_category`;

CREATE TABLE `eb_product_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon',
  `level` int(2) NOT NULL DEFAULT '1' COMMENT '级别:1，2，3',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `is_del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品分类表';

LOCK TABLES `eb_product_category` WRITE;
/*!40000 ALTER TABLE `eb_product_category` DISABLE KEYS */;

INSERT INTO `eb_product_category` (`id`, `pid`, `name`, `icon`, `level`, `sort`, `is_show`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,0,'服饰内衣','crmebimage/public/store/2022/05/06/ea0100f331e34be58aaf101d43907d379klx18srk0.jpg',1,0,1,1,'2022-05-10 19:21:42','2022-05-11 11:23:50'),
	(2,1,'上衣','crmebimage/public/store/2022/05/06/ea0100f331e34be58aaf101d43907d379klx18srk0.jpg',2,0,1,1,'2022-05-10 19:24:57','2022-05-10 19:24:57'),
	(3,2,'衬衣','crmebimage/public/store/2022/05/06/ea0100f331e34be58aaf101d43907d379klx18srk0.jpg',3,0,1,1,'2022-05-10 19:25:14','2022-05-10 19:25:14'),
	(4,0,'服饰内衣',NULL,1,0,1,1,'2022-05-11 11:23:35','2022-05-11 11:23:35'),
	(5,1,'女装','crmebimage/public/product/2022/06/01/d6c205df0b874ed7bb7c2bd29c969770ngjs9o7jnm.png',3,0,1,1,'2022-05-11 11:23:56','2022-06-14 10:04:54'),
	(6,5,'连衣裙','crmebimage/public/product/2022/06/01/d6c205df0b874ed7bb7c2bd29c969770ngjs9o7jnm.png',3,0,1,1,'2022-05-11 11:24:04','2022-06-06 10:46:12'),
	(7,0,'生鲜','crmebimage/public/maintain/2022/05/11/25cdb51c3e4c4fcda0547875281926727yq8l7ooa4.jpg',1,0,1,1,'2022-05-11 18:03:46','2022-05-11 18:03:46'),
	(8,7,'水果','crmebimage/public/store/2022/05/11/d753c242331f48edbc664b59da86a9c4jammdgqfno.jpg',2,0,1,1,'2022-05-11 18:04:09','2022-05-11 18:04:09'),
	(9,8,'本地水果','crmebimage/public/store/2022/05/11/339a2b419c674798bc9bf44f0867fdf1f01yah3dna.jpg',3,0,1,1,'2022-05-11 18:04:44','2022-05-11 18:04:44'),
	(10,1,'1',NULL,2,0,1,1,'2022-05-13 19:27:39','2022-05-13 19:27:39'),
	(11,0,'test','crmebimage/public/product/2022/05/13/20a7cdbdfffc487fb630be3710a363dfj5k4dolx4d.png',1,0,1,1,'2022-05-14 11:17:56','2022-05-14 11:17:56'),
	(12,11,'test01','crmebimage/public/product/2022/05/13/16014b3e4b4141c1b79ae275cf2a787bebe7ps0m7f.jpg',2,0,1,1,'2022-05-14 11:18:20','2022-05-14 11:18:20'),
	(13,12,'test001','crmebimage/public/maintain/2022/05/13/c54938f478a04b95961f0829a2930d768i8h9a7s7m.jpg',3,0,1,1,'2022-05-14 11:18:44','2022-05-14 11:18:44'),
	(14,11,'123123',NULL,2,0,1,1,'2022-05-14 14:45:49','2022-05-14 14:45:49'),
	(15,0,'fresh','crmebimage/public/product/2022/06/01/6dd9bee79afe4c8ca3a35d08119fc29aumwr4n051a.jpg',1,2,1,1,'2022-05-16 11:22:13','2022-06-06 11:09:19'),
	(16,15,'fruit','crmebimage/public/product/2022/06/01/6dd9bee79afe4c8ca3a35d08119fc29aumwr4n051a.jpg',2,0,1,1,'2022-05-16 11:23:04','2022-06-06 11:09:10'),
	(17,15,'蔬菜','crmebimage/public/product/2022/05/13/5e848fc181d7418496f23d45bd26753bvm6b80s0gg.jpg',2,2,1,1,'2022-05-16 11:23:25','2022-05-16 11:23:25'),
	(18,17,'时令蔬菜','crmebimage/public/store/2022/05/16/4de3f01139f04a79b1c676b00178a73627w11e3tfa.jpeg',3,0,1,1,'2022-05-16 11:24:31','2022-05-16 11:24:31'),
	(19,16,'时令水果','crmebimage/public/product/2022/05/31/5ae92d6f46aa4402923297db8c3bde21rlbvzfqk9w.jpg',3,0,1,1,'2022-05-16 11:24:51','2022-06-06 11:02:37'),
	(20,0,'HOME PET','crmebimage/public/product/2022/06/18/791b3949677845cbb726160a10f29185j4j6vtpg2f.png',1,1,1,0,'2022-06-06 15:14:14','2022-06-18 16:21:04'),
	(21,20,'Pet supp','crmebimage/public/product/2022/06/18/89f34c76bff9437a822f4dbe55cc8906xjrr5dqdb8.png',2,1,1,0,'2022-06-06 15:14:39','2022-06-18 16:21:47'),
	(22,20,'Supplies','crmebimage/public/store/2022/06/06/db5055588c774c43a0b34c2bb8cda881nhlfc8and0.png',2,1,1,1,'2022-06-06 15:15:13','2022-06-06 15:15:13'),
	(23,21,'Pet s','crmebimage/public/product/2022/06/18/89f34c76bff9437a822f4dbe55cc8906xjrr5dqdb8.png',3,1,1,0,'2022-06-06 15:16:24','2022-06-18 16:21:29'),
	(24,21,'Supplies','crmebimage/public/product/2022/06/18/8eeb59b0137b41799b24d6319a10fbccrnp2v5ly1d.png',3,1,1,0,'2022-06-06 15:16:51','2022-06-18 16:22:11'),
	(25,20,'Bedding','crmebimage/public/product/2022/06/18/256625d2bf3a4c6092b4fe66764a2e28oxkm006stx.jpg',2,1,1,0,'2022-06-06 15:17:21','2022-06-18 16:23:41'),
	(26,25,'Beddings','crmebimage/public/product/2022/06/18/256625d2bf3a4c6092b4fe66764a2e28oxkm006stx.jpg',3,1,1,0,'2022-06-06 15:17:55','2022-06-18 16:23:52'),
	(27,25,'Pillow','crmebimage/public/product/2022/06/18/49940e8a977f42f7b55e3925f9955da8340kd104n8.jpg',3,1,1,0,'2022-06-06 15:18:36','2022-06-18 16:24:10'),
	(28,20,'Organize','crmebimage/public/product/2022/06/18/cf85ef8fa16344a982a2077fcf8fd8c9w96h34lbjn.jpg',2,1,1,0,'2022-06-06 15:19:24','2022-06-18 16:25:44'),
	(29,28,'C Organi','crmebimage/public/product/2022/06/18/cf85ef8fa16344a982a2077fcf8fd8c9w96h34lbjn.jpg',3,1,1,0,'2022-06-06 15:19:52','2022-06-18 16:25:58'),
	(30,28,'M Organi','crmebimage/public/product/2022/06/18/0e40a97d8b0c47fbb9f2dd83274dd33db1c2kk71q7.jpg',3,1,1,0,'2022-06-06 15:20:19','2022-06-18 16:26:17'),
	(31,20,'Cookware','crmebimage/public/product/2022/06/18/e3be2b2e4b9443e4bda60a0d811367e5t1d4kft06j.jpg',2,1,1,0,'2022-06-06 15:20:47','2022-06-18 16:27:27'),
	(32,31,'Pan','crmebimage/public/product/2022/06/18/e3be2b2e4b9443e4bda60a0d811367e5t1d4kft06j.jpg',3,1,1,0,'2022-06-06 15:21:26','2022-06-18 16:27:50'),
	(33,31,'Plate','crmebimage/public/product/2022/06/18/a5fb7d2e4e1a46fdb3c273a901089507497u3lglzw.jpg',3,1,1,0,'2022-06-06 15:22:21','2022-06-18 16:28:02'),
	(34,20,'Yiwu ind','crmebimage/public/product/2022/06/18/a0bc21996af64d1ea1fcca907f026c35plk82xme1p.jpg',2,1,1,0,'2022-06-06 15:22:48','2022-06-18 16:30:32'),
	(35,34,'small co','crmebimage/public/product/2022/06/18/a0bc21996af64d1ea1fcca907f026c35plk82xme1p.jpg',3,1,1,0,'2022-06-06 15:23:19','2022-06-18 16:30:42'),
	(36,34,'Commonly','crmebimage/public/product/2022/06/18/7311574d9e434fa594d64b891756199dea0i8mb1ha.jpg',3,1,1,0,'2022-06-06 15:23:49','2022-06-18 16:30:58'),
	(37,20,'Pet Top','crmebimage/public/product/2022/06/18/e79c2e164e9d455093c70cfb2dbfc85ezku22wmraq.jpg',2,1,1,0,'2022-06-06 15:24:27','2022-06-18 16:31:34'),
	(38,37,'Top Sale','crmebimage/public/product/2022/06/18/e79c2e164e9d455093c70cfb2dbfc85ezku22wmraq.jpg',3,1,1,0,'2022-06-06 15:24:57','2022-06-18 16:31:44'),
	(39,37,'Used','crmebimage/public/product/2022/06/18/8bcb2eb92dc948859c326f89df32283epfxcr2zc3r.jpg',3,1,1,0,'2022-06-06 15:25:24','2022-06-18 16:31:59'),
	(40,0,'OUTDOORS','crmebimage/public/product/2022/06/18/59a74fa47497499e87f419fa2af4b48b6urr56beba.png',1,1,1,0,'2022-06-06 15:37:27','2022-06-18 16:34:01'),
	(41,40,'WaterSpo','crmebimage/public/product/2022/06/18/dce92e38b7564e1abafcd206865dc688ed1tgfx0tt.png',2,1,1,0,'2022-06-06 15:38:13','2022-06-18 16:34:24'),
	(42,41,'Swimming','crmebimage/public/product/2022/06/18/dce92e38b7564e1abafcd206865dc688ed1tgfx0tt.png',3,1,1,0,'2022-06-06 15:38:46','2022-06-18 16:34:35'),
	(43,41,'diving','crmebimage/public/product/2022/06/18/c895ed62a6f74427a99c090574f53550h9c9lcdfhf.png',3,1,1,0,'2022-06-06 15:39:11','2022-06-18 16:34:51'),
	(44,40,'Yoga sui','crmebimage/public/product/2022/06/18/997b9a0d96aa409f86b61e8935212436gazj63iupp.png',2,1,1,0,'2022-06-06 15:39:33','2022-06-18 16:36:26'),
	(45,44,'Yoga Pan','crmebimage/public/product/2022/06/18/524910ddd9c6482c8c70e1e6029cd18endhup3y4gl.png',3,1,1,0,'2022-06-06 15:40:14','2022-06-18 16:36:52'),
	(46,44,'Yoga','crmebimage/public/product/2022/06/18/997b9a0d96aa409f86b61e8935212436gazj63iupp.png',3,1,1,0,'2022-06-06 15:40:38','2022-06-18 16:37:01'),
	(47,40,'Team Spo','crmebimage/public/product/2022/06/18/34c0cd755bfb4c0da2d7ed0261905edcp4n01l30w7.png',2,1,1,0,'2022-06-06 15:41:02','2022-06-18 16:38:37'),
	(48,47,'Ball','crmebimage/public/product/2022/06/18/34c0cd755bfb4c0da2d7ed0261905edcp4n01l30w7.png',3,1,1,0,'2022-06-06 15:41:19','2022-06-18 16:38:54'),
	(49,47,'Badminto','crmebimage/public/product/2022/06/18/0c41763dac274e29b45cd273338a2b52yzb0cwj0c0.png',3,1,1,0,'2022-06-06 15:41:50','2022-06-18 16:39:05'),
	(50,40,'Out door','crmebimage/public/product/2022/06/18/13c7420d36f742ac880d501aba30dd7cnab51hroks.png',2,1,1,0,'2022-06-06 15:42:42','2022-06-18 16:39:29'),
	(51,50,'Mountain','crmebimage/public/product/2022/06/18/13c7420d36f742ac880d501aba30dd7cnab51hroks.png',3,1,1,0,'2022-06-06 15:43:13','2022-06-18 16:39:39'),
	(52,50,'lighting','crmebimage/public/product/2022/06/18/1aef8b8b35254ab98f6b5a4b69436c6368eoz7c4ce.png',3,1,1,0,'2022-06-06 15:43:42','2022-06-18 16:39:52'),
	(53,40,'Fitness ','crmebimage/public/product/2022/06/18/4ad28d4d84534aa1be84b6fe9f782e64gffx0tqq2k.jpg',2,1,1,0,'2022-06-06 15:45:52','2022-06-18 16:40:22'),
	(54,53,'Body bui','crmebimage/public/product/2022/06/18/3e05b25d8cce4072aac085ac486b3da5yz4enhu7ak.png',3,1,1,0,'2022-06-06 15:46:25','2022-06-18 16:40:28'),
	(55,53,'Bbuildin','crmebimage/public/product/2022/06/18/4ad28d4d84534aa1be84b6fe9f782e64gffx0tqq2k.jpg',3,1,1,0,'2022-06-06 15:47:29','2022-06-18 16:40:34'),
	(56,40,'B TopSal','crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png',2,1,1,0,'2022-06-06 15:47:53','2022-06-18 16:40:48'),
	(57,56,'BuildBod','crmebimage/public/product/2022/06/18/115ca3ee301d41a6824365b393f76590kj66zndb4e.png',3,1,1,0,'2022-06-06 15:48:44','2022-06-18 16:40:53'),
	(58,56,'Body Spo','crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png',3,1,1,0,'2022-06-06 15:49:10','2022-06-18 16:40:58'),
	(59,0,'APPLIANC','crmebimage/public/product/2022/06/18/392a8673e6064d519ff3e7ed572380866nlt52z8oy.png',1,1,1,0,'2022-06-06 15:55:22','2022-06-18 16:41:22'),
	(60,59,'Man','crmebimage/public/store/2022/06/06/37ea8a4c43594c3bb63f2431751512209u7xitvn3p.png',3,1,1,1,'2022-06-06 15:56:11','2022-06-06 15:57:25'),
	(61,59,'Women','crmebimage/public/store/2022/06/06/9e80289a8b98406aa4cf3f2cf7254f5eprehjsazud.png',3,1,1,1,'2022-06-06 15:56:39','2022-06-06 15:57:52'),
	(62,59,'Personal','crmebimage/public/product/2022/06/18/f2b3d7340ffa43348733805175f4569avbr0xw892q.png',2,1,1,0,'2022-06-06 15:57:15','2022-06-18 16:41:51'),
	(63,62,'Man','crmebimage/public/product/2022/06/18/f2b3d7340ffa43348733805175f4569avbr0xw892q.png',3,1,1,0,'2022-06-06 16:09:42','2022-06-18 16:42:06'),
	(64,62,'Women','crmebimage/public/product/2022/06/18/39bd35f345404f2e923ab9e1aaea55355fjx89v8go.png',3,1,1,0,'2022-06-06 16:10:01','2022-06-18 16:42:22'),
	(65,59,'LivingRo','crmebimage/public/product/2022/06/18/4130adc7bbf249409222d3075952a1f4plk9tsdqvu.png',2,1,1,0,'2022-06-06 16:13:36','2022-06-18 16:44:31'),
	(66,65,'Living','crmebimage/public/product/2022/06/18/4130adc7bbf249409222d3075952a1f4plk9tsdqvu.png',3,1,1,0,'2022-06-06 16:13:50','2022-06-18 16:44:42'),
	(67,65,'Room','crmebimage/public/product/2022/06/18/6c48e6761825426d83c4315e139dcc58qdotn3725v.png',3,1,1,0,'2022-06-06 16:14:07','2022-06-18 16:44:53'),
	(68,59,'Dessert','crmebimage/public/product/2022/06/18/b2d497f822b545da99af8b8f761d8e96sdzercwxj3.png',2,1,1,0,'2022-06-06 16:14:30','2022-06-18 16:45:56'),
	(69,68,'breakfas','crmebimage/public/product/2022/06/18/b2d497f822b545da99af8b8f761d8e96sdzercwxj3.png',3,1,1,0,'2022-06-06 16:16:56','2022-06-18 16:46:15'),
	(70,68,'Baking','crmebimage/public/product/2022/06/18/02172a55fb74426d803185375a97e04fhldonwcwwv.png',3,1,1,0,'2022-06-06 16:17:24','2022-06-18 16:46:29'),
	(71,59,'Beverage','crmebimage/public/product/2022/06/18/556bf5fd0fc348c99873b018d9053e0ee4wmp526hf.png',2,1,1,0,'2022-06-06 16:17:48','2022-06-18 16:46:57'),
	(72,71,'drink','crmebimage/public/product/2022/06/18/556bf5fd0fc348c99873b018d9053e0ee4wmp526hf.png',3,1,1,0,'2022-06-06 16:18:17','2022-06-18 16:47:11'),
	(73,71,'Coffice','crmebimage/public/product/2022/06/18/be452292b38245bc9b65decbaaf2aa53sthba80p26.png',3,1,1,0,'2022-06-06 16:18:37','2022-06-18 16:47:20'),
	(74,71,'fruit ju','crmebimage/public/product/2022/06/18/77bd47e6e1da4b1092c12e6efe6f947fr5wc98pn1z.png',3,1,1,0,'2022-06-06 16:19:09','2022-06-18 16:47:32'),
	(75,68,'Noodle s','crmebimage/public/product/2022/06/18/f293b290922f4ccbab84d70f2880c914sty4qif2eg.png',3,1,1,0,'2022-06-06 16:20:10','2022-06-18 16:46:39'),
	(76,65,'QueChao','crmebimage/public/product/2022/06/18/6c814b9b8c864263a39baab06173c741l2zl3u87gc.png',3,1,1,0,'2022-06-06 16:20:36','2022-06-18 16:45:07'),
	(77,62,'Fried','crmebimage/public/product/2022/06/18/d0eb461e6c7c43cb910fb9db89d37c3buk9j6bsscl.png',3,1,1,0,'2022-06-06 16:20:58','2022-06-18 16:42:44'),
	(78,62,'Flat Mic','crmebimage/public/product/2022/06/18/86b4489ccdad406595bb57cece8424406plwrosd7r.jpg',3,1,1,0,'2022-06-06 16:57:16','2022-06-18 16:43:31'),
	(79,62,'Insuranc','crmebimage/public/product/2022/06/18/334e9846f761457d92a60292a3230065s15w09qylg.jpg',3,1,1,0,'2022-06-06 16:57:48','2022-06-18 16:43:53'),
	(80,62,'flip flo','crmebimage/public/product/2022/06/18/5f4ab802d2e64c9d9f68cf165362dbe8y4z2012h4y.jpg',3,1,1,0,'2022-06-06 16:58:14','2022-06-18 16:44:11'),
	(81,65,'Live Man','crmebimage/public/product/2022/06/18/f2b3d7340ffa43348733805175f4569avbr0xw892q.png',3,1,1,0,'2022-06-06 16:59:29','2022-06-18 16:45:25'),
	(82,21,'warm','crmebimage/public/product/2022/06/18/8eeb59b0137b41799b24d6319a10fbccrnp2v5ly1d.png',3,1,1,0,'2022-06-06 18:22:18','2022-06-18 16:23:09'),
	(83,25,'Can live','crmebimage/public/product/2022/06/18/460731c735d947849fe30ca4f38c3131waoa5ek011.jpg',3,1,1,0,'2022-06-06 18:22:57','2022-06-18 16:24:51'),
	(84,28,'Swimwear','crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png',3,1,1,0,'2022-06-06 18:25:13','2022-06-18 16:27:04'),
	(85,31,'Headgear','crmebimage/public/product/2022/06/18/b87f1fd44b764a3a8c2fbddb39a5fc11cm4e9h2utd.png',3,1,1,0,'2022-06-06 18:26:23','2022-06-18 16:30:10'),
	(86,34,'Smart de','crmebimage/public/product/2022/06/18/71b8a93bdecf46cfa56f57e2246f4fb3usgiba3jdw.jpg',3,1,1,0,'2022-06-06 18:27:21','2022-06-18 16:31:12'),
	(87,37,'Damp pan','crmebimage/public/product/2022/06/18/79e3eb9aaf184975a4079f8cad402e7ena2xzu5urm.jpg',3,1,1,0,'2022-06-06 18:31:27','2022-06-18 16:33:03'),
	(88,41,'full dre','crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png',3,1,1,0,'2022-06-06 18:49:30','2022-06-18 16:36:05'),
	(89,44,'charge','crmebimage/public/product/2022/06/18/b67d01a813b347ae9372292e7d4eabebq1k95rrknm.jpg',3,1,1,0,'2022-06-06 18:50:30','2022-06-18 16:38:17'),
	(90,15,'绿植','crmebimage/public/store/2022/06/09/1f21080415b140819ab3b67c650d7e16i2kwh1e63s.jpg',2,1,1,1,'2022-06-09 19:11:04','2022-06-09 19:11:04'),
	(91,90,'小型绿植','crmebimage/public/store/2022/06/09/5c6f148882724f90aa16871d5e51f481a2j9qflbyj.jpg',3,1,1,1,'2022-06-09 19:11:48','2022-06-09 19:11:48'),
	(92,5,'背带裤',NULL,3,1,1,1,'2022-06-15 11:51:18','2022-06-15 11:51:18'),
	(93,0,'Clothin','crmebimage/public/product/2022/06/18/664b4b33c955462b940c00d0d9ff776bxxvh0hmx6b.png',1,1,1,1,'2022-06-18 17:15:06','2022-06-18 17:15:06'),
	(94,93,'Dresses','crmebimage/public/product/2022/06/18/dea767f91a574b7d963c693efe8a5b96tfvwzez0gt.png',2,1,1,1,'2022-06-18 17:15:30','2022-06-18 17:15:30'),
	(95,0,'Apparel','crmebimage/public/product/2022/06/18/664b4b33c955462b940c00d0d9ff776bxxvh0hmx6b.png',1,1,1,0,'2022-06-18 17:18:33','2022-06-18 17:18:33'),
	(96,95,'Clothin','crmebimage/public/product/2022/06/18/d0d07f3a6f4246c3ac106519b5118b96aksli1vamn.png',2,1,1,0,'2022-06-18 17:19:02','2022-06-18 17:19:02'),
	(97,96,'Dresses','crmebimage/public/product/2022/06/18/dea767f91a574b7d963c693efe8a5b96tfvwzez0gt.png',3,1,1,0,'2022-06-18 17:19:25','2022-06-18 17:19:25');

/*!40000 ALTER TABLE `eb_product_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_product_day_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_product_day_record`;

CREATE TABLE `eb_product_day_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) NOT NULL DEFAULT '' COMMENT '日期',
  `product_id` int(11) DEFAULT '0' COMMENT '商品id',
  `mer_id` int(11) DEFAULT '0' COMMENT '商户id',
  `page_view` int(11) DEFAULT '0' COMMENT '浏览量',
  `collect_num` int(11) DEFAULT '0' COMMENT '收藏量',
  `add_cart_num` int(11) DEFAULT '0' COMMENT '加购件数',
  `order_product_num` int(11) DEFAULT '0' COMMENT '下单商品数（销售件数）',
  `order_success_product_fee` decimal(8,2) DEFAULT '0.00' COMMENT '销售额',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品日记录表';



# Dump of table eb_product_guarantee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_product_guarantee`;

CREATE TABLE `eb_product_guarantee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT '' COMMENT '保障条款名称',
  `icon` varchar(255) DEFAULT '' COMMENT '图标',
  `content` varchar(255) DEFAULT '' COMMENT '条款内容',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `is_del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品保障服务表';

LOCK TABLES `eb_product_guarantee` WRITE;
/*!40000 ALTER TABLE `eb_product_guarantee` DISABLE KEYS */;

INSERT INTO `eb_product_guarantee` (`id`, `name`, `icon`, `content`, `sort`, `is_show`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,'正品保证','https://api.beta.adminwm.java.crmeb.net/crmebimage/public/store/2022/04/19/2908ddf602cd4e8b93294a814e27640d31exoy3jje.png','该商品均为原厂正品，有防伪标识与唯一追溯码，100%保证正品。',1,1,0,'2022-03-14 12:43:10','2022-04-19 18:40:39'),
	(2,'假一赔四','crmebimage/public/content/2022/02/24/b25359cf27f249a088177a941fe87d06y26vcveqad.jpg','正品保障，用户通过专柜验证为假货，将以正品同等价格的4倍金额或四件正品商品赔偿用户。',1,1,0,'2022-03-14 12:49:31','2022-03-14 12:49:31'),
	(3,'7天无理由退款','crmebimage/public/product/2022/04/24/6af1a9cbf54f40bfb909c63091a73b5b7f1t2rupfy.png','在不影响二次销售的情况下，支持7天无理由退款',5,1,0,'2022-04-28 16:17:38','2022-05-16 09:28:04'),
	(4,'24-hour delivery','crmebimage/public/product/2022/04/24/ac9d87fa007745959959b80434011e83ct186vcn6r.png','24-hour delivery',6,1,0,'2022-04-28 16:19:11','2022-04-28 16:19:11');

/*!40000 ALTER TABLE `eb_product_guarantee` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_schedule_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_schedule_job`;

CREATE TABLE `eb_schedule_job` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_delte` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='定时任务';

LOCK TABLES `eb_schedule_job` WRITE;
/*!40000 ALTER TABLE `eb_schedule_job` DISABLE KEYS */;

INSERT INTO `eb_schedule_job` (`job_id`, `bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `is_delte`, `create_time`)
VALUES
	(1,'CouponOverdueTask','couponOverdue','','0 */1 * * * ?',0,'优惠券过期处理',0,'2021-12-01 10:55:06'),
	(2,'OrderAutoCancelTask','autoCancel','','0 */1 * * * ?',0,'系统自动取消未支付订单',0,'2021-12-01 10:55:06'),
	(3,'OrderCancelTask','userCancel','','0 */1 * * * ?',0,'用户取消订单处理',0,'2021-12-01 10:55:06'),
	(4,'OrderPaySuccessTask','orderPayAfter','','0 */1 * * * ?',0,'订单支付成功后置处理',0,'2021-12-01 10:55:07'),
	(5,'OrderRefundTask','orderRefund','','0 */1 * * * ?',0,'订单退款处理',0,'2021-12-01 10:55:07'),
	(6,'StatisticsTask','statistics','','0 0 0 */1 * ?',0,'统计定时任务',0,'2021-12-01 10:55:07'),
	(7,'AutoDeleteLogTask','autoDeleteLog','','0 0 0 */1 * ?',0,'自动删除不需要的历史日志',0,'2022-01-05 15:03:18'),
	(8,'OrderAutoReceivingTask','autoReceiving','','0 0 0 */1 * ?',0,'自动收货',0,'2022-01-05 15:03:18'),
	(9,'StatementTask','dailyStatement','','0 0 0 */1 * ?',0,'每日帐单定时任务',0,'2022-04-07 20:46:10'),
	(10,'StatementTask','monthStatement','','0 0 2 1 * ?',0,'每月帐单定时任务',0,'2022-04-08 11:06:29');

/*!40000 ALTER TABLE `eb_schedule_job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_schedule_job_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_schedule_job_log`;

CREATE TABLE `eb_schedule_job_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` int(11) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='定时任务日志';



# Dump of table eb_sensitive_method_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_sensitive_method_log`;

CREATE TABLE `eb_sensitive_method_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `admin_id` int(11) NOT NULL COMMENT '管理员id',
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '店铺id，平台为0',
  `admin_account` varchar(32) DEFAULT '' COMMENT '管理员账号',
  `description` varchar(50) DEFAULT '' COMMENT '接口描述',
  `method_type` varchar(30) DEFAULT '' COMMENT '业务类型',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `ip` varchar(50) DEFAULT '' COMMENT '主机地址',
  `request_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='敏感操作日志表';

LOCK TABLES `eb_sensitive_method_log` WRITE;
/*!40000 ALTER TABLE `eb_sensitive_method_log` DISABLE KEYS */;

INSERT INTO `eb_sensitive_method_log` (`id`, `admin_id`, `mer_id`, `admin_account`, `description`, `method_type`, `method`, `request_method`, `url`, `ip`, `request_param`, `result`, `status`, `error_msg`, `create_time`)
VALUES
	(1,21,2,'stivepeim@outlook.com','下架商品','修改','com.zbkj.admin.controller.merchant.MerchantProductController.offShell()','GET','/api/admin/merchant/product/offShell/10','113.132.64.198','{id=10}','{\"code\":200,\"message\":\"操作成功\"}',0,'','2022-06-20 16:23:39'),
	(2,21,2,'stivepeim@outlook.com','上架商品','修改','com.zbkj.admin.controller.merchant.MerchantProductController.putOn()','GET','/api/admin/merchant/product/putOnShell/10','8.218.3.137','{id=10}','{\"code\":200,\"message\":\"操作成功\"}',0,'','2022-06-20 16:30:16');

/*!40000 ALTER TABLE `eb_sensitive_method_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_shopping_product_day_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_shopping_product_day_record`;

CREATE TABLE `eb_shopping_product_day_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) DEFAULT NULL COMMENT '日期',
  `add_product_num` int(11) DEFAULT NULL COMMENT '新增商品数量',
  `page_view` int(11) DEFAULT NULL COMMENT '浏览量',
  `collect_num` int(11) DEFAULT NULL COMMENT '收藏量',
  `add_cart_num` int(11) DEFAULT NULL COMMENT '加购件数',
  `order_product_num` int(11) DEFAULT NULL COMMENT '下单商品数',
  `order_success_product_num` int(11) DEFAULT NULL COMMENT '交易成功商品数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商城商品日记录表';



# Dump of table eb_sms_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_sms_record`;

CREATE TABLE `eb_sms_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '短信发送记录编号',
  `uid` varchar(255) DEFAULT NULL COMMENT '短信平台账号',
  `phone` varchar(50) NOT NULL COMMENT '接受短信的手机号',
  `content` text COMMENT '短信内容',
  `add_ip` varchar(30) DEFAULT NULL COMMENT '添加记录ip',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `template` varchar(255) DEFAULT '' COMMENT '短信模板ID',
  `resultcode` int(6) unsigned DEFAULT NULL COMMENT '状态码 100=成功,130=失败,131=空号,132=停机,133=关机,134=无状态',
  `record_id` int(11) unsigned DEFAULT NULL COMMENT '发送记录id',
  `memo` text COMMENT '短信平台返回信息',
  `country_code` varchar(10) NOT NULL DEFAULT '' COMMENT '国标区号',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '请求状态码',
  `message` varchar(255) NOT NULL DEFAULT '' COMMENT '状态码的描述',
  `biz_id` varchar(255) NOT NULL DEFAULT '' COMMENT '发送回执ID。可根据发送回执ID在接口QuerySendDetails中查询具体的发送状态。',
  `ali_request_id` varchar(50) NOT NULL DEFAULT '' COMMENT '请求ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='短信发送记录表';

LOCK TABLES `eb_sms_record` WRITE;
/*!40000 ALTER TABLE `eb_sms_record` DISABLE KEYS */;

INSERT INTO `eb_sms_record` (`id`, `uid`, `phone`, `content`, `add_ip`, `create_time`, `template`, `resultcode`, `record_id`, `memo`, `country_code`, `code`, `message`, `biz_id`, `ali_request_id`)
VALUES
	(1,NULL,'+8615389137772','{\"codeNo\":642859}',NULL,'2022-06-20 10:18:15','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','76ED05DE-B332-583E-A0B0-94D4207114E4'),
	(2,NULL,'+8615389137772','{\"codeNo\":767728}',NULL,'2022-06-20 10:22:48','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','7435847F-2A80-5199-9435-42CF679CEA69'),
	(3,NULL,'+8618292417675','{\"codeNo\":161539}',NULL,'2022-06-20 15:31:51','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','A4D72FBA-DB86-5420-B425-BDCDB7F871BD'),
	(4,NULL,'+8618292417675','{\"codeNo\":527346}',NULL,'2022-06-20 15:32:48','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','AC975C28-C543-575A-9647-90A45E3E59DB'),
	(5,NULL,'+8618292417675','{\"codeNo\":799509}',NULL,'2022-06-20 15:42:22','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','55017751-0E8E-5C91-9441-E1134A1516B5'),
	(6,NULL,'+8618292417675','{\"codeNo\":141258}',NULL,'2022-06-20 17:47:26','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','F4A59CAD-BDE5-5851-8323-D044CC5CBBDD'),
	(7,NULL,'+8618292417675','{\"codeNo\":227470}',NULL,'2022-06-20 17:48:42','SMS_234157184',NULL,NULL,NULL,'','OK','OK','','F88DE564-DD94-57AE-9AA0-55EDECC0CE6D');

/*!40000 ALTER TABLE `eb_sms_record` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_sms_template
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_sms_template`;

CREATE TABLE `eb_sms_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `temp_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '短信模板id',
  `temp_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '模板类型:1-国内，2-国际/港澳台',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '模板说明',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '类型',
  `temp_key` varchar(50) NOT NULL DEFAULT '' COMMENT '模板编号',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态',
  `content` varchar(500) NOT NULL COMMENT '短息内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='短信模板表';

LOCK TABLES `eb_sms_template` WRITE;
/*!40000 ALTER TABLE `eb_sms_template` DISABLE KEYS */;

INSERT INTO `eb_sms_template` (`id`, `temp_id`, `temp_type`, `title`, `type`, `temp_key`, `status`, `content`, `create_time`)
VALUES
	(1,'SMS_234157184',1,'通用验证码','验证码','',1,'crmeb：您的验证码是 ${codeNo} ，请勿分享。','2021-12-09 19:31:13'),
	(2,'SMS_234157186',1,'发货提醒','通知','',1,'CRMEB：您的订单 ${orderNo} 已发货','2021-12-09 20:49:44'),
	(3,'SMS_234157188',1,'支付成功','通知','',1,'CRMEB：您在crmeb外贸版成功支付${price}，订单号为：${orderNo}','2021-12-09 19:33:38'),
	(4,'SMS_234157041',2,'通用验证码','验证码','',1,'crmeb: your verification code is ${codeNo} , please do not share.','2021-12-09 21:07:49'),
	(5,'SMS_234142007',2,'发货提醒','通知','',1,'CRMEB: Your order ${orderNo} has been shipped','2021-12-09 20:33:31'),
	(6,'SMS_234152022',2,'支付成功','通知','',1,'crmeb reminder: you have successfully paid ${price} in the foreign trade version of crmeb, the order number is: ${orderNo}','2021-12-09 21:12:05');

/*!40000 ALTER TABLE `eb_sms_template` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_cart
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_cart`;

CREATE TABLE `eb_store_cart` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '购物车表ID',
  `uid` int(10) unsigned NOT NULL COMMENT '用户ID',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户Id',
  `product_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `product_attr_unique` int(10) NOT NULL DEFAULT '0' COMMENT '商品属性',
  `cart_num` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '商品数量',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '购物车状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`uid`) USING BTREE,
  KEY `search_id` (`id`,`uid`,`mer_id`,`status`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='购物车表';



# Dump of table eb_store_coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_coupon`;

CREATE TABLE `eb_store_coupon` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '优惠券表ID',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户ID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `money` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '兑换的优惠券面值',
  `is_limited` tinyint(1) DEFAULT '0' COMMENT '是否限量, 默认0 不限量， 1限量',
  `total` int(11) NOT NULL DEFAULT '0' COMMENT '发放总数',
  `last_total` int(11) DEFAULT '0' COMMENT '剩余数量',
  `use_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '使用类型 1-商家券, 2-商品券, 3-平台券',
  `primary_key` varchar(255) NOT NULL DEFAULT '' COMMENT '所属商品id',
  `min_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '最低消费，0代表不限制',
  `receive_start_time` timestamp NOT NULL COMMENT '可领取开始时间',
  `receive_end_time` timestamp NULL DEFAULT NULL COMMENT '可领取结束时间',
  `is_fixed_time` tinyint(1) DEFAULT '0' COMMENT '是否固定使用时间, 默认0 否， 1是',
  `use_start_time` timestamp NULL DEFAULT NULL COMMENT '可使用时间范围 开始时间',
  `use_end_time` timestamp NULL DEFAULT NULL COMMENT '可使用时间范围 结束时间',
  `day` int(4) DEFAULT '0' COMMENT '天数',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '优惠券类型 1 手动领取, 2 新人券, 3 赠送券',
  `sort` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '排序',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态（0：关闭，1：开启）',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 状态（0：否，1：是）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE,
  KEY `is_del` (`is_del`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='优惠券表';

LOCK TABLES `eb_store_coupon` WRITE;
/*!40000 ALTER TABLE `eb_store_coupon` DISABLE KEYS */;

INSERT INTO `eb_store_coupon` (`id`, `mer_id`, `name`, `money`, `is_limited`, `total`, `last_total`, `use_type`, `primary_key`, `min_price`, `receive_start_time`, `receive_end_time`, `is_fixed_time`, `use_start_time`, `use_end_time`, `day`, `type`, `sort`, `status`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,1,'满10减1',1.00,0,0,0,1,'',10.00,'2022-06-15 14:28:55',NULL,0,NULL,NULL,10,1,9,1,0,'2022-06-15 14:28:55','2022-06-15 14:28:55');

/*!40000 ALTER TABLE `eb_store_coupon` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_coupon_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_coupon_user`;

CREATE TABLE `eb_store_coupon_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_id` int(11) NOT NULL COMMENT '优惠券id',
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '店铺id，平台为0',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `use_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '使用类型 1-商家券, 2-商品券, 3-平台券',
  `primary_key` varchar(255) NOT NULL DEFAULT '' COMMENT '所属商品id',
  `money` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '优惠券的面值',
  `min_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '最低消费多少金额可用优惠券',
  `type` varchar(32) NOT NULL DEFAULT 'send' COMMENT '获取方式，send后台发放, get-用户领取',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始使用时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `use_time` timestamp NULL DEFAULT NULL COMMENT '使用时间',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态（0：未使用，1：已使用, 2:已失效）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户优惠券表';



# Dump of table eb_store_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_order`;

CREATE TABLE `eb_store_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `master_order_no` varchar(32) NOT NULL COMMENT '主订单号',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户ID',
  `uid` int(11) unsigned NOT NULL COMMENT '用户id',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `real_name` varchar(32) NOT NULL COMMENT '收货人姓名',
  `user_phone` varchar(18) NOT NULL COMMENT '收货人电话',
  `user_address` varchar(100) NOT NULL COMMENT '收货人详细地址',
  `total_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单商品总数',
  `pro_total_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品总价',
  `total_postage` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '邮费',
  `total_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总价',
  `pay_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
  `pay_postage` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '支付邮费',
  `coupon_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '优惠券id',
  `coupon_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '优惠券金额',
  `paid` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `pay_type` varchar(32) NOT NULL DEFAULT '' COMMENT '支付方式:paypal',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '订单状态（0：待发货,1：待收货,2：已收货,3：已完成，9：已取消）',
  `refund_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '退款状态：0 未退款 1 申请中 2 退款中 3 已退款',
  `is_reply` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否评价，0-未评价，1-已评价',
  `delivery_name` varchar(64) DEFAULT '' COMMENT '快递公司名称',
  `delivery_code` varchar(50) DEFAULT '' COMMENT '快递公司简称',
  `delivery_id` varchar(64) DEFAULT '' COMMENT '快递单号',
  `user_remark` varchar(512) NOT NULL DEFAULT '' COMMENT '用户备注',
  `mer_remark` varchar(512) NOT NULL DEFAULT '' COMMENT '商户备注',
  `platform_remark` varchar(512) NOT NULL DEFAULT '' COMMENT '平台备注',
  `is_merchant_del` tinyint(1) DEFAULT '0' COMMENT '商户是否删除',
  `is_user_del` tinyint(1) DEFAULT '0' COMMENT '用户是否删除',
  `is_user_cancel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否用户取消',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `master_order_no` (`master_order_no`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户订单表';



# Dump of table eb_store_order_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_order_info`;

CREATE TABLE `eb_store_order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mer_order_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户ID',
  `product_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `product_name` varchar(128) NOT NULL COMMENT '商品名称',
  `image` varchar(256) NOT NULL COMMENT '商品图片',
  `attr_value_id` int(11) NOT NULL COMMENT '商品规格值 ID',
  `sku` varchar(128) NOT NULL COMMENT '商品sku',
  `price` decimal(8,2) unsigned NOT NULL COMMENT '商品价格',
  `pay_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '购买数量',
  `weight` decimal(8,2) unsigned NOT NULL COMMENT '重量',
  `volume` decimal(8,2) unsigned NOT NULL COMMENT '体积',
  `is_reply` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否评价，0-未评价，1-已评价',
  `is_receipt` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否收货，0-未收货，1-已收货',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_order_no` (`mer_order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户订单详情表';



# Dump of table eb_store_order_profit_sharing
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_order_profit_sharing`;

CREATE TABLE `eb_store_order_profit_sharing` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分账id',
  `mer_order_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户ID',
  `profit_sharing_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '分账金额',
  `profit_sharing_mer_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '分账给商户金额',
  `profit_sharing_time` timestamp NULL DEFAULT NULL COMMENT '分账时间',
  `profit_sharing_refund` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:未分账 1:已分账 2:已退款',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_order_no` (`mer_order_no`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单分账表';



# Dump of table eb_store_order_refund_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_order_refund_status`;

CREATE TABLE `eb_store_order_refund_status` (
  `refund_order_no` varchar(32) NOT NULL COMMENT '退款订单号',
  `change_type` varchar(32) NOT NULL COMMENT '操作类型：apply-申请退款，fail-决绝退款，refund-退款',
  `change_message` varchar(256) NOT NULL COMMENT '操作备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  KEY `refund_order_no` (`refund_order_no`) USING BTREE,
  KEY `change_type` (`change_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单退款操作记录表';



# Dump of table eb_store_order_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_order_status`;

CREATE TABLE `eb_store_order_status` (
  `mer_order_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `change_type` varchar(32) NOT NULL COMMENT '操作类型：create-订单生成，cancel-订单取消，pay-支付，express-发货，receipt-收货，complete-完成,refund-退款',
  `change_message` varchar(256) NOT NULL COMMENT '操作备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  KEY `mer_order_no` (`mer_order_no`) USING BTREE,
  KEY `change_type` (`change_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单操作记录表';



# Dump of table eb_store_product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product`;

CREATE TABLE `eb_store_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户Id',
  `image` varchar(256) NOT NULL DEFAULT '' COMMENT '商品图片',
  `flat_pattern` varchar(1000) NOT NULL DEFAULT '' COMMENT '展示图',
  `slider_image` varchar(2000) NOT NULL DEFAULT '' COMMENT '轮播图',
  `store_name` varchar(128) NOT NULL DEFAULT '' COMMENT '商品名称',
  `store_info` varchar(256) NOT NULL DEFAULT '' COMMENT '商品简介',
  `keyword` varchar(256) NOT NULL DEFAULT '' COMMENT '关键字',
  `cate_id` varchar(64) NOT NULL DEFAULT '' COMMENT '商户分类id(逗号拼接)',
  `brand_id` int(11) NOT NULL DEFAULT '0' COMMENT '品牌id',
  `category_id` int(11) NOT NULL DEFAULT '0' COMMENT '平台分类id',
  `guarantee_ids` varchar(64) NOT NULL DEFAULT '0' COMMENT '保障服务ids(英文逗号拼接)',
  `price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `vip_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '会员价格',
  `ot_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '市场价',
  `postage` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '邮费',
  `unit_name` varchar(32) NOT NULL DEFAULT '' COMMENT '单位名',
  `sales` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '销量',
  `stock` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '库存',
  `give_integral` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',
  `cost` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '成本价',
  `ficti` int(11) DEFAULT '0' COMMENT '虚拟销量',
  `browse` int(11) DEFAULT '0' COMMENT '浏览量',
  `sort` smallint(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `rank` smallint(11) NOT NULL DEFAULT '0' COMMENT '总后台排序',
  `spec_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '规格 0单 1多',
  `is_recycle` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否回收站',
  `is_show` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0：未上架，1：上架）',
  `is_forced` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否强制下架，0-否，1-是',
  `audit_status` int(2) unsigned NOT NULL DEFAULT '1' COMMENT '审核状态：0-待审核，1-审核成功，2-审核拒绝',
  `reason` varchar(100) NOT NULL DEFAULT '' COMMENT '拒绝原因',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品表';

LOCK TABLES `eb_store_product` WRITE;
/*!40000 ALTER TABLE `eb_store_product` DISABLE KEYS */;

INSERT INTO `eb_store_product` (`id`, `mer_id`, `image`, `flat_pattern`, `slider_image`, `store_name`, `store_info`, `keyword`, `cate_id`, `brand_id`, `category_id`, `guarantee_ids`, `price`, `vip_price`, `ot_price`, `postage`, `unit_name`, `sales`, `stock`, `give_integral`, `cost`, `ficti`, `browse`, `sort`, `rank`, `spec_type`, `is_recycle`, `is_show`, `is_forced`, `audit_status`, `reason`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,1,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png','','[\"crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png\",\"crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png\"]','PORTS宝姿商场同款2022春夏新款女装“牵萦蕾丝”印花连衣长裙SD8D027PPP066 PINK BLACK 6','PORTS宝姿商场同款2022春夏新款女装“牵萦蕾丝”印花连衣长裙SD8D027PPP066 PINK BLACK 6','连衣裙','1,2',1,6,'4,3,1,2',10.00,0.00,200.00,1.00,'条',0,2000,0,1000.00,0,0,99,0,1,1,0,1,1,'',1,'2022-06-15 14:18:10','2022-06-18 16:50:28'),
	(2,1,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg','','[\"crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg\",\"crmebimage/public/maintain/2022/06/17/019ff26087b2487f8e20fbe6c131e9f86my2w8ip31.jpg\",\"crmebimage/public/maintain/2022/06/17/9d9ad2f85b304fffb3229865f55d01adlya8znoj0g.jpg\"]','PORTS宝姿女装2022春季新款时尚气质七分袖连衣裙ALM8D600JWP052 BLACK 4','PORTS宝姿女装2022春季新款时尚气质七分袖连衣裙ALM8D600JWP052 BLACK 4','连衣裙','1,2',1,6,'4,3,1,2',13.00,0.00,300.00,1.00,'条',0,1000,0,1000.00,0,0,8,0,1,1,0,1,1,'',1,'2022-06-15 14:22:56','2022-06-18 16:50:30'),
	(3,1,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg','','[\"crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg\",\"crmebimage/public/maintain/2022/06/17/a7eeb7a9d48d4b219f4dc1eeb804eb32uywxotwv5r.jpg\",\"crmebimage/public/maintain/2022/06/17/cb231b389660489e961db554b9f686c4fceg7unng6.jpg\"]','PORTS宝姿商场同款2022春夏新款女装烫钻圆领无袖鱼尾连衣裙SD8D048PLQ011 WHITE 2','PORTS宝姿商场同款2022春夏新款女装烫钻圆领无袖鱼尾连衣裙SD8D048PLQ011 WHITE 2','连衣裙','1,2',1,6,'4,3,2,1',20.00,0.00,450.00,1.00,'条',0,1000,0,1000.00,0,0,99,0,1,1,0,1,1,'',1,'2022-06-15 14:24:57','2022-06-18 16:50:26'),
	(4,3,'crmebimage/public/content/2022/06/18/ec7d5ac111b74237b61cd080f13c53ee1igvpmmg6f.png','','[\"crmebimage/public/content/2022/06/18/1497de9478b14f4e99b52b7f089827963a7jbfep86.jpg\",\"crmebimage/public/content/2022/06/18/47b779fb10424c1d8b4dfc415386cc24go3tsrof3z.jpg\",\"crmebimage/public/content/2022/06/18/ec7d5ac111b74237b61cd080f13c53ee1igvpmmg6f.png\",\"crmebimage/public/content/2022/06/18/53fb1d2323634291ad699c8d3abbfcb4hthhqrj5s4.jpg\",\"crmebimage/public/content/2022/06/18/5d77a6e0346744faad9cf5cad8c86294rf94ydusrf.jpg\"]','sokany 122 Hot selling non-stick waffle maker Home waffle maker mini pancake machine for children common','sokany 122 Hot selling non-stick waffle maker Home waffle maker mini pancake machine for children common','cake','4',2,30,'3',9.99,0.00,13.99,6.00,'kg',0,10,0,5.99,0,3,1,0,0,0,1,0,1,'',0,'2022-06-18 15:22:52','2022-06-20 16:37:37'),
	(5,1,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg','','[\"crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg\",\"crmebimage/public/content/2022/06/18/2c500367798d4254afd424a3481f6ca4hlmjyk6b2s.jpg\",\"crmebimage/public/content/2022/06/18/cb89e089bea24c49a63d7b7a50f12793ruixp9am8n.jpg\",\"crmebimage/public/content/2022/06/18/d4d15af3f376477e82270072fab9272ag6hvs9efnq.jpg\",\"crmebimage/public/content/2022/06/18/bee5e6e174344246a22d51f3ba7c9befwkvijd70t6.jpg\"]','Crepe Off-the-Shoulder Dress','The LAUREN Ralph Lauren® Crepe Off-The-Shoulder Dress looks adorable and stylish. It offers the perfect look for a fun night out.','Dresses','1,2',3,97,'4,3,1,2',124.00,0.00,165.00,1.00,'strip',0,1500,0,200.00,0,1,99,0,1,0,1,0,1,'',0,'2022-06-18 17:29:05','2022-06-20 15:36:59'),
	(6,1,'crmebimage/public/content/2022/06/18/0d99fd5bd89e45a7a73559f7ffb0da15mfjbqbmejy.jpg','','[\"crmebimage/public/content/2022/06/18/0d99fd5bd89e45a7a73559f7ffb0da15mfjbqbmejy.jpg\",\"crmebimage/public/content/2022/06/18/40d470010d4e4f9f8b04e0d22fb9c80fpmu92j3p7y.jpg\",\"crmebimage/public/content/2022/06/18/3951deb637da460fb57edf00e6669cd6obk7hazakn.jpg\",\"crmebimage/public/content/2022/06/18/f91e8b39378d415fbf6ef73afe5db778i93yiwhgtn.jpg\"]','Foiled Jersey Cocktail Dress','Exhibit an adorable and dainty look wearing the LAUREN Ralph Lauren® Foiled Jersey Cocktail Dress.\n','Dress','1,2',3,97,'4,3,1,2',130.00,0.00,175.00,1.00,'strip',0,400,0,100.00,0,2,99,0,1,0,1,0,1,'',0,'2022-06-18 17:43:49','2022-06-20 09:58:01'),
	(7,1,'crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg','','[\"crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg\",\"crmebimage/public/content/2022/06/18/5fb28aac023945648658b2ee10713a28ys6ekff7iy.jpg\",\"crmebimage/public/content/2022/06/18/1f55a4f592c84b92b7481682f0bf50dfv0skn4t6vb.jpg\",\"crmebimage/public/content/2022/06/18/b30fe9e1bda54437880cc6cb1014de183z0aom39wu.jpg\"]','Ruffle-Trim Chiffon Dress','Have a romantic appeal wearing the cute and stylish LAUREN Ralph Lauren® Ruffle-Trim Chiffon Dress.','Dress','1,2',3,97,'4,1,3,2',145.00,0.00,195.00,1.00,'strip',0,600,0,100.00,0,1,99,0,1,0,1,0,1,'',0,'2022-06-18 17:47:18','2022-06-18 17:52:43'),
	(8,1,'crmebimage/public/content/2022/06/18/682a2309187e4012a5e21766dd6d863a5qsp7t24aj.jpg','','[\"crmebimage/public/content/2022/06/18/682a2309187e4012a5e21766dd6d863a5qsp7t24aj.jpg\",\"crmebimage/public/content/2022/06/18/0b795f3d96904397a9f59e6d81edb02d6buesnfw1h.jpg\",\"crmebimage/public/content/2022/06/18/4db62bf9e57444788d31af6ef369eeeapxmvgdkr50.jpg\"]','Cotton Eyelet Midi Dress','LAUREN Ralph Lauren® Cotton Eyelet Midi Dress is s classic pick to wear on any casual day!','Dress','1,2',3,97,'4,3,1,2',245.00,0.00,300.00,1.00,'strip',0,200,0,200.00,0,2,100,0,0,0,1,0,1,'',0,'2022-06-18 17:52:55','2022-06-20 17:46:48'),
	(9,1,'crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg','','[\"crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg\",\"crmebimage/public/content/2022/06/18/2b866d80688c41ee9c2e802c4644d93dvuq79bza7q.jpg\",\"crmebimage/public/content/2022/06/18/5f16f53a10464bf8b81de1c1a9e3c1fea2bi5y2u1s.jpg\",\"crmebimage/public/content/2022/06/18/6b7a785e4e9b4447a63c42d5f8c762183t972ze4pg.jpg\"]','Cotton Eyelet Midi Dress','LAUREN Ralph Lauren® Cotton Eyelet Midi Dress is s classic pick to wear on any casual day!','Dress','1,2',3,97,'4,3,1,2',245.00,0.00,300.00,1.00,'strip',0,900,0,200.00,0,1,99,0,1,0,1,0,1,'',0,'2022-06-18 18:00:11','2022-06-20 11:33:22'),
	(10,2,'crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png','','[\"crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png\"]','Zhen Tou','Zhen Tou','Zhen Tou','7',2,26,'3,1,2',0.50,0.00,2.00,1.00,'S',0,999,0,1.00,0,4,1,0,0,0,1,0,1,'',0,'2022-06-18 18:07:02','2022-06-20 11:36:08'),
	(11,3,'crmebimage/public/content/2022/06/18/b9a0f136b58243e58bc938c6ff3e57aes7rz0y0g8f.jpg','','[\"crmebimage/public/content/2022/06/18/b9a0f136b58243e58bc938c6ff3e57aes7rz0y0g8f.jpg\",\"crmebimage/public/content/2022/06/18/8de2d2fb27f547cb8ff23b639c5f4bafa6jmpzx9zw.jpg\",\"crmebimage/public/content/2022/06/18/8d941ced90064eb094dce282a94e1bcafd95xw515z.jpg\"]','022 New Foldable 3 in 1 Portable Mobile Phone 15W Magnetic Qi Wireless Charger White TYPE-C','2022 New Foldable 3 in 1 Portable Mobile Phone 15W Magnetic Qi Wireless Charger White TYPE-C','2022 New Foldable','6',2,23,'3',16.93,0.00,18.00,1.00,'Pieces',0,666,0,6.00,0,0,1,0,0,0,0,0,1,'',0,'2022-06-18 19:51:04','2022-06-18 19:51:04'),
	(12,3,'crmebimage/public/content/2022/06/18/4bc7d0814fff4155bd2d8a1c20ef209bdpgnr6jncg.jpg','','[\"crmebimage/public/content/2022/06/18/fa404430a3464ac1932c15cf8efcc3ac3v0ok05c33.jpg\",\"crmebimage/public/content/2022/06/18/4bc7d0814fff4155bd2d8a1c20ef209bdpgnr6jncg.jpg\"]','2022 summer new American retro lapel short-sleeved shirt men\'s loose all-match design niche shirt White L','2022 summer new American retro lapel short-sleeved shirt men\'s loose all-match design niche shirt White L','2022 summer new American','4',2,23,'3,1',7.25,0.00,9.25,1.00,'Pieces',0,789,0,2.25,0,0,1,0,0,0,0,0,1,'',0,'2022-06-18 19:55:47','2022-06-18 19:55:47'),
	(13,3,'crmebimage/public/content/2022/06/18/7b5634bed37c4e0a8db0505797ea47bezxto1e48jr.jpg','','[\"crmebimage/public/content/2022/06/18/7b5634bed37c4e0a8db0505797ea47bezxto1e48jr.jpg\"]','Eco Friendly Home Restaurant Black Walnut Wood Wooden Irregular Sushi Fruit Snack Tea Coffee Serving Tray Natural 60x17x2.5cm','Eco Friendly Home Restaurant Black Walnut Wood Wooden Irregular Sushi Fruit Snack Tea Coffee Serving Tray Natural 60x17x2.5cm','Black Walnut Wood Wooden','4,5',2,23,'3,1',15.54,0.00,16.54,1.00,'pices',0,845,0,5.54,0,0,1,0,0,0,0,0,1,'',0,'2022-06-18 19:58:51','2022-06-18 19:58:51'),
	(14,3,'crmebimage/public/content/2022/06/18/154713b5a1d04d98b47f0b6602e0dfe61xhxn12658.jpg','','[\"crmebimage/public/content/2022/06/18/154713b5a1d04d98b47f0b6602e0dfe61xhxn12658.jpg\",\"crmebimage/public/content/2022/06/18/936cbb1e7f4548d2b7d4b7e51dcce066b1uy2qi1qp.jpg\"]','Customized 4.5 Inch Creative Ceramic Mortar Mashed Mini Auxiliary Food Grinder Manual Ceramic Grinding Bowl Red','Customized 4.5 Inch Creative Ceramic Mortar Mashed Mini Auxiliary Food Grinder Manual Ceramic Grinding Bowl Red',' Manual Ceramic Grinding Bowl Red','4',2,23,'3,1',27.00,0.00,45.00,1.00,'pices',0,56,0,7.00,0,0,1,0,0,0,0,0,1,'',0,'2022-06-18 21:01:01','2022-06-18 21:01:01');

/*!40000 ALTER TABLE `eb_store_product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_product_attr
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_attr`;

CREATE TABLE `eb_store_product_attr` (
  `id` mediumint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `attr_name` varchar(32) NOT NULL COMMENT '属性名',
  `attr_values` varchar(1000) NOT NULL COMMENT '属性值',
  `type` tinyint(1) DEFAULT '0' COMMENT '活动类型 0=商品，1=秒杀，2=砍价，3=拼团',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `store_id` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品属性表';

LOCK TABLES `eb_store_product_attr` WRITE;
/*!40000 ALTER TABLE `eb_store_product_attr` DISABLE KEYS */;

INSERT INTO `eb_store_product_attr` (`id`, `product_id`, `attr_name`, `attr_values`, `type`, `is_del`)
VALUES
	(1,1,'颜色','PINK BLACK,BLUE BLACK',0,1),
	(2,1,'尺码','2,4,6,8,10',0,1),
	(3,2,'尺码','2,4,6,8,10',0,1),
	(4,3,'尺码','2,4,6,8,10',0,1),
	(5,1,'颜色','PINK BLACK,BLUE BLACK',0,0),
	(6,1,'尺码','2,4,6,8,10',0,0),
	(7,2,'尺码','2,4,6,8,10',0,0),
	(8,3,'尺码','2,4,6,8,10',0,0),
	(9,4,'规格','默认',0,0),
	(10,5,'尺码','2,4,6,8,10',0,0),
	(11,6,'尺码','6,8',0,0),
	(12,7,'尺码','6,8,10',0,0),
	(13,8,'规格','默认',0,0),
	(14,9,'尺码','6,8,10',0,0),
	(15,10,'规格','默认',0,0),
	(16,11,'规格','默认',0,0),
	(17,12,'规格','默认',0,0),
	(18,13,'规格','默认',0,0),
	(19,14,'规格','默认',0,0);

/*!40000 ALTER TABLE `eb_store_product_attr` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_product_attr_value
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_attr_value`;

CREATE TABLE `eb_store_product_attr_value` (
  `id` mediumint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `sku` varchar(128) NOT NULL COMMENT '商品属性索引值 (attr_value|attr_value[|....])',
  `stock` int(10) unsigned NOT NULL COMMENT '属性对应的库存',
  `sales` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '销量',
  `price` decimal(8,2) unsigned NOT NULL COMMENT '属性金额',
  `image` varchar(1000) DEFAULT NULL COMMENT '图片',
  `cost` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '成本价',
  `bar_code` varchar(50) NOT NULL DEFAULT '' COMMENT '商品条码',
  `ot_price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '原价',
  `weight` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '重量',
  `volume` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '体积',
  `brokerage` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '一级返佣',
  `brokerage_two` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '二级返佣',
  `type` tinyint(1) DEFAULT '0' COMMENT '活动类型 0=商品，1=秒杀，2=砍价，3=拼团',
  `quota` int(11) DEFAULT NULL COMMENT '活动限购数量',
  `quota_show` int(11) DEFAULT NULL COMMENT '活动限购数量显示',
  `attr_value` text COMMENT 'attr_values 创建更新时的属性对应',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0-否，1-是',
  `version` int(11) DEFAULT '0' COMMENT '并发版本控制',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `unique` (`sku`) USING BTREE,
  KEY `store_id` (`product_id`,`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品属性值表';

LOCK TABLES `eb_store_product_attr_value` WRITE;
/*!40000 ALTER TABLE `eb_store_product_attr_value` DISABLE KEYS */;

INSERT INTO `eb_store_product_attr_value` (`id`, `product_id`, `sku`, `stock`, `sales`, `price`, `image`, `cost`, `bar_code`, `ot_price`, `weight`, `volume`, `brokerage`, `brokerage_two`, `type`, `quota`, `quota_show`, `attr_value`, `is_del`, `version`)
VALUES
	(1,1,'PINK BLACK,2',200,0,10.00,'crmebimage/public/content/2022/06/15/04e98f868fb54e9583d27fd8ce498c36l89odc7kg3.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"2\"}',1,0),
	(2,1,'PINK BLACK,4',200,0,10.00,'crmebimage/public/content/2022/06/15/04e98f868fb54e9583d27fd8ce498c36l89odc7kg3.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"4\"}',1,0),
	(3,1,'PINK BLACK,6',200,0,10.00,'crmebimage/public/content/2022/06/15/04e98f868fb54e9583d27fd8ce498c36l89odc7kg3.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"6\"}',1,0),
	(4,1,'PINK BLACK,8',200,0,10.00,'crmebimage/public/content/2022/06/15/04e98f868fb54e9583d27fd8ce498c36l89odc7kg3.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"8\"}',1,0),
	(5,1,'PINK BLACK,10',200,0,10.00,'crmebimage/public/content/2022/06/15/04e98f868fb54e9583d27fd8ce498c36l89odc7kg3.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"10\"}',1,0),
	(6,1,'BLUE BLACK,2',200,0,10.00,'crmebimage/public/content/2022/06/15/89c1c42a46454e6ba5df5002d33bd0d5oshxikkd9y.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"2\"}',1,0),
	(7,1,'BLUE BLACK,4',200,0,10.00,'crmebimage/public/content/2022/06/15/89c1c42a46454e6ba5df5002d33bd0d5oshxikkd9y.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"4\"}',1,0),
	(8,1,'BLUE BLACK,6',200,0,10.00,'crmebimage/public/content/2022/06/15/89c1c42a46454e6ba5df5002d33bd0d5oshxikkd9y.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"6\"}',1,0),
	(9,1,'BLUE BLACK,8',200,0,10.00,'crmebimage/public/content/2022/06/15/89c1c42a46454e6ba5df5002d33bd0d5oshxikkd9y.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"8\"}',1,0),
	(10,1,'BLUE BLACK,10',200,0,10.00,'crmebimage/public/content/2022/06/15/89c1c42a46454e6ba5df5002d33bd0d5oshxikkd9y.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"10\"}',1,0),
	(11,2,'2',200,0,13.00,'crmebimage/public/content/2022/06/15/d882a15eaf154dc1a98890791c7ccf08i3p7n6xzi2.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"2\"}',1,0),
	(12,2,'4',200,0,13.00,'crmebimage/public/content/2022/06/15/d882a15eaf154dc1a98890791c7ccf08i3p7n6xzi2.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"4\"}',1,0),
	(13,2,'6',200,0,13.00,'crmebimage/public/content/2022/06/15/d882a15eaf154dc1a98890791c7ccf08i3p7n6xzi2.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',1,0),
	(14,2,'8',200,0,13.00,'crmebimage/public/content/2022/06/15/d882a15eaf154dc1a98890791c7ccf08i3p7n6xzi2.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',1,0),
	(15,2,'10',200,0,13.00,'crmebimage/public/content/2022/06/15/d882a15eaf154dc1a98890791c7ccf08i3p7n6xzi2.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',1,0),
	(16,3,'2',200,0,20.00,'crmebimage/public/maintain/2022/05/12/6c79ade3e6f54c74a0061d2da9ab7154qrg46klfgd.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"2\"}',1,0),
	(17,3,'4',200,0,20.00,'crmebimage/public/maintain/2022/05/12/6c79ade3e6f54c74a0061d2da9ab7154qrg46klfgd.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"4\"}',1,0),
	(18,3,'6',200,0,20.00,'crmebimage/public/maintain/2022/05/12/6c79ade3e6f54c74a0061d2da9ab7154qrg46klfgd.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',1,0),
	(19,3,'8',200,0,20.00,'crmebimage/public/maintain/2022/05/12/6c79ade3e6f54c74a0061d2da9ab7154qrg46klfgd.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',1,0),
	(20,3,'10',200,0,20.00,'crmebimage/public/maintain/2022/05/12/6c79ade3e6f54c74a0061d2da9ab7154qrg46klfgd.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',1,0),
	(21,1,'PINK BLACK,2',200,0,10.00,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"2\"}',0,0),
	(22,1,'PINK BLACK,4',200,0,10.00,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"4\"}',0,0),
	(23,1,'PINK BLACK,6',200,0,10.00,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"6\"}',0,0),
	(24,1,'PINK BLACK,8',200,0,10.00,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"8\"}',0,0),
	(25,1,'PINK BLACK,10',200,0,10.00,'crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"PINK BLACK\",\"尺码\":\"10\"}',0,0),
	(26,1,'BLUE BLACK,2',200,0,10.00,'crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"2\"}',0,0),
	(27,1,'BLUE BLACK,4',200,0,10.00,'crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"4\"}',0,0),
	(28,1,'BLUE BLACK,6',200,0,10.00,'crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"6\"}',0,0),
	(29,1,'BLUE BLACK,8',200,0,10.00,'crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"8\"}',0,0),
	(30,1,'BLUE BLACK,10',200,0,10.00,'crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png',1000.00,'',200.00,0.00,0.00,0.00,0.00,0,0,0,'{\"颜色\":\"BLUE BLACK\",\"尺码\":\"10\"}',0,0),
	(31,2,'2',200,0,13.00,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"2\"}',0,0),
	(32,2,'4',200,0,13.00,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"4\"}',0,0),
	(33,2,'6',200,0,13.00,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(34,2,'8',200,0,13.00,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(35,2,'10',200,0,13.00,'crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg',1000.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',0,0),
	(36,3,'2',200,0,20.00,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"2\"}',0,0),
	(37,3,'4',200,0,20.00,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"4\"}',0,0),
	(38,3,'6',200,0,20.00,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(39,3,'8',200,0,20.00,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(40,3,'10',200,0,20.00,'crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg',1000.00,'',450.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',0,0),
	(41,4,'默认',10,0,9.99,'crmebimage/public/content/2022/06/18/ec7d5ac111b74237b61cd080f13c53ee1igvpmmg6f.png',5.99,'',13.99,20.00,60.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(42,5,'2',300,0,124.00,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg',200.00,'',165.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"2\"}',0,0),
	(43,5,'4',300,0,124.00,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg',200.00,'',165.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"4\"}',0,0),
	(44,5,'6',300,0,124.00,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg',200.00,'',165.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(45,5,'8',300,0,124.00,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg',200.00,'',165.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(46,5,'10',300,0,124.00,'crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg',200.00,'',165.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',0,0),
	(47,6,'6',200,0,130.00,'crmebimage/public/content/2022/06/18/0d99fd5bd89e45a7a73559f7ffb0da15mfjbqbmejy.jpg',100.00,'',175.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(48,6,'8',200,0,130.00,'crmebimage/public/content/2022/06/18/0d99fd5bd89e45a7a73559f7ffb0da15mfjbqbmejy.jpg',100.00,'',175.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(49,7,'6',200,0,145.00,'crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg',100.00,'',195.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(50,7,'8',200,0,145.00,'crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg',100.00,'',195.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(51,7,'10',200,0,145.00,'crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg',100.00,'',195.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',0,0),
	(52,8,'默认',200,0,245.00,'crmebimage/public/content/2022/06/18/682a2309187e4012a5e21766dd6d863a5qsp7t24aj.jpg',200.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(53,9,'6',300,0,245.00,'crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg',200.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"6\"}',0,0),
	(54,9,'8',300,0,245.00,'crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg',200.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"8\"}',0,0),
	(55,9,'10',300,0,245.00,'crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg',200.00,'',300.00,0.00,0.00,0.00,0.00,0,0,0,'{\"尺码\":\"10\"}',0,0),
	(56,10,'默认',999,0,0.50,'crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png',1.00,'',2.00,1.00,1.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(57,11,'默认',666,0,16.93,'crmebimage/public/content/2022/06/18/b9a0f136b58243e58bc938c6ff3e57aes7rz0y0g8f.jpg',6.00,'',18.00,0.50,0.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(58,12,'默认',789,0,7.25,'crmebimage/public/content/2022/06/18/4bc7d0814fff4155bd2d8a1c20ef209bdpgnr6jncg.jpg',2.25,'',9.25,0.00,0.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(59,13,'默认',845,0,15.54,'crmebimage/public/content/2022/06/18/7b5634bed37c4e0a8db0505797ea47bezxto1e48jr.jpg',5.54,'',16.54,0.00,0.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0),
	(60,14,'默认',56,0,27.00,'crmebimage/public/content/2022/06/18/154713b5a1d04d98b47f0b6602e0dfe61xhxn12658.jpg',7.00,'',45.00,0.00,0.00,0.00,0.00,0,0,0,'{\"规格\":\"默认\"}',0,0);

/*!40000 ALTER TABLE `eb_store_product_attr_value` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_product_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_category`;

CREATE TABLE `eb_store_product_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户Id',
  `pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon',
  `sort` int(5) NOT NULL DEFAULT '999' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `is_del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户商品分类表';

LOCK TABLES `eb_store_product_category` WRITE;
/*!40000 ALTER TABLE `eb_store_product_category` DISABLE KEYS */;

INSERT INTO `eb_store_product_category` (`id`, `mer_id`, `pid`, `name`, `icon`, `sort`, `is_show`, `is_del`, `create_time`, `update_time`)
VALUES
	(1,1,0,' 服饰内衣',NULL,1,1,0,'2022-06-15 11:48:03','2022-06-15 11:48:03'),
	(2,1,1,'连衣裙',NULL,1,1,0,'2022-06-15 11:48:13','2022-06-15 11:48:13'),
	(3,1,1,'运动背心',NULL,1,1,0,'2022-06-17 17:44:52','2022-06-17 17:44:52'),
	(4,3,0,'Sandwich','crmebimage/public/content/2022/06/18/53fb1d2323634291ad699c8d3abbfcb4hthhqrj5s4.jpg',1,1,0,'2022-06-18 15:05:20','2022-06-18 15:05:20'),
	(5,3,0,'electric','crmebimage/public/content/2022/06/18/5d77a6e0346744faad9cf5cad8c86294rf94ydusrf.jpg',1,1,0,'2022-06-18 15:05:41','2022-06-18 15:05:41'),
	(6,3,5,'cake','crmebimage/public/content/2022/06/18/ec7d5ac111b74237b61cd080f13c53ee1igvpmmg6f.png',1,1,0,'2022-06-18 15:06:05','2022-06-18 15:06:05'),
	(7,2,0,'Modern','crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png',1,1,0,'2022-06-18 18:05:40','2022-06-18 18:05:40'),
	(8,2,7,'Pillow','crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png',1,1,0,'2022-06-20 16:27:14','2022-06-20 16:27:14');

/*!40000 ALTER TABLE `eb_store_product_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_product_coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_coupon`;

CREATE TABLE `eb_store_product_coupon` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL DEFAULT '0' COMMENT '商品id',
  `issue_coupon_id` int(10) NOT NULL DEFAULT '0' COMMENT '优惠劵id',
  `add_time` int(10) NOT NULL DEFAULT '0' COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品优惠券表';



# Dump of table eb_store_product_description
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_description`;

CREATE TABLE `eb_store_product_description` (
  `product_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品ID',
  `description` text NOT NULL COMMENT '商品详情',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '商品类型',
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `product_id` (`product_id`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品描述表';

LOCK TABLES `eb_store_product_description` WRITE;
/*!40000 ALTER TABLE `eb_store_product_description` DISABLE KEYS */;

INSERT INTO `eb_store_product_description` (`product_id`, `description`, `type`, `id`)
VALUES
	(1,'<p><img class=\"wscnph\" src=\"crmebimage/public/maintain/2022/06/17/5619ef4031434955aba0af0a4fa3037coznklnzk1k.jpg\" /></p>',0,4),
	(2,'<p><img class=\"wscnph\" src=\"crmebimage/public/maintain/2022/06/17/d9f904ddbce341ba9cdb3d0827feef5fswhz8w8oef.jpg\" /></p>',0,5),
	(3,'<p><img class=\"wscnph\" src=\"crmebimage/public/maintain/2022/06/17/5b4c7b8ef24d41b494480675a915a2e2hyo0trk8et.jpg\" /><img class=\"wscnph\" src=\"crmebimage/public/maintain/2022/06/17/c5de48d783c840fdb08be567974c781aoiv8w9cmpy.jpg\" /></p>',0,6),
	(4,'<p><img src=\"https://img20.360buyimg.com/pop/jfs/t1/218448/6/18392/549164/628ddb23E7d1ce16c/38997fb2fc20ea45.jpg\" /></p>\n<p><img src=\"https://img13.360buyimg.com/pop/jfs/t1/85232/23/25981/555977/628ddb31E0c4e0155/aaad0c7464354cfc.jpg\" /></p>',0,7),
	(5,'<p><img class=\"wscnph\" src=\"crmebimage/public/content/2022/06/18/fe7dc9d546734d2b870e16e36f4814326h3fy8ol7m.jpg\" /></p>\n<ul>\n<li class=\"J6-z\">The LAUREN Ralph Lauren&reg; Crepe Off-The-Shoulder Dress looks adorable and stylish. It offers the perfect look for a fun night out.</li>\n<li>Slim fit.</li>\n<li>Above-the-knee length design.</li>\n<li>Off-the-shoulder neckline.</li>\n<li>Knotted draped design on the waist.</li>\n<li>Hook-and-eye and zippered back closure.</li>\n<li>89% polyester, 11% elastane.</li>\n<li>Machine wash, lay flat to dry.</li>\n<li>Imported.</li>\n<li>Product measurements were taken using size 4. Please note that measurements may vary by size.</li>\n<li>Measurements:\n<ul>\n<li>Length: 37&nbsp;<sup>1</sup>&frasl;<sub>2</sub>&nbsp;in</li>\n</ul>\n</li>\n</ul>',0,8),
	(6,'<div class=\"Q6-z\"><a class=\"oo-z\" title=\"LAUREN Ralph Lauren\" href=\"https://www.zappos.com/b/lauren-ralph-lauren/brand/358\" data-track-action=\"Product-Page\" data-track-label=\"Tabs\" data-track-value=\"Brand-Logo\"><img class=\"fk-z gk-z\" src=\"https://www.zappos.com/boutiques/3178/lauren-ralph-lauren_head_060915.jpg\" alt=\"LAUREN Ralph Lauren\" /></a></div>\n<ul>\n<li class=\"J6-z\">Exhibit an adorable and dainty look wearing the LAUREN Ralph Lauren&reg; Foiled Jersey Cocktail Dress.</li>\n<li>Surplice neckline and flutter sleeves.</li>\n<li>Zipper detailing with hook-and-eye closure on the back.</li>\n<li>Hem hits the knee.</li>\n<li>Pull-on style.</li>\n<li>95% polyester, 5% elastane.</li>\n<li>Hand wash.</li>\n<li>Imported.</li>\n</ul>',0,9),
	(7,'<div class=\"Q6-z\"><a class=\"oo-z\" title=\"LAUREN Ralph Lauren\" href=\"https://www.zappos.com/b/lauren-ralph-lauren/brand/358\" data-track-action=\"Product-Page\" data-track-label=\"Tabs\" data-track-value=\"Brand-Logo\"><img class=\"fk-z gk-z\" src=\"https://www.zappos.com/boutiques/3178/lauren-ralph-lauren_head_060915.jpg\" alt=\"LAUREN Ralph Lauren\" /></a></div>\n<ul>\n<li class=\"J6-z\">Have a romantic appeal wearing the cute and stylish LAUREN Ralph Lauren&reg; Ruffle-Trim Chiffon Dress.</li>\n<li>Surplice neckline with flutter sleeves.</li>\n<li>Threaded belt loops.</li>\n<li>Comes with a buckled belt.</li>\n<li>Cascading ruffles down the skirt.</li>\n<li>Threaded belt loops.</li>\n<li>Fully lined except for the sleeves.</li>\n<li>Ruffled hemline.</li>\n<li>Zippered with hook-and-eye closure.</li>\n<li>100% polyester.</li>\n<li>Machine wash, tumble dry.</li>\n<li>Imported.</li>\n<li>Measurements:\n<ul>\n<li>Length: 39 in</li>\n</ul>\n</li>\n<li>Length: 39 in</li>\n<li>Product measurements were taken using size 4. Please note that measurements may vary by size.</li>\n<li>Measurements:<br />Length: 39 in.</li>\n</ul>',0,10),
	(8,'<div class=\"Q6-z\"><a class=\"oo-z\" title=\"LAUREN Ralph Lauren\" href=\"https://www.zappos.com/e/ralph-lauren\" data-track-action=\"Product-Page\" data-track-label=\"Tabs\" data-track-value=\"Brand-Logo\"><img class=\"fk-z gk-z\" src=\"https://www.zappos.com/boutiques/3178/lauren-ralph-lauren_head_060915.jpg\" alt=\"LAUREN Ralph Lauren\" /></a></div>\n<ul>\n<li class=\"J6-z\">LAUREN Ralph Lauren&reg; Cotton Eyelet Midi Dress is s classic pick to wear on any casual day!</li>\n<li>V-surplice neckline.</li>\n<li>Short flowy sleeves.</li>\n<li>Tie-waist closure.</li>\n<li>Eyelets pattern allover.</li>\n<li>Falls below the knees.</li>\n<li>100% cotton.</li>\n<li>Hand wash.</li>\n<li>Imported.</li>\n</ul>',0,11),
	(9,'<div class=\"Q6-z\"><a class=\"oo-z\" title=\"LAUREN Ralph Lauren\" href=\"https://www.zappos.com/b/lauren-ralph-lauren/brand/358\" data-track-action=\"Product-Page\" data-track-label=\"Tabs\" data-track-value=\"Brand-Logo\"><img class=\"fk-z gk-z\" src=\"https://www.zappos.com/boutiques/3178/lauren-ralph-lauren_head_060915.jpg\" alt=\"LAUREN Ralph Lauren\" /></a></div>\n<ul>\n<li class=\"J6-z\">LAUREN Ralph Lauren&reg; Cotton Eyelet Midi Dress is s classic pick to wear on any casual day!</li>\n<li>V-surplice neckline.</li>\n<li>Short flowy sleeves.</li>\n<li>Tie-waist closure.</li>\n<li>Eyelets pattern allover.</li>\n<li>Falls below the knees.</li>\n<li>100% cotton.</li>\n<li>Hand wash.</li>\n<li>Imported.</li>\n</ul>',0,12),
	(10,'<p><img class=\"wscnph\" src=\"crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png\" /></p>',0,14),
	(11,'<div class=\"spec-card-container  \">\n<div class=\"spec-card-title\">Product Description</div>\n<div class=\"spec-card-content\">\n<div>\n<div>\n<ul>\n<li>Place of Origin: Guangdong, China</li>\n<li>Warranty: 12 Months</li>\n<li>Output Power: 15W</li>\n<li>Multi-function integrated: with Magnets</li>\n<li>Use: Smart Watch, Earphone, mobile phone</li>\n<li>Size: 247*74.5*8.5mm</li>\n<li>Charging distance: 0-8mm</li>\n<li>Material: ABS+Silicone</li>\n<li>Model Number: AD-WC15</li>\n<li>Weight: 105g</li>\n<li>MOQ: 1 Pcs</li>\n<li>Brand Name:Quantum Preferred&nbsp;</li>\n<li>Input: 9V/2A, 9V/3A, 9v-2a</li>\n<li>Product name: Magnetic 3 in 1 Wireless Charger</li>\n<li>Output: 9V/1.67A, 9V/1.2A, 5V/1A, 5W/7.5W/10W/15W</li>\n<li>Private Mold: Yes</li>\n<li>Charging Efficiency: 80%</li>\n</ul>\n</div>\n<div><img src=\"//img30.360buyimg.com/pop/s750x1449_jfs/t1/129778/10/23784/763583/62a41301E6882649b/ed0d7c750c92cd6a.jpg\" /><img src=\"//img13.360buyimg.com/pop/s750x1449_jfs/t1/133841/5/23348/545999/62a41302Eef249f96/220c01c2c73562af.jpg\" /><img src=\"//img30.360buyimg.com/pop/s750x1449_jfs/t1/196450/23/25213/816760/62a41304E692b25ca/7b2cead63696b2ee.jpg\" /><img src=\"//img10.360buyimg.com/pop/s750x1449_jfs/t1/207218/27/20825/790354/62a4130cEcd25a789/851679ea77117666.jpg\" /><img src=\"//img30.360buyimg.com/pop/s750x1449_jfs/t1/115206/35/27264/527454/62a41311E8ca6ab4d/be1d12e6dcdf1ffe.jpg\" /><img src=\"//img11.360buyimg.com/pop/s750x1449_jfs/t1/183784/31/25528/322747/62a41313Ee73dc0d1/42f93ab2d93f4dd1.jpg\" /><img src=\"//img20.360buyimg.com/pop/s750x879_jfs/t1/167694/16/24802/489152/62a41318E00b68353/d12e6e3814aa4803.jpg\" /></div>\n</div>\n</div>\n</div>',0,15),
	(12,'<div class=\"spec-card-content\"><img src=\"//img10.360buyimg.com/pop/jfs/t1/202254/35/23285/283366/628c8e50E7b2ff5ed/b1b8781d367c70a8.jpg\" alt=\"\" width=\"800\" height=\"800\" /></div>',0,16),
	(13,'<div class=\"spec-card-content\"><img src=\"//img10.360buyimg.com/pop/jfs/t1/51487/12/17798/492583/6284664bE789d11a1/cad977be45cfed08.jpg\" alt=\"\" width=\"750\" height=\"1063\" /><br />\n<table border=\"1\">\n<tbody>\n<tr>\n<td>Product Name</td>\n<td>Serving Tray</td>\n</tr>\n<tr>\n<td>Material</td>\n<td>Black Walnut Wood</td>\n</tr>\n<tr>\n<td>Size</td>\n<td>60x17x2.5cm</td>\n</tr>\n<tr>\n<td>Color</td>\n<td>Natural</td>\n</tr>\n<tr>\n<td>Logo</td>\n<td>Custom</td>\n</tr>\n</tbody>\n</table>\n<img src=\"//img10.360buyimg.com/pop/jfs/t1/213589/4/18565/585286/628466e7Ee4165892/def7eae12b0ce19e.jpg\" alt=\"\" width=\"750\" height=\"750\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/108003/17/29412/640352/628467f4E88e0f9ff/b38b306c852900b7.jpg\" alt=\"\" width=\"750\" height=\"750\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/212394/29/19006/488903/62846800Edfbc90fd/93b44332d73b21b3.jpg\" alt=\"\" width=\"750\" height=\"750\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/194970/25/24228/462999/62846814E02a4728d/a50d3692e971baa6.jpg\" alt=\"\" width=\"750\" height=\"750\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/128355/15/21985/170266/62846880Efab3792b/8be0be2668894f93.jpg\" alt=\"\" width=\"750\" height=\"750\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/217878/40/18546/417167/62846890E4f1845f0/773143db13b1aab2.jpg\" alt=\"\" width=\"750\" height=\"1704\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/20924/10/16012/847591/62846898E1a1e7b8a/f8624843510ed32d.jpg\" alt=\"\" width=\"750\" height=\"2238\" /><img src=\"//img10.360buyimg.com/pop/jfs/t1/85350/6/28964/730450/6284689fEa0440ff4/0325416548050102.jpg\" alt=\"\" width=\"750\" height=\"1930\" />Q1. What is your terms of packing? A: Generally, we pack our goods in inner box and brown cartons. If you have legally registered patent, we can pack the goods in your branded boxes after getting your authorization letters. Q2. What is your terms of payment? A: T/T 30% as deposit, and 70% before delivery. Well show you the photos of the products and packages before you pay the balance. Q3. What is your terms of delivery? A: EXW, FOB, CFR, CIF,DDU Q4. How about your delivery time? A: Generally, it will take 45 to 75 days after receiving your advance payment. The specific delivery time depends on the items and the quantity of your order. Q5. Can you produce according to the samples? A: Yes, we can produce by your samples or technical drawings. We can build the molds and fixtures. Q6. What is your sample policy? A: We can supply the sample if we have ready parts in stock, but the customers have to pay the sample cost and the courier cost. Q7: How do you make our business long-term and good relationship? A:1. We keep good quality and competitive price to ensure our customers benefit ; 2. We respect every customer as our friend and we sincerely do business and make friends with them, no matter where they come from.</div>',0,17),
	(14,'<div class=\"spec-card-content\">\n<div>\n<ul>\n<li>pattern:solid color</li>\n<li>Product Category:Bowl</li>\n<li>color:red, green, blue, grey</li>\n<li>Material:porcelain</li>\n<li>Custom processing:Yes</li>\n<li>Item number:DF1052</li>\n<li>Micro-wave oven:available</li>\n<li>Applicable scene:hotel, meals, coffee, tea</li>\n<li>Surface Technology:overglaze</li>\n</ul>\n<img src=\"//img20.360buyimg.com/pop/jfs/t1/211742/11/19132/500566/62898dbeEc6c83ad6/d177dc1f2efab36b.jpg\" /></div>\n<div><img src=\"//img20.360buyimg.com/pop/jfs/t1/119965/6/21988/318967/62898dbfE5d93a8d3/c666473d2092077a.jpg\" /> <img src=\"//img30.360buyimg.com/pop/jfs/t1/180051/33/25134/355097/62898dc0Eca63abf4/dcc9091dc2596497.jpg\" /> <img src=\"//img14.360buyimg.com/pop/jfs/t1/56601/18/18438/330012/62898dbfE440aaad3/601593ce8996b1e0.jpg\" /> <img src=\"//img11.360buyimg.com/pop/jfs/t1/116071/14/26085/456507/62898dbeE51b91817/41e726017424c250.jpg\" /></div>\n</div>',0,18);

/*!40000 ALTER TABLE `eb_store_product_description` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_product_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_log`;

CREATE TABLE `eb_store_product_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `type` varchar(10) NOT NULL COMMENT '类型visit,cart,order,pay,collect,refund',
  `product_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品ID',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `visit_num` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否浏览',
  `cart_num` int(11) NOT NULL DEFAULT '0' COMMENT '加入购物车数量',
  `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '下单数量',
  `pay_num` int(11) NOT NULL DEFAULT '0' COMMENT '支付数量',
  `pay_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `cost_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品成本价',
  `pay_uid` int(11) NOT NULL DEFAULT '0' COMMENT '支付用户ID',
  `refund_num` int(11) NOT NULL DEFAULT '0' COMMENT '退款数量',
  `refund_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `collect_num` tinyint(1) NOT NULL DEFAULT '0' COMMENT '收藏',
  `add_time` bigint(14) NOT NULL DEFAULT '0' COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品日志表';



# Dump of table eb_store_product_relation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_relation`;

CREATE TABLE `eb_store_product_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` int(10) unsigned NOT NULL COMMENT '用户ID',
  `product_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `type` varchar(32) NOT NULL COMMENT '类型(收藏(collect）、点赞(like))',
  `category` varchar(32) NOT NULL COMMENT '某种类型的商品(普通商品、秒杀商品)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uid` (`uid`,`product_id`,`type`,`category`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `category` (`category`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品点赞和收藏表';



# Dump of table eb_store_product_reply
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_reply`;

CREATE TABLE `eb_store_product_reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '店铺id',
  `order_no` varchar(32) NOT NULL DEFAULT '' COMMENT '商户订单编号',
  `order_info_id` int(11) NOT NULL DEFAULT '0' COMMENT '订单详情id',
  `product_id` int(11) NOT NULL COMMENT '商品id',
  `attr_value_id` int(11) NOT NULL COMMENT '商品规格属性id',
  `sku` varchar(128) NOT NULL DEFAULT '' COMMENT '商品规格属性值,多个,号隔开',
  `star` tinyint(1) NOT NULL DEFAULT '5' COMMENT '星级',
  `comment` varchar(512) NOT NULL DEFAULT '' COMMENT '评论内容',
  `pics` text NOT NULL COMMENT '评论图片',
  `merchant_reply_content` varchar(300) DEFAULT '' COMMENT '管理员回复内容',
  `merchant_reply_time` timestamp NULL DEFAULT NULL COMMENT '管理员回复时间',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0未删除1已删除',
  `is_reply` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未回复1已回复',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名称',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '用户头像',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `order_no` (`order_no`) USING BTREE,
  KEY `order_info_id` (`order_info_id`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE,
  KEY `star` (`star`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='评论表';



# Dump of table eb_store_product_rule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_product_rule`;

CREATE TABLE `eb_store_product_rule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '店铺id，平台为0',
  `rule_name` varchar(32) NOT NULL COMMENT '规格名称',
  `rule_value` text NOT NULL COMMENT '规格值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品规则值(规格)表';

LOCK TABLES `eb_store_product_rule` WRITE;
/*!40000 ALTER TABLE `eb_store_product_rule` DISABLE KEYS */;

INSERT INTO `eb_store_product_rule` (`id`, `mer_id`, `rule_name`, `rule_value`)
VALUES
	(1,1,'连衣裙','[{\"value\":\"颜色\",\"detail\":[\"PINK BLACK\",\"BLUE BLACK\"],\"inputVisible\":false},{\"value\":\"尺码\",\"detail\":[\"2\",\"4\",\"6\",\"8\",\"10\"],\"inputVisible\":false}]'),
	(2,1,'尺码','[{\"value\":\"尺码\",\"detail\":[\"2\",\"4\",\"6\",\"8\",\"10\"],\"inputVisible\":false}]');

/*!40000 ALTER TABLE `eb_store_product_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_store_refund_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_store_refund_order`;

CREATE TABLE `eb_store_refund_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `refund_order_no` varchar(32) NOT NULL COMMENT '退款订单号',
  `mer_order_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `master_order_no` varchar(32) NOT NULL COMMENT '主订单号',
  `mer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商户ID',
  `uid` int(11) unsigned NOT NULL COMMENT '用户id',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `real_name` varchar(32) NOT NULL COMMENT '收货人姓名',
  `user_phone` varchar(18) NOT NULL COMMENT '收货人电话',
  `user_address` varchar(100) NOT NULL COMMENT '收货人详细地址',
  `total_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单商品总数',
  `refund_reason_wap` varchar(255) NOT NULL DEFAULT '' COMMENT '退款原因',
  `refund_reason_wap_img` varchar(5000) NOT NULL DEFAULT '' COMMENT '退款图片',
  `refund_reason_wap_explain` varchar(255) NOT NULL DEFAULT '' COMMENT '退款用户说明',
  `refund_status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '退款状态：0:待审核 1:审核未通过 2：退款中 3:已退款',
  `refund_reason` varchar(255) NOT NULL DEFAULT '' COMMENT '拒绝退款说明',
  `refund_price` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `mer_remark` varchar(512) NOT NULL DEFAULT '' COMMENT '商户备注',
  `platform_remark` varchar(512) NOT NULL DEFAULT '' COMMENT '平台备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_order_no` (`mer_order_no`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商户退款订单表';



# Dump of table eb_stripe_callback
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_stripe_callback`;

CREATE TABLE `eb_stripe_callback` (
  `id` varchar(50) NOT NULL DEFAULT '' COMMENT 'ID',
  `type` varchar(255) DEFAULT NULL COMMENT '事件类型',
  `data_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'ID',
  `data` text COMMENT '内容',
  `created` bigint(50) DEFAULT NULL COMMENT '创建日期',
  `response_status` int(10) DEFAULT NULL COMMENT '响应状态',
  `response_des` varchar(255) DEFAULT NULL COMMENT '响应描述',
  `request` text COMMENT '全部内容',
  `add_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='Stripe回调表';



# 转储表 eb_system_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_admin`;

CREATE TABLE `eb_system_admin` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '后台管理员表ID',
  `account` varchar(32) NOT NULL COMMENT '后台管理员账号',
  `pwd` char(32) NOT NULL COMMENT '后台管理员密码',
  `real_name` varchar(16) NOT NULL COMMENT '后台管理员姓名',
  `roles` varchar(128) NOT NULL COMMENT '后台管理员权限(menus_id)',
  `last_ip` varchar(16) DEFAULT NULL COMMENT '后台管理员最后一次登录ip',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '后台管理员最后一次登录时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '后台管理员添加时间',
  `login_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '登录次数',
  `level` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '后台管理员级别',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '后台管理员状态 1有效0无效',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除：1-删除',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号码',
  `is_sms` tinyint(1) unsigned DEFAULT '0' COMMENT '是否接收短信',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '管理员类型：1= 平台超管, 2=商户超管, 3=系统管理员，4=商户管理员',
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '商户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `account` (`account`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='后台管理员表';

LOCK TABLES `eb_system_admin` WRITE;
/*!40000 ALTER TABLE `eb_system_admin` DISABLE KEYS */;

INSERT INTO `eb_system_admin` (`id`, `account`, `pwd`, `real_name`, `roles`, `last_ip`, `update_time`, `create_time`, `login_count`, `level`, `status`, `is_del`, `phone`, `is_sms`, `type`, `mer_id`)
VALUES
	(1,'admin','4NupqARy6N4=','超级管理员','1','127.0.0.1','2023-08-18 18:04:10','2021-07-16 09:59:12',1033,1,1,0,'18292417675',0,1,0),
	(3,'demo','7iIl3H5zCinwrYbrxAR7cQ==','演示账号','4','124.116.164.94','2023-07-27 17:31:16','2021-06-09 11:21:42',898,1,1,0,'18888888888',0,3,0),
	(21,'stivepeim@outlook.com','33HNiLCcUC0=','大粽子的杂货店','2','124.116.164.34','2023-08-18 18:03:04','2022-06-16 11:30:04',524,1,1,0,NULL,0,2,2);

/*!40000 ALTER TABLE `eb_system_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_attachment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_attachment`;

CREATE TABLE `eb_system_attachment` (
  `att_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '附件名称',
  `att_dir` varchar(200) NOT NULL DEFAULT '' COMMENT '附件路径',
  `satt_dir` varchar(200) DEFAULT NULL COMMENT '压缩图片路径',
  `att_size` char(30) NOT NULL DEFAULT '' COMMENT '附件大小',
  `att_type` char(30) NOT NULL DEFAULT '' COMMENT '附件类型',
  `pid` int(10) NOT NULL DEFAULT '0' COMMENT '分类ID0编辑器,1商品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图， 7前台用户',
  `image_type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '图片上传类型 1本地 2七牛云 3OSS 4COS ',
  `owner` int(11) DEFAULT '-1' COMMENT '资源归属方',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`att_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='附件管理表';

LOCK TABLES `eb_system_attachment` WRITE;
/*!40000 ALTER TABLE `eb_system_attachment` DISABLE KEYS */;

INSERT INTO `eb_system_attachment` (`att_id`, `name`, `att_dir`, `satt_dir`, `att_size`, `att_type`, `pid`, `image_type`, `owner`, `create_time`, `update_time`)
VALUES
	(1,'宝姿logo.jpeg','','crmebimage/public/maintain/2022/06/17/13721c6ae14a4880b25474715bb773b0edtz6cw85a.jpeg','8386','jpeg',845,1,1,'2022-06-17 17:26:07','2022-06-17 17:26:07'),
	(2,'宝姿背景图.jpeg','','crmebimage/public/maintain/2022/06/17/5aadeafee0514afa9362faa76775aa5cnkdv0yzp85.jpeg','23025','jpeg',845,1,1,'2022-06-17 17:26:07','2022-06-17 17:26:07'),
	(3,'宝姿店铺街.gif','','crmebimage/public/maintain/2022/06/17/42a94a4554214224a4de137f947635c0uamnmrkjpu.gif','570439','gif',845,1,1,'2022-06-17 17:26:14','2022-06-17 17:26:14'),
	(4,'蓝1.png','','crmebimage/public/maintain/2022/06/17/6adadd9603b84ce2bbde6e116d7081f386qlbtjn1q.png','838756','png',846,1,1,'2022-06-17 17:36:56','2022-06-17 17:36:56'),
	(5,'黄1.png','','crmebimage/public/maintain/2022/06/17/7bcc601a43e34a2683ef5f9184ab339e29ft5yscwd.png','872674','png',846,1,1,'2022-06-17 17:36:57','2022-06-17 17:36:57'),
	(6,'b0f452de858d5db9.jpg','','crmebimage/public/maintain/2022/06/17/d9f904ddbce341ba9cdb3d0827feef5fswhz8w8oef.jpg','166107','jpeg',846,1,1,'2022-06-17 17:37:33','2022-06-17 17:37:33'),
	(7,'735ca47a0087bb94.jpg','','crmebimage/public/maintain/2022/06/17/019ff26087b2487f8e20fbe6c131e9f86my2w8ip31.jpg','232406','jpeg',846,1,1,'2022-06-17 17:37:35','2022-06-17 17:37:35'),
	(8,'45628790d443dcd0.jpg','','crmebimage/public/maintain/2022/06/17/16951358e91648039cf36fc17c6d5cecpk6v5ags1f.jpg','287016','jpeg',846,1,1,'2022-06-17 17:37:35','2022-06-17 17:37:35'),
	(9,'1c7cac73264f7456.jpg','','crmebimage/public/maintain/2022/06/17/9d9ad2f85b304fffb3229865f55d01adlya8znoj0g.jpg','209787','jpeg',846,1,1,'2022-06-17 17:37:47','2022-06-17 17:37:47'),
	(10,'2ee446f47246ba1a.jpg','','crmebimage/public/maintain/2022/06/17/5b4c7b8ef24d41b494480675a915a2e2hyo0trk8et.jpg','110621','jpeg',846,1,1,'2022-06-17 17:38:15','2022-06-17 17:38:15'),
	(11,'eff82762b98dee8e.jpg','','crmebimage/public/maintain/2022/06/17/a7eeb7a9d48d4b219f4dc1eeb804eb32uywxotwv5r.jpg','336882','jpeg',846,1,1,'2022-06-17 17:38:29','2022-06-17 17:38:29'),
	(12,'b919d66f0d0064da.jpg','','crmebimage/public/maintain/2022/06/17/cb231b389660489e961db554b9f686c4fceg7unng6.jpg','393864','jpeg',846,1,1,'2022-06-17 17:38:31','2022-06-17 17:38:31'),
	(13,'475d6f37e1266f74.jpg','','crmebimage/public/maintain/2022/06/17/3dc9a7972a6b4a93bacb949f60aeaa924n7z4b7ehi.jpg','447985','jpeg',846,1,1,'2022-06-17 17:38:33','2022-06-17 17:38:33'),
	(14,'6b246e982738819f.jpg','','crmebimage/public/maintain/2022/06/17/c5de48d783c840fdb08be567974c781aoiv8w9cmpy.jpg','501116','jpeg',846,1,1,'2022-06-17 17:38:34','2022-06-17 17:38:34'),
	(15,'ea46e927714e7776.jpg','','crmebimage/public/maintain/2022/06/17/5619ef4031434955aba0af0a4fa3037coznklnzk1k.jpg','1021275','jpeg',846,1,1,'2022-06-17 17:38:41','2022-06-17 17:38:41'),
	(16,'275e60789951438c9cac5928bd2b0ec33brze97mkn.png','','crmebimage/public/product/2022/06/17/9b6f9eeb611d46019fc75d50d22d14f8qo1nybufk7.png','3428','png',768,1,-1,'2022-06-17 18:08:19','2022-06-17 18:08:19'),
	(17,'03d6b5c9c108421183e5d602271ee40fq02myezxqa.png','','crmebimage/public/product/2022/06/17/ee50e78ebb9344dd9ecafd94e45e61660ugjjm32z5.png','117604','png',0,1,-1,'2022-06-17 18:08:51','2022-06-17 18:08:51'),
	(18,'398c165115c247ba8ae79fc5cade8226tpf3ap2and.png','','crmebimage/public/product/2022/06/17/5b29ba0909b6431abd4a741611a6d9a11eoxhe1g0v.png','24056','png',0,1,-1,'2022-06-17 18:09:54','2022-06-17 18:09:54'),
	(19,'834580d86f1c43529247fedf3c4304a51twgcts5bs.png','','crmebimage/public/product/2022/06/17/818293a5b095496287dbcf59a18b0b9e1ozzowfx5a.png','9371','png',0,1,-1,'2022-06-17 18:10:22','2022-06-17 18:10:22'),
	(20,'aa6e46fd9fbd424e8cb636a34db50176nj0e4r0f6o.png','','crmebimage/public/product/2022/06/17/474a2d745b094151b7d9d46ed692ac9ek3970ns7g4.png','2081','png',0,1,-1,'2022-06-17 18:13:55','2022-06-17 18:13:55'),
	(21,'log.jpg','','crmebimage/public/operation/2022/06/18/e1cae981a71f4d05a123cdb7b4c96b5cjcnx2hirt4.jpg','6100','jpeg',0,1,3,'2022-06-18 14:41:02','2022-06-18 14:41:02'),
	(22,'5b18f64d5a634065a4ee0303ed9ed205a0vkffpwth.jpg','','crmebimage/public/product/2022/06/18/657314edfc124a75a2788f8dbc32c396xx77jduaq7.jpg','26852','jpeg',0,1,-1,'2022-06-18 14:49:28','2022-06-18 14:49:28'),
	(23,'16pic_1598639_s.png','','crmebimage/public/operation/2022/06/18/de4d04af754c4e8fa1fe037953f20c3282y0lpkzc4.png','28016','png',0,1,3,'2022-06-18 14:49:34','2022-06-18 14:49:34'),
	(24,'index.jpg','','crmebimage/public/operation/2022/06/18/3f58cff369044bd6b54467bd7a9d5f1e6ymte67t67.jpg','157686','jpeg',0,1,3,'2022-06-18 14:49:43','2022-06-18 14:49:43'),
	(25,'l.png','','crmebimage/public/content/2022/06/18/ec7d5ac111b74237b61cd080f13c53ee1igvpmmg6f.png','23348','png',0,1,3,'2022-06-18 14:57:03','2022-06-18 14:57:03'),
	(26,'微信截图_20220618145715.png','','crmebimage/public/product/2022/06/18/1dc61934d1de4dfb9e5434bb4262297cryuyctycp3.png','151343','png',834,1,-1,'2022-06-18 14:57:28','2022-06-18 14:59:33'),
	(27,'banner.png','','crmebimage/public/product/2022/06/18/8dced0fe59a04e0db43e83148571c97a1rzfulbszq.png','718029','png',836,1,-1,'2022-06-18 14:59:03','2022-06-18 14:59:03'),
	(28,'75c6c43754710d46.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/e0a3c8acfa4b41738c89d4ed0be49893p9cz0gwztk.webp','33404','webp',0,1,3,'2022-06-18 14:59:07','2022-06-18 14:59:07'),
	(29,'3e9bd3b2eb2e4d8f.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/0e58aaeeb22e42ac8d9cc0ad30f9429606zzfas221.webp','26644','webp',0,1,3,'2022-06-18 15:00:29','2022-06-18 15:00:29'),
	(30,'1ff32a5a4d675317.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/1be4f1eafc8744f1ac132d63f768ca0c0mptyoppy9.webp','9150','webp',0,1,3,'2022-06-18 15:00:34','2022-06-18 15:00:34'),
	(31,'75c6c43754710d46.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/0ce2e555b05d4a559074dc902e8e93d8rlab4sz4nf.webp','33404','webp',0,1,3,'2022-06-18 15:00:41','2022-06-18 15:00:41'),
	(32,'3e9bd3b2eb2e4d8f.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/a1fd91854bde4d70aa5b1583544712f4zyfc4y5q49.webp','26644','webp',0,1,3,'2022-06-18 15:00:46','2022-06-18 15:00:46'),
	(33,'9af3107b79fb6e1c.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/bbdb0a95045949f48eb71b3ce2031642txeoaqagg5.webp','37650','webp',782,1,3,'2022-06-18 15:01:05','2022-06-18 15:01:05'),
	(34,'1ff32a5a4d675317.jpg!q70.dpg.webp','','crmebimage/public/content/2022/06/18/259274f2326e43c4bb13f1285bbdf5716jpuaxwise.webp','9150','webp',0,1,3,'2022-06-18 15:01:20','2022-06-18 15:01:20'),
	(35,'1ff32a5a4d675317.jpg','','crmebimage/public/content/2022/06/18/53fb1d2323634291ad699c8d3abbfcb4hthhqrj5s4.jpg','9150','jpeg',0,1,3,'2022-06-18 15:01:44','2022-06-18 15:01:44'),
	(36,'75c6c43754710d46.jpg','','crmebimage/public/content/2022/06/18/5d77a6e0346744faad9cf5cad8c86294rf94ydusrf.jpg','33404','jpeg',0,1,3,'2022-06-18 15:02:23','2022-06-18 15:02:23'),
	(37,'9af3107b79fb6e1c.jpg','','crmebimage/public/content/2022/06/18/47b779fb10424c1d8b4dfc415386cc24go3tsrof3z.jpg','37650','jpeg',0,1,3,'2022-06-18 15:02:29','2022-06-18 15:02:29'),
	(38,'3e9bd3b2eb2e4d8f.jpg','','crmebimage/public/content/2022/06/18/1497de9478b14f4e99b52b7f089827963a7jbfep86.jpg','26644','jpeg',0,1,3,'2022-06-18 15:02:33','2022-06-18 15:02:33'),
	(39,'3c36f96885b3487296a7d677564dcecd3cd641031f.png','','crmebimage/public/product/2022/06/18/3e88a5e85d674e1f8d8283c96c7dfc6a1wajapbutf.png','58030','png',0,1,-1,'2022-06-18 15:03:07','2022-06-18 15:03:07'),
	(40,'0016d1efa86d41e38e76801a7cd743c6wcupy21hvb.jpg','','crmebimage/public/product/2022/06/18/2b813c5b0a614694ab2b1bba017d6c4cpb909hu07o.jpg','71811','jpeg',0,1,-1,'2022-06-18 15:04:44','2022-06-18 15:04:44'),
	(41,'banner.png','','crmebimage/public/product/2022/06/18/21f102999ad04afab46ef8ded3dff5cdtq5fdrdyrs.png','749060','png',0,1,-1,'2022-06-18 15:07:09','2022-06-18 15:07:09'),
	(42,'fdc708ec25ad489cae2263cea8d4627alttl1qb8g2.gif','','crmebimage/public/product/2022/06/18/b5eea8de3dc844ca80c47fd44171e23dg52k1el1ze.gif','305932','gif',0,1,-1,'2022-06-18 15:09:31','2022-06-18 15:09:31'),
	(43,'06546df138f543a4b9cf3d98eca3c474ggiz9or1f7.jpg','','crmebimage/public/product/2022/06/18/2ba89f51e47f40279081815258ca4a4dpy5w3x8v51.jpg','84121','jpeg',0,1,-1,'2022-06-18 15:09:36','2022-06-18 15:09:36'),
	(44,'l.png','','crmebimage/public/store/2022/06/18/b153883e27dc47c9a13b8fa77f50b8a8ytr7zhk933.png','23348','png',0,1,-1,'2022-06-18 15:10:34','2022-06-18 15:10:34'),
	(45,'119d4de19a49470d90c6c24fef1b91d8iw2c48cwge.png','','crmebimage/public/product/2022/06/18/17dfd6211f5f4bc09631e9990299f96818527sqfe2.png','4206','png',0,1,-1,'2022-06-18 15:27:21','2022-06-18 15:27:21'),
	(46,'4d2f77391edc4519bd19062b36363b0403ryp7gjjo.png','','crmebimage/public/product/2022/06/18/89674707e3834933b4d4b623fea09ed1jjiea34l4l.png','3214','png',0,1,-1,'2022-06-18 15:27:21','2022-06-18 15:27:21'),
	(47,'4ea95924af7d4ecab386419add93effcdchkzdwio9.png','','crmebimage/public/product/2022/06/18/865c4d1385534806b99db13766bb0977sgyi097jso.png','4256','png',0,1,-1,'2022-06-18 15:27:21','2022-06-18 15:27:21'),
	(48,'14f3f8b5d5fe4181b85c7eceeab6f8feapfgk7v6ci.png','','crmebimage/public/product/2022/06/18/5687d37941fa467a9b474a46b150e3f80bq8cnzyal.png','4050','png',0,1,-1,'2022-06-18 15:27:21','2022-06-18 15:27:21'),
	(49,'d4c7282740fb4e71bd2d7e80fad985d8o1wfu26pyd.jpg','','crmebimage/public/product/2022/06/18/df6214011a2f4683b1b5feab362f0d882pneycevbh.jpg','20103','jpeg',0,1,-1,'2022-06-18 15:32:10','2022-06-18 15:32:10'),
	(50,'9d95f61a5db84591972fbc90f31c1fb21g5krqax8i.jpg','','crmebimage/public/product/2022/06/18/7caee6176c4f44e4baea64206c59f6274ywzmc143e.jpg','70122','jpeg',0,1,-1,'2022-06-18 15:32:10','2022-06-18 15:32:10'),
	(51,'886e04146694474c966d346222fe2897ealwo6qycj.png','','crmebimage/public/product/2022/06/18/a1a67a5c7cd74f44959f2bba2a9492e63bznmbr6zw.png','2053','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(52,'3308f2d779874079b0e02ae206cf141bwb23ii2z52.png','','crmebimage/public/product/2022/06/18/477ed879ac184774b41f296db9bcc6d0uc9cw3qcjv.png','1781','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(53,'ed96d273687c47c09e7587a93394312f622fr1rw4k.jpg','','crmebimage/public/product/2022/06/18/1d4ca008abcd4470b98a4b1e2920f98b6k6s1os89z.jpg','10643','jpeg',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(54,'9b0fbab1464c482eaabdf90f7993eed1qnnwzv1xis.png','','crmebimage/public/product/2022/06/18/10de6d79d68d492eac7cd67af833baeczrhu8jcg82.png','26288','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(55,'11113e1876ac427bb3173086a3bd6c5bhyan1ea9pz.png','','crmebimage/public/product/2022/06/18/96e9ecdd289d405ca9f7df66f8d4c667qqn1r1zsph.png','1859','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(56,'dd497d333f4541d7ae41b2c579706370jhp2x39yrg.png','','crmebimage/public/product/2022/06/18/7d034a06a3404bf8b24ec82143e99017fpmlsx0om1.png','2308','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(57,'e86d9084677e4a50bce41dfe0d697dc706pxwdrl4h.png','','crmebimage/public/product/2022/06/18/d29434234e6d41b5bacfddab9bbf2a0d19vikgbtad.png','54912','png',720,1,-1,'2022-06-18 15:35:21','2022-06-18 15:35:21'),
	(58,'0eabacb2ab774f54bddd9b2b0ff212a49e7etvhouq.gif','','crmebimage/public/product/2022/06/18/8d3e31140c6d4f9683edbd2d333e462fl3td6skp2w.gif','350370','gif',847,1,-1,'2022-06-18 15:39:01','2022-06-18 15:39:01'),
	(59,'9deb872da95c42d2bf8c432075153b1egrrxlb666p.gif','','crmebimage/public/product/2022/06/18/895816ec51c14563b679f02d729d210b2tdhub0hsb.gif','410412','gif',847,1,-1,'2022-06-18 15:40:00','2022-06-18 15:40:00'),
	(60,'ca4e301ed76a44f8aa23e70b7df107b8v26l2p79u3.gif','','crmebimage/public/product/2022/06/18/ff8278c17aef444db2556eec57d602a6p8l777730y.gif','360926','gif',847,1,-1,'2022-06-18 15:40:33','2022-06-18 15:40:33'),
	(61,'89180960b858402eb15708fac8c27b84ihc3v7h00i.jpg','','crmebimage/public/product/2022/06/18/b1475f2ded8a4e23adb74d5cb3e08544k4rtv1sef4.jpg','22958','jpeg',0,1,-1,'2022-06-18 15:42:12','2022-06-18 15:42:12'),
	(62,'4085d05796bb49d3b0e1c3b2b766bb4adrqz6xyn36.jpg','','crmebimage/public/product/2022/06/18/b29d82deb7c443ca88795c3d341b6887h43udf7n4v.jpg','78676','jpeg',0,1,-1,'2022-06-18 15:42:12','2022-06-18 15:42:12'),
	(63,'d087ebc5d3d2454a9a9663297eced081ggjlqza78h.jpg','','crmebimage/public/product/2022/06/18/d11be7f78a014558924a31874a50617at9bfdsqy0v.jpg','130739','jpeg',0,1,-1,'2022-06-18 15:42:12','2022-06-18 15:42:12'),
	(64,'dd67587544934f47beb4557427446de0ieuhyrupv1.jpg','','crmebimage/public/product/2022/06/18/4248cfea71c040c1bf5a3a6a75f8c6381hujr27fou.jpg','77157','jpeg',0,1,-1,'2022-06-18 15:42:12','2022-06-18 15:42:12'),
	(65,'60e1d297eb5a49bca13b92e2b0b411c2siripyrcu0.jpg','','crmebimage/public/product/2022/06/18/825310b0a5ad4abf9a8484c616e992c9m4jdeyzrz6.jpg','26376','jpeg',0,1,-1,'2022-06-18 15:47:02','2022-06-18 15:47:02'),
	(66,'4bdd334e3b084908ae56b7ce1e576204a43i68txom.jpg','','crmebimage/public/product/2022/06/18/b2d29d1bf4394e24985ce5fa3770b2f2uqqytv1paq.jpg','19973','jpeg',0,1,-1,'2022-06-18 15:47:02','2022-06-18 15:47:02'),
	(67,'广告1.jpg','','crmebimage/public/product/2022/06/18/cd2eb6f08bd9431c927ac3f301398786jqtiawlmgt.jpg','43517','jpeg',848,1,-1,'2022-06-18 15:52:30','2022-06-18 15:52:30'),
	(68,'广告3.jpg','','crmebimage/public/product/2022/06/18/d84711061eff4fd2b4d1e17885756184u8dks7vz5l.jpg','49031','jpeg',848,1,-1,'2022-06-18 15:52:30','2022-06-18 15:52:30'),
	(69,'广告2.jpg','','crmebimage/public/product/2022/06/18/7629f5f69e7540619b9ead71d00cd50f0fa68ab9b3.jpg','48609','jpeg',848,1,-1,'2022-06-18 15:52:30','2022-06-18 15:52:30'),
	(70,'广告4.jpg','','crmebimage/public/product/2022/06/18/bc0d97725d1d4d8d8cb70b2518ad0ea9g4kyp53mks.jpg','43149','jpeg',848,1,-1,'2022-06-18 15:52:30','2022-06-18 15:52:30'),
	(71,'ab34f5848e8db8ca.jpg','','crmebimage/public/product/2022/06/18/5f4ab802d2e64c9d9f68cf165362dbe8y4z2012h4y.jpg','134288','jpeg',822,1,-1,'2022-06-18 16:07:05','2022-06-18 16:07:05'),
	(72,'7e157a90ede0bf2c.jpg','','crmebimage/public/product/2022/06/18/256625d2bf3a4c6092b4fe66764a2e28oxkm006stx.jpg','182777','jpeg',822,1,-1,'2022-06-18 16:07:05','2022-06-18 16:07:05'),
	(73,'f6c98f277d067a3a.jpg','','crmebimage/public/product/2022/06/18/49940e8a977f42f7b55e3925f9955da8340kd104n8.jpg','117343','jpeg',822,1,-1,'2022-06-18 16:07:05','2022-06-18 16:07:05'),
	(74,'bb6cf113329e78ef.jpg','','crmebimage/public/product/2022/06/18/460731c735d947849fe30ca4f38c3131waoa5ek011.jpg','317912','jpeg',822,1,-1,'2022-06-18 16:07:05','2022-06-18 16:07:05'),
	(75,'0aed9fbffe7cfb27.jpg','','crmebimage/public/product/2022/06/18/334e9846f761457d92a60292a3230065s15w09qylg.jpg','371905','jpeg',822,1,-1,'2022-06-18 16:07:06','2022-06-18 16:07:06'),
	(76,'8d82237a720ad4e7.jpg','','crmebimage/public/product/2022/06/18/86b4489ccdad406595bb57cece8424406plwrosd7r.jpg','532558','jpeg',822,1,-1,'2022-06-18 16:07:06','2022-06-18 16:07:06'),
	(77,'90eb47901031f791.jpg','','crmebimage/public/product/2022/06/18/1c7a193f424649a3820142575edf56b34jdaughsiw.jpg','151488','jpeg',821,1,-1,'2022-06-18 16:07:19','2022-06-18 16:07:19'),
	(78,'4a17f793aad9ab5f.jpg','','crmebimage/public/product/2022/06/18/23409732d30b4a3b9216f74e93028fe5b0w773wt6x.jpg','337828','jpeg',821,1,-1,'2022-06-18 16:07:20','2022-06-18 16:07:20'),
	(79,'3046e122f5716c86.png','','crmebimage/public/product/2022/06/18/89f34c76bff9437a822f4dbe55cc8906xjrr5dqdb8.png','125613','png',821,1,-1,'2022-06-18 16:07:20','2022-06-18 16:07:20'),
	(80,'f8961556975e9175.png','','crmebimage/public/product/2022/06/18/8eeb59b0137b41799b24d6319a10fbccrnp2v5ly1d.png','127496','png',821,1,-1,'2022-06-18 16:07:20','2022-06-18 16:07:20'),
	(81,'184260eac042419d.jpg','','crmebimage/public/product/2022/06/18/859bbb41c5864e4fbe324a4d4140e235qgogx5okod.jpg','8841','jpeg',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(82,'35a246bb921365a0.jpg','','crmebimage/public/product/2022/06/18/cf85ef8fa16344a982a2077fcf8fd8c9w96h34lbjn.jpg','45856','jpeg',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(83,'eb12b7cb6c562907.jpg','','crmebimage/public/product/2022/06/18/3a8e614e87404d49a465e45ceb1825b6gvlnkjmvg5.jpg','33391','jpeg',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(84,'39014225c119af08.jpg','','crmebimage/public/product/2022/06/18/0e40a97d8b0c47fbb9f2dd83274dd33db1c2kk71q7.jpg','89601','jpeg',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(85,'63a8068ab73678c5.png','','crmebimage/public/product/2022/06/18/0a6b0c874c654bcdbd7ce73824cf1c66mya2dqayw8.png','515923','png',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(86,'9ed961ad898d0dba.jpg','','crmebimage/public/product/2022/06/18/fa196dba82cd4320b8564d2c844d0352jye8vt97ts.jpg','284326','jpeg',823,1,-1,'2022-06-18 16:07:31','2022-06-18 16:07:31'),
	(87,'b0cd163878be33ac.jpg','','crmebimage/public/product/2022/06/18/e3be2b2e4b9443e4bda60a0d811367e5t1d4kft06j.jpg','58327','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(88,'92bee5dfde7cd666.jpg','','crmebimage/public/product/2022/06/18/a5fb7d2e4e1a46fdb3c273a901089507497u3lglzw.jpg','60121','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(89,'952b8054ad54139d.jpg','','crmebimage/public/product/2022/06/18/f9030eb4402c44fb9e02a457c1c8acaa5pn60u4gdw.jpg','74997','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(90,'a6ea1bfedef2d823.jpg','','crmebimage/public/product/2022/06/18/e4fd493d155a46aa8b7153b3cd94b1929gcmcuqdgk.jpg','199943','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(91,'a3b24f4deed51077.jpg','','crmebimage/public/product/2022/06/18/fd5aa018db724e27aecf690f8855ee3fickie90o1b.jpg','616444','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(92,'fc1e42cc5ca5a962.jpg','','crmebimage/public/product/2022/06/18/81dbb9b656964de2b0228afde1f35c040i67rpm6gb.jpg','110426','jpeg',824,1,-1,'2022-06-18 16:07:45','2022-06-18 16:07:45'),
	(93,'4e151dec527e526f.jpg','','crmebimage/public/product/2022/06/18/53e11bc8b1a44cdbb9adece922677eedwwys41k1ra.jpg','20614','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(94,'6832cd83f5190bb6.jpg','','crmebimage/public/product/2022/06/18/7311574d9e434fa594d64b891756199dea0i8mb1ha.jpg','15533','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(95,'8193c24189dd376e.jpg','','crmebimage/public/product/2022/06/18/a0bc21996af64d1ea1fcca907f026c35plk82xme1p.jpg','48804','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(96,'11a294df0979a7b2.jpg','','crmebimage/public/product/2022/06/18/30175ee94faa48f2bebb86f41e8addeabsyhjulds0.jpg','63686','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(97,'dfa2e727adbf87ac.jpg','','crmebimage/public/product/2022/06/18/d71995e6fd94437b94ba4900d28f917504oz5bjhpa.jpg','186326','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(98,'fceb91533f6cae90.jpg','','crmebimage/public/product/2022/06/18/71b8a93bdecf46cfa56f57e2246f4fb3usgiba3jdw.jpg','95081','jpeg',825,1,-1,'2022-06-18 16:07:54','2022-06-18 16:07:54'),
	(99,'82360d83e0203526.png','','crmebimage/public/product/2022/06/18/791b3949677845cbb726160a10f29185j4j6vtpg2f.png','156246','png',796,1,-1,'2022-06-18 16:09:12','2022-06-18 16:09:12'),
	(100,'926ee9647edb974a.png','','crmebimage/public/product/2022/06/18/afcdd40d4a114546a6d81a83f052c96f9ip0gpi5bb.png','75828','png',792,1,-1,'2022-06-18 16:10:39','2022-06-18 16:10:39'),
	(101,'cfc8bae0511bcd03.jpg','','crmebimage/public/product/2022/06/18/bfa72c7d523341619357109fdb8b4675fa7ewmr9vx.jpg','28514','jpeg',797,1,-1,'2022-06-18 16:10:47','2022-06-18 16:10:47'),
	(102,'79ef0af8b5f82683.jpg','','crmebimage/public/product/2022/06/18/1cdad132b5d346b0bae7fe3735aa945919z51nx8ww.jpg','92115','jpeg',797,1,-1,'2022-06-18 16:10:47','2022-06-18 16:10:47'),
	(103,'95c1b7cc40971780.jpg','','crmebimage/public/product/2022/06/18/3ee1b7aad92a401989b74132298ec2dbfl3lpy42y6.jpg','56217','jpeg',798,1,-1,'2022-06-18 16:10:56','2022-06-18 16:10:56'),
	(104,'bb0eb3e997454d10.png','','crmebimage/public/product/2022/06/18/d8b8a5570fbc49989004c5b0bc12023a6y8clk6kf5.png','148501','png',798,1,-1,'2022-06-18 16:10:56','2022-06-18 16:10:56'),
	(105,'761ee3eed488c9ba.jpg','','crmebimage/public/product/2022/06/18/ac075ca2f1824b62a3bd9ad0d4c08460sli4d2k9h8.jpg','106043','jpeg',799,1,-1,'2022-06-18 16:11:04','2022-06-18 16:11:04'),
	(106,'e66ce43ccaeb8b00.jpg','','crmebimage/public/product/2022/06/18/59802946a7bf4407aa0eb83556cc1e1ed785p6vw6x.jpg','291460','jpeg',799,1,-1,'2022-06-18 16:11:04','2022-06-18 16:11:04'),
	(107,'77f765482edada4b.png','','crmebimage/public/product/2022/06/18/bc80791a1b3f4b098a3778c4a842034akmc6wypfuk.png','158139','png',800,1,-1,'2022-06-18 16:11:15','2022-06-18 16:11:15'),
	(108,'9f9d87d659c7d333.png','','crmebimage/public/product/2022/06/18/941632f74af04a12b6d8d0d4f3476bdfm8xu90fa46.png','165930','png',800,1,-1,'2022-06-18 16:11:15','2022-06-18 16:11:15'),
	(109,'54e6b594e7f8596d.jpg','','crmebimage/public/product/2022/06/18/b67d01a813b347ae9372292e7d4eabebq1k95rrknm.jpg','59042','jpeg',801,1,-1,'2022-06-18 16:11:23','2022-06-18 16:11:23'),
	(110,'150c41f8cc1741f9.jpg','','crmebimage/public/product/2022/06/18/bcc2a779de1342909687509acbebad61ipvj7e0qes.jpg','343563','jpeg',801,1,-1,'2022-06-18 16:11:23','2022-06-18 16:11:23'),
	(111,'282e3977ab080b87.jpg','','crmebimage/public/product/2022/06/18/2d631cdad12340d5afd83e98bc8564005nyskwi5al.jpg','43796','jpeg',802,1,-1,'2022-06-18 16:11:31','2022-06-18 16:11:31'),
	(112,'1b3b6cc1c01c97dc.png','','crmebimage/public/product/2022/06/18/d3c4c04ee81b458db3c42ec767c98accf16vu6dz5r.png','132032','png',802,1,-1,'2022-06-18 16:11:31','2022-06-18 16:11:31'),
	(113,'d1b8ea16acd2fb7f.png','','crmebimage/public/product/2022/06/18/648679ab234e420eb84831d726d7b024wz62qrklwv.png','172080','png',793,1,-1,'2022-06-18 16:11:46','2022-06-18 16:11:46'),
	(114,'8bee10000860e3af.png','','crmebimage/public/product/2022/06/18/bfeb9289572344f7983906f455103e87v681pnl4n5.png','577460','png',803,1,-1,'2022-06-18 16:11:56','2022-06-18 16:11:56'),
	(115,'1dc7e29cd6f6cc74.png','','crmebimage/public/product/2022/06/18/d2e0212243ab47d5b6605f290fbcf8c1e1vtj2so5s.png','1005362','png',803,1,-1,'2022-06-18 16:11:56','2022-06-18 16:11:56'),
	(116,'b4d61d0502da90f2.png','','crmebimage/public/product/2022/06/18/b87f1fd44b764a3a8c2fbddb39a5fc11cm4e9h2utd.png','487970','png',804,1,-1,'2022-06-18 16:12:04','2022-06-18 16:12:04'),
	(117,'e2990588eeea1475.png','','crmebimage/public/product/2022/06/18/2fc30990cd80486190cdf44bcd5e53808h01jnnhzk.png','967601','png',804,1,-1,'2022-06-18 16:12:04','2022-06-18 16:12:04'),
	(118,'dadde91293bd1dfa.png','','crmebimage/public/product/2022/06/18/1675f8b4e6e045ca850e6928940449fc27xmbxxq5u.png','740308','png',805,1,-1,'2022-06-18 16:12:11','2022-06-18 16:12:11'),
	(119,'1ea8c289caef070c.png','','crmebimage/public/product/2022/06/18/5a1056744a2f4935b4d9db968b208cc3nsq9qt6syx.png','838238','png',805,1,-1,'2022-06-18 16:12:11','2022-06-18 16:12:11'),
	(120,'6bcb3e5f76fde6a1.png','','crmebimage/public/product/2022/06/18/e3d68662977346df8a9d10d2953bfdb02s3i1wpeh5.png','392667','png',806,1,-1,'2022-06-18 16:12:20','2022-06-18 16:12:20'),
	(121,'2ae4c605f53ce741.png','','crmebimage/public/product/2022/06/18/d29fb6cc07d94aa0af516075ccc3cca1b7wcnmr38d.png','574380','png',806,1,-1,'2022-06-18 16:12:20','2022-06-18 16:12:20'),
	(122,'8c2a39b78dd1d256.png','','crmebimage/public/product/2022/06/18/d0d07f3a6f4246c3ac106519b5118b96aksli1vamn.png','537836','png',807,1,-1,'2022-06-18 16:12:28','2022-06-18 16:12:28'),
	(123,'e43e7d444e8c47ba.png','','crmebimage/public/product/2022/06/18/dea767f91a574b7d963c693efe8a5b96tfvwzez0gt.png','1015068','png',807,1,-1,'2022-06-18 16:12:28','2022-06-18 16:12:28'),
	(124,'acdc46a608b76926.png','','crmebimage/public/product/2022/06/18/a31a093ce2804693b0fbaadef9d5527945a8t1m54h.png','212206','png',808,1,-1,'2022-06-18 16:12:35','2022-06-18 16:12:35'),
	(125,'4dc39ae253eb0cd5.png','','crmebimage/public/product/2022/06/18/664b4b33c955462b940c00d0d9ff776bxxvh0hmx6b.png','796368','png',808,1,-1,'2022-06-18 16:12:35','2022-06-18 16:12:35'),
	(126,'dbef7cb684a38bbd.png','','crmebimage/public/product/2022/06/18/392a8673e6064d519ff3e7ed572380866nlt52z8oy.png','73717','png',794,1,-1,'2022-06-18 16:12:45','2022-06-18 16:12:45'),
	(127,'5537f54e16111c5e.png','','crmebimage/public/product/2022/06/18/f2b3d7340ffa43348733805175f4569avbr0xw892q.png','526242','png',809,1,-1,'2022-06-18 16:13:25','2022-06-18 16:13:25'),
	(128,'c69cc7bd85797bd3.png','','crmebimage/public/product/2022/06/18/39bd35f345404f2e923ab9e1aaea55355fjx89v8go.png','298452','png',809,1,-1,'2022-06-18 16:13:25','2022-06-18 16:13:25'),
	(129,'4df871bab7e11986.png','','crmebimage/public/product/2022/06/18/4130adc7bbf249409222d3075952a1f4plk9tsdqvu.png','169314','png',810,1,-1,'2022-06-18 16:13:34','2022-06-18 16:13:34'),
	(130,'32654358c1c66995.png','','crmebimage/public/product/2022/06/18/6c48e6761825426d83c4315e139dcc58qdotn3725v.png','610908','png',810,1,-1,'2022-06-18 16:13:34','2022-06-18 16:13:34'),
	(131,'8a08e036ff6ab7bf.png','','crmebimage/public/product/2022/06/18/02172a55fb74426d803185375a97e04fhldonwcwwv.png','66068','png',811,1,-1,'2022-06-18 16:13:42','2022-06-18 16:13:42'),
	(132,'53886e3379e4f6f1.png','','crmebimage/public/product/2022/06/18/b2d497f822b545da99af8b8f761d8e96sdzercwxj3.png','500744','png',811,1,-1,'2022-06-18 16:13:42','2022-06-18 16:13:42'),
	(133,'877f9feceebe02d4.png','','crmebimage/public/product/2022/06/18/be452292b38245bc9b65decbaaf2aa53sthba80p26.png','187337','png',812,1,-1,'2022-06-18 16:13:50','2022-06-18 16:13:50'),
	(134,'cc30e78544c556cc.png','','crmebimage/public/product/2022/06/18/556bf5fd0fc348c99873b018d9053e0ee4wmp526hf.png','490030','png',812,1,-1,'2022-06-18 16:13:50','2022-06-18 16:13:50'),
	(135,'ea99d13be158332f.png','','crmebimage/public/product/2022/06/18/6c814b9b8c864263a39baab06173c741l2zl3u87gc.png','317985','png',813,1,-1,'2022-06-18 16:13:57','2022-06-18 16:13:57'),
	(136,'402a22c307c7516d.png','','crmebimage/public/product/2022/06/18/77bd47e6e1da4b1092c12e6efe6f947fr5wc98pn1z.png','428252','png',813,1,-1,'2022-06-18 16:13:57','2022-06-18 16:13:57'),
	(137,'fd9dcd4ece84dd12.png','','crmebimage/public/product/2022/06/18/d0eb461e6c7c43cb910fb9db89d37c3buk9j6bsscl.png','528986','png',814,1,-1,'2022-06-18 16:14:06','2022-06-18 16:14:06'),
	(138,'c129f2af7e35c2a4.png','','crmebimage/public/product/2022/06/18/f293b290922f4ccbab84d70f2880c914sty4qif2eg.png','440461','png',814,1,-1,'2022-06-18 16:14:06','2022-06-18 16:14:06'),
	(139,'0955b1a7cf43e6b7.png','','crmebimage/public/product/2022/06/18/59a74fa47497499e87f419fa2af4b48b6urr56beba.png','154134','png',795,1,-1,'2022-06-18 16:14:16','2022-06-18 16:14:16'),
	(140,'5c8eaafd45feebce.png','','crmebimage/public/product/2022/06/18/dce92e38b7564e1abafcd206865dc688ed1tgfx0tt.png','139473','png',815,1,-1,'2022-06-18 16:14:45','2022-06-18 16:14:45'),
	(141,'fc26ea88e89f982e.png','','crmebimage/public/product/2022/06/18/c895ed62a6f74427a99c090574f53550h9c9lcdfhf.png','494159','png',815,1,-1,'2022-06-18 16:14:45','2022-06-18 16:14:45'),
	(142,'fbe87e63cef203ce.png','','crmebimage/public/product/2022/06/18/524910ddd9c6482c8c70e1e6029cd18endhup3y4gl.png','307836','png',816,1,-1,'2022-06-18 16:14:53','2022-06-18 16:14:53'),
	(143,'6aa8d37a1676ad19.png','','crmebimage/public/product/2022/06/18/997b9a0d96aa409f86b61e8935212436gazj63iupp.png','589909','png',816,1,-1,'2022-06-18 16:14:53','2022-06-18 16:14:53'),
	(144,'2a0ad3c680438582.png','','crmebimage/public/product/2022/06/18/34c0cd755bfb4c0da2d7ed0261905edcp4n01l30w7.png','304761','png',817,1,-1,'2022-06-18 16:15:00','2022-06-18 16:15:00'),
	(145,'1d177c69191d80ac.png','','crmebimage/public/product/2022/06/18/0c41763dac274e29b45cd273338a2b52yzb0cwj0c0.png','128834','png',817,1,-1,'2022-06-18 16:15:00','2022-06-18 16:15:00'),
	(146,'2aa3117cb7be3806.png','','crmebimage/public/product/2022/06/18/13c7420d36f742ac880d501aba30dd7cnab51hroks.png','221391','png',818,1,-1,'2022-06-18 16:15:08','2022-06-18 16:15:08'),
	(147,'b9ec13dba888ae83.png','','crmebimage/public/product/2022/06/18/1aef8b8b35254ab98f6b5a4b69436c6368eoz7c4ce.png','172219','png',818,1,-1,'2022-06-18 16:15:08','2022-06-18 16:15:08'),
	(148,'76c25b2e724f8391.jpg','','crmebimage/public/product/2022/06/18/4ad28d4d84534aa1be84b6fe9f782e64gffx0tqq2k.jpg','54723','jpeg',819,1,-1,'2022-06-18 16:15:17','2022-06-18 16:15:17'),
	(149,'8c50f84042ce9c5f.png','','crmebimage/public/product/2022/06/18/3e05b25d8cce4072aac085ac486b3da5yz4enhu7ak.png','157138','png',819,1,-1,'2022-06-18 16:15:17','2022-06-18 16:15:17'),
	(150,'0c9af4335ef60773.png','','crmebimage/public/product/2022/06/18/115ca3ee301d41a6824365b393f76590kj66zndb4e.png','75525','png',820,1,-1,'2022-06-18 16:15:29','2022-06-18 16:15:29'),
	(151,'7997435818baa99f.png','','crmebimage/public/product/2022/06/18/c3ea8f6f85e940f88f539fea2c4df96b72m4w0yacl.png','176886','png',820,1,-1,'2022-06-18 16:15:29','2022-06-18 16:15:29'),
	(152,'919f5daacb48aeef.jpg','','crmebimage/public/product/2022/06/18/e79c2e164e9d455093c70cfb2dbfc85ezku22wmraq.jpg','36819','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(153,'87be1c8157af8810.jpg','','crmebimage/public/product/2022/06/18/8bcb2eb92dc948859c326f89df32283epfxcr2zc3r.jpg','33536','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(154,'b7938e0d65b22dbd.jpg','','crmebimage/public/product/2022/06/18/79e3eb9aaf184975a4079f8cad402e7ena2xzu5urm.jpg','79943','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(155,'080eca7e5be128b0.jpg','','crmebimage/public/product/2022/06/18/8fc10d9bbec747fc978199e8ba731fe873054yexp7.jpg','100076','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(156,'e6bab81a0bf52168.jpg','','crmebimage/public/product/2022/06/18/4d4ea639506a4add8b5cf7062b58ba12iokzsd9ha9.jpg','165209','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(157,'cd5c47fae20ad6cc.jpg','','crmebimage/public/product/2022/06/18/91ac2a1b069b477dbbac13fea837a785y6chifhvv3.jpg','186326','jpeg',826,1,-1,'2022-06-18 16:15:48','2022-06-18 16:15:48'),
	(158,'宝姿logo.jpeg','','crmebimage/public/store/2022/06/18/a535246301f44d3f8d511ac3e2dbe34454i3ti6t57.jpeg','8386','jpeg',0,1,-1,'2022-06-18 16:48:21','2022-06-18 16:48:21'),
	(159,'lauren-ralph-lauren_head_060915.jpg','','crmebimage/public/store/2022/06/18/2b621f5bfc2248b7976b065ffb26c3e8wv5kovrs0m.jpg','5917','jpeg',0,1,-1,'2022-06-18 17:20:15','2022-06-18 17:20:15'),
	(160,'51l9NRtsBlL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/bee5e6e174344246a22d51f3ba7c9befwkvijd70t6.jpg','11904','jpeg',846,1,1,'2022-06-18 17:27:36','2022-06-18 17:27:36'),
	(161,'71tT-H+hICL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/cb89e089bea24c49a63d7b7a50f12793ruixp9am8n.jpg','30567','jpeg',846,1,1,'2022-06-18 17:27:37','2022-06-18 17:27:37'),
	(162,'61jEovYadLL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/d4d15af3f376477e82270072fab9272ag6hvs9efnq.jpg','23883','jpeg',846,1,1,'2022-06-18 17:27:37','2022-06-18 17:27:37'),
	(163,'61P1+6-HwWL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/1c1d6d6d13e04b73951715b9addd675dxb0wl0litt.jpg','22325','jpeg',846,1,1,'2022-06-18 17:27:37','2022-06-18 17:27:37'),
	(164,'61GpyvkoRjL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/2c500367798d4254afd424a3481f6ca4hlmjyk6b2s.jpg','22422','jpeg',846,1,1,'2022-06-18 17:27:37','2022-06-18 17:27:37'),
	(165,'lauren-ralph-lauren_head_060915.jpg','','crmebimage/public/content/2022/06/18/fe7dc9d546734d2b870e16e36f4814326h3fy8ol7m.jpg','5917','jpeg',846,1,1,'2022-06-18 17:27:37','2022-06-18 17:27:37'),
	(166,'51GH16lBa9L._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/3951deb637da460fb57edf00e6669cd6obk7hazakn.jpg','25055','jpeg',846,1,1,'2022-06-18 17:42:39','2022-06-18 17:42:39'),
	(167,'51qNWpZCFHL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/0d99fd5bd89e45a7a73559f7ffb0da15mfjbqbmejy.jpg','26408','jpeg',846,1,1,'2022-06-18 17:42:39','2022-06-18 17:42:39'),
	(168,'51I95hPb64L._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/40d470010d4e4f9f8b04e0d22fb9c80fpmu92j3p7y.jpg','30232','jpeg',846,1,1,'2022-06-18 17:42:39','2022-06-18 17:42:39'),
	(169,'71Lp0ifGgEL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/f91e8b39378d415fbf6ef73afe5db778i93yiwhgtn.jpg','87465','jpeg',846,1,1,'2022-06-18 17:42:39','2022-06-18 17:42:39'),
	(170,'71UO8SAvxQL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/b30fe9e1bda54437880cc6cb1014de183z0aom39wu.jpg','44710','jpeg',846,1,1,'2022-06-18 17:46:19','2022-06-18 17:46:19'),
	(171,'61H0iOBa6xL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/5fb28aac023945648658b2ee10713a28ys6ekff7iy.jpg','31450','jpeg',846,1,1,'2022-06-18 17:46:19','2022-06-18 17:46:19'),
	(172,'71IQ1zd0P5L._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/54b13d109caa4880a984e022271669c5xl1gcmc4ye.jpg','34657','jpeg',846,1,1,'2022-06-18 17:46:19','2022-06-18 17:46:19'),
	(173,'81sAqXusC3L._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/1f55a4f592c84b92b7481682f0bf50dfv0skn4t6vb.jpg','91722','jpeg',846,1,1,'2022-06-18 17:46:19','2022-06-18 17:46:19'),
	(174,'81UTysXMFYL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/0b795f3d96904397a9f59e6d81edb02d6buesnfw1h.jpg','64531','jpeg',846,1,1,'2022-06-18 17:52:07','2022-06-18 17:52:07'),
	(175,'81A2ilEjROL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/682a2309187e4012a5e21766dd6d863a5qsp7t24aj.jpg','64224','jpeg',846,1,1,'2022-06-18 17:52:07','2022-06-18 17:52:07'),
	(176,'81r3bq6rmIL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/4db62bf9e57444788d31af6ef369eeeapxmvgdkr50.jpg','168582','jpeg',846,1,1,'2022-06-18 17:52:07','2022-06-18 17:52:07'),
	(177,'81yuj-ZbmpL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/6b7a785e4e9b4447a63c42d5f8c762183t972ze4pg.jpg','81719','jpeg',846,1,1,'2022-06-18 17:59:22','2022-06-18 17:59:22'),
	(178,'71fWAvE6WYL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/5f16f53a10464bf8b81de1c1a9e3c1fea2bi5y2u1s.jpg','53353','jpeg',846,1,1,'2022-06-18 17:59:22','2022-06-18 17:59:22'),
	(179,'713YKbVi3tL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/eaa186578c994fc2ba79956f6b8a8384dk2e308nus.jpg','51260','jpeg',846,1,1,'2022-06-18 17:59:22','2022-06-18 17:59:22'),
	(180,'71astCCw0YL._AC_SR736,920_.jpg','','crmebimage/public/content/2022/06/18/2b866d80688c41ee9c2e802c4644d93dvuq79bza7q.jpg','47708','jpeg',846,1,1,'2022-06-18 17:59:22','2022-06-18 17:59:22'),
	(181,'download.webp','','crmebimage/public/store/2022/06/18/b155b9b759b34a2896900bf6f75df8dd46bbwdq5vv.webp','78868','webp',849,1,2,'2022-06-18 18:05:15','2022-06-18 18:05:15'),
	(182,'download.png','','crmebimage/public/store/2022/06/18/98c914267cad4e4d83f08bbb1b57ffaap525x1w2u7.png','78868','png',849,1,2,'2022-06-18 18:05:37','2022-06-18 18:05:37'),
	(183,'9f6a51440757e3.jpg','','crmebimage/public/content/2022/06/18/b9a0f136b58243e58bc938c6ff3e57aes7rz0y0g8f.jpg','46346','jpeg',0,1,3,'2022-06-18 19:48:08','2022-06-18 19:48:08'),
	(184,'47f8cf9de63d4fa5.jpg!q70.jpg','','crmebimage/public/content/2022/06/18/8de2d2fb27f547cb8ff23b639c5f4bafa6jmpzx9zw.jpg','20630','jpeg',0,1,3,'2022-06-18 19:49:41','2022-06-18 19:49:41'),
	(185,'622c358cbc6d062d.jpg!q70.jpg','','crmebimage/public/content/2022/06/18/8d941ced90064eb094dce282a94e1bcafd95xw515z.jpg','42016','jpeg',0,1,3,'2022-06-18 19:49:42','2022-06-18 19:49:42'),
	(186,'9fc0401c81b1508a.jpg','','crmebimage/public/content/2022/06/18/4bc7d0814fff4155bd2d8a1c20ef209bdpgnr6jncg.jpg','122864','jpeg',0,1,3,'2022-06-18 19:54:28','2022-06-18 19:54:28'),
	(187,'d3a3ea5f929330cc.jpg','','crmebimage/public/content/2022/06/18/fa404430a3464ac1932c15cf8efcc3ac3v0ok05c33.jpg','129270','jpeg',0,1,3,'2022-06-18 19:54:28','2022-06-18 19:54:28'),
	(188,'4b8de6483d637b9d.jpg','','crmebimage/public/content/2022/06/18/7b5634bed37c4e0a8db0505797ea47bezxto1e48jr.jpg','182088','jpeg',0,1,3,'2022-06-18 19:57:53','2022-06-18 19:57:53'),
	(189,'5681ff0347a2c81e.jpg','','crmebimage/public/content/2022/06/18/936cbb1e7f4548d2b7d4b7e51dcce066b1uy2qi1qp.jpg','20114','jpeg',0,1,3,'2022-06-18 21:00:05','2022-06-18 21:00:05'),
	(190,'ceb521a9c457a506.jpg','','crmebimage/public/content/2022/06/18/154713b5a1d04d98b47f0b6602e0dfe61xhxn12658.jpg','22560','jpeg',0,1,3,'2022-06-18 21:00:05','2022-06-18 21:00:05'),
	(191,'v2-bc5323333585706b5c8e528666ed74fa_r.jpg','','crmebimage/public/operation/2022/06/18/55cef1e3ad164483b78df91fc3dfd5378jy3uk4tvq.jpg','149023','jpeg',0,1,3,'2022-06-18 21:07:00','2022-06-18 21:07:00'),
	(192,'dazongzi.png','','crmebimage/public/maintain/2022/06/20/4a32b494cccc4196b769fa673174a077b2geq843dh.png','732239','png',0,1,-1,'2022-06-20 15:44:05','2022-06-20 15:44:05'),
	(193,'dazongzi.png','','crmebimage/public/maintain/2022/06/20/bec07333a4e64925ae92a91daebb0ebbqo2fsyqmr6.png','732239','png',0,1,-1,'2022-06-20 15:44:31','2022-06-20 15:44:31'),
	(194,'活动banner 3.jpg','','crmebimage/public/operation/2022/06/20/53d232f8b7714e6a8322cb513e7a59d18udnr8q24c.jpg','108317','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(195,'活动banner 1.jpg','','crmebimage/public/operation/2022/06/20/81657874c2984c5cbffc5f6e4ccb81ackl6jglnp9f.jpg','103446','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(196,'首页banner 4.jpg','','crmebimage/public/operation/2022/06/20/4a20fe1498bd438180d962eb5a5d0718tlljyy1kq6.jpg','144537','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(197,'首页banner 2.jpg','','crmebimage/public/operation/2022/06/20/be1888e8f85d42a581a812e59fbb52477yc4ocmkqe.jpg','159425','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(198,'活动banner 2.jpg','','crmebimage/public/operation/2022/06/20/104089330b8943939610d5c3983c9156au2kvwu22l.jpg','195683','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(199,'店铺街banner.jpg','','crmebimage/public/operation/2022/06/20/d69b8bb418c74e1f9497a54fa10ddbdd1uxlu7sm61.jpg','216036','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(200,'首页banner 1.jpg','','crmebimage/public/operation/2022/06/20/e981ae8cc8e94c56987be5f22cda3d8b7ahfm8qfby.jpg','364025','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23'),
	(201,'首页banner 3.jpg','','crmebimage/public/operation/2022/06/20/82f27cd31c8f437c83e1426b04cd95a88vzp1tlcj9.jpg','124366','jpeg',844,1,2,'2022-06-20 16:02:23','2022-06-20 16:02:23');

/*!40000 ALTER TABLE `eb_system_attachment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_config`;

CREATE TABLE `eb_system_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '字段名称',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '字段提示文字',
  `form_id` int(10) DEFAULT '0' COMMENT '表单id',
  `value` text COMMENT '值',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `status+name` (`name`) USING BTREE,
  KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='配置表';

LOCK TABLES `eb_system_config` WRITE;
/*!40000 ALTER TABLE `eb_system_config` DISABLE KEYS */;

INSERT INTO `eb_system_config` (`id`, `name`, `title`, `form_id`, `value`, `status`, `create_time`, `update_time`)
VALUES
	(177,'close_system','',0,'0',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(195,'store_postage','',0,'0',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(202,'main_business','',0,' IT\\u79d1\\u6280 \\u4e92\\u8054\\u7f51|\\u7535\\u5b50\\u5546\\u52a1',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(203,'vice_business','',0,'IT\\u79d1\\u6280 IT\\u8f6f\\u4ef6\\u4e0e\\u670d\\u52a1 ',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(204,'store_brokerage_rate_1','',0,'80',0,'2020-05-14 15:20:25','2020-07-14 16:44:50'),
	(205,'user_extract_min_price','',0,'1',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(206,'sx_sign_min_int','',0,'1',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(207,'sx_sign_max_int','',0,'5',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(218,'store_brokerage_rate_2','',0,'60',0,'2020-05-14 15:20:25','2020-07-14 16:44:54'),
	(219,'store_brokerage_status','store_brokerage_status',0,'1',0,'2020-05-14 15:20:25','2020-08-05 17:42:38'),
	(227,'user_extract_bank','',0,'中国银行\n建设银行\n农业银行',0,'2020-05-14 15:20:25','2020-06-09 17:43:00'),
	(228,'fast_number','',0,'10',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(229,'bast_number','',0,'10',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(230,'first_number','',0,'10',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(234,'accessKey','',0,'AKIDylJg18Ln2312312313QMdYsQ',0,'2020-05-14 15:20:25','2020-06-03 17:20:54'),
	(235,'secretKey','',0,'SebIWXNxtKgmbEeqKckC4i',0,'2020-05-14 15:20:25','2020-06-03 17:21:02'),
	(236,'storage_name','',0,'wuht-13009031231239283',0,'2020-05-14 15:20:25','2020-06-03 17:21:18'),
	(242,'storage_region','',0,'ap-chengdu',0,'2020-05-14 15:20:25','2020-06-03 17:37:19'),
	(245,'system_delivery_time','',0,'1',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(254,'cache_config','',0,'86400',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(262,'confirmTakeOverSwitch','',0,'0',0,'2020-05-14 15:20:25','2020-06-17 09:34:31'),
	(269,'extract_time','',0,'0',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(270,'store_brokerage_price','',0,'1',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(272,'promotion_number','',0,'3',0,'2020-05-14 15:20:25','2020-05-14 16:37:47'),
	(645,'importProductTB','',0,'https://api03.6bqb.com/taobao/detail',0,'2020-06-05 14:35:08','2020-09-08 12:15:01'),
	(646,'importProductJD','',0,'https://api03.6bqb.com/jd/detail',0,'2020-06-05 14:35:34','2020-09-08 12:15:08'),
	(647,'importProductSN','',0,'https://api03.6bqb.com/suning/detail',0,'2020-06-05 14:35:41','2020-09-08 12:14:59'),
	(648,'importProductPDD','',0,'https://api03.6bqb.com/pdd/detail',0,'2020-06-05 14:36:20','2020-09-08 12:15:04'),
	(649,'importProductTM','',0,'https://api03.6bqb.com/tmall/detail',0,'2020-06-09 14:24:32','2020-09-08 12:14:57'),
	(657,'sms_account','',0,'18292417675',0,'2020-06-16 12:17:29','2020-12-23 17:16:10'),
	(658,'sms_token','',0,'1qazxsw2',0,'2020-06-16 12:17:35','2020-12-23 17:16:09'),
	(988,'integralRatio','integralRatio',0,'0',0,'2020-07-08 15:21:59','2020-08-10 21:27:45'),
	(989,'balance_func_status','balance_func_status',0,'1',0,'2020-07-09 10:09:33','2020-07-14 19:02:16'),
	(991,'brokerage_func_status','brokerage_func_status',0,'1',0,'2020-07-21 10:42:36','2020-07-21 10:42:36'),
	(992,'store_brokerage_ratio','store_brokerage_ratio',0,'10',0,'2020-07-21 10:45:10','2020-12-18 11:34:26'),
	(993,'store_brokerage_two','store_brokerage_two',0,'5',0,'2020-07-21 10:45:41','2020-07-21 10:45:41'),
	(994,'brokerage_bindind','brokerage_bindind',0,'0',0,'2020-07-21 15:29:25','2020-07-21 15:29:25'),
	(3172,'config_export_temp_id','config_export_temp_id',0,'42bd223a111f075299a',0,'2020-12-10 14:50:34','2020-12-17 14:49:31'),
	(3173,'config_export_id','config_export_id',0,'23',0,'2020-12-10 14:50:45','2020-12-17 14:49:31'),
	(3174,'config_export_com','config_export_com',0,'yuantongkuaiyun',0,'2020-12-10 14:52:57','2020-12-17 14:49:31'),
	(4053,'config_export_open','config_export_open',129,'1',0,'2021-02-04 11:21:21','2021-02-04 11:21:21'),
	(4054,'config_export_to_name','config_export_to_name',129,'王大蛋',0,'2021-02-04 11:21:21','2021-02-04 11:21:21'),
	(4055,'config_export_to_tel','config_export_to_tel',129,'18710383544',0,'2021-02-04 11:21:21','2021-02-04 11:21:21'),
	(4056,'config_export_to_address','config_export_to_address',129,'陕西省西安市曲江新区',0,'2021-02-04 11:21:21','2021-02-04 11:21:21'),
	(4057,'config_export_siid','config_export_siid',129,'1111',0,'2021-02-04 11:21:21','2021-02-04 11:21:21'),
	(4410,'bastInfo','bastInfo',133,'asda sd',0,'2021-02-23 14:58:53','2021-02-23 14:58:53'),
	(4411,'firstInfo','firstInfo',133,'1',0,'2021-02-23 14:58:53','2021-02-23 14:58:53'),
	(4412,'salesInfo','salesInfo',133,'1',0,'2021-02-23 14:58:53','2021-02-23 14:58:53'),
	(4413,'hotInfo','hotInfo',133,'1',0,'2021-02-23 14:58:53','2021-02-23 14:58:53'),
	(5916,'change_color_config','change_color_config',0,'1',0,'2021-07-16 09:55:24','2021-07-17 09:14:44'),
	(6081,'category_page_config','category_page_config',0,'4',0,'2021-08-12 15:57:49','2021-08-13 11:05:11'),
	(6082,'is_show_category','is_show_category',0,'true',0,'2021-08-12 15:59:56','2021-08-13 11:05:14'),
	(6216,'store_brokerage_quota','store_brokerage_quota',0,'10',0,'2021-07-16 09:55:24','2021-07-16 09:55:24'),
	(6217,'store_brokerage_is_bubble','store_brokerage_is_bubble',0,'1',0,'2021-08-18 11:57:17','2021-08-18 12:23:14'),
	(6582,'txUploadUrl','txUploadUrl',83,'https://wuht-1300909283.cos.ap-chengdu.myqcloud.com',0,'2021-08-23 14:54:05','2021-08-23 14:54:05'),
	(6583,'txAccessKey','txAccessKey',83,'1111',0,'2021-08-23 14:54:05','2021-08-23 14:54:05'),
	(6584,'txSecretKey','txSecretKey',83,'1111',0,'2021-08-23 14:54:06','2021-08-23 14:54:06'),
	(6585,'txStorageName','txStorageName',83,'wuht-1300909283',0,'2021-08-23 14:54:06','2021-08-23 14:54:06'),
	(6586,'txStorageRegion','txStorageRegion',83,'ap-chengdu',0,'2021-08-23 14:54:06','2021-08-23 14:54:06'),
	(6670,'qnUploadUrl','qnUploadUrl',82,'https://qiniu.crmeb.net',0,'2021-08-23 18:01:03','2021-08-23 18:01:03'),
	(6671,'qnAccessKey','qnAccessKey',82,'1111',0,'2021-08-23 18:01:03','2021-08-23 18:01:03'),
	(6672,'qnSecretKey','qnSecretKey',82,'1111',0,'2021-08-23 18:01:03','2021-08-23 18:01:03'),
	(6673,'qnStorageName','qnStorageName',82,'crmeb-pro',0,'2021-08-23 18:01:03','2021-08-23 18:01:03'),
	(6674,'qnStorageRegion','qnStorageRegion',82,'huabei',0,'2021-08-23 18:01:03','2021-08-23 18:01:03'),
	(6814,'alUploadUrl','alUploadUrl',81,'1111',0,'2021-08-23 19:29:48','2021-08-23 19:29:48'),
	(6815,'alAccessKey','alAccessKey',81,'1111',0,'2021-08-23 19:29:48','2021-08-23 19:29:48'),
	(6816,'alSecretKey','alSecretKey',81,'1111',0,'2021-08-23 19:29:48','2021-08-23 19:29:48'),
	(6817,'alStorageName','alStorageName',81,'wuht',0,'2021-08-23 19:29:48','2021-08-23 19:29:48'),
	(6818,'alStorageRegion','alStorageRegion',81,'oss-cn-beijing.aliyuncs.com',0,'2021-08-23 19:29:48','2021-08-23 19:29:48'),
	(7178,'homePageSaleListStyle','',0,'1',0,'2021-10-28 09:27:31','2021-10-28 09:27:31'),
	(7885,'google_client_id','google_client_id',146,'527732578974-i1v5q5h1tgvi9mf.apps.googleusercontent.com',0,'2022-02-11 18:06:17','2022-02-11 18:06:17'),
	(7886,'google_open','google_open',146,'\'1\'',0,'2022-02-11 18:06:17','2022-02-11 18:06:17'),
	(7900,'twitter_consumer_key','twitter_consumer_key',145,'X9IleGcyiqyZPPzwQwETOCQVy',0,'2022-02-11 18:26:45','2022-02-11 18:26:45'),
	(7901,'twitter_consumer_secret','twitter_consumer_secret',145,'darXo7gSPyvhgdXHjvxVF7FB359KsFU',0,'2022-02-11 18:26:45','2022-02-11 18:26:45'),
	(7902,'twitter_open','twitter_open',145,'\'1\'',0,'2022-02-11 18:26:45','2022-02-11 18:26:45'),
	(7974,'aliyun_sms_key_id','aliyun_sms_key_id',111,'LTAI4Fp8pbzCbiVkL',0,'2022-02-16 11:35:10','2022-02-16 11:35:10'),
	(7975,'aliyun_sms_key_secret','aliyun_sms_key_secret',111,'fQ3BB0rgbWhBkKPA',0,'2022-02-16 11:35:10','2022-02-16 11:35:10'),
	(7976,'aliyun_sms_sign_name','aliyun_sms_sign_name',111,'CRMEB外贸版',0,'2022-02-16 11:35:10','2022-02-16 11:35:10'),
	(7977,'sms_code_expire','sms_code_expire',111,'3',0,'2022-02-16 11:35:10','2022-02-16 11:35:10'),
	(8094,'importProductToken','importProductToken',122,'6DA0B12785EC597953548B71265A28B5',0,'2022-03-31 09:52:46','2022-03-31 09:52:46'),
	(8095,'id','id',122,'122',0,'2022-03-31 09:52:46','2022-03-31 09:52:46'),
	(8096,'guaranteed_amount','guaranteed_amount',0,'5',0,'2022-04-06 18:08:46','2022-04-06 18:08:46'),
	(8097,'transfer_min_amount','transfer_min_amount',0,'2',0,'2022-04-06 18:09:00','2022-04-06 18:09:00'),
	(8098,'transfer_max_amount','transfer_max_amount',0,'3',0,'2022-04-06 18:09:12','2022-04-06 18:09:12'),
	(8099,'balance_freeze_day','balance_freeze_day',0,'0',0,'2022-04-06 18:09:19','2022-04-06 18:09:19'),
	(8167,'ylyprint_app_id','ylyprint_app_id',143,'1037537262',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8168,'ylyprint_app_secret','ylyprint_app_secret',143,'1111',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8169,'ylyprint_app_machine_code','ylyprint_app_machine_code',143,'4004627180',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8170,'ylyprint_app_machine_msign','ylyprint_app_machine_msign',143,'1111',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8171,'ylyprint_auto_status','ylyprint_auto_status',143,'\'0\'',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8172,'ylyprint_status','ylyprint_status',143,'\'0\'',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8173,'id','id',143,'143',0,'2022-04-25 10:52:34','2022-04-25 10:52:34'),
	(8300,'index_banner_type','index_banner_type',0,'1',0,'2022-05-10 14:43:56','2022-05-10 15:16:11'),
	(8301,'merSettlementAgreement','',0,'<h2>1.本协议的订立</h2>\n<p class=\"cont\">在本网站依据《商城网站用户注册协议》登记注册，且符合本网站 商家入驻标准的用户（以下简称\"商家\"），在同意本协议全部条款后，方有资格使用\"本商城商家在线入驻系统\"（以下简称\"入驻系统\"）申请入驻。一经商家点击\"同意以上协议，下一步\"按键， 即意味着商家同意与本网站签订本协议并同意受本协议约束。</p>\n<h2>2.入驻系统使用说明</h2>\n<p class=\"cont\">2.1 商家通过入驻系统提出入驻申请，并按照要求填写商家信息、提供商家资质资料后，由本网站审核并与有合作意向的商家联系协商合作相关事宜，经双方协商一致线下签订书面《开放平台 供应商合作运营协议》（以下简称\"运营协议\"），且商家按照\"运营协议\"约定支付相应平台使用费及保证金等必要费用后，商家正式入驻本网站。本网站将为入驻商家开通商家后 台系统，商家可通过商家后台系统在本网站运营自己的入驻店铺。</p>\n<p class=\"cont\">2.2 商家以及本网站通过入驻系统做出的申请、资料提交及确认等各类沟通，仅为双方合作的意向以及本网站对商家资格审核的必备程序，除遵守本协议各项约定外，对双方不产生法律约束力。 双方间最终合作事宜及运营规则均以\"运营协议\"的约定及商家后台系统公示的各项规则为准。</p>\n<h2>3.商家权利义务</h2>\n<p>用户使用\"商城商家在线入驻系统\"前请认真阅读并理解本协议内容，本协议内容中以加粗方式显著标识的条款，请用户着重阅读、慎重考虑。</p>\n<h2>1.本协议的订立</h2>\n<p class=\"cont\">在本网站依据《商城网站用户注册协议》登记注册，且符合本网站商家入驻标准的用户（以下简称\"商家\"），在同意本协议全部条款后，方有资格使用\"商城商家在线入驻系统\"（以下简称\"入驻系统\"） 申请入驻。一经商家点击\"同意以上协议，下一步\"按键，即意味着商家同意与本网站签订本协议并同意受本协议约束。</p>\n<h2>2.入驻系统使用说明</h2>\n<p class=\"cont\">2.1 商家通过入驻系统提出入驻申请，并按照要求填写商家信息、提供商家资质资料后，由本网站审核并与有合作意向的商家联系协商合作相关事宜，经双方协商一致线下签订书面《开放平台供应商合作运营协议》 （以下简称\"运营协议\"），且商家按照\"运营协议\"约定支付相应平台使用费及保证金等必要费用后，商家正式入驻本网站。本网站将为入驻商家开通商家后台系统，商家可通过商家后台系统在本网站运营自己的入驻店铺。</p>\n<p class=\"cont\">2.2 商家以及本网站通过入驻系统做出的申请、资料提交及确认等各类沟通，仅为双方合作的意向以及本网站对商家资格审核的必备程序，除遵守本协议各项约定外，对双方不产生法律约束力。双方间最终合作事宜及 运营规则均以\"运营协议\"的约定及商家后台系统公示的各项规则为准。</p>\n<h2>3.商家权利义务</h2>\n<p class=\"cont\">用户使用\"商城商家在线入驻系统\"前请认真阅读并理解本协议内容，本协议内容中以加粗方式显著标识的条款，请用户着重阅读、慎重考虑。</p>\n<p>&nbsp;</p>',0,'2022-05-10 16:58:22','2022-05-10 16:59:12'),
	(8477,'facebook_appid','facebook_appid',144,'1767736616929813',0,'2022-06-09 11:11:36','2022-06-09 11:11:36'),
	(8478,'facebook_open','facebook_open',144,'\'0\'',0,'2022-06-09 11:11:36','2022-06-09 11:11:36'),
	(8479,'id','id',144,'144',0,'2022-06-09 11:11:36','2022-06-09 11:11:36'),
	(8554,'paypal_client_id','paypal_client_id',147,'AX7kgV-03kKpyAX4l65-se4zO2j96hlhsZryJxD',0,'2022-06-10 11:35:23','2022-06-10 11:35:23'),
	(8555,'paypal_client_secret','paypal_client_secret',147,'EOV34NlnZ9sKnHfCu4i16qG-0TlC6eh-ebUyVLMBvB6YDKetX5dW91',0,'2022-06-10 11:35:23','2022-06-10 11:35:23'),
	(8556,'paypal_mode','paypal_mode',147,'sandbox',0,'2022-06-10 11:35:23','2022-06-10 11:35:23'),
	(8557,'paypal_switch','paypal_switch',147,'\'1\'',0,'2022-06-10 11:35:23','2022-06-10 11:35:23'),
	(8558,'id','id',147,'147',0,'2022-06-10 11:35:23','2022-06-10 11:35:23'),
	(8559,'stripe_api_key','stripe_api_key',164,'sk_test_51L3yjJEWHO5x2V9iyF3rl6114IaGfpv1Wu7IbkpWdJrxY7eBsGct4Qt781kH8Vx00Fd056OJm',0,'2022-06-10 14:14:28','2022-06-10 14:14:28'),
	(8560,'stripe_switch','stripe_switch',164,'\'1\'',0,'2022-06-10 14:14:29','2022-06-10 14:14:29'),
	(8561,'id','id',164,'164',0,'2022-06-10 14:14:29','2022-06-10 14:14:29'),
	(8574,'visitor_open','visitor_open',148,'\'1\'',0,'2022-06-13 17:50:07','2022-06-13 17:50:07'),
	(8575,'id','id',148,'148',0,'2022-06-13 17:50:07','2022-06-13 17:50:07'),
	(8576,'merLoginAgreement','',0,'<p style=\"text-align: center;\"><span style=\"font-size: 18pt;\"><strong>RMEB用户注册协议</strong></span></p>\n<p style=\"text-align: left;\"><br />一、服务条款的确认及接受</p>\n<p>1、CRMEB网站（指*****.com及其移动客户端软件、应用程序，以下称&ldquo;本网站&rdquo;）各项电子服务的所有权和运作权归属于&ldquo;CRMEB&rdquo;所有，本网站提供的服务将完全按照其发布的服务条款和操作规则严格执行。您确认所有服务条款并完成注册程序时，本协议在您与本网站间成立并发生法律效力，同时您成为本网站正式用户。<br />2、根据国家法律法规变化及本网站运营需要，CRMEB有权对本协议条款及相关规则不时地进行修改，修改后的内容一旦以任何形式公布在本网站上即生效，并取代此前相关内容，您应不时关注本网站公告、提示信息及协议、规则等相关内容的变动。您知悉并确认，如您不同意更新后的内容，应立即停止使用本网站；如您继续使用本网站，即视为知悉变动内容并同意接受。<br />3、本协议内容中以加粗方式显著标识的条款，请您着重阅读。您点击&ldquo;同意&rdquo;按钮即视为您完全接受本协议，在点击之前请您再次确认已知悉并完全理解本协议的全部内容。<br />二、服务须知<br />1、本网站运用自身开发的操作系统通过国际互联网络为用户提供购买商品等服务。使用本网站，您必须：</p>\n<p>（1）自行配备上网的所需设备，包括个人手机、平板电脑、调制解调器、路由器等；</p>\n<p>（2）自行负担个人上网所支付的与此服务有关的电话费用、网络费用等；</p>\n<p>（3）选择与所安装终端设备相匹配的软件版本，包括但不限于iOS、Android、Windows等多个CRMEB发布的应用版本。</p>\n<p>2、基于本网站所提供的网络服务的重要性，您确认并同意：</p>\n<p>（1）提供的注册资料真实、准确、完整、合法有效，注册资料如有变动的，应及时更新；</p>\n<p>（2）如果您提供的注册资料不合法、不真实、不准确、不详尽的，您需承担因此引起的相应责任及后果，并且CRMEB保留终止您使用本网站各项服务的权利。</p>\n<p>三、订单<br />1、使用本网站下订单，您应具备购买相关商品的权利能力和行为能力，如果您在18周岁以下，您需要在监护人的监护参与下才能注册并使用本网站。在下订单的同时，即视为您满足上述条件，并对您在订单中提供的所有信息的真实性负责。<br />2、在您下订单时，请您仔细确认所购商品的名称、价格、数量、型号、规格、尺寸、联系地址、电话、收货人等信息。收货人与您本人不一致的，收货人的行为和意思表示视为您的行为和意思表示，您应对收货人的行为及意思表示的法律后果承担连带责任。<br />3、您理解并同意：本网站上销售商展示的商品和价格等信息仅仅是要约邀请，您下单时须填写您希望购买的商品数量、价款及支付方式、收货人、联系方式、收货地址（合同履行地点）、合同履行方式等内容；系统生成的订单信息是计算机信息系统根据您填写的内容自动生成的数据，仅是您向销售商发出的合同要约；销售商收到您的订单信息后，在销售商将您在订单中订购的商品从仓库实际直接向您发出时（以商品出库为标志），视为您与销售商之间就实际直接向您发出的商品建立了合同关系；如果您在一份订单里订购了多种商品并且销售商只给您发出了部分商品时，您与销售商之间仅就实际直接向您发出的商品建立了合同关系，只有在销售商实际直接向您发出了订单中订购的其他商品时，您和销售商之间就订单中其他已实际直接向您发出的商品才成立合同关系；对于电子书、数字音乐、在线手机充值等数字化商品，您下单并支付货款后合同即成立。当您作为消费者为生活消费需要下单并支付货款的情况下，您货款支付成功后即视为您与销售商之间就已支付货款部分的订单建立了合同关系。<br />4、尽管销售商做出最大的努力，但由于市场变化及各种以合理商业努力难以控制因素的影响，本网站无法避免您提交的订单信息中的商品出现缺货、价格标示错误等情况；如您下单所购买的商品出现以上情况，您有权取消订单，销售商亦有权依法、依约取消订单，若您已经付款，则为您办理退款，并提供订单处理方案。<br />四、配送和交付<br />1、您在本网站购买的商品将按照本网站上您所指定的送货地址进行配送。订单信息中列出的送货时间为参考时间，参考时间的计算是根据库存状况、正常的处理过程和送货时间、送货地点的基础上估计得出的。您应当清楚准确地填写您的送货地址、联系人及联系方式等配送信息，您知悉并确认，您所购买的商品应仅由您填写的联系人接受身份查验后接收商品，因您变更联系人或相关配送信息而造成的损失由您自行承担。<br />2、因如下情况造成订单延迟或无法配送等，本网站将无法承担迟延配送或无法配送的责任：</p>\n<p>（1）客户提供错误信息和不详细的地址；<br />（2）货物送达无人签收或拒收，由此造成的重复配送所产生的费用及相关的后果。&nbsp;<br />（3）不可抗力，例如：自然灾害及恶劣天气、交通戒严等政府、司法机关的行为、决定或命令、意外交通事故、罢工、法规政策的修改、恐怖事件、抢劫、抢夺等暴力犯罪、突发战争等。</p>',0,'2022-06-14 10:51:42','2022-06-14 10:51:42'),
	(8583,'consumer_h5_url','consumer_h5_url',76,'https://cschat.antcloud.com.cn/index.htm?tntInstId=jm7_c46J&scene=SCE01197657',0,'2022-06-14 21:03:06','2022-06-14 21:03:06'),
	(8584,'consumer_hotline','consumer_hotline',76,'400-8888-794',0,'2022-06-14 21:03:06','2022-06-14 21:03:06'),
	(8585,'consumer_message','consumer_message',76,'https://m.me/peim.stive',0,'2022-06-14 21:03:06','2022-06-14 21:03:06'),
	(8586,'consumer_email','consumer_email',76,'337031187@qq.com',0,'2022-06-14 21:03:07','2022-06-14 21:03:07'),
	(8587,'consumer_type','consumer_type',76,'hotline',0,'2022-06-14 21:03:07','2022-06-14 21:03:07'),
	(8588,'id','id',76,'76',0,'2022-06-14 21:03:07','2022-06-14 21:03:07'),
	(8589,'logistics_app_key','logistics_app_key',128,'204076019',0,'2022-06-15 11:34:51','2022-06-15 11:34:51'),
	(8590,'logistics_app_secret','logistics_app_secret',128,'eBoSP4JwsnFHSpmkw1',0,'2022-06-15 11:34:51','2022-06-15 11:34:51'),
	(8591,'id','id',128,'128',0,'2022-06-15 11:34:51','2022-06-15 11:34:51'),
	(8662,'stor_reason','stor_reason',77,'收货地址填错了 \n与描述不符 \n信息填错了\n重新拍 \n收到商品损坏了 \n未按预定时间发货 其它原因',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8663,'mobile_top_logo','mobile_top_logo',77,'crmebimage/public/product/2022/06/17/474a2d745b094151b7d9d46ed692ac9ek3970ns7g4.png',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8664,'mobile_login_logo','mobile_login_logo',77,'crmebimage/public/product/2022/06/17/5b29ba0909b6431abd4a741611a6d9a11eoxhe1g0v.png',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8665,'h5_avatar','h5_avatar',77,'crmebimage/public/product/2022/06/18/657314edfc124a75a2788f8dbc32c396xx77jduaq7.jpg',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8666,'order_cancel_time','order_cancel_time',77,'2',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8667,'site_name','site_name',77,'CRMEB JAVA 外贸 多商户',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8668,'seo_title','seo_title',77,'CRMEB JAVA 外贸 多商户',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8669,'news_slides_limit','news_slides_limit',77,'3',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8670,'id','id',77,'77',0,'2022-06-18 14:49:39','2022-06-18 14:49:39'),
	(8676,'site_logo_lefttop','site_logo_lefttop',64,'crmebimage/public/product/2022/06/17/818293a5b095496287dbcf59a18b0b9e1ozzowfx5a.png',0,'2022-06-18 15:04:50','2022-06-18 15:04:50'),
	(8677,'site_logo_square','site_logo_square',64,'crmebimage/public/product/2022/06/17/5b29ba0909b6431abd4a741611a6d9a11eoxhe1g0v.png',0,'2022-06-18 15:04:50','2022-06-18 15:04:50'),
	(8678,'site_logo_login','site_logo_login',64,'crmebimage/public/product/2022/06/17/9b6f9eeb611d46019fc75d50d22d14f8qo1nybufk7.png',0,'2022-06-18 15:04:50','2022-06-18 15:04:50'),
	(8679,'admin_login_bg_pic','admin_login_bg_pic',64,'crmebimage/public/product/2022/06/18/2b813c5b0a614694ab2b1bba017d6c4cpb909hu07o.jpg',0,'2022-06-18 15:04:50','2022-06-18 15:04:50'),
	(8680,'id','id',64,'64',0,'2022-06-18 15:04:50','2022-06-18 15:04:50'),
	(8681,'pc_home_recommend_image','pc_home_recommend_image',163,'crmebimage/public/product/2022/06/18/1dc61934d1de4dfb9e5434bb4262297cryuyctycp3.png',0,'2022-06-18 18:01:47','2022-06-18 18:01:47'),
	(8682,'pc_shop_street_header_image','pc_shop_street_header_image',163,'crmebimage/public/product/2022/06/18/8dced0fe59a04e0db43e83148571c97a1rzfulbszq.png',0,'2022-06-18 18:01:47','2022-06-18 18:01:47'),
	(8683,'pc_top_logo','pc_top_logo',163,'crmebimage/public/product/2022/06/17/9b6f9eeb611d46019fc75d50d22d14f8qo1nybufk7.png',0,'2022-06-18 18:01:47','2022-06-18 18:01:47'),
	(8684,'pc_login_left_image','pc_login_left_image',163,'crmebimage/public/product/2022/06/18/3e88a5e85d674e1f8d8283c96c7dfc6a1wajapbutf.png',0,'2022-06-18 18:01:47','2022-06-18 18:01:47'),
	(8685,'id','id',163,'163',0,'2022-06-18 18:01:47','2022-06-18 18:01:47'),
	(8686,'localUploadUrl','localUploadUrl',108,'https://api.adminwm.java.crmeb.net',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8687,'image_ext_str','image_ext_str',108,'jpg,jpeg,gif,png,bmp,PNG,JPG,mp4,webp',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8688,'image_max_size','image_max_size',108,'10',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8689,'file_ext_str','file_ext_str',108,'zip,doc,docx,xls,xlsx,pdf,mp3,wma,wav,amr,mp4',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8690,'file_max_size','file_max_size',108,'20',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8691,'uploadType','uploadType',108,'1',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8692,'file_is_save','file_is_save',108,'1',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8693,'id','id',108,'108',0,'2022-06-20 11:03:39','2022-06-20 11:03:39'),
	(8751, 'shop_pay_currency', '', 0, '{\"country\":\" 中国大陆\",\"currency\":\"人民币\",\"symbol\":\"¥\",\"ID\":\"53\"}', 0, '2023-08-10 14:50:34', '2023-08-10 14:50:34');

/*!40000 ALTER TABLE `eb_system_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_form_temp
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_form_temp`;

CREATE TABLE `eb_system_form_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表单模板id',
  `name` varchar(500) NOT NULL DEFAULT '' COMMENT '表单名称',
  `info` varchar(500) NOT NULL DEFAULT '' COMMENT '表单简介',
  `content` text NOT NULL COMMENT '表单内容',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='表单模板';

LOCK TABLES `eb_system_form_temp` WRITE;
/*!40000 ALTER TABLE `eb_system_form_temp` DISABLE KEYS */;

INSERT INTO `eb_system_form_temp` (`id`, `name`, `info`, `content`, `create_time`, `update_time`)
VALUES
	(64,'基础配置','系统设置-基础配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":300,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"左上角菜单logo(236x64)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":124,\"renderKey\":1595658064081,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"picture-card\",\"multiple\":false,\"__vModel__\":\"site_logo_lefttop\"},{\"__config__\":{\"label\":\"左上角缩回菜单logo(164x164)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":127,\"renderKey\":1595658695317,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"site_logo_square\"},{\"__config__\":{\"label\":\"登录页logo(164x164)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1612510257294,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"picture-card\",\"multiple\":false,\"__vModel__\":\"site_logo_login\"},{\"__config__\":{\"label\":\"登录页背景图(1920x1080)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":152,\"renderKey\":1596017451389,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"admin_login_bg_pic\"}]}','2020-05-15 17:20:10','2022-04-14 16:13:11'),
	(72,'上传文件配置-基础配置','上传文件配置-基础配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":130,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"layout\":\"rowFormItem\",\"tagIcon\":\"row\",\"label\":\"行容器\",\"layoutTree\":true,\"children\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/layout\",\"formId\":101,\"span\":24,\"renderKey\":1592474288129,\"componentName\":\"row101\",\"gutter\":15},\"type\":\"default\",\"justify\":\"start\",\"align\":\"top\"}]}','2020-05-19 17:10:40','2020-06-18 17:58:09'),
	(76,'客服配置','客服配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":120,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"客服H5链接\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1599641784700,\"tips\":true,\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2322225\",\"tipsDesc\":\"点击查看详细\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服H5链接客服H5链接\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"consumer_h5_url\"},{\"__config__\":{\"label\":\"客服电话\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"info\":false,\"desc\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1634805887754,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服电话客服电话\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"consumer_hotline\"},{\"__config__\":{\"label\":\"Message\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1644477701613},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Message客服链接\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"consumer_message\"},{\"__config__\":{\"label\":\"客服邮箱\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1644477803104},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服邮箱\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"consumer_email\"},{\"__config__\":{\"label\":\"客服方式\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1644477979588},\"__slot__\":{\"options\":[{\"label\":\"云智服\",\"value\":\"h5\"},{\"label\":\"电话\",\"value\":\"hotline\"},{\"label\":\"Message\",\"value\":\"message\"},{\"label\":\"邮件\",\"value\":\"email\"}]},\"placeholder\":\"请选择客服方式\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":false,\"multiple\":false,\"__vModel__\":\"consumer_type\"}]}','2020-05-21 11:07:21','2022-02-11 18:11:30'),
	(77,'商城基础配置','商城配置-商城基础配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":300,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"退货理由\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":103,\"renderKey\":1644890538190},\"type\":\"textarea\",\"placeholder\":\"请填写退货理由退货理由退货理由\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"95%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"stor_reason\"},{\"__config__\":{\"label\":\"移动端顶部logo图标(127*45)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":300,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":124,\"renderKey\":1595659136385,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"mobile_top_logo\"},{\"__config__\":{\"label\":\"移动端登录页logo(90x90)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":300,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":102,\"renderKey\":1629094835969,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"mobile_login_logo\"},{\"__config__\":{\"label\":\"用户H5默认头像\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":300,\"required\":false,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":103,\"renderKey\":1644568256196,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"h5_avatar\"},{\"__config__\":{\"label\":\"普通商品未支付取消订单时间(单位:小时)\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":300,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":105,\"renderKey\":1590032096481,\"defaultValue\":2,\"tips\":false},\"placeholder\":\"普通商品未支付取消订单时间(单位:小时)\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"order_cancel_time\",\"max\":99999,\"min\":0,\"precision\":0},{\"__config__\":{\"label\":\"网站名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1629086692739,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入网站名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"site_name\"},{\"__config__\":{\"label\":\"SEO标题\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1629087874359,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入SEO标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"seo_title\"},{\"__config__\":{\"label\":\"新闻幻灯片数量上限\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":101,\"renderKey\":1629087608122,\"defaultValue\":1,\"tips\":false},\"placeholder\":\"新闻幻灯片数量上限新闻幻灯片数量上限新闻幻灯片数量上限新闻幻灯片数量上限新闻幻灯片数量上限\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"news_slides_limit\",\"min\":1,\"max\":3}]}','2020-05-16 10:19:37','2022-05-16 16:34:29'),
	(81,'阿里云配置','阿里云配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":150,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"空间域名 Domain\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1590041796581,\"tips\":true,\"tipsIsLink\":true,\"tipsLink\":\"https://doc.crmeb.com/web/java/crmeb_java/701\",\"tipsDesc\":\"地址怎么配置？\",\"defaultValue\":\"http://yourdomain\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本空间域名 Domain空间域名 Domain\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"alUploadUrl\"},{\"__config__\":{\"label\":\"accessKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1590041835433,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本accessKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"alAccessKey\"},{\"__config__\":{\"label\":\"secretKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1590041835651,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本secretKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"alSecretKey\"},{\"__config__\":{\"label\":\"存储空间名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1590041835857,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本存储空间名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"alStorageName\"},{\"__config__\":{\"label\":\"所属地域\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":101,\"renderKey\":1629717898666,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"华北2（北京）｜oss-cn-beijing.aliyuncs.com\",\"value\":\"oss-cn-beijing.aliyuncs.com\"},{\"label\":\"华北 3（张家口）｜oss-cn-zhangjiakou.aliyuncs.com\",\"value\":\"oss-cn-zhangjiakou.aliyuncs.com\"},{\"label\":\"华东1（杭州）｜oss-cn-hangzhou.aliyuncs.com\",\"value\":\"oss-cn-hangzhou.aliyuncs.com\"},{\"label\":\"华东2（上海）｜oss-cn-shanghai.aliyuncs.com\",\"value\":\"oss-cn-shanghai.aliyuncs.com\"},{\"label\":\"华北1（青岛）｜oss-cn-qingdao.aliyuncs.com\",\"value\":\"oss-cn-qingdao.aliyuncs.com\"},{\"label\":\"华北5（呼和浩特）｜oss-cn-huhehaote.aliyuncs.com\",\"value\":\"oss-cn-huhehaote.aliyuncs.com\"},{\"label\":\"华北6（乌兰察布）｜oss-cn-wulanchabu.aliyuncs.com\",\"value\":\"oss-cn-wulanchabu.aliyuncs.com\"},{\"label\":\"华南1（深圳）｜oss-cn-shenzhen.aliyuncs.com\",\"value\":\"oss-cn-shenzhen.aliyuncs.com\"},{\"label\":\"华南2（河源）｜oss-cn-heyuan.aliyuncs.com\",\"value\":\"oss-cn-heyuan.aliyuncs.com\"},{\"label\":\"华南3（广州）｜oss-cn-guangzhou.aliyuncs.com\",\"value\":\"oss-cn-guangzhou.aliyuncs.com\"},{\"label\":\"西南1（成都）｜oss-cn-chengdu.aliyuncs.com\",\"value\":\"oss-cn-chengdu.aliyuncs.com\"},{\"label\":\"华南2（河源）｜oss-cn-heyuan.aliyuncs.com\",\"value\":\"oss-cn-heyuan.aliyuncs.com\"},{\"label\":\"华南3（广州）｜oss-cn-guangzhou.aliyuncs.com\",\"value\":\"oss-cn-guangzhou.aliyuncs.com\"},{\"label\":\"西南1（成都）｜oss-cn-chengdu.aliyuncs.com\",\"value\":\"oss-cn-chengdu.aliyuncs.com\"},{\"label\":\"中国（香港）｜oss-cn-hongkong.aliyuncs.com\",\"value\":\"oss-cn-hongkong.aliyuncs.com\"},{\"label\":\"美国西部1（硅谷）｜oss-us-west-1.aliyuncs.com\",\"value\":\"oss-us-west-1.aliyuncs.com\"},{\"label\":\"美国东部1（弗吉尼亚）｜oss-us-east-1.aliyuncs.com\",\"value\":\"oss-us-east-1.aliyuncs.com\"},{\"label\":\"亚太东南1（新加坡）｜oss-ap-southeast-1.aliyuncs.com\",\"value\":\"oss-ap-southeast-1.aliyuncs.com\"},{\"label\":\"亚太东南2（悉尼）｜oss-ap-southeast-2.aliyuncs.com\",\"value\":\"oss-ap-southeast-2.aliyuncs.com\"},{\"label\":\"亚太东南3（吉隆坡）｜oss-ap-southeast-3.aliyuncs.com\",\"value\":\"oss-ap-southeast-3.aliyuncs.com\"},{\"label\":\"亚太东南5（雅加达）｜oss-ap-southeast-5.aliyuncs.com\",\"value\":\"oss-ap-southeast-5.aliyuncs.com\"},{\"label\":\"亚太东北1（日本）｜oss-ap-northeast-1.aliyuncs.com\",\"value\":\"oss-ap-northeast-1.aliyuncs.com\"},{\"label\":\"亚太南部1（孟买）｜oss-ap-south-1.aliyuncs.com\",\"value\":\"oss-ap-south-1.aliyuncs.com\"},{\"label\":\"欧洲中部1（法兰克福）｜oss-eu-central-1.aliyuncs.com\",\"value\":\"oss-eu-central-1.aliyuncs.com\"},{\"label\":\"英国（伦敦）｜oss-eu-west-1.aliyuncs.com\",\"value\":\"oss-eu-west-1.aliyuncs.com\"},{\"label\":\"中东东部1（迪拜）｜oss-me-east-1.aliyuncs.com\",\"value\":\"oss-me-east-1.aliyuncs.com\"}]},\"placeholder\":\"请选择下拉选择所属地域所属地域所属地域所属地域\",\"style\":{\"width\":\"100%\"},\"clearable\":false,\"disabled\":false,\"filterable\":false,\"multiple\":false,\"__vModel__\":\"alStorageRegion\"}]}','2020-05-21 14:18:12','2021-12-24 11:11:12'),
	(82,'七牛云配置','七牛云配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":150,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"空间域名 Domain\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1590041796581,\"tips\":true,\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2102105\",\"tipsDesc\":\"点击查看详细\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本空间域名 Domain\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"qnUploadUrl\"},{\"__config__\":{\"label\":\"accessKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1590041835433,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本accessKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"qnAccessKey\"},{\"__config__\":{\"label\":\"qnSecretKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1590041835651,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本qnSecretKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"qnSecretKey\"},{\"__config__\":{\"label\":\"存储空间名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1590041835857,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本存储空间名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"qnStorageName\"},{\"__config__\":{\"label\":\"所属地域\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1590041836043,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本所属地域\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"qnStorageRegion\"}]}','2020-05-21 14:18:29','2021-11-02 16:53:27'),
	(83,'腾讯云配置','腾讯云配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":150,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"空间域名 Domain\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1590041796581,\"tips\":true,\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2102106\",\"tipsDesc\":\"点击查看详细\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本空间域名 Domain\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"txUploadUrl\"},{\"__config__\":{\"label\":\"accessKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1590041835433,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本accessKeyaccessKeyaccessKeyaccessKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"txAccessKey\"},{\"__config__\":{\"label\":\"secretKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1590041835651,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本secretKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"txSecretKey\"},{\"__config__\":{\"label\":\"存储空间名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1590041835857,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本存储空间名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"txStorageName\"},{\"__config__\":{\"label\":\"所属地域\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1590041836043,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入单行文本所属地域\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"txStorageRegion\"}]}','2020-05-21 14:18:46','2021-11-02 16:52:54'),
	(89,'个人中心菜单','个人中心菜单','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"菜单名\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"name\",\"placeholder\":\"请输入菜单名菜单名菜单名菜单名菜单名菜单名菜单名\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":11,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"图标(48*48)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":105,\"renderKey\":1596072707659,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"},{\"__config__\":{\"label\":\"h5端链接\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":142,\"renderKey\":1591060899296,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"地址管理\",\"value\":\"/pages/users/user_address_list/index\"},{\"label\":\"会员中心\",\"value\":\"/pages/users/user_vip/index\"},{\"label\":\"砍价记录\",\"value\":\"/pages/activity/bargain/index\"},{\"label\":\"推广中心\",\"value\":\"/pages/users/user_spread_user/index\"},{\"label\":\"我的余额\",\"value\":\"/pages/users/user_money/index\"},{\"label\":\"我的收藏\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"label\":\"优惠券\",\"value\":\"/pages/users/user_coupon/index\"},{\"label\":\"后台订单管理\",\"value\":\"/pages/admin/order/index\"},{\"label\":\"联系客服\",\"value\":\"/pages/service/index\"},{\"label\":\"订单核销\",\"value\":\"/pages/admin/order_cancellation/index\"}]},\"placeholder\":\"请选择h5端链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"url\"},{\"__config__\":{\"label\":\"PC端链接\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":140,\"renderKey\":1591064071606,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"地址管理\",\"value\":\"/pages/users/user_address_list/index\"},{\"label\":\"会员中心\",\"value\":\"/pages/users/user_vip/index\"},{\"label\":\"砍价记录\",\"value\":\"/pages/activity/bargain/index\"},{\"label\":\"推广中心\",\"value\":\"/pages/users/user_spread_user/index\"},{\"label\":\"我的余额\",\"value\":\"/pages/users/user_money/index\"},{\"label\":\"我的收藏\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"label\":\"优惠券\",\"value\":\"/pages/users/user_coupon/index\"},{\"label\":\"后台订单管理\",\"value\":\"/pages/admin/order/index\"},{\"label\":\"联系客服\",\"value\":\"https://yzf.qq.com/xv/web/static/chat/index.html?sign=37ef9b97db2656c32340cde61ce2b56a2176efe72ac7ed421c77607b5c816611ec4775a17c7605b33df1ffe1d22a4ce7464dd07b\"},{\"label\":\"订单核销\",\"value\":\"/pages/admin/order_cancellation/index\"}]},\"placeholder\":\"请输入PC端链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"pc_url\"}]}','2020-06-02 09:24:19','2022-06-02 12:00:34'),
	(90,'个人中心轮播图','个人中心轮播图','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":120,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"图片(690x138)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":102,\"renderKey\":1597289113769,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"},{\"__config__\":{\"label\":\"跳转链接\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":140,\"renderKey\":1591064520353,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入跳转跳转链接跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"url\"}]}','2020-06-02 10:24:06','2021-12-22 16:18:44'),
	(95,'首页banner滚动图','首页banner滚动图','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"标题\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"name\",\"placeholder\":\"请输入标题标题标题标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"跳转链接\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1591065625767,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"url\",\"placeholder\":\"请输入跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"图片(750 x 375)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":130,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":102,\"renderKey\":1597286491734,\"tips\":false,\"tipsDesc\":\"\"},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"}]}','2020-06-02 10:41:29','2021-12-22 16:06:49'),
	(96,'导航模块','导航模块','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":120,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"标题\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"name\",\"placeholder\":\"请输入标题标题标题标题标题标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"图片(90x90)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1597287354047,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"},{\"__config__\":{\"label\":\"跳转链接\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1591080591082,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"商城首页\",\"value\":\"/pages/index/index\"},{\"label\":\"个人推广\",\"value\":\"/pages/user_spread_user/index\"},{\"label\":\"优惠券\",\"value\":\"/pages/users/user_get_coupon/index\"},{\"label\":\"个人中心\",\"value\":\"/pages/user/user\"},{\"label\":\"秒杀列表\",\"value\":\"/pages/activity/goods_seckill/index\"},{\"label\":\"拼团列表页\",\"value\":\"/pages/activity/goods_combination/index\"},{\"label\":\"砍价列表\",\"value\":\"/pages/activity/goods_bargain/index\"},{\"label\":\"分类页面\",\"value\":\"/pages/goods_cate/goods_cate\"},{\"label\":\"地址列表\",\"value\":\"/pages/users/user_address_list/index\"},{\"label\":\"提现页面\",\"value\":\"/pages/user_cash/index\"},{\"label\":\"推广统计\",\"value\":\"/pages/promoter-list/index\"},{\"label\":\"账户金额\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"label\":\"推广二维码页面\",\"value\":\"/pages/promotion-card/promotion-card\"},{\"label\":\"购物车页面\",\"value\":\"/pages/order_addcart/order_addcart\"},{\"label\":\"订单列表页面\",\"value\":\"/pages/users/order_list/index\"},{\"label\":\"文章列表页\",\"value\":\"/pages/news_list/index\"},{\"label\":\"我要签到\",\"value\":\"/pages/users/user_sgin/index\"},{\"label\":\"我的收藏\",\"value\":\"/pages/users/user_goods_collection/index\"}]},\"placeholder\":\"请选择跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"url\"},{\"__config__\":{\"label\":\"底部菜单\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":103,\"renderKey\":1591080806108,\"defaultValue\":1,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":2}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"show\"}]}','2020-06-02 10:41:29','2021-12-22 16:20:21'),
	(97,'首页滚动新闻','首页滚动新闻','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"滚动文字\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"info\",\"placeholder\":\"请输入滚动文字滚动文字\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"跳转链接\",\"showLabel\":true,\"labelWidth\":120,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1591080591082},\"__slot__\":{\"options\":[{\"label\":\"商城首页\",\"value\":\"/pages/index/index\"},{\"label\":\"个人推广\",\"value\":\"/pages/user_spread_user/index\"},{\"label\":\"优惠券\",\"value\":\"/pages/users/user_get_coupon/index\"},{\"label\":\"个人中心\",\"value\":\"/pages/user/user\"},{\"label\":\"秒杀列表\",\"value\":\"/pages/activity/goods_seckill/index\"},{\"label\":\"拼团列表页\",\"value\":\"/pages/activity/goods_combination/index\"},{\"label\":\"砍价列表\",\"value\":\"/pages/activity/goods_bargain/index\"},{\"label\":\"分类页面\",\"value\":\"/pages/goods_cate/goods_cate\"},{\"label\":\"地址列表\",\"value\":\"/pages/users/user_address_list/index\"},{\"label\":\"提现页面\",\"value\":\"/pages/user_cash/index\"},{\"label\":\"推广统计\",\"value\":\"/pages/promoter-list/index\"},{\"label\":\"账户金额\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"label\":\"推广二维码页面\",\"value\":\"/pages/promotion-card/promotion-card\"},{\"label\":\"购物车页面\",\"value\":\"/pages/order_addcart/order_addcart\"},{\"label\":\"订单列表页面\",\"value\":\"/pages/users/order_list/index\"},{\"label\":\"文章列表页\",\"value\":\"/pages/news_list/index\"}]},\"placeholder\":\"请选择跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"url\"},{\"__config__\":{\"label\":\"底部菜单\",\"labelWidth\":120,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":103,\"renderKey\":1591080806108,\"defaultValue\":1},\"__slot__\":{\"options\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":2}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"show\"}]}','2020-06-02 10:41:29','2020-08-13 10:35:02'),
	(98,'首页活动区域图片','首页活动区域图片','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"图片(710*280)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":130,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":102,\"renderKey\":1597286612904},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"image\"},{\"__config__\":{\"label\":\"标题\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"title\",\"placeholder\":\"请输入标题标题标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"简介\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1591081196107},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"info\",\"placeholder\":\"请输入简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"跳转链接\",\"showLabel\":true,\"labelWidth\":130,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1591080591082},\"__slot__\":{\"options\":[{\"label\":\"秒杀列表\",\"value\":\"/pages/activity/goods_seckill/index\"},{\"label\":\"拼团列表页\",\"value\":\"/pages/activity/goods_combination/index\"},{\"label\":\"砍价列表\",\"value\":\"/pages/activity/goods_bargain/index\"}]},\"placeholder\":\"请选择跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"link\"}]}','2020-06-02 10:41:29','2020-08-13 10:44:02'),
	(99,'首页超值爆款','首页超值爆款','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"图片(710*280)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":130,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1597286019454,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"},{\"__config__\":{\"label\":\"标题\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"name\",\"placeholder\":\"请输入标题标题标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"简介\",\"labelWidth\":130,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1591081196107,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"info\",\"placeholder\":\"请输入简介简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"类型\",\"showLabel\":true,\"labelWidth\":130,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1591080591082,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"精品推荐\",\"value\":1},{\"label\":\"热门榜单\",\"value\":2},{\"label\":\"首发新品\",\"value\":3},{\"label\":\"促销单品\",\"value\":4}]},\"placeholder\":\"请选择类型\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"type\"}]}','2020-06-02 10:41:29','2021-12-23 16:00:42'),
	(100,'首页文字配置','首页文字配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"快速选择简介\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"fast_info\",\"placeholder\":\"请输入快速选择简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"精品推荐简介\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1591081579041},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"bast_info\",\"placeholder\":\"请输入精品推荐简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"首发新品简介\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1591081598503},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"first_info\",\"placeholder\":\"请输入首发新品简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"促销单品简介\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1591081599048},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"sales_info\",\"placeholder\":\"请输入促销单品简介\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false}]}','2020-06-02 15:07:11','2020-06-02 15:07:11'),
	(101,'首页精品推荐benner图','首页精品推荐benner图','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"图片\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1597285983911},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"img\"},{\"__config__\":{\"label\":\"描述\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1591083877308},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入描述\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"comment\"},{\"__config__\":{\"label\":\"跳转链接\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1591083894228},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入跳转链接\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"link\"}]}','2020-06-02 15:45:01','2020-08-13 10:33:18'),
	(102,'热门搜索','热门搜索','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"标签\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"title\",\"placeholder\":\"请输入标签\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":11,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false}]}','2020-06-03 11:34:29','2020-06-03 11:34:29'),
	(105,'模板消息','模板消息','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"模板编号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"tempKey\",\"placeholder\":\"请输入模板编号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"模板ID\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":106,\"renderKey\":1592281492122},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入模板ID\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"tempId\"},{\"__config__\":{\"label\":\"模板名\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1592281490129},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入模板名\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"name\"},{\"__config__\":{\"label\":\"回复内容\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":104,\"renderKey\":1592281487212},\"type\":\"textarea\",\"placeholder\":\"请输入回复内容\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"content\"},{\"__config__\":{\"label\":\"状态\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":109,\"renderKey\":1592289933100,\"defaultValue\":0},\"__slot__\":{\"options\":[{\"label\":\"开启\",\"value\":1},{\"label\":\"关闭\",\"value\":0}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"status\"}]}','2020-06-16 12:28:38','2020-12-04 16:49:38'),
	(106,'拒绝退款','拒绝退款','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"退款单号\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"refundOrderNo\",\"placeholder\":\"请输入拒绝退款单号退款单号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":true,\"disabled\":false},{\"__config__\":{\"label\":\"不退款原因\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":112,\"renderKey\":1592451273684,\"tips\":false},\"type\":\"textarea\",\"placeholder\":\"请输入不退款原因\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"reason\"}]}','2020-06-18 11:36:28','2022-03-30 16:03:23'),
	(107,'立即退款','立即退款','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"退款单号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"orderId\",\"placeholder\":\"请输入退款单号退款单号退款单号退款单号退款单号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":true,\"disabled\":false},{\"__config__\":{\"label\":\"退款金额\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":110,\"renderKey\":1592452495718,\"tips\":false},\"placeholder\":\"退款金额\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":true,\"__vModel__\":\"amount\",\"min\":0}]}','2020-06-18 11:57:03','2022-02-25 17:59:46'),
	(108,'文件上传-基础配置','文件上传-本地配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"本地图片域名\",\"labelWidth\":270,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1592476026393,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入本地图片域名\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"localUploadUrl\"},{\"__config__\":{\"label\":\"允许上传图片后缀\",\"labelWidth\":270,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1598344152903,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"允许上传图片后缀，多个英文逗号分割允许上传图片后缀\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"image_ext_str\"},{\"__config__\":{\"label\":\"允许上传最大图片(单位 M，最大值50 )\",\"labelWidth\":270,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"regList\":[],\"formId\":104,\"renderKey\":1598344206314,\"tips\":false},\"placeholder\":\"单位 M允许上传最大图片(单位 M，最大值50 )\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"image_max_size\",\"min\":1,\"max\":50},{\"__config__\":{\"label\":\"允许上传文件后缀\",\"labelWidth\":270,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":106,\"renderKey\":1598344273484,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"，多个英文逗号分割\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"file_ext_str\"},{\"__config__\":{\"label\":\"允许上传最大文件(单位 M，最大值500 )\",\"labelWidth\":270,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"regList\":[],\"formId\":107,\"renderKey\":1598344275586,\"tips\":false},\"placeholder\":\"单位 M允许上传最大文件(单位 M，最大值500 )\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"file_max_size\",\"min\":1,\"max\":500},{\"__config__\":{\"label\":\"文件存储\",\"showLabel\":true,\"labelWidth\":270,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1599545811870,\"defaultValue\":1,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"本地\",\"value\":1},{\"label\":\"七牛云\",\"value\":2},{\"label\":\"阿里云\",\"value\":3},{\"label\":\"腾讯云\",\"value\":4}]},\"placeholder\":\"请选择文件存储文件存储\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"disabled\":false,\"filterable\":true,\"multiple\":false,\"__vModel__\":\"uploadType\"},{\"__config__\":{\"label\":\"文件是否保存本地（云存储）\",\"labelWidth\":270,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":102,\"renderKey\":1629358181244,\"defaultValue\":1,\"tips\":false},\"__slot__\":{\"options\":[{\"label\":\"保存\",\"value\":1},{\"label\":\"不保存\",\"value\":0}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"file_is_save\"}]}','2020-06-18 18:28:13','2021-12-09 14:33:45'),
	(110,'短信模板消息','短信模板消息','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"模板名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":140,\"renderKey\":1595402251597},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入模板名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"title\"},{\"__config__\":{\"label\":\"模板内容\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":141,\"renderKey\":1595402320867,\"defaultValue\":\"您的验证码是：{$code}，有效期为{$time}分钟。如非本人操作，可不用理会。模板中的{$code}和{$time}需要替换成对应的变量，请开发者知晓。修改此项无效！\"},\"type\":\"textarea\",\"placeholder\":\"请输入模板内容\",\"autosize\":{\"minRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"content\"},{\"__config__\":{\"label\":\"模板类型\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":142,\"renderKey\":1595402411016,\"defaultValue\":1},\"__slot__\":{\"options\":[{\"label\":\"验证码\",\"value\":1},{\"label\":\"通知\",\"value\":2},{\"label\":\"推广\",\"value\":3}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"type\"}]}','2020-07-22 15:21:26','2020-12-01 11:56:33'),
	(111,'短信设置','短信设置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":300,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":16,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"阿里云短信Key Id\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":16,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1644982308964},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入阿里云短信Key Id\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"aliyun_sms_key_id\"},{\"__config__\":{\"label\":\"阿里云短信Key Secret\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":16,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1644982309985},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入阿里云短信Key Secret\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"aliyun_sms_key_secret\"},{\"__config__\":{\"label\":\"阿里云短信通用签名\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":16,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1644982314862},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入阿里云短信通用签名\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"aliyun_sms_sign_name\"},{\"__config__\":{\"label\":\"验证码有效时间 (分钟)\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"required\":true,\"layout\":\"colFormItem\",\"span\":16,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"regList\":[],\"formId\":103,\"renderKey\":1598497111542,\"defaultValue\":1,\"tips\":false},\"placeholder\":\"请输入验证码有效时间 (分钟)\",\"step\":1,\"step-strictly\":true,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"sms_code_expire\",\"min\":1}]}','2020-07-22 17:00:03','2022-02-16 11:34:32'),
	(114,'后台登录页轮播图','后台登录页轮播图','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":130,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"轮播图(286x420)\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1596017879033,\"tips\":false},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"pic\"}]}','2020-07-29 18:15:46','2021-12-22 16:23:15'),
	(115,'订单详情状态图','订单详情状态图','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"订单状态\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"orderStatus\",\"placeholder\":\"0=未支付,1=待发货,2=待收货,3=待评价,4=已完成订单状态订单状态订单状态\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"透明动图\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1640163223626},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"image\"}]}','2020-07-29 19:40:23','2021-12-22 17:29:12'),
	(122,'99api','99api','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"ApiKey\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1599538285999,\"tips\":true,\"tipsDesc\":\"点击查看详细\",\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2103903\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入ApiKey\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"importProductToken\"}]}','2020-09-08 12:17:06','2021-11-02 16:40:49'),
	(125,'银行卡提现申请','银行卡提现申请','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"姓名\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"defaultValue\":\"\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"realName\",\"placeholder\":\"请输入姓名\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"15\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"提现金额\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1603703481683,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入提现金额\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":true,\"disabled\":false,\"__vModel__\":\"extractPrice\"},{\"__config__\":{\"label\":\"银行卡号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1603703520389,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入银行卡号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"bankCode\"},{\"__config__\":{\"label\":\"开户行\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1603703557229,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入开户行\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"bankName\"},{\"__config__\":{\"label\":\"备注\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":false,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":104,\"renderKey\":1603703559381,\"tips\":false},\"type\":\"textarea\",\"placeholder\":\"请输入备注\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"mark\"}]}','2020-10-26 17:14:21','2021-10-30 11:46:11'),
	(128,'物流查询','物流查询','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"物流App Key\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1607574358258,\"tips\":false,\"tipsDesc\":\"购买链接\",\"tipsIsLink\":false,\"tipsLink\":\"https://market.aliyun.com/products/57126001/cmapi021863.html?userCode=dligum2z快递查询密钥\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"清输入物流App Key\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"logistics_app_key\"},{\"__config__\":{\"label\":\"物流App Secret\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1644992391177},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入物流App Secret\",\"style\":{\"width\":\"80%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"logistics_app_secret\"}]}','2020-12-10 12:11:38','2022-02-16 14:21:44'),
	(129,'电子面单','电子面单','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"电子面单是否开启\",\"labelWidth\":150,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":101,\"renderKey\":1607573397849,\"defaultValue\":1,\"tips\":true,\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2098999\",\"tipsDesc\":\"点击查看详细\"},\"__slot__\":{\"options\":[{\"label\":\"打开\",\"value\":1},{\"label\":\"关闭\",\"value\":2}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"config_export_open\"},{\"__config__\":{\"label\":\"发货人姓名\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1607573602374,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"快递面单发货人姓名\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"el-icon-warning-outline\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"config_export_to_name\"},{\"__config__\":{\"label\":\"发货人电话\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1607573608679,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"快递面单发货人电话\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"config_export_to_tel\"},{\"__config__\":{\"label\":\"发货人详细地址\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1607573609130,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"快递面单发货人详细地址\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"config_export_to_address\"},{\"__config__\":{\"label\":\"电子面单打印机编号\",\"labelWidth\":150,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1607573609617,\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请购买快递100电子面单打印机，淘宝地址：https://m.tb.cn/h.437NvI0 官网：https://www.kuaidi100.com/cloud/print/cloudprinterSecond.shtml\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"el-icon-warning-outline\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"config_export_siid\"}]}','2020-12-10 12:18:18','2021-11-02 16:55:15'),
	(130,'充值退款','充值退款','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"编号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1608271559043},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入编号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":true,\"__vModel__\":\"id\"},{\"__config__\":{\"label\":\"退款单号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"orderId\",\"placeholder\":\"请输入退款单号退款单号退款单号\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":true,\"disabled\":false},{\"__config__\":{\"label\":\"状态\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":101,\"renderKey\":1608263757741,\"defaultValue\":1},\"__slot__\":{\"options\":[{\"label\":\"本金+赠送\",\"value\":2},{\"label\":\"仅本金\",\"value\":1}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"type\"}]}','2020-12-18 11:56:49','2020-12-18 14:49:17'),
	(133,'首页配置','首页配置','{\"formRef\":\"stivepeimEdited\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":140,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"精品推荐简介\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":103,\"renderKey\":1591148546642},\"type\":\"textarea\",\"placeholder\":\"请输入精品推荐简介\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"bastInfo\"},{\"__config__\":{\"label\":\"首发新品简介\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":104,\"renderKey\":1591148566944},\"type\":\"textarea\",\"placeholder\":\"请输入首发新品简介\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"firstInfo\"},{\"__config__\":{\"label\":\"促销单品简介\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":105,\"renderKey\":1591148567412},\"type\":\"textarea\",\"placeholder\":\"请输入促销单品简介\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"salesInfo\"},{\"__config__\":{\"label\":\"热门推荐简介\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":102,\"renderKey\":1597821020375},\"type\":\"textarea\",\"placeholder\":\"请输入热门推荐简介\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"100%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"hotInfo\"}]}','2021-02-06 11:19:43','2021-02-06 11:19:43'),
	(139,'商城配置','商城配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"Api地址【支付回调】\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[]},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"api_url\",\"placeholder\":\"webSiet网站地址Api地址【支付回调】Api地址【支付回调】\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"主页左上角logo[后台]\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":124,\"renderKey\":1595658064081},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"picture-card\",\"multiple\":false,\"__vModel__\":\"site_logo_lefttop\"},{\"__config__\":{\"label\":\"主页左上角缩回菜单logo[后台]\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":127,\"renderKey\":1595658695317},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"site_logo_square\"},{\"__config__\":{\"label\":\"登录页logo[后台]\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1612510257294},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"picture-card\",\"multiple\":false,\"__vModel__\":\"site_logo_login\"},{\"__config__\":{\"label\":\"登录页背景图\",\"tag\":\"self-upload\",\"tagIcon\":\"upload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"span\":24,\"showTip\":false,\"buttonText\":\"点击上传\",\"regList\":[],\"changeTag\":true,\"fileSize\":2,\"sizeUnit\":\"MB\",\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":152,\"renderKey\":1596017451389},\"__slot__\":{\"list-type\":true},\"action\":\"https://jsonplaceholder.typicode.com/posts/\",\"disabled\":true,\"accept\":\"\",\"name\":\"file\",\"auto-upload\":true,\"list-type\":\"text\",\"multiple\":false,\"__vModel__\":\"admin_login_bg_pic\"}]}','2021-08-19 15:15:38','2021-08-19 15:15:38'),
	(140,'导航下拉菜单','导航下拉菜单','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"标题\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[{\"pattern\":\"/^1(3|4|5|7|8|9)\\\\d{9}$/\",\"message\":\"手机号格式错误\"}],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"select_nav_name\",\"placeholder\":\"请输入标题标题标题\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"el-icon-mobile\",\"suffix-icon\":\"\",\"maxlength\":11,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false}]}','2021-09-08 10:45:37','2021-10-29 18:36:06'),
	(143,'易联云打印设置','易联云打印设置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"易联云应用ID\",\"labelWidth\":180,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":true,\"tipsDesc\":\"易联云 官网获取\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"ylyprint_app_id\",\"placeholder\":\"请输入易联云应用ID易联云应用ID易联云应用ID易联云应用ID易联云应用ID易联云应用ID易联云应用ID易联云应用ID\",\"style\":{\"width\":\"97%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":11,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"易联云应用密钥\",\"labelWidth\":180,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false,\"formId\":101,\"renderKey\":1638000310366},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"ylyprint_app_secret\",\"placeholder\":\"请输入易联云应用密钥\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"易联云打印机设备码\",\"labelWidth\":180,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false,\"formId\":102,\"renderKey\":1638000357901},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"ylyprint_app_machine_code\",\"placeholder\":\"请输入易联云打印机设备码\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"易联云打印机设备密钥\",\"labelWidth\":180,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false,\"formId\":103,\"renderKey\":1638000464706},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"ylyprint_app_machine_msign\",\"placeholder\":\"请输入易联云打印机设备密钥\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"支付成功自动打印开关\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":180,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":101,\"renderKey\":1638002536348},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"ylyprint_auto_status\"},{\"__config__\":{\"label\":\"打印开关\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'1\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":180,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":102,\"renderKey\":1638156858490},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"ylyprint_status\"}]}','2021-11-27 16:09:51','2021-12-10 12:17:01'),
	(144,'Meta(Facebook)','Meta(Facebook)','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":140,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"Facebook APPID\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"facebook_appid\",\"placeholder\":\"请输入Facebook APPID\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"开启\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":null,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":102,\"renderKey\":1642837619938},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"facebook_open\"}]}','2022-01-22 15:48:59','2022-02-11 18:08:19'),
	(145,'Twitter(推特)','Twitter(推特)','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":150,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"Consumer Key\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"twitter_consumer_key\",\"placeholder\":\"请输入Twitter开发者平台申请的Consumer Key\",\"style\":{\"width\":\"90%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"Consumer Secret\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1642837917797},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Twitter开发者平台申请的Consumer Secret\",\"style\":{\"width\":\"90%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"twitter_consumer_secret\"},{\"__config__\":{\"label\":\"开启\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":null,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":102,\"renderKey\":1642838172360},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"twitter_open\"}]}','2022-01-22 15:57:16','2022-02-11 17:15:18'),
	(146,'Goggle(谷歌)','Goggle(谷歌)','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":200,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"Google Client Id\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1642838694372},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Google Client Id\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"google_client_id\"},{\"__config__\":{\"label\":\"开启\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":null,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":102,\"renderKey\":1642838744686},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"google_open\"}]}','2022-01-22 16:06:18','2022-02-11 17:29:32'),
	(147,'Paypal支付','Paypal支付','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":120,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"Client Id\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1642838898948},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Client IdClient Id\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"paypal_client_id\"},{\"__config__\":{\"label\":\"Client Secret\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1642839088119},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Client SecretClient SecretClient SecretClient Secret\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"paypal_client_secret\"},{\"__config__\":{\"label\":\"mode\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":102,\"renderKey\":1644980867602,\"defaultValue\":\"sandbox\"},\"__slot__\":{\"options\":[{\"label\":\"sandbox\",\"value\":\"sandbox\"},{\"label\":\"live\",\"value\":\"live\"}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"paypal_mode\"},{\"__config__\":{\"label\":\"Paypal开关\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":null,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":102,\"renderKey\":1654507345321},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"paypal_switch\"}]}','2022-01-22 16:12:48','2022-06-09 11:18:27'),
	(148,'匿名下单','匿名下单','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"匿名下单\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":null,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":101,\"renderKey\":1644575380561},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"visitor_open\"}]}','2022-02-11 18:30:13','2022-06-09 11:16:22'),
	(151,'商户分类','商户分类','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"分类名称\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"name\",\"placeholder\":\"请输入分类名称\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"el-icon-mobile\",\"suffix-icon\":\"\",\"maxlength\":11,\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"手续费(%)\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":101,\"renderKey\":1646879946357,\"defaultValue\":0},\"placeholder\":\"手续费(%)\",\"step\":1,\"step-strictly\":true,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"handlingFee\",\"min\":0,\"max\":100}]}','2022-03-10 10:40:08','2022-05-13 18:55:01'),
	(152,'店铺类型','店铺类型','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":110,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":18,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"店铺类型\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":18,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1646895171066},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入店铺类型\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"name\"},{\"__config__\":{\"label\":\"店铺类型要求\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":18,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":104,\"renderKey\":1646895357761},\"type\":\"textarea\",\"placeholder\":\"请输入店铺类型要求\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"95%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"info\"}]}','2022-03-10 14:57:59','2022-04-27 14:52:52'),
	(153,'修改商户密码','修改商户密码','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"密码\",\"showLabel\":true,\"labelWidth\":null,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"password\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":114,\"renderKey\":1646986639692,\"defaultValue\":\"\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入密码\",\"show-password\":true,\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"newPassword\"},{\"__config__\":{\"label\":\"确认密码\",\"showLabel\":true,\"labelWidth\":null,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"password\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":113,\"renderKey\":1646986633094,\"defaultValue\":\"\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入确认密码\",\"show-password\":true,\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"passwordAgain\"}]}','2022-03-11 16:18:33','2022-03-11 16:18:33'),
	(154,'修改复制商品数量','修改复制商品数量','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":10,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"复制次数\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":false,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":10,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1646991510381},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入复制次数\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":true,\"__vModel__\":\"copyProductNum\"},{\"__config__\":{\"label\":\"修改类型\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":102,\"renderKey\":1646991528364,\"defaultValue\":\"add\"},\"__slot__\":{\"options\":[{\"label\":\"增加\",\"value\":\"add\"},{\"label\":\"减少\",\"value\":\"sub\"}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"type\"},{\"__config__\":{\"label\":\"修改数量\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":103,\"renderKey\":1646991546233},\"placeholder\":\"修改数量\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"num\",\"min\":0,\"max\":9999}]}','2022-03-11 17:41:18','2022-05-14 15:36:00'),
	(155,'添加服务条款','添加服务条款','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":110,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"服务条款\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":101,\"renderKey\":1647230988327},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入服务条款\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"name\"},{\"__config__\":{\"label\":\"服务内容描述\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-input\",\"tagIcon\":\"textarea\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"formId\":102,\"renderKey\":1647231011480},\"type\":\"textarea\",\"placeholder\":\"请输入服务内容描述\",\"autosize\":{\"minRows\":4,\"maxRows\":4},\"style\":{\"width\":\"95%\"},\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"content\"},{\"__config__\":{\"label\":\"服务条款图标(100*100px)\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":103,\"renderKey\":1647231714586},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"icon\"},{\"__config__\":{\"label\":\"排序\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":104,\"renderKey\":1647231749284,\"defaultValue\":0},\"placeholder\":\"排序\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"sort\",\"min\":0,\"max\":99999}]}','2022-03-14 12:23:03','2022-05-17 15:55:23'),
	(156,'虚拟销量','虚拟销量','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":120,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":10,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"现有虚拟销量\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":false,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":10,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1647489084443,\"defaultValue\":\"\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入现有虚拟销量\",\"style\":{\"width\":\"95%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":true,\"__vModel__\":\"ficti\"},{\"__config__\":{\"label\":\"修改类型\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":102,\"renderKey\":1647317130950,\"defaultValue\":\"add\"},\"__slot__\":{\"options\":[{\"label\":\"增加\",\"value\":\"add\"},{\"label\":\"减少\",\"value\":\"sub\"}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"type\"},{\"__config__\":{\"label\":\"修改虚拟销量数\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":101,\"renderKey\":1647317126714,\"defaultValue\":0},\"placeholder\":\"修改虚拟销量数\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"num\",\"min\":0,\"max\":99999}]}','2022-03-15 12:07:54','2022-05-17 15:18:01'),
	(157,'店铺配置','店铺配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":150,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\" 商户     主头像     （90*90 px）\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":114,\"renderKey\":1650619545531},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"avatar\"},{\"__config__\":{\"label\":\" 移动端商户背景图（375*180 px）\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":116,\"renderKey\":1650619559395},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"backImage\"},{\"__config__\":{\"label\":\"移动端商户街背景图（355*78 px）\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":115,\"renderKey\":1650619557080},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"streetBackImage\"},{\"__config__\":{\"label\":\"pc端 商户背景图 （1200 *180 px）\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1654743072224},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pcBackImage\"},{\"__config__\":{\"label\":\"pc端 商户轮播图 （1200*340 px）\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":102,\"renderKey\":1654745345193},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":true,\"__vModel__\":\"pcBanner\"},{\"__config__\":{\"label\":\"商户地址\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":111,\"renderKey\":1649237041964},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入商户地址\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"address\"},{\"__config__\":{\"label\":\"商户简介\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":112,\"renderKey\":1649237091405},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入商户简介\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"intro\"},{\"__config__\":{\"label\":\"商户关键字\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":false,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":113,\"renderKey\":1649237128115},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入商户关键字\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"keywords\"},{\"__config__\":{\"label\":\"商户电话\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":117,\"renderKey\":1650619756125},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入商户电话\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"phone\"},{\"__config__\":{\"label\":\"警戒库存\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":true,\"tipsDesc\":\"设置商品的警戒库存\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":103,\"renderKey\":1648454702884,\"defaultValue\":0},\"placeholder\":\"警戒库存\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"alertStock\"},{\"__config__\":{\"label\":\"客服方式\",\"showLabel\":true,\"labelWidth\":null,\"tag\":\"el-select\",\"tagIcon\":\"select\",\"layout\":\"colFormItem\",\"span\":24,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/select\",\"formId\":102,\"renderKey\":1648455695237,\"defaultValue\":\"云智服\"},\"__slot__\":{\"options\":[{\"label\":\"云智服\",\"value\":\"H5\"},{\"label\":\"电话\",\"value\":\"phone\"},{\"label\":\"Message\",\"value\":\"message\"},{\"label\":\"邮件\",\"value\":\"email\"}]},\"placeholder\":\"请选择客服方式客服方式\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"disabled\":false,\"filterable\":false,\"multiple\":false,\"__vModel__\":\"serviceType\"},{\"__config__\":{\"label\":\"客服H5链接\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":false,\"tips\":false,\"tipsDesc\":\"点击查看详细\",\"tipsIsLink\":true,\"tipsLink\":\"http://help.crmeb.net/crmeb_java/2322225\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1648455799280},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服H5链接\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"serviceLink\"},{\"__config__\":{\"label\":\"客服电话\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[{\"pattern\":\"/^[^\\\\u4e00-\\\\u9fa5]+$/\",\"message\":\"请输入正确的客服电话\"}],\"formId\":104,\"renderKey\":1648455812417,\"defaultValue\":\"\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服电话\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"servicePhone\"},{\"__config__\":{\"label\":\"Message\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":105,\"renderKey\":1648455834488},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入Message\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"serviceMessage\"},{\"__config__\":{\"label\":\"客服邮箱\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":false,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[{\"pattern\":\"/^([a-zA-Z\\\\d])(\\\\w|\\\\-)+@[a-zA-Z\\\\d]+\\\\.[a-zA-Z]{2,4}$/\",\"message\":\"请输入正确的邮箱地址\"}],\"formId\":106,\"renderKey\":1648455843696,\"defaultValue\":\"588888888888\"},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入客服邮箱\",\"style\":{\"width\":\"40%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"serviceEmail\"}]}','2022-03-28 16:16:02','2022-06-09 11:30:37'),
	(158,'转账信息','转账信息','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":24,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"转账类型\",\"labelWidth\":null,\"showLabel\":true,\"tag\":\"el-radio-group\",\"tagIcon\":\"radio\",\"changeTag\":true,\"layout\":\"colFormItem\",\"span\":24,\"optionType\":\"default\",\"regList\":[],\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"border\":false,\"document\":\"https://element.eleme.cn/#/zh-CN/component/radio\",\"formId\":101,\"renderKey\":1649246410301,\"defaultValue\":\"bank\"},\"__slot__\":{\"options\":[{\"label\":\"银行卡\",\"value\":\"bank\"}]},\"style\":{},\"size\":\"medium\",\"disabled\":false,\"__vModel__\":\"transferType\"},{\"__config__\":{\"label\":\"姓名\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":102,\"renderKey\":1649246483051},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入姓名\",\"style\":{\"width\":\"50%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"transferName\"},{\"__config__\":{\"label\":\"开户银行\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":103,\"renderKey\":1649246483883},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入开户银行\",\"style\":{\"width\":\"50%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":null,\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"transferBank\"},{\"__config__\":{\"label\":\"银行卡号\",\"labelWidth\":null,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"formId\":104,\"renderKey\":1649246604188},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"placeholder\":\"请输入银行卡号\",\"style\":{\"width\":\"50%\"},\"clearable\":true,\"prefix-icon\":\"\",\"suffix-icon\":\"\",\"maxlength\":\"30\",\"show-word-limit\":false,\"readonly\":false,\"disabled\":false,\"__vModel__\":\"transferBankCard\"}]}','2022-04-06 20:05:17','2022-05-14 10:45:31'),
	(159,'转账设置','转账设置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":200,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"商户保证金额\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":true,\"tipsDesc\":\"指商户的余额至少大于该金额部分，才可以转账，设置为0时默认商户余额可以全部转账\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":101,\"renderKey\":1649381945392,\"defaultValue\":999999},\"placeholder\":\"商户保证金额\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"guaranteedAmount\",\"min\":0,\"precision\":2,\"max\":999999},{\"__config__\":{\"label\":\"商户每笔最小转账额度\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":true,\"tipsDesc\":\"指商户的每次申请转账最小的金额；设置为0时默认不限制最小额度\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":102,\"renderKey\":1649381946440},\"placeholder\":\"商户每笔最小转账额度\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"transferMinAmount\",\"min\":0,\"precision\":2,\"max\":999999},{\"__config__\":{\"label\":\"商户每笔最高转账额度\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":true,\"tipsDesc\":\"商户每次转账申请的最高额度，设置0时默认不限制\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":103,\"renderKey\":1649381946840},\"placeholder\":\"商户每笔最高转账额度\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"transferMaxAmount\",\"min\":0,\"precision\":2,\"max\":999999}]}','2022-04-08 09:47:17','2022-05-14 10:45:18'),
	(160,'商品排序','商品排序','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"排序\",\"showLabel\":true,\"changeTag\":true,\"labelWidth\":null,\"tag\":\"el-input-number\",\"tagIcon\":\"number\",\"span\":24,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"document\":\"https://element.eleme.cn/#/zh-CN/component/input-number\",\"formId\":101,\"renderKey\":1650787971375,\"defaultValue\":0},\"placeholder\":\"排序\",\"step\":1,\"step-strictly\":false,\"controls-position\":\"\",\"disabled\":false,\"__vModel__\":\"rank\",\"min\":0,\"max\":9999}]}','2022-04-24 16:13:32','2022-04-24 16:13:32'),
	(161,'店铺街','店铺街','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"封面图\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":null,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1651730633716},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pic\"}]}','2022-05-05 14:04:37','2022-05-05 18:32:43'),
	(162,'PC首页Banner','PC首页Banner','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"PC首页Banner\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":150,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1653297265618},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pic\"}]}','2022-05-23 17:19:00','2022-05-23 17:23:21'),
	(163,'PC商城配置','PC商城配置','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"首页推荐商品竖图\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":200,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1653298547907},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pc_home_recommend_image\"},{\"__config__\":{\"label\":\"店铺街头图\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":200,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1653299373101},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pc_shop_street_header_image\"},{\"__config__\":{\"label\":\"顶部logo图标\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":200,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1653299856936},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pc_top_logo\"},{\"__config__\":{\"label\":\"PC登录页左侧展示图\",\"tag\":\"self-upload\",\"tagIcon\":\"selfUpload\",\"layout\":\"colFormItem\",\"defaultValue\":null,\"showLabel\":true,\"labelWidth\":200,\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"span\":24,\"showTip\":false,\"buttonText\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/upload\",\"formId\":101,\"renderKey\":1653300230954},\"__slot__\":{\"list-type\":true},\"disabled\":true,\"accept\":\"image\",\"name\":\"file\",\"multiple\":false,\"__vModel__\":\"pc_login_left_image\"}]}','2022-05-23 17:40:09','2022-06-10 10:27:17'),
	(164,'Stripe支付','Stripe支付','{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"apiKey\",\"labelWidth\":120,\"showLabel\":true,\"changeTag\":true,\"tag\":\"el-input\",\"tagIcon\":\"input\",\"required\":true,\"layout\":\"colFormItem\",\"span\":24,\"document\":\"https://element.eleme.cn/#/zh-CN/component/input\",\"regList\":[],\"tips\":false},\"__slot__\":{\"prepend\":\"\",\"append\":\"\"},\"__vModel__\":\"stripe_api_key\",\"placeholder\":\"请输入apiKeyapiKey\",\"style\":{\"width\":\"100%\"},\"clearable\":true,\"prefix-icon\":\"el-icon-mobile\",\"suffix-icon\":\"\",\"maxlength\":\"255\",\"show-word-limit\":true,\"readonly\":false,\"disabled\":false},{\"__config__\":{\"label\":\"Stripe开关\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":120,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":103,\"renderKey\":1654507754350},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"stripe_switch\"}]}','2022-06-06 17:30:37','2022-06-09 11:16:50'),
	(165,'微信支付', '微信支付', '{\"formRef\":\"elForm\",\"formModel\":\"formData\",\"size\":\"medium\",\"labelPosition\":\"right\",\"labelWidth\":100,\"formRules\":\"rules\",\"gutter\":15,\"disabled\":false,\"span\":24,\"formBtns\":true,\"fields\":[{\"__config__\":{\"label\":\"微信支付开关\",\"tag\":\"el-switch\",\"tagIcon\":\"switch\",\"defaultValue\":\"\'0\'\",\"span\":24,\"showLabel\":true,\"labelWidth\":150,\"layout\":\"colFormItem\",\"required\":true,\"tips\":false,\"tipsDesc\":\"\",\"tipsIsLink\":false,\"tipsLink\":\"\",\"regList\":[],\"changeTag\":true,\"document\":\"https://element.eleme.cn/#/zh-CN/component/switch\",\"formId\":101,\"renderKey\":1691373475708},\"style\":{},\"disabled\":false,\"active-text\":\"\",\"inactive-text\":\"\",\"active-color\":null,\"inactive-color\":null,\"active-value\":\"\'1\'\",\"inactive-value\":\"\'0\'\",\"__vModel__\":\"wechat_pay_switch\"}]}', '2023-08-07 09:59:04', '2023-08-07 09:59:04');

/*!40000 ALTER TABLE `eb_system_form_temp` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_group`;

CREATE TABLE `eb_system_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组合数据ID',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '数据组名称',
  `info` varchar(256) NOT NULL DEFAULT '' COMMENT '简介',
  `form_id` int(11) NOT NULL DEFAULT '0' COMMENT 'form 表单 id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='组合数据表';

LOCK TABLES `eb_system_group` WRITE;
/*!40000 ALTER TABLE `eb_system_group` DISABLE KEYS */;

INSERT INTO `eb_system_group` (`id`, `name`, `info`, `form_id`, `create_time`, `update_time`)
VALUES
	(1,'移动端_首页中部推荐banner图','移动端_首页中部推荐banner图',95,'2020-05-15 10:38:22','2021-02-24 18:24:35'),
	(2,'移动端_首页banner滚动图','移动端_首页banner滚动图',95,'2020-05-15 10:38:22','2021-02-24 18:24:06'),
	(3,'移动端_订单详情状态图','移动端_订单详情状态图',115,'2020-05-15 10:38:22','2021-02-24 18:23:25'),
	(4,'移动端_个人中心功能菜单','移动端_个人中心功能菜单',89,'2020-05-15 10:38:22','2021-02-24 18:22:22'),
	(5,'移动端_个人中心轮播图','移动端_个人中心轮播图',90,'2020-06-02 10:25:03','2021-02-24 17:38:12'),
	(6,'移动端_首页导航','移动端_首页导航',96,'2020-06-02 14:54:41','2021-02-24 17:37:59'),
	(7,'移动端_首页滚动新闻_二期配置','移动端_首页滚动新闻_二期配置',97,'2020-06-02 18:00:47','2021-02-24 18:08:45'),
	(8,'移动端_热门搜索','移动端_热门搜索',102,'2020-06-03 11:34:42','2021-02-24 16:43:36'),
	(9,'平台管理端_登录页轮播图','平台管理端_登录页轮播图',114,'2020-07-29 18:16:11','2022-05-07 16:37:53'),
	(10,'移动端_店铺街','移动端_店铺街、商品排行入口',161,'2022-05-05 14:05:35','2022-05-05 14:05:35'),
	(11,'PC_首页_Banner','PC_首页_Banner',162,'2022-05-23 17:19:39','2022-05-23 17:19:39');

/*!40000 ALTER TABLE `eb_system_group` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_group_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_group_data`;

CREATE TABLE `eb_system_group_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组合数据详情ID',
  `gid` int(11) NOT NULL DEFAULT '0' COMMENT '对应的数据组id',
  `value` text NOT NULL COMMENT '数据组对应的数据值（json数据）',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（1：开启；2：关闭；）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='组合数据详情表';

LOCK TABLES `eb_system_group_data` WRITE;
/*!40000 ALTER TABLE `eb_system_group_data` DISABLE KEYS */;

INSERT INTO `eb_system_group_data` (`id`, `gid`, `value`, `sort`, `status`, `create_time`, `update_time`)
VALUES
	(1,1,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"精品推荐\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"#\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/df6214011a2f4683b1b5feab362f0d882pneycevbh.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"1\"}],\"id\":95,\"sort\":1,\"status\":true}',1,1,'2020-08-13 12:34:52','2022-06-18 15:46:23'),
	(2,1,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"BANNER\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"#\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/b2d29d1bf4394e24985ce5fa3770b2f2uqqytv1paq.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"2\"}],\"id\":95,\"sort\":2,\"status\":true}',2,1,'2020-08-13 12:41:15','2022-06-18 15:47:10'),
	(3,1,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"广告\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"#\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/825310b0a5ad4abf9a8484c616e992c9m4jdeyzrz6.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"3\"}],\"id\":95,\"sort\":1,\"status\":true}',1,1,'2021-06-03 12:20:13','2022-06-18 15:47:05'),
	(8,3,'{\"fields\":[{\"name\":\"orderStatus\",\"title\":\"orderStatus\",\"value\":\"0\"},{\"name\":\"image\",\"title\":\"image\",\"value\":\"crmebimage/public/product/2022/06/18/8d3e31140c6d4f9683edbd2d333e462fl3td6skp2w.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"8\"}],\"id\":115,\"sort\":1,\"status\":true}',1,1,'2020-07-29 19:59:14','2022-06-18 15:39:04'),
	(9,3,'{\"fields\":[{\"name\":\"orderStatus\",\"title\":\"orderStatus\",\"value\":\"1\"},{\"name\":\"image\",\"title\":\"image\",\"value\":\"crmebimage/public/product/2022/06/18/8d3e31140c6d4f9683edbd2d333e462fl3td6skp2w.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"9\"}],\"id\":115,\"sort\":1,\"status\":true}',1,1,'2020-07-29 19:59:29','2022-06-18 15:39:52'),
	(10,3,'{\"fields\":[{\"name\":\"orderStatus\",\"title\":\"orderStatus\",\"value\":\"2\"},{\"name\":\"image\",\"title\":\"image\",\"value\":\"crmebimage/public/product/2022/06/18/895816ec51c14563b679f02d729d210b2tdhub0hsb.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"10\"}],\"id\":115,\"sort\":1,\"status\":true}',1,1,'2020-07-29 19:59:47','2022-06-18 15:40:05'),
	(11,3,'{\"fields\":[{\"name\":\"orderStatus\",\"title\":\"orderStatus\",\"value\":\"3\"},{\"name\":\"image\",\"title\":\"image\",\"value\":\"crmebimage/public/product/2022/06/18/ff8278c17aef444db2556eec57d602a6p8l777730y.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"11\"}],\"id\":115,\"sort\":1,\"status\":true}',1,1,'2020-07-29 20:00:02','2022-06-18 15:40:37'),
	(12,3,'{\"fields\":[{\"name\":\"orderStatus\",\"title\":\"orderStatus\",\"value\":\"4\"},{\"name\":\"image\",\"title\":\"image\",\"value\":\"crmebimage/public/product/2022/06/18/ff8278c17aef444db2556eec57d602a6p8l777730y.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"12\"}],\"id\":115,\"sort\":1,\"status\":true}',1,1,'2020-07-29 20:00:14','2022-06-18 15:40:56'),
	(26,7,'{\"id\":97,\"sort\":0,\"fields\":[{\"name\":\"show\",\"title\":\"show\",\"value\":\"1\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/news_details/index?id=10\"},{\"name\":\"info\",\"title\":\"info\",\"value\":\"CRMEB电商系统Java版正式发布！\"}],\"status\":true}',0,1,'2021-12-17 15:47:11','2021-12-17 15:47:11'),
	(27,7,'{\"id\":97,\"sort\":1,\"fields\":[{\"name\":\"show\",\"title\":\"show\",\"value\":\"1\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/news_details/index?id=11\"},{\"name\":\"info\",\"title\":\"info\",\"value\":\"这是美好的一天\"}],\"status\":true}',1,1,'2021-12-17 15:47:11','2021-12-17 15:47:11'),
	(28,8,'{\"fields\":[{\"name\":\"title\",\"title\":\"title\",\"value\":\"键盘\"}],\"id\":102,\"sort\":1,\"status\":true}',1,1,'2020-06-03 11:34:54','2021-11-02 14:38:13'),
	(29,8,'{\"fields\":[{\"name\":\"title\",\"title\":\"title\",\"value\":\"鞋子\"}],\"id\":102,\"sort\":2,\"status\":true}',2,1,'2020-06-03 11:35:00','2020-06-03 11:35:00'),
	(30,8,'{\"fields\":[{\"name\":\"title\",\"title\":\"title\",\"value\":\"男装\"}],\"id\":102,\"sort\":3,\"status\":true}',3,1,'2020-06-03 11:35:07','2021-11-02 14:39:53'),
	(31,8,'{\"fields\":[{\"name\":\"title\",\"title\":\"title\",\"value\":\"MacBook\"}],\"id\":102,\"sort\":4,\"status\":true}',4,1,'2020-06-03 11:35:29','2021-11-02 14:40:03'),
	(32,8,'{\"fields\":[{\"name\":\"title\",\"title\":\"title\",\"value\":\"茶叶\"}],\"id\":102,\"sort\":0,\"status\":true}',0,1,'2021-02-07 12:18:07','2021-11-02 14:39:03'),
	(34,10,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/b5eea8de3dc844ca80c47fd44171e23dg52k1el1ze.gif\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"34\"}],\"id\":161,\"sort\":1,\"status\":true}',1,1,'2022-05-05 18:39:01','2022-06-18 15:09:40'),
	(35,10,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/2ba89f51e47f40279081815258ca4a4dpy5w3x8v51.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"35\"}],\"id\":161,\"sort\":1,\"status\":true}',1,1,'2022-05-05 18:39:01','2022-06-18 15:09:47'),
	(47,5,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/df6214011a2f4683b1b5feab362f0d882pneycevbh.jpg\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"www.crmeb.com\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"47\"}],\"id\":90,\"sort\":1,\"status\":true}',1,1,'2022-05-13 16:46:07','2022-06-18 15:32:22'),
	(48,5,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/7caee6176c4f44e4baea64206c59f6274ywzmc143e.jpg\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"www.crmeb.com\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"48\"}],\"id\":90,\"sort\":1,\"status\":true}',1,1,'2022-05-13 16:46:07','2022-06-18 15:32:30'),
	(58,2,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"pro\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/goods_details/index?id=9\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/d11be7f78a014558924a31874a50617at9bfdsqy0v.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"58\"}],\"id\":95,\"sort\":1,\"status\":true}',1,1,'2022-05-16 15:28:46','2022-06-18 15:42:17'),
	(59,2,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"123\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/goods_details/index?id=6\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/4248cfea71c040c1bf5a3a6a75f8c6381hujr27fou.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"59\"}],\"id\":95,\"sort\":1,\"status\":true}',1,1,'2022-05-16 15:28:46','2022-06-18 15:42:25'),
	(60,2,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"123\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/goods_details/index?id=16\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/b29d82deb7c443ca88795c3d341b6887h43udf7n4v.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"60\"}],\"id\":95,\"sort\":2,\"status\":false}',2,0,'2022-05-16 15:28:46','2022-06-18 15:42:32'),
	(61,2,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"456\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/goods_details/index?id=4\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/b1475f2ded8a4e23adb74d5cb3e08544k4rtv1sef4.jpg\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"61\"}],\"id\":95,\"sort\":3,\"status\":true}',3,1,'2022-05-16 15:28:46','2022-06-18 15:42:38'),
	(65,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"My profile\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/7d034a06a3404bf8b24ec82143e99017fpmlsx0om1.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_info/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/user_info\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"65\"}],\"id\":89,\"sort\":1,\"status\":true}',1,1,'2022-06-06 10:40:53','2022-06-18 15:36:23'),
	(66,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Address Information\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/a1a67a5c7cd74f44959f2bba2a9492e63bznmbr6zw.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_address_list/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/address_list\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"66\"}],\"id\":89,\"sort\":1,\"status\":true}',1,1,'2022-06-06 10:40:53','2022-06-18 15:36:31'),
	(67,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"My favorite\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/477ed879ac184774b41f296db9bcc6d0uc9cw3qcjv.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/collection_list\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"67\"}],\"id\":89,\"sort\":2,\"status\":true}',2,1,'2022-06-06 10:40:53','2022-06-18 15:36:43'),
	(68,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Coupons\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/96e9ecdd289d405ca9f7df66f8d4c667qqn1r1zsph.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_coupon/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/coupon_list\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"68\"}],\"id\":89,\"sort\":3,\"status\":true}',3,1,'2022-06-06 10:40:53','2022-06-18 15:37:05'),
	(69,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"follow merchant\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/1d4ca008abcd4470b98a4b1e2920f98b6k6s1os89z.jpg\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_merchant_collection/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\" \"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"69\"}],\"id\":89,\"sort\":4,\"status\":true}',4,1,'2022-06-06 10:40:53','2022-06-18 15:37:16'),
	(70,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"apply\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/d29434234e6d41b5bacfddab9bbf2a0d19vikgbtad.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/merchant/settled/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/merchant_settled\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"70\"}],\"id\":89,\"sort\":5,\"status\":true}',5,1,'2022-06-06 10:40:53','2022-06-18 15:37:24'),
	(71,4,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Application record\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/10de6d79d68d492eac7cd67af833baeczrhu8jcg82.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/merchant/application_record/index\"},{\"name\":\"pc_url\",\"title\":\"pc_url\",\"value\":\"/users/application_record\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"71\"}],\"id\":89,\"sort\":6,\"status\":true}',6,1,'2022-06-06 10:40:53','2022-06-18 15:37:33'),
	(99,6,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Category\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/5687d37941fa467a9b474a46b150e3f80bq8cnzyal.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/goods_cate/index\"},{\"name\":\"show\",\"title\":\"show\",\"value\":\"1\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"99\"}],\"id\":96,\"sort\":1,\"status\":true}',1,1,'2022-06-10 10:44:48','2022-06-18 15:27:26'),
	(100,6,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Favorite\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/89674707e3834933b4d4b623fea09ed1jjiea34l4l.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_goods_collection/index\"},{\"name\":\"show\",\"title\":\"show\",\"value\":\"2\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"100\"}],\"id\":96,\"sort\":1,\"status\":true}',1,1,'2022-06-10 10:44:48','2022-06-18 15:27:37'),
	(101,6,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Address\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/17dfd6211f5f4bc09631e9990299f96818527sqfe2.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/user_address_list/index\"},{\"name\":\"show\",\"title\":\"show\",\"value\":\"2\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"101\"}],\"id\":96,\"sort\":2,\"status\":true}',2,1,'2022-06-10 10:44:48','2022-06-18 15:27:51'),
	(102,6,'{\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"Order\"},{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/865c4d1385534806b99db13766bb0977sgyi097jso.png\"},{\"name\":\"url\",\"title\":\"url\",\"value\":\"/pages/users/order_list/index\"},{\"name\":\"show\",\"title\":\"show\",\"value\":\"2\"},{\"name\":\"id\",\"title\":\"id\",\"value\":\"102\"}],\"id\":96,\"sort\":3,\"status\":true}',3,1,'2022-06-10 10:44:48','2022-06-18 15:28:00'),
	(104,11,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/18/21f102999ad04afab46ef8ded3dff5cdtq5fdrdyrs.png\"},{\"name\":\"id\",\"title\":\"id\"}],\"id\":162,\"sort\":1,\"status\":true}',1,1,'2022-06-18 15:07:18','2022-06-18 15:07:18'),
	(105,9,'{\"fields\":[{\"name\":\"pic\",\"title\":\"pic\",\"value\":\"crmebimage/public/product/2022/06/17/ee50e78ebb9344dd9ecafd94e45e61660ugjjm32z5.png\"},{\"name\":\"id\",\"title\":\"id\"}],\"id\":114,\"sort\":1,\"status\":true}',1,1,'2022-06-18 15:07:43','2022-06-18 15:07:43'),
	(561, 14, '{\"id\":57,\"sort\":0,\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"首页\"},{\"name\":\"link\",\"title\":\"link\",\"value\":\"/pages/index/index\"},{\"name\":\"unchecked\",\"title\":\"unchecked\",\"value\":\"crmebimage/public/content/2023/08/09/482fd9aa66444da6a2d384c205b5fb4dmncfp7bqan.png\"},{\"name\":\"checked\",\"title\":\"checked\",\"value\":\"crmebimage/public/content/2023/08/09/cc1dffc929864a9daeb3cc89e0023068ikcfhefjzq.png\"}],\"status\":true}', 0, 1, '2023-08-18 11:31:31', '2023-08-18 11:31:31'),
	(562, 14, '{\"id\":57,\"sort\":1,\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"商品分类\"},{\"name\":\"link\",\"title\":\"link\",\"value\":\"/pages/goods_cate/index\"},{\"name\":\"unchecked\",\"title\":\"unchecked\",\"value\":\"crmebimage/public/content/2023/08/09/b3d03dc9ced24cca8979e6665bbd05desxr37idzrx.png\"},{\"name\":\"checked\",\"title\":\"checked\",\"value\":\"crmebimage/public/content/2023/08/09/66e0cb9b03aa4303a18144b3e64bf119p76mssng6w.png\"}],\"status\":true}', 1, 1, '2023-08-18 11:31:31', '2023-08-18 11:31:31'),
	(563, 14, '{\"id\":57,\"sort\":2,\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"购物车\"},{\"name\":\"link\",\"title\":\"link\",\"value\":\"/pages/order_addcart/order_addcart\"},{\"name\":\"unchecked\",\"title\":\"unchecked\",\"value\":\"crmebimage/public/content/2023/08/09/bd747d916c304191a446246442dddc7dnygnb6v095.png\"},{\"name\":\"checked\",\"title\":\"checked\",\"value\":\"crmebimage/public/content/2023/08/09/0764ed2d44e24717ad1ae9b05cdd7d5ec3y2o4ie1p.png\"}],\"status\":true}', 2, 1, '2023-08-18 11:31:31', '2023-08-18 11:31:31'),
	(564, 14, '{\"id\":57,\"sort\":3,\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"我的\"},{\"name\":\"link\",\"title\":\"link\",\"value\":\"/pages/user/index\"},{\"name\":\"unchecked\",\"title\":\"unchecked\",\"value\":\"crmebimage/public/content/2023/08/09/44b599d8605f40dea853bc5598b0a0c1ekyj4wfj2d.png\"},{\"name\":\"checked\",\"title\":\"checked\",\"value\":\"crmebimage/public/content/2023/08/09/e17fc4e9669047fa9e5a41e4622931b6ld6ufelvze.png\"}],\"status\":true}', 3, 1, '2023-08-18 11:31:31', '2023-08-18 11:31:31'),
	(565, 14, '{\"id\":57,\"sort\":4,\"fields\":[{\"name\":\"name\",\"title\":\"name\",\"value\":\"店铺街\"},{\"name\":\"link\",\"title\":\"link\",\"value\":\"/pages/merchant/street/index\"},{\"name\":\"unchecked\",\"title\":\"unchecked\",\"value\":\"crmebimage/public/content/2023/08/09/175e1b35b08d49c293975f98ef9a25e1mma4erkx2e.png\"},{\"name\":\"checked\",\"title\":\"checked\",\"value\":\"crmebimage/public/content/2023/08/09/58c3c6b5f4d04316ae3b8320e68e0b01wfgstekfpp.png\"}],\"status\":false}', 4, 0, '2023-08-18 11:31:31', '2023-08-18 11:31:31');

/*!40000 ALTER TABLE `eb_system_group_data` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_menu`;

CREATE TABLE `eb_system_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon',
  `perms` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `menu_type` varchar(2) DEFAULT 'M' COMMENT '类型，M-目录，C-菜单，A-按钮',
  `sort` int(5) NOT NULL DEFAULT '99999' COMMENT '排序',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `is_delte` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `type` int(11) NOT NULL COMMENT '系统菜单类型：3-平台,4-商户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统菜单表';

LOCK TABLES `eb_system_menu` WRITE;
/*!40000 ALTER TABLE `eb_system_menu` DISABLE KEYS */;

INSERT INTO `eb_system_menu` (`id`, `pid`, `name`, `icon`, `perms`, `component`, `menu_type`, `sort`, `is_show`, `is_delte`, `create_time`, `update_time`, `type`)
VALUES
	(1,0,'运营','menu',NULL,'/dashboard','M',9999,1,0,'2021-11-16 12:11:17','2022-04-19 23:36:33',3),
	(2,0,'商品','s-goods',NULL,'/store','M',9996,1,0,'2021-11-16 12:11:17','2022-04-19 23:52:31',3),
	(3,0,'订单','s-order',NULL,'/order','M',9997,1,0,'2021-11-16 12:11:17','2022-04-19 23:52:28',3),
	(4,0,'用户','user-solid',NULL,'/user','M',9995,1,0,'2021-11-16 12:11:17','2022-04-19 23:52:49',3),
	(5,0,'财务','s-finance',NULL,'/financial','M',9994,1,0,'2021-11-16 12:11:18','2022-04-19 23:52:59',3),
	(6,0,'设置','s-tools',NULL,'/operation','M',9993,1,0,'2021-11-16 12:11:18','2022-04-19 23:53:03',3),
	(7,0,'维护','s-open',NULL,'/maintain','M',9992,1,0,'2021-11-16 12:11:18','2022-04-19 23:53:06',3),
	(8,2,'商品分类',NULL,'','/store/sort','C',10,1,0,'2021-11-16 12:19:02','2022-04-19 23:36:39',3),
	(9,14,'库存变动',NULL,'','api/admin/store/product/stock','A',1,1,0,'2021-11-16 15:55:00','2022-04-19 23:36:46',3),
	(10,14,'虚拟销量',NULL,'','api/admin/store/product/ficti','A',1,1,0,'2021-11-16 15:55:00','2022-04-19 23:36:46',3),
	(11,32,'订单更新',NULL,'','api/admin/store/order/update','A',1,1,1,'2021-11-16 15:58:16','2022-04-19 23:36:46',3),
	(12,171,'领取记录',NULL,'merchant:coupon:user:page:list','/marketing/coupon/record','C',10,1,1,'2021-11-16 16:48:47','2022-04-24 14:33:15',4),
	(13,12,'领取优惠券',NULL,'','api/admin/marketing/coupon/user/receive','A',1,1,1,'2021-11-16 16:50:02','2022-04-20 09:42:19',3),
	(14,6,'管理权限',NULL,'','/operation/roleManager','M',10,1,0,'2021-11-16 17:23:37','2022-04-19 23:55:25',3),
	(15,6,'页面管理',NULL,'','/operation/design','M',10,1,0,'2021-11-16 17:23:37','2022-04-19 23:55:26',3),
	(16,14,'角色管理',NULL,'platform:admin:role:list','/operation//roleManager/identityManager','C',10,1,0,'2021-11-16 17:30:23','2022-04-19 23:36:39',3),
	(17,14,'管理员列表',NULL,'platform:admin:list','/operation//roleManager/adminList','C',10,1,0,'2021-11-16 17:30:23','2022-04-19 23:36:39',3),
	(18,14,'权限规则',NULL,'','/operation//roleManager/promiseRules','C',10,1,0,'2021-11-16 17:30:23','2022-04-20 00:29:51',3),
	(19,15,'一键换色',NULL,'','/operation/design/theme','C',10,1,0,'2021-11-16 17:30:23','2022-04-19 23:36:39',3),
	(20,15,'页面设计',NULL,'platform:page:layout:index','/operation/design/viewDesign','C',10,1,0,'2021-11-16 17:30:23','2022-04-19 23:36:39',3),
	(21,16,'身份添加',NULL,'platform:admin:role:save','api/admin/system/role/save','A',1,1,1,'2021-11-16 17:35:42','2022-04-19 23:36:46',3),
	(22,16,'身份删除',NULL,'platform:admin:role:delete','','A',1,1,1,'2021-11-16 17:35:42','2022-04-19 23:36:46',3),
	(23,16,'身份修改',NULL,'platform:admin:role:update','','A',1,1,1,'2021-11-16 17:35:43','2022-04-19 23:36:46',3),
	(24,17,'管理员添加',NULL,'platform:admin:save','','A',1,1,0,'2021-11-16 17:35:43','2022-05-10 17:12:01',3),
	(25,17,'管理员修改',NULL,'platform:admin:update','','A',1,1,0,'2021-11-16 17:35:43','2022-04-19 23:36:46',3),
	(26,17,'管理员删除',NULL,'platform:admin:delete','','A',1,1,0,'2021-11-16 17:35:43','2022-04-19 23:36:46',3),
	(27,17,'管理员状态更新',NULL,'platform:admin:update:status','','A',1,1,0,'2021-11-16 17:35:43','2022-04-19 23:36:46',3),
	(28,16,'权限新增',NULL,'platform:admin:role:save',NULL,'A',1,1,0,'2021-11-16 17:35:43','2022-04-20 00:30:00',3),
	(29,16,'权限删除',NULL,'platform:admin:role:delete',NULL,'A',1,1,0,'2021-11-16 17:35:43','2022-04-20 00:30:00',3),
	(30,16,'权限更新',NULL,'platform:admin:role:update',NULL,'A',1,1,0,'2021-11-16 17:35:43','2022-04-20 00:30:00',3),
	(31,16,'更新权限状态',NULL,'platform:admin:role:update:status',NULL,'A',1,1,1,'2021-11-16 17:35:43','2022-04-20 00:30:00',3),
	(32,7,'素材管理',NULL,'platform:attachment:list','/maintain/picture','C',10,1,0,'2021-11-16 17:38:38','2022-04-19 23:36:39',3),
	(33,7,'开发配置',NULL,'','/maintain//devconfiguration','M',10,1,0,'2021-11-16 17:38:38','2022-04-19 23:55:30',3),
	(34,7,'申请授权',NULL,'','/maintain/authCRMEB','C',10,1,0,'2021-11-16 17:38:38','2022-04-19 23:36:39',3),
	(35,7,'物流设置',NULL,'','/maintain/logistics','M',10,1,0,'2021-11-16 17:38:38','2022-04-19 23:55:31',3),
	(36,32,'删除素材',NULL,'platform:attachment:delete','api/admin/system/attachment/delete','A',1,1,0,'2021-11-16 17:41:33','2022-04-20 00:01:26',3),
	(37,33,'配置分类',NULL,'','/maintain/devconfiguration/configCategory','C',10,1,0,'2021-11-16 17:41:33','2022-04-19 23:58:29',3),
	(38,33,'组合数据',NULL,'platform:config:group:list','/maintain/devconfiguration/combineddata','C',10,1,0,'2021-11-16 17:41:33','2022-04-19 23:58:29',3),
	(39,33,'表单配置',NULL,'platform:config:form:list','/maintain/devconfiguration/formConfig','C',10,1,0,'2021-11-16 17:41:33','2022-04-19 23:58:29',3),
	(40,38,'数据组添加',NULL,'platform:config:group:save','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:56:44',3),
	(41,38,'数据组修改',NULL,'platform:config:group:update','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:56:56',3),
	(42,38,'数据组删除',NULL,'platform:config:group:delete','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:57:05',3),
	(43,38,'组合数据添加',NULL,'platform:config:group:data:save','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:57:19',3),
	(44,38,'组合数据修改',NULL,'platform:config:group:data:update','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:57:28',3),
	(45,38,'组合数据删除',NULL,'platform:config:group:data:delete','','A',1,1,0,'2021-11-16 17:44:35','2022-05-05 14:57:37',3),
	(46,39,'表单添加',NULL,'platform:config:form:save','','A',1,1,0,'2021-11-16 17:44:35','2022-04-20 00:33:16',3),
	(47,39,'表单修改',NULL,'platform:config:form:update','','A',1,1,0,'2021-11-16 17:44:35','2022-04-20 00:33:16',3),
	(48,1,'控制台','','','/dashboard','C',10,1,0,'2021-11-30 14:13:37','2022-04-19 23:36:39',3),
	(49,1,'用户统计','','','/statuser','C',10,1,1,'2021-11-30 14:14:10','2022-04-19 23:36:39',3),
	(50,1,'交易统计','','','/transaction','C',10,1,1,'2021-11-30 14:14:34','2022-04-19 23:36:39',3),
	(51,1,'商品统计','','','/product','C',10,1,1,'2021-11-30 14:17:46','2022-04-19 23:36:39',3),
	(52,38,'组合数据详情','','platform:config:group:data:info','','A',1,1,0,'2021-12-02 11:35:48','2022-05-05 14:57:46',3),
	(53,0,'登录管理',NULL,NULL,NULL,'M',9991,0,0,'2021-12-03 16:31:43','2022-04-19 23:53:10',3),
	(54,16,'角色详情',NULL,'platform:admin:role:info',NULL,'A',1,1,0,'2021-12-03 16:39:41','2022-04-20 00:27:02',3),
	(55,48,'首页数据','','platform:statistics:home:index','','A',1,1,0,'2021-12-03 16:51:09','2022-04-20 16:21:26',3),
	(56,48,'用户曲线图','','admin:statistics:home:chart:user','','A',1,1,1,'2021-12-03 16:51:29','2022-04-19 23:43:35',3),
	(57,48,'用户购买统计','','admin:statistics:home:chart:user:buy','','A',1,1,1,'2021-12-03 16:51:59','2022-04-19 23:43:35',3),
	(58,48,'30天订单量趋势','','admin:statistics:home:chart:order','','A',1,1,1,'2021-12-03 16:52:47','2022-04-19 23:43:35',3),
	(59,48,'周订单量趋势','','admin:statistics:home:chart:order:week','','A',1,1,1,'2021-12-03 16:53:00','2022-04-19 23:43:35',3),
	(60,48,'月订单量趋势','','admin:statistics:home:chart:order:month','','A',1,1,1,'2021-12-03 16:53:16','2022-04-19 23:43:35',3),
	(61,48,'年订单量趋势','','admin:statistics:home:chart:order:year','','A',1,1,1,'2021-12-03 16:53:36','2022-04-19 23:43:35',3),
	(62,0,'分类服务(素材/设置/文章)','','','','M',9990,0,0,'2021-12-03 17:15:29','2022-04-19 23:53:14',3),
	(63,62,'分类列表','','platform:category:list','','A',1,1,0,'2021-12-03 17:16:12','2022-04-20 00:10:52',3),
	(64,62,'新增分类','','platform:category:save','','A',1,1,0,'2021-12-03 17:16:35','2022-04-20 00:10:52',3),
	(65,62,'删除分类','','platform:category:delete','','A',1,1,0,'2021-12-03 17:16:52','2022-04-20 00:10:52',3),
	(66,62,'修改分类','','platform:category:update','','A',1,1,0,'2021-12-03 17:17:11','2022-04-20 00:10:52',3),
	(67,62,'分类详情','','platform:category:info','','A',1,1,0,'2021-12-03 17:17:35','2022-04-20 00:10:52',3),
	(68,62,'分类tree列表','','platform:category:list:tree','','A',1,1,0,'2021-12-03 17:18:06','2022-04-20 00:10:52',3),
	(69,62,'根据id集合获取分类列表','','platform:category:list:ids','','A',1,1,0,'2021-12-03 17:18:23','2022-04-20 00:10:52',3),
	(70,62,'更改分类状态','','platform:category:update:status','','A',1,1,0,'2021-12-03 17:18:38','2022-04-20 00:10:52',3),
	(71,38,'分页组合数据详情','','platform:config:group:data:list','','A',1,1,0,'2021-12-03 17:39:37','2022-05-05 14:57:59',3),
	(72,32,'订单操作记录列表','','merchant:order:status:list','','A',1,1,0,'2021-12-06 09:45:04','2022-04-19 23:36:46',3),
	(73,16,'角色详情','','platform:admin:role:info','','A',1,1,1,'2021-12-06 10:50:32','2022-04-19 23:36:46',3),
	(74,16,'修改角色状态','','platform:admin:role:update:status','','A',1,1,0,'2021-12-06 10:51:09','2022-04-19 23:36:46',3),
	(75,17,'管理员详情','','platform:admin:info','','A',1,1,0,'2021-12-06 11:04:41','2022-04-19 23:36:46',3),
	(76,87,'表单模板详情','','platform:config:form:info','','A',1,1,0,'2021-12-07 09:33:10','2022-04-20 00:11:53',3),
	(77,360,'整体保存表单数据','','platform:config:save:form','','A',1,1,0,'2021-12-07 09:35:50','2022-04-22 17:04:13',3),
	(78,48,'经营数据','','platform:statistics:home:operating:data','','A',1,1,0,'2021-12-07 11:41:07','2022-04-20 16:22:09',3),
	(79,20,'页面布局首页保存','','platform:page:layout:save','','A',1,1,0,'2021-12-07 15:36:08','2022-05-05 14:59:02',3),
	(80,20,'页面布局首页banner保存','','platform:page:layout:index:banner:save','','A',1,1,0,'2021-12-07 15:36:45','2022-05-05 14:59:10',3),
	(81,20,'页面布局首页menu保存','','platform:page:layout:index:menu:save','','A',1,1,0,'2021-12-07 15:37:13','2022-05-05 14:59:20',3),
	(82,20,'页面首页新闻保存','','platform:page:layout:index:news:save','','A',1,1,0,'2021-12-07 15:37:38','2022-05-05 14:59:29',3),
	(83,20,'页面用户中心导航保存','','platform:page:layout:user:menu:save','','A',1,1,0,'2021-12-07 15:38:32','2022-05-05 14:59:37',3),
	(84,20,'页面用户中心商品table保存','','platform:page:layout:index:table:save','','A',1,1,0,'2021-12-07 15:38:57','2022-05-05 14:59:46',3),
	(85,20,'分类页配置保存','','platform:page:layout:category:config:save','','A',1,1,0,'2021-12-07 15:39:29','2022-05-05 15:00:13',3),
	(86,20,'获取分类页配置','','platform:page:layout:category:config','','A',1,1,0,'2021-12-08 10:25:27','2022-05-05 15:00:38',3),
	(87,0,'公共服务','','','','M',9989,0,0,'2021-12-08 18:56:36','2022-04-19 23:53:26',3),
	(88,87,'根据key存储','','platform:config:saveuniq','','A',1,1,0,'2021-12-08 18:57:26','2022-04-20 00:11:53',3),
	(89,87,'根据key获取','','platform:config:getuniq','','A',1,1,0,'2021-12-08 18:58:55','2022-04-20 00:11:54',3),
	(90,32,'修改图片分组','','platform:attachment:move','','A',1,1,0,'2021-12-10 15:38:24','2022-04-20 00:01:32',3),
	(91,87,'检测表单name是否存在','','platform:config:check','','A',1,1,0,'2021-12-13 10:30:58','2022-04-20 00:11:54',3),
	(92,87,'根据key获取配置','','platform:config:get','','A',1,1,0,'2021-12-13 10:31:24','2022-04-20 00:11:54',3),
	(93,87,'更新配置信息','','platform:config:update','','A',1,1,0,'2021-12-13 10:31:44','2022-04-20 00:11:54',3),
	(94,87,'组合数据详情','','platform:config:group:info','','A',1,1,0,'2021-12-13 10:32:28','2022-04-20 00:11:54',3),
	(95,287,'消息通知','','platform:system:notification:list','/operation/notification','C',10,1,0,'2022-02-10 09:58:00','2022-04-20 00:03:34',3),
	(96,95,'通知详情','','platform:system:notification:detail','','A',1,1,0,'2022-02-10 10:03:42','2022-04-20 00:04:37',3),
	(97,95,'修改通知','','platform:system:notification:update','','A',1,1,0,'2022-02-10 10:04:27','2022-04-20 00:04:37',3),
	(98,95,'发送短信开关','','platform:system:notification:sms:switch','','A',1,1,0,'2022-02-17 15:18:09','2022-04-20 00:04:37',3),
	(99,95,'发送邮件开关','','platform:system:notification:email:switch','','A',1,1,0,'2022-02-17 15:20:18','2022-04-20 00:04:37',3),
	(100,95,'海外短信开关','','platform:system:notification:oversea:sms:switch','','A',1,1,0,'2022-02-17 15:21:34','2022-04-20 00:04:37',3),
	(101,53,'平台端登出','','platform:logout',NULL,'A',1,1,0,'2022-03-03 16:12:33','2022-04-20 00:19:27',3),
	(102,53,'平台端获取用户详情','','platform:login:user:info',NULL,'A',1,1,0,'2022-03-03 16:14:10','2022-04-20 00:19:53',3),
	(103,53,'平台端获取管理员可访问目录','','platform:login:menus',NULL,'A',1,1,0,'2022-03-03 16:14:56','2022-04-20 00:19:53',3),
	(104,162,'商户分类分页列表','','platform:merchant:category:list',NULL,'A',1,1,0,'2022-03-03 17:09:51','2022-04-20 00:21:11',3),
	(105,162,'添加商户分类','','platform:merchant:category:add',NULL,'A',1,1,0,'2022-03-03 17:10:02','2022-04-20 00:21:11',3),
	(106,162,'编辑商户分类','','platform:merchant:category:update',NULL,'A',1,1,0,'2022-03-03 17:10:06','2022-04-20 00:21:12',3),
	(107,162,'删除商户分类','','platform:merchant:category:delete',NULL,'A',1,1,0,'2022-03-03 17:11:04','2022-04-20 00:21:12',3),
	(108,162,'获取全部商户分类列表','','platform:merchant:category:all',NULL,'A',1,1,0,'2022-03-03 17:11:08','2022-04-20 00:21:12',3),
	(109,166,'商户类型分页列表','','platform:merchant:type:list',NULL,'A',1,1,1,'2022-03-03 17:11:14','2022-04-20 00:23:12',3),
	(110,166,'添加商户类型','','platform:merchant:type:add',NULL,'A',1,1,0,'2022-03-03 17:11:15','2022-04-20 00:23:12',3),
	(111,166,'编辑类型类型','','platform:merchant:type:update',NULL,'A',1,1,0,'2022-03-03 17:11:23','2022-04-20 00:23:12',3),
	(112,166,'删除商户类型','','platform:merchant:type:delete',NULL,'A',1,1,0,'2022-03-03 17:27:04','2022-04-20 00:23:12',3),
	(113,166,'获取全部商户类型列表','','platform:merchant:type:all',NULL,'A',1,1,0,'2022-03-03 17:27:54','2022-04-20 00:23:12',3),
	(114,18,'平台端菜单列表','','platform:menu:list',NULL,'A',1,1,0,'2022-03-07 11:15:00','2022-04-20 00:30:48',3),
	(115,18,'新增菜单',NULL,'platform:menu:add',NULL,'A',1,1,0,'2022-03-07 11:15:41','2022-04-20 00:30:48',3),
	(116,18,'删除菜单',NULL,'platform:menu:delete',NULL,'A',1,1,0,'2022-03-07 11:15:58','2022-04-20 00:30:48',3),
	(117,18,'修改菜单',NULL,'platform:menu:update',NULL,'A',1,1,0,'2022-03-07 11:16:15','2022-04-20 00:30:48',3),
	(118,18,'菜单详情',NULL,'platform:menu:info',NULL,'A',1,1,0,'2022-03-07 11:16:24','2022-04-20 00:30:48',3),
	(119,18,'修改菜单显示状态',NULL,'platform:menu:show:status',NULL,'A',1,1,0,'2022-03-07 11:16:34','2022-04-20 00:30:48',3),
	(120,18,'菜单缓存树',NULL,'platform:menu:cache:tree',NULL,'A',1,1,0,'2022-03-07 11:16:45','2022-04-20 00:30:48',3),
	(121,350,'菜单缓存树',NULL,'merchant:menu:cache:tree',NULL,'A',1,1,0,'2022-03-07 11:17:18','2022-04-21 01:00:52',4),
	(122,350,'登出',NULL,'merchant:logout',NULL,'A',1,1,0,'2022-03-07 11:17:23','2022-04-21 01:00:01',4),
	(123,180,'获取登录用户详情',NULL,'merchant:login:user:info',NULL,'A',1,1,0,'2022-03-07 11:17:38','2022-04-20 09:51:27',4),
	(124,350,'获取管理员可访问目录',NULL,'merchant:login:menus',NULL,'A',1,1,0,'2022-03-07 11:17:46','2022-04-21 01:00:10',4),
	(125,165,'商户入驻分页列表',NULL,'platform:merchant:apply:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:10','2022-04-20 00:32:11',3),
	(126,165,'审核',NULL,'platform:merchant:apply:audit',NULL,'A',1,1,0,'2022-03-07 17:26:17','2022-04-20 00:36:24',3),
	(127,165,'备注',NULL,'platform:merchant:apply:remark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:36:24',3),
	(128,163,'商户分页列表',NULL,'platform:merchant:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:42',3),
	(129,163,'添加商户',NULL,'platform:merchant:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:42',3),
	(130,163,'编辑商户',NULL,'platform:merchant:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-05-14 14:35:25',3),
	(131,163,'修改商户密码',NULL,'platform:merchant:update:password',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:42',3),
	(132,163,'修改复制商品数量',NULL,'platform:merchant:copy:prodcut:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:39:34',3),
	(133,163,'商户详情',NULL,'platform:merchant:detail',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:43',3),
	(134,163,'推荐开关',NULL,'platform:merchant:recommend:switch',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:39:44',3),
	(135,163,'关闭商户',NULL,'platform:merchant:close',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:43',3),
	(136,163,'开启商户',NULL,'platform:merchant:open',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:37:43',3),
	(137,164,'平台端商户菜单列表',NULL,'platform:merchant:menu:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(138,164,'新增菜单',NULL,'platform:merchant:menu:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(139,164,'删除菜单',NULL,'platform:merchant:menu:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(140,164,'修改菜单',NULL,'platform:merchant:menu:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(141,164,'菜单详情',NULL,'platform:merchant:menu:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(142,164,'修改菜单显示状态',NULL,'platform:merchant:menu:show:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:38:23',3),
	(143,8,'平台端商品分类列表',NULL,'platform:product:category:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:29',3),
	(144,8,'新增分类',NULL,'platform:product:category:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:45',3),
	(145,8,'删除分类',NULL,'platform:product:category:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:45',3),
	(146,8,'修改分类',NULL,'platform:product:category:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:45',3),
	(147,8,'修改分类显示状态',NULL,'platform:product:category:show:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:45',3),
	(148,8,'分类缓存树',NULL,'platform:product:category:cache:tree',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:40:45',3),
	(149,167,'平台端商品品牌分页列表',NULL,'platform:product:brand:list',NULL,'A',1,1,1,'2022-03-07 17:26:31','2022-04-20 00:41:53',3),
	(150,167,'新增品牌',NULL,'platform:product:brand:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:41:53',3),
	(151,167,'删除品牌',NULL,'platform:product:brand:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:41:53',3),
	(152,167,'修改品牌',NULL,'platform:product:brand:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:41:53',3),
	(153,167,'修改品牌显示状态',NULL,'platform:product:brand:show:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:41:53',3),
	(154,167,'品牌缓存列表(全部)',NULL,'platform:product:brand:cache:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:41:54',3),
	(155,160,'平台端商品保障服务列表',NULL,'platform:product:guarantee:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:42:45',3),
	(156,160,'新增保障服务',NULL,'platform:product:guarantee:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:42:45',3),
	(157,160,'删除保障服务',NULL,'platform:product:guarantee:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:42:45',3),
	(158,160,'修改保障服务',NULL,'platform:product:guarantee:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:42:46',3),
	(159,160,'修改保障服务显示状态',NULL,'platform:product:guarantee:show:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:42:46',3),
	(160,2,'保障服务','','platform:product:guarantee:list','/store/guarantee','C',10,1,0,'2022-03-14 10:16:06','2022-04-19 23:36:39',3),
	(161,0,'商户','s-shop','','/merchant','M',9998,1,0,'2022-03-14 12:57:51','2022-04-19 23:52:20',3),
	(162,161,'商户分类','','','/merchant/classify','C',10,1,0,'2022-03-14 12:59:57','2022-04-20 00:05:30',3),
	(163,161,'商户列表','','','/merchant/list','C',10,1,0,'2022-03-14 13:01:10','2022-04-20 00:05:31',3),
	(164,161,'商户菜单管理','','','/merchant/system','C',10,1,0,'2022-03-14 13:02:12','2022-04-20 00:05:31',3),
	(165,161,'商户入驻申请','','','/merchant/application','C',10,1,0,'2022-03-14 13:02:55','2022-04-20 00:05:31',3),
	(166,161,'店铺类型','','platform:merchant:type:list','/merchant/type','C',10,1,0,'2022-03-14 13:03:39','2022-04-27 15:27:00',3),
	(167,2,'品牌列表','','platform:product:brand:list','/store/brand','C',10,1,0,'2022-03-14 13:14:50','2022-04-20 11:06:15',3),
	(168,166,'店铺类型','','','/merchant/type/list','C',10,1,1,'2022-03-14 16:45:26','2022-04-20 00:07:19',3),
	(169,166,'店铺类型说明','','','/merchant/type/desc','C',10,1,1,'2022-03-14 16:45:53','2022-04-20 00:07:19',3),
	(170,0,'首页','s-grid','','/dashboard','M',9999,1,0,'2022-03-14 17:02:46','2022-04-24 10:50:50',4),
	(171,0,'商品','s-goods','','/product','M',9999,1,0,'2022-03-14 17:05:29','2022-04-19 23:36:33',4),
	(172,171,'商品列表','','','/product/list','C',10,1,0,'2022-03-14 17:24:41','2022-04-20 09:24:53',4),
	(173,171,'商品分类','','','/product/classify','C',10,1,0,'2022-03-14 17:25:39','2022-04-20 09:24:53',4),
	(174,171,'商品规格','','','/product/attr','C',10,1,0,'2022-03-14 17:26:06','2022-04-20 09:24:53',4),
	(175,171,'商品评价','','','/product/reviews','C',10,1,0,'2022-03-14 17:26:33','2022-04-20 09:24:53',4),
	(176,0,'订单','s-order','','/order','M',9999,1,0,'2022-03-14 17:27:37','2022-04-19 23:36:33',4),
	(177,171,'商品分类','','','/product/classify','C',10,1,1,'2022-03-14 17:30:13','2022-04-20 09:24:53',4),
	(178,176,'订单管理','','','/order/list','C',10,1,0,'2022-03-14 17:33:00','2022-04-20 09:25:02',4),
	(179,176,'退款单','','','/order/refund','C',10,1,0,'2022-03-14 17:33:13','2022-04-20 09:25:02',4),
	(180,0,'用户','user-solid','','/user','M',9999,1,0,'2022-03-14 17:33:47','2022-04-19 23:36:33',4),
	(181,180,'用户标签','','','/user/label','C',10,1,1,'2022-03-14 17:35:22','2022-04-20 00:13:29',4),
	(182,180,'用户列表','','','/user/index','C',10,1,0,'2022-03-14 17:36:23','2022-04-20 00:13:29',4),
	(183,0,'财务','s-finance','','/accounts','M',9999,1,0,'2022-03-14 18:30:51','2022-04-19 23:36:33',4),
	(184,183,'财务对账','','','/accounts/reconciliation','C',10,1,1,'2022-03-14 18:53:06','2022-04-20 09:25:16',4),
	(185,183,'资金流水','','','/accounts/capitalFlow','C',10,1,0,'2022-03-14 18:53:26','2022-04-20 09:25:16',4),
	(186,183,'转账记录 ','','','/accounts/transManagement','C',10,1,0,'2022-03-14 18:53:48','2022-04-20 09:25:17',4),
	(187,183,'账单管理','','','/accounts/statement','C',10,1,0,'2022-03-14 18:55:22','2022-04-20 09:25:17',4),
	(188,2,'商品列表',NULL,'platform:product:page:list','/store/list','C',1,1,0,'2022-03-07 17:26:31','2022-04-20 11:04:59',3),
	(189,188,'平台端商品审核',NULL,'platform:product:audit',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:45:32',3),
	(190,188,'平台端强制下架商品',NULL,'platform:product:force:down',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:45:32',3),
	(191,188,'平台端虚拟销量',NULL,'platform:product:virtual:sales',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:47:13',3),
	(192,188,'平台端商品详情',NULL,'platform:product:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:45:33',3),
	(193,171,'商户端品牌分页列表',NULL,'merchant:product:brand:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:11:09',4),
	(194,171,'品牌缓存列表(全部)',NULL,'merchant:product:brand:cache:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:11:09',4),
	(195,171,'商户端平台商品分类缓存树',NULL,'merchant:product:category:cache:tree',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:45:43',4),
	(196,172,'商户端商品分页列表',NULL,'merchant:product:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(197,172,'商户端新增商品',NULL,'merchant:product:save',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(198,172,'商户端删除商品',NULL,'merchant:product:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(199,172,'商户端恢复商品',NULL,'merchant:product:restore',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(200,172,'商户端商品修改',NULL,'merchant:product:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(201,172,'商户端商品详情',NULL,'merchant:product:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:03',4),
	(202,172,'商户端商品表头数量',NULL,'merchant:product:tabs:headers',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:04',4),
	(203,172,'商户端商品上架',NULL,'merchant:product:up',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:04',4),
	(204,172,'商户端商品下架',NULL,'merchant:product:down',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:04',4),
	(205,172,'商户端导入99Api商品',NULL,'merchant:product:import:product',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:04',4),
	(206,171,'商户端保障服务列表',NULL,'merchant:product:guarantee:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:11:25',4),
	(207,173,'商户端商户商品分类列表',NULL,'merchant:store:product:category:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(208,173,'商户端商户商品新增分类',NULL,'merchant:store:product:category:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(209,173,'商户端商户商品删除分类',NULL,'merchant:store:product:category:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(210,173,'商户端商户商品修改分类',NULL,'merchant:store:product:category:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(211,173,'商户端商户商品修改分类显示状态',NULL,'merchant:store:product:category:show:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(212,173,'商户端商户商品分类缓存树',NULL,'merchant:store:product:category:cache:tree',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:19',4),
	(213,178,'商户端订单分页列表',NULL,'merchant:order:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:49:51',4),
	(214,213,'获取订单各状态数量',NULL,'merchant:order:status:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:00',4),
	(215,213,'商户端订单删除',NULL,'merchant:order:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:11',4),
	(216,213,'商户端备注订单',NULL,'merchant:order:mark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:11',4),
	(217,213,'商户端订单详情',NULL,'merchant:order:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:11',4),
	(218,213,'商户端订单发货',NULL,'merchant:order:send',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:11',4),
	(219,213,'商户端订单物流详情',NULL,'merchant:order:logistics:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:11',4),
	(220,3,'订单列表',NULL,'platform:order:page:list','/order/list','C',1,1,0,'2022-03-07 17:26:31','2022-04-20 11:25:58',3),
	(221,220,'平台端获取订单各状态数量',NULL,'platform:order:status:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:49:21',3),
	(222,220,'平台端备注',NULL,'platform:order:mark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:50:00',3),
	(223,220,'平台端订单详情',NULL,'platform:order:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:49:21',3),
	(224,179,'商户端退款订单分页列表',NULL,'merchant:refund:order:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:30',4),
	(225,179,'商户端获取退款订单各状态数量',NULL,'merchant:refund:order:status:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:30',4),
	(226,179,'商户备注退款订单',NULL,'merchant:refund:order:mark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:30',4),
	(227,179,'商户端订单退款',NULL,'merchant:refund:order:refund',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:50:30',4),
	(228,179,'商户端拒绝退款',NULL,'merchant:refund:order:refund:refuse',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-06-13 14:44:21',4),
	(229,3,'退款单',NULL,'platform:refund:order:page:list','/order/refund','C',1,1,0,'2022-03-07 17:26:31','2022-04-20 11:28:06',3),
	(230,229,'平台端获取退款订单各状态数量',NULL,'platform:refund:order:status:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:49:29',3),
	(231,229,'平台备注退款订单',NULL,'platform:refund:order:mark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:49:29',3),
	(232,0,'设置','s-tools','','/operation','M',9999,1,0,'2022-03-26 11:29:43','2022-04-19 23:36:33',4),
	(233,346,'查询表单模板信息',NULL,'admin:system:form:info',NULL,'A',1,0,0,'2022-03-26 11:57:15','2022-04-21 01:01:03',4),
	(234,174,'商户端商品规则值(规格)分页列表',NULL,'merchant:product:rule:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:31',4),
	(235,174,'商户端商品规则值(规格)新增',NULL,'merchant:product:rule:save',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:31',4),
	(236,174,'商户端商品规则值(规格)删除',NULL,'merchant:product:rule:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:31',4),
	(237,174,'商户端商品规则值(规格)修改',NULL,'merchant:product:rule:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:32',4),
	(238,174,'商户端商品规则值(规格)详情',NULL,'merchant:product:rule:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:32',4),
	(239,232,'商户基本设置','','','/operation/modifyStoreInfo','C',10,1,0,'2022-03-26 17:19:23','2022-04-20 09:25:30',4),
	(240,366,'角色管理','','','/operation/roleManager/identityManager','C',10,1,0,'2022-03-26 17:21:03','2022-04-24 16:02:20',4),
	(241,232,'商城设置','','','/operation/mallConfiguration','C',10,0,0,'2022-03-26 17:43:18','2022-04-20 09:25:30',4),
	(242,248,'商户端优惠券分页列表',NULL,'merchant:coupon:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:54:09',4),
	(243,248,'商户端新增优惠券',NULL,'merchant:coupon:save',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:54:09',4),
	(244,248,'商户端修改优惠券状态',NULL,'merchant:coupon:update:status',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:54:09',4),
	(245,248,'商户端优惠券详情',NULL,'merchant:coupon:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:54:09',4),
	(246,248,'商户端删除优惠券',NULL,'merchant:coupon:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:54:09',4),
	(247,0,'优惠券','s-ticket','','/coupon','M',10,1,0,'2022-03-28 11:10:10','2022-04-24 15:49:36',4),
	(248,247,'优惠券列表','','','/coupon/list','C',10,1,0,'2022-03-28 11:12:30','2022-04-24 15:49:24',4),
	(249,247,'领取记录','','merchant:coupon:user:page:list','/coupon/record','C',10,1,0,'2022-03-28 11:13:42','2022-05-05 12:06:03',4),
	(250,284,'素材管理','','','/maintain/picture','C',10,1,0,'2022-03-28 15:58:34','2022-04-20 09:27:27',4),
	(251,285,'物流公司','','','/maintain/logistics/companyList','C',10,1,1,'2022-03-28 18:14:31','2022-04-20 09:28:02',4),
	(252,232,'数据导出','','','/operation/export','C',10,1,1,'2022-03-28 18:26:12','2022-04-20 09:28:52',4),
	(253,2,'商品评论',NULL,'platform:product:reply:list','/store/comment','C',1,1,0,'2022-03-07 17:26:31','2022-04-20 11:12:28',3),
	(254,253,'平台端删除评论',NULL,'platform:product:reply:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:46:16',3),
	(255,175,'商户端商品评论分页列表',NULL,'merchant:product:reply:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:46:40',4),
	(256,255,'商户端虚拟评论',NULL,'merchant:product:reply:virtual',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:08:28',4),
	(257,255,'商户端删除评论',NULL,'merchant:product:reply:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:08:28',4),
	(258,255,'商户端回复评论',NULL,'merchant:product:reply:comment',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:08:28',4),
	(259,3,'退款单','','','/order/refund','C',10,1,1,'2022-03-30 16:16:12','2022-04-20 10:31:54',3),
	(260,282,'平台端活动分页列表',NULL,'platform:activity:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(261,282,'平台端添加活动',NULL,'platform:activity:add',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(262,282,'平台备删除活动',NULL,'platform:activity:delete',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(263,282,'平台端编辑活动',NULL,'platform:activity:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(264,282,'平台备活动详情',NULL,'platform:activity:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(265,282,'平台端活动开关',NULL,'platform:activity:switch',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:51:00',3),
	(266,4,'用户列表',NULL,'platform:user:page:list','/user/index','C',10,1,0,'2022-03-07 17:26:31','2022-04-21 22:03:29',3),
	(267,266,'平台端修改用户信息',NULL,'platform:user:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:05:07',3),
	(268,266,'平台用户详情',NULL,'platform:user:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:05:18',3),
	(269,266,'平台端获取用户消费记录',NULL,'platform:user:expenses:record',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:05:29',3),
	(270,266,'平台端获取用户持有优惠券',NULL,'platform:user:have:coupons',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:05:38',3),
	(271,266,'平台端用户详情页Top数据',NULL,'platform:user:topdetail',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:05:47',3),
	(272,4,'平台端用户分配分组',NULL,'platform:user:group',NULL,'A',1,1,1,'2022-03-07 17:26:31','2022-04-20 00:53:09',3),
	(273,266,'平台端用户分配标签',NULL,'platform:user:tag',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-21 22:06:05',3),
	(274,182,'商户端用户分页列表',NULL,'merchant:user:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:51:03',4),
	(275,176,'商户端查询全部物流公司',NULL,'merchant:express:all',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-05-05 12:01:44',4),
	(276,35,'平台端物流公司分页列表',NULL,'platform:express:page:list',NULL,'A',1,1,1,'2022-03-07 17:26:31','2022-04-20 00:54:02',3),
	(277,369,'平台端物流公司修改显示状态',NULL,'platform:express:update:show',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-05-11 11:07:24',3),
	(278,373,'平台端敏感操作日志分页列表',NULL,'platform:log:sensitive:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-05-09 15:35:03',3),
	(279,284,'商户端敏感操作日志分页列表',NULL,'merchant:log:sensitive:list',NULL,'A',1,1,1,'2022-03-07 17:26:31','2022-04-20 10:14:16',4),
	(280,20,'页面用户中心banner保存',NULL,'platform:page:layout:user:banner:save',NULL,'A',1,1,0,'2022-03-31 18:55:02','2022-05-05 15:00:28',3),
	(281,288,'平台端短信发送记录',NULL,'platform:sms:recored',NULL,'A',1,1,0,'2022-03-31 19:17:38','2022-04-20 00:55:28',3),
	(282,0,'活动','s-data','','/activity','M',9988,1,0,'2022-03-31 19:34:18','2022-04-19 23:53:36',3),
	(283,282,'活动列表','','','/activity/list','C',10,1,0,'2022-03-31 19:35:21','2022-04-20 00:08:03',3),
	(284,0,'维护','s-open','','/maintain','M',9999,1,0,'2022-04-01 09:18:45','2022-04-19 23:36:33',4),
	(285,232,'物流设置','','','/maintain/logistics','C',10,1,1,'2022-04-01 09:23:05','2022-04-20 09:27:56',4),
	(286,284,'敏感操作日志','','merchant:log:sensitive:list','/maintain/sensitiveLog','C',10,1,0,'2022-04-01 09:23:52','2022-04-21 01:05:41',4),
	(287,0,'通知','message-solid','','','M',9987,1,0,'2022-04-01 17:08:47','2022-04-19 23:53:46',3),
	(288,287,'短信记录','','','/operation/messageRecord','C',10,1,0,'2022-04-01 17:10:30','2022-04-20 00:03:34',3),
	(289,315,'平台端资金流水分页列表',NULL,'platform:finance:monitor:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:56:04',3),
	(290,315,'平台端资金流水详情',NULL,'platform:finance:monitor:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:56:04',3),
	(291,185,'商户端资金流水分页列表',NULL,'merchant:finance:monitor:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:15',4),
	(292,239,'商户端商户基础信息',NULL,'merchant:base:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:04:43',4),
	(293,239,'商户端商户配置信息',NULL,'merchant:config:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:04:43',4),
	(294,186,'商户端商户转账信息',NULL,'merchant:transfer:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:29',4),
	(295,239,'商户端商户配置信息编辑',NULL,'merchant:config:info:edit',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:04:43',4),
	(296,186,'商户端商户转账信息编辑',NULL,'merchant:transfer:info:edit',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:29',4),
	(297,239,'商户端商户开关',NULL,'merchant:switch:update',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 10:04:43',4),
	(298,32,'图片上传','','platform:upload:image','','A',1,1,0,'2022-04-06 17:13:36','2022-04-20 00:01:33',3),
	(299,32,'文件上传','','platform:upload:file','','A',1,1,0,'2022-04-06 17:14:12','2022-04-20 00:01:35',3),
	(300,250,'素材列表','','merchant:attachment:list','','A',1,1,0,'2022-04-06 19:45:21','2022-04-20 09:55:00',4),
	(301,250,'素材删除','','merchant:attachment:delete','','A',1,1,0,'2022-04-06 19:45:45','2022-04-20 09:55:00',4),
	(302,250,'移动素材','','merchant:attachment:move','','A',1,1,0,'2022-04-06 19:46:08','2022-04-20 09:55:00',4),
	(303,186,'商户端获取转账申请基础信息',NULL,'merchant:finance:transfer:base:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:30',4),
	(304,186,'商户端转账申请',NULL,'merchant:finance:transfer:apply',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:30',4),
	(305,186,'商户端转账记录分页列表',NULL,'merchant:finance:transfer:record:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:30',4),
	(306,186,'商户端转账记录详情',NULL,'merchant:finance:transfer:record:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:52:30',4),
	(307,326,'平台端获取转账设置',NULL,'platform:finance:transfer:config',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:56:44',3),
	(308,326,'平台端编辑转账设置',NULL,'platform:finance:transfer:config:edit',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:56:44',3),
	(309,325,'平台端转账记录分页列表',NULL,'platform:finance:transfer:record:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:57:34',3),
	(310,325,'平台端转账记录详情',NULL,'platform:finance:transfer:record:info',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:57:34',3),
	(311,325,'平台端转账审核',NULL,'platform:finance:transfer:audit',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:57:34',3),
	(312,325,'平台端转账凭证',NULL,'platform:finance:transfer:proof',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:57:34',3),
	(313,325,'平台端转账备注',NULL,'platform:finance:transfer:remark',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:57:35',3),
	(314,266,'平台端用户分配标签',NULL,'platform:user:tag',NULL,'A',1,1,1,'2022-03-07 17:26:31','2022-04-21 22:06:19',3),
	(315,5,'资金流水','','','/financial/capitalFlow','C',10,1,0,'2022-04-07 16:44:24','2022-04-19 23:36:39',3),
	(316,0,'基础分类(素材/设置/文章)','','','','M',9999,0,1,'2022-04-07 16:44:28','2022-04-19 23:36:33',4),
	(317,351,'分类列表','','merchant:category:list','','A',1,1,0,'2022-04-07 16:45:03','2022-04-21 01:02:32',4),
	(318,351,'分类新增','','merchant:category:save','','A',1,1,0,'2022-04-07 16:45:26','2022-04-21 01:02:41',4),
	(319,351,'分类删除','','merchant:category:delete','','A',1,1,0,'2022-04-07 16:46:09','2022-04-21 01:03:00',4),
	(320,351,'修改分类','','merchant:category:update','','A',1,1,0,'2022-04-07 16:46:41','2022-04-21 01:02:47',4),
	(321,351,'分类详情','','merchant:category:info','','A',1,1,0,'2022-04-07 16:46:59','2022-04-21 01:02:54',4),
	(322,351,'分类tree','','merchant:category:list:tree','','A',1,1,0,'2022-04-07 16:47:19','2022-04-21 01:03:12',4),
	(323,351,'分类ids','','merchant:category:list:ids','','A',1,1,0,'2022-04-07 16:47:38','2022-04-21 01:03:07',4),
	(324,351,'分类状态修改','','merchant:category:update:status','','A',1,1,0,'2022-04-07 16:48:04','2022-04-21 01:03:18',4),
	(325,5,'转账记录','','','/financial/transferRecord','C',10,1,0,'2022-04-07 18:10:21','2022-04-19 23:36:39',3),
	(326,5,'转账设置','','','/financial/setting','C',10,1,0,'2022-04-07 18:12:20','2022-04-19 23:36:39',3),
	(327,240,'角色列表','','merchant:admin:role:list','','A',1,1,0,'2022-04-07 20:40:35','2022-04-21 01:03:43',4),
	(328,240,'创建角色','','merchant:admin:role:save','','A',1,1,0,'2022-04-07 20:41:06','2022-04-21 01:03:52',4),
	(329,240,'删除角色','','merchant:admin:role:delete','','A',1,1,0,'2022-04-07 20:41:33','2022-04-21 01:03:57',4),
	(330,240,'更新角色','','merchant:admin:role:update','','A',1,1,0,'2022-04-07 20:41:54','2022-04-21 01:04:03',4),
	(331,240,'角色详情','','merchant:admin:role:info','','A',1,1,0,'2022-04-07 20:42:15','2022-04-21 01:04:08',4),
	(332,240,'修改角色状态','','merchant:admin:role:update:status','','A',1,1,0,'2022-04-07 20:42:42','2022-04-21 01:04:14',4),
	(333,183,'商户端日帐单管理分页列表','','merchant:finance:daily:statement:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:53:04',4),
	(334,183,'商户端月帐单管理分页列表','','merchant:finance:month:statement:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 09:53:04',4),
	(335,5,'平台端日帐单管理分页列表','','platform:finance:daily:statement:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:59:29',3),
	(336,5,'平台端月帐单管理分页列表','','platform:finance:month:statement:page:list',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-20 00:59:29',3),
	(337,5,'账单管理','','','/financial/statement','C',10,1,0,'2022-04-08 19:25:19','2022-04-19 23:36:39',3),
	(338,346,'文件上传','','merchant:upload:file','','A',1,1,0,'2022-04-09 11:09:52','2022-04-21 01:04:21',4),
	(339,346,'图片上传','','merchant:upload:image','','A',1,1,0,'2022-04-09 11:10:34','2022-04-21 01:04:27',4),
	(340,0,'表单组件','','','','M',9999,0,1,'2022-04-12 10:58:22','2022-04-19 23:36:33',4),
	(341,346,'表单组件详情','','merchant:config:form:info','','A',1,1,0,'2022-04-12 10:58:48','2022-04-21 01:05:56',4),
	(342,87,'加载表单配置详情','','platform:config:info','','A',1,1,0,'2022-04-12 14:26:15','2022-04-20 00:11:54',3),
	(343,87,'加载表单配置详情','','platform:config:info','','A',1,1,0,'2022-04-12 14:26:16','2022-04-20 00:11:54',3),
	(344,188,'修改商品后台排序',NULL,'platform:product:update:rank',NULL,'A',1,1,0,'2022-04-13 16:22:43','2022-04-20 00:45:33',3),
	(345,163,'商户分页列表表头数量',NULL,'platform:merchant:list:header:num',NULL,'A',1,1,0,'2022-03-07 17:26:31','2022-04-22 17:02:48',3),
	(346,0,'公共','','','','M',8888,0,0,'2022-04-20 12:16:30','2022-04-21 00:59:32',4),
	(347,346,'获取公共配置1','','merchant:config:get','','A',0,1,0,'2022-04-20 12:17:05','2022-04-20 12:17:05',4),
	(348,346,'获取公共配置2','','merchant:config:getuniq','','A',0,1,0,'2022-04-20 12:17:31','2022-04-20 12:17:31',4),
	(349,48,'用户渠道数据','','platform:statistics:home:user:channel','','A',1,1,0,'2022-04-20 16:22:39','2022-04-20 16:22:39',3),
	(350,0,'登录管理','','','','M',8888,0,0,'2022-04-21 00:50:52','2022-04-21 00:59:17',4),
	(351,0,'基础分类(素材/设置/文章)','','','','M',8888,0,0,'2022-04-21 01:02:22','2022-04-21 01:05:01',4),
	(352,170,'首页数据','','merchant:statistics:home:index','','A',0,1,0,'2022-04-21 01:06:43','2022-04-21 01:06:43',4),
	(353,170,'经营数据','','merchant:statistics:home:operating:data','','A',0,1,0,'2022-04-21 01:07:06','2022-04-21 01:07:06',4),
	(354,4,'用户标签','','platform:user:tag:list','/user/label','C',10,1,0,'2022-04-21 22:03:11','2022-04-22 10:05:34',3),
	(355,354,'新增用户标签','','platform:user:tag:save','','A',1,1,0,'2022-04-21 22:04:01','2022-04-21 22:04:01',3),
	(356,354,'删除用户标签','','platform:user:tag:delete','','A',1,1,0,'2022-04-21 22:04:16','2022-05-14 17:59:25',3),
	(357,354,'修改用户标签','','platform:user:tag:update','','A',1,1,0,'2022-04-21 22:04:31','2022-04-21 22:04:31',3),
	(358,354,'用户标签全部列表','','platform:user:tag:all:list','','A',1,1,0,'2022-04-21 22:04:47','2022-04-21 22:04:47',3),
	(359,0,'test','','','/product/classify?id=1','C',0,1,1,'2022-04-22 10:56:39','2022-04-22 10:58:21',4),
	(360,6,'平台设置','','platform:config:info','/operation/setting','C',11,1,0,'2022-04-22 17:03:59','2022-04-22 17:04:22',3),
	(361,53,'修改登录用户信息','','platform:login:admin:update','','A',1,1,0,'2022-04-24 10:31:28','2022-04-24 10:31:53',3),
	(362,350,'修改登录用户信息','','merchant:login:admin:update','','A',1,1,0,'2022-04-24 10:37:32','2022-04-24 10:37:48',4),
	(363,247,'商品可用优惠券列表','','merchant:coupon:product:usable:list','','A',1,1,0,'2022-04-24 14:36:41','2022-04-24 14:36:41',4),
	(364,0,'优惠券','s-ticket','','/coupon','M',0,1,1,'2022-04-24 15:47:24','2022-04-24 15:47:24',4),
	(365,366,'管理员列表','','merchant:admin:list','/operation/roleManager/adminList','C',0,1,0,'2022-04-24 15:59:06','2022-05-05 10:12:09',4),
	(366,232,'管理权限','','','/operation/roleManager','C',0,1,0,'2022-04-24 16:01:28','2022-04-24 16:01:28',4),
	(367,166,'入驻协议','','','/merchant/type/accord','C',1,1,0,'2022-04-27 15:19:36','2022-05-09 15:19:05',3),
	(368,166,'店铺类型','','','/merchant/type/list','C',1,1,0,'2022-04-27 15:26:16','2022-04-27 15:28:01',3),
	(369,35,'物流公司','','platform:express:page:list','/maintain/logistics/companyList','C',0,1,0,'2022-05-05 10:30:21','2022-05-11 11:05:35',3),
	(370,20,'页面首页排行入口保存','','platform:page:layout:index:ranking:save','','A',1,1,0,'2022-05-05 15:01:21','2022-05-05 15:01:21',3),
	(371,188,'商品表头数量','','platform:product:tabs:headers','','A',1,1,0,'2022-05-06 15:06:26','2022-05-06 15:06:26',3),
	(372,3,'平台端订单物流详情','','platform:order:logistics:info','','A',0,1,0,'2022-05-07 15:40:33','2022-05-07 15:40:33',3),
	(373,7,'敏感操作日志','','','/maintain/sensitiveLog','C',0,1,0,'2022-05-09 15:34:50','2022-05-09 15:36:58',3),
	(374,365,'新增后台管理员','','merchant:admin:save','','A',0,1,0,'2022-05-11 15:30:05','2022-05-11 15:30:05',4),
	(375,365,'删除后台管理员','','merchant:admin:delete','','A',0,1,0,'2022-05-11 15:30:29','2022-05-11 15:30:29',4),
	(376,365,'修改后台管理员','','merchant:admin:update','','A',0,1,0,'2022-05-11 15:30:45','2022-05-11 15:30:45',4),
	(377,365,'后台管理员详情','','merchant:admin:info','','A',0,1,0,'2022-05-11 15:31:04','2022-05-11 15:31:04',4),
	(378,365,'修改后台管理员状态','','merchant:admin:update:status','','A',0,1,0,'2022-05-11 15:31:18','2022-05-11 15:31:18',4),
	(379,350,'商户端获取用户详情','','merchant:login:user:info','','A',0,1,0,'2022-05-16 15:54:43','2022-05-16 15:54:43',4),
	(380,6,'协议管理','','','/operation/agreement','C',1,1,0,'2022-06-13 10:10:52','2022-06-13 10:11:16',3),
	(381, 20, '底部导航获取', '', 'platform:page:layout:bottom:navigation', '', 'A', 1, 1, 0, '2023-08-08 12:04:03', '2023-08-08 12:04:03', 3),
	(382, 20, '底部导航保存', '', 'platform:page:layout:bottom:navigation:save', '', 'A', 1, 1, 0, '2023-08-08 12:04:26', '2023-08-08 12:04:26', 3),
	(383, 173, '平台端商品分类列表', '', 'merchant:product:category:cache:tree', '', 'A', 1, 1, 0, '2023-08-10 18:31:43', '2023-08-10 18:31:43', 4),
	(384, 368, '店铺类型分页列表', '', 'platform:merchant:type:list', '', 'A', 1, 1, 0, '2023-08-12 15:31:46', '2023-08-12 15:31:46', 3);

/*!40000 ALTER TABLE `eb_system_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_notification
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_notification`;

CREATE TABLE `eb_system_notification` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mark` varchar(50) NOT NULL DEFAULT '' COMMENT '标识',
  `type` varchar(50) NOT NULL DEFAULT '' COMMENT '通知类型',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '通知场景说明',
  `is_sms` tinyint(2) NOT NULL DEFAULT '0' COMMENT '发送短信（0：不存在，1：开启，2：关闭）',
  `sms_id` int(11) NOT NULL DEFAULT '0' COMMENT '短信模板id',
  `is_oversea_sms` tinyint(2) NOT NULL DEFAULT '0' COMMENT '发送海外短信（0：不存在，1：开启，2：关闭）',
  `oversea_sms_id` int(11) NOT NULL DEFAULT '0' COMMENT '海外短信模板id',
  `is_email` tinyint(2) NOT NULL DEFAULT '0' COMMENT '发送邮箱（0：不存在，1：开启，2：关闭）',
  `email_id` int(11) NOT NULL DEFAULT '0' COMMENT '邮箱模板id',
  `send_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '发送类型（1：用户，2：管理员）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mark` (`mark`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='通知设置表';

LOCK TABLES `eb_system_notification` WRITE;
/*!40000 ALTER TABLE `eb_system_notification` DISABLE KEYS */;

INSERT INTO `eb_system_notification` (`id`, `mark`, `type`, `description`, `is_sms`, `sms_id`, `is_oversea_sms`, `oversea_sms_id`, `is_email`, `email_id`, `send_type`, `create_time`)
VALUES
	(1,'PaySuccess','订单支付成功通知','订单支付成功通知',1,3,1,6,1,2,1,'2022-02-15 16:39:45'),
	(2,'DeliverGoods','订单发货通知','订单发货通知',1,2,1,5,1,3,1,'2022-02-15 16:43:41'),
	(3,'Captcha','通用验证码','公共验证码',1,1,1,4,1,1,1,'2022-02-15 16:45:16'),
	(4,'merchantAuditSuccess','商户审核成功通知','商户审核成功通知',0,0,0,0,1,4,1,'2022-04-20 14:56:56');

/*!40000 ALTER TABLE `eb_system_notification` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_role`;

CREATE TABLE `eb_system_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '身份管理id',
  `role_name` varchar(32) NOT NULL COMMENT '身份管理名称',
  `rules` text NOT NULL COMMENT '身份管理权限(menus_id)',
  `level` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '管理员类型：1= 平台超管, 2=商户超管, 3=系统管理员，4=商户管理员',
  `mer_id` int(11) NOT NULL DEFAULT '0' COMMENT '商户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='身份管理表';

LOCK TABLES `eb_system_role` WRITE;
/*!40000 ALTER TABLE `eb_system_role` DISABLE KEYS */;

INSERT INTO `eb_system_role` (`id`, `role_name`, `rules`, `level`, `status`, `create_time`, `update_time`, `type`, `mer_id`)
VALUES
	(1,'超级管理员','',0,1,'2020-04-18 11:19:25','2022-04-08 20:54:33',1,0),
	(2,'商户超级管理员','',0,1,'2022-03-04 10:03:54','2022-04-08 20:54:39',2,0),
	(4,'管理员','',0,1,'2022-05-11 09:32:21','2022-05-11 09:32:21',3,0);

/*!40000 ALTER TABLE `eb_system_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_system_role_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_system_role_menu`;

CREATE TABLE `eb_system_role_menu` (
  `rid` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`rid`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

LOCK TABLES `eb_system_role_menu` WRITE;
/*!40000 ALTER TABLE `eb_system_role_menu` DISABLE KEYS */;

INSERT INTO `eb_system_role_menu` (`rid`, `menu_id`)
VALUES
	(1,1),
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(1,7),
	(1,8),
	(1,9),
	(1,10),
	(1,13),
	(1,14),
	(1,15),
	(1,16),
	(1,17),
	(1,18),
	(1,19),
	(1,20),
	(1,24),
	(1,25),
	(1,26),
	(1,27),
	(1,28),
	(1,29),
	(1,30),
	(1,32),
	(1,33),
	(1,34),
	(1,35),
	(1,36),
	(1,37),
	(1,38),
	(1,39),
	(1,40),
	(1,41),
	(1,42),
	(1,43),
	(1,44),
	(1,45),
	(1,46),
	(1,47),
	(1,48),
	(1,52),
	(1,53),
	(1,54),
	(1,55),
	(1,62),
	(1,63),
	(1,64),
	(1,65),
	(1,66),
	(1,67),
	(1,68),
	(1,69),
	(1,70),
	(1,71),
	(1,72),
	(1,74),
	(1,75),
	(1,76),
	(1,77),
	(1,78),
	(1,79),
	(1,80),
	(1,81),
	(1,82),
	(1,83),
	(1,84),
	(1,85),
	(1,86),
	(1,87),
	(1,88),
	(1,89),
	(1,90),
	(1,91),
	(1,92),
	(1,93),
	(1,94),
	(1,95),
	(1,96),
	(1,97),
	(1,98),
	(1,99),
	(1,100),
	(1,101),
	(1,102),
	(1,103),
	(1,104),
	(1,105),
	(1,106),
	(1,107),
	(1,108),
	(1,110),
	(1,111),
	(1,112),
	(1,113),
	(1,114),
	(1,115),
	(1,116),
	(1,117),
	(1,118),
	(1,119),
	(1,120),
	(1,125),
	(1,126),
	(1,127),
	(1,128),
	(1,129),
	(1,130),
	(1,131),
	(1,132),
	(1,133),
	(1,134),
	(1,135),
	(1,136),
	(1,137),
	(1,138),
	(1,139),
	(1,140),
	(1,141),
	(1,142),
	(1,143),
	(1,144),
	(1,145),
	(1,146),
	(1,147),
	(1,148),
	(1,150),
	(1,151),
	(1,152),
	(1,153),
	(1,154),
	(1,155),
	(1,156),
	(1,157),
	(1,158),
	(1,159),
	(1,160),
	(1,161),
	(1,162),
	(1,163),
	(1,164),
	(1,165),
	(1,166),
	(1,167),
	(1,188),
	(1,189),
	(1,190),
	(1,191),
	(1,192),
	(1,220),
	(1,221),
	(1,222),
	(1,223),
	(1,229),
	(1,230),
	(1,231),
	(1,253),
	(1,254),
	(1,260),
	(1,261),
	(1,262),
	(1,263),
	(1,264),
	(1,265),
	(1,266),
	(1,267),
	(1,268),
	(1,269),
	(1,270),
	(1,271),
	(1,273),
	(1,277),
	(1,278),
	(1,280),
	(1,281),
	(1,282),
	(1,283),
	(1,287),
	(1,288),
	(1,289),
	(1,290),
	(1,298),
	(1,299),
	(1,307),
	(1,308),
	(1,309),
	(1,310),
	(1,311),
	(1,312),
	(1,313),
	(1,315),
	(1,325),
	(1,326),
	(1,335),
	(1,336),
	(1,337),
	(1,342),
	(1,343),
	(1,344),
	(1,345),
	(1,349),
	(1,354),
	(1,355),
	(1,356),
	(1,357),
	(1,358),
	(1,360),
	(1,361),
	(1,367),
	(1,368),
	(1,369),
	(1,370),
	(1,371),
	(1,372),
	(1,373),
	(1,380),
	(2,121),
	(2,122),
	(2,123),
	(2,124),
	(2,170),
	(2,171),
	(2,172),
	(2,173),
	(2,174),
	(2,175),
	(2,176),
	(2,178),
	(2,179),
	(2,180),
	(2,182),
	(2,183),
	(2,185),
	(2,186),
	(2,187),
	(2,193),
	(2,194),
	(2,195),
	(2,196),
	(2,197),
	(2,198),
	(2,199),
	(2,200),
	(2,201),
	(2,202),
	(2,203),
	(2,204),
	(2,205),
	(2,206),
	(2,207),
	(2,208),
	(2,209),
	(2,210),
	(2,211),
	(2,212),
	(2,213),
	(2,214),
	(2,215),
	(2,216),
	(2,217),
	(2,218),
	(2,219),
	(2,224),
	(2,225),
	(2,226),
	(2,227),
	(2,228),
	(2,232),
	(2,233),
	(2,234),
	(2,235),
	(2,236),
	(2,237),
	(2,238),
	(2,239),
	(2,240),
	(2,241),
	(2,242),
	(2,243),
	(2,244),
	(2,245),
	(2,246),
	(2,247),
	(2,248),
	(2,249),
	(2,250),
	(2,255),
	(2,256),
	(2,257),
	(2,258),
	(2,274),
	(2,275),
	(2,284),
	(2,286),
	(2,291),
	(2,292),
	(2,293),
	(2,294),
	(2,295),
	(2,296),
	(2,297),
	(2,300),
	(2,301),
	(2,302),
	(2,303),
	(2,304),
	(2,305),
	(2,306),
	(2,317),
	(2,318),
	(2,319),
	(2,320),
	(2,321),
	(2,322),
	(2,323),
	(2,324),
	(2,327),
	(2,328),
	(2,329),
	(2,330),
	(2,331),
	(2,332),
	(2,333),
	(2,334),
	(2,338),
	(2,339),
	(2,341),
	(2,346),
	(2,347),
	(2,348),
	(2,350),
	(2,351),
	(2,352),
	(2,353),
	(2,362),
	(2,363),
	(2,365),
	(2,366),
	(2,374),
	(2,375),
	(2,376),
	(2,377),
	(2,378),
	(2,379),
	(4,1),
	(4,2),
	(4,3),
	(4,4),
	(4,5),
	(4,6),
	(4,7),
	(4,8),
	(4,9),
	(4,10),
	(4,11),
	(4,14),
	(4,15),
	(4,16),
	(4,17),
	(4,18),
	(4,19),
	(4,20),
	(4,21),
	(4,22),
	(4,23),
	(4,24),
	(4,25),
	(4,26),
	(4,27),
	(4,28),
	(4,29),
	(4,30),
	(4,31),
	(4,32),
	(4,33),
	(4,34),
	(4,35),
	(4,36),
	(4,37),
	(4,38),
	(4,39),
	(4,40),
	(4,41),
	(4,42),
	(4,43),
	(4,44),
	(4,45),
	(4,46),
	(4,47),
	(4,48),
	(4,52),
	(4,53),
	(4,54),
	(4,55),
	(4,62),
	(4,63),
	(4,64),
	(4,65),
	(4,66),
	(4,67),
	(4,68),
	(4,69),
	(4,70),
	(4,71),
	(4,72),
	(4,73),
	(4,74),
	(4,75),
	(4,76),
	(4,77),
	(4,78),
	(4,79),
	(4,80),
	(4,81),
	(4,82),
	(4,83),
	(4,84),
	(4,85),
	(4,86),
	(4,87),
	(4,88),
	(4,89),
	(4,90),
	(4,91),
	(4,92),
	(4,93),
	(4,94),
	(4,95),
	(4,96),
	(4,97),
	(4,98),
	(4,99),
	(4,100),
	(4,101),
	(4,102),
	(4,103),
	(4,104),
	(4,105),
	(4,106),
	(4,107),
	(4,108),
	(4,110),
	(4,111),
	(4,112),
	(4,113),
	(4,114),
	(4,115),
	(4,116),
	(4,117),
	(4,118),
	(4,119),
	(4,120),
	(4,125),
	(4,126),
	(4,127),
	(4,128),
	(4,129),
	(4,130),
	(4,131),
	(4,132),
	(4,133),
	(4,134),
	(4,135),
	(4,136),
	(4,137),
	(4,138),
	(4,139),
	(4,140),
	(4,141),
	(4,142),
	(4,143),
	(4,144),
	(4,145),
	(4,146),
	(4,147),
	(4,148),
	(4,150),
	(4,151),
	(4,152),
	(4,153),
	(4,154),
	(4,155),
	(4,156),
	(4,157),
	(4,158),
	(4,159),
	(4,160),
	(4,161),
	(4,162),
	(4,163),
	(4,164),
	(4,165),
	(4,166),
	(4,167),
	(4,188),
	(4,189),
	(4,190),
	(4,191),
	(4,192),
	(4,220),
	(4,221),
	(4,222),
	(4,223),
	(4,229),
	(4,230),
	(4,231),
	(4,253),
	(4,254),
	(4,260),
	(4,261),
	(4,262),
	(4,263),
	(4,264),
	(4,265),
	(4,266),
	(4,267),
	(4,268),
	(4,269),
	(4,270),
	(4,271),
	(4,273),
	(4,276),
	(4,277),
	(4,278),
	(4,280),
	(4,281),
	(4,282),
	(4,283),
	(4,287),
	(4,288),
	(4,289),
	(4,290),
	(4,298),
	(4,299),
	(4,307),
	(4,308),
	(4,309),
	(4,310),
	(4,311),
	(4,312),
	(4,313),
	(4,315),
	(4,325),
	(4,326),
	(4,335),
	(4,336),
	(4,337),
	(4,342),
	(4,343),
	(4,344),
	(4,345),
	(4,349),
	(4,354),
	(4,355),
	(4,356),
	(4,357),
	(4,358),
	(4,360),
	(4,361),
	(4,367),
	(4,368),
	(4,369),
	(4,370),
	(4,371),
	(4,372),
	(4,373);

/*!40000 ALTER TABLE `eb_system_role_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_trading_day_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_trading_day_record`;

CREATE TABLE `eb_trading_day_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) DEFAULT NULL COMMENT '日期',
  `product_order_num` int(11) DEFAULT NULL COMMENT '订单数量',
  `product_order_pay_num` int(11) DEFAULT NULL COMMENT '订单支付数量',
  `product_order_pay_fee` decimal(8,2) DEFAULT NULL COMMENT '订单支付金额',
  `product_order_refund_num` int(11) DEFAULT NULL COMMENT '订单退款数量',
  `product_order_refund_fee` decimal(8,2) DEFAULT NULL COMMENT '订单退款金额',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商城交易日记录表';



# Dump of table eb_transfer_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_transfer_record`;

CREATE TABLE `eb_transfer_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '转账id',
  `mer_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商户id',
  `amount` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '金额',
  `transfer_type` varchar(20) NOT NULL DEFAULT 'bank' COMMENT '转账类型:bank-银行卡',
  `transfer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '转账姓名',
  `transfer_bank` varchar(50) NOT NULL DEFAULT '' COMMENT '转账银行',
  `transfer_bank_card` varchar(30) NOT NULL DEFAULT '' COMMENT '转账银行卡号',
  `transfer_status` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '转账状态：0-未到账，1-已到账',
  `transfer_proof` varchar(1000) NOT NULL DEFAULT '' COMMENT '转账凭证',
  `transfer_time` timestamp NULL DEFAULT NULL COMMENT '转账时间',
  `audit_status` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核，1-通过审核，2-审核失败',
  `refusal_reason` varchar(50) NOT NULL DEFAULT '' COMMENT '拒绝原因',
  `audit_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '审核员id',
  `audit_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '商户备注',
  `platform_mark` varchar(255) NOT NULL DEFAULT '' COMMENT '平台备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mer_id` (`mer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='转账记录表';



# Dump of table eb_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user`;

CREATE TABLE `eb_user` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(32) NOT NULL DEFAULT '' COMMENT '用户账号',
  `pwd` varchar(32) DEFAULT '' COMMENT '用户密码',
  `real_name` varchar(25) DEFAULT '' COMMENT '真实姓名',
  `birthday` varchar(32) DEFAULT '' COMMENT '生日',
  `card_id` varchar(20) DEFAULT '' COMMENT '身份证号码',
  `mark` varchar(255) DEFAULT '' COMMENT '用户备注',
  `partner_id` int(11) DEFAULT NULL COMMENT '合伙人id',
  `group_id` varchar(255) DEFAULT '' COMMENT '用户分组id',
  `tag_id` varchar(255) DEFAULT '' COMMENT '标签id',
  `nickname` varchar(255) DEFAULT '' COMMENT '用户昵称',
  `avatar` varchar(256) DEFAULT '' COMMENT '用户头像',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `add_ip` varchar(16) DEFAULT '' COMMENT '添加ip',
  `last_ip` varchar(16) DEFAULT '' COMMENT '最后一次登录ip',
  `now_money` decimal(16,2) unsigned DEFAULT '0.00' COMMENT '用户余额',
  `brokerage_price` decimal(8,2) DEFAULT '0.00' COMMENT '佣金金额',
  `integral` int(11) DEFAULT '0' COMMENT '用户剩余积分',
  `experience` int(11) unsigned DEFAULT '0' COMMENT '用户剩余经验',
  `sign_num` int(11) DEFAULT '0' COMMENT '连续签到天数',
  `status` tinyint(1) DEFAULT '1' COMMENT '1为正常，0为禁止',
  `level` tinyint(2) unsigned DEFAULT '0' COMMENT '等级',
  `spread_uid` int(10) unsigned DEFAULT '0' COMMENT '推广员id',
  `spread_time` timestamp NULL DEFAULT NULL COMMENT '推广员关联时间',
  `user_type` varchar(32) NOT NULL DEFAULT '' COMMENT '用户类型',
  `is_promoter` tinyint(1) unsigned DEFAULT '0' COMMENT '是否为推广员',
  `pay_count` int(11) unsigned DEFAULT '0' COMMENT '用户购买次数',
  `spread_count` int(11) DEFAULT '0' COMMENT '下级人数',
  `addres` varchar(255) DEFAULT '' COMMENT '详细地址',
  `adminid` int(11) unsigned DEFAULT '0' COMMENT '管理员编号 ',
  `login_type` varchar(36) NOT NULL DEFAULT '' COMMENT '用户登陆类型，h5,wechat,routine',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后一次登录时间',
  `clean_time` timestamp NULL DEFAULT NULL COMMENT '清除时间',
  `path` varchar(255) NOT NULL DEFAULT '/0/' COMMENT '推广等级记录',
  `subscribe` tinyint(3) DEFAULT '0' COMMENT '是否关注公众号',
  `subscribe_time` timestamp NULL DEFAULT NULL COMMENT '关注公众号时间',
  `sex` tinyint(1) DEFAULT '1' COMMENT '性别，0未知，1男，2女，3保密',
  `country` varchar(20) DEFAULT 'CN' COMMENT '国家，中国CN，其他OTHER',
  `promoter_time` timestamp NULL DEFAULT NULL COMMENT '成为分销员时间',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `identity` varchar(255) NOT NULL DEFAULT '' COMMENT '用户标识',
  `country_code` varchar(10) NOT NULL DEFAULT '' COMMENT '国标区号',
  PRIMARY KEY (`uid`) USING BTREE,
  KEY `spreaduid` (`spread_uid`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `is_promoter` (`is_promoter`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';

LOCK TABLES `eb_user` WRITE;
/*!40000 ALTER TABLE `eb_user` DISABLE KEYS */;

INSERT INTO `eb_user` (`uid`, `account`, `pwd`, `real_name`, `birthday`, `card_id`, `mark`, `partner_id`, `group_id`, `tag_id`, `nickname`, `avatar`, `phone`, `add_ip`, `last_ip`, `now_money`, `brokerage_price`, `integral`, `experience`, `sign_num`, `status`, `level`, `spread_uid`, `spread_time`, `user_type`, `is_promoter`, `pay_count`, `spread_count`, `addres`, `adminid`, `login_type`, `create_time`, `update_time`, `last_login_time`, `clean_time`, `path`, `subscribe`, `subscribe_time`, `sex`, `country`, `promoter_time`, `email`, `identity`, `country_code`)
VALUES
	(1,'18956003345','ADy07t/uufuQt6poCVJ8KQ==','','','','',NULL,'','','580505e9d219','crmebimage/public/operation/2022/02/11/5b18f64d5a634065a4ee0303ed9ed205a0vkffpwth.jpg','18956003345','','',0.00,0.00,0,0,0,1,0,0,NULL,'phone',0,0,0,'',0,'','2022-06-16 14:14:50','2022-06-16 14:14:51','2022-06-16 14:14:50',NULL,'/0/',0,NULL,0,'CN',NULL,'','crmeb2022061639307','+86'),
	(2,'15389137772','P6/RFcb+GvGpRL+MJlcBJg==','','','','',NULL,'','','fd0c7ae9a61e','crmebimage/public/product/2022/06/18/657314edfc124a75a2788f8dbc32c396xx77jduaq7.jpg','15389137772','','',0.00,0.00,0,0,0,1,0,0,NULL,'phone',0,0,0,'',0,'','2022-06-20 10:25:17','2022-06-20 10:25:17','2022-06-20 11:33:01',NULL,'/0/',0,NULL,0,'CN',NULL,'','crmeb2022062087625','+86'),
	(3,'18292417675','wb6Zns/zLStiQDCfAJHxhQ==','','','','',NULL,'','','大粽子','crmebimage/public/maintain/2022/06/20/bec07333a4e64925ae92a91daebb0ebbqo2fsyqmr6.png','18292417675','','',0.00,0.00,0,0,0,1,0,0,NULL,'phone',0,0,0,'',0,'','2022-06-20 15:42:47','2022-06-20 17:45:56','2022-06-20 17:49:19',NULL,'/0/',0,NULL,0,'CN',NULL,'','crmeb2022062040088','+86');

/*!40000 ALTER TABLE `eb_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_user_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_address`;

CREATE TABLE `eb_user_address` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户地址id',
  `uid` int(10) unsigned NOT NULL COMMENT '用户id',
  `real_name` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `phone` varchar(50) NOT NULL DEFAULT '' COMMENT '收货人电话',
  `province` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人所在省',
  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人所在市',
  `city_id` int(11) NOT NULL DEFAULT '0' COMMENT '城市id',
  `district` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人所在区',
  `detail` varchar(500) NOT NULL DEFAULT '' COMMENT '收货人详细地址',
  `post_code` int(10) NOT NULL DEFAULT '0' COMMENT '邮编',
  `longitude` varchar(16) NOT NULL DEFAULT '0' COMMENT '经度',
  `latitude` varchar(16) NOT NULL DEFAULT '0' COMMENT '纬度',
  `is_default` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认',
  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `country` varchar(20) NOT NULL DEFAULT '' COMMENT '国家',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `country_code` varchar(10) NOT NULL DEFAULT '' COMMENT '国标区号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `is_default` (`is_default`) USING BTREE,
  KEY `is_del` (`is_del`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户地址表';



# Dump of table eb_user_bill
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_bill`;

CREATE TABLE `eb_user_bill` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户账单id',
  `uid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户uid',
  `link_id` varchar(32) NOT NULL DEFAULT '0' COMMENT '关联id',
  `pm` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0 = 支出 1 = 获得',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '账单标题',
  `category` varchar(64) NOT NULL DEFAULT '' COMMENT '明细种类',
  `type` varchar(64) NOT NULL DEFAULT '' COMMENT '明细类型',
  `number` decimal(8,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '明细数字',
  `balance` decimal(16,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '剩余',
  `mark` varchar(512) NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 = 带确定 1 = 有效 -1 = 无效',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `openid` (`uid`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `add_time` (`create_time`) USING BTREE,
  KEY `pm` (`pm`) USING BTREE,
  KEY `type` (`category`,`type`,`link_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户账单表';



# Dump of table eb_user_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_group`;

CREATE TABLE `eb_user_group` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(64) DEFAULT NULL COMMENT '用户分组名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户分组表';

LOCK TABLES `eb_user_group` WRITE;
/*!40000 ALTER TABLE `eb_user_group` DISABLE KEYS */;

INSERT INTO `eb_user_group` (`id`, `group_name`)
VALUES
	(1,'初级会员'),
	(2,'中级会员'),
	(3,'高级会员');

/*!40000 ALTER TABLE `eb_user_group` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_user_merchant_collect
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_merchant_collect`;

CREATE TABLE `eb_user_merchant_collect` (
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `mer_id` int(11) NOT NULL COMMENT '商户ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uid`,`mer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户商户收藏表';



# Dump of table eb_user_tag
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_tag`;

CREATE TABLE `eb_user_tag` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL COMMENT '标签名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='标签管理';

LOCK TABLES `eb_user_tag` WRITE;
/*!40000 ALTER TABLE `eb_user_tag` DISABLE KEYS */;

INSERT INTO `eb_user_tag` (`id`, `name`)
VALUES
	(1,'中级'),
	(2,'高级'),
	(3,'黄金'),
	(4,'超级'),
	(5,'钻石');

/*!40000 ALTER TABLE `eb_user_tag` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table eb_user_visit_record
# ------------------------------------------------------------

DROP TABLE IF EXISTS `eb_user_visit_record`;

CREATE TABLE `eb_user_visit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) DEFAULT NULL COMMENT '日期',
  `uid` int(11) DEFAULT NULL COMMENT '用户uid',
  `visit_type` int(2) DEFAULT NULL COMMENT '访问类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户访问记录表';



# Dump of table QRTZ_BLOB_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;

CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_CALENDARS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;

CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_CRON_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;

CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_FIRED_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;

CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


# Dump of table QRTZ_JOB_DETAILS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;

CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_LOCKS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_LOCKS`;

CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;

INSERT INTO `QRTZ_LOCKS` (`SCHED_NAME`, `LOCK_NAME`)
VALUES
	('quartzScheduler','TRIGGER_ACCESS');

/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table QRTZ_PAUSED_TRIGGER_GRPS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_SCHEDULER_STATE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;

CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_SIMPLE_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_SIMPROP_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table QRTZ_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;

CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
