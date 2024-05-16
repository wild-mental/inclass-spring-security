package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    // 상품 쿼리 케이스 제어 (1) : 상품 이름에 따른 상품 조회
    List<Product> findByName(String name);
    // 구현부를 전혀 작성하지 않아도 JPA 에서 약속된 이름의 메서드를 선언하기만 하면 구체적인 로직은 자동 생성

    // 상품 쿼리 케이스 제어 (2) : 상품 상태에 따른 상품 리스트 조회
    List<Product> findByStatus(ProductStatusEnum status);

    // 상품 쿼리 케이스 제어 (3) : 상품 상태 리스트에 따른 상품 리스트 조회
    @Query("SELECT p FROM Product p WHERE p.status in :statusList")
    List<Product> findByStatusList(@Param("statusList") List<ProductStatusEnum> statusList);

    // 상품 쿼리 케이스 제어 (4) : DB 벤더에 따른 SQL 쿼리 작성 // "nativeQuery = true"
    @Query(value = "SELECT * FROM product WHERE status in :statusList", nativeQuery = true)
    List<Product> findByStatusListNative(@Param("statusList") List<ProductStatusEnum> statusList);
}