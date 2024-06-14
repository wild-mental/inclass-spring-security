package ac.su.inclassspringsecurity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "cart_product")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

//    private int offerPrice;
//
//    @ManyToOne
//    @JoinColumn(name = "coupon_id")
//    private Coupon coupon;


    @Override
    public String toString() {
        return "CartProduct{" +
            "id=" + id +
            ", cart=" + cart +
            ", product=" + product +
            ", quantity=" + quantity +
            '}';
    }
}
