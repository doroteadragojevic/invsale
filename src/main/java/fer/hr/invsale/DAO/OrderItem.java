package fer.hr.invsale.DAO;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents one order item.
 * Key elements are product, unit, order and quantity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrderItem;
    @ManyToOne
    @NonNull
    private Product product;
    @ManyToOne
    @NonNull
    private Unit unit;
    @NonNull
    private Integer quantity;
    @ManyToOne
    @NonNull
    private Order order;

}
