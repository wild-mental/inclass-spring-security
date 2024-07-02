package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    void getOrderWithUserFetched() {
        Order order = orderService.getOrderWithUserFetched();
        System.out.println(order);
    }
}