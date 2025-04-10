package fer.hr.invsale.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * This class represents one order item.
 * Key elements are product, unit, order and quantity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Nullable
    private Order order;

}
