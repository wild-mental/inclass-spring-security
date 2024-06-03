package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.config.jwt.TokenProvider;
import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.AccessTokenDTO;
import ac.su.inclassspringsecurity.domain.SpringUser;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;

// userdetails 패키지에 Spring Security 가 제공하는 인증용 User 관련 클래스 모두 들어있음
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
//import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional  // All or Nothing -> 실패 시 발생하는 예외 처리를 위해 사용
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 로그인 전용 메서드 Override
    @Override
    public UserDetails loadUserByUsername(
        String email  // 로그인 ID 를 말함
    ) throws UsernameNotFoundException {
        Optional<User> registeredUser = userRepository.findByEmail(email);
        if (registeredUser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
//        User foundUser = registeredUser.get();
//        return new SpringUser( // 인증에 사용하기 위해 준비된 UserDetails 구현체
//            foundUser.getEmail(),
//            foundUser.getPassword(),
//            new ArrayList<>()
//        );
        return SpringUser.getSpringUserDetails(registeredUser.get());
    }

    // CRUD 기능 구현
    public void create(
        String username,
        String password1,
        String email
    ) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(
            passwordEncoder.encode(password1)
        );
        newUser.setEmail(email);
        newUser.setRole(UserRole.USER);  // Enum default validation 필수 체크됨

        // 중복 유저 체크
        validateDuplicateUser(newUser);
        User savedUser = userRepository.save(newUser);
    }

    // 중복 유저 검사 메서드 선언
    public void validateDuplicateUser(User user) {
        // username 중복 검사
        if (existsByUsername(user.getUsername())) {
            throw new IllegalStateException("이미 존재하는 username 입니다.");
        }
        // email 중복 검사
        if (existsByEmail(user.getEmail())) {
            throw new IllegalStateException("이미 존재하는 email 입니다.");
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String username) {
        return userRepository.existsByEmail(username);
    }

    public AccessTokenDTO getAccessToken(User user) {
        // 1) Spring Security 로그인 전용 메서드 loadUserByUsername 사용해 인증
        UserDetails userDetails;
        try {
            userDetails = loadUserByUsername(user.getEmail());
        } catch (Exception e) {
            return null;
        }
        // 2) UserService 에 TokenProvider 주입 -> Done
        // 3) TokenProvider 에서 Token String 을 생성
        //    - 비밀번호 체크
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            // 4) AccessTokenDTO 로 Wrapping 및 리턴
            String accessToken = tokenProvider.generateToken(user, Duration.ofHours(1L));
            String tokenType = "Bearer";
            return new AccessTokenDTO(
                accessToken, tokenType
            );
        }
        return null;
    }
}
