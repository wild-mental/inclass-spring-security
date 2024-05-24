package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // All or Nothing -> 실패 시 발생하는 예외 처리를 위해 사용
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //
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
}
