package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.Cart;
import ac.su.inclassspringsecurity.domain.CartProduct;
import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CartProductRepositoryTest {
    // 1) Repository 주입
    // User, Product, Cart, CartProduct Repository 주입
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    // 2) Dummy Data 생성
    void CreateDummyData() {
        // User, Product, Cart, CartProduct 데이터 생성
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("testEmail@tt.cc");
        user.setRole(UserRole.USER);
        user = userRepository.save(user);  // User 데이터 저장 후 ID 값이 생성됨

        // Product 데이터 생성
        Product product = new Product();
        product.setName("testProduct");
        product.setPrice(1000);
        product.setStockCount(10);
        product.setStatus(ProductStatusEnum.IN_STOCK);
        product.setCreatedAt(String.valueOf(LocalDateTime.now()));
        product.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        product = productRepository.save(product);  // Product 데이터 저장 후 ID 값이 생성됨

        // Cart 데이터 생성
        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);  // Cart 데이터 저장 후 ID 값이 생성됨

        // CartProduct 데이터 생성
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(1);
        cartProduct.setQuantity(10);
        cartProduct = cartProductRepository.save(cartProduct);  // CartProduct 데이터 저장 후 ID 값이 생성됨

        // 관심 대상 Entity 출력
        System.out.println(cartProduct);
    }

    void CreateMultipleDummyData() {
        // 여러 개의 엔티티를 생성하는 메서드
        // 모든 엔티티 객체 5개씩 (CartProduct은 25개)
        // User 데이터 5개 생성
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("testUser" + i);
            user.setPassword("testPassword" + i);
            user.setEmail("testEmail" + i + "@tt.cc");
            user.setRole(UserRole.USER);
            userList.add(user);
        }
        userRepository.saveAll(userList);  // User 데이터 저장

        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName("testProduct" + i);
            product.setPrice(1000 * (i + 1));
            product.setStockCount(10 * (i + 1));
            product.setStatus(ProductStatusEnum.IN_STOCK);
            product.setCreatedAt(String.valueOf(LocalDateTime.now()));
            product.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            productList.add(product);
        }
        productRepository.saveAll(productList);  // Product 데이터 저장

        List<Cart> cartList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Cart cart = new Cart();
            cart.setUser(userList.get(i));  // 위에 생성된 유저를 하나씩 할당
            cartList.add(cart);
        }
        cartRepository.saveAll(cartList);  // Cart 데이터 저장

        List<CartProduct> cartProductList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {  // 카트 5개 순회
            for (int j = 0; j < 5; j++) {  // 카트 하나당 product 5개 추가
                CartProduct cartProduct = new CartProduct();
                cartProduct.setCart(cartList.get(i));  // 위에 생성된 카트를 하나씩 할당
                cartProduct.setProduct(productList.get(j));  // 위에 생성된 상품을 하나씩 할당
                if (i == 2) { // 3번째 카트에 대해서만 수량을 10로 설정
                    cartProduct.setQuantity(10);
                } else {
                    cartProduct.setQuantity(1);
                }
                cartProductList.add(cartProduct);
            }
        }
        cartProductRepository.saveAll(cartProductList);  // CartProduct 데이터 저장

        // 관심 대상 Entity 출력
        System.out.println(cartProductList);
    }

    // 3) 데이터 조회 쿼리 호출
    // CartProduct 데이터를 cart_id 기준으로 조회
    @Test
    @DisplayName("카트 아이디로 카트 상품 조회")
    void findByCartId() {
        // Given (테스트 환경, 설정 등을 제공 : 테스트 사전 요건들)
        // CreateDummyData();
        CreateMultipleDummyData();  // 더미 데이터 를 생성

        // When (테스트 핵심 목표가 되는 메서드 호출)
        // cart_id 기준으로 CartProduct 데이터 조회
        List<CartProduct> cartProductList1 = cartProductRepository.findByCartId(1L);
        List<CartProduct> cartProductList3 = cartProductRepository.findByCartId(3L);

        // Then (테스트 결과 검증 및 확인)
        // 테스트 결과가 의도에 부합 하는지 검사
        assert !cartProductList1.isEmpty();
        assert !cartProductList3.isEmpty();
        // console 출력값 확인
        System.out.println(cartProductList1);  // 개행 및 파싱 들어가는 print 라이브러리 사용 추천
        System.out.println(cartProductList3);  // 개행 및 파싱 들어가는 print 라이브러리 사용 추천
    }

    @Test
    @DisplayName("상품 아이디로 카트 상품 조회")
    void findByProductId() {
        CreateDummyData();

        // cart_id 기준으로 CartProduct 데이터 조회
        List<CartProduct> cartProductList = cartProductRepository.findByProductId(1L);
        assert !cartProductList.isEmpty();

        // 4) console 출력값 확인
        System.out.println(cartProductList);  // 개행 및 파싱 들어가는 print 라이브러리 사용 추천
    }
}