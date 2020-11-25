DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `is_deleted` varchar(1) COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '是否删除标记',
  `acount_name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `nick_name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `password` varchar(125) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `email` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(11) COLLATE utf8mb4_bin NOT NULL COMMENT '手机号码',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '账户状态 0待审核/1正常/2异常封禁',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` bigint(20) NOT NULL COMMENT '创建者id',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次的修改时间',
  `modifier` bigint(20) NOT NULL COMMENT '修改者id',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin


DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `is_deleted` varchar(1) COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '是否删除标记',
  `product_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `product_image` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '产品图片地址',
  `price` decimal(10,0) NOT NULL COMMENT '商品单价',
  `counts` bigint(20) NOT NULL COMMENT '商品数量',
  `status` tinyint(2) DEFAULT '0' COMMENT '0:未上架1:已经上架',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` bigint(20) NOT NULL COMMENT '创建者id',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次的修改时间',
  `modifier` bigint(20) NOT NULL COMMENT '修改者id',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin


DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键流水号',
  `account_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `product_counts` bigint(20) NOT NULL COMMENT '商品数量',
  `orer_amount` decimal(10,0) DEFAULT NULL COMMENT '交易商品总额',
  `ship_fee` decimal(10,0) DEFAULT NULL COMMENT '运费',
  `delivery_info` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流信息',
  `orer_remark` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单备注',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '订单状态0未支付/1已支付/2已退款/3已删除',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_acc_status` (`account_id`,`pay_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin

DROP TABLE IF EXISTS `order_snapshot`;
CREATE TABLE `order_snapshot` (
  `snapshot_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `account_id` bigint(20) NOT NULL COMMENT '用户ID',
  `acount_name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `product_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `product_counts` bigint(20) NOT NULL COMMENT '商品数量',
  `product_price` decimal(10,0) NOT NULL COMMENT '商品单价',
  `orer_amount` decimal(10,0) DEFAULT NULL COMMENT '交易金额',
  `ship_fee` decimal(10,0) DEFAULT NULL COMMENT '运费',
  `delivery_info` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流信息',
  `orer_remark` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单备注',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '订单状态/1已完成/2已退款/3已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_time` datetime NOT NULL COMMENT '支付时间',
  `refund_time` datetime NOT NULL COMMENT '退款时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`snapshot_id`),
  KEY `idx_acc_status` (`account_id`,`pay_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin