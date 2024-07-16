package ac.su.inclassspringsecurity.domain;

import ac.su.inclassspringsecurity.constant.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter @Setter
//@ToString(exclude = "cart")
@Table(name = "app_user")  // 테이블명은 예약어를 피하기 위해서 단순 user 로 하지 않기
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상세한 제어를 위한 @Column 어노테이션 사용
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private UserRole role;

    // @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cart> carts;

//    @OneToOne
//    @JoinColumn(name = "cart_id")
//    private Cart cart_id;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", Role=" + role +
//            (carts != null ? ", cart=" + carts : "") +
//            (orders != null ? ", order=" + orders : "") +
            '}';
    }
}