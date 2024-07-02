package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.domain.Order;
import ac.su.inclassspringsecurity.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    @Transactional
    public Order getOrderWithUserFetched() {
        Long orderId = 14L;
        String jpqlJoinQuery =
            "SELECT o " +
            "FROM Order o " +
            "JOIN FETCH o.user " +  // Lazy 전략 설정된 User 객체를 Eager 전략 로드
            "WHERE o.id = :orderId";
        Order orderWithEagerLoadedUser =  // 변수명 명시적으로 사용
            entityManager.createQuery(jpqlJoinQuery, Order.class)
                         .setParameter("orderId", orderId)
                         .getSingleResult();
        return orderWithEagerLoadedUser;
    }
}
