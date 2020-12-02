-- 创建订单表
CREATE TABLE `orders` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键流水号',
  `account_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `product_counts` bigint(20) NOT NULL COMMENT '商品数量',
  `order_amount` decimal(10,0) DEFAULT NULL COMMENT '交易商品总额',
  `ship_fee` decimal(10,0) DEFAULT NULL COMMENT '运费',
  `delivery_info` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流信息',
  `order_remark` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单备注',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '订单状态0未支付/1已支付/2已退款/3已删除',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_acc_status` (`account_id`,`pay_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin


-- 1.插入数据到订单表，关闭自动提交，一次性全部插入，86.656s
DROP PROCEDURE IF EXISTS insert_orders_data;
DELIMITER $
CREATE PROCEDURE insert_orders_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    set autocommit=false;
    WHILE i<=1000000 DO
            insert into myshop.orders (account_id, product_id, product_counts, order_amount, ship_fee, delivery_info, order_remark, pay_status, pay_time, refund_time, create_time, update_time)
            VALUES (CEILING(rand()*100), CEILING(rand()*100), 3, 30, 5, 'deliveryInfo',  'test_remark', 1, CURRENT_TIMESTAMP, null, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP);
        SET i = i+1;
    END WHILE;
    commit;
END $
CALL insert_orders_data();