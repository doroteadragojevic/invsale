package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
