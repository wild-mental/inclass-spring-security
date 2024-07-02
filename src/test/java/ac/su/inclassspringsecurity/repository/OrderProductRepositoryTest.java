package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.OrderStatus;
import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.Order;
import ac.su.inclassspringsecurity.domain.OrderProduct;
import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// TestPropertySource 를 별도로 쓰지 않고 MySQL DB 에 직접 테스트 하며 데이터 확인
//@TestPropertySource(locations = "classpath:application-test.properties")
class OrderProductRepositoryTest {
    // User & Order & OrderProduct & Product 생성 및 테스트
    // 1) 리포지토리 주입
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

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

    public List<Product> createDummyProducts() {
        List<Product> productList = new ArrayList<>();
        // 상품 10 개의 더미 데이터 생성 후 저장
        for (int i = 1; i <= 5; i++) {
            Product product = new Product();
            product.setName("테스트 상품 " + i);
            product.setPrice(1000 * (int)(Math.random() * 10));
            product.setStockCount(100 * i);
            product.setStatus(ProductStatusEnum.IN_STOCK);
            product.setCreatedAt(String.valueOf(LocalDateTime.now()));
            product.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            product.setDescription("상세정보를 입력합니다");
            product.setMemo("메모 입력");
            product.setImage("/static/path/to/image");
            productList.add(product);
        }
        // 상품 리스트 저장 및 확인
        List<Product> savedProductList = productRepository.saveAll(productList);
        savedProductList.forEach(System.out::println);
        return savedProductList;
    }

    @Transactional  // -> 효력이 없음
    // 효력이 없는 이유 : 클래스 단위로 외부 클래스 를 호출할 때에, Proxy 패턴을 통해 적용 되기 때문
    // 클래스 간의 호출인 경우만 Transaction 의 시작과 종료를 제어 하게 된다.
    public void createDummyOrderProduct(){
        // 2) 더미 데이터 생성 : 총액 계산 및 상품 재고 차감 구현
        // 2-1) User & Order 생성 부분 OrderRepositoryTest 에서 복사
        createDummyOrder();
        // 2-2) Product 생성 부분 ProductRepositoryTest 에서 복사
        List<Product> createdProductList = createDummyProducts();
        // 2-3) OrderProduct 생성 -> 주문 수량 만큼 Product 재고 차감 & 총액 계산
        List<OrderProduct> createdOrderProductList = new ArrayList<>();
        Map<Product, Integer> OrderAmountPerProduct = new HashMap<>();
        for (Product product: createdProductList) {
            OrderProduct orderProduct = new OrderProduct();
            // 초기화 (상품을 랜덤 개수로 1 ~ 10개 담기!)
            Optional<Order> lastOrder = orderRepository.findFirstByOrderByIdDesc();
            assert lastOrder.isPresent();
            Order order = lastOrder.get();

            // 관련 객체 초기화
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);

            // 주문 수량 랜덤 설정
            int orderedAmount = (int)(Math.random() * 10) + 1;
            orderProduct.setQuantity(orderedAmount);  // 주문을 수행 -> 상품 엔티티 변경이 반드시 동반 되어야 함
            OrderAmountPerProduct.put(product, orderedAmount);
            // 가격 계산
            orderProduct.setTotalPrice(product.getPrice() * orderedAmount);

            // 기타 필수 필드 초기화
            orderProduct.setCreatedAt(String.valueOf(LocalDateTime.now()));
            orderProduct.setUpdatedAt(String.valueOf(LocalDateTime.now()));

            createdOrderProductList.add(orderProduct);
        }
        orderProductRepository.saveAll(createdOrderProductList);

        //      재고 차감 직전에 에러를 발생 시키기!
        String errorStr = null;
        System.out.println(errorStr.length());
        //       1) 아는 원인 => 데이터 validation 가능
        //         - 주문 수량이 재고 보다 많을 때 에러가 나는지 확인 (데이터 문제 Validation)
        //       2) 모르는 원인 => 데이터 validation 불가
        //         - 알 수 없는 에러가 나도 항상 대응할 수 있는 데이터 정합성 보장 필요
        //         - Transaction 적용 All or Nothing (Service Layer 에서 Transaction 적용)

        for (Product product: createdProductList) {
            // 직전 재고 수량 에서 주문 수량 차감 후 저장
            product.setStockCount(product.getStockCount() - OrderAmountPerProduct.get(product));
            productRepository.save(product);
        }
    }

    // 3) 테스트 메서드 생성
    @Test
    void findByOrderId() {
        // 여기에 테스트 구현
        // Given - When- Then 패턴으로 구현
        // Given : 테스트 데이터 생성
        createDummyOrderProduct();

        // When : 테스트 메서드 실행


        // Then : 테스트 결과 검증 및 확인


    }

}