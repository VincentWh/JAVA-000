package org.sky.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sky.Application;
import org.sky.bean.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void findById() {
        Order order = orderRepository.findById(1L).get();
        log.info(String.valueOf(order.getOrderId()));
    }
}