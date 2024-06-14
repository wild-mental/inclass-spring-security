package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, QuerydslPredicateExecutor<Cart> {
    // user_id 기반으로 Cart 를 조회하는 메서드 선언
    Optional<Cart> findByUserId(Long userId);
}