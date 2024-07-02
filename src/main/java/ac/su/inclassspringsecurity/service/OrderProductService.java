package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.constant.OrderStatus;
import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.Order;
import ac.su.inclassspringsecurity.domain.OrderProduct;
import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.OrderProductRepository;
import ac.su.inclassspringsecurity.repository.OrderRepository;
import ac.su.inclassspringsecurity.repository.ProductRepository;
import ac.su.inclassspringsecurity.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderProductService {
    // User & Order & OrderProduct & Product 생성 및 테스트
    // 1) 리포지토리 주입
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // TransactionTemplate 을 사용하여 Transaction 을 직접 제어할 수 있음
    private final TransactionTemplate transactionTemplate;

    // 2) EntityManager 주입
    // EntityManager 를 사용하여 트랜젝션을 직접 제어할 수 있음
    // 현재 Bean 내에서 동일한 EntityManager 객체로 Transaction 을 제어하는 단위가 형성됨
    // => DB 접근을 Transaction 의 요건에 맞춰서 제어할 수 있는 단일 Context 로 작용
    // => ACID 요건 중 Isolation 요건을 갖추려면 이렇게 단일 Context 로 작용하는 EntityManager 가 필요
    @PersistenceContext
    private EntityManager entityManager;  // 현재 객체에 주입 되는 Repository 들과 Binding 되는 효과

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

    @Transactional  // 서비스 외부에서 호출 시 트랜젝션 적용 가능
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
        // String errorStr = null;
        // System.out.println(errorStr.length());
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

    // 인라인 트랜젝션 적용 코드 예제
    public void createDummyOrderProductWithInlineTransaction(){
        // save 가 이루어지는 부분을 트랜젝션으로 묶어서 처리
        transactionTemplate.execute(status -> {
            try {
                createDummyOrder();
                List<Product> createdProductList = createDummyProducts();
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

                for (Product product: createdProductList) {
                    // 직전 재고 수량 에서 주문 수량 차감 후 저장
                    product.setStockCount(product.getStockCount() - OrderAmountPerProduct.get(product));
                }
                // 트랜젝션 시작
                // 연관 관계 뿐 아니라, Atomic 하게 다루고 싶은 작업은 전부 트랜젝션으로 묶어서 처리
                // disk, cache 등등 어떤 대상이건 Atomic 하게 다룰 수 있게 된다.
                // Application 단에서의 Transaction 이 가지는 장점!
                orderProductRepository.saveAll(createdOrderProductList);
//                String errorStr = null;
//                System.out.println(errorStr.length());
                productRepository.saveAll(createdProductList);
                // 모든 연관 관계 Entity 저장 성공 시 트랜젝션 커밋
            } catch (Exception e) {
                status.setRollbackOnly();
                // 함께 롤백 되어야 하는 부가 로직들을 정의할 수가 있음
                // DB 뿐만 아니라, 다른 영역에서의 작업들을 함께 롤백 처리 가능
                throw e;
            }
            return null;
        });
    }
}
