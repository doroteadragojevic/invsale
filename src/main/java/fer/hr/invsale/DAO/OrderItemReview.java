package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
