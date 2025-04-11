package fer.hr.invsale.DAO;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

/**
 * This is a review for a specific product.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemReview extends Review{
    @ManyToOne
    private OrderItem orderItem;
}
