package fer.hr.invsale.DAO;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

/**
 * This is a order review.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReview extends Review{

    @ManyToOne
    private Order order;

}
