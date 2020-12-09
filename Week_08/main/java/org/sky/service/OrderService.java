package org.sky.service;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.sky.bean.Order;
import org.sky.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public Order updateOrder() {
        Order order = orderRepository.findById(1L).get();
        order.setAddressId(1);
        return orderRepository.save(order);
    }
}
