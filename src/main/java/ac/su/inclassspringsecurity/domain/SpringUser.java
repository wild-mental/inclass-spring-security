package ac.su.inclassspringsecurity.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SpringUser extends User {  // Wrapper 로 작용
    public SpringUser(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }

    public SpringUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }


    public static UserDetails getSpringUserDetails(
        ac.su.inclassspringsecurity.domain.User appUser)
    {
        return User.builder()
            // [중요] Email 필드를 username(로그인 ID) 로 삼아서 데이터 전달!
            .username(appUser.getEmail())
            .password(appUser.getPassword())
            .roles(appUser.getRole().toString())
            .build();
    }
}
