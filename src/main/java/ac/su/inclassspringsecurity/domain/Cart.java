package ac.su.inclassspringsecurity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        // referencedColumnName = "user_id",
        //  : 참조할 대상 엔티티의 컬럼명,
        //  : user_id 등 혼란을 주는 컬럼명이 있으면 변경 고려
        nullable = false
    )
    // name 은 현재 엔티티가 가진 컬럼명
    // referencedColumnName 참조할 대상 엔티티가 가진 컬럼명
    private User user;

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + id +
            ", user=" + user +
            '}';
    }
}