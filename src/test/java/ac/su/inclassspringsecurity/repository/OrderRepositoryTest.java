package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.OrderStatus;
import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.Order;
import ac.su.inclassspringsecurity.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {
    // User & Order 생성 및 테스트
    // 1) 리포지토리 주입
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // 2) 더미 데이터 생성
    private String createDummyOrder() {
        // 테스트 회차를 반복하며 유저를 여러번 생성할 때,
        // Unique field 중복 유저가 쉽게 발생하지 않도록 생성
        // => UUID 사용 (랜덤 string 발생시키는 방향이면 뭐든지 좋음)
        String uuid10 = UUID.randomUUID().toString().substring(0, 10);
        // User 생성
        User user = new User();
        String username = "user_" + uuid10;
        user.setUsername(username);
        user.setEmail(uuid10 + "@tt.cc");
        user.setPassword("12341234");
        user.setRole(UserRole.USER);
        userRepository.save(user);
        // User 와 관련된 Order 생성
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(String.valueOf(LocalDateTime.now()));
        order.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        order.setStatus(OrderStatus.ORDERED);
        orderRepository.save(order);

        return username;
    }

    @Test
    void findByUserId() {
        // 여기에 테스트 구현
        // Given - When- Then 패턴으로 구현
        // Given
        String username = createDummyOrder();
        Optional<User> user = userRepository.findByUsername(username);
        assert user.isPresent();
        Long userId = user.get().getId();

        // When
        List<Order> orderList = orderRepository.findByUserId(userId);

        // Then
        assert !orderList.isEmpty();
        System.out.println(user.get());
        System.out.println(orderList);
    }

    @Test
    void findFirstByOrderByIdDesc() {
        // 여기에 테스트 구현
        // Given - When- Then 패턴 구현
        // Given
        createDummyOrder();

        // When
        Optional<Order> lastOrder = orderRepository.findFirstByOrderByIdDesc();

        // Then
        assert lastOrder.isPresent();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String lastOrderJSON = objectMapper.writeValueAsString(lastOrder.get());
            System.out.println(lastOrderJSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}