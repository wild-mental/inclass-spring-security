package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long>,
    QuerydslPredicateExecutor<CartProduct>
{
    // 직접 작성해 보세요!
    // 1) cart_id 로 검색 쿼리 (user_id 기준으로 cart 검색 후 2차 접근 가능)
    List<CartProduct> findByCartId(Long cartId);

    List<CartProduct> findByProductId(Long productId);
}
