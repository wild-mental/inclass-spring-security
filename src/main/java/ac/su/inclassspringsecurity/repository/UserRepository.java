package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
//    Optional<User> findByUsername(String username);

    // (1) 중복 유저 체크를 위한 기본 쿼리 (username, email) 준비
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
