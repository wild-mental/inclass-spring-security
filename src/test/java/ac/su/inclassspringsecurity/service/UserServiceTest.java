package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.config.jwt.TokenProvider;
import ac.su.inclassspringsecurity.domain.AccessTokenDTO;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
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
}