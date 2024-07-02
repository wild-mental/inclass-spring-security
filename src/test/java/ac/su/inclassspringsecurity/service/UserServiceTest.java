package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.config.jwt.TokenProvider;
import ac.su.inclassspringsecurity.domain.AccessTokenDTO;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("publish token test")
    public void getAccessTokenTest() {
        userService.create(
            "tester01",
            "1234",
            "tester01@tt.cc"
        );
        Optional<User> optionalUser = userRepository.findByEmail("tester01@tt.cc");
        assert optionalUser.isPresent();
        User createdUser = optionalUser.get();
        createdUser.setPassword("1234");
        AccessTokenDTO accessToken = userService.getAccessToken(createdUser);
        assert accessToken != null;
    }

    @Test
    @DisplayName("User data with Order data test")
    void getUserWithFetchedOrders() {
        User user = userService.getUserWithFetchedOrders();
        System.out.println(user);
    }

    @Test
    @DisplayName("User list data with Order data test")
    void getUserListWithFetchedOrders() {
        List<User> users = userService.getUserListWithFetchedOrders();
        // ObjectMapper 사용해서 출력하기
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            // ObjectMapper 는 toString() 메소드를 사용하지 않고, writeValueAsString() 메소드를 사용한다.
            // 모든 필드를 접근 => Lazy Loading 에러 발생
            // 모든 필드 접근 전에 초기화 필요 or 원하는 필드 외에는 JsonIgnore 처리 필요
            String userListJson = objectMapper.writeValueAsString(users);
            System.out.println(userListJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("User list data with Order data test")
    void getUserWithJoinFetchedCartsOrders() {
        // 서비스 레이어에 의존
        // JPQL 방식으로 JOIN FETCH 수행
        // 여러 개의 연관 관계를 JOIN FETCH 로 가져오기
        // 중복 데이터가 발생 가능성 처리 필요 (DISTINCT)
        // 여러 개의 Collection (XxxToMany) 의 경우 모두 JOIN FETCH 처리 불가능
        // => 일부는 JOIN FETCH, 일부는 LAZY 로딩으로 처리
    }
}