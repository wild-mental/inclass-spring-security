package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.User;
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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    // PasswordEncoder 는 Interface (구현체 Bean 을 알아서 주입)
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("User Create Test")
    public void create() {
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword(
            passwordEncoder.encode("test")
        );
        newUser.setEmail("sample.sam.ple");
        newUser.setRole(UserRole.ADMIN);  // Enum default validation 필수 체크됨
        User savedUser = userRepository.save(newUser);
        System.out.println(savedUser);
    }

    @Test
    @DisplayName("유저네임 중복 검사 테스트")
    public void duplicateUsername() {
        create();
        boolean exists = userRepository.existsByUsername("test");
        System.out.println(exists);
        assert (exists);
        exists = userRepository.existsByUsername("test1");
        System.out.println(exists);
        assert (!exists);
    }

    @Test
    @DisplayName("email 필드 기준 유저 검색")
    public void findByEmail() {
        create();
        Optional<User> foundUser = userRepository.findByEmail("sample.sam.ple");
        assert foundUser.isPresent();
        System.out.println("유저 검색 성공!");

        foundUser = userRepository.findByEmail("sample.sam.ple2");
        assert foundUser.isEmpty();
        System.out.println("없는 유저 검색 결과 빈값 반환!");
    }

    @Test
    @DisplayName("이메일 중복 검사 테스트")
    public void duplicateEmail() {
        create();
        boolean exists = userRepository.existsByEmail("sample.sam.ple");
        System.out.println(exists);
        assert (exists);
        exists = userRepository.existsByEmail("sample.sam.ple1");
        System.out.println(exists);
        assert (!exists);
    }

    @Test
    public void validateDuplicateUser() {
        // 기존 유저 생성
        create();
        // 신규 유저 등록 검사
        User user = new User();
        user.setUsername("test");
        user.setPassword(
            passwordEncoder.encode("test")
        );
        user.setEmail("sample.sam.ple");
        user.setRole(UserRole.ADMIN);

        // username 중복 검사
        assert userRepository.existsByUsername(user.getUsername());
        // email 중복 검사
        assert userRepository.existsByEmail(user.getEmail());
    }
}