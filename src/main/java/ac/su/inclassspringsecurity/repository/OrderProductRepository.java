package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>, QuerydslPredicateExecutor<OrderProduct> {
    // 주문 번호에 따른 주문 상품 리스트 조회
    List<OrderProduct> findByOrderId(Long orderId);

    // TBD (Product -> OrderProduct -> Order 까지 찾는 쿼리 추가)
}
